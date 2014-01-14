/*
 * The MIT License
 *
 * Copyright 2013 Praqma.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.praqma.jenkins.pac;

import net.praqma.jenkins.pac.command.PACRunCommandDescriptor;
import net.praqma.jenkins.pac.command.PACRunCommand;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import net.praqma.jenkins.pac.exception.PacPathNotFoundException;
import net.praqma.jenkins.pac.exception.SettingsFileNotFoundException;
import net.praqma.jenkins.pac.exception.TailParameterNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author Praqma
 */
public class PacBuilder extends Builder {

    public final String settingsFile;
    public final PACRunCommand pac;
    public final String pathToPac;

    @DataBoundConstructor
    public PacBuilder(String settingsFile, PACRunCommand pac, String pathToPac) {
        this.settingsFile = StringUtils.isBlank(settingsFile) ? "default_settings.yml" : settingsFile;
        this.pac = pac;
        this.pathToPac = StringUtils.isBlank(pathToPac) ? "pac.rb" : pathToPac;
    }
    
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, PacPathNotFoundException, SettingsFileNotFoundException, TailParameterNotFoundException {

        PrintStream out = listener.getLogger();

        try {
            String generatedChangeLog = build.getWorkspace().act(new PacRemoteOperation(pathToPac, settingsFile, pac, listener));
            File buildRoot = new File(build.getRootDir(), generatedChangeLog);
            FilePath local = new FilePath(buildRoot);
            List<FilePath> files = build.getWorkspace().list(new PacChangelogFileFilter(generatedChangeLog));

            if (files.isEmpty()) {
                out.println("No changelog created, no commits in your selection.");
            } else {
                for (int i = 0; i < files.size(); i++) {
                    files.get(i).copyToWithPermission(local);
                    files.get(i).delete();
                }
                PacBuildAction action = new PacBuildAction(build, generatedChangeLog);
                build.addAction(action);
                
            }
        } catch (TailParameterNotFoundException tpnf) {
            tpnf.printToConsole(out);
            return false;
        } catch (PacPathNotFoundException ex) {
            ex.printToConsole(out);
            return false;
        } catch (SettingsFileNotFoundException ex2) {
            ex2.printToConsole(out);
            return false;
        } catch (IOException ex3) {
            ex3.printStackTrace(out);
            return false;
        }

        return true;
    }

    @Override
    public Action getProjectAction(final AbstractProject<?,?> project) {
        return new PacProjectAction(project);
    }
    
    @Extension
    public static class PacBuilderImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> proj) {
            return true;
        }

        public List<PACRunCommandDescriptor<?>> getDescriptors() {
            return PACRunCommand.getDescriptors();
        }

        @Override
        public String getDisplayName() {
            return "PAC Changelog Generator";
        }
     
    }
}

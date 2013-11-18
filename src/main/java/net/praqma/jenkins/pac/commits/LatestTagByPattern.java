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
package net.praqma.jenkins.pac.commits;

import hudson.Extension;
import hudson.model.Descriptor;
import java.io.File;
import java.io.Serializable;
import jenkins.model.Jenkins;
import net.praqma.jenkins.pac.command.PACRunCommand;
import net.praqma.jenkins.pac.command.PACRunCommandDescriptor;
import net.praqma.util.execute.CommandLine;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Current support only for Git
 *
 * @author Praqma
 */
public class LatestTagByPattern extends PACRunCommand implements Serializable {

    private String pattern;

    @DataBoundConstructor
    public LatestTagByPattern(String pattern) {
        this.pattern = pattern;
    }

    public LatestTagByPattern() {
    }

    @Override
    public String getTail() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Descriptor<PACRunCommand> getDescriptor() {
        return (Descriptor<PACRunCommand>) Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    @Override
    public String run(File workspace, String settingsFile, String pathToPac) {
        String latestCommand = String.format("ruby %s -t LATEST --settings=%s --outpath=%s --pattern=%s", pathToPac, settingsFile, workspace.getAbsolutePath(), "'" + getTail() + "'");
        CommandLine.getInstance().run(latestCommand, workspace);
        return latestCommand;
    }

    @Extension
    public static final class DescriptorImpl extends PACRunCommandDescriptor<LatestTagByPattern> {

        @Override
        public String getDisplayName() {
            return "Find by latest tag";
        }

        @Override
        public PACRunCommand newInstance(StaplerRequest req, JSONObject formData, PACRunCommand instance) throws Descriptor.FormException {
            LatestTagByPattern latestTag = (LatestTagByPattern) instance;
            save();
            return latestTag;
        }
    }
}

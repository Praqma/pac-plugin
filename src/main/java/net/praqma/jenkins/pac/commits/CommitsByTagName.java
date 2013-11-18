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

import net.praqma.jenkins.pac.command.PACRunCommandDescriptor;
import net.praqma.jenkins.pac.command.PACRunCommand;
import hudson.Extension;
import hudson.model.Descriptor;
import java.io.File;
import java.io.Serializable;
import jenkins.model.Jenkins;
import net.praqma.util.execute.CommandLine;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author Praqma
 */
public class CommitsByTagName extends PACRunCommand implements Serializable {

    private String tail;
    private String head;

    @DataBoundConstructor
    public CommitsByTagName(String tail, String head) {
        this.tail =  tail;
        this.head = head == null ? "" : head;
    }

    public CommitsByTagName() {
    }
    
    @Override
    public String getTail() {
        return tail;
    }

    public String getHead() {
        return head;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public Descriptor<PACRunCommand> getDescriptor() {
        return (Descriptor<PACRunCommand>) Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    @Override
    public String run(File workspace, String settingsFile, String pathToPac) {
        String command = String.format("ruby %s -t %s %s --settings=%s --outpath=%s", pathToPac, tail, head, settingsFile, workspace.getAbsolutePath());
        CommandLine.getInstance().run(command, workspace);
        return command;
    }

    

@Extension
public static final class DescriptorImpl extends PACRunCommandDescriptor<CommitsByTagName> {

        @Override
        public String getDisplayName() {
            return "Find by tag name";
        }

        @Override
        public PACRunCommand newInstance(StaplerRequest req, JSONObject formData, PACRunCommand instance) throws Descriptor.FormException {
            CommitsByTagName byTag = (CommitsByTagName) instance;
            save();
            return byTag;
        }
    }
}

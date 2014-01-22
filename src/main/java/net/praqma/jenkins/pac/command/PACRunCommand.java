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
package net.praqma.jenkins.pac.command;

import hudson.DescriptorExtensionList;
import hudson.EnvVars;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import net.praqma.jenkins.pac.exception.TailParameterNotFoundException;

/**
 *
 * @author Praqma
 */
public abstract class PACRunCommand implements Describable<PACRunCommand>, ExtensionPoint {
   
    public abstract String run(EnvVars vars, File workspace, String settingsFile, String pathToPac) throws TailParameterNotFoundException;
    public abstract String getCommand(File workspace, String settingsFile, String pathToPac, EnvVars vars);
    public abstract String getTail();
    protected static final Logger logger = Logger.getLogger(PACRunCommand.class.toString());

    public PACRunCommand() { }
    
    public static DescriptorExtensionList<PACRunCommand, PACRunCommandDescriptor<PACRunCommand>> all() {
        return Jenkins.getInstance().<PACRunCommand, PACRunCommandDescriptor<PACRunCommand>>getDescriptorList(PACRunCommand.class);
    }

    public static List<PACRunCommandDescriptor<?>> getDescriptors() {
        List<PACRunCommandDescriptor<?>> list = new ArrayList<PACRunCommandDescriptor<?>>();
        for (PACRunCommandDescriptor<?> d : all()) {
            list.add(d);
        }
        return list;
    }
}

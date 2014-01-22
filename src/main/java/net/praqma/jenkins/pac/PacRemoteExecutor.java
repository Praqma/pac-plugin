/*
 * The MIT License
 *
 * Copyright 2014 mads.
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

import com.esotericsoftware.yamlbeans.YamlReader;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import net.praqma.jenkins.pac.command.PACRunCommand;
import net.praqma.jenkins.pac.exception.PacPathNotFoundException;

/**
 *
 * @author mads
 */
public class PacRemoteExecutor implements FilePath.FileCallable<PacResult> {
    
    public final EnvVars env;
    public final BuildListener listener;
    public final String pathToPac;
    public final String settingsFilePath;
    public final PACRunCommand cmd;
    
    public PacRemoteExecutor(EnvVars env, BuildListener listener, String pathToPac, String settingsFilePath, PACRunCommand cmd) {
        this.env = env;
        this.listener = listener;
        this.pathToPac = pathToPac;
        this.cmd = cmd;
        this.settingsFilePath = settingsFilePath;
    }

    @Override
    public PacResult invoke(File file, VirtualChannel vc) throws IOException, InterruptedException {
        String resultingPathToPac = checkFile(file, env.expand(pathToPac)).getAbsolutePath();
        String resultingSettingsFilePath = checkFile(file, env.expand(settingsFilePath)).getAbsolutePath();
        
        YamlReader reader = new YamlReader(new FileReader(resultingSettingsFilePath));
        HashMap<?, ?> settings = (HashMap<?, ?>) reader.read();
        
        listener.getLogger().println(String.format("Executing command: %s", cmd.getCommand(file, resultingSettingsFilePath, resultingPathToPac, env)));
        cmd.run(env, file, resultingSettingsFilePath, resultingPathToPac);
        PacResult res = new PacResult(settings);
        return res;
    }
    
    private File checkFile(File workspace, String child) throws PacPathNotFoundException {
        File computed = new File(child);
        if (!computed.isAbsolute()) {
            computed = new File(workspace, child);
        }
        if (!computed.exists()) {
            throw new PacPathNotFoundException(computed);
        }

        return computed;
    }

}

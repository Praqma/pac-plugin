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

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import net.praqma.jenkins.pac.command.PACRunCommand;
import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import net.praqma.jenkins.pac.exception.PacPathNotFoundException;
import net.praqma.jenkins.pac.exception.SettingsFileNotFoundException;

/**
 * @author Praqma
 *
 */
public class PacRemoteOperation implements FilePath.FileCallable<String> {

    private String settingsFile;
    private PACRunCommand pac;
    private String pathToPac;

    public PacRemoteOperation() {
    }

    public PacRemoteOperation(String pathToPac, String settingsFile, PACRunCommand pac) {
        this.pathToPac = pathToPac;
        this.settingsFile = settingsFile;
        this.pac = pac;
    }

    public File absolutifyPacFile(File workspace) throws PacPathNotFoundException {
        File computed = new File(pathToPac);
        if (!computed.isAbsolute()) {
            computed = makeAbsolute(workspace, pathToPac);
        }
        if (!computed.exists()) {
            throw new PacPathNotFoundException(computed);
        }

        return computed;
    }

    public File absolutifySettingsFile(File workspace) throws SettingsFileNotFoundException {
        File computed = new File(settingsFile);
        if (!computed.isAbsolute()) {
            computed = makeAbsolute(workspace, settingsFile);
        }
        if (!computed.exists()) {
            throw new SettingsFileNotFoundException(computed);
        }

        return computed;
    }

    private File makeAbsolute(File workspace, String target) {
        File absolute = new File(workspace, target);
        return absolute;
    }

    @Override
    public String invoke(File f, VirtualChannel channel) throws PacPathNotFoundException, SettingsFileNotFoundException, InterruptedException, FileNotFoundException, YamlException {

        File path = absolutifyPacFile(f);
        File settings = absolutifySettingsFile(f);
        
        getPac().run(f, settings.getAbsolutePath(), path.getAbsolutePath());

        YamlReader reader = new YamlReader(new FileReader(settings.getAbsolutePath()));
        HashMap<?, ?> object = (HashMap<?, ?>) reader.read();
        String changeLogName = (String) ((HashMap<?, ?>) object.get(":general")).get("changelog_name");
        return changeLogName;
    }

    /**
     * @return the settingsFile
     */
    public String getSettingsFile() {
        return settingsFile;
    }

    /**
     * @param settingsFile the settingsFile to set
     */
    public void setSettingsFile(String settingsFile) {
        this.settingsFile = settingsFile;
    }

    /**
     * @return the pac
     */
    public PACRunCommand getPac() {
        return pac;
    }

    /**
     * @param pac the pac to set
     */
    public void setPac(PACRunCommand pac) {
        this.pac = pac;
    }

    /**
     * @return the pathToPac
     */
    public String getPathToPac() {
        return pathToPac;
    }

    /**
     * @param pathToPac the pathToPac to set
     */
    public void setPathToPac(String pathToPac) {
        this.pathToPac = pathToPac;
    }
}

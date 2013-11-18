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

import hudson.model.AbstractBuild;
import hudson.model.Action;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author Praqma
 */
public class PacBuildAction implements Action {

    public String name;
    public String root;

    @DataBoundConstructor
    public PacBuildAction(AbstractBuild<?, ?> build, String name) {
        this.name = name;
        this.root = new File(build.getRootDir(), name).getAbsolutePath();
    }

    public PacBuildAction() {
    }

    @Override
    public String getIconFileName() {
        return "/plugin/PAC-plugin/images/64x64/pac-logo.png";
    }

    @Override
    public String getDisplayName() {
        return "PAC Publisher";
    }

    @Override
    public String getUrlName() {
        return "pac-publisher";
    }

    @Override
    public String toString() {
        String fileString = null;
        try {
            fileString = readFile();
        } catch (IOException ex) {
            ex.getMessage();
        }
        return fileString;
    }


    public String readFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(root));

        try {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } finally {
            bufferedReader.close();
        }
    }
}

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
package net.praqma.jenkins.pac.integration;

import java.io.File;
import net.praqma.jenkins.pac.exception.PacPathNotFoundException;
import net.praqma.jenkins.pac.exception.SettingsFileNotFoundException;
import net.praqma.jenkins.pac.PacRemoteOperation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Praqma
 */
public class PacRemoteOperationTest {

    public PacRemoteOperationTest() {
    }
    
    @Test (expected = SettingsFileNotFoundException.class)
    public void testAbsolutifySettingsFileNotFound() throws SettingsFileNotFoundException {
        PacRemoteOperation pro = new PacRemoteOperation(null, "unexisting.yml", null, null);
        File settingsFile = new File(PacRemoteOperationTest.class.getResource("").getFile());
        pro.absolutifySettingsFile(settingsFile);
    }
    
    @Test (expected = PacPathNotFoundException.class)
    public void testAbsolutifyPacFileNotFound() throws PacPathNotFoundException{
        PacRemoteOperation pro = new PacRemoteOperation("unexisting.rb", null, null, null);
        File pacFile = new File(PacRemoteOperationTest.class.getResource("").getFile());
        pro.absolutifyPacFile(pacFile);
    }
     
    @Test
    public void testMakeAbsolute(){
        File workspace = new File(PacRemoteOperationTest.class.getResource("").getFile());
        String target = "pac.rb";
        File abs = new File(workspace, target);
        assertTrue(abs.isAbsolute());      
    }
    
}

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

import hudson.model.AbstractProject;
import hudson.model.Actionable;
import hudson.model.ProminentProjectAction;

/**
 *
 * @author mads
 */
public class PacProjectAction extends Actionable implements ProminentProjectAction {
  
    public final AbstractProject<?,?> project;
    
    public PacProjectAction(final AbstractProject<?,?> project) {
        this.project = project;
    }

    @Override
    public String getIconFileName() {
        return PacBuilder.LOGO_LOCATION;
    }

    @Override
    public String getDisplayName() {
        return "Latest PAC";
    }

    /**
     * Link to the latest changelog generated
     * @return 
     */
    @Override
    public String getUrlName() {
        if(getLatestPacBuildAction() == null) {
            return PacBuildAction.PAC_CHANGELOG_URL_NAME;
        } else {
            return getLatestPacBuildAction().build.number+ "/"+PacBuildAction.PAC_CHANGELOG_URL_NAME;
        }
    }

    @Override
    public String getSearchUrl() {
        return "pac-search";
    }
    
    public PacBuildAction getLatestPacBuildAction() {
        if(project.getLastSuccessfulBuild() == null) {
            return null;
        } else {
            return project.getLastSuccessfulBuild().getAction(PacBuildAction.class);
        }
         
    }
    
}

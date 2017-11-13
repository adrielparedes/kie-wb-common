/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.workbench.common.screens.library.client.screens;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.common.client.dom.Input;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.library.client.resources.i18n.LibraryConstants;

@Templated
public class PopulatedLibraryView implements PopulatedLibraryScreen.View,
                                             IsElement {

    private PopulatedLibraryScreen presenter;

    @Inject
    private ProjectsDetailScreen projectsDetailScreen;

    @Inject
    private TranslationService ts;

    @Inject
    @DataField("project-list")
    Div projectList;

    @Inject
    @DataField("filter-text")
    Input filterText;

    @Inject
    @DataField("add-project")
    Button addProject;

    @Override
    public void init(final PopulatedLibraryScreen presenter) {
        this.presenter = presenter;
        filterText.setAttribute("placeholder",
                                ts.getTranslation(LibraryConstants.Search));
        addProject.setHidden(!presenter.userCanCreateProjects());
    }

    @Override
    public void clearProjects() {
        projectList.setTextContent("");
    }

    @Override
    public void addProject(final HTMLElement project) {
        projectList.appendChild(project);
    }

    @Override
    public void clearFilterText() {
        this.filterText.setValue("");
    }

    @Override
    public String getNumberOfAssetsMessage(int numberOfAssets) {
        return ts.format(LibraryConstants.NumberOfAssets,
                         numberOfAssets);
    }

    @EventHandler("filter-text")
    public void filterTextChange(final KeyUpEvent event) {
        presenter.filterProjects(filterText.getValue());
    }

    @EventHandler("add-project")
    public void addProject(final ClickEvent event) {
        presenter.addProject();
    }
}
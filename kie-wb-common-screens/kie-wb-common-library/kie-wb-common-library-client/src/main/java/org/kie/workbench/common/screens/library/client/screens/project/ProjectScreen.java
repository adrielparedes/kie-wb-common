/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.screens.library.client.screens.project;

import javax.inject.Inject;

import elemental2.dom.HTMLElement;
import org.kie.workbench.common.screens.library.client.perspective.LibraryPerspective;
import org.kie.workbench.common.screens.library.client.screens.assets.EmptyAssetsScreen;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElemental;

@WorkbenchScreen(identifier = LibraryPlaces.PROJECT_SCREEN,
        owningPerspective = LibraryPerspective.class)
public class ProjectScreen {

    private final LibraryPlaces libraryPlaces;
    private EmptyAssetsScreen emptyAssetsScreen;

    public interface View extends UberElemental<ProjectScreen> {

        void setAssetsCount(int count);

        void setContributorsCount(int count);

        void setContent(HTMLElement content);
    }

    private ProjectScreen.View view;

    @Inject
    public ProjectScreen(final View view,
                         final LibraryPlaces libraryPlaces,
                         final EmptyAssetsScreen emptyAssetsScreen) {
        this.view = view;
        this.libraryPlaces = libraryPlaces;
        this.emptyAssetsScreen = emptyAssetsScreen;
    }

    public void showAssets() {
        this.view.setContent(this.emptyAssetsScreen.getView().getElement());
    }

    public void showMetrics() {

    }

    public void showContributors() {

    }

    public void showSettings() {

    }

    @WorkbenchPartTitle
    public String getTitle() {
        return this.libraryPlaces.getProjectInfo().getProject().getProjectName();
    }

    @WorkbenchPartView
    public ProjectScreen.View getView() {
        return view;
    }
}

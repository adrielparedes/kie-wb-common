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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.jboss.errai.common.client.api.Caller;
import org.kie.workbench.common.screens.library.api.LibraryService;
import org.kie.workbench.common.screens.library.client.perspective.LibraryPerspective;
import org.kie.workbench.common.screens.library.client.screens.assets.EmptyAssetsScreen;
import org.kie.workbench.common.screens.library.client.screens.organizationalunit.contributors.tab.ContributorsListPresenter;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElemental;

@WorkbenchScreen(identifier = LibraryPlaces.PROJECT_SCREEN,
        owningPerspective = LibraryPerspective.class)
public class ProjectScreen {

    public interface View extends UberElemental<ProjectScreen> {

        void setAssetsCount(int count);

        void setContributorsCount(int count);

        void setContent(HTMLElement content);

        void setTitle(String projectName);
    }

    private final LibraryPlaces libraryPlaces;
    private EmptyAssetsScreen emptyAssetsScreen;
    private ContributorsListPresenter contributorsListScreen;
    private ProjectMetricsScreen projectMetricsScreen;
    private Caller<LibraryService> libraryService;
    private ProjectScreen.View view;

    @Inject
    public ProjectScreen(final View view,
                         final LibraryPlaces libraryPlaces,
                         final EmptyAssetsScreen emptyAssetsScreen,
                         final ContributorsListPresenter contributorsListScreen,
                         final ProjectMetricsScreen projectMetricsScreen,
                         final Caller<LibraryService> libraryService) {
        this.view = view;
        this.libraryPlaces = libraryPlaces;
        this.emptyAssetsScreen = emptyAssetsScreen;
        this.contributorsListScreen = contributorsListScreen;
        this.projectMetricsScreen = projectMetricsScreen;
        this.libraryService = libraryService;
    }

    @PostConstruct
    public void initialize() {
        this.view.init(this);
        this.view.setTitle(libraryPlaces.getProjectInfo().getProject().getProjectName());
        this.view.setAssetsCount(libraryPlaces.getProjectInfo().getProject().getNumberOfAssets());
        this.view.setContributorsCount(this.contributorsListScreen.getContributorsCount());
        this.showAssets();
    }

    public void showAssets() {

//        libraryService.call((RemoteCallback<List<AssetInfo>>) assetsList -> {
//
//            assets = assetsList;
//
//            setupAssets(assets);
//
//            busyIndicatorView.hideBusyIndicator();
//
//            isProjectLoadInProgress = false;
//
//            if (isProjectLoadPending) {
//                isProjectLoadPending = false;
//                loadProjectInfo();
//            } else {
//                reloader.check(assetsList);
//            }
//        }).getProjectAssets(new ProjectAssetsQuery(projectInfo.getProject(),
//                                                   view.getFilterValue(),
//                                                   view.getFirstIndex(),
//                                                   view.getStep()));
        this.view.setContent(this.emptyAssetsScreen.getView().getElement());
    }

    public void showMetrics() {
        this.view.setContent(Js.cast(this.projectMetricsScreen.getView().getElement()));
    }

    public void showContributors() {
        this.view.setContent(Js.cast(this.contributorsListScreen.getView().getElement()));
    }

    public void showSettings() {
        this.view.setContent(new HTMLElement());
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

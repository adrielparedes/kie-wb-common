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
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.core.client.Callback;
import elemental2.dom.HTMLElement;
import org.guvnor.common.services.project.client.security.ProjectController;
import org.guvnor.structure.client.security.OrganizationalUnitController;
import org.guvnor.structure.events.AfterEditOrganizationalUnitEvent;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.dom.elemental2.Elemental2DomUtil;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.screens.defaulteditor.client.editor.NewFileUploader;
import org.kie.workbench.common.screens.library.api.LibraryService;
import org.kie.workbench.common.screens.library.api.ProjectInfo;
import org.kie.workbench.common.screens.library.client.perspective.LibraryPerspective;
import org.kie.workbench.common.screens.library.client.screens.assets.AssetsScreen;
import org.kie.workbench.common.screens.library.client.screens.assets.EmptyAssetsScreen;
import org.kie.workbench.common.screens.library.client.screens.assets.events.UpdatedAssetsEvent;
import org.kie.workbench.common.screens.library.client.screens.organizationalunit.contributors.edit.EditContributorsPopUpPresenter;
import org.kie.workbench.common.screens.library.client.screens.organizationalunit.contributors.tab.ContributorsListPresenter;
import org.kie.workbench.common.screens.library.client.screens.project.delete.DeleteProjectPopUpScreen;
import org.kie.workbench.common.screens.library.client.screens.project.rename.RenameProjectPopUpScreen;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.kie.workbench.common.screens.projecteditor.client.build.BuildExecutor;
import org.kie.workbench.common.widgets.client.handlers.NewResourcePresenter;
import org.kie.workbench.common.widgets.client.handlers.NewResourceSuccessEvent;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElemental;

@WorkbenchScreen(identifier = LibraryPlaces.PROJECT_SCREEN,
        owningPerspective = LibraryPerspective.class)
public class ProjectScreen {

    private Elemental2DomUtil elemental2DomUtil;
    protected ProjectInfo projectInfo;

    public interface View extends UberElemental<ProjectScreen>,
                                  BuildExecutor.View {

        void setAssetsCount(int count);

        void setContributorsCount(int count);

        void setContent(HTMLElement content);

        void setTitle(String projectName);

        void setEditContributorsVisible(boolean visible);

        void setAddAssetVisible(boolean visible);

        void setImportAssetVisible(boolean visible);

        void setBuildEnabled(boolean enabled);

        void setDeployEnabled(boolean enabled);

        void setDeleteProjectVisible(boolean visible);
    }

    private final LibraryPlaces libraryPlaces;

    private EmptyAssetsScreen emptyAssetsScreen;
    private AssetsScreen assetsScreen;
    private ContributorsListPresenter contributorsListScreen;
    private ProjectMetricsScreen projectMetricsScreen;
    private ProjectController projectController;
    private OrganizationalUnitController organizationalUnitController;
    private final NewFileUploader newFileUploader;
    private final NewResourcePresenter newResourcePresenter;
    private BuildExecutor buildExecutor;
    private ManagedInstance<EditContributorsPopUpPresenter> editContributorsPopUpPresenter;
    private ManagedInstance<DeleteProjectPopUpScreen> deleteProjectPopUpScreen;
    private ManagedInstance<RenameProjectPopUpScreen> renameProjectPopUpScreen;
    private Caller<LibraryService> libraryService;
    private ProjectScreen.View view;

    @Inject
    public ProjectScreen(final View view,
                         final LibraryPlaces libraryPlaces,
                         final EmptyAssetsScreen emptyAssetsScreen,
                         final AssetsScreen assetsScreen,
                         final ContributorsListPresenter contributorsListScreen,
                         final ProjectMetricsScreen projectMetricsScreen,
                         final ProjectController projectController,
                         final OrganizationalUnitController organizationalUnitController,
                         final NewFileUploader newFileUploader,
                         final NewResourcePresenter newResourcePresenter,
                         final BuildExecutor buildExecutor,
                         final ManagedInstance<EditContributorsPopUpPresenter> editContributorsPopUpPresenter,
                         final ManagedInstance<DeleteProjectPopUpScreen> deleteProjectPopUpScreen,
                         final ManagedInstance<RenameProjectPopUpScreen> renameProjectPopUpScreen,
                         final Caller<LibraryService> libraryService) {
        this.view = view;
        this.libraryPlaces = libraryPlaces;
        this.emptyAssetsScreen = emptyAssetsScreen;
        this.assetsScreen = assetsScreen;
        this.contributorsListScreen = contributorsListScreen;
        this.projectMetricsScreen = projectMetricsScreen;
        this.projectController = projectController;
        this.organizationalUnitController = organizationalUnitController;
        this.newFileUploader = newFileUploader;
        this.newResourcePresenter = newResourcePresenter;
        this.buildExecutor = buildExecutor;
        this.editContributorsPopUpPresenter = editContributorsPopUpPresenter;
        this.deleteProjectPopUpScreen = deleteProjectPopUpScreen;
        this.renameProjectPopUpScreen = renameProjectPopUpScreen;
        this.libraryService = libraryService;
        this.elemental2DomUtil = new Elemental2DomUtil();
    }

    @PostConstruct
    public void initialize() {
        this.projectInfo = this.libraryPlaces.getProjectInfo();
        this.view.init(this);
        this.buildExecutor.init(this.view);
        this.view.setTitle(libraryPlaces.getProjectInfo().getProject().getProjectName());
        this.resolveContributorsCount();
        this.resolveAssetsCount();
        this.showAssets();

        this.view.setEditContributorsVisible(this.canEditContributors());
        this.view.setAddAssetVisible(this.userCanUpdateProject());
        this.view.setImportAssetVisible(this.userCanUpdateProject());
        this.view.setImportAssetVisible(this.userCanUpdateProject());
        this.view.setBuildEnabled(this.userCanBuildProject());
        this.view.setDeployEnabled(this.userCanBuildProject());
        this.view.setDeleteProjectVisible(this.userCanDeleteRepository());

        newFileUploader.acceptContext(new Callback<Boolean, Void>() {
            @Override
            public void onFailure(Void reason) {
                view.setImportAssetVisible(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                view.setImportAssetVisible(result);
            }
        });
    }

    public void setAssetsCount(Integer assetsCount) {
        this.view.setAssetsCount(assetsCount);
    }

    public void onAddAsset(@Observes NewResourceSuccessEvent event) {
        resolveAssetsCount();
    }

    public void onAssetsUpdated(@Observes UpdatedAssetsEvent event) {
        resolveAssetsCount();
    }

    public void onContriburorsUpdated(@Observes AfterEditOrganizationalUnitEvent event) {
        resolveContributorsCount();
    }

    private void resolveContributorsCount() {
        this.view.setContributorsCount(this.contributorsListScreen.getContributorsCount());
    }

    private void resolveAssetsCount() {
        this.libraryService.call((Integer numberOfAssets) -> this.setAssetsCount(numberOfAssets))
                .getNumberOfAssets(this.projectInfo.getProject());
    }

    public void showAssets() {
        this.view.setContent(this.assetsScreen.getView().getElement());
    }

    public void showMetrics() {
        this.projectMetricsScreen.onStartup(projectInfo);
        this.view.setContent(elemental2DomUtil.asHTMLElement(this.projectMetricsScreen.getView().getElement()));
    }

    public void showContributors() {
        this.view.setContent(elemental2DomUtil.asHTMLElement(this.contributorsListScreen.getView().getElement()));
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return this.libraryPlaces.getProjectInfo().getProject().getProjectName();
    }

    public void delete() {
        if (userCanDeleteRepository()) {
            final DeleteProjectPopUpScreen popUp = deleteProjectPopUpScreen.get();
            popUp.show(this.projectInfo.getProject());
        }
    }

    public void addAsset() {
        if (userCanUpdateProject()) {
            this.libraryPlaces.goToAddAsset(this.projectInfo);
        }
    }

    public void importAsset() {
        if (userCanUpdateProject()) {
            newFileUploader.getCommand(newResourcePresenter).execute();
        }
    }

    public void showSettings() {
        if (userCanUpdateProject()) {
            libraryPlaces.goToSettings(this.projectInfo);
        }
    }

    public void rename() {
        if (userCanUpdateProject()) {
            final RenameProjectPopUpScreen popUp = renameProjectPopUpScreen.get();
            popUp.show(this.projectInfo);
        }
    }

    public void editContributors() {
        if (this.canEditContributors()) {
            final EditContributorsPopUpPresenter popUp = editContributorsPopUpPresenter.get();
            popUp.show(this.projectInfo.getOrganizationalUnit());
        }
    }

    public void build() {
        if (this.userCanBuildProject()) {
            this.buildExecutor.triggerBuild();
        }
    }

    public void deploy() {
        if (this.userCanBuildProject()) {
            this.buildExecutor.triggerBuildAndDeploy();
        }
    }

    public boolean canEditContributors() {
        return this.organizationalUnitController.canUpdateOrgUnit(this.projectInfo.getOrganizationalUnit());
    }

    public boolean userCanDeleteRepository() {
        return projectController.canDeleteProject(this.projectInfo.getProject());
    }

    public boolean userCanBuildProject() {
        return projectController.canBuildProject(this.projectInfo.getProject());
    }

    public boolean userCanUpdateProject() {
        return projectController.canUpdateProject(this.projectInfo.getProject());
    }

    @WorkbenchPartView
    public ProjectScreen.View getView() {
        return view;
    }
}

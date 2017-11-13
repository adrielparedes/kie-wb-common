/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

import org.guvnor.common.services.project.client.security.ProjectController;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.library.client.screens.importrepository.ImportRepositoryPopUpPresenter;
import org.kie.workbench.common.screens.library.client.screens.project.AddProjectPopUpPresenter;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmptyLibraryScreenTest {

    @Mock
    private EmptyLibraryScreen.View view;

    @Mock
    private ManagedInstance<AddProjectPopUpPresenter> addProjectPopUpPresenters;

    @Mock
    private ProjectController projectController;

    @Mock
    private LibraryPlaces libraryPlaces;

    @Mock
    private ManagedInstance<ImportRepositoryPopUpPresenter> importRepositoryPopUpPresenters;

    @Mock
    private AddProjectPopUpPresenter addProjectPopUpPresenter;

    @Mock
    private ImportRepositoryPopUpPresenter importRepositoryPopUpPresenter;

    private EmptyLibraryScreen emptyLibraryScreen;

    @Before
    public void setup() {
        doReturn(addProjectPopUpPresenter).when(addProjectPopUpPresenters).get();
        doReturn(importRepositoryPopUpPresenter).when(importRepositoryPopUpPresenters).get();

        doReturn(true).when(projectController).canCreateProjects();

        emptyLibraryScreen = new EmptyLibraryScreen(view,
                                                    addProjectPopUpPresenters,
                                                    projectController,
                                                    libraryPlaces,
                                                    importRepositoryPopUpPresenters);
    }

    @Test
    public void setupTest() {
        emptyLibraryScreen.setup();

        verify(view).init(emptyLibraryScreen);
    }

    @Test
    public void addProjectWithPermissionTest() {
        emptyLibraryScreen.addProject();

        verify(addProjectPopUpPresenter).show();
    }

    @Test
    public void addProjectWithoutPermissionTest() {
        doReturn(false).when(projectController).canCreateProjects();

        emptyLibraryScreen.addProject();

        verify(addProjectPopUpPresenter,
               never()).show();
    }

    @Test
    public void trySamplesWithPermissionTest() {
        emptyLibraryScreen.trySamples();

        verify(libraryPlaces).goToTrySamples();
    }

    @Test
    public void trySamplesWithoutPermissionTest() {
        doReturn(false).when(projectController).canCreateProjects();

        emptyLibraryScreen.trySamples();

        verify(libraryPlaces,
               never()).goToTrySamples();
    }

    @Test
    public void importProjectWithPermissionTest() {
        emptyLibraryScreen.importProject();

        verify(importRepositoryPopUpPresenter).show();
    }

    @Test
    public void importProjectWithoutPermissionTest() {
        doReturn(false).when(projectController).canCreateProjects();

        emptyLibraryScreen.importProject();

        verify(importRepositoryPopUpPresenter,
               never()).show();
    }
}
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
 *
 */

package org.kie.workbench.common.screens.library.client.screens.assets;

import org.guvnor.common.services.project.model.Project;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.library.api.LibraryService;
import org.kie.workbench.common.screens.library.api.ProjectInfo;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.mocks.CallerMock;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssetsScreenTest {

    private AssetsScreen assetsScreen;

    @Mock
    private AssetsScreen.View view;

    @Mock
    private LibraryPlaces libraryPlaces;

    @Mock
    private EmptyAssetsScreen emptyAssetsScreen;

    @Mock
    private PopulatedAssetsScreen populatedAssetsScreen;

    @Mock
    private TranslationService ts;

    @Mock
    private BusyIndicatorView busyIndicatorView;

    @Mock
    private LibraryService libraryService;

    @Before
    public void setUp() {

        ProjectInfo projectInfo = mock(ProjectInfo.class);
        when(projectInfo.getProject()).thenReturn(mock(Project.class));
        when(libraryPlaces.getProjectInfo()).thenReturn(projectInfo);

        when(emptyAssetsScreen.getView()).thenReturn(mock(EmptyAssetsView.class));
        when(populatedAssetsScreen.getView()).thenReturn(mock(PopulatedAssetsView.class));

        this.assetsScreen = new AssetsScreen(view,
                                             libraryPlaces,
                                             emptyAssetsScreen,
                                             populatedAssetsScreen,
                                             ts,
                                             busyIndicatorView,
                                             new CallerMock<>(libraryService));
    }

    @Test
    public void testShowEmptyScreenAssets() {
        when(libraryService.hasAssets(any(Project.class))).thenReturn(false);
        this.assetsScreen.init();
        verify(emptyAssetsScreen,
               times(1)).getView();
        verify(populatedAssetsScreen,
               never()).getView();
    }

    @Test
    public void testShowPopulatedScreenAssets() {
        when(libraryService.hasAssets(any(Project.class))).thenReturn(true);
        this.assetsScreen.init();
        verify(emptyAssetsScreen,
               never()).getView();
        verify(populatedAssetsScreen,
               times(1)).getView();
    }
}
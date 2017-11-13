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

package org.kie.workbench.common.screens.library.client.screens.importrepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImportRepositoryPopUpPresenterTest {

    @Mock
    private ImportRepositoryPopUpPresenter.View view;

    @Mock
    private LibraryPlaces libraryPlaces;

    private ImportRepositoryPopUpPresenter presenter;

    @Before
    public void setup() {
        presenter = new ImportRepositoryPopUpPresenter(view,
                                                       libraryPlaces);
    }

    @Test
    public void setupTest() {
        presenter.setup();

        verify(view).init(presenter);
    }

    @Test
    public void showTest() {
        presenter.show();

        verify(view).show();
    }

    @Test
    public void importRepositoryTest() {
        doReturn("repoUrl").when(view).getRepositoryURL();

        presenter.importRepository();

        verify(view).hide();
        verify(libraryPlaces).goToImportProjects("repoUrl");
    }

    @Test
    public void cancelTest() {
        presenter.cancel();

        verify(view).hide();
    }
}

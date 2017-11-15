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

package org.kie.workbench.common.screens.library.client.screens.project.delete;

import javax.enterprise.event.Event;

import org.guvnor.common.services.project.client.security.ProjectController;
import org.guvnor.common.services.project.events.DeleteProjectEvent;
import org.guvnor.common.services.project.model.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.mocks.CallerMock;
import org.uberfire.mocks.EventSourceMock;
import org.uberfire.workbench.events.NotificationEvent;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteProjectPopUpScreenTest {

    private DeleteProjectPopUpScreen presenter;

    @Mock
    private DeleteProjectPopUpScreen.View view;

    @Mock
    private KieProjectService kieProjectService;

    @Mock
    private ProjectController projectController;

    @Mock
    private LibraryPlaces libraryPlaces;

    @Mock
    private Project project;

    @Mock
    private Event<NotificationEvent> notificationEventEvent;

    @Before
    public void setUp() {

        when(project.getProjectName()).thenReturn("kieProject");
        presenter = spy(new DeleteProjectPopUpScreen(view,
                                                     new CallerMock<>(kieProjectService),
                                                     projectController,
                                                     notificationEventEvent,
                                                     libraryPlaces));
    }

    @Test
    public void testShowDeleteIfIsAbleTo() {

        this.presenter.show(project);

        verify(this.view,
               times(1)).show(eq(project.getProjectName()));
    }

    @Test
    public void testDeleteActionWithDifferentConfirmationName() {
        when(this.view.getConfirmedName()).thenReturn("anotherName");
        this.presenter.show(project);
        this.presenter.delete();

        verify(this.view,
               times(1)).showError(anyString());

        verify(this.libraryPlaces,
               never()).goToOrganizationalUnits();
    }

    @Test
    public void testDeleteAction() {
        when(this.view.getConfirmedName()).thenReturn("kieProject");
        this.presenter.show(project);
        this.presenter.delete();

        verify(this.view,
               never()).showError(anyString());

        verify(this.kieProjectService,
               times(1)).delete(any(),
                                anyString());
        verify(this.notificationEventEvent,
               times(1)).fire(any());
    }
}
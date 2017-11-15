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

package org.kie.workbench.common.screens.library.client.screens.project.delete;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.guvnor.common.services.project.client.security.ProjectController;
import org.guvnor.common.services.project.events.DeleteProjectEvent;
import org.guvnor.common.services.project.model.Project;
import org.jboss.errai.common.client.api.Caller;
import org.kie.workbench.common.screens.library.client.resources.i18n.LibraryConstants;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.uberfire.client.mvp.UberElemental;
import org.uberfire.ext.widgets.common.client.callbacks.HasBusyIndicatorDefaultErrorCallback;
import org.uberfire.ext.widgets.common.client.common.HasBusyIndicator;
import org.uberfire.workbench.events.NotificationEvent;

public class DeleteProjectPopUpScreen {

    public static final String PROJECT_DELETED = "Project deleted";

    public interface View extends UberElemental<DeleteProjectPopUpScreen>,
                                  HasBusyIndicator {

        String getConfirmedName();

        void show(String name);

        void showError(final String errorMessage);

        void hide();

        String getWrongConfirmedNameValidationMessage();

        String getDeletingMessage();

        String getDeleteSuccessMessage();
    }

    private Project project;

    private DeleteProjectPopUpScreen.View view;

    private Caller<KieProjectService> projectService;
    private ProjectController projectController;

    private Event<DeleteProjectEvent> deleteProjectEvent;

    private Event<NotificationEvent> notificationEvent;

    private LibraryPlaces libraryPlaces;

    @Inject
    public DeleteProjectPopUpScreen(final DeleteProjectPopUpScreen.View view,
                                    final Caller<KieProjectService> projectService,
                                    final ProjectController projectController,
                                    final Event<NotificationEvent> notificationEvent,
                                    final LibraryPlaces libraryPlaces) {
        this.view = view;
        this.projectService = projectService;
        this.projectController = projectController;
        this.notificationEvent = notificationEvent;
        this.libraryPlaces = libraryPlaces;
    }

    @PostConstruct
    public void setup() {
        view.init(this);
    }

    public void show(final Project project) {
        this.project = project;
        view.show(project.getProjectName());
    }

    public void delete() {
        final String confirmedName = view.getConfirmedName();
        if (!project.getProjectName().equals(confirmedName)) {
            view.showError(view.getWrongConfirmedNameValidationMessage());
            return;
        }

        view.showBusyIndicator(view.getDeletingMessage());
        projectService.call(v -> {
                                view.hideBusyIndicator();
                                notificationEvent.fire(new NotificationEvent(view.getDeleteSuccessMessage(),
                                                                             NotificationEvent.NotificationType.SUCCESS));
                                view.hide();
                            },
                            new HasBusyIndicatorDefaultErrorCallback(view)).delete(project.getPomXMLPath(),
                                                                                   PROJECT_DELETED);
    }

    public void cancel() {
        view.hide();
    }
}

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

package org.kie.workbench.common.screens.library.client.screens.project.fork;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.guvnor.common.services.project.model.WorkspaceProject;
import org.kie.workbench.common.screens.library.client.screens.assets.AssetsScreen;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.uberfire.client.mvp.UberElemental;
import org.uberfire.ext.widgets.common.client.common.HasBusyIndicator;

public class ForkProjectPopUpScreen {

    private View view;
    private Event<ForkProjectRequestEvent> forkProjectRequestEvent;
    private LibraryPlaces libraryPlaces;
    private WorkspaceProject projectInfo;

    @Inject
    public ForkProjectPopUpScreen(final View view,
                                  final Event<ForkProjectRequestEvent> forkProjectRequestEvent,
                                  final LibraryPlaces libraryPlaces) {
        this.view = view;
        this.forkProjectRequestEvent = forkProjectRequestEvent;
        this.libraryPlaces = libraryPlaces;
    }

    @PostConstruct
    public void setup() {
        view.init(this);
    }

    public void show(final WorkspaceProject projectInfo) {
        this.projectInfo = projectInfo;
        view.buildModal(projectInfo);
        view.show();
    }

    public void fork() {
        Map<String, String> params = new HashMap<>();
        params.put(AssetsScreen.SHOW,
                   AssetsScreen.FORK);
        libraryPlaces.goToProject(this.projectInfo,
                                  () -> {
                                      view.hide();
                                  });
    }

    public void cancel() {
        this.view.hide();
    }

    public interface View extends UberElemental<ForkProjectPopUpScreen>,
                                  HasBusyIndicator {

        void show();

        void hide();

        void buildModal(WorkspaceProject projectInfo);
    }
}
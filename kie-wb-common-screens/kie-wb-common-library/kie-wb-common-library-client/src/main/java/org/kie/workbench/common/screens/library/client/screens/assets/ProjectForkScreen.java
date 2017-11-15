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

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.guvnor.structure.repositories.Repository;
import org.guvnor.structure.repositories.RepositoryService;
import org.jboss.errai.common.client.api.Caller;
import org.kie.workbench.common.screens.library.client.screens.project.fork.ForkProjectCompleteEvent;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.uberfire.client.mvp.UberElemental;
import org.uberfire.spaces.Space;

public class ProjectForkScreen {

    private final View view;
    private final Caller<RepositoryService> projectService;
    private final Event<ForkProjectCompleteEvent> forkProjectCompleteEvent;
    private final LibraryPlaces libraryPlaces;

    @Inject
    public ProjectForkScreen(final ProjectForkScreen.View view,
                             final Caller<RepositoryService> projectService,
                             final Event<ForkProjectCompleteEvent> forkProjectCompleteEvent,
                             final LibraryPlaces libraryPlaces) {
        this.view = view;
        this.projectService = projectService;
        this.forkProjectCompleteEvent = forkProjectCompleteEvent;
        this.libraryPlaces = libraryPlaces;
    }

    @PostConstruct
    public void initialize() {
        this.view.init(this);
    }

    public void fork() {

        Repository repository = libraryPlaces.getActiveWorkspaceContext().getRepository();
        Space space = libraryPlaces.getActiveWorkspaceContext().getOrganizationalUnit().getSpace();
        this.projectService
                .call(forkedProject -> {
                    this.forkProjectCompleteEvent.fire(new ForkProjectCompleteEvent());
                })
                .fork(space,
                      repository);
    }

    public View getView() {
        return this.view;
    }

    public interface View extends UberElemental<ProjectForkScreen> {

    }
}

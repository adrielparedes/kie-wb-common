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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.widgets.common.client.common.HasBusyIndicator;

public class ImportRepositoryPopUpPresenter {

    public interface View extends UberElement<ImportRepositoryPopUpPresenter>,
                                  HasBusyIndicator {

        String getRepositoryURL();

        void show();

        void hide();

        void showError(final String errorMessage);
    }

    private View view;

    private LibraryPlaces libraryPlaces;

    @Inject
    public ImportRepositoryPopUpPresenter(final View view,
                                          final LibraryPlaces libraryPlaces) {
        this.view = view;
        this.libraryPlaces = libraryPlaces;
    }

    @PostConstruct
    public void setup() {
        view.init(this);
    }

    public void show() {
        view.show();
    }

    public void importRepository() {
        view.hide();
        libraryPlaces.goToImportProjects(view.getRepositoryURL());
    }

    public void cancel() {
        view.hide();
    }

    public View getView() {
        return view;
    }
}

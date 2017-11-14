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

package org.kie.workbench.common.screens.library.client.screens.assets;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import elemental2.dom.HTMLElement;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.screens.library.api.LibraryService;
import org.kie.workbench.common.screens.library.api.ProjectInfo;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.uberfire.client.mvp.UberElemental;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;

public class AssetsScreen {

    private final View view;
    private LibraryPlaces libraryPlaces;
    private EmptyAssetsScreen emptyAssetsScreen;
    private PopulatedAssetsScreen populatedAssetsScreen;
    private BusyIndicatorView busyIndicatorView;
    private TranslationService translationService;
    private Caller<LibraryService> libraryService;
    private ProjectInfo projectInfo;

    public interface View extends UberElemental<AssetsScreen> {

        void setContent(HTMLElement element);
    }

    @Inject
    public AssetsScreen(final View view,
                        final LibraryPlaces libraryPlaces,
                        final EmptyAssetsScreen emptyAssetsScreen,
                        final PopulatedAssetsScreen populatedAssetsScreen,
                        final TranslationService translationService,
                        final Caller<LibraryService> libraryService) {
        this.view = view;
        this.libraryPlaces = libraryPlaces;
        this.emptyAssetsScreen = emptyAssetsScreen;
        this.populatedAssetsScreen = populatedAssetsScreen;
        this.translationService = translationService;
        this.libraryService = libraryService;
    }

    @PostConstruct
    public void init() {
        this.view.init(this);
        this.projectInfo = libraryPlaces.getProjectInfo();
        this.showAssets();
    }

    private void showAssets() {
        libraryService.call((Boolean hasAssets) -> {
            if (hasAssets) {
                this.view.setContent(populatedAssetsScreen.getView().getElement());
            } else {
                this.view.setContent(emptyAssetsScreen.getView().getElement());
            }
        }).hasAssets(this.projectInfo.getProject());
    }

    public int getProjectAssetsCount() {
        return libraryPlaces.getProjectInfo().getProject().getNumberOfAssets();
    }

    public View getView() {
        return view;
    }
}

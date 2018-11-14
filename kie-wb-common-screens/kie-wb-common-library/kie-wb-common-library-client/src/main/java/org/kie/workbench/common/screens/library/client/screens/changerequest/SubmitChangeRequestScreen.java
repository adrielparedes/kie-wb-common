/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.screens.library.client.screens.changerequest;

import javax.inject.Inject;

import org.kie.workbench.common.screens.library.client.perspective.LibraryPerspective;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElemental;

@WorkbenchScreen(identifier = LibraryPlaces.SUBMIT_CHANGE_REQUEST,
        owningPerspective = LibraryPerspective.class)
public class SubmitChangeRequestScreen {

    private final View view;

    @Inject
    public SubmitChangeRequestScreen(final View view) {
        this.view = view;
        this.view.init(this);
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return this.getView().getTitle();
    }

    @WorkbenchPartView
    public SubmitChangeRequestScreen.View getView() {
        return this.view;
    }

    public interface View extends UberElemental<SubmitChangeRequestScreen> {

        String getTitle();

        void setFromBranch(String from);

        void setToBranch(String to);

        void setSummary(String summary);

        void setDescription(String description);
    }
}

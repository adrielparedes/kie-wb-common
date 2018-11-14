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

import elemental2.dom.HTMLDivElement;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.library.client.resources.i18n.LibraryConstants;

@Templated
public class SubmitChangeRequestView implements SubmitChangeRequestScreen.View,
                                                IsElement {

    private SubmitChangeRequestScreen screen;

    @Inject
    private TranslationService ts;

    @Inject
    @DataField("title")
    private HTMLDivElement title;

    @Override
    public void init(SubmitChangeRequestScreen presenter) {
        this.screen = presenter;
        this.title.textContent = ts.getTranslation(LibraryConstants.SubmitChangeRequest);
    }

    @Override
    public void setFromBranch(String from) {

    }

    @Override
    public void setToBranch(String to) {

    }

    @Override
    public void setSummary(String summary) {

    }

    @Override
    public void setDescription(String description) {

    }
}

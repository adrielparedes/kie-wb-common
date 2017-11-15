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

import javax.inject.Inject;
import javax.inject.Named;

import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import org.guvnor.common.services.project.model.WorkspaceProject;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.library.client.resources.i18n.LibraryConstants;
import org.kie.workbench.common.screens.library.client.widgets.common.PopUpUtils;
import org.uberfire.ext.editor.commons.client.file.popups.CommonModalBuilder;
import org.uberfire.ext.widgets.common.client.common.BusyPopup;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;

@Templated
public class ForkProjectPopUpView implements ForkProjectPopUpScreen.View,
                                             IsElement {

    private ForkProjectPopUpScreen presenter;

    @Inject
    private TranslationService ts;

    @Inject
    @DataField("body")
    private HTMLDivElement body;

    @Inject
    @DataField("description")
    @Named("span")
    private HTMLElement description;

    private BaseModal modal;

    @Override
    public void init(ForkProjectPopUpScreen presenter) {
        this.presenter = presenter;
    }

    private Widget createFork() {
        return PopUpUtils.button(ts.getTranslation(LibraryConstants.Fork),
                                 () -> this.presenter.fork(),
                                 ButtonType.PRIMARY);
    }

    private Widget createCancel() {
        return PopUpUtils.button(ts.getTranslation(LibraryConstants.Cancel),
                                 () -> this.presenter.cancel(),
                                 ButtonType.DEFAULT);
    }

    @Override
    public void show() {
        this.modal.show();
    }

    @Override
    public void hide() {
        this.modal.hide();
    }

    @Override
    public void buildModal(WorkspaceProject projectInfo) {
        String projectName = projectInfo.getName();
        this.description.textContent = ts.format(LibraryConstants.ForkDescription,
                                                 projectName);
        this.modal = new CommonModalBuilder()
                .addHeader(ts.format(LibraryConstants.ForkTitle,
                                     projectName))
                .addBody(body)
                .addFooter(PopUpUtils.footer(createCancel(),
                                             createFork()))
                .build();
    }

    @Override
    public void showBusyIndicator(final String message) {
        BusyPopup.showMessage(message);
    }

    @Override
    public void hideBusyIndicator() {
        BusyPopup.close();
    }
}
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

package org.kie.workbench.common.screens.library.client.screens.organizationalunit;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.library.client.resources.i18n.LibraryConstants;
import org.kie.workbench.common.screens.library.client.util.TranslationUtils;
import org.kie.workbench.common.screens.library.client.widgets.common.TileWidget;

@Templated
public class OrganizationalUnitsView implements OrganizationalUnitsScreen.View,
                                                IsElement {

    private OrganizationalUnitsScreen presenter;

    @Inject
    private TranslationService ts;

    @Inject
    private TranslationUtils translationUtils;

    @Inject
    @DataField("title")
    Div title;

    @Inject
    @DataField("create-organizational-unit")
    Button createOrganizationalUnit;

    @Inject
    @DataField("cards-container")
    Div cardsContainer;

    @Override
    public void init(OrganizationalUnitsScreen presenter) {
        this.presenter = presenter;
        createOrganizationalUnit.setTextContent(ts.format(LibraryConstants.CreateOrganizationalUnit,
                                                          translationUtils.getOrganizationalUnitAliasInSingular()));
        title.setTextContent(translationUtils.getOrganizationalUnitAliasInPlural());
    }

    @Override
    public void clearOrganizationalUnits() {
        cardsContainer.setTextContent("");
    }

    @Override
    public void hideCreateOrganizationalUnitAction() {
        createOrganizationalUnit.setHidden(true);
    }

    @Override
    public void addOrganizationalUnit(final TileWidget tileWidget) {
        cardsContainer.appendChild(tileWidget.getView().getElement());
    }

    @Override
    public String getNumberOfContributorsLabel(int numberOfContributors) {
        return ts.format(LibraryConstants.NumberOfContributors,
                         numberOfContributors);
    }

    @Override
    public String getNumberOfRepositoriesLabel(int numberOfRepositories) {
        return ts.format(LibraryConstants.NumberOfRepositories,
                         numberOfRepositories);
    }

    @EventHandler("create-organizational-unit")
    public void createOrganizationalUnit(final ClickEvent event) {
        presenter.createOrganizationalUnit();
    }
}

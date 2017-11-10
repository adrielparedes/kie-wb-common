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

package org.kie.workbench.common.screens.library.client.screens.project;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLOListElement;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated
public class ProjectView implements ProjectScreen.View,
                                    IsElement {

    public static final String ACTIVE = "active";
    private ProjectScreen presenter;

    @Inject
    @DataField("assets-link")
    HTMLAnchorElement assetsTabLink;

    @Inject
    @DataField("assets-tab")
    HTMLOListElement assetsTabItem;

    @Inject
    @DataField("contributors-link")
    HTMLAnchorElement contributorsTabLink;

    @Inject
    @DataField("contributors-tab")
    HTMLOListElement contributorsTabItem;

    @Inject
    @DataField("metrics-link")
    HTMLAnchorElement metricsTabLink;

    @Inject
    @DataField("metrics-tab")
    HTMLOListElement metricsTabItem;

    @Inject
    @DataField("settings-link")
    HTMLAnchorElement settingsTabLink;

    @Inject
    @DataField("settings-tab")
    HTMLOListElement settingsTabItem;

    @Inject
    @DataField("main-container")
    HTMLDivElement mainContainer;

    @Inject
    @DataField("assets-count")
    HTMLElement assetsCount;

    @Inject
    @DataField("contributors-count")
    HTMLElement contributorsCount;

    @Override
    public void setAssetsCount(int count) {
        assetsCount.textContent = String.valueOf(count);
    }

    @Override
    public void setContributorsCount(int count) {
        contributorsCount.textContent = String.valueOf(count);
    }

    @Override
    public void setContent(HTMLElement content) {
        this.mainContainer.appendChild(content);
    }

    @Override
    public void init(ProjectScreen presenter) {
        this.presenter = presenter;
    }

    @EventHandler("assets-link")
    public void clickAssetsTab(final ClickEvent clickEvent) {
        this.deactivateAllTabs();
        this.activate(this.assetsTabItem);
        this.presenter.showAssets();
    }

    @EventHandler("contributors-link")
    public void clickContributorsTab(final ClickEvent clickEvent) {
        this.deactivateAllTabs();
        this.activate(this.contributorsTabItem);
        this.presenter.showContributors();
    }

    @EventHandler("metrics-link")
    public void clickMetricsTab(final ClickEvent clickEvent) {
        this.deactivateAllTabs();
        this.activate(this.metricsTabItem);
        this.presenter.showMetrics();
    }

    @EventHandler("settings-link")
    public void clickSettingsTab(final ClickEvent clickEvent) {
        this.deactivateAllTabs();
        this.activate(this.settingsTabItem);
        this.presenter.showSettings();
    }

    private void activate(HTMLOListElement element) {
        element.classList.add(ACTIVE);
    }

    private void deactivate(HTMLOListElement element) {
        element.classList.remove(ACTIVE);
    }

    private void deactivateAllTabs() {
        this.deactivate(this.assetsTabItem);
        this.deactivate(this.contributorsTabItem);
        this.deactivate(this.metricsTabItem);
        this.deactivate(this.settingsTabItem);
    }
}

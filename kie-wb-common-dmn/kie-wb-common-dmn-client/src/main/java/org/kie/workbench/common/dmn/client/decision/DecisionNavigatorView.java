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
 */

package org.kie.workbench.common.dmn.client.decision;

import javax.inject.Inject;

import elemental2.dom.HTMLDivElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.dmn.client.decision.included.components.DecisionComponents;
import org.kie.workbench.common.dmn.client.decision.tree.DecisionNavigatorTreePresenter;

@Templated
public class DecisionNavigatorView implements DecisionNavigatorPresenter.View {

    @DataField("main-tree")
    private final HTMLDivElement mainTree;

    @DataField("decision-components")
    private final HTMLDivElement decisionComponents;

    private DecisionNavigatorPresenter presenter;

    @Inject
    public DecisionNavigatorView(final HTMLDivElement mainTree,
                                 final HTMLDivElement decisionComponents) {
        this.mainTree = mainTree;
        this.decisionComponents = decisionComponents;
    }

    @Override
    public void init(final DecisionNavigatorPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupMainTree(final DecisionNavigatorTreePresenter.View mainTreeComponent) {
        mainTree.appendChild(mainTreeComponent.getElement());
    }

    @Override
    public void setupDecisionComponents(final DecisionComponents.View decisionComponentsComponent) {
        decisionComponents.appendChild(decisionComponentsComponent.getElement());
    }
}

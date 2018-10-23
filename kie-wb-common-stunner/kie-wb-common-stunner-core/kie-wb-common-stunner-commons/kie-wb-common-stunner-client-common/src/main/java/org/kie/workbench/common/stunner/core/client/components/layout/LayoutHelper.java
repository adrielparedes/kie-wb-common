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

package org.kie.workbench.common.stunner.core.client.components.layout;

import javax.inject.Inject;

import org.kie.workbench.common.stunner.core.graph.Graph;
import org.kie.workbench.common.stunner.core.graph.processing.layout.Layout;
import org.kie.workbench.common.stunner.core.graph.processing.layout.LayoutExecutor;
import org.kie.workbench.common.stunner.core.graph.processing.layout.LayoutService;

public final class LayoutHelper {

    private final LayoutService layoutService;
    private final LayoutExecutor layoutExecutor;

    @Inject
    public LayoutHelper(final LayoutService layoutService,
                        final LayoutExecutor layoutExecutor) {
        this.layoutService = layoutService;
        this.layoutExecutor = layoutExecutor;
    }

    public void applyLayout(final Graph graph) {
        if (graph != null && !this.layoutService.hasLayoutInformation(graph)) {
            final Layout layout = this.layoutService.createLayout(graph);
            this.layoutExecutor.applyLayout(layout, graph);
        }
    }
}
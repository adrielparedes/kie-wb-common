/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.project.cli.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.guvnor.structure.backend.backcompat.BackwardCompatibleUtil;
import org.guvnor.structure.contributors.Contributor;
import org.guvnor.structure.contributors.ContributorType;
import org.guvnor.structure.organizationalunit.config.RepositoryConfiguration;
import org.guvnor.structure.organizationalunit.config.RepositoryInfo;
import org.guvnor.structure.organizationalunit.config.SpaceConfigStorageRegistry;
import org.guvnor.structure.organizationalunit.config.SpaceInfo;
import org.guvnor.structure.server.config.ConfigGroup;
import org.guvnor.structure.server.config.ConfigItem;
import org.guvnor.structure.server.config.ConfigType;
import org.guvnor.structure.server.config.ConfigurationService;

@Dependent
public class ConfigGroupToSpaceInfoConverter {

    private ConfigurationService configurationService;
    private BackwardCompatibleUtil backwardCompatibleUtil;
    private SpaceConfigStorageRegistry spaceConfigStorageRegistry;

    @Inject
    public ConfigGroupToSpaceInfoConverter(final ConfigurationService configurationService, final BackwardCompatibleUtil backwardCompatibleUtil, final SpaceConfigStorageRegistry spaceConfigStorageRegistry) {
        this.configurationService = configurationService;
        this.backwardCompatibleUtil = backwardCompatibleUtil;
        this.spaceConfigStorageRegistry = spaceConfigStorageRegistry;
    }

    public SpaceInfo toSpaceInfo(ConfigGroup configGroup) {
        final String spaceName = extractName(configGroup);

        Optional<SpaceInfo> optional = Optional.ofNullable(spaceConfigStorageRegistry.get(spaceName).loadSpaceInfo());

        if (optional.isPresent()) {
            return optional.get();
        }

        final String defaultGroupId = extractDefaultGroupId(configGroup);
        final Collection<Contributor> contributors = extractContributors(configGroup);
        final List<RepositoryInfo> repositories = extractRepositories(spaceName);
        final List<String> securityGroups = extractSecurityGroups(configGroup);

        return new SpaceInfo(spaceName,
                                    false,
                                    defaultGroupId,
                                    contributors,
                                    repositories,
                                    securityGroups);
    }

    private String extractName(final ConfigGroup groupConfig) {
        return groupConfig.getName();
    }

    private String extractDefaultGroupId(final ConfigGroup groupConfig) {
        String defaultGroupId = groupConfig.getConfigItemValue("defaultGroupId");

        if (defaultGroupId == null || defaultGroupId.trim().isEmpty()) {
            defaultGroupId = getSanitizedDefaultGroupId(extractName(groupConfig));
        }

        return defaultGroupId;
    }

    private String getSanitizedDefaultGroupId(final String proposedGroupId) {
        //Only [A-Za-z0-9_\-.] are valid so strip everything else out
        return proposedGroupId != null ? proposedGroupId.replaceAll("[^A-Za-z0-9_\\-.]",
                                                                    "") : proposedGroupId;
    }

    private Collection<Contributor> extractContributors(final ConfigGroup configGroup) {
        final List<Contributor> contributors = new ArrayList<>();
        boolean oldConfigGroup = false;

        final String oldOwner = configGroup.getConfigItemValue("owner");
        if (oldOwner != null) {
            oldConfigGroup = true;
            contributors.add(new Contributor(oldOwner,
                                             ContributorType.OWNER));
        }

        ConfigItem<List<String>> oldContributors = configGroup.getConfigItem("contributors");
        if (oldContributors != null) {
            oldConfigGroup = true;

            for (String userName : oldContributors.getValue()) {
                if (!userName.equals(oldOwner)) {
                    contributors.add(new Contributor(userName,
                                                     ContributorType.CONTRIBUTOR));
                }
            }
        }

        if (!oldConfigGroup) {
            ConfigItem<List<Contributor>> newContributorsConfigItem = configGroup.getConfigItem("space-contributors");
            contributors.addAll(newContributorsConfigItem.getValue());
        }

        return contributors;
    }

    private List<RepositoryInfo> extractRepositories(final String spaceName) {
        List<ConfigGroup> repos = configurationService.getConfiguration(ConfigType.REPOSITORY, spaceName);

        return repos.stream().map(this::toRepositoryInfo)
                .collect(Collectors.toList());
    }

    private RepositoryInfo toRepositoryInfo(ConfigGroup configGroup) {
        final Map<String, Object> environment = backwardCompatibleUtil.compat(configGroup).getItems().stream().collect(Collectors.toMap(item -> item.getName(), item -> item.getValue()));

        return new RepositoryInfo(configGroup.getName(), false, new RepositoryConfiguration(environment));
    }

    private List<String> extractSecurityGroups(final ConfigGroup groupConfig) {
        ConfigItem<List<String>> securityGroups = backwardCompatibleUtil.compat(groupConfig).getConfigItem("security:groups");
        return securityGroups.getValue();
    }
}

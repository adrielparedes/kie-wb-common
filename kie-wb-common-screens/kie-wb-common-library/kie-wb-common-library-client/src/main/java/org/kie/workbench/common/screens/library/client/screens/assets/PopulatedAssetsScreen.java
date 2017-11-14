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

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.ext.uberfire.social.activities.client.widgets.utils.SocialDateFormatter;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.screens.explorer.client.utils.Classifier;
import org.kie.workbench.common.screens.explorer.client.utils.Utils;
import org.kie.workbench.common.screens.explorer.model.FolderItemType;
import org.kie.workbench.common.screens.library.api.AssetInfo;
import org.kie.workbench.common.screens.library.api.LibraryService;
import org.kie.workbench.common.screens.library.api.ProjectAssetsQuery;
import org.kie.workbench.common.screens.library.api.ProjectInfo;
import org.kie.workbench.common.screens.library.client.resources.i18n.LibraryConstants;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.kie.workbench.common.screens.library.client.widgets.project.AssetItemWidget;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.mvp.UberElemental;
import org.uberfire.client.workbench.type.ClientResourceType;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.mvp.Command;
import org.uberfire.util.URIUtil;

public class PopulatedAssetsScreen {

    private View view;
    private BusyIndicatorView busyIndicatorView;
    private LibraryPlaces libraryPlaces;
    private TranslationService ts;
    private Classifier assetClassifier;
    private ManagedInstance<AssetItemWidget> assetItemWidget;
    private Caller<LibraryService> libraryService;
    private ProjectInfo projectInfo;

    public interface View extends UberElemental<PopulatedAssetsScreen> {

        void addAsset(HTMLElement element);
    }

    @Inject
    public PopulatedAssetsScreen(final View view,
                                 final BusyIndicatorView busyIndicatorView,
                                 final LibraryPlaces libraryPlaces,
                                 final TranslationService ts,
                                 final Classifier assetClassifier,
                                 final ManagedInstance<AssetItemWidget> assetItemWidget,
                                 final Caller<LibraryService> libraryService) {
        this.view = view;
        this.busyIndicatorView = busyIndicatorView;
        this.libraryPlaces = libraryPlaces;
        this.ts = ts;
        this.assetClassifier = assetClassifier;
        this.assetItemWidget = assetItemWidget;
        this.libraryService = libraryService;
    }

    @PostConstruct
    public void init() {
        this.projectInfo = libraryPlaces.getProjectInfo();
        this.view.init(this);
        this.getAssets(assetInfos -> {
            assetInfos.forEach(asset -> {
                if (!asset.getFolderItem().getType().equals(FolderItemType.FOLDER)) {
                    AssetItemWidget item = assetItemWidget.get();
                    final ClientResourceType assetResourceType = getResourceType(asset);
                    final String assetName = getAssetName(asset,
                                                          assetResourceType);

                    item.init(assetName,
                              getAssetPath(asset),
                              assetResourceType.getDescription(),
                              assetResourceType.getIcon(),
                              getLastModifiedTime(asset),
                              getCreatedTime(asset),
                              detailsCommand((Path) asset.getFolderItem().getItem()),
                              selectCommand((Path) asset.getFolderItem().getItem()));
                    this.view.addAsset(Js.cast(item.getElement()));
                }
            });
        });
    }

    private ClientResourceType getResourceType(AssetInfo asset) {
        return assetClassifier.findResourceType(asset.getFolderItem());
    }

    private String getAssetName(AssetInfo asset,
                                ClientResourceType assetResourceType) {
        return Utils.getBaseFileName(asset.getFolderItem().getFileName(),
                                     assetResourceType.getSuffix());
    }

    private String getAssetPath(final AssetInfo asset) {
        final String fullPath = ((Path) asset.getFolderItem().getItem()).toURI();
        final String projectRootPath = projectInfo.getProject().getRootPath().toURI();
        final String relativeAssetPath = fullPath.substring(projectRootPath.length() + 1);
        final String decodedRelativeAssetPath = URIUtil.decode(relativeAssetPath);

        return decodedRelativeAssetPath;
    }

    private void getAssets(RemoteCallback<List<AssetInfo>> callback) {
        libraryService.call(callback).getProjectAssets(new ProjectAssetsQuery(libraryPlaces.getProjectInfo().getProject(),
                                                                              "",
                                                                              0,
                                                                              100));
    }

    private String getLastModifiedTime(final AssetInfo asset) {
        return ts.format(LibraryConstants.LastModified) + " " + SocialDateFormatter.format(asset.getLastModifiedTime());
    }

    String getCreatedTime(final AssetInfo asset) {
        return ts.format(LibraryConstants.Created) + " " + SocialDateFormatter.format(asset.getCreatedTime());
    }

    Command selectCommand(final Path assetPath) {
        return () -> libraryPlaces.goToAsset(projectInfo,
                                             assetPath);
    }

    Command detailsCommand(final Path assetPath) {
        return selectCommand(assetPath);
    }

    public View getView() {
        return view;
    }
}

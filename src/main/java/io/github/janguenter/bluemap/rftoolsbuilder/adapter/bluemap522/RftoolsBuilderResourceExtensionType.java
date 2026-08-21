/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;

/** Resource-pack extension factory registered before resource loading. */
final class RftoolsBuilderResourceExtensionType
        implements ResourcePack.Extension<RftoolsBuilderResourceExtension> {

    private static final Key KEY =
            Key.parse("bluemap_rftoolsbuilder:shield_extension");
    private final RftoolsBuilderRuntime runtime;

    RftoolsBuilderResourceExtensionType(RftoolsBuilderRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public RftoolsBuilderResourceExtension create(ResourcePack pack) {
        return new RftoolsBuilderResourceExtension(pack, runtime);
    }
}

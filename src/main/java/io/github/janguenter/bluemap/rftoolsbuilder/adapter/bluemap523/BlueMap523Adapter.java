/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState;
import de.bluecolored.bluemap.core.util.Key;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.RegistryGuard;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.ResourceExtensionType;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.SyntheticDispatch;

/** Exact BlueMap 5.23 feature-backport internal ABI registration boundary. */
public final class BlueMap523Adapter {

    private static final RftoolsBuilderRuntime RUNTIME = RftoolsBuilderRuntime.INSTANCE;
    private static final BlockRendererType RENDERER = new BlockRendererType.Impl(
            Key.parse("bluemap_rftoolsbuilder:shield"),
            (pack, gallery, settings) -> new RftoolsBuilderShieldRenderer(
                    pack, gallery, settings, RUNTIME
            )
    );
    private static final ResourcePack.Extension<RftoolsBuilderResourceExtension> EXTENSION =
            new ResourceExtensionType<>(
                    Key.parse("bluemap_rftoolsbuilder:shield_extension"),
                    pack -> new RftoolsBuilderResourceExtension(pack, RUNTIME)
            );

    private BlueMap523Adapter() {
    }

    public static synchronized boolean install() {
        if (!RegistryGuard.canRegister(BlockRendererType.REGISTRY, RENDERER)
                || !RegistryGuard.canRegister(ResourcePack.Extension.REGISTRY, EXTENSION)) {
            RUNTIME.inactive("registry-collision");
            return false;
        }
        if (!RegistryGuard.register(BlockRendererType.REGISTRY, RENDERER)
                || !RegistryGuard.register(ResourcePack.Extension.REGISTRY, EXTENSION)) {
            RUNTIME.inactive("registry-registration-failed");
            return false;
        }
        return true;
    }

    static boolean isExpectedDispatch(BlockState state) {
        return SyntheticDispatch.matches(state, RENDERER);
    }
}

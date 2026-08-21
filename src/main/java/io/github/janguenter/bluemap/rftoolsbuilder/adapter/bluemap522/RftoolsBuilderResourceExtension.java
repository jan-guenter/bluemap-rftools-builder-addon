/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.BlockProperties;
import de.bluecolored.bluemap.core.world.BlockState;
import io.github.janguenter.bluemap.rftoolsbuilder.profile.ExactRftoolsBuilderArtifactDetector;
import io.github.janguenter.bluemap.rftoolsbuilder.profile.RftoolsBuilder705Profile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/** Exact-artifact activation, resource validation, and narrow host routing. */
final class RftoolsBuilderResourceExtension implements ResourcePackExtension {

    static final Key SYNTHETIC = Key.parse("bluemap_rftoolsbuilder:shield");

    private final ResourcePack resourcePack;
    private final RftoolsBuilderRuntime runtime;

    RftoolsBuilderResourceExtension(
            ResourcePack resourcePack,
            RftoolsBuilderRuntime runtime
    ) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
    }

    @Override
    public void loadResources(Iterable<Path> roots) {
        if (Boolean.getBoolean("bluemap.rftoolsbuilder.disabled")) {
            runtime.inactive("operator-disabled");
            return;
        }
        if (!ExactRftoolsBuilderArtifactDetector.matches(
                roots,
                RftoolsBuilder705Profile.JAR_SHA256,
                RftoolsBuilder705Profile.JAR_SIZE
        )) {
            runtime.inactive("exact-artifact-missing-or-duplicate");
            return;
        }
        if (!validDispatch(resourcePack.getBlockStates().get(SYNTHETIC))) {
            runtime.inactive("synthetic-dispatch-invalid");
            return;
        }
        runtime.activate();
    }

    @Override
    public Set<Key> collectUsedTextureKeys() {
        return runtime.active() ? RftoolsBuilder705Profile.REQUIRED_TEXTURES : Set.of();
    }

    @Override
    public void bake() {
        if (!runtime.active()) {
            return;
        }
        try {
            for (Key key : RftoolsBuilder705Profile.SHIELD_TEXTURES) {
                if (!validTexture(key, 16, 816, true)) {
                    runtime.inactive("shield-texture-invalid");
                    return;
                }
            }
            if (!validTexture(RftoolsBuilder705Profile.TRANSPARENT_TEXTURE, 16, 16, false)
                    || !validTexture(RftoolsBuilder705Profile.SOLID_TEXTURE, 16, 16, false)) {
                runtime.inactive("fixed-texture-invalid");
                return;
            }
        } catch (IOException | RuntimeException exception) {
            runtime.inactive("texture-unreadable");
            return;
        }
        System.out.println("BlueMap RFTools Builder add-on active: routed 3 exact shield hosts.");
    }

    @Override
    public Key getBlockStateKey(Key key) {
        return runtime.active() && RftoolsBuilder705Profile.owns(key.getFormatted())
                ? SYNTHETIC : key;
    }

    @Override
    public void getBlockProperties(BlockState state, BlockProperties.Builder builder) {
        if (runtime.active() && RftoolsBuilder705Profile.owns(state.getId().getFormatted())) {
            builder.culling(false).occluding(false).cullingIdentical(false);
        }
    }

    private boolean validTexture(Key key, int width, int height, boolean animated)
            throws IOException {
        Texture texture = resourcePack.getTextures().get(key);
        if (texture == null || (texture.getAnimation() != null) != animated) {
            return false;
        }
        BufferedImage image = texture.getTextureImage();
        return image != null && image.getWidth() == width && image.getHeight() == height;
    }

    private static boolean validDispatch(
            de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState state
    ) {
        if (state == null || state.getMultipart() != null) {
            return false;
        }
        Variants variants = state.getVariants();
        if (variants == null || variants.getDefaultVariant() == null) {
            return false;
        }
        VariantSet set = variants.getDefaultVariant();
        return set.getVariants().length == 1
                && BlueMap522Adapter.isExpectedDispatch(set.getVariants()[0]);
    }
}

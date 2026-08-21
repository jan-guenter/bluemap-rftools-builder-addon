/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.MaxCapacityReachedException;
import de.bluecolored.bluemap.core.map.hires.RenderSettings;
import de.bluecolored.bluemap.core.map.hires.TileModelView;
import de.bluecolored.bluemap.core.map.hires.block.BlockRenderer;
import de.bluecolored.bluemap.core.map.hires.block.ResourceModelRenderer;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.math.Color;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;
import io.github.janguenter.bluemap.rftoolsbuilder.profile.RftoolsBuilder705Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Exact shielding renderer with whole-block stock fallback. */
final class RftoolsBuilderShieldRenderer implements BlockRenderer {

    private static final float MINT_RED = 0x96 / 255F;
    private static final float MINT_GREEN = 0xFF / 255F;
    private static final float MINT_BLUE = 0xC8 / 255F;

    private final ResourcePack resourcePack;
    private final RftoolsBuilderRuntime runtime;
    private final ResourceModelRenderer stock;
    private final ShieldCubeEmitter emitter;

    RftoolsBuilderShieldRenderer(
            ResourcePack resourcePack,
            TextureGallery textures,
            RenderSettings settings,
            RftoolsBuilderRuntime runtime
    ) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
        this.stock = new ResourceModelRenderer(resourcePack, textures, settings);
        this.emitter = new ShieldCubeEmitter(textures);
    }

    @Override
    public void render(
            BlockNeighborhood block,
            Variant ignored,
            TileModelView target,
            Color mapColor
    ) {
        int start = target.getStart();
        Color initialMapColor = new Color().set(mapColor);
        if (!runtime.active()) {
            renderStock(block, target, mapColor);
            return;
        }
        try {
            if (!renderExact(block, target, mapColor)) {
                resetAndRenderStock(block, target, start, mapColor, initialMapColor);
            }
        } catch (MaxCapacityReachedException exception) {
            resetPartialGeometry(target, start, mapColor, initialMapColor);
            throw exception;
        } catch (RuntimeException | LinkageError exception) {
            runtime.report("render-failed-" + exception.getClass().getSimpleName());
            resetAndRenderStock(block, target, start, mapColor, initialMapColor);
        }
    }

    private boolean renderExact(
            BlockNeighborhood block,
            TileModelView target,
            Color mapColor
    ) {
        String blockId = block.getBlockState().getId().getFormatted();
        if (!RftoolsBuilder705Profile.owns(blockId)) {
            return false;
        }
        Optional<ShieldRenderMode> parsed = ShieldRenderMode.parse(
                block.getBlockState().getProperties().get("render")
        );
        if (parsed.isEmpty() || !parsed.orElseThrow().supported()) {
            return false;
        }
        ShieldRenderMode mode = parsed.orElseThrow();
        if (mode == ShieldRenderMode.INVISIBLE) {
            mapColor.set(0F, 0F, 0F, 0F, true);
            return true;
        }

        ShieldCubeEmitter.Emission emission = emitter.emit(block, target, mode);
        if (emission.representativeTexture() == null) {
            mapColor.set(0F, 0F, 0F, 0F, true);
            return true;
        }
        Texture texture = resourcePack.getTextures().get(emission.representativeTexture());
        if (texture == null) {
            return false;
        }
        mapColor.set(texture.getColorStraight());
        if (mode == ShieldRenderMode.SHIELD) {
            mapColor.r *= MINT_RED;
            mapColor.g *= MINT_GREEN;
            mapColor.b *= MINT_BLUE;
        }
        return true;
    }

    private void resetAndRenderStock(
            BlockNeighborhood block,
            TileModelView target,
            int start,
            Color mapColor,
            Color initialMapColor
    ) {
        resetPartialGeometry(target, start, mapColor, initialMapColor);
        try {
            renderStock(block, target, mapColor);
        } catch (MaxCapacityReachedException exception) {
            resetPartialGeometry(target, start, mapColor, initialMapColor);
            throw exception;
        }
    }

    static void resetPartialGeometry(
            TileModelView target,
            int start,
            Color mapColor,
            Color initialMapColor
    ) {
        target.getTileModel().reset(start);
        target.initialize(start);
        mapColor.set(initialMapColor);
    }

    private void renderStock(
            BlockNeighborhood block,
            TileModelView target,
            Color mapColor
    ) {
        de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState raw =
                resourcePack.getBlockStates().get(block.getBlockState().getId());
        if (raw == null) {
            return;
        }
        List<Color> colors = new ArrayList<>();
        raw.forEach(
                block.getBlockState(), block.getX(), block.getY(), block.getZ(),
                variant -> {
                    Color color = new Color().set(0F, 0F, 0F, 0F, true);
                    target.initialize();
                    stock.render(block, variant, target, color);
                    colors.add(color);
                }
        );
        combineColors(mapColor, colors);
    }

    private static void combineColors(Color target, List<Color> colors) {
        if (colors.isEmpty()) {
            return;
        }
        target.set(0F, 0F, 0F, 0F, true);
        for (Color color : colors) {
            target.add(color.premultiplied());
        }
        target.div(colors.size()).straight();
    }
}

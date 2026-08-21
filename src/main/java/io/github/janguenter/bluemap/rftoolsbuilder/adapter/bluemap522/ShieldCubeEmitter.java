/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.TileModel;
import de.bluecolored.bluemap.core.map.hires.TileModelView;
import de.bluecolored.bluemap.core.util.Direction;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.LightData;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;
import io.github.janguenter.bluemap.rftoolsbuilder.profile.RftoolsBuilder705Profile;

import java.util.HashMap;
import java.util.Map;

/** Emits the six client-evidenced cube-boundary shield faces. */
final class ShieldCubeEmitter {

    private static final float MINT_RED = 0x96 / 255F;
    private static final float MINT_GREEN = 0xFF / 255F;
    private static final float MINT_BLUE = 0xC8 / 255F;

    private final TextureGallery textures;

    ShieldCubeEmitter(TextureGallery textures) {
        this.textures = textures;
        resolveMaterials();
    }

    Emission emit(
            BlockNeighborhood block,
            TileModelView target,
            ShieldRenderMode mode
    ) {
        Map<Key, Integer> materials = resolveMaterials();
        Key representative = null;
        int faces = 0;
        for (Direction direction : Direction.values()) {
            if (sameBlockId(block, direction)) {
                continue;
            }
            Key texture = textureFor(mode, direction, block.getX(), block.getY(), block.getZ());
            Integer material = materials.get(texture);
            if (material == null) {
                throw new IllegalStateException("shield texture is unavailable");
            }
            if (direction == Direction.UP || representative == null) {
                representative = texture;
            }
            quad(block, target, direction, material, mode);
            faces++;
        }
        return new Emission(faces, representative);
    }

    private Map<Key, Integer> resolveMaterials() {
        Map<Key, Integer> resolved = new HashMap<>();
        for (Key key : RftoolsBuilder705Profile.REQUIRED_TEXTURES) {
            int material = textures.get(key);
            if (material <= 0) {
                throw new IllegalStateException("shield texture is unavailable: " + key);
            }
            resolved.put(key, material);
        }
        return Map.copyOf(resolved);
    }

    static Key textureFor(
            ShieldRenderMode mode,
            Direction direction,
            int x,
            int y,
            int z
    ) {
        return switch (mode) {
            case SHIELD -> RftoolsBuilder705Profile.SHIELD_TEXTURES.get(
                    direction == Direction.UP || direction == Direction.DOWN
                            ? topDownTextureIndex(x, z)
                            : sideTextureIndex(x, y, z)
            );
            case TRANSP -> RftoolsBuilder705Profile.TRANSPARENT_TEXTURE;
            case SOLID -> RftoolsBuilder705Profile.SOLID_TEXTURE;
            case INVISIBLE, MIMIC -> throw new IllegalArgumentException(
                    "mode does not emit a cube"
            );
        };
    }

    static int topDownTextureIndex(int x, int z) {
        return (z & 1) * 2 + (x & 1);
    }

    static int sideTextureIndex(int x, int y, int z) {
        return (y & 1) * 2 + ((x + z) & 1);
    }

    static boolean cullsAgainst(String ownId, String neighborId) {
        return ownId != null && ownId.equals(neighborId);
    }

    private static boolean sameBlockId(BlockNeighborhood block, Direction direction) {
        String own = block.getBlockState().getId().getFormatted();
        String neighbor = block.getNeighborBlock(
                direction.toVector().getX(),
                direction.toVector().getY(),
                direction.toVector().getZ()
        ).getBlockState().getId().getFormatted();
        return cullsAgainst(own, neighbor);
    }

    private static void quad(
            BlockNeighborhood block,
            TileModelView target,
            Direction direction,
            int material,
            ShieldRenderMode mode
    ) {
        switch (direction) {
            case DOWN -> quad(block, target, direction,
                    0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 1F, 0F, 0F, 1F,
                    material, mode);
            case UP -> quad(block, target, direction,
                    0F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 0F, 0F, 1F, 0F,
                    material, mode);
            case NORTH -> quad(block, target, direction,
                    1F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F,
                    material, mode);
            case SOUTH -> quad(block, target, direction,
                    0F, 0F, 1F, 1F, 0F, 1F, 1F, 1F, 1F, 0F, 1F, 1F,
                    material, mode);
            case WEST -> quad(block, target, direction,
                    0F, 0F, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F, 1F, 0F,
                    material, mode);
            case EAST -> quad(block, target, direction,
                    1F, 0F, 1F, 1F, 0F, 0F, 1F, 1F, 0F, 1F, 1F, 1F,
                    material, mode);
        }
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    private static void quad(
            BlockNeighborhood block,
            TileModelView target,
            Direction direction,
            float ax,
            float ay,
            float az,
            float bx,
            float by,
            float bz,
            float cx,
            float cy,
            float cz,
            float dx,
            float dy,
            float dz,
            int material,
            ShieldRenderMode mode
    ) {
        int start = target.add(2);
        TileModel model = target.getTileModel();
        model.setPositions(start, ax, ay, az, bx, by, bz, cx, cy, cz);
        model.setPositions(start + 1, ax, ay, az, cx, cy, cz, dx, dy, dz);
        model.setUvs(start,
                0F, 0F,
                0F, 1F,
                1F, 1F);
        model.setUvs(start + 1,
                0F, 0F,
                1F, 1F,
                1F, 0F);
        model.setMaterialIndex(start, material);
        model.setMaterialIndex(start + 1, material);
        float red = mode == ShieldRenderMode.SHIELD ? MINT_RED : 1F;
        float green = mode == ShieldRenderMode.SHIELD ? MINT_GREEN : 1F;
        float blue = mode == ShieldRenderMode.SHIELD ? MINT_BLUE : 1F;
        model.setColor(start, red, green, blue);
        model.setColor(start + 1, red, green, blue);
        model.setAOs(start, 1F, 1F, 1F);
        model.setAOs(start + 1, 1F, 1F, 1F);

        LightSample light = sampleLight(block, direction);
        model.setSunlight(start, light.sunlight());
        model.setSunlight(start + 1, light.sunlight());
        model.setBlocklight(start, light.blocklight());
        model.setBlocklight(start + 1, light.blocklight());
    }

    private static LightSample sampleLight(BlockNeighborhood block, Direction direction) {
        LightData own = block.getLightData();
        LightData faced = block.getNeighborBlock(
                direction.toVector().getX(),
                direction.toVector().getY(),
                direction.toVector().getZ()
        ).getLightData();
        return new LightSample(
                Math.max(own.getSkyLight(), faced.getSkyLight()),
                Math.max(own.getBlockLight(), faced.getBlockLight())
        );
    }

    record Emission(int faces, Key representativeTexture) {
    }

    private record LightSample(int sunlight, int blocklight) {
    }
}

/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.adapter.bluemap522;

import de.bluecolored.bluemap.core.util.Direction;
import io.github.janguenter.bluemap.rftoolsbuilder.profile.RftoolsBuilder705Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShieldCubeEmitterTest {

    @Test
    void coordinateParityMatchesTheExactClientContract() {
        assertEquals(0, ShieldCubeEmitter.topDownTextureIndex(0, 0));
        assertEquals(1, ShieldCubeEmitter.topDownTextureIndex(1, 0));
        assertEquals(2, ShieldCubeEmitter.topDownTextureIndex(0, 1));
        assertEquals(3, ShieldCubeEmitter.topDownTextureIndex(1, 1));

        assertEquals(0, ShieldCubeEmitter.sideTextureIndex(0, 0, 0));
        assertEquals(1, ShieldCubeEmitter.sideTextureIndex(1, 0, 0));
        assertEquals(2, ShieldCubeEmitter.sideTextureIndex(0, 1, 0));
        assertEquals(3, ShieldCubeEmitter.sideTextureIndex(1, 1, 0));
    }

    @Test
    void shieldAndFixedModesChooseOnlyInstalledProfileTextures() {
        assertEquals(RftoolsBuilder705Profile.SHIELD_TEXTURES.get(3),
                ShieldCubeEmitter.textureFor(ShieldRenderMode.SHIELD,
                        Direction.UP, 1, 0, 1));
        assertEquals(RftoolsBuilder705Profile.SHIELD_TEXTURES.get(2),
                ShieldCubeEmitter.textureFor(ShieldRenderMode.SHIELD,
                        Direction.NORTH, 0, 1, 0));
        assertEquals(RftoolsBuilder705Profile.TRANSPARENT_TEXTURE,
                ShieldCubeEmitter.textureFor(ShieldRenderMode.TRANSP,
                        Direction.EAST, 9, 9, 9));
        assertEquals(RftoolsBuilder705Profile.SOLID_TEXTURE,
                ShieldCubeEmitter.textureFor(ShieldRenderMode.SOLID,
                        Direction.DOWN, 9, 9, 9));
    }

    @Test
    void internalFaceCullingRequiresTheSameExactBlockId() {
        assertTrue(ShieldCubeEmitter.cullsAgainst(
                "rftoolsbuilder:shielding_solid",
                "rftoolsbuilder:shielding_solid"
        ));
        assertFalse(ShieldCubeEmitter.cullsAgainst(
                "rftoolsbuilder:shielding_solid",
                "rftoolsbuilder:shielding_translucent"
        ));
        assertFalse(ShieldCubeEmitter.cullsAgainst(
                "rftoolsbuilder:shielding_solid",
                "minecraft:stone"
        ));
    }
}

/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.adapter.bluemap522;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShieldRenderModeTest {

    @Test
    void parsesExactPersistedNamesAndRejectsUnknownData() {
        assertEquals(ShieldRenderMode.SHIELD, ShieldRenderMode.parse("shield").orElseThrow());
        assertEquals(ShieldRenderMode.TRANSP, ShieldRenderMode.parse("transp").orElseThrow());
        assertEquals(ShieldRenderMode.SOLID, ShieldRenderMode.parse("solid").orElseThrow());
        assertEquals(ShieldRenderMode.INVISIBLE,
                ShieldRenderMode.parse("invisible").orElseThrow());
        assertTrue(ShieldRenderMode.parse("unexpected").isEmpty());
        assertTrue(ShieldRenderMode.parse(null).isEmpty());
    }

    @Test
    void mimicIsTheOnlyKnownExcludedMode() {
        assertFalse(ShieldRenderMode.MIMIC.supported());
        assertTrue(ShieldRenderMode.SHIELD.supported());
        assertTrue(ShieldRenderMode.TRANSP.supported());
        assertTrue(ShieldRenderMode.SOLID.supported());
        assertTrue(ShieldRenderMode.INVISIBLE.supported());
    }

    @Test
    void everyVisibleFixedModeUsesTheNaturalProjectorTint() {
        assertEquals(0x96ffc8, ShieldRenderMode.SHIELD.naturalDefaultTint());
        assertEquals(0x96ffc8, ShieldRenderMode.TRANSP.naturalDefaultTint());
        assertEquals(0x96ffc8, ShieldRenderMode.SOLID.naturalDefaultTint());
    }
}

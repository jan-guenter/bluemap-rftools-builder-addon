/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.adapter.bluemap522;

import java.util.Locale;
import java.util.Optional;

/** Persisted shield render property values in the exact profile. */
enum ShieldRenderMode {
    INVISIBLE,
    SHIELD,
    MIMIC,
    TRANSP,
    SOLID;

    static Optional<ShieldRenderMode> parse(String value) {
        if (value == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(value.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    boolean supported() {
        return this != MIMIC;
    }
}

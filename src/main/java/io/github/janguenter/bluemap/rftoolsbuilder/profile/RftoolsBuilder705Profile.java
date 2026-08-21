/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.profile;

import de.bluecolored.bluemap.core.util.Key;

import java.util.List;
import java.util.Set;

/** Exact All the Mons 1.2.0 profile for RFToolsBuilder 1.21-7.0.5. */
public final class RftoolsBuilder705Profile {

    public static final String PROFILE_ID = "rftoolsbuilder-1.21-7.0.5";
    public static final String MOD_ID = "rftoolsbuilder";
    public static final String VERSION = "1.21-7.0.5";
    public static final String ARTIFACT = "rftoolsbuilder-1.21-7.0.5.jar";
    public static final long JAR_SIZE = 1_054_852L;
    public static final String JAR_SHA1 =
            "da588b53a1ee9ab55b38b94d0a24e63367a558a4";
    public static final String JAR_SHA256 =
            "802cea7c7eb5d6a25d820113bdd9f59fe0c15b390265b4964a46dcf024cdffa6";
    public static final String SOURCE_COMMIT =
            "b95e14835fa4378c1b13728be398c6c0de0b87ea";
    public static final String SOURCE_TREE =
            "8d9a78836bdf59a85d2a80a5d9e4db53661ec6cd";

    public static final Set<String> HOST_IDS = Set.of(
            "rftoolsbuilder:shielding_solid",
            "rftoolsbuilder:shielding_translucent",
            "rftoolsbuilder:shielding_cutout"
    );
    public static final List<Key> SHIELD_TEXTURES = List.of(
            Key.parse("rftoolsbuilder:block/shield/shield0"),
            Key.parse("rftoolsbuilder:block/shield/shield1"),
            Key.parse("rftoolsbuilder:block/shield/shield2"),
            Key.parse("rftoolsbuilder:block/shield/shield3")
    );
    public static final Key TRANSPARENT_TEXTURE =
            Key.parse("rftoolsbuilder:block/shield/shieldtransparent");
    public static final Key SOLID_TEXTURE =
            Key.parse("rftoolsbuilder:block/shield/shieldfull");
    public static final Set<Key> REQUIRED_TEXTURES = Set.of(
            SHIELD_TEXTURES.get(0),
            SHIELD_TEXTURES.get(1),
            SHIELD_TEXTURES.get(2),
            SHIELD_TEXTURES.get(3),
            TRANSPARENT_TEXTURE,
            SOLID_TEXTURE
    );

    private RftoolsBuilder705Profile() {
    }

    public static boolean owns(String blockId) {
        return HOST_IDS.contains(blockId);
    }
}

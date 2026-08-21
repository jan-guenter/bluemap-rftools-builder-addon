/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsbuilder.profile;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RftoolsBuilder705ProfileTest {

    @Test
    void ownsOnlyTheThreeShieldingHosts() {
        assertTrue(RftoolsBuilder705Profile.owns("rftoolsbuilder:shielding_solid"));
        assertTrue(RftoolsBuilder705Profile.owns("rftoolsbuilder:shielding_translucent"));
        assertTrue(RftoolsBuilder705Profile.owns("rftoolsbuilder:shielding_cutout"));
        assertFalse(RftoolsBuilder705Profile.owns("rftoolsbuilder:shield_block1"));
        assertFalse(RftoolsBuilder705Profile.owns("minecraft:stone"));
    }

    @Test
    void exactArtifactDetectorAcceptsOnlyThePinnedDeclaredJar() {
        String configured = System.getProperty("rftoolsBuilderJar");
        assertTrue(configured != null && !configured.isBlank());
        Path jar = Path.of(configured);
        assertTrue(Files.isRegularFile(jar));
        assertTrue(ExactRftoolsBuilderArtifactDetector.matches(
                List.of(jar),
                RftoolsBuilder705Profile.JAR_SHA256,
                RftoolsBuilder705Profile.JAR_SIZE
        ));
        assertFalse(ExactRftoolsBuilderArtifactDetector.matches(
                List.of(Path.of("missing-rftoolsbuilder.jar")),
                RftoolsBuilder705Profile.JAR_SHA256,
                RftoolsBuilder705Profile.JAR_SIZE
        ));
        assertThrows(IllegalArgumentException.class,
                () -> ExactRftoolsBuilderArtifactDetector.matches(
                        List.of(jar), "0".repeat(64), RftoolsBuilder705Profile.JAR_SIZE
                ));
    }
}

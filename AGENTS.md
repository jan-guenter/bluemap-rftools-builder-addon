# Agent guide for BlueMap RFTools Builder Add-on

Read `/root/work/allthemons/AGENTS.md` and this file before changing this
standalone repository.

## Exact baseline

| Component | Identity |
| --- | --- |
| All the Mons | `1.2.0`, commit `c7bb230f21d14d26859d0b92548f089b3a493ad9` |
| Minecraft / NeoForge / Java | `1.21.1` / `21.1.248` / `21` |
| BlueMap | `5.22-feature.backport-5.23-stateless-java-web-server-46`, commit `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac`, API `285c9a60eff3ac2b0cab308ce1058d1565be0971` |
| Adapter API | `0.1.0-alpha.2`, commit `e81f08bc4bfbf02d810ec8949a019130e2e61634`, source tree `2f974c9bb2ba13888d69682f86f30f58922d30eb` |
| RFToolsBuilder | `rftoolsbuilder-1.21-7.0.5.jar`, 1,054,852 bytes, SHA-256 `802cea7c7eb5d6a25d820113bdd9f59fe0c15b390265b4964a46dcf024cdffa6` |

## Alpha boundary

- Own only `shielding_solid`, `shielding_translucent`, and `shielding_cutout`.
- Support only persisted `render=invisible|shield|transp|solid`.
- `shield` uses the installed default shield texture quartet and coordinate
  parity. `shield`, `transp`, and `solid` all use the natural default projector
  tint `0x96ffc8`; the latter two use their installed fixed textures. Internal
  faces are removed only between the same exact block ID.
- `mimic`, unknown/malformed states, custom projector colors/textures, stripe
  mode, and orphan-projector white fallback remain stock in this alpha.
- Package no RFToolsBuilder asset, class, source, archive, or derived mesh.

## Minimum gate

Use the exact promotion Gradle `9.6.1`; other Gradle versions can change the
published module metadata even when the JARs remain byte-identical:

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-adapter-api
gradle --no-daemon \
  -PbluemapSourcePath=/absolute/path/to/BlueMap-at-7e07f4e7 \
  -PrftoolsBuilderJar=/absolute/path/rftoolsbuilder-1.21-7.0.5.jar \
  -PreleaseTag=v0.1.0-alpha.2 \
  clean check build generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication verifyPublicationArtifacts \
  verifyReleaseCandidate
```

Do not claim natural-fixture behavior, client parity, publication, or
deployment unless that exact gate runs. Release promotion also requires the
independent audit, hosted CI, exact annotated tag, and publication checks in
`docs/RELEASING.md`.

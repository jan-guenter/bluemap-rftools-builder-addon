# BlueMap RFTools Builder Add-on

[![CI](https://github.com/jan-guenter/bluemap-rftools-builder-addon/actions/workflows/ci.yml/badge.svg)](https://github.com/jan-guenter/bluemap-rftools-builder-addon/actions/workflows/ci.yml)

A narrow Java 21 add-on for the exact BlueMap 5.23 feature backport and the
dynamic RFToolsBuilder shield blocks that BlueMap's ordinary resource-model
path cannot interpret.

Version `0.1.0-alpha.2` is the owner-accepted BlueMap 5.23 release candidate.
Its production JAR is 70,037 bytes with SHA-256
`3302fbf77a92cc0643e04dfe7981816a3699b0da00f3993360b6b76ca389c09b`.
It preserves the owner-accepted alpha.1 renderer, profile, gallery, and
fallback behavior.

The add-on activates only with the exact All the Mons 1.2.0
`rftoolsbuilder-1.21-7.0.5.jar` (1,054,852 bytes, SHA-256
`802cea7c7eb5d6a25d820113bdd9f59fe0c15b390265b4964a46dcf024cdffa6`).
It redirects exactly the solid, translucent, and cutout shielding block IDs to
a custom block renderer. The renderer emits full cube-boundary faces, removes
an internal face only when the neighboring block has the same registry ID,
and reads all textures from the operator-installed mod.

The alpha supports `invisible`, `shield`, `transp`, and `solid`. Default
`shield` uses the client-evidenced coordinate parity texture selection.
`shield`, `transp`, and `solid` all use the natural default-projector mint tint
`0x96ffc8`; the latter two use the installed fixed textures. Damage and blocking
properties do not affect visual output.

`mimic`, malformed or unknown modes, custom projector colors, stripe texture,
and orphan-projector white fallback deliberately use BlueMap's stock result in
this first candidate. No RFToolsBuilder texture, model, class, source, binary,
or captured mesh is packaged.

## Build

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

The output JAR belongs in BlueMap's `packs` directory. Removing it and
restarting restores stock rendering without changing world data.

The exact BlueMap checkout is commit
`7e07f4e74ec1e92a6ead9aa1e66054af3e133aac` with API commit
`285c9a60eff3ac2b0cab308ce1058d1565be0971`. The Adapter API source module is
pinned at commit `e81f08bc4bfbf02d810ec8949a019130e2e61634`, source tree
`2f974c9bb2ba13888d69682f86f30f58922d30eb`; exactly four helpers are compiled
as source and no module JAR is installed, bundled, or nested.

The intended immutable tag is `v0.1.0-alpha.2`, and the Maven coordinate is
`io.github.jan-guenter:bluemap-rftools-builder-addon:0.1.0-alpha.2`.
Publication is allowed only after the independently audited pull request and
its final-head CI pass. See [the release procedure](docs/RELEASING.md) and
[recorded candidate provenance](provenance/release.json).

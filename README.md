# BlueMap RFTools Builder Add-on

[![CI](https://github.com/jan-guenter/bluemap-rftools-builder-addon/actions/workflows/ci.yml/badge.svg)](https://github.com/jan-guenter/bluemap-rftools-builder-addon/actions/workflows/ci.yml)

A narrow Java 21 BlueMap 5.22 add-on for the dynamic RFToolsBuilder shield
blocks that BlueMap's ordinary resource-model path cannot interpret.

Version `0.1.0-alpha.1` is the owner-accepted release candidate. Its final
production JAR is 65,006 bytes with SHA-256
`776d4242bf6826a8aafe081aca5b32f242bc0af5677efccbdd3469b65ba7cfe6`.

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
gradle --no-daemon \
  -PrftoolsBuilderJar=/absolute/path/rftoolsbuilder-1.21-7.0.5.jar \
  -PreleaseTag=v0.1.0-alpha.1 \
  clean check build generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication verifyPublicationArtifacts \
  verifyReleaseCandidate
```

The output JAR belongs in BlueMap's `packs` directory. Removing it and
restarting restores stock rendering without changing world data.

The intended immutable tag is `v0.1.0-alpha.1`, and the Maven coordinate is
`io.github.jan-guenter:bluemap-rftools-builder-addon:0.1.0-alpha.1`.
Publication is allowed only after the independently audited pull request and
its final-head CI pass. See [the release procedure](docs/RELEASING.md) and
[recorded candidate provenance](provenance/release.json).

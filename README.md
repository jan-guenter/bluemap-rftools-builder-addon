# BlueMap RFTools Builder Add-on

A narrow Java 21 BlueMap 5.22 add-on for the dynamic RFToolsBuilder shield
blocks that BlueMap's ordinary resource-model path cannot interpret.

This prototype activates only with the exact All the Mons 1.2.0
`rftoolsbuilder-1.21-7.0.5.jar` (1,054,852 bytes, SHA-256
`802cea7c7eb5d6a25d820113bdd9f59fe0c15b390265b4964a46dcf024cdffa6`).
It redirects exactly the solid, translucent, and cutout shielding block IDs to
a custom block renderer. The renderer emits full cube-boundary faces, removes
an internal face only when the neighboring block has the same registry ID,
and reads all textures from the operator-installed mod.

The alpha supports `invisible`, `shield`, `transp`, and `solid`. Default
`shield` uses the client-evidenced coordinate parity texture selection and the
natural default-projector mint tint `0x96ffc8`. `transp` and `solid` use the
installed fixed textures. Damage and blocking properties do not affect visual
output.

`mimic`, malformed or unknown modes, custom projector colors, stripe texture,
and orphan-projector white fallback deliberately use BlueMap's stock result in
this first candidate. No RFToolsBuilder texture, model, class, source, binary,
or captured mesh is packaged.

## Build

```bash
../bluemap-backport/gradlew --no-daemon \
  -PrftoolsBuilderJar=/absolute/path/rftoolsbuilder-1.21-7.0.5.jar \
  clean check build
```

The output JAR belongs in BlueMap's `packs` directory. Removing it and
restarting restores stock rendering without changing world data.

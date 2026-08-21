# RFToolsBuilder staging gallery

This directory defines the deliberately tiny deterministic datapack used to
review the exact RFToolsBuilder `1.21-7.0.5` BlueMap prototype. The exact
operator-installed runtime JAR is 1,054,852 bytes with SHA-1
`da588b53a1ee9ab55b38b94d0a24e63367a558a4` and SHA-256
`802cea7c7eb5d6a25d820113bdd9f59fe0c15b390265b4964a46dcf024cdffa6`.

The gallery is synthetic prototype evidence. It directly places exact block
states and the narrow persisted projector-coordinate NBT used by the client;
it does not claim that a player composed these shield cells. The fixture is
confined to inclusive x `160..183`, y `99..104`, z `160..183` in a disposable
staging world. Its smooth-stone pad occupies x `160..180`, y `99`,
z `160..181`.

## Cells

| Section | Fixture | Exact coordinates | Count | Purpose |
| --- | --- | --- | ---: | --- |
| A | stock `shield_block1` | `164 100 164` | 1 | ordinary cube-model control and real default render-data source |
| A | stock blue shield template | `168 100 164` | 1 | ordinary cutout-model control |
| A | stock bricks | `172 100 164` | 1 | ordinary Minecraft-model control |
| B | isolated SHIELD | `164 100 171` | 1 | custom loader, default shield texture and default mint tint |
| B | isolated TRANSP | `168 100 171` | 1 | fixed transparent shield texture |
| B | isolated SOLID | `172 100 171` | 1 | fixed solid shield texture |
| B | invisible control | `176 100 171` | 1 | intentional zero geometry |
| C | horizontal 2x2 SHIELD cluster | x `164..165`, y `100`, z `178..179` | 4 | all top parity tiles plus same-ID internal-face culling |

All eight shielding block entities point to the real default projector at
`164 100 164` through `sx`, `sy`, and `sz`. Its exact default SHIELD color is
the mint RGB value `0x96ffc8`; the prototype uses the normal `shield` texture.
Custom colors, the `stripes` texture, orphan-projector white fallback, MIMIC,
and the naturally unselected `shielding_cutout` registry are outside this
first alpha gallery. Damage/collision bits remain false and `opaque` remains
true because those fields do not alter the selected static geometry.

Without the add-on, BlueMap has only the loader-only
`rftoolsbuilder:block/shielding` model, which has no vanilla elements, so the
shielding cells have no stock geometry while the three ordinary controls
remain visible. With the add-on, compare this same fixture in the exact modded
client and BlueMap: the three visible isolated modes and the 2x2 cluster must
appear, the three controls must stay stock, and the invisible cell must remain
empty. Animation phase is not a comparison target.

## Generate, lint, and package

Run from the repository root:

```text
PYTHONDONTWRITEBYTECODE=1 python3 gallery/generate.py --check
PYTHONDONTWRITEBYTECODE=1 python3 gallery/lint.py
bash gallery/package.sh /tmp/bluemap-rftools-builder-gallery.zip
```

Running `gallery/generate.py` without `--check` rewrites only the generated
ledger, datapack files, and `SHA256SUMS`. Packaging verifies those files and
creates a ZIP from sorted paths with fixed modes, stripped metadata, and a
fixed DOS epoch. It bundles no RFToolsBuilder, Minecraft, or BlueMap code,
models, or textures.

## Staging functions

```text
/function rftools_builder_gallery:build
/function rftools_builder_gallery:verify
/function rftools_builder_gallery:clear
/function rftools_builder_gallery:release
```

`build` increments a persistent one-build counter, clears the bounded
envelope, places the pad and eleven asserted blocks, verifies immediately,
and repeats the same compact check after 20 ticks. Each phase checks eleven
block states, eight shielding block-entity projector pointers, and the one
build counter. Require:

```text
#immediate_checked = 20   #immediate_failures = 0
#20t_checked       = 20   #20t_failures       = 0
```

`release` cancels the delayed check and removes only this gallery's forceload
ticket; it deliberately retains the fixture for rendering. To deliberately
rebuild in the disposable world after `clear`, reset `#builds` in objective
`rftb_gallery` to zero first.

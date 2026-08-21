# Agent guide for BlueMap RFTools Builder Add-on

Read `/root/work/allthemons/AGENTS.md` and this file before changing this
standalone repository.

## Exact baseline

| Component | Identity |
| --- | --- |
| All the Mons | `1.2.0`, commit `c7bb230f21d14d26859d0b92548f089b3a493ad9` |
| Minecraft / NeoForge / Java | `1.21.1` / `21.1.248` / `21` |
| BlueMap | `5.22-agent.backport-5.22-mc1.21.1-2`, commit `9be321df995a1103808621d529eb72773e719d4d` |
| RFToolsBuilder | `rftoolsbuilder-1.21-7.0.5.jar`, 1,054,852 bytes, SHA-256 `802cea7c7eb5d6a25d820113bdd9f59fe0c15b390265b4964a46dcf024cdffa6` |

## Alpha boundary

- Own only `shielding_solid`, `shielding_translucent`, and `shielding_cutout`.
- Support only persisted `render=invisible|shield|transp|solid`.
- `shield` uses the installed default shield texture quartet, coordinate parity,
  and the natural default projector tint `0x96ffc8`.
- `transp` and `solid` use their installed fixed textures. Internal faces are
  removed only between the same exact block ID.
- `mimic`, unknown/malformed states, custom projector colors/textures, stripe
  mode, and orphan-projector white fallback remain stock in this alpha.
- Package no RFToolsBuilder asset, class, source, archive, or derived mesh.

## Gate

The system Gradle package may be unusable; the pinned BlueMap wrapper is an
accepted launcher for this project:

```bash
../bluemap-backport/gradlew --no-daemon \
  -PrftoolsBuilderJar=/absolute/path/rftoolsbuilder-1.21-7.0.5.jar \
  clean check build
```

Do not claim staging, client parity, owner acceptance, release, or deployment
unless that exact action has been observed.

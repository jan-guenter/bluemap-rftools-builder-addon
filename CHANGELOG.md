# Changelog

## 0.1.0-alpha.2 - 2026-09-01

- Migrate the adapter boundary to exact BlueMap feature-backport commit
  `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac` and API commit
  `285c9a60eff3ac2b0cab308ce1058d1565be0971`.
- Compile the four Adapter API `0.1.0-alpha.2` helpers from exact gitlink
  `e81f08bc4bfbf02d810ec8949a019130e2e61634`; remove the displaced local
  compatibility, registry, extension-type, and dispatch helpers.
- Rename the local adapter package from `bluemap522` to `bluemap523` while
  preserving the accepted shielding renderer, profile, gallery, and fallback
  behavior.

## 0.1.0-alpha.1 - 2026-08-21

- Add an exact-artifact BlueMap renderer for RFToolsBuilder shielding blocks.
- Support persisted `invisible`, `shield`, `transp`, and `solid` modes while
  leaving `mimic` and malformed or deferred render data on BlueMap's stock path.
- Emit six cube-boundary faces with exact client UV order and installed texture
  selection; remove an internal face only for the same shielding block ID.
- Apply the natural default-projector mint tint `0x96ffc8` to all three visible
  modes and preserve installed texture alpha through BlueMap's texture gallery.
- Add focused Java 21 tests, strict artifact/resource validation, atomic stock
  fallback, deterministic staging gallery evidence, and release-byte gates.
- Store release JAR entries without compression and exclude empty directories
  so exact archive bytes remain reproducible across accepted build hosts.

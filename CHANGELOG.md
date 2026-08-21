# Changelog

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

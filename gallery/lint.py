#!/usr/bin/env python3
# SPDX-License-Identifier: MIT
"""Lint the generated RFToolsBuilder gallery without starting Minecraft."""

from __future__ import annotations

from collections import Counter
import json
from pathlib import Path
import re
import sys

sys.dont_write_bytecode = True
import generate


ROOT = Path(__file__).resolve().parent


def fail(message: str) -> None:
    raise ValueError(message)


def main() -> int:
    expected = generate.generated_files()
    for relative, payload in expected.items():
        path = ROOT / relative
        if not path.is_file() or path.read_bytes() != payload:
            fail(f"generated file differs: {relative}")

    json.loads((ROOT / "datapack/pack.mcmeta").read_text(encoding="utf-8"))
    json.loads(
        (ROOT / "datapack/data/minecraft/tags/function/load.json").read_text(
            encoding="utf-8"
        )
    )

    function_root = ROOT / f"datapack/data/{generate.NAMESPACE}/function"
    build = (function_root / "build.mcfunction").read_text(encoding="utf-8")
    clear = (function_root / "clear.mcfunction").read_text(encoding="utf-8")
    verify = (function_root / "verify.mcfunction").read_text(encoding="utf-8")
    all_functions = "\n".join(
        path.read_text(encoding="utf-8")
        for path in sorted(function_root.glob("*.mcfunction"))
    )

    if len(generate.PLACEMENTS) != 11:
        fail("gallery must define exactly eleven target placements")
    if len(re.findall(r"^setblock ", build, re.MULTILINE)) != 11:
        fail("build must contain exactly eleven setblock placements")
    if len(re.findall(r"^scoreboard players add #checked ", verify, re.MULTILINE)) != 20:
        fail("verify must contain eleven state, eight NBT, and one counter check")
    if build.count(
        f"scoreboard players add #builds {generate.OBJECTIVE} 1"
    ) != 1:
        fail("exactly one persistent build-counter increment is required")

    block_ids = Counter(
        placement.block_state.split("[", 1)[0]
        for placement in generate.PLACEMENTS
    )
    expected_ids = Counter(
        {
            "rftoolsbuilder:shield_block1": 1,
            "rftoolsbuilder:blue_shield_template_block": 1,
            "minecraft:bricks": 1,
            "rftoolsbuilder:shielding_translucent": 7,
            "rftoolsbuilder:shielding_solid": 1,
        }
    )
    if block_ids != expected_ids:
        fail(f"unexpected target block census: {block_ids}")

    render_modes = Counter()
    for placement in generate.PLACEMENTS:
        match = re.search(r"(?:^|,)render=([^,\]]+)", placement.block_state)
        if match:
            render_modes[match.group(1)] += 1
    if render_modes != Counter(
        {"shield": 5, "transp": 1, "solid": 1, "invisible": 1}
    ):
        fail(f"unexpected render-mode census: {render_modes}")

    coordinates = [
        (placement.x, placement.y, placement.z)
        for placement in generate.PLACEMENTS
    ]
    if len(set(coordinates)) != len(coordinates):
        fail("target coordinates must be unique")
    envelope = generate.ENVELOPE
    for coordinate in coordinates:
        x, y, z = coordinate
        if not (
            envelope["min_x"] <= x <= envelope["max_x"]
            and envelope["min_y"] <= y <= envelope["max_y"]
            and envelope["min_z"] <= z <= envelope["max_z"]
        ):
            fail(f"placement escaped safe envelope: {coordinate}")

    nbt_placements = [
        placement
        for placement in generate.PLACEMENTS
        if placement.nbt is not None
    ]
    if len(nbt_placements) != 8:
        fail("exactly eight shielding block entities require projector NBT")
    if any(
        placement.nbt != generate.PROJECTOR_NBT
        for placement in nbt_placements
    ):
        fail("every shielding block must point to the one default projector")
    projector_rows = [
        placement
        for placement in generate.PLACEMENTS
        if (placement.x, placement.y, placement.z) == generate.PROJECTOR
    ]
    if len(projector_rows) != 1 or projector_rows[0].block_state != (
        "rftoolsbuilder:shield_block1"
    ):
        fail("the exact projector pointer target must be the stock control")

    cluster = {
        (placement.x, placement.y, placement.z)
        for placement in generate.PLACEMENTS
        if placement.section == "C"
    }
    expected_cluster = {
        (164, 100, 178),
        (165, 100, 178),
        (164, 100, 179),
        (165, 100, 179),
    }
    if cluster != expected_cluster:
        fail("section C must be the exact horizontal 2x2 cluster")
    cluster_rows = [
        placement
        for placement in generate.PLACEMENTS
        if placement.section == "C"
    ]
    if any(placement.block_state != generate.SHIELD for placement in cluster_rows):
        fail("every cluster member must use the same translucent SHIELD ID/state")
    top_parities = {
        (placement.z & 1) * 2 + (placement.x & 1)
        for placement in cluster_rows
    }
    if top_parities != {0, 1, 2, 3}:
        fail("the 2x2 cluster must exercise all four top parity tiles")

    expected_clear = "fill 160 99 160 183 104 183 minecraft:air"
    clear_commands = re.findall(r"^fill .* minecraft:air$", clear, re.MULTILINE)
    if clear_commands != [expected_clear]:
        fail("clear must cover exactly the bounded legal-volume envelope")

    lowered = all_functions.lower()
    for token in (
        "render=mimic",
        "shielding_cutout",
        "shieldstripes",
        "mimic:",
        "summon ",
        "data merge",
    ):
        if token in lowered:
            fail(f"forbidden first-alpha gallery path: {token}")
    if build.count(generate.PROJECTOR_NBT) != 8:
        fail("build must persist exactly eight projector pointers")
    if verify.count(generate.PROJECTOR_NBT) != 8:
        fail("verify must check exactly eight projector pointers")
    if build.count(
        f"schedule function {generate.NAMESPACE}:verify_20t 20t replace"
    ) != 1:
        fail("build must schedule exactly one retained 20-tick check")

    print(
        "RFToolsBuilder gallery lint passed: 11 placements, "
        "8 projector pointers, 20 checks/phase"
    )
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except ValueError as error:
        print(f"lint failed: {error}", file=sys.stderr)
        raise SystemExit(1)

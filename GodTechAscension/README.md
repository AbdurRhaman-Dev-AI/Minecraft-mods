# GodTech: Ascension

**Mod ID:** `godtechascension`  
**Version:** `1.0.0` (Milestone 1 — Foundation)  
**Target:** Minecraft `1.21.1` — NeoForge `21.1.x` (Java 21)

An endgame industrial progression mod designed to sit alongside **Create**, **TFMG**, **Create Diesel Generators**, **Create Nuclear**, **PowerGrid**, **Create Aeronautics**, **JEI**, and **GodTrades5050**.

All items follow **Create-style naming** and register **JEI Info & Recipe entries** so you can search `@godtechascension` (or any item name like `Titanium` or `God`) in JEI and press `R` (Recipe) or `U` (Usage) to see how every item is crafted and used.

---

## Features (Milestone 1)

- **Titanium Ore Worldgen:** Spawns `Titanium Ore` and `Deepslate Titanium Ore` underground (`Y = -64` to `32`, Fortune-compatible).
- **Create-Style Processing Chain:** `Raw Titanium` → `Crushed Raw Titanium` → `Titanium Ingot` → `Titanium Sheet` → `Titanium Alloy` / `Titanium Casing`.
- **High-Tech Components:** `God Crystal`, `Void Shard`, `Reinforced Mechanism`, `Quantum Circuit`, and `God Core`.
- **Endgame God Tools:**
  - **God Pickaxe:** High-speed mining with **`3×3` area mining** (hold `Shift`/sneak to mine `1×1`).
  - **God Sword:** High damage with a **Right-Click AoE Energy Slash** (`3`-second cooldown) that damages nearby hostile mobs.
- **Full JEI Integration:** Every item has an **Info** tab in JEI explaining how to obtain and use it, plus all crafting, smelting, and blasting recipes.
- **Hold `[W]` for Guide:** Hover over any `GodTech` item or block (in your inventory, Creative tab, or JEI) and hold `W` to open a Create-style popup panel showing how to obtain it, how to use it, and clickable links to related progression items.

---

## Items, Recipes & Usage

| Item / Block | How to Obtain / Recipe | How to Use |
| :--- | :--- | :--- |
| **Titanium Ore** & **Deepslate Titanium Ore** | Mines underground (`Y = -64` to `32`) with Iron+ Pickaxe | Drops `Raw Titanium` (affected by Fortune). |
| **Raw Titanium** | Mined from Titanium Ore | Smelt/Blast into `Titanium Ingot`, or crush into `Crushed Raw Titanium`. |
| **Crushed Raw Titanium** | `Raw Titanium` + `Flint` (shapeless) or Create Crushing | Smelt/Blast into `Titanium Ingot` for clean industrial processing. |
| **Titanium Ingot** | Smelt/Blast `Raw Titanium` or `Crushed Raw Titanium` | Craft into `Titanium Sheet` and `Titanium Alloy`. |
| **Titanium Sheet** | `2×2` `Titanium Ingot` → `4× Titanium Sheet` | Used for `Titanium Casing` and `Reinforced Mechanism`. |
| **Titanium Alloy** | `4× Iron Ingot` + `2× Titanium Ingot` + `2× Copper Ingot` → `2× Titanium Alloy` | Core alloy for `Quantum Circuit` and `God Pickaxe`/`God Sword`. |
| **Titanium Casing** | `4× Titanium Sheet` + `4× Iron Ingot` + `1× Redstone` | Structural casing block for industrial builds & upcoming machines. |
| **God Crystal** | `1× Diamond` + `4× Amethyst Shard` + `2× Blaze Powder` + `2× Redstone` | Primary energy crystal for `Quantum Circuit` and `God Core`. |
| **Void Shard** | `4× Obsidian` + `4× Ender Pearl` + `1× Amethyst Shard` | Void component used in `God Core` and endgame tools. |
| **Reinforced Mechanism** | `4× Titanium Sheet` + `2× Gold Ingot` + `2× Copper Ingot` + `1× Redstone` | Mechanical core for `God Pickaxe` and future machines. |
| **Quantum Circuit** | `4× Redstone` + `2× Gold Ingot` + `2× Titanium Alloy` + `1× God Crystal` | High-tier electronics for `God Core`. |
| **God Core** | `4× God Crystal` + `4× Quantum Circuit` + `1× Nether Star` | Ultimate crafting core for `God Pickaxe` and `God Sword`. |
| **God Pickaxe** | `3× Titanium Alloy` + `1× God Core` + `1× Reinforced Mechanism` + `2× Void Shard` | Mines `3×3` blocks at once (sneak for `1×1`). |
| **God Sword** | `2× Titanium Alloy` + `1× God Core` + `1× Void Shard` + `1× Blaze Rod` | High-damage blade; right-click unleashes an AoE shockwave (`3s` cooldown). |

---

## Media & Visuals

- Item icons: [images/items/](./images/items)
- Recipe infographics: [images/recipes/](./images/recipes)

---

## Installation (Multiplayer & SKlauncher)

1. Download [godtechascension-1.0.0.jar](./godtechascension-1.0.0.jar).
2. Place it in **both** your dedicated server's `mods/` folder **and** you & your friend's SKlauncher `mods/` folders (NeoForge `1.21.1`).
3. Launch the game and search `@godtechascension` in **JEI** to view every item, recipe, and usage tab.

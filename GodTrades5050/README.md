# GodTrades5050

**Mod ID:** `godtrades5050`  
**Version:** `1.0.0`  
**Target:** Minecraft `1.21.1` — NeoForge `21.1.x` (Java 21)

Every villager profession has a strict **50/50 chance** (`random.nextBoolean()`) per trade slot across levels `1` through `5` to offer a **God-level trade** for **`1` Emerald** instead of its regular vanilla/modded trade.

## How It Works

- Hooks into NeoForge's `VillagerTradesEvent` on `NeoForge.EVENT_BUS` for **all** `VillagerProfession` types (all vanilla professions plus any modded professions).
- Wraps every registered `VillagerTrades.ItemListing` across levels `1–5` inside `FiftyFiftyGodListing`.
- When a villager generates an offer:
  - **50% False:** Returns the original vanilla/modded `MerchantOffer` untouched.
  - **50% True:** Returns a God-level `MerchantOffer` (`1` Emerald cost, `64` max uses, `30` villager XP, `0.05f` price multiplier) built using Minecraft `1.21.1` Data Components (`DataComponents.ENCHANTMENTS` / `DataComponents.STORED_ENCHANTMENTS`).

## God-Level Trade Pools

- **Armorer:** Maxed Netherite Armor (`Protection IV`, `Unbreaking III`, `Mending`, `Thorns III`, plus `Respiration III` + `Aqua Affinity` on Helmets and `Feather Falling IV` + `Depth Strider III` on Boots)
- **Weaponsmith:** Maxed Netherite Sword (`Sharpness V`, `Looting III`, `Unbreaking III`, `Fire Aspect II`, `Sweeping Edge III`, `Mending`) and Netherite Axe (`Sharpness V`, `Efficiency V`, `Unbreaking III`, `Mending`)
- **Toolsmith:** Maxed Netherite Pickaxe (`Efficiency V`, `Fortune III`, `Unbreaking III`, `Mending`), Silk Touch Pickaxe, and Silk Touch Shovel
- **Fletcher:** Maxed Bow (`Power V`, `Punch II`, `Flame`, `Infinity`, `Unbreaking III`) and Crossbow (`Multishot`, `Quick Charge III`, `Unbreaking III`, `Mending`)
- **Librarian:** Max-level Enchanted Books (`Mending`, `Sharpness V`, `Protection IV`, `Efficiency V`, `Fortune III`, `Unbreaking III`, `Looting III`)
- **Cleric / Farmer / Other & Modded Professions:** `16` Enchanted Golden Apples, `1` Nether Star, `1` Totem of Undying, `1` Beacon, `1` Elytra (`Unbreaking III`, `Mending`), or `64` Bottles o' Enchanting

## Installation

1. Download [godtrades5050-1.0.0.jar](./godtrades5050-1.0.0.jar).
2. Drop it into your **NeoForge 1.21.1** server's `mods/` folder (and/or your SKlauncher `mods/` folder for singleplayer).
3. **Server-side friendly:** Because `VillagerTradesEvent` runs on the logical server, players joining a dedicated server do not need the mod installed on their client.
4. **Testing tip:** Villager trades are rolled when first opening a villager's trade UI or leveling up. Spawn a fresh villager or reset a `0`-XP Novice villager's workstation to see the 50/50 split.

## Building from Source

```bash
cd GodTrades5050
./gradlew build
```

The compiled JAR is output to `GodTrades5050/build/libs/godtrades5050-1.0.0.jar`.

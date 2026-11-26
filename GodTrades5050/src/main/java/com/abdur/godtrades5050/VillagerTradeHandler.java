package com.abdur.godtrades5050;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Wraps all villager trade listings (levels 1-5) across every profession so each
 * trade slot has a strict 50/50 chance to roll a God-level trade for 1 Emerald.
 */
public final class VillagerTradeHandler {

    private VillagerTradeHandler() {
    }

    public static void onVillagerTrades(VillagerTradesEvent event) {
        VillagerProfession profession = event.getType();

        for (int level = 1; level <= 5; level++) {
            List<VillagerTrades.ItemListing> listings = event.getTrades().get(level);
            if (listings == null || listings.isEmpty()) {
                continue;
            }

            for (int i = 0; i < listings.size(); i++) {
                VillagerTrades.ItemListing original = listings.get(i);
                if (original != null) {
                    listings.set(i, new FiftyFiftyGodListing(original, profession));
                }
            }
        }
    }

    public record FiftyFiftyGodListing(
            VillagerTrades.ItemListing original,
            VillagerProfession profession
    ) implements VillagerTrades.ItemListing {

        @Override
        @Nullable
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            if (!random.nextBoolean()) {
                return original.getOffer(trader, random);
            }

            ItemStack reward = createGodItem(trader.registryAccess(), profession, random);
            return createGodOffer(reward);
        }

        public static MerchantOffer createGodOffer(ItemStack reward) {
            return new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    reward,
                    64,
                    30,
                    0.05f
            );
        }

        public static ItemStack createGodItem(
                HolderLookup.Provider registries,
                VillagerProfession profession,
                RandomSource random
        ) {
            HolderLookup.RegistryLookup<Enchantment> enchants = registries.lookupOrThrow(Registries.ENCHANTMENT);

            if (profession == VillagerProfession.ARMORER) {
                return createArmorerItem(enchants, random);
            }
            if (profession == VillagerProfession.WEAPONSMITH) {
                return createWeaponsmithItem(enchants, random);
            }
            if (profession == VillagerProfession.TOOLSMITH) {
                return createToolsmithItem(enchants, random);
            }
            if (profession == VillagerProfession.FLETCHER) {
                return createFletcherItem(enchants, random);
            }
            if (profession == VillagerProfession.LIBRARIAN) {
                return createLibrarianBook(enchants, random);
            }

            return createGeneralGodItem(enchants, random);
        }

        private static ItemStack createArmorerItem(
                HolderLookup.RegistryLookup<Enchantment> enchants,
                RandomSource random
        ) {
            return switch (random.nextInt(4)) {
                case 0 -> enchantedItem(Items.NETHERITE_HELMET, enchants, Map.of(
                        Enchantments.PROTECTION, 4,
                        Enchantments.UNBREAKING, 3,
                        Enchantments.MENDING, 1,
                        Enchantments.THORNS, 3,
                        Enchantments.RESPIRATION, 3,
                        Enchantments.AQUA_AFFINITY, 1
                ));
                case 1 -> enchantedItem(Items.NETHERITE_CHESTPLATE, enchants, Map.of(
                        Enchantments.PROTECTION, 4,
                        Enchantments.UNBREAKING, 3,
                        Enchantments.MENDING, 1,
                        Enchantments.THORNS, 3
                ));
                case 2 -> enchantedItem(Items.NETHERITE_LEGGINGS, enchants, Map.of(
                        Enchantments.PROTECTION, 4,
                        Enchantments.UNBREAKING, 3,
                        Enchantments.MENDING, 1,
                        Enchantments.THORNS, 3
                ));
                default -> enchantedItem(Items.NETHERITE_BOOTS, enchants, Map.of(
                        Enchantments.PROTECTION, 4,
                        Enchantments.UNBREAKING, 3,
                        Enchantments.MENDING, 1,
                        Enchantments.THORNS, 3,
                        Enchantments.FEATHER_FALLING, 4,
                        Enchantments.DEPTH_STRIDER, 3
                ));
            };
        }

        private static ItemStack createWeaponsmithItem(
                HolderLookup.RegistryLookup<Enchantment> enchants,
                RandomSource random
        ) {
            if (random.nextBoolean()) {
                return enchantedItem(Items.NETHERITE_SWORD, enchants, Map.of(
                        Enchantments.SHARPNESS, 5,
                        Enchantments.LOOTING, 3,
                        Enchantments.UNBREAKING, 3,
                        Enchantments.FIRE_ASPECT, 2,
                        Enchantments.SWEEPING_EDGE, 3,
                        Enchantments.MENDING, 1
                ));
            }
            return enchantedItem(Items.NETHERITE_AXE, enchants, Map.of(
                    Enchantments.SHARPNESS, 5,
                    Enchantments.EFFICIENCY, 5,
                    Enchantments.UNBREAKING, 3,
                    Enchantments.MENDING, 1
            ));
        }

        private static ItemStack createToolsmithItem(
                HolderLookup.RegistryLookup<Enchantment> enchants,
                RandomSource random
        ) {
            return switch (random.nextInt(3)) {
                case 0 -> enchantedItem(Items.NETHERITE_PICKAXE, enchants, Map.of(
                        Enchantments.EFFICIENCY, 5,
                        Enchantments.FORTUNE, 3,
                        Enchantments.UNBREAKING, 3,
                        Enchantments.MENDING, 1
                ));
                case 1 -> enchantedItem(Items.NETHERITE_PICKAXE, enchants, Map.of(
                        Enchantments.EFFICIENCY, 5,
                        Enchantments.SILK_TOUCH, 1,
                        Enchantments.UNBREAKING, 3,
                        Enchantments.MENDING, 1
                ));
                default -> enchantedItem(Items.NETHERITE_SHOVEL, enchants, Map.of(
                        Enchantments.EFFICIENCY, 5,
                        Enchantments.SILK_TOUCH, 1,
                        Enchantments.UNBREAKING, 3,
                        Enchantments.MENDING, 1
                ));
            };
        }

        private static ItemStack createFletcherItem(
                HolderLookup.RegistryLookup<Enchantment> enchants,
                RandomSource random
        ) {
            if (random.nextBoolean()) {
                return enchantedItem(Items.BOW, enchants, Map.of(
                        Enchantments.POWER, 5,
                        Enchantments.PUNCH, 2,
                        Enchantments.FLAME, 1,
                        Enchantments.INFINITY, 1,
                        Enchantments.UNBREAKING, 3
                ));
            }
            return enchantedItem(Items.CROSSBOW, enchants, Map.of(
                    Enchantments.MULTISHOT, 1,
                    Enchantments.QUICK_CHARGE, 3,
                    Enchantments.UNBREAKING, 3,
                    Enchantments.MENDING, 1
            ));
        }

        private static ItemStack createLibrarianBook(
                HolderLookup.RegistryLookup<Enchantment> enchants,
                RandomSource random
        ) {
            return switch (random.nextInt(7)) {
                case 0 -> enchantedBook(enchants, Enchantments.MENDING, 1);
                case 1 -> enchantedBook(enchants, Enchantments.SHARPNESS, 5);
                case 2 -> enchantedBook(enchants, Enchantments.PROTECTION, 4);
                case 3 -> enchantedBook(enchants, Enchantments.EFFICIENCY, 5);
                case 4 -> enchantedBook(enchants, Enchantments.FORTUNE, 3);
                case 5 -> enchantedBook(enchants, Enchantments.UNBREAKING, 3);
                default -> enchantedBook(enchants, Enchantments.LOOTING, 3);
            };
        }

        private static ItemStack createGeneralGodItem(
                HolderLookup.RegistryLookup<Enchantment> enchants,
                RandomSource random
        ) {
            return switch (random.nextInt(6)) {
                case 0 -> new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 16);
                case 1 -> new ItemStack(Items.NETHER_STAR, 1);
                case 2 -> new ItemStack(Items.TOTEM_OF_UNDYING, 1);
                case 3 -> new ItemStack(Items.BEACON, 1);
                case 4 -> enchantedItem(Items.ELYTRA, enchants, Map.of(
                        Enchantments.UNBREAKING, 3,
                        Enchantments.MENDING, 1
                ));
                default -> new ItemStack(Items.EXPERIENCE_BOTTLE, 64);
            };
        }

        private static ItemStack enchantedItem(
                Item item,
                HolderLookup.RegistryLookup<Enchantment> enchants,
                Map<ResourceKey<Enchantment>, Integer> enchantLevels
        ) {
            ItemStack stack = new ItemStack(item);
            ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            for (Map.Entry<ResourceKey<Enchantment>, Integer> entry : enchantLevels.entrySet()) {
                builder.set(enchants.getOrThrow(entry.getKey()), entry.getValue());
            }
            stack.set(DataComponents.ENCHANTMENTS, builder.toImmutable());
            return stack;
        }

        private static ItemStack enchantedBook(
                HolderLookup.RegistryLookup<Enchantment> enchants,
                ResourceKey<Enchantment> enchant,
                int level
        ) {
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            builder.set(enchants.getOrThrow(enchant), level);
            book.set(DataComponents.STORED_ENCHANTMENTS, builder.toImmutable());
            return book;
        }
    }
}

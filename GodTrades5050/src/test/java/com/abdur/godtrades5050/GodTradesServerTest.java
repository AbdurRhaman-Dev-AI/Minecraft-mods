package com.abdur.godtrades5050;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EphemeralTestServerProvider.class)
public class GodTradesServerTest {

    @Test
    void testVillagerTradesEventWrappedAllProfessions(MinecraftServer server) {
        List<VillagerProfession> professions = List.of(
                VillagerProfession.ARMORER,
                VillagerProfession.WEAPONSMITH,
                VillagerProfession.TOOLSMITH,
                VillagerProfession.FLETCHER,
                VillagerProfession.LIBRARIAN,
                VillagerProfession.FARMER,
                VillagerProfession.CLERIC,
                VillagerProfession.MASON
        );

        for (VillagerProfession prof : professions) {
            var levelMap = VillagerTrades.TRADES.get(prof);
            assertNotNull(levelMap, "Missing VillagerTrades map for " + prof);
            for (int lvl = 1; lvl <= 5; lvl++) {
                VillagerTrades.ItemListing[] listings = levelMap.get(lvl);
                if (listings == null || listings.length == 0) {
                    continue;
                }
                for (VillagerTrades.ItemListing listing : listings) {
                    assertInstanceOf(
                            VillagerTradeHandler.FiftyFiftyGodListing.class,
                            listing,
                            "Expected wrapped FiftyFiftyGodListing for " + prof + " level " + lvl
                    );
                }
            }
        }
    }

    @Test
    void testGodItemPoolsAcrossAllProfessions(MinecraftServer server) {
        RandomSource rand = RandomSource.create(12345L);
        List<VillagerProfession> professions = List.of(
                VillagerProfession.ARMORER,
                VillagerProfession.WEAPONSMITH,
                VillagerProfession.TOOLSMITH,
                VillagerProfession.FLETCHER,
                VillagerProfession.LIBRARIAN,
                VillagerProfession.FARMER,
                VillagerProfession.CLERIC,
                VillagerProfession.MASON
        );

        for (VillagerProfession prof : professions) {
            for (int i = 0; i < 20; i++) {
                ItemStack reward = VillagerTradeHandler.FiftyFiftyGodListing.createGodItem(
                        server.registryAccess(), prof, rand);
                assertFalse(reward.isEmpty(), "Empty God reward for " + prof);

                MerchantOffer offer = VillagerTradeHandler.FiftyFiftyGodListing.createGodOffer(reward);
                assertTrue(offer.getBaseCostA().is(Items.EMERALD));
                assertEquals(1, offer.getBaseCostA().getCount());
                assertEquals(64, offer.getMaxUses());
                assertEquals(30, offer.getXp());

                if (prof == VillagerProfession.LIBRARIAN) {
                    assertTrue(reward.is(Items.ENCHANTED_BOOK));
                    assertTrue(reward.has(DataComponents.STORED_ENCHANTMENTS));
                } else if (prof == VillagerProfession.ARMORER
                        || prof == VillagerProfession.WEAPONSMITH
                        || prof == VillagerProfession.TOOLSMITH
                        || prof == VillagerProfession.FLETCHER) {
                    assertTrue(reward.has(DataComponents.ENCHANTMENTS));
                }
            }
        }
    }
}

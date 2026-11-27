package com.abdur.godtechascension;

import com.abdur.godtechascension.block.AlloySynthesizerBlockEntity;
import com.abdur.godtechascension.block.MkvReactorFallbackBlockEntity;
import com.abdur.godtechascension.block.ModEnergyStorage;
import com.abdur.godtechascension.menu.AlloySynthesizerMenu;
import com.abdur.godtechascension.recipe.AlloyRecipe;
import com.abdur.godtechascension.recipe.AlloyRecipeInput;
import com.abdur.godtechascension.registry.ModBlocks;
import com.abdur.godtechascension.registry.ModItems;
import com.abdur.godtechascension.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EphemeralTestServerProvider.class)
public class GodTechServerTest {

    @Test
    void testGodGearStatsAndComponents(MinecraftServer server) {
        ItemStack sword = new ItemStack(ModItems.GOD_SWORD.get());
        assertTrue(sword.has(DataComponents.UNBREAKABLE), "God Sword must be unbreakable");
        assertTrue(sword.has(DataComponents.FIRE_RESISTANT), "God Sword must be fire/lava resistant");

        AtomicReference<Double> swordDamage = new AtomicReference<>(0.0);
        sword.getAttributeModifiers().forEach(EquipmentSlotGroup.MAINHAND, (attr, mod) -> {
            if (attr.equals(Attributes.ATTACK_DAMAGE)) {
                swordDamage.updateAndGet(v -> v + mod.amount());
            }
        });
        assertEquals(25.0, 1.0 + swordDamage.get(), 0.01, "God Sword total attack damage must equal 25");

        ItemStack jetpack = new ItemStack(ModItems.ASCENSION_JETPACK.get());
        AtomicBoolean emptyGrantsFlight = new AtomicBoolean(false);
        jetpack.getAttributeModifiers().forEach(EquipmentSlotGroup.CHEST, (attr, mod) -> {
            if (attr.equals(NeoForgeMod.CREATIVE_FLIGHT) && mod.amount() >= 1.0) {
                emptyGrantsFlight.set(true);
            }
        });
        assertFalse(emptyGrantsFlight.get(), "Empty Jetpack (0 fuel) must NOT grant CREATIVE_FLIGHT");

        // Refuel with Blaze Powder and verify flight attribute + odometer persistence
        ItemStack powder = new ItemStack(Items.BLAZE_POWDER, 3);
        assertTrue(com.abdur.godtechascension.item.AscensionJetpackItem.tryAddFuelFromStack(jetpack, powder, false));
        assertEquals(2, powder.getCount());
        assertEquals(com.abdur.godtechascension.item.AscensionJetpackItem.FUEL_PER_BLAZE_POWDER,
                com.abdur.godtechascension.item.AscensionJetpackItem.getData(jetpack).fuel());

        AtomicBoolean fueledGrantsFlight = new AtomicBoolean(false);
        jetpack.getAttributeModifiers().forEach(EquipmentSlotGroup.CHEST, (attr, mod) -> {
            if (attr.equals(NeoForgeMod.CREATIVE_FLIGHT) && mod.amount() >= 1.0) {
                fueledGrantsFlight.set(true);
            }
        });
        assertTrue(fueledGrantsFlight.get(), "Fueled Jetpack must grant CREATIVE_FLIGHT attribute");

        ItemStack pick = new ItemStack(ModItems.GOD_PICKAXE.get());
        assertTrue(pick.has(DataComponents.UNBREAKABLE), "God Pickaxe must be unbreakable");
        assertTrue(pick.has(DataComponents.FIRE_RESISTANT), "God Pickaxe must be fire/lava resistant");
        assertTrue(pick.getDestroySpeed(Blocks.OBSIDIAN.defaultBlockState()) >= 180.0F,
                "God Pickaxe mining speed must be at least 180.0F");

        for (ItemStack gear : List.of(
                new ItemStack(ModItems.ASCENSION_HELMET.get()),
                new ItemStack(ModItems.ASCENSION_CHESTPLATE.get()),
                new ItemStack(ModItems.ASCENSION_LEGGINGS.get()),
                new ItemStack(ModItems.ASCENSION_BOOTS.get()),
                new ItemStack(ModItems.ASCENSION_JETPACK.get()),
                new ItemStack(ModItems.PLASMA_RAILGUN.get()),
                new ItemStack(ModItems.GOD_CORE.get()),
                new ItemStack(ModItems.ARC_REACTOR.get())
        )) {
            assertTrue(gear.has(DataComponents.FIRE_RESISTANT), gear.getHoverName().getString() + " must be fire resistant");
        }
    }

    @Test
    void testRecipesRegisteredAndMatch(MinecraftServer server) {
        List<RecipeHolder<AlloyRecipe>> alloyRecipes =
                server.getRecipeManager().getAllRecipesFor(ModRecipes.ALLOY_TYPE.get());
        assertEquals(3, alloyRecipes.size(), "Expected 3 Alloy Synthesizer recipes");

        AlloyRecipeInput singularityInput = new AlloyRecipeInput(
                new ItemStack(ModItems.GOD_CRYSTAL.get()),
                new ItemStack(ModItems.VOID_SHARD.get())
        );
        var match = server.getRecipeManager().getRecipeFor(ModRecipes.ALLOY_TYPE.get(), singularityInput, null);
        assertTrue(match.isPresent(), "God Crystal + Void Shard must match Singularity Shard synthesizer recipe");
        assertTrue(match.get().value().output().is(ModItems.SINGULARITY_SHARD.get()));
        assertEquals(2, match.get().value().output().getCount());

        long godTechCount = server.getRecipeManager().getRecipes().stream()
                .filter(h -> h.id().getNamespace().equals(GodTechAscension.MODID))
                .count();
        assertTrue(godTechCount >= 28, "Expected at least 28 registered GodTech recipes, got " + godTechCount);
    }

    @Test
    void testArcReactorEnergyAndSynthesizer32BitSync(MinecraftServer server) {
        MkvReactorFallbackBlockEntity mkv = new MkvReactorFallbackBlockEntity(
                BlockPos.ZERO, ModBlocks.MKV_REACTOR_CONTROLLER.get().defaultBlockState());
        AlloySynthesizerBlockEntity synth = new AlloySynthesizerBlockEntity(
                BlockPos.ZERO, ModBlocks.ALLOY_SYNTHESIZER.get().defaultBlockState());

        // Test ModEnergyStorage generation & clamping (100k FE/t, 10M FE cap)
        ModEnergyStorage reactorEnergy = (ModEnergyStorage) mkv.getEnergyStorage();
        reactorEnergy.addEnergy(MkvReactorFallbackBlockEntity.GENERATION_PER_TICK);
        assertEquals(100_000, reactorEnergy.getEnergyStored());
        reactorEnergy.addEnergy(20_000_000);
        assertEquals(MkvReactorFallbackBlockEntity.CAPACITY, reactorEnergy.getEnergyStored(), "Capacity must clamp at 10,000,000 FE");

        // Verify 1-block Arc Reactor & MK-V Create stress capacity math = 5x Max Level-18 Steam Engines (1,474,560 su)
        assertEquals(1_474_560.0F,
                com.abdur.godtechascension.compat.create.CreateArcReactorBlockEntity.STRESS_CAPACITY_PER_RPM * 256.0F,
                0.1F);
        assertEquals(1_474_560.0F,
                com.abdur.godtechascension.compat.create.CreateMkvReactorBlockEntity.STRESS_CAPACITY_PER_RPM * 256.0F,
                0.1F);

        // Test automation item handler slot guards (slots 0,1 insert only; slot 2 extract only)
        IItemHandler auto = synth.getAutomationItemHandler();
        assertEquals(1, auto.insertItem(2, new ItemStack(Items.IRON_INGOT), false).getCount(),
                "Output slot 2 must reject external insertion");
        assertTrue(auto.insertItem(0, new ItemStack(ModItems.GOD_CRYSTAL.get()), false).isEmpty());
        assertTrue(auto.insertItem(1, new ItemStack(ModItems.VOID_SHARD.get()), false).isEmpty());
        assertTrue(auto.extractItem(0, 1, false).isEmpty(),
                "Input slot 0 must reject external extraction");

        // Test 32-bit FE sync through AlloySynthesizerMenu past 16-bit signed short max (32,767)
        ModEnergyStorage synthEnergy = (ModEnergyStorage) synth.getEnergyStorage();
        synthEnergy.setEnergy(85_432);
        Inventory dummyInv = new Inventory(null);
        AlloySynthesizerMenu menu = new AlloySynthesizerMenu(0, dummyInv, synth, synth.getData());
        assertEquals(85_432, menu.getEnergyStored(), "32-bit energy sync must not overflow 16-bit short");
        assertEquals(AlloySynthesizerBlockEntity.ENERGY_CAPACITY, menu.getMaxEnergy(), "Max FE sync must equal 100,000");
    }
}

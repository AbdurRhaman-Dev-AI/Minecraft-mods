package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers the Creative Mode tab for GodTech: Ascension.
 */
public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GodTechAscension.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GODTECH_TAB = CREATIVE_MODE_TABS.register(
            "godtech_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + GodTechAscension.MODID))
                    .icon(() -> new ItemStack(ModItems.GOD_CORE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.TITANIUM_ORE.get());
                        output.accept(ModItems.DEEPSLATE_TITANIUM_ORE.get());
                        output.accept(ModItems.TITANIUM_CASING.get());
                        output.accept(ModItems.RAW_TITANIUM.get());
                        output.accept(ModItems.CRUSHED_RAW_TITANIUM.get());
                        output.accept(ModItems.TITANIUM_INGOT.get());
                        output.accept(ModItems.TITANIUM_SHEET.get());
                        output.accept(ModItems.TITANIUM_ALLOY.get());
                        output.accept(ModItems.REINFORCED_MECHANISM.get());
                        output.accept(ModItems.GOD_CRYSTAL.get());
                        output.accept(ModItems.VOID_SHARD.get());
                        output.accept(ModItems.QUANTUM_CIRCUIT.get());
                        output.accept(ModItems.GOD_CORE.get());
                        output.accept(ModItems.GOD_PICKAXE.get());
                        output.accept(ModItems.GOD_SWORD.get());
                    })
                    .build()
    );

    private ModCreativeTabs() {
    }
}

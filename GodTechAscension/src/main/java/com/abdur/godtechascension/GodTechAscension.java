package com.abdur.godtechascension;

import com.abdur.godtechascension.registry.ModArmorMaterials;
import com.abdur.godtechascension.registry.ModBlockEntities;
import com.abdur.godtechascension.registry.ModBlocks;
import com.abdur.godtechascension.registry.ModCreativeTabs;
import com.abdur.godtechascension.registry.ModDataComponents;
import com.abdur.godtechascension.registry.ModItems;
import com.abdur.godtechascension.registry.ModMenus;
import com.abdur.godtechascension.registry.ModRecipes;
import com.abdur.godtechascension.registry.ModSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Main entrypoint for GodTech: Ascension (NeoForge 1.21.1).
 */
@Mod(GodTechAscension.MODID)
public class GodTechAscension {

    public static final String MODID = "godtechascension";

    public GodTechAscension(IEventBus modEventBus) {
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModRecipes.TYPES.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}

package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.menu.AlloySynthesizerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers MenuTypes for GodTech: Ascension GUIs.
 */
public final class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, GodTechAscension.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<AlloySynthesizerMenu>> ALLOY_SYNTHESIZER =
            MENUS.register(
                    "alloy_synthesizer",
                    () -> IMenuTypeExtension.create(AlloySynthesizerMenu::new)
            );

    private ModMenus() {
    }
}

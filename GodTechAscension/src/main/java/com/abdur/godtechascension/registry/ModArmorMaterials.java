package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

/**
 * Registers ArmorMaterials for the Ascension Exosuit and 3D Backtank Jetpack.
 */
public final class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, GodTechAscension.MODID);

    public static final Holder<ArmorMaterial> ASCENSION = ARMOR_MATERIALS.register(
            "ascension",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.LEGGINGS, 7);
                        map.put(ArmorItem.Type.CHESTPLATE, 9);
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.BODY, 11);
                    }),
                    22,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.of(ModItems.TITANIUM_ALLOY.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "ascension")
                    )),
                    4.0F,
                    0.2F
            )
    );

    public static final Holder<ArmorMaterial> ASCENSION_JETPACK = ARMOR_MATERIALS.register(
            "ascension_jetpack",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.CHESTPLATE, 8);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    22,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.of(ModItems.TITANIUM_ALLOY.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "ascension_jetpack")
                    )),
                    3.0F,
                    0.1F
            )
    );

    private ModArmorMaterials() {
    }
}

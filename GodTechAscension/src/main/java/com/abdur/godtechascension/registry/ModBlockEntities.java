package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.block.AlloySynthesizerBlockEntity;
import com.abdur.godtechascension.block.ArcReactorBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers BlockEntities for GodTech: Ascension machines.
 */
public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GodTechAscension.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArcReactorBlockEntity>> ARC_REACTOR =
            BLOCK_ENTITIES.register(
                    "arc_reactor",
                    () -> BlockEntityType.Builder.of(ArcReactorBlockEntity::new, ModBlocks.ARC_REACTOR.get()).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlloySynthesizerBlockEntity>> ALLOY_SYNTHESIZER =
            BLOCK_ENTITIES.register(
                    "alloy_synthesizer",
                    () -> BlockEntityType.Builder.of(AlloySynthesizerBlockEntity::new, ModBlocks.ALLOY_SYNTHESIZER.get()).build(null)
            );

    private ModBlockEntities() {
    }
}

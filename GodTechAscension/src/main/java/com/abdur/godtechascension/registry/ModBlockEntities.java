package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.block.AlloySynthesizerBlockEntity;
import com.abdur.godtechascension.block.ArcReactorBlockEntity;
import com.abdur.godtechascension.block.MkvReactorFallbackBlockEntity;
import com.abdur.godtechascension.compat.create.CreateCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers BlockEntities for GodTech: Ascension machines.
 */
public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GodTechAscension.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntity>> ARC_REACTOR =
            BLOCK_ENTITIES.register(
                    "arc_reactor",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> ModList.get().isLoaded("create")
                                    ? CreateCompat.createArcReactorBlockEntity(pos, state)
                                    : new ArcReactorBlockEntity(pos, state),
                            ModBlocks.ARC_REACTOR.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlloySynthesizerBlockEntity>> ALLOY_SYNTHESIZER =
            BLOCK_ENTITIES.register(
                    "alloy_synthesizer",
                    () -> BlockEntityType.Builder.of(AlloySynthesizerBlockEntity::new, ModBlocks.ALLOY_SYNTHESIZER.get()).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntity>> MKV_REACTOR_CONTROLLER =
            BLOCK_ENTITIES.register(
                    "mkv_reactor_controller",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> ModList.get().isLoaded("create")
                                    ? CreateCompat.createMkvControllerBlockEntity(pos, state)
                                    : new MkvReactorFallbackBlockEntity(pos, state),
                            ModBlocks.MKV_REACTOR_CONTROLLER.get()
                    ).build(null)
            );

    private ModBlockEntities() {
    }
}

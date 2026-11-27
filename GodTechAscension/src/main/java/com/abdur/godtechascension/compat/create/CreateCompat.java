package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.block.MkvReactorFallbackBlockEntity;
import com.abdur.godtechascension.registry.ModBlocks;
import com.abdur.godtechascension.registry.ModItems;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * Isolated Create 6.0.10 compatibility bridge (only classloaded when Create is present).
 */
public final class CreateCompat {

    private CreateCompat() {
    }

    public static Block createArcReactorBlock(BlockBehaviour.Properties properties) {
        return new CreateArcReactorBlock(properties);
    }

    public static BlockEntity createArcReactorBlockEntity(BlockPos pos, BlockState state) {
        return new CreateArcReactorBlockEntity(pos, state);
    }

    public static Block createMkvControllerBlock(BlockBehaviour.Properties properties) {
        return new CreateMkvReactorBlock(properties);
    }

    public static BlockEntity createMkvControllerBlockEntity(BlockPos pos, BlockState state) {
        return new CreateMkvReactorBlockEntity(pos, state);
    }

    public static IEnergyStorage getMkvEnergyStorage(BlockEntity be) {
        if (be instanceof CreateMkvReactorBlockEntity createBe) {
            return createBe.getEnergyStorage();
        }
        if (be instanceof MkvReactorFallbackBlockEntity fb) {
            return fb.getEnergyStorage();
        }
        return null;
    }

    public static void onCommonSetup() {
        GogglesItem.addIsWearingPredicate(
                player -> player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.ASCENSION_HELMET.get())
        );
        BlockStressValues.CAPACITIES.register(
                ModBlocks.ARC_REACTOR.get(),
                () -> CreateArcReactorBlockEntity.STRESS_CAPACITY_PER_RPM
        );
        BlockStressValues.CAPACITIES.register(
                ModBlocks.MKV_REACTOR_CONTROLLER.get(),
                () -> CreateMkvReactorBlockEntity.STRESS_CAPACITY_PER_RPM
        );
    }
}

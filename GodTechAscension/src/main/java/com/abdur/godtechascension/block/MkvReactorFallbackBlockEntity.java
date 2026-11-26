package com.abdur.godtechascension.block;

import com.abdur.godtechascension.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * Fallback BlockEntity for MK-V Reactor Controller when Create is not installed.
 */
public class MkvReactorFallbackBlockEntity extends BlockEntity {

    public static final int CAPACITY = 10_000_000;
    public static final int GENERATION_PER_TICK = 100_000;

    private final ModEnergyStorage energy = new ModEnergyStorage(
            CAPACITY, 0, GENERATION_PER_TICK * 2, this::setChanged);

    public MkvReactorFallbackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MKV_REACTOR_CONTROLLER.get(), pos, state);
    }

    public IEnergyStorage getEnergyStorage() {
        return energy;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MkvReactorFallbackBlockEntity be) {
        var res = MkvMultiblockHelper.checkFormation(level, pos);
        if (res.formed() && res.powered()) {
            be.energy.addEnergy(GENERATION_PER_TICK);
        }

        if (be.energy.getEnergyStored() <= 0) {
            return;
        }
        for (Direction dir : Direction.values()) {
            IEnergyStorage target = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.relative(dir), dir.getOpposite());
            if (target != null && target.canReceive()) {
                int accepted = target.receiveEnergy(Math.min(GENERATION_PER_TICK, be.energy.getEnergyStored()), false);
                if (accepted > 0) {
                    be.energy.extractEnergy(accepted, false);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Energy", energy.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Energy")) {
            energy.setEnergy(tag.getInt("Energy"));
        }
    }
}

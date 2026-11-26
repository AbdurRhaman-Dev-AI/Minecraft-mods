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
 * Generates 100,000 FE/t continuously and pushes FE to all 6 adjacent blocks.
 */
public class ArcReactorBlockEntity extends BlockEntity {

    public static final int CAPACITY = 10_000_000;
    public static final int GENERATION_PER_TICK = 100_000;

    private final ModEnergyStorage energy = new ModEnergyStorage(
            CAPACITY, 0, GENERATION_PER_TICK * 2, this::setChanged);

    public ArcReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARC_REACTOR.get(), pos, state);
    }

    public IEnergyStorage getEnergyStorage() {
        return energy;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ArcReactorBlockEntity reactor) {
        if (reactor.energy.getEnergyStored() < CAPACITY) {
            reactor.energy.addEnergy(GENERATION_PER_TICK);
        }

        if (reactor.energy.getEnergyStored() <= 0) {
            return;
        }

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            IEnergyStorage target = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, dir.getOpposite());
            if (target != null && target.canReceive()) {
                int toSend = Math.min(GENERATION_PER_TICK, reactor.energy.getEnergyStored());
                int accepted = target.receiveEnergy(toSend, false);
                if (accepted > 0) {
                    reactor.energy.extractEnergy(accepted, false);
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

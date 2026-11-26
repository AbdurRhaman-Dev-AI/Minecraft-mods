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
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * Generates 16,384 FE/t continuously and pushes FE to all 6 adjacent blocks.
 */
public class ArcReactorBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1_000_000;
    public static final int GENERATION_PER_TICK = 16_384;

    private final EnergyStorage energy = new EnergyStorage(CAPACITY, 0, GENERATION_PER_TICK * 2) {
        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            int extracted = super.extractEnergy(toExtract, simulate);
            if (!simulate && extracted > 0) {
                setChanged();
            }
            return extracted;
        }
    };

    public ArcReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARC_REACTOR.get(), pos, state);
    }

    public IEnergyStorage getEnergyStorage() {
        return energy;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ArcReactorBlockEntity reactor) {
        int current = reactor.energy.getEnergyStored();
        if (current < CAPACITY) {
            int next = Math.min(CAPACITY, current + GENERATION_PER_TICK);
            reactor.setInternalEnergy(next);
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

    private void setInternalEnergy(int value) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Energy", Math.max(0, Math.min(CAPACITY, value)));
        energy.deserializeNBT(level != null ? level.registryAccess() : null, tag.get("Energy"));
        setChanged();
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
            energy.deserializeNBT(registries, tag.get("Energy"));
        }
    }
}

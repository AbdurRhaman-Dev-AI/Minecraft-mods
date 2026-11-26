package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.block.MkvMultiblockHelper;
import com.abdur.godtechascension.block.ModEnergyStorage;
import com.abdur.godtechascension.registry.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * Generates 1,474,560 su (5760 capacity * 256 RPM = 5x Max Steam Engines) and 100,000 FE/t
 * when the 4x2x2 MK-V Reactor multiblock is formed and powered by Redstone.
 */
public class CreateMkvReactorBlockEntity extends GeneratingKineticBlockEntity {

    public static final int CAPACITY = 10_000_000;
    public static final int GENERATION_PER_TICK = 100_000;
    public static final float STRESS_CAPACITY_PER_RPM = 5760.0F; // 5760 * 256 = 1,474,560 su

    private final ModEnergyStorage energy = new ModEnergyStorage(
            CAPACITY, 0, GENERATION_PER_TICK * 2, this::setChanged);

    private boolean active;

    public CreateMkvReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MKV_REACTOR_CONTROLLER.get(), pos, state);
    }

    public IEnergyStorage getEnergyStorage() {
        return energy;
    }

    @Override
    public float getGeneratedSpeed() {
        return active ? 256.0F : 0.0F;
    }

    @Override
    public float calculateAddedStressCapacity() {
        return active ? STRESS_CAPACITY_PER_RPM : 0.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) {
            return;
        }

        if (level.getGameTime() % 10 == 0) {
            var res = MkvMultiblockHelper.checkFormation(level, worldPosition);
            boolean shouldBeActive = res.formed() && res.powered();
            if (shouldBeActive != active) {
                active = shouldBeActive;
                updateGeneratedRotation();
                setChanged();
            }
        }

        if (active) {
            energy.addEnergy(GENERATION_PER_TICK);
        }

        if (energy.getEnergyStored() > 0) {
            for (Direction dir : Direction.values()) {
                IEnergyStorage target = level.getCapability(
                        Capabilities.EnergyStorage.BLOCK, worldPosition.relative(dir), dir.getOpposite());
                if (target != null && target.canReceive()) {
                    int accepted = target.receiveEnergy(
                            Math.min(GENERATION_PER_TICK, energy.getEnergyStored()), false);
                    if (accepted > 0) {
                        energy.extractEnergy(accepted, false);
                    }
                }
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putInt("Energy", energy.getEnergyStored());
        compound.putBoolean("Active", active);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        if (compound.contains("Energy")) {
            energy.setEnergy(compound.getInt("Energy"));
        }
        active = compound.getBoolean("Active");
    }
}

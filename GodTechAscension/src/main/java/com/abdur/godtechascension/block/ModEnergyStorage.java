package com.abdur.godtechascension.block;

import net.neoforged.neoforge.energy.EnergyStorage;

/**
 * EnergyStorage subclass exposing direct internal energy adjustment for machines/generators
 * without NBT round-trips every tick.
 */
public class ModEnergyStorage extends EnergyStorage {

    private final Runnable onChanged;

    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract, Runnable onChanged) {
        super(capacity, maxReceive, maxExtract);
        this.onChanged = onChanged;
    }

    public void setEnergy(int amount) {
        int clamped = Math.max(0, Math.min(this.capacity, amount));
        if (this.energy != clamped) {
            this.energy = clamped;
            this.onChanged.run();
        }
    }

    public void addEnergy(int amount) {
        setEnergy(this.energy + amount);
    }

    public void consumeEnergy(int amount) {
        setEnergy(this.energy - amount);
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        int received = super.receiveEnergy(toReceive, simulate);
        if (!simulate && received > 0) {
            this.onChanged.run();
        }
        return received;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        int extracted = super.extractEnergy(toExtract, simulate);
        if (!simulate && extracted > 0) {
            this.onChanged.run();
        }
        return extracted;
    }
}

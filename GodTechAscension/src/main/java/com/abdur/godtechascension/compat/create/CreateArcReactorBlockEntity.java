package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.registry.ModBlockEntities;
import com.abdur.godtechascension.registry.ModSounds;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Pure Create rotational SU generator producing 1,474,560 su (5760 * 256 RPM = 5x Max Steam Engines)
 * when powered by a Redstone signal / Lever, playing a 16-block audible generator loop.
 */
public class CreateArcReactorBlockEntity extends GeneratingKineticBlockEntity {

    public static final float STRESS_CAPACITY_PER_RPM = 5760.0F; // 5760 * 256 = 1,474,560 su
    private static final int SOUND_LOOP_TICKS = 235; // ~11.75s overlap for seamless 12s OGG loop

    private boolean active;
    private int soundTimer;

    public CreateArcReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARC_REACTOR.get(), pos, state);
    }

    public boolean isActive() {
        return active;
    }

    public void updateRedstoneState() {
        if (level == null || level.isClientSide) {
            return;
        }
        boolean powered = level.hasNeighborSignal(worldPosition);
        if (powered != active) {
            active = powered;
            soundTimer = 0;
            updateGeneratedRotation();
            setChanged();
            if (active) {
                playGeneratorSound();
            }
        }
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

        if (level.getGameTime() % 5 == 0) {
            updateRedstoneState();
        }

        if (active) {
            soundTimer++;
            if (soundTimer >= SOUND_LOOP_TICKS) {
                soundTimer = 0;
                playGeneratorSound();
            }
        }
    }

    private void playGeneratorSound() {
        if (level != null) {
            level.playSound(
                    null,
                    worldPosition,
                    ModSounds.ARC_REACTOR_LOOP.get(),
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );
        }
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putBoolean("Active", active);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        active = compound.getBoolean("Active");
    }
}

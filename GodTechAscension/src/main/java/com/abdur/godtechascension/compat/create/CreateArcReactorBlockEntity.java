package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.registry.ModBlockEntities;
import com.abdur.godtechascension.registry.ModSounds;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Pure Create rotational SU generator producing 1,474,560 su (5x Max Steam Engines)
 * with an interactive Create RPM & direction Speed Controller (-256 to +256 RPM),
 * Redstone/Lever ON/OFF control, and a 16-block audible generator loop.
 */
public class CreateArcReactorBlockEntity extends GeneratingKineticBlockEntity {

    public static final int DEFAULT_SPEED = 256;
    public static final float TARGET_STRESS_CAPACITY = 1_474_560.0F;
    public static final float STRESS_CAPACITY_PER_RPM = TARGET_STRESS_CAPACITY / DEFAULT_SPEED; // 5760.0F
    private static final int SOUND_LOOP_TICKS = 235;

    private ScrollValueBehaviour generatedSpeed;
    private boolean active;
    private int soundTimer;

    public CreateArcReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARC_REACTOR.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        generatedSpeed = new KineticScrollValueBehaviour(
                Component.literal("Rotation Speed"),
                this,
                new SpeedSlotTransform()
        );
        generatedSpeed.between(-256, 256);
        generatedSpeed.value = DEFAULT_SPEED;
        generatedSpeed.withCallback(i -> updateGeneratedRotation());
        behaviours.add(generatedSpeed);
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
        if (!active) {
            return 0.0F;
        }
        return generatedSpeed != null ? generatedSpeed.getValue() : DEFAULT_SPEED;
    }

    @Override
    public float calculateAddedStressCapacity() {
        if (!active) {
            return 0.0F;
        }
        int speed = generatedSpeed != null ? Math.abs(generatedSpeed.getValue()) : DEFAULT_SPEED;
        return speed > 0 ? TARGET_STRESS_CAPACITY / speed : 0.0F;
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

    private static final class SpeedSlotTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return new Vec3(0.5D, 0.375D, 0.96D);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction side) {
            Direction facing = state.getValue(HorizontalKineticBlock.HORIZONTAL_FACING);
            return side == facing.getClockWise() || side == facing.getCounterClockWise();
        }
    }
}

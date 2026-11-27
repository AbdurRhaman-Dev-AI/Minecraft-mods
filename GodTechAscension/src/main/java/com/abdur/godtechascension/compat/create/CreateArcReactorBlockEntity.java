package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.item.AscensionJetpackItem;
import com.abdur.godtechascension.registry.ModBlockEntities;
import com.abdur.godtechascension.registry.ModSounds;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Pure Create rotational SU generator producing 1,474,560 su (5x Max Steam Engines)
 * with an interactive Create RPM & direction Speed Controller (-256 to +256 RPM),
 * Redstone/Lever ON/OFF control, Blaze Cake fueling (600s/cake, max 64),
 * 30-second steam puffs, and a 16-block audible generator loop.
 */
public class CreateArcReactorBlockEntity extends GeneratingKineticBlockEntity {

    public static final int DEFAULT_SPEED = 256;
    public static final float TARGET_STRESS_CAPACITY = 1_474_560.0F;
    public static final float STRESS_CAPACITY_PER_RPM = TARGET_STRESS_CAPACITY / DEFAULT_SPEED; // 5760.0F
    public static final int MAX_BLAZE_CAKES = 64;
    public static final int FUEL_PER_BLAZE_CAKE = 600 * 20; // 600 seconds = 12,000 ticks
    public static final int MAX_FUEL_TICKS = MAX_BLAZE_CAKES * FUEL_PER_BLAZE_CAKE;
    private static final int SOUND_LOOP_TICKS = 235;
    private static final int STEAM_INTERVAL_TICKS = 30 * 20; // 30 seconds = 600 ticks

    private ScrollValueBehaviour generatedSpeed;
    private boolean active;
    private int fuelTicks;
    private int soundTimer;
    private int steamTimer;

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

    public boolean isRunning() {
        return active && fuelTicks > 0;
    }

    public int getFuelTicks() {
        return fuelTicks;
    }

    public int getBlazeCakeCount() {
        return fuelTicks / FUEL_PER_BLAZE_CAKE;
    }

    public int tryAddBlazeCakes(ItemStack stack, boolean consume) {
        if (!AscensionJetpackItem.isBlazeCake(stack) || fuelTicks >= MAX_FUEL_TICKS) {
            return 0;
        }
        int neededTicks = MAX_FUEL_TICKS - fuelTicks;
        int maxInsert = neededTicks / FUEL_PER_BLAZE_CAKE;
        if (maxInsert <= 0) {
            return 0;
        }
        int toAdd = Math.min(stack.getCount(), maxInsert);
        if (toAdd <= 0) {
            return 0;
        }

        boolean wasRunning = isRunning();
        if (consume) {
            stack.shrink(toAdd);
        }
        fuelTicks = Math.min(MAX_FUEL_TICKS, fuelTicks + toAdd * FUEL_PER_BLAZE_CAKE);
        setChanged();
        sendData();
        if (!wasRunning && isRunning()) {
            soundTimer = 0;
            updateGeneratedRotation();
            playGeneratorSound();
        }
        return toAdd;
    }

    public int extractAllBlazeCakes(Player player) {
        int cakes = fuelTicks / FUEL_PER_BLAZE_CAKE;
        if (cakes <= 0) {
            return 0;
        }
        Item blazeCake = AscensionJetpackItem.getBlazeCakeItem();
        if (blazeCake == Items.AIR) {
            return 0;
        }
        boolean wasRunning = isRunning();
        fuelTicks -= cakes * FUEL_PER_BLAZE_CAKE;
        player.getInventory().placeItemBackInInventory(new ItemStack(blazeCake, cakes));
        setChanged();
        sendData();
        if (wasRunning && !isRunning()) {
            updateGeneratedRotation();
        }
        return cakes;
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
            sendData();
            if (isRunning()) {
                playGeneratorSound();
            }
        }
    }

    @Override
    public float getGeneratedSpeed() {
        if (!isRunning()) {
            return 0.0F;
        }
        return generatedSpeed != null ? generatedSpeed.getValue() : DEFAULT_SPEED;
    }

    @Override
    public float calculateAddedStressCapacity() {
        if (!isRunning()) {
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

        if (isRunning()) {
            fuelTicks--;
            if (fuelTicks <= 0) {
                fuelTicks = 0;
                updateGeneratedRotation();
                setChanged();
                sendData();
                return;
            }
            if (level.getGameTime() % 20 == 0) {
                setChanged();
            }

            soundTimer++;
            if (soundTimer >= SOUND_LOOP_TICKS) {
                soundTimer = 0;
                playGeneratorSound();
            }

            steamTimer++;
            if (steamTimer >= STEAM_INTERVAL_TICKS) {
                steamTimer = 0;
                emitSteamPuff();
            }
        } else {
            steamTimer = 0;
        }
    }

    private void emitSteamPuff() {
        if (level instanceof ServerLevel serverLevel) {
            double x = worldPosition.getX() + 0.5D;
            double y = worldPosition.getY() + 1.05D;
            double z = worldPosition.getZ() + 0.5D;
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 5, 0.12D, 0.05D, 0.12D, 0.025D);
            serverLevel.sendParticles(ParticleTypes.CLOUD, x, y, z, 3, 0.10D, 0.04D, 0.10D, 0.015D);
            serverLevel.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25F, 1.4F);
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
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        int cakes = getBlazeCakeCount();
        int totalSecs = fuelTicks / 20;
        int mins = totalSecs / 60;
        int secs = totalSecs % 60;
        tooltip.add(Component.literal(String.format("    Blaze Cake Fuel: %d / %d (%dm %02ds)",
                        cakes, MAX_BLAZE_CAKES, mins, secs))
                .withStyle(fuelTicks > 0 ? ChatFormatting.GOLD : ChatFormatting.RED));
        return true;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putBoolean("Active", active);
        compound.putInt("FuelTicks", fuelTicks);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        active = compound.getBoolean("Active");
        fuelTicks = compound.getInt("FuelTicks");
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

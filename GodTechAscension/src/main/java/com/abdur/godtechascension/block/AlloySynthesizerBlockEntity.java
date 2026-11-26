package com.abdur.godtechascension.block;

import com.abdur.godtechascension.menu.AlloySynthesizerMenu;
import com.abdur.godtechascension.recipe.AlloyRecipe;
import com.abdur.godtechascension.recipe.AlloyRecipeInput;
import com.abdur.godtechascension.registry.ModBlockEntities;
import com.abdur.godtechascension.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * BlockEntity for the Alloy Synthesizer (2 inputs, 1 output, FE buffer).
 */
public class AlloySynthesizerBlockEntity extends BlockEntity implements MenuProvider {

    public static final int ENERGY_CAPACITY = 100_000;
    public static final int MAX_RECEIVE = 4_096;

    private final ItemStackHandler items = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final EnergyStorage energy = new EnergyStorage(ENERGY_CAPACITY, MAX_RECEIVE, 0) {
        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            int received = super.receiveEnergy(toReceive, simulate);
            if (!simulate && received > 0) {
                setChanged();
            }
            return received;
        }
    };

    private int progress;
    private int maxProgress = 40;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                case 2 -> energy.getEnergyStored();
                case 3 -> energy.getMaxEnergyStored();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
                default -> { }
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public AlloySynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALLOY_SYNTHESIZER.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return items;
    }

    public IEnergyStorage getEnergyStorage() {
        return energy;
    }

    public ContainerData getData() {
        return data;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlloySynthesizerBlockEntity be) {
        ItemStack inA = be.items.getStackInSlot(0);
        ItemStack inB = be.items.getStackInSlot(1);
        if (inA.isEmpty() || inB.isEmpty()) {
            be.progress = 0;
            return;
        }

        AlloyRecipeInput input = new AlloyRecipeInput(inA, inB);
        Optional<RecipeHolder<AlloyRecipe>> match = level.getRecipeManager()
                .getRecipeFor(ModRecipes.ALLOY_TYPE.get(), input, level);

        if (match.isEmpty()) {
            be.progress = 0;
            return;
        }

        AlloyRecipe recipe = match.get().value();
        ItemStack result = recipe.output();
        ItemStack outSlot = be.items.getStackInSlot(2);

        boolean canFit = outSlot.isEmpty()
                || (ItemStack.isSameItemSameComponents(outSlot, result)
                && outSlot.getCount() + result.getCount() <= outSlot.getMaxStackSize());

        if (!canFit) {
            be.progress = 0;
            return;
        }

        be.maxProgress = Math.max(1, recipe.processTicks());
        int energyPerTick = Math.max(1, recipe.energyCost() / be.maxProgress);

        if (be.energy.getEnergyStored() < energyPerTick) {
            return;
        }

        be.consumeInternalEnergy(energyPerTick);
        be.progress++;

        if (be.progress >= be.maxProgress) {
            be.items.extractItem(0, 1, false);
            be.items.extractItem(1, 1, false);
            if (outSlot.isEmpty()) {
                be.items.setStackInSlot(2, result.copy());
            } else {
                outSlot.grow(result.getCount());
            }
            be.progress = 0;
            be.setChanged();
        }
    }

    private void consumeInternalEnergy(int amount) {
        int next = Math.max(0, energy.getEnergyStored() - amount);
        CompoundTag tag = new CompoundTag();
        tag.putInt("Energy", next);
        energy.deserializeNBT(level != null ? level.registryAccess() : null, tag.get("Energy"));
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", items.serializeNBT(registries));
        tag.putInt("Energy", energy.getEnergyStored());
        tag.putInt("Progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) {
            items.deserializeNBT(registries, tag.getCompound("Items"));
        }
        if (tag.contains("Energy")) {
            energy.deserializeNBT(registries, tag.get("Energy"));
        }
        progress = tag.getInt("Progress");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.godtechascension.alloy_synthesizer");
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
        return new AlloySynthesizerMenu(containerId, playerInv, this, data);
    }
}

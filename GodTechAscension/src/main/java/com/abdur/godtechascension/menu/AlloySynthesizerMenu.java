package com.abdur.godtechascension.menu;

import com.abdur.godtechascension.block.AlloySynthesizerBlockEntity;
import com.abdur.godtechascension.registry.ModBlocks;
import com.abdur.godtechascension.registry.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * Container menu for the Alloy Synthesizer (2 inputs, 1 output + player inventory).
 */
public class AlloySynthesizerMenu extends AbstractContainerMenu {

    private final AlloySynthesizerBlockEntity blockEntity;
    private final ContainerData data;

    public AlloySynthesizerMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(
                containerId,
                playerInv,
                (AlloySynthesizerBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(6)
        );
    }

    public AlloySynthesizerMenu(
            int containerId,
            Inventory playerInv,
            AlloySynthesizerBlockEntity blockEntity,
            ContainerData data
    ) {
        super(ModMenus.ALLOY_SYNTHESIZER.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = data;

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 44, 35));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 66, 35));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 2, 124, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        addDataSlots(data);
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return Math.max(1, data.get(1));
    }

    public int getEnergyStored() {
        return ((data.get(3) & 0xFFFF) << 16) | (data.get(2) & 0xFFFF);
    }

    public int getMaxEnergy() {
        int max = ((data.get(5) & 0xFFFF) << 16) | (data.get(4) & 0xFFFF);
        return Math.max(1, max);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        if (index < 3) {
            if (!this.moveItemStackTo(stack, 3, 39, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(stack, 0, 2, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, stack);
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity != null && stillValid(
                ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player,
                ModBlocks.ALLOY_SYNTHESIZER.get()
        );
    }
}

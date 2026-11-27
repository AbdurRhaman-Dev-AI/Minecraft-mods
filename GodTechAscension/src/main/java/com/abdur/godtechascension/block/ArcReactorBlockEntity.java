package com.abdur.godtechascension.block;

import com.abdur.godtechascension.registry.ModBlockEntities;
import com.abdur.godtechascension.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fallback BlockEntity for Arc Reactor when Create is not installed.
 */
public class ArcReactorBlockEntity extends BlockEntity {

    private int soundTimer;

    public ArcReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARC_REACTOR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ArcReactorBlockEntity be) {
        if (level.hasNeighborSignal(pos)) {
            if (be.soundTimer == 0) {
                level.playSound(null, pos, ModSounds.ARC_REACTOR_LOOP.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            be.soundTimer++;
            if (be.soundTimer >= 235) {
                be.soundTimer = 0;
            }
        } else {
            be.soundTimer = 0;
        }
    }
}

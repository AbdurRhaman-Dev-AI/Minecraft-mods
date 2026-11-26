package com.abdur.godtechascension.block;

import com.abdur.godtechascension.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Validates the 4x2x2 (or 2x2x4) MK-V Arc Reactor multiblock structure
 * and checks whether any block in the formed reactor receives a Redstone signal (e.g., top Lever).
 */
public final class MkvMultiblockHelper {

    public record FormationResult(boolean formed, boolean powered, BlockPos minCorner) {
        public static final FormationResult INVALID = new FormationResult(false, false, BlockPos.ZERO);
    }

    private MkvMultiblockHelper() {
    }

    public static FormationResult checkFormation(Level level, BlockPos controllerPos) {
        // Test 4x2x2 and 2x2x4 footprints anchored around controllerPos
        int[][] sizes = {{4, 2, 2}, {2, 2, 4}};
        for (int[] size : sizes) {
            int sx = size[0];
            int sy = size[1];
            int sz = size[2];

            for (int ox = 0; ox < sx; ox++) {
                for (int oy = 0; oy < sy; oy++) {
                    for (int oz = 0; oz < sz; oz++) {
                        BlockPos min = controllerPos.offset(-ox, -oy, -oz);
                        FormationResult res = validateBox(level, min, sx, sy, sz, controllerPos);
                        if (res.formed()) {
                            return res;
                        }
                    }
                }
            }
        }
        return FormationResult.INVALID;
    }

    private static FormationResult validateBox(
            Level level, BlockPos min, int sx, int sy, int sz, BlockPos controllerPos) {
        int cores = 0;
        boolean powered = false;

        for (int dx = 0; dx < sx; dx++) {
            for (int dy = 0; dy < sy; dy++) {
                for (int dz = 0; dz < sz; dz++) {
                    BlockPos p = min.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(p);

                    if (p.equals(controllerPos)) {
                        if (!state.is(ModBlocks.MKV_REACTOR_CONTROLLER.get())) {
                            return FormationResult.INVALID;
                        }
                    } else if (state.is(ModBlocks.MKV_REACTOR_CORE.get())) {
                        cores++;
                    } else if (!state.is(ModBlocks.TITANIUM_CASING.get())) {
                        return FormationResult.INVALID;
                    }

                    if (!powered && level.hasNeighborSignal(p)) {
                        powered = true;
                    }
                }
            }
        }

        return cores >= 2 ? new FormationResult(true, powered, min) : FormationResult.INVALID;
    }
}

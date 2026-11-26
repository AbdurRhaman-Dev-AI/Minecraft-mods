package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.block.AlloySynthesizerBlock;
import com.abdur.godtechascension.block.ArcReactorBlock;
import com.abdur.godtechascension.block.QuantumWarpBeaconBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all blocks and machines for GodTech: Ascension.
 */
public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GodTechAscension.MODID);

    public static final DeferredBlock<Block> TITANIUM_ORE = BLOCKS.register(
            "titanium_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(2, 5),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(3.5F, 3.0F)
            )
    );

    public static final DeferredBlock<Block> DEEPSLATE_TITANIUM_ORE = BLOCKS.register(
            "deepslate_titanium_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(2, 5),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(4.5F, 3.0F)
                            .sound(SoundType.DEEPSLATE)
            )
    );

    public static final DeferredBlock<Block> TITANIUM_CASING = BLOCKS.register(
            "titanium_casing",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .requiresCorrectToolForDrops()
                            .strength(4.0F, 6.0F)
                            .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<Block> ARC_REACTOR = BLOCKS.register(
            "arc_reactor",
            () -> new ArcReactorBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 12.0F)
                            .lightLevel(state -> 14)
                            .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<Block> ALLOY_SYNTHESIZER = BLOCKS.register(
            "alloy_synthesizer",
            () -> new AlloySynthesizerBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .requiresCorrectToolForDrops()
                            .strength(4.5F, 8.0F)
                            .lightLevel(state -> 8)
                            .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<Block> QUANTUM_WARP_BEACON = BLOCKS.register(
            "quantum_warp_beacon",
            () -> new QuantumWarpBeaconBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .requiresCorrectToolForDrops()
                            .strength(4.0F, 10.0F)
                            .lightLevel(state -> 12)
                            .sound(SoundType.AMETHYST)
            )
    );

    private ModBlocks() {
    }
}

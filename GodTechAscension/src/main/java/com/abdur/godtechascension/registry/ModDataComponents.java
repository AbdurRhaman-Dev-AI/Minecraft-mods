package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers custom Minecraft 1.21.1 Data Components for GodTech: Ascension.
 */
public final class ModDataComponents {

    public record WarpTarget(String dimension, BlockPos pos) {
        public static final Codec<WarpTarget> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("dimension").forGetter(WarpTarget::dimension),
                        BlockPos.CODEC.fieldOf("pos").forGetter(WarpTarget::pos)
                ).apply(instance, WarpTarget::new)
        );

        public static final StreamCodec<ByteBuf, WarpTarget> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, WarpTarget::dimension,
                BlockPos.STREAM_CODEC, WarpTarget::pos,
                WarpTarget::new
        );
    }

    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GodTechAscension.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WarpTarget>> WARP_TARGET =
            DATA_COMPONENTS.registerComponentType(
                    "warp_target",
                    builder -> builder.persistent(WarpTarget.CODEC).networkSynchronized(WarpTarget.STREAM_CODEC)
            );

    private ModDataComponents() {
    }
}

package net.dolpen.mcmod.lib.search.position;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class Positions {

    /**
     * 足元少し前方の座標を返します。
     * ドロップアイテムを集約するときの座標などに。
     *
     * @param player 対象プレイヤー
     * @return だいたい足元
     */
    public static BlockPos foot(@Nonnull EntityPlayer player) {
        double direction = Math.PI * player.rotationYaw / 180;
        return new BlockPos(
                player.posX - Math.sin(direction) * 0.5d,
                player.posY + 0.5d,
                player.posZ + Math.cos(direction) * 0.5d
        );
    }

    public static AxisAlignedBB getAABB(BlockPos base, int range, boolean includeUnder) {
        int fixedRange = Math.max(0, range);
        BlockPos upperNE = base.add(fixedRange, fixedRange, -fixedRange);//
        BlockPos lowerSW = base.add(-fixedRange, includeUnder ? -fixedRange : 0, fixedRange);
        return new AxisAlignedBB(upperNE, lowerSW);
    }


    public static AxisAlignedBB getHorizontalAABB(BlockPos base, int range) {
        int fixedRange = Math.max(0, range);
        BlockPos ne = base.add(fixedRange, 0, -fixedRange);
        BlockPos sw = base.add(-fixedRange, 0, fixedRange);
        return new AxisAlignedBB(ne, sw);
    }

    public static AxisAlignedBB getUpperWithHeight(BlockPos base, int range, int maxHeight) {
        int fixedRange = Math.max(0, range);
        BlockPos upperNE = new BlockPos(base.getX(), maxHeight, base.getZ())
                .add(fixedRange, 0, -fixedRange);
        BlockPos lowerSW = base.add(-fixedRange, 0, fixedRange);
        return new AxisAlignedBB(upperNE, lowerSW);
    }
}

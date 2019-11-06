package net.dolpen.mcmod.ext.utils;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 座標関連
 */
public class Positions {

    /**
     * 足元少し前方の座標を返します。
     * ドロップアイテムを集約するときの座標などに。
     *
     * @param player 対象プレイヤー
     * @return
     */
    public static BlockPos foot(@Nonnull EntityPlayer player) {
        double direction = Math.PI * player.rotationYaw / 180;
        return new BlockPos(
                player.posX - Math.sin(direction) * 0.5d,
                player.posY + 0.5d,
                player.posZ + Math.cos(direction) * 0.5d
        );
    }


    /**
     * 六面の隣接 BlockPos を取得します
     *
     * @param base 原点
     * @return
     */
    public static Set<BlockPos> neighbors3D(@Nonnull BlockPos base) {
        return Sets.newHashSet(base.north(), base.east(), base.south(), base.west(), base.up(), base.down());
    }

    /**
     * 平面上の隣接 BlockPos を取得します
     *
     * @param base 原点
     * @return
     */
    public static Set<BlockPos> neighbors2D(@Nonnull BlockPos base) {
        return Sets.newHashSet(base.north(), base.east(), base.south(), base.west());
    }

    /**
     * 軸ごとに距離 axisRange 以内の立方体内部の BlockPos を全て取得します
     * BlockPos.getAllInBox から原点のみ除いたものです
     *
     * @param base      原点
     * @param axisRange 距離
     * @return
     */
    public static Set<BlockPos> getAllInRange(@Nonnull BlockPos base, int axisRange) {
        Set<BlockPos> positions = Sets.newHashSet();
        for (int x = -axisRange; x <= axisRange; x++) {
            for (int y = -axisRange; y <= axisRange; y++) {
                for (int z = -axisRange; z <= axisRange; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    positions.add(base.add(x, y, z));
                }
            }
        }
        return positions;
    }

    /**
     * 注目点と原点との軸距離を返します
     *
     * @param seeing 注目点
     * @param base   原点
     * @return 距離
     */
    public static int axisDistance3D(@Nonnull BlockPos seeing, @Nonnull BlockPos base) {
        return IntStream.of(
                seeing.getX() - base.getX(),
                seeing.getY() - base.getY(),
                seeing.getZ() - base.getZ()
        ).map(Math::abs).max().orElse(0);
    }

    /**
     * 注目点と原点との平面軸距離を返します
     *
     * @param seeing 注目点
     * @param base   原点
     * @return 距離
     */
    public static int axisDistance2D(@Nonnull BlockPos seeing, @Nonnull BlockPos base) {
        return IntStream.of(
                seeing.getX() - base.getX(),
                seeing.getZ() - base.getZ()
        ).map(Math::abs).max().orElse(0);
    }

    /**
     * 注目点と原点が投降どうかを返します
     *
     * @param seeing 注目点
     * @param base   原点
     * @return seeing.y < base.y
     */
    public static boolean isSameHeight(@Nonnull BlockPos seeing, @Nonnull BlockPos base) {
        return base.getY() == seeing.getY();
    }


    /**
     * 注目点が原点の下(yが小さい)かどうかを返します
     *
     * @param seeing 注目点
     * @param base   原点
     * @return seeing.y < base.y
     */
    public static boolean isUnder(@Nonnull BlockPos seeing, @Nonnull BlockPos base) {
        return base.getY() - seeing.getY() > 0;
    }

}

package net.dolpen.mcmod.lib.block.filter.state;


import net.minecraft.block.state.IBlockState;

public class BlockBreakToolFilter implements IBlockStateFilter {
    public static final BlockBreakToolFilter MINE = of(Type.MINE);
    public static final BlockBreakToolFilter DIG = of(Type.DIG);
    public static final BlockBreakToolFilter CUT = of(Type.CUT);
    public final Type type;

    private BlockBreakToolFilter(Type type) {
        this.type = type;
    }

    private static BlockBreakToolFilter of(Type type) {
        return new BlockBreakToolFilter(type);
    }

    @Override
    public boolean test(IBlockState blockState) {
        return type.toolClassName.equals(blockState.getBlock().getHarvestTool(blockState));
    }

    public enum Type {
        MINE("pickaxe"),
        CUT("axe"),
        DIG("shovel");

        public final String toolClassName;

        Type(String toolClassName) {
            this.toolClassName = toolClassName;
        }
    }
}

package net.dolpen.mcmod.lib.block.filter;


import net.minecraft.block.state.IBlockState;

public class ToolMatchRule implements IMatchRule {
    public static final ToolMatchRule MINE = of(Type.MINE);
    public static final ToolMatchRule DIG = of(Type.DIG);
    public static final ToolMatchRule CUT = of(Type.CUT);
    public final Type type;

    private ToolMatchRule(Type type) {
        this.type = type;
    }

    private static ToolMatchRule of(Type type) {
        return new ToolMatchRule(type);
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

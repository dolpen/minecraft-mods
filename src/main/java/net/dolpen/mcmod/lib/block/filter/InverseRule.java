package net.dolpen.mcmod.lib.block.filter;


import net.minecraft.block.state.IBlockState;

public class InverseRule implements IMatchRule {

    public final IMatchRule rule;

    private InverseRule(IMatchRule rule) {
        this.rule = rule;
    }


    public static InverseRule not(IMatchRule rule) {
        return new InverseRule(rule);
    }

    @Override
    public boolean test(IBlockState blockState) {
        return !rule.test(blockState);
    }

}

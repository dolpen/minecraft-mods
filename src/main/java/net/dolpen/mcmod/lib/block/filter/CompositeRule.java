package net.dolpen.mcmod.lib.block.filter;


import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CompositeRule implements IMatchRule {

    public final Type type;
    public final Set<IMatchRule> rules;

    private CompositeRule(Type type, IMatchRule... rules) {
        this.rules = Sets.newHashSet(rules);
        this.type = type;
    }

    private CompositeRule(Type type, Collection<IMatchRule> rules) {
        this.rules = Sets.newHashSet(rules);
        this.type = type;
    }

    public static CompositeRule any(IMatchRule... rules) {
        return new CompositeRule(Type.OR, rules);
    }

    public static CompositeRule all(IMatchRule... rules) {
        return new CompositeRule(Type.AND, rules);
    }

    public static CompositeRule anyName(String... names) {
        return new CompositeRule(
                Type.OR,
                Arrays.stream(names)
                        .map(BlockMatchRule::ofName)
                        .collect(Collectors.toSet())
        );
    }

    public static <T extends Block, U extends Class<T>> CompositeRule anyClass(U... parents) {
        return new CompositeRule(
                Type.OR,
                Arrays.stream(parents)
                        .map(ClassMatchRule::of)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public boolean test(IBlockState blockState) {
        if (this.type.equals(Type.AND)) {
            return rules.stream().allMatch(r -> r.test(blockState));
        } else {
            return
                    rules.stream().anyMatch(r -> r.test(blockState));
        }
    }

    public enum Type {
        OR,
        AND
    }
}

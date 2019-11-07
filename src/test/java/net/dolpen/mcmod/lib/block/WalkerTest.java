package net.dolpen.mcmod.lib.block;

import net.dolpen.mcmod.lib.block.walker.BoxWalker;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class WalkerTest {

    @Test
    public void testBoxWalker() {
        BlockPos base = new BlockPos(1, 2, 3);
        Assertions.assertEquals(3 * 3 * 3 - 1, new BoxWalker(1).apply(base).size());
    }

    @Test
    public void testBoxWalkerInSize() {
        BlockPos base = new BlockPos(1, 2, 3);
        Assertions.assertEquals(7 * 7 * 7 - 1, new BoxWalker(3).apply(base).size());
    }

}

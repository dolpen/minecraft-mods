package net.dolpen.mcmod.ext.mod;


import com.google.common.collect.Lists;
import net.dolpen.mcmod.ext.setting.Constants;
import net.dolpen.mcmod.ext.tile.TileInfinityStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

public class CustomTiles {

    public static final ArrayList<Entry> entries = Lists.newArrayList(
            new Entry("tile_infinity_storage", TileInfinityStorage.class)
    );

    public static void registerAllTile() {
        entries.forEach(
                e -> GameRegistry.registerTileEntity(
                        e.target,
                        new ResourceLocation(
                                Constants.MOD_NAME,
                                e.name
                        )
                )
        );
    }

    public static class Entry {
        public final String name;
        public final Class<? extends TileEntity> target;

        public Entry(String name, Class<? extends TileEntity> tileEntity) {
            this.name = name;
            this.target = tileEntity;
        }
    }

}

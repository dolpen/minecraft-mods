package net.dolpen.mcmod.ext.mod;


import com.google.common.collect.Lists;
import net.dolpen.mcmod.ext.block.BlockInfinityStorage;
import net.dolpen.mcmod.ext.setting.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class CustomBlocks {

    public static final Block infinity_storage = new BlockInfinityStorage();
    public static final ArrayList<Entry> entries = Lists.newArrayList(
            new Entry("infinity_storage", infinity_storage)
    );

    public static void registerAllBlock(IForgeRegistry<Block> blockRegistry) {
        entries.forEach(e -> blockRegistry.register(e.target));
    }

    public static void registerAllItemBlock(IForgeRegistry<Item> itemRegistry) {
        entries.forEach(e -> itemRegistry.register(e.toItemBlock()));
    }

    public static void registerAllModel() {
        entries.forEach(
                e -> ModelLoader.setCustomModelResourceLocation(
                        Item.getItemFromBlock(e.target), 0, e.toNormalModel()
                )
        );
    }

    public static class Entry {
        public final String name;
        public final Block target;

        public Entry(String name, Block block) {
            this.name = name;
            this.target = block.setUnlocalizedName(name).setRegistryName(Constants.MOD_ID, name);
        }

        public Item toItemBlock() {
            return new ItemBlock(target).setRegistryName(Constants.MOD_ID, name);
        }


        public ModelResourceLocation toNormalModel() {
            return new ModelResourceLocation(
                    new ResourceLocation(Constants.MOD_ID, name), "normal"
            );
        }


        public ModelResourceLocation toVariantModel(String variant) {
            return new ModelResourceLocation(
                    new ResourceLocation(Constants.MOD_ID, name), variant
            );
        }
    }

}

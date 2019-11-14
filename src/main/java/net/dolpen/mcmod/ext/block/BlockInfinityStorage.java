package net.dolpen.mcmod.ext.block;

import net.dolpen.mcmod.ext.gui.GuiInfinityStorageContainer;
import net.dolpen.mcmod.ext.gui.InfinityStorageContainer;
import net.dolpen.mcmod.ext.tile.TileInfinityStorage;
import net.dolpen.mcmod.lib.block.BlockAdvancedStorage;
import net.dolpen.mcmod.lib.tile.TileAdvanceStorage;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockInfinityStorage extends BlockAdvancedStorage {

    public BlockInfinityStorage() {
        super(new Material(MapColor.STONE));
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(2.0F);
        setResistance(30.0F);
        setDefaultState(this.blockState.getBaseState());
    }

    public static InfinityStorageContainer getContainer(EntityPlayer player, World world, int x, int y, int z) {
        return new InfinityStorageContainer(player.inventory, (TileInfinityStorage) world.getTileEntity(new BlockPos(x, y, z)));
    }

    public static GuiInfinityStorageContainer getGuiContainer(EntityPlayer player, World world, int x, int y, int z) {
        return new GuiInfinityStorageContainer(player.inventory, (TileInfinityStorage) world.getTileEntity(new BlockPos(x, y, z)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (tab == this.getCreativeTabToDisplayOn()) {
            list.add(new ItemStack(this, 1, 0));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + displayNBTItemStack(stack));
    }

    // GUI

    public String displayNBTItemStack(ItemStack itemStack) {
        if (itemStack.isEmpty() || !itemStack.hasTagCompound())
            return I18n.format("inventory.item.empty");
        NBTTagCompound tileInfo = itemStack.getTagCompound().getCompoundTag("tileInfo");
        NBTTagList tags = tileInfo.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        NBTTagCompound pool = tags.getCompoundTagAt(TileInfinityStorage.POOL);
        NBTTagCompound out = tags.getCompoundTagAt(TileInfinityStorage.POOL);
        int count = pool.getInteger("Quantity") + out.getInteger("Quantity");
        ItemStack dummy = new ItemStack(pool);
        dummy.setCount(count);
        return dummy.isEmpty()
                ? I18n.format("inventory.item.empty")
                : I18n.format("inventory.item.stack", dummy.getCount(), dummy.getDisplayName());
    }

    @Override
    protected TileAdvanceStorage createTileAdvanceStorage(World world, int meta) {
        return new TileInfinityStorage();
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return net.dolpen.mcmod.ext.setting.Constants.MOD_ID + "" + getName();
    }

    @Override
    public String getName() {
        return "infinity_block";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("blockitem.infinity_storage.name");
    }
}
package net.dolpen.mcmod.lib.block;

import net.dolpen.mcmod.ext.DolpenMain;
import net.dolpen.mcmod.lib.tile.TileAdvanceStorage;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

// Custom Storage Container(like vanilla BlockContainer)
public abstract class BlockAdvancedStorage extends Block implements IInteractionObject, ITileEntityProvider {


    protected BlockAdvancedStorage(Material material) {
        this(material, material.getMaterialMapColor());
    }

    protected BlockAdvancedStorage(Material material, MapColor color) {
        super(material, color);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    private Optional<TileAdvanceStorage> getTileStorage(World world, BlockPos pos) {
        return Optional.ofNullable(world.getTileEntity(pos))
                .filter(te -> te instanceof TileAdvanceStorage)
                .map(te -> (TileAdvanceStorage) te);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        getTileStorage(world, pos).ifPresent(tileStorage -> {
            tileStorage.fromItemStackWithNBT(stack);
            world.notifyBlockUpdate(pos, state, state, 3);
        });
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }

    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tileEntity, ItemStack stack) {
        Optional<TileAdvanceStorage> tileStorageOptional = Optional.ofNullable(tileEntity)
                .filter(te -> te instanceof TileAdvanceStorage)
                .map(te -> (TileAdvanceStorage) te);
        if (world.isRemote || !tileStorageOptional.isPresent()) {
            super.harvestBlock(world, player, pos, state, tileEntity, stack);
        } else {
            TileAdvanceStorage tileStorage = tileStorageOptional.get();
            float xOffset = world.rand.nextFloat() * 0.6f + 0.2f;
            float yOffset = world.rand.nextFloat() * 0.6f + 0.2f;
            float zOffset = world.rand.nextFloat() * 0.6f + 0.2f;
            ItemStack nbtStack = tileStorage.toItemStackWithNBT(this);
            EntityItem entityitem = new EntityItem(world, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, nbtStack.splitStack(1));
            world.spawnEntity(entityitem);
        }
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        if (!player.isSneaking()) {
            getTileStorage(world, pos).ifPresent(tileStorage -> {
                tileStorage.sync(player);
                player.openGui(DolpenMain.getInstance(), tileStorage.getGuiId(), world, pos.getX(), pos.getY(), pos.getZ());
            });
        }


        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return createTileAdvanceStorage(world, meta);
    }

    protected abstract TileAdvanceStorage createTileAdvanceStorage(World world, int meta);


}
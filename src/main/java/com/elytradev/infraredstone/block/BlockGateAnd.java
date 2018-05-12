package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityGateAnd;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockGateAnd extends BlockModule<TileEntityGateAnd> implements IBlockBase {

    protected String name;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool FRONT_ACTIVE = PropertyBool.create("front_active");
    public static final PropertyBool LEFT_ACTIVE = PropertyBool.create("left_active");
    public static final PropertyBool RIGHT_ACTIVE = PropertyBool.create("right_active");
    public static int FACE = 3;

    public BlockGateAnd() {
        super(Material.CIRCUITS, "gate_and");
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FRONT_ACTIVE, false).withProperty(LEFT_ACTIVE, false).withProperty(RIGHT_ACTIVE, false));

        this.setHardness(0.5f);
    }

    //TODO: make this pop off when the block it's on is broken, also maybe by water but ¯\_(ツ)_/¯

    @Override
    public Class<TileEntityGateAnd> getTileEntityClass() {
        return TileEntityGateAnd.class;
    }

    @Override
    public TileEntityGateAnd createTileEntity(World world, IBlockState state) {
        return new TileEntityGateAnd();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && !player.isSneaking() && world.getTileEntity(pos) instanceof TileEntityGateAnd) {
            TileEntityGateAnd te = (TileEntityGateAnd)world.getTileEntity(pos);
            te.toggleInvert();
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2/16.0, 1.0D);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, FRONT_ACTIVE, LEFT_ACTIVE, RIGHT_ACTIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        int meta = 0;
        meta |= state.getValue(FACING).getHorizontalIndex();
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        int facebits = meta & FACE;
        EnumFacing facing = EnumFacing.getHorizontal(facebits);
        return blockState.getBaseState().withProperty(FACING, facing);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te==null || !(te instanceof TileEntityGateAnd)) return state;
        TileEntityGateAnd and = (TileEntityGateAnd)te;
        return state.withProperty(FRONT_ACTIVE, and.isActive()).withProperty(LEFT_ACTIVE, and.isLeftActive()).withProperty(RIGHT_ACTIVE, and.isRightActive());
    }

//    @Override
//    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
//        return getStrongPower(state, world, pos, side);
//    }
//
//    @Override
//    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
//        if (side!=state.getValue(FACING).getOpposite()) return 0;
//        TileEntity te = world.getTileEntity(pos);
//        if (te!=null && te instanceof TileEntityGateAnd) {
//            return ((TileEntityGateAnd)te).isActive()?16:0;
//            //int result = (te).getCapability(InfraRedstone.CAPABILITY_IR, null).getSignalValue();
//            //return (result > 16) ? 16 : result;
//        }
//        return 0;
//    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(FRONT_ACTIVE, false).withProperty(LEFT_ACTIVE, false).withProperty(RIGHT_ACTIVE, false);
    }
}

package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockGateXor;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.logic.InRedLogic;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;

import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityGateXor extends TileEntityIRComponent implements ITickable {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();
    private int valLeft;
    private int valRight;

    //Transient data to throttle sync down here
    boolean lastActive = false;
    int lastValLeft = 0;
    int lastValRight = 0;

    public void update() {
        if (world.isRemote || !hasWorld()) return;

        IBlockState state = world.getBlockState(this.getPos());

        if (InRedLogic.isIRTick()) {
            //IR tick means we're searching for a next value
            if (state.getBlock() instanceof BlockGateXor) {
                EnumFacing left = state.getValue(BlockGateXor.FACING).rotateYCCW();
                EnumFacing right = state.getValue(BlockGateXor.FACING).rotateY();
                int sigLeft = InRedLogic.findIRValue(world, pos, left);
                int sigRight = InRedLogic.findIRValue(world, pos, right);
                valLeft = sigLeft;
                valRight = sigRight;
                if (sigLeft > 0 && sigRight == 0) {
                    signal.setNextSignalValue(sigLeft);
                } else if (sigLeft == 0 && sigRight > 0) {
                    signal.setNextSignalValue(sigRight);
                } else {
                    signal.setNextSignalValue(0);
                }
                markDirty();
            }

        } else {
            //Not an IR tick, so this is a "copy" tick. Adopt the previous tick's "next" value.
            signal.setSignalValue(signal.getNextSignalValue());
            markDirty();
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability==InfraRedstone.CAPABILITY_IR) {
            if (world==null) return true;
            if (facing==null) return true;
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock()==ModBlocks.GATE_XOR) {
                EnumFacing gateXorFront = state.getValue(BlockGateXor.FACING);
                if (gateXorFront==facing) {
                    return true;
                } else if (gateXorFront==facing.rotateYCCW()) {
                    return true;
                } else if (gateXorFront==facing.rotateY()) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability==InfraRedstone.CAPABILITY_IR) {
            if (world==null) return (T)InfraRedstoneHandler.ALWAYS_OFF;
            if (facing==null) return (T) signal;

            IBlockState state = world.getBlockState(pos);
            if (state.getBlock()==ModBlocks.GATE_XOR) {
                EnumFacing gateXorFront = state.getValue(BlockGateXor.FACING);
                if (gateXorFront==facing) {
                    return (T) signal;
                } else if (gateXorFront==facing.rotateYCCW()) {
                    return (T) InfraRedstoneHandler.ALWAYS_OFF;
                } else if (gateXorFront==facing.rotateY()) {
                    return (T)InfraRedstoneHandler.ALWAYS_OFF;
                } else {
                    return null;
                }
            }
            return (T)InfraRedstoneHandler.ALWAYS_OFF; //We can't tell what our front face is, so supply a dummy that's always-off.
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setTag("Signal", InfraRedstone.CAPABILITY_IR.writeNBT(signal, null));
        //please forgive me, falk. We'll work on moving these out soon.
        tag.setInteger("Left", valLeft);
        tag.setInteger("Right", valRight);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Signal")) InfraRedstone.CAPABILITY_IR.readNBT(signal, null, compound.getTag("Signal"));
        valLeft = compound.getInteger("Left");
        valRight = compound.getInteger("Right");
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
        IBlockState state = world.getBlockState(pos);
        getWorld().markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2 | 16);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public void markDirty() {
        super.markDirty();
        // please excuse the black magic
        if (!hasWorld() || getWorld().isRemote) return;

        if (valLeft!=lastValLeft
                || valRight!=lastValRight
                || isActive()!=lastActive) { //Throttle updates - only send when something important changes

            WorldServer ws = (WorldServer)getWorld();
            Chunk c = getWorld().getChunkFromBlockCoords(getPos());
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
            for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
                if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
                    player.connection.sendPacket(packet);
                }
            }

            lastValLeft = valLeft;
            lastValRight = valRight;
            lastActive = isActive();

            IBlockState state = world.getBlockState(pos);
            ws.markAndNotifyBlock(pos, c, state, state, 1 | 16);
        }
    }

    public boolean isActive() {
        return signal.getSignalValue()!=0;
    }
    public boolean isLeftActive() {
        return valLeft!=0;
    }
    public boolean isRightActive() {
        return valRight!=0;
    }
}
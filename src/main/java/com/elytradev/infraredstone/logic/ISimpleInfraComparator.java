package com.elytradev.infraredstone.logic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Version of IInfraComparator that can be implemented by non-TE Blocks.
 */
public interface ISimpleInfraComparator {

	/**
	 * Has extra information from the standard IInfraComparator to compensate for not having a TE
	 * @param world the current world.
	 * @param pos the position of your object.
	 * @param state the current blockstate of your object.
	 * @param inspectingFrom the direction the infra-comparator is looking from.
	 * @return a value from 0-63 depending on the state of your object and the given parameters.
	 * It may be helpful to format the value in binaru: `0b00_0000`
	 */
	int getComparatorValue(World world, BlockPos pos, IBlockState state, EnumFacing inspectingFrom);
}

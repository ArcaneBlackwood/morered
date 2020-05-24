package com.github.commoble.morered;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class GateBlockStateProperties
{
	public static final DirectionProperty ATTACHMENT_DIRECTION = BlockStateProperties.FACING;
	public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0,3);

	
	public static Direction getOutputDirection(BlockState state)
	{
		if (!state.has(ATTACHMENT_DIRECTION) || !state.has(ROTATION))
		{
			return Direction.DOWN;
		}
		
		Direction attachmentDirection = state.get(ATTACHMENT_DIRECTION);
		int rotationIndex = state.get(ROTATION);
		
		return BlockStateUtil.getOutputDirection(attachmentDirection, rotationIndex);
	}
	
	public static BlockState getStateForPlacedGatePlate(BlockState state, BlockItemUseContext context)
	{
		Direction attachmentDirection = context.getFace().getOpposite();
		int rotationIndex = BlockStateUtil.getRotationIndexForPlacement(attachmentDirection, context.getNearestLookingDirections());
		if (state.has(ATTACHMENT_DIRECTION) && state.has(ROTATION))
		{
			return state.with(ATTACHMENT_DIRECTION, attachmentDirection).with(ROTATION, rotationIndex);
		}
		else
		{
			return state;
		}
	}
}

package commoble.morered.bitwise_logic;

import java.util.EnumSet;

import javax.annotation.Nonnull;

import commoble.morered.MoreRed;
import commoble.morered.api.ChanneledPowerSupplier;
import commoble.morered.api.MoreRedAPI;
import commoble.morered.plate_blocks.AnalogPowerStorageBlockEntity;
import commoble.morered.plate_blocks.InputSide;
import commoble.morered.plate_blocks.PlateBlock;
import commoble.morered.plate_blocks.PlateBlockStateProperties;
import commoble.morered.util.BlockStateUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

//The names not too long, I swear o.o
public class SingleInputBitwiseAnalogLogicPlateBlock extends BitwiseLogicPlateBlock {
	public BusToSingleFunction function;
	
	public SingleInputBitwiseAnalogLogicPlateBlock(Properties properties, BusToSingleFunction function)
	{
		super(properties);
		this.function = function;
		BlockState baseState = this.defaultBlockState();
		this.registerDefaultState(baseState
			.setValue(BlockStateProperties.POWERED, false));
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return MoreRed.get().analogLogicGateBeType.get().create(pos, state);
	}
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED);
	}
	
	

	@Override
	protected void updatePower(Level level, BlockPos thisPos, BlockState thisState)
	{
		BlockEntity be = level.getBlockEntity(thisPos);
		if (be instanceof AnalogPowerStorageBlockEntity powerBe)
		{
			// get capability from output side
			Direction outputDir = PlateBlockStateProperties.getOutputDirection(thisState);
			Direction inputDir = outputDir.getOpposite();
			BlockPos inputPos = thisPos.relative(inputDir);
			BlockEntity inputTE = level.getBlockEntity(inputPos);
			ChanneledPowerSupplier inputSupplier = inputTE == null
				? BitwiseLogicPlateBlock.NO_POWER_SUPPLIER
				: inputTE.getCapability(MoreRedAPI.CHANNELED_POWER_CAPABILITY, inputDir.getOpposite()).orElse(NO_POWER_SUPPLIER);
			Direction attachmentDir = thisState.getValue(PlateBlockStateProperties.ATTACHMENT_DIRECTION);
			
			
			char a = 0;
			for (int i=0; i<16; i++)
			{
				a = (char)(a | ((inputSupplier.getPowerOnChannel(level, thisPos, thisState, attachmentDir, i) > 0 ? 1 : 0) << i));
			}
			int out = function.apply((char)0, a, (char)0);
			
			
			powerBe.setPower(out);
			notifyNeighbors(level, thisPos, thisState);
			BlockState newBlockState = thisState.setValue(BlockStateProperties.POWERED, out > 0);
			level.setBlock(thisPos, newBlockState, 2);
		}
	}

	
	@Override
	public boolean canConnectToAdjacentCable(@Nonnull BlockGetter world, @Nonnull BlockPos thisPos, @Nonnull BlockState thisState, @Nonnull BlockPos wirePos, @Nonnull BlockState wireState, @Nonnull Direction wireFace, @Nonnull Direction directionToWire)
	{
		Direction plateAttachmentDir = thisState.getValue(PlateBlock.ATTACHMENT_DIRECTION);
		Direction attachmentDir = thisState.getValue(PlateBlockStateProperties.ATTACHMENT_DIRECTION);
		int rotationIndex = thisState.getValue(PlateBlockStateProperties.ROTATION);
		Direction inputSideB = BlockStateUtil.getInputDirection(attachmentDir, rotationIndex, InputSide.B.rotationsFromOutput);
		return plateAttachmentDir == wireFace &&
			(directionToWire == inputSideB);
	}

	
	
	
	
	@Deprecated
	@Override
	public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction sideOfAdjacentBlock)
	{

		Direction outputDirectionWhenPowered = PlateBlockStateProperties.getOutputDirection(state);
		if (sideOfAdjacentBlock.getOpposite() != outputDirectionWhenPowered)
			return 0;
		
		byte power = 0;
		BlockEntity be = blockAccess.getBlockEntity(pos);
		if (be instanceof AnalogPowerStorageBlockEntity powerBe)
			power = (byte) powerBe.getPower();
		return power;
	}
	
	// Get the redstone power output that can be conducted indirectly through solid cubes
	@Deprecated
	@Override
	public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getSignal(blockAccess, pos, side);
	}
		
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side)
	{
		if (side == null)
			return false;
		
		Direction primaryOutputDirection = PlateBlockStateProperties.getOutputDirection(state);
		if (side == primaryOutputDirection.getOpposite())
			return true;
		
		// check input sides
		Direction attachmentDirection = state.getValue(PlateBlockStateProperties.ATTACHMENT_DIRECTION);
		int baseRotation = state.getValue(PlateBlockStateProperties.ROTATION);
		Direction inputDirection = BlockStateUtil.getInputDirection(attachmentDirection, baseRotation, 0);
		if (side == inputDirection.getOpposite())
			return true;
		return false;
	}
	public void notifyNeighbors(Level level, BlockPos pos, BlockState state)
	{
		EnumSet<Direction> outputDirections = EnumSet.of(PlateBlockStateProperties.getOutputDirection(state));
		if (!net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(level, pos, level.getBlockState(pos), outputDirections, false).isCanceled())
		{
			for (Direction outputDirection : outputDirections)
			{
				BlockPos outputPos = pos.relative(outputDirection);
				{
					level.neighborChanged(outputPos, this, pos);
				}
				level.updateNeighborsAtExceptFromFacing(outputPos, this, outputDirection.getOpposite());
			}
		}
	}
}

package commoble.morered.bitwise_logic;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import commoble.morered.MoreRed;
import commoble.morered.api.voxels.BlockBuilder;
import commoble.morered.api.voxels.IndexedVoxelShape;
import commoble.morered.api.voxels.MultiIndexedVoxelShape;
import commoble.morered.api.voxels.VoxelShapeBlockHitResult;
import commoble.morered.plate_blocks.PlateBlock;
import commoble.morered.plate_blocks.PlateBlockStateProperties;
import commoble.morered.util.BlockStateUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class MatrixInputBlock extends BitwiseLogicPlateBlock {
	protected static ImmutableMap<Direction, ImmutableMap<Rotation, ImmutableSet<IndexedVoxelShape>>> shapeTemplates;
	protected static ImmutableMap<Direction, VoxelShape> shapeBase;
	private static void BuildShapeTemplates() {
		ImmutableMap.Builder<Direction, ImmutableMap<Rotation, ImmutableSet<IndexedVoxelShape>>> directionBuilder = new ImmutableMap.Builder<>();
		ImmutableMap.Builder<Direction, VoxelShape> baseBuilder = new ImmutableMap.Builder<>();
		
		ArrayList<BlockBuilder> builders = new ArrayList<>();
		for (Direction direction : Direction.values()) {
			ImmutableMap.Builder<Rotation, ImmutableSet<IndexedVoxelShape>> rotationsBuilder = new ImmutableMap.Builder<>();
			for (Rotation rotation : Rotation.values()) {
				ImmutableSet.Builder<IndexedVoxelShape> shapesBuilder = new ImmutableSet.Builder<>();
				builders.clear();
				
				builders.add(new BlockBuilder(0, 0, 0, 16, 5, 16));
				
				for (int y=0; y<4; y++)
					for (int x=0; x<4; x++)
						builders.add(new BlockBuilder(
								x*4+1,5,y*3+3,
								x*4+3,7,y*3+5));

				int counter = -1;
				for (BlockBuilder b : builders) {
					b.rotate(Axis.Y, rotation);
					b.setDirection(direction);
					shapesBuilder.add(new IndexedVoxelShape(b.compile(), counter++));
				}
				
				rotationsBuilder.put(rotation, shapesBuilder.build());
			}
			baseBuilder.put(direction, builders.get(0).compile());
			directionBuilder.put(direction, rotationsBuilder.build());
		}
		shapeBase = baseBuilder.build();
		shapeTemplates = directionBuilder.build();
	}
	
	
	public MatrixInputBlock(Properties properties) {
		super(properties);
		
		if (shapeTemplates == null)
			BuildShapeTemplates();
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return MoreRed.get().matrixInputBeType.get().create(pos, state);
	}

	
	
	///TODO: NEEDS REPAIR, DOESNT WORK WITH LEVER OR REDSTONE DUST 
	byte getAnalogInput(LevelReader level, BlockState state, BlockPos pos) {
		Direction attachmentDirection = state.getValue(PlateBlockStateProperties.ATTACHMENT_DIRECTION);
		int baseRotation = state.getValue(PlateBlockStateProperties.ROTATION);
		
		Direction inputDirection = BlockStateUtil.getInputDirection(attachmentDirection, baseRotation, 2);
		

		BlockPos inputPos = pos.relative(inputDirection);

		int power = level.getSignal(inputPos, inputDirection);
		MoreRed.LOGGER.debug("========= Redstone level "+power);
		if (power > 0) {
			return (byte)power;
		} else {
			BlockState inputState = level.getBlockState(inputPos);
			return inputState.getBlock() == Blocks.REDSTONE_WIRE ? inputState.getValue(RedStoneWireBlock.POWER).byteValue() : 0;
		}
	}
	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);

		BlockEntity be = level.getBlockEntity(pos);
		MoreRed.LOGGER.debug("MatrixInputBlock onNeighborChange");
		if (be instanceof MatrixInputBlockEntity matrixBe) {
			byte inputPower = getAnalogInput(level, state, pos);
			if (matrixBe.SetReset(inputPower > 0));
				level.scheduleTick(pos, this, TICK_DELAY, TickPriority.HIGH);
		}
	}
	@Override
	protected void updatePower(Level level, BlockPos pos, BlockState state) {
		BlockEntity be = level.getBlockEntity(pos);
		MoreRed.LOGGER.debug("MatrixInputBlock updatePower");
		if (be instanceof MatrixInputBlockEntity matrixBe) {
			if (matrixBe.GetReset()) {
				MoreRed.LOGGER.debug("MatrixInputBlock Reset");
				matrixBe.setPower(new byte[16]);
				matrixBe.markDirty();
			}
		}
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
		if (side == primaryOutputDirection) //Input direction, 180 from output
			return true;
		return false;
	}

	@Override
	public boolean canConnectToAdjacentCable(BlockGetter world, BlockPos thisPos, BlockState thisState,
			BlockPos wirePos, BlockState wireState, Direction wireFace, Direction directionToWire) {
		Direction plateAttachmentDir = thisState.getValue(PlateBlock.ATTACHMENT_DIRECTION);
		Direction primaryOutputDirection = PlateBlockStateProperties.getOutputDirection(thisState);
		return plateAttachmentDir == wireFace &&
			(directionToWire == primaryOutputDirection); // || directionToWire == primaryOutputDirection.getOpposite()
	}
	
	protected MultiIndexedVoxelShape generateIndexedShape(BlockState state, BlockGetter level, BlockPos pos) {
		Direction direction = state.getValue(ATTACHMENT_DIRECTION);
		Integer intRotation = state.getValue(ROTATION);
		Rotation rotation = Rotation.NONE;
		switch (intRotation) {
		case 0:
			rotation = Rotation.NONE;
			break;
		case 1:
			rotation = Rotation.CLOCKWISE_90;
			break;
		case 2:
			rotation = Rotation.CLOCKWISE_180;
			break;
		case 3:
			rotation = Rotation.COUNTERCLOCKWISE_90;
			break;
		}
		return new MultiIndexedVoxelShape(shapeTemplates.get(direction).get(rotation));
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof MatrixInputBlockEntity matrixBe) {
			if (matrixBe.shape==null)
				matrixBe.shape = generateIndexedShape(state, level, pos);
			return matrixBe.shape;
		} else
			return generateIndexedShape(state, level, pos);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
		Direction direction = state.getValue(ATTACHMENT_DIRECTION);
		return shapeBase.get(direction);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity be = level.getBlockEntity(pos);
		InteractionResult superResult = super.use(state, level, pos, player, hand, hit);
		if (superResult != InteractionResult.PASS) {
			if (be instanceof MatrixInputBlockEntity matrixBe)
				matrixBe.shape = null;
			return superResult;
		}
		
		if (be instanceof MatrixInputBlockEntity matrixBe) {
			if (matrixBe.shape==null)
				matrixBe.shape = generateIndexedShape(state, level, pos);
			MultiIndexedVoxelShape shape = matrixBe.shape;
			VoxelShapeBlockHitResult clipped = shape.clip(player.getEyePosition(), player.getEyePosition().add(player.getForward().scale(player.getBlockReach())), pos);
			
			if (clipped==null) return InteractionResult.PASS;
			if (clipped.subHit < 0) return InteractionResult.PASS;
			if (getAnalogInput(level, state, pos) > 0) return InteractionResult.CONSUME;

			boolean power = matrixBe.getPower(clipped.subHit) > 0;
			float f = power ? 0.5F : 0.6F;
			level.playSound((Player)null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
			matrixBe.setPower(clipped.subHit, power ? 0 : 31);

			matrixBe.markDirty();
			return InteractionResult.SUCCESS;
		}
		
		//BlockPos localPos = hit.getBlockPos().subtract(pos);
		return InteractionResult.PASS;
	}

}

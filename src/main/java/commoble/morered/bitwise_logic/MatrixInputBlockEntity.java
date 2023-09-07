package commoble.morered.bitwise_logic;

import commoble.morered.MoreRed;
import commoble.morered.api.voxels.MultiIndexedVoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MatrixInputBlockEntity extends ChanneledPowerStorageBlockEntity {
	public MultiIndexedVoxelShape shape;
	protected boolean resetEnabled = false;
	
	public static MatrixInputBlockEntity create(BlockPos pos, BlockState state)
	{
		return new MatrixInputBlockEntity(MoreRed.get().matrixInputBeType.get(), pos, state);
	}
	public MatrixInputBlockEntity(BlockPos pos, BlockState state)
	{
		this(MoreRed.get().matrixInputBeType.get(), pos, state);
	}
	public MatrixInputBlockEntity(BlockEntityType<? extends MatrixInputBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	
	public void markDirty() {
		BlockState state = this.getBlockState();
		if (!this.level.isClientSide)
			this.setChanged();
		this.level.sendBlockUpdated(this.worldPosition, state, state, 0);
		
	}
	public boolean SetReset(boolean newReset) {
		if (resetEnabled != newReset) {
			resetEnabled = newReset;
			markDirty();
			return true;
		}
		return false;
		
	}
	public boolean GetReset() {
		return resetEnabled;
	}
	

	
	public static final String RESET = "reset";
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.putBoolean(RESET, resetEnabled);
	}
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		resetEnabled = compound.getBoolean(RESET);
	}
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}
		
	@Override
	public CompoundTag getUpdateTag() {
	  CompoundTag tag = new CompoundTag();
	  saveAdditional(tag);
	  return tag;
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		  return ClientboundBlockEntityDataPacket.create(this);
	}
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
		markDirty();
	}
}

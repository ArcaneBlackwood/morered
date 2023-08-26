package commoble.morered.plate_blocks;

import commoble.morered.MoreRed;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AnalogPowerStorageBlockEntity extends BlockEntity {
	public static final String POWER = "power";
	protected int power;
	
	public static AnalogPowerStorageBlockEntity create(BlockPos pos, BlockState state)
	{
		return new AnalogPowerStorageBlockEntity(MoreRed.get().analogLogicGateBeType.get(), pos, state);
	}
	public AnalogPowerStorageBlockEntity(BlockPos pos, BlockState state)
	{
		this(MoreRed.get().analogLogicGateBeType.get(), pos, state);
	}
	public AnalogPowerStorageBlockEntity(BlockEntityType<? extends AnalogPowerStorageBlockEntity> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	public int getPower() {
		return this.power;
	}
	public void setPower(int power) {
		if (power < 0)
			this.power = 0;
		else if (power > 16)
			this.power = 16;
		else
			this.power = power;
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.putByte(POWER, (byte)this.power);
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		this.power = compound.getByte(POWER);
	}
}

package commoble.morered.api.voxels;


import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockBuilder {
	public int minX, minY, minZ, maxX, maxY, maxZ;
	public BlockBuilder(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	
	@SuppressWarnings("incomplete-switch")
	public void rotate(Axis axis, Rotation rotation) {
		int horzMax = 0, horzMin = 0, vertMax = 0, vertMin = 0;
		switch (axis) {
		case X:
			horzMax = maxZ;
			horzMin = minZ;
			vertMax = maxY;
			vertMin = minY;
			break;
		case Y:
			horzMax = maxX;
			horzMin = minX;
			vertMax = maxZ;
			vertMin = minZ;
			break;
		case Z:
			horzMax = maxX;
			horzMin = minX;
			vertMax = maxY;
			vertMin = minY;
			break;
		}
		
		switch (rotation) {
		    case CLOCKWISE_180:
		        // Rotate 180 degrees clockwise
		        int tempMinX = horzMin;
		        int tempMaxX = horzMax;
		        int tempMinY = vertMin;
		        int tempMaxY = vertMax;
	
		        horzMin = 16 - tempMaxX;
		        horzMax = 16 - tempMinX;
		        vertMin = 16 - tempMaxY;
		        vertMax = 16 - tempMinY;
		        break;
	
		    case CLOCKWISE_90:
		        // Rotate 90 degrees clockwise
		        int tempMinX90 = horzMin;
		        int tempMaxX90 = horzMax;
		        int tempMinY90 = vertMin;
		        int tempMaxY90 = vertMax;
	
		        horzMin = 16 - tempMaxY90;
		        horzMax = 16 - tempMinY90;
		        vertMin = tempMinX90;
		        vertMax = tempMaxX90;
		        break;
	
		    case COUNTERCLOCKWISE_90:
		        // Rotate 90 degrees counterclockwise
		        int tempMinX270 = horzMin;
		        int tempMaxX270 = horzMax;
		        int tempMinY270 = vertMin;
		        int tempMaxY270 = vertMax;
	
		        horzMin = tempMinY270;
		        horzMax = tempMaxY270;
		        vertMin = 16 - tempMaxX270;
		        vertMax = 16 - tempMinX270;
		        break;
		}
		if (horzMin > horzMax || vertMin > vertMax) {
		    throw new IllegalStateException("Invalid cube configuration after rotation: min > max");
		}
		
		
		
		switch (axis) {
		case X:
			 maxZ = horzMax;
			 minZ = horzMin;
			 maxY = vertMax;
			 minY = vertMin;
			break;
		case Y:
			 maxX = horzMax;
			 minX = horzMin;
			 maxZ = vertMax;
			 minZ = vertMin;
			break;
		case Z:
			 maxX = horzMax;
			 minX = horzMin;
			 maxY = vertMax;
			 minY = vertMin;
			break;
		}
		
	}
	
	
	public void flip(Axis axis) {
		int newMax, newMin;
		switch (axis) {
		case X:
			newMin = 16 - maxX;
			newMax = 16 - minX;
			minX = newMin;
			maxX = newMax;
			break;
		case Y:
			newMin = 16 - maxY;
			newMax = 16 - minY;
			minY = newMin;
			maxY = newMax;
			break;
		case Z:
			newMin = 16 - maxZ;
			newMax = 16 - minZ;
			minZ = newMin;
			maxZ = newMax;
			break;
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	public void setDirection(Direction direction) {
		switch (direction) {
		case UP:
			rotate(Axis.Y, Rotation.CLOCKWISE_180);
			rotate(Axis.X, Rotation.CLOCKWISE_180);
			break;
		case NORTH:
			rotate(Axis.X, Rotation.COUNTERCLOCKWISE_90);
			break;
		case SOUTH:
			rotate(Axis.Y, Rotation.CLOCKWISE_180);
			rotate(Axis.X, Rotation.CLOCKWISE_90);
			break;
		case EAST:
			rotate(Axis.Y, Rotation.CLOCKWISE_90);
			rotate(Axis.Z, Rotation.CLOCKWISE_90);
			break;
		case WEST:
			rotate(Axis.Y, Rotation.COUNTERCLOCKWISE_90);
			rotate(Axis.Z, Rotation.COUNTERCLOCKWISE_90);
			break;
		}
	}
	public VoxelShape compile() {
		return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
	}
}

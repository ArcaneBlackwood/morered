package commoble.morered.api.voxels;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Represents a RayTraceResult generated by a {@link IndexedVoxelShape} or {@link MultiIndexedVoxelShape}.
 * <p>
 * Provides easy access to the distance, as well as the {@link IndexedVoxelShape} which the ray hit.
 * <p>
 * Created by covers1624 on 8/9/2016.
 */
public class VoxelShapeBlockHitResult extends BlockHitResult implements Comparable<VoxelShapeBlockHitResult> {

    public IndexedVoxelShape shape;
	public final double dist;
    public final Object hitInfo;
    public final int subHit;


    public VoxelShapeBlockHitResult(BlockHitResult other, IndexedVoxelShape shape, double dist) {
        this(other.getLocation(), other.getDirection(), other.getBlockPos(), other.isInside(), shape, dist);
        this.shape = shape;
    }

    public VoxelShapeBlockHitResult(Vec3 hit, Direction side, BlockPos pos, boolean isInside, IndexedVoxelShape shape, double dist) {
        this(false, hit, side, pos, isInside, shape, dist);
        this.shape = shape;
    }

    protected VoxelShapeBlockHitResult(boolean isMissIn, Vec3 hitVec, Direction faceIn, BlockPos posIn, boolean isInside, IndexedVoxelShape shape, double dist) {
    	super(isMissIn, hitVec, faceIn, posIn, isInside);
        if (shape.getData() instanceof Integer d) {
            subHit = d;
        } else {
            subHit = -1;
        }
        hitInfo = shape.getData();
        this.dist = dist;
        this.shape = shape;
    }
    

    @Override
    public VoxelShapeBlockHitResult withDirection(Direction newFace) {
        return new VoxelShapeBlockHitResult(getType() == Type.MISS, getLocation(), newFace, getBlockPos(), isInside(), shape, dist);
    }

    @Override
    public String toString() {
        return (super.toString().replace("}", "") + ", subHit=" + subHit + ", sqDist: " + dist + "}").replace("}", "") + ", cuboid=" + shape.toString() + "}";
    }
    
    @Override
    public int compareTo(VoxelShapeBlockHitResult o) {
        return Double.compare(dist, o.dist);
    }
}
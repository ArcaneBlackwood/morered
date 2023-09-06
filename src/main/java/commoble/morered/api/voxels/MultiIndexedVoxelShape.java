package commoble.morered.api.voxels;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MultiIndexedVoxelShape extends VoxelShape {


    private final VoxelShape merged;
    private final ImmutableSet<IndexedVoxelShape> shapes;
    public Object hitShapeData;

    /**
     * Construct a MultiIndexedVoxelShape, using the combination of all the sub-components
     * as this VoxelShape.
     *
     * @param shapes The sub-components.
     */
    public MultiIndexedVoxelShape(ImmutableSet<IndexedVoxelShape> shapes) {
        this(merge(castSet(shapes)), shapes);
    }
    public static VoxelShape merge(ImmutableSet<VoxelShape> shapes) {
        VoxelShape shape = shapes.stream().reduce(Shapes.empty(), Shapes::or);
        return shape;
    }
    public static <A extends B, B> ImmutableSet<B> castSet(ImmutableSet<A> set) {
    	ImmutableSet.Builder<B> builder = ImmutableSet.builder();
    	for (A element : set)
    	    builder.add((B) element);
    	return builder.build();
    }

    /**
     * Constructs a MultiIndexedVoxelShape, using the provided VoxelShape as this shape,
     * whilst still RayTracing against all the sub-components.
     *
     * @param merged The base shape.
     * @param shapes The sub-components.
     */
    public MultiIndexedVoxelShape(VoxelShape merged, ImmutableSet<IndexedVoxelShape> shapes) {
        super(merged.shape);
        this.merged = merged;
        this.shapes = shapes;
    }

    @Nullable
    @Override
    public VoxelShapeBlockHitResult clip(Vec3 start, Vec3 end, BlockPos pos) {
    	VoxelShapeBlockHitResult closestHit = null;
        double dist = Double.MAX_VALUE;
        for (IndexedVoxelShape shape : shapes) {
            VoxelShapeBlockHitResult hit = shape.clip(start, end, pos);
            if (hit != null && dist >= hit.dist) {
            	closestHit = hit;
                dist = hit.dist;
            }
        }
        if (closestHit==null) {
        	this.shape = merged.shape;
        	hitShapeData = null;
        } else {
        	this.shape = closestHit.shape.shape;
        	if (closestHit.shape.getData() instanceof Integer i)
            	if (i==-1) this.shape = merged.shape;
        	hitShapeData = closestHit.shape.getData();
        }

        return closestHit;
    }

	@Override
	public DoubleList getCoords(Axis axis) {
        return merged.getCoords(axis);
	}

}

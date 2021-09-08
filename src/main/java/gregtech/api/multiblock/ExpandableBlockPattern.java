package gregtech.api.multiblock;

import gnu.trove.map.TIntObjectMap;
import gregtech.api.util.IntRange;
import gregtech.api.util.RelativeDirection;
import gregtech.common.blocks.BlockCleanroomCasing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;

import static gregtech.common.blocks.MetaBlocks.CLEANROOM_CASING;

public class ExpandableBlockPattern extends BlockPattern {

    private static final int xRadius = 7;
    private static final int zRadius = 7;
    private static final int yHeight = 14;

    public ExpandableBlockPattern(Predicate<BlockWorldState>[][][] predicatesIn,
                                  List<Pair<Predicate<BlockWorldState>, IntRange>> countMatches,
                                  TIntObjectMap<Predicate<PatternMatchContext>> layerMatchers,
                                  List<Predicate<PatternMatchContext>> validators,
                                  RelativeDirection[] structureDir,
                                  int[][] aisleRepetitions) {
        super(predicatesIn, countMatches, layerMatchers, validators, structureDir, aisleRepetitions);
    }

    @Override
    public PatternMatchContext checkPatternAt(World world, BlockPos centerPos, EnumFacing facing) {
        // get the corners
        Tuple<BlockPos.MutableBlockPos, BlockPos.MutableBlockPos> xBounds = testSpecificDirection(world, new BlockPos.MutableBlockPos(centerPos), xRadius, xRadius, RelativeDirection.LEFT.apply(facing));
        if (xBounds == null)
            return null;
        Tuple<BlockPos.MutableBlockPos, BlockPos.MutableBlockPos> zBounds = testSpecificDirection(world, new BlockPos.MutableBlockPos(centerPos), zRadius, zRadius, RelativeDirection.FRONT.apply(facing));
        if (zBounds == null)
            return null;
        Tuple<BlockPos.MutableBlockPos, BlockPos.MutableBlockPos> yBounds = testSpecificDirection(world, new BlockPos.MutableBlockPos(centerPos), 0, yHeight, RelativeDirection.UP.apply(facing));
        if (yBounds == null)
            return null;

        BlockPos topCorner = new BlockPos(xBounds.getFirst().getX(), yBounds.getFirst().getY(), zBounds.getFirst().getZ());
        BlockPos bottomCorner = new BlockPos(xBounds.getSecond().getX(), yBounds.getSecond().getY(), zBounds.getSecond().getZ());

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(topCorner.getX(), topCorner.getY(), bottomCorner.getZ());

        // test top layer
        for (int i = 0; i < topCorner.getY() - bottomCorner.getY() + 1; i++) {
            mutableBlockPos.add(0, 1, 0);
            for (int j = 1; j < topCorner.getX() - bottomCorner.getX(); j++) {
                if (testEdge(world, new BlockPos.MutableBlockPos(mutableBlockPos), j, RelativeDirection.FRONT.apply(facing)) == null)
                    return null;
            }
        }



        return super.checkPatternAt(world, centerPos, facing);
    }

    public Tuple<BlockPos.MutableBlockPos, BlockPos.MutableBlockPos> testSpecificDirection(World world, BlockPos.MutableBlockPos centerPos, int radius1, int radius2, EnumFacing facing) {

        BlockPos.MutableBlockPos coord1;
        coord1 = testDirection(world, centerPos, radius1, facing);
        if (coord1 == null)
            return null;

        BlockPos.MutableBlockPos coord2;
        coord2 = testDirection(world, centerPos, radius2, facing.getOpposite());
        if (coord2 == null)
            return null;

        return new Tuple<>(coord1, coord2);
    }

    public BlockPos.MutableBlockPos testDirection(World world, BlockPos.MutableBlockPos centerPos, int distance, EnumFacing facing) {
        if (facing == EnumFacing.WEST || facing == EnumFacing.NORTH || facing == EnumFacing.UP) {
            for (int i = 0; i < distance; i++) {
                return testEdge(world, centerPos, i, facing);
            }
        } else {
            for (int i = distance; i > 0; i--) {
                return testEdge(world, centerPos, -i, facing);
            }
        }
        return null;
    }

    public BlockPos.MutableBlockPos testEdge(World world, BlockPos.MutableBlockPos centerPos, int i, EnumFacing facing) {
        if (facing == EnumFacing.WEST || facing.getOpposite() == EnumFacing.WEST)
            centerPos.add(i, 0, 0);
        if (facing == EnumFacing.DOWN)
            centerPos.add(0, i, 0);
        if (facing == EnumFacing.NORTH || facing.getOpposite() == EnumFacing.NORTH)
            centerPos.add(0, 0, i);

        if (isPlascrete(world.getBlockState(centerPos)))
            return centerPos;

        return null;
    }

    public boolean isPlascrete(IBlockState blockState) {
        return blockState.equals(CLEANROOM_CASING.getState(BlockCleanroomCasing.casingType.PLASCRETE));
    }
}

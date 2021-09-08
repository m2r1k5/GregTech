package gregtech.api.multiblock;

import gnu.trove.map.TIntObjectMap;
import gregtech.api.util.IntRange;
import gregtech.api.util.RelativeDirection;
import gregtech.common.blocks.BlockCleanroomCasing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;

import static gregtech.common.blocks.MetaBlocks.CLEANROOM_CASING;

public class CleanroomBlockPattern extends BlockPattern {

    private static final int xRadius = 7;
    private static final int zRadius = 7;
    private static final int yHeight = 14;

    public CleanroomBlockPattern(Predicate<BlockWorldState>[][][] predicatesIn,
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
        BlockPos[] xBounds = testHorizontalDirection(world, centerPos, xRadius, EnumFacing.EAST, false);
        if (xBounds == null)
            return null;
        BlockPos[] zBounds = testHorizontalDirection(world, centerPos, zRadius, EnumFacing.NORTH, false);
        if (zBounds == null)
            return null;
        BlockPos[] yBounds = testHorizontalDirection(world, centerPos, yHeight, EnumFacing.DOWN, true);
        if (yBounds == null)
            return null;

        BlockPos topCorner = new BlockPos(xBounds[0].getX(), yBounds[0].getY(), zBounds[0].getZ());
        BlockPos bottomCorner = new BlockPos(xBounds[1].getX(), yBounds[1].getY(), zBounds[1].getZ());

        // test top layer
        for (int i = 0; i < topCorner.getY() - bottomCorner.getY() + 1; i++) {
            for (int j = 1; j < topCorner.getX() - bottomCorner.getX(); j++) {
                if (testEdge(world, new BlockPos(topCorner.getX(), topCorner.getY() + i, bottomCorner.getZ()), j, EnumFacing.NORTH) == null)
                    return null;
            }
        }



        return super.checkPatternAt(world, centerPos, facing);
    }

    public BlockPos[] testHorizontalDirection(World world, BlockPos centerPos, int radius, EnumFacing facing, boolean isY) {
        BlockPos[] coords = new BlockPos[2];

        if (isY) {
            coords[0] = centerPos;
        } else {
            coords[0] = testDirection(world, centerPos, radius, facing);
            if (coords[0] == null)
                return null;
        }

        coords[1] = testEdge(world, centerPos, radius, facing.getOpposite());
        if (coords[1] == null)
            return null;

        return coords;
    }

    public BlockPos testDirection(World world, BlockPos centerPos, int distance, EnumFacing facing) {
        if (facing == EnumFacing.WEST || facing == EnumFacing.NORTH) {
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

    public BlockPos testEdge(World world, BlockPos centerPos, int i, EnumFacing facing) {
        int xPos = centerPos.getX();
        if (facing == EnumFacing.WEST || facing.getOpposite() == EnumFacing.WEST)
            xPos += i;
        int yPos = centerPos.getY();
        if (facing == EnumFacing.DOWN)
            yPos += i;
        int zPos = centerPos.getZ();
        if (facing == EnumFacing.NORTH || facing.getOpposite() == EnumFacing.NORTH)
            zPos += i;

        BlockPos blockPos = new BlockPos(new BlockPos(xPos, yPos, zPos));

        if (isPlascrete(world.getBlockState(blockPos)))
            return blockPos;

        return null;
    }

    public boolean isPlascrete(IBlockState blockState) {
        return blockState.equals(CLEANROOM_CASING.getState(BlockCleanroomCasing.casingType.PLASCRETE));
    }
}

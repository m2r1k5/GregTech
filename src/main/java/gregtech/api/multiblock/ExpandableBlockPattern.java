package gregtech.api.multiblock;

import gnu.trove.map.TIntObjectMap;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.util.IntRange;
import gregtech.api.util.RelativeDirection;
import gregtech.common.blocks.BlockCleanroomCasing;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static gregtech.common.blocks.MetaBlocks.CLEANROOM_CASING;

//todo scan interior and apply changes to it with boolean to toggle
public class ExpandableBlockPattern extends BlockPattern {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = { MultiblockAbility.INPUT_ENERGY, MultiblockAbility.MAINTENANCE_HATCH };

    private int currentFrameDistanceLeft;
    private int currentFrameDistanceRight;
    private int currentFrameDistanceUp;
    private int currentFrameDistanceDown;
    private int currentFrameDistanceBack;
    private int currentFrameDistanceFront;

    private final int maxFrameDistanceLeft;
    private final int maxFrameDistanceRight;
    private final int maxFrameDistanceUp;
    private final int maxFrameDistanceDown;
    private final int maxFrameDistanceBack;
    private final int maxFrameDistanceFront;

    public ExpandableBlockPattern(Predicate<BlockWorldState>[][][] predicatesIn,
                                  List<Pair<Predicate<BlockWorldState>, IntRange>> countMatches,
                                  TIntObjectMap<Predicate<PatternMatchContext>> layerMatchers,
                                  List<Predicate<PatternMatchContext>> validators,
                                  RelativeDirection[] structureDir,
                                  int[][] aisleRepetitions) {
        super(predicatesIn, countMatches, layerMatchers, validators, structureDir, aisleRepetitions);

        this.maxFrameDistanceLeft = 7;
        this.maxFrameDistanceRight = 7;
        this.maxFrameDistanceUp = 0;
        this.maxFrameDistanceDown = 14;
        this.maxFrameDistanceBack = 7;
        this.maxFrameDistanceFront = 7;
    }

    @Override
    public PatternMatchContext checkPatternAt(World world, BlockPos centerPos, EnumFacing facing) {
        Tuple<BlockPos.MutableBlockPos, BlockPos.MutableBlockPos> corners = getCorners(world, centerPos, facing);
        if (corners == null)
            return null;

        int xDistance = currentFrameDistanceLeft + currentFrameDistanceRight;
        int yDistance = currentFrameDistanceDown + currentFrameDistanceUp;
        int zDistance = currentFrameDistanceBack + currentFrameDistanceFront;

        if (!testFrame(world, corners.getFirst(), corners.getSecond(), facing, xDistance, yDistance, zDistance))
            return null;

        if (!testWalls(world, corners.getFirst(), corners.getSecond(), facing, xDistance, yDistance, zDistance))
            return null;

        return super.checkPatternAt(world, centerPos, facing);
    }

    /**
     * finds the two corners of the structure frame
     * @param world the world to check the structure in
     * @param centerPos the position of the controller
     * @param facing the direction the controller is facing
     * @return a Tuple of 2 MutableBlockPoses for the back upper left and front lower right corners if found, else null
     */
    @Nullable
    public Tuple<BlockPos.MutableBlockPos, BlockPos.MutableBlockPos> getCorners(World world, BlockPos centerPos, EnumFacing facing) {

        // find the first corner (back upper left)
        BlockPos.MutableBlockPos firstCorner = new BlockPos.MutableBlockPos(centerPos);

        // move left
        int amountLeft = findFrame(world, firstCorner, RelativeDirection.LEFT.apply(facing), this.maxFrameDistanceLeft);
        if (amountLeft == -1)
            return null;

        // move back
        int amountBack = findFrame(world, firstCorner, RelativeDirection.BACK.apply(facing), this.maxFrameDistanceBack);
        if (amountBack == -1)
            return null;

        // move up
        int amountUp = findFrame(world, firstCorner, RelativeDirection.UP.apply(facing), this.maxFrameDistanceUp);
        if (amountUp == -1)
            return null;

        // find the second corner (front lower right)
        BlockPos.MutableBlockPos secondCorner = new BlockPos.MutableBlockPos(centerPos);

        // move right
        int amountRight = findFrame(world, secondCorner, RelativeDirection.RIGHT.apply(facing), this.maxFrameDistanceRight);
        if (amountRight == -1)
            return null;

        // move forwards
        int amountFront = findFrame(world, secondCorner, RelativeDirection.FRONT.apply(facing), this.maxFrameDistanceFront);
        if (amountFront == -1)
            return null;

        // move down
        int amountDown = findFrame(world, secondCorner, RelativeDirection.DOWN.apply(facing), this.maxFrameDistanceDown);
        if (amountDown == -1)
            return null;

        this.currentFrameDistanceLeft = amountLeft;
        this.currentFrameDistanceRight = amountRight;
        this.currentFrameDistanceUp = amountUp;
        this.currentFrameDistanceDown = amountDown;
        this.currentFrameDistanceBack = amountBack;
        this.currentFrameDistanceFront = amountFront;

        return new Tuple<>(firstCorner, secondCorner);
    }

    /**
     * finds the edge of the structure frame in a certain direction from a given location
     * @param world the world to check the structure in
     * @param mutableBlockPos the starting position for checking
     * @param facing the direction to find the frame in
     * @param distance the maximum distance the frame is away from the starting position
     * @return the distance to the frame's edge if found, else null
     */
    private int findFrame(World world, BlockPos.MutableBlockPos mutableBlockPos, EnumFacing facing, int distance) {
        // distance can be zero if the controller is on an edge, allows checking to continue
        if (distance == 0)
            return 0;

        // move to the max distance
        mutableBlockPos.move(facing, distance);

        // crawl from the end inwards, finding the frame end in the direction specified
        for (int i = distance; i > 0; i--) {

            // once a frame is found, return the current position
            if (framePredicate().test(world.getBlockState(mutableBlockPos)))
                return i;

            // no frame was found this time, so move one inwards
            mutableBlockPos.move(facing.getOpposite());
        }

        // there was no valid frame, so return -1
        return -1;
    }

    /**
     * tests the structure frame
     * @param world the world to check the structure in
     * @param firstCorner one of the two corners to check
     * @param secondCorner one of the two corners to check
     * @param facing the direction the controller is facing
     * @param xDistance the amount of blocks to check in the x direction after the first block
     * @param yDistance the amount of blocks to check in the y direction after the first block
     * @param zDistance the amount of blocks to check in the z direction after the first block
     * @return true if the frame is valid, else false
     */
    private boolean testFrame(World world, BlockPos.MutableBlockPos firstCorner, BlockPos.MutableBlockPos secondCorner, EnumFacing facing, int xDistance, int yDistance, int zDistance) {

        // check back upper left corner's two adjacent sides
        if (!testAdjacentSides(world, firstCorner, RelativeDirection.RIGHT, RelativeDirection.FRONT, facing, framePredicate(), xDistance, zDistance))
            return false;

        // move downwards for the next checks
        firstCorner.move(RelativeDirection.DOWN.apply(facing), yDistance);

        // check front lower right corner's two adjacent sides
        if (!testAdjacentSides(world, firstCorner, RelativeDirection.BACK, RelativeDirection.LEFT, facing, framePredicate(), xDistance, zDistance))
            return false;

        // move upwards for the next checks
        secondCorner.move(RelativeDirection.UP.apply(facing), yDistance);

        // check back upper left corner's two adjacent sides in the new location
        if (!testAdjacentSides(world, firstCorner, RelativeDirection.RIGHT, RelativeDirection.FRONT, facing, framePredicate(), xDistance, zDistance))
            return false;

        // check front lower right corner's two adjacent sides in the new location
        if (!testAdjacentSides(world, secondCorner, RelativeDirection.BACK, RelativeDirection.LEFT, facing, framePredicate(), xDistance, zDistance))
            return false;

        // test the four corners and return them to their original positions
        if (!testLine(world, firstCorner, RelativeDirection.DOWN.apply(facing), framePredicate(), yDistance))
            return false;

        firstCorner.move(RelativeDirection.RIGHT.apply(facing), xDistance);

        if (!testLine(world, firstCorner, RelativeDirection.DOWN.apply(facing), framePredicate(), yDistance))
            return false;

        firstCorner.move(RelativeDirection.RIGHT.apply(facing.getOpposite()), xDistance);

        if (!testLine(world, secondCorner, RelativeDirection.DOWN.apply(facing), framePredicate(), yDistance))
            return false;

        secondCorner.move(RelativeDirection.LEFT.apply(facing), xDistance);

        if (!testLine(world, secondCorner, RelativeDirection.DOWN.apply(facing), framePredicate(), yDistance))
            return false;

        secondCorner.move(RelativeDirection.LEFT.apply(facing.getOpposite()), xDistance);

        return true;
    }

    /**
     * tests the straight lines in two adjancent directions to a position
     * @param world the world to check the structure in
     * @param blockPos the starting MutableBlockPos to check
     * @param relativeDirection1 the first direction to check
     * @param relativeDirection2 the second direction to check
     * @param facing the direction the controller is facing
     * @param predicate the predicate to test the sides with
     * @param distance1 the distance to check in the first direction
     * @param distance2 the distance to check in the second direction
     * @return true if the structure passes the checks, else false
     */
    private boolean testAdjacentSides(World world, BlockPos.MutableBlockPos blockPos, RelativeDirection relativeDirection1, RelativeDirection relativeDirection2, EnumFacing facing, Predicate<IBlockState> predicate, int distance1, int distance2) {

        if (!testLine(world, blockPos, relativeDirection1.apply(facing), framePredicate(), distance1))
            return false;

        // move the position back
        blockPos.move(relativeDirection1.apply(facing.getOpposite()), distance1);

        if (!testLine(world, blockPos, relativeDirection2.apply(facing), framePredicate(), distance2))
            return false;

        // move the position back
        blockPos.move(relativeDirection2.apply(facing.getOpposite()), distance2);
        return true;
    }

    /**
     * Tests the blocks in a straight line in any direction
     * @param world the world to check the structure in
     * @param blockPos the starting MutableBlockPos to check
     * @param facing the direction to check blocks in
     * @param predicate the Predicate to test each block with
     * @param distance the amount of blocks to check in the direction after the first block
     * @return true if all blocks meet the predicate, else false
     */
    private boolean testLine(World world, BlockPos.MutableBlockPos blockPos, EnumFacing facing, Predicate<IBlockState> predicate, int distance) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(blockPos);
        // check every block in the row
        for (int i = 0; i <= distance; i++) {

            // check the current block, if the predicate is not met, return false
            if (!predicate.test(world.getBlockState(mutableBlockPos)))
                return false;

            // predicates passed, so move to the next block in the row
            mutableBlockPos.move(facing);
        }

        // all blocks passed, so return true
        return true;
    }

    /**
     * tests the six walls of the structure
     * @param world the world to check the structure in
     * @param firstCorner one of the two corners to check
     * @param secondCorner one of the two corners to check
     * @param facing the direction the controller is facing
     * @param xDistance the amount of blocks to check in the x direction after the first block
     * @param yDistance the amount of blocks to check in the y direction after the first block
     * @param zDistance the amount of blocks to check in the z direction after the first block
     * @return true if all six walls are valid, else false
     */
    private boolean testWalls(World world, BlockPos.MutableBlockPos firstCorner, BlockPos.MutableBlockPos secondCorner, EnumFacing facing, int xDistance, int yDistance, int zDistance) {
        // align the mutable positions to no longer include the frame
        firstCorner.move(RelativeDirection.FRONT.apply(facing));
        firstCorner.move(RelativeDirection.RIGHT.apply(facing));

        secondCorner.move(RelativeDirection.BACK.apply(facing));
        secondCorner.move(RelativeDirection.LEFT.apply(facing));

        // test the top and bottom walls
        for (int i = 0; i <= xDistance; i++) {
            if (!testLine(world, firstCorner, RelativeDirection.FRONT.apply(facing), wallPredicate(), zDistance - 2))
                return false;

            if (!testLine(world, secondCorner, RelativeDirection.BACK.apply(facing), wallPredicate(), zDistance - 2))
                return false;

            // reset corners
            firstCorner.move(RelativeDirection.FRONT.apply(facing.getOpposite()), zDistance - 2);
            secondCorner.move(RelativeDirection.BACK.apply(facing.getOpposite()), zDistance - 2);

            // shift row
            firstCorner.move(RelativeDirection.RIGHT.apply(facing));
            secondCorner.move(RelativeDirection.LEFT.apply(facing));
        }

        // reset corners
        firstCorner.move(RelativeDirection.RIGHT.apply(facing.getOpposite()), xDistance);
        secondCorner.move(RelativeDirection.LEFT.apply(facing.getOpposite()), xDistance);

        // test the left and right walls
        for (int i = 0; i <= yDistance; i++) {
            if (!testLine(world, firstCorner, RelativeDirection.FRONT.apply(facing), wallPredicate(), zDistance - 2))
                return false;

            if (!testLine(world, secondCorner, RelativeDirection.BACK.apply(facing), wallPredicate(), zDistance - 2))
                return false;

            // reset corners
            firstCorner.move(RelativeDirection.FRONT.apply(facing.getOpposite()), zDistance - 2);
            secondCorner.move(RelativeDirection.BACK.apply(facing.getOpposite()), zDistance - 2);

            // shift row
            firstCorner.move(RelativeDirection.DOWN.apply(facing));
            secondCorner.move(RelativeDirection.UP.apply(facing));
        }

        // reset corners
        firstCorner.move(RelativeDirection.DOWN.apply(facing.getOpposite()), yDistance);
        secondCorner.move(RelativeDirection.UP.apply(facing.getOpposite()), yDistance);

        // test the back and front walls
        for (int i = 0; i <= yDistance; i++) {
            if (!testLine(world, firstCorner, RelativeDirection.RIGHT.apply(facing), wallPredicate(), xDistance - 2))
                return false;

            if (!testLine(world, secondCorner, RelativeDirection.LEFT.apply(facing), wallPredicate(), xDistance - 2))
                return false;

            // reset corners
            firstCorner.move(RelativeDirection.RIGHT.apply(facing.getOpposite()), xDistance - 2);
            secondCorner.move(RelativeDirection.LEFT.apply(facing.getOpposite()), xDistance - 2);

            // shift row
            firstCorner.move(RelativeDirection.DOWN.apply(facing));
            secondCorner.move(RelativeDirection.UP.apply(facing));
        }

        // reset corners
        firstCorner.move(RelativeDirection.DOWN.apply(facing.getOpposite()), yDistance);
        secondCorner.move(RelativeDirection.UP.apply(facing.getOpposite()), yDistance);

        // include the frame again
        firstCorner.move(RelativeDirection.FRONT.apply(facing.getOpposite()));
        firstCorner.move(RelativeDirection.RIGHT.apply(facing.getOpposite()));

        secondCorner.move(RelativeDirection.BACK.apply(facing.getOpposite()));
        secondCorner.move(RelativeDirection.LEFT.apply(facing.getOpposite()));

        return true;
    }

    // todo allow one controller
    public static Predicate<IBlockState> wallPredicate() {
        return blockState -> {
            if (blockState.getBlock().equals(CLEANROOM_CASING))
                return true;

            return isValidMultiblockPart(blockState.getBlock());
        };
    }

    public static Predicate<IBlockState> framePredicate() {
        return blockState -> {
            if (blockState.equals(CLEANROOM_CASING.getState(BlockCleanroomCasing.casingType.PLASCRETE)))
                return true;

            return isValidMultiblockPart(blockState.getBlock());
        };
    }

    private static boolean isValidMultiblockPart(Block block) {
        return block instanceof IMultiblockAbilityPart<?> && ArrayUtils.contains(ALLOWED_ABILITIES, ((IMultiblockAbilityPart<?>) block).getAbility());
    }
}

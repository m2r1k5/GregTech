package gregtech.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class BlockCleanroomCasing extends VariantBlock<BlockCleanroomCasing.casingType> {

    public BlockCleanroomCasing() {
        super(Material.IRON);
        setTranslationKey("cleanroom_casing");
        setHardness(25.0f);
        setResistance(10.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 2);
        setDefaultState(getState(casingType.PLASCRETE));
    }

    @Override
    public boolean canCreatureSpawn(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull SpawnPlacementType type) {
        return false;
    }

    public enum casingType implements IStringSerializable {

        PLASCRETE("plascrete", 0),
        FILTER_1("filter_1", 1),
        FILTER_2("filter_2", 2),
        FILTER_3("filter_3", 4),
        FILTER_4("filter_4", 8),
        FILTER_5("filter_5", 16),
        FILTER_6("filter_6", 32),
        FILTER_7("filter_7", 64),
        FILTER_8("filter_8", 128);

        private final String name;
        private final int level;

        casingType(String name, int level) {
            this.name = name;
            this.level = level;
        }

        @Nonnull
        @Override
        public String getName() {
            return this.name;
        }

        public int getLevel(){
            return this.level;
        }

    }

}

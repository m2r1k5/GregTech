package gregtech.common.metatileentities.multi.electric;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class MetaTileEntityCleanroom extends RecipeMapMultiblockController {

    private static MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.INPUT_ENERGY
    };

    public MetaTileEntityCleanroom(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.CLEANROOM_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityCleanroom(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXXXXX", "XXX XXX", "XXX XXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX")
                .aisle("XXXXXXX", "X     X", "X     X", "X     X", "X     X", "X     X", "XXXXXXX").setRepeatable(2, 2)
                .aisle("XXXXXXX", "       ", "       ", "X     X", "X     X", "X     X", "XXXSXXX")
                .aisle("XXXXXXX", "X     X", "X     X", "X     X", "X     X", "X     X", "XXXXXXX").setRepeatable(2, 2)
                .aisle("XXXXXXX", "XXX XXX", "XXX XXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX")
                .where('X', maintenancePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('S', selfPredicate())
                .where(' ', (tile) -> true)
                .build();
    }

    private IBlockState getCasingState() {
        return MetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.PLASCRETE);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.PLASCRETE;
    }

    @Nonnull
    @Override
    protected OrientedOverlayRenderer getFrontOverlay() {
        return Textures.CLEANROOM_OVERLAY;
    }
}

package gregtech.common.metatileentities.multi.electric;

import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MetaTileEntityCleanroom extends RecipeMapMultiblockController {

    private static MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.INPUT_ENERGY
    };

    private int cleanliness;

    public MetaTileEntityCleanroom(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.CLEANROOM_RECIPES);
        this.recipeMapWorkable = new CleanroomWorkableHandler(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityCleanroom(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXXXXX", "XXX XXX", "XXX XXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX")
                .aisle("XXXXXXX", "X     X", "X     X", "X     X", "X     X", "X     X", "XFFFFFX").setRepeatable(2, 2)
                .aisle("XXXXXXX", "       ", "       ", "X     X", "X     X", "X     X", "XFFSFFX")
                .aisle("XXXXXXX", "X     X", "X     X", "X     X", "X     X", "X     X", "XFFFFFX").setRepeatable(2, 2)
                .aisle("XXXXXXX", "XXX XXX", "XXX XXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX")
                .where('X', maintenancePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('F', statePredicate(getFilterState()))
                .where('S', selfPredicate())
                .where(' ', (tile) -> true)
                .build();
    }

    private IBlockState getCasingState() {
        return MetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.PLASCRETE);
    }

    private IBlockState getFilterState() {
        return MetaBlocks.MULTIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.FILTER_CASING);
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

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.cleanliness = 0;
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.cleanliness = 0;
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        textList.add(new TextComponentTranslation("gregtech.multiblock.cleanroom.cleanliness", cleanliness));
    }

    protected void incrementCleanliness(int amount) {
        this.cleanliness = Math.min(100, this.cleanliness + amount);
    }

    protected static class CleanroomWorkableHandler extends MultiblockRecipeLogic {

        private static final Recipe cleanroomRecipe = new Recipe(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 20, 4, true);

        public CleanroomWorkableHandler(RecipeMapMultiblockController metaTileEntity) {
            super(metaTileEntity);
        }

        @Override
        protected void trySearchNewRecipe() {
            // do not run recipes when there are more than 5 maintenance problems
            MultiblockWithDisplayBase controller = (MultiblockWithDisplayBase) metaTileEntity;
            if (controller.hasMaintenanceMechanics() && controller.getNumMaintenanceProblems() > 5)
                return;

            setupRecipe(cleanroomRecipe);
        }

        @Override
        protected void setupRecipe(Recipe recipe) {
            this.progressTime = 1;
            setMaxProgress(recipe.getDuration());
            this.recipeEUt = recipe.getEUt();
            if (this.wasActiveAndNeedsUpdate) {
                this.wasActiveAndNeedsUpdate = false;
            } else {
                this.setActive(true);
            }
        }

        @Override
        protected void completeRecipe() {
            // increase total on time
            MultiblockWithDisplayBase controller = (MultiblockWithDisplayBase) metaTileEntity;
            if (controller.hasMaintenanceMechanics())
                controller.calculateMaintenance(this.progressTime);

            // increase cleanliness
            ((MetaTileEntityCleanroom) metaTileEntity).incrementCleanliness(GTUtility.getTierByVoltage(getMaxVoltage()));

            this.progressTime = 0;
            setMaxProgress(0);
            this.recipeEUt = 0;
            this.hasNotEnoughEnergy = false;
            this.wasActiveAndNeedsUpdate = true;
        }
    }
}

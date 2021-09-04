package gregtech.api.capability.impl;

import gregtech.api.GTValues;
import gregtech.api.capability.ICleanroomReceiver;
import gregtech.api.capability.ICleanroomTransmitter;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.MatchingMode;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.recipeproperties.CleanroomProperty;
import gregtech.api.recipes.recipeproperties.CleanroomProperty.CleanroomLevel;
import gregtech.common.ConfigHolder;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.Supplier;

public class RecipeLogicEnergy extends AbstractRecipeLogic {

    private final Supplier<IEnergyContainer> energyContainer;

    public RecipeLogicEnergy(MetaTileEntity tileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer) {
        super(tileEntity, recipeMap);
        this.energyContainer = energyContainer;
    }

    @Override
    protected long getEnergyStored() {
        return energyContainer.get().getEnergyStored();
    }

    @Override
    protected long getEnergyCapacity() {
        return energyContainer.get().getEnergyCapacity();
    }

    @Override
    protected boolean drawEnergy(int recipeEUt) {
        long resultEnergy = getEnergyStored() - recipeEUt;
        if (resultEnergy >= 0L && resultEnergy <= getEnergyCapacity()) {
            energyContainer.get().changeEnergy(-recipeEUt);
            return true;
        } else return false;
    }

    @Override
    protected long getMaxVoltage() {
        return Math.max(energyContainer.get().getInputVoltage(),
                energyContainer.get().getOutputVoltage());
    }

    @Override
    protected void trySearchNewRecipe() {
        long maxVoltage = getMaxVoltage();
        Recipe currentRecipe;
        IItemHandlerModifiable importInventory = getInputInventory();
        IMultipleTankHandler importFluids = getInputTank();

        // see if the last recipe we used still works
        if (this.previousRecipe != null && this.previousRecipe.matches(false, importInventory, importFluids))
            currentRecipe = this.previousRecipe;
            // If there is no active recipe, then we need to find one.
        else
            currentRecipe = findRecipe(maxVoltage, importInventory, importFluids, MatchingMode.DEFAULT);

        if (currentRecipe != null && metaTileEntity instanceof ICleanroomReceiver) {
            ICleanroomReceiver cleanroomReceiver = (ICleanroomReceiver) metaTileEntity;
            if (currentRecipe.hasProperty(CleanroomProperty.getInstance())) {
                if (cleanroomReceiver.hasCleanroom()) {
                    CleanroomLevel cleanroomRequiredLevel = currentRecipe.getProperty(CleanroomProperty.getInstance(), null);
                    CleanroomLevel cleanroomLevel = cleanroomReceiver.getCleanroom().getCleanRoomLevel();
                    if (cleanroomRequiredLevel.greaterThan(cleanroomLevel))
                        currentRecipe = null;
                } else {
                    currentRecipe = null;
                }
            }
        }

        // If a recipe was found, then inputs were valid. Cache found recipe.
        if (currentRecipe != null)
            this.previousRecipe = currentRecipe;

        // this could cause problems if inputs are provided before the cleanroom gets around to setting state
        // on the machine, then the inputs would need to be modified again before it will run. Need to be
        // careful with handling this to avoid a regression in performance improvements.
        if (currentRecipe == null)
            this.invalidInputsForRecipes = true;

        // proceed if we have a usable recipe.
        if (currentRecipe != null && setupAndConsumeRecipeInputs(currentRecipe))
            setupRecipe(currentRecipe);
        // Inputs have been inspected.
        metaTileEntity.getNotifiedItemInputList().clear();
        metaTileEntity.getNotifiedFluidInputList().clear();
    }

    @Override
    protected int[] calculateOverclock(int EUt, long voltage, int duration) {
        int perfectOverclocks = 0;
        if (metaTileEntity instanceof ICleanroomReceiver) {
            ICleanroomReceiver cleanroomReceiver = (ICleanroomReceiver) metaTileEntity;
            if (cleanroomReceiver.hasCleanroom() && previousRecipe.hasProperty(CleanroomProperty.getInstance())) {
                ICleanroomTransmitter cleanroomTransmitter = cleanroomReceiver.getCleanroom();
                CleanroomLevel cleanroomLevel = cleanroomTransmitter.getCleanRoomLevel();
                CleanroomLevel cleanroomRecipeLevel = this.previousRecipe.getProperty(CleanroomProperty.getInstance(), null);

                perfectOverclocks = Math.max(cleanroomLevel.getDifference(cleanroomRecipeLevel), 0);
            }
        }

        if (!allowOverclocking) {
            return new int[]{EUt, duration};
        }
        boolean negativeEU = EUt < 0;
        int tier = getOverclockingTier(voltage);

        // Cannot overclock
        if (GTValues.V[tier] <= EUt || tier == 0)
            return new int[]{EUt, duration};

        if (negativeEU)
            EUt = -EUt;

        int resultEUt = EUt;
        double resultDuration = duration;
        int maxOverclocks = tier - 1; // exclude ULV overclocking

        //do not overclock further if duration is already too small
        while (resultDuration >= 3 && resultEUt <= GTValues.V[tier - 1] && maxOverclocks != 0 && perfectOverclocks > 0) {
            resultEUt *= 4;
            resultDuration /= 4.0;
            maxOverclocks--;
        }

        //do not overclock further if duration is already too small
        while (resultDuration >= 3 && resultEUt <= GTValues.V[tier - 1] && maxOverclocks != 0) {
            resultEUt *= 4;
            resultDuration /= ConfigHolder.U.overclockDivisor;
            maxOverclocks--;
        }

        return new int[]{negativeEU ? -resultEUt : resultEUt, (int) Math.ceil(resultDuration)};
    }
}

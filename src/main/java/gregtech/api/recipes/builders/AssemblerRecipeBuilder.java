package gregtech.api.recipes.builders;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.ValidationResult;

public class AssemblerRecipeBuilder extends IntCircuitRecipeBuilder {

    @Override
    public ValidationResult<Recipe> build() {
        RecipeBuilder<?> builder = this.copy();
        RecipeBuilder<?> builder2 = this.copy();
        RecipeBuilder<?> builder3 = this.copy();
        if (fluidInputs.size() == 1 && fluidInputs.get(0).getFluid() == Materials.SolderingAlloy.getFluid()) {
            int amount = fluidInputs.get(0).amount;
            fluidInputs.clear();
            builder.fluidInputs(Materials.SolderingAlloy.getFluid(amount));

            if (this.cleanroomLevel != null)
                builder.cleanroomLevel(this.cleanroomLevel);
            builder.buildAndRegister();

            builder2 = this.copy().fluidInputs(Materials.Tin.getFluid((int) (amount * 1.5)));

            if (this.cleanroomLevel != null)
                builder2.cleanroomLevel(this.cleanroomLevel);
            builder2.buildAndRegister();

            builder3 = this.copy().fluidInputs(Materials.Lead.getFluid(amount * 2));

            if (this.cleanroomLevel != null)
                builder3.cleanroomLevel(this.cleanroomLevel);
            builder3.buildAndRegister();
        }

        return ValidationResult.newResult(finalizeAndValidate(),
                new Recipe(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs, duration, EUt, hidden));
    }

}

package gregtech.api.recipes.recipeproperties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class CleanroomProperty extends RecipeProperty<Integer> {

    public static final String KEY = "cleanroom";

    private static CleanroomProperty INSTANCE;

    private CleanroomProperty() {
        super(KEY, Integer.class);
    }

    public static CleanroomProperty getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CleanroomProperty();
        return INSTANCE;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
        minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe.cleanroom",
                castValue(value)), x, y, color);
    }
}

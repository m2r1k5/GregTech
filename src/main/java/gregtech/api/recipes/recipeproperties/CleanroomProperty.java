package gregtech.api.recipes.recipeproperties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CleanroomProperty extends RecipeProperty<CleanroomProperty.CleanroomLevel> {

    public static final String KEY = "cleanroom";

    private static CleanroomProperty INSTANCE;

    private CleanroomProperty() {
        super(KEY, CleanroomLevel.class);
    }

    public static CleanroomProperty getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CleanroomProperty();
        return INSTANCE;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
        minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe.cleanroom",
                castValue(value).translate()), x, y, color);
    }

    public enum CleanroomLevel {

        // ISO9 not represented since it means "no cleanroom" in our context
        /**
         * The lowest Cleanroom Level
         */
        ISO8(1),
        ISO7(2),
        ISO6(3),
        ISO5(4),
        ISO4(5),
        ISO3(6),
        ISO2(7),
        /**
         * The highest Cleanroom Level
         */
        ISO1(8);

        private final int levelInternal;
        private final String key;

        CleanroomLevel(int levelInternal) {
            this.levelInternal = levelInternal;
            this.key = "" + Math.abs(levelInternal - 9);
        }

        public boolean greaterThan(CleanroomLevel other) {
            return levelInternal > other.levelInternal;
        }

        public int getDifference(CleanroomLevel other) {
            return levelInternal - other.levelInternal;
        }

        @SideOnly(Side.CLIENT)
        public String translate() {
            return I18n.format("gregtech.multiblock.cleanroom.iso", key);
        }
    }
}

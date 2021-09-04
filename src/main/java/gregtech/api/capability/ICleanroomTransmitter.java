package gregtech.api.capability;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.recipeproperties.CleanroomProperty.CleanroomLevel;

public interface ICleanroomTransmitter {

    MetaTileEntity getCleanroomTileEntity();

    CleanroomLevel getCleanRoomLevel();
}

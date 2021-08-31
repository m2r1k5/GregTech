package gregtech.api.capability;

import gregtech.api.metatileentity.MetaTileEntity;

public interface ICleanroomReceiver {

    void setCleanroom(ICleanroomTransmitter cleanroomTransmitter);

    boolean hasCleanroom();

    MetaTileEntity getCleanroom();
}

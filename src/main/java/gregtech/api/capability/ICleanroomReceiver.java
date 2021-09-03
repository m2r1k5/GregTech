package gregtech.api.capability;

public interface ICleanroomReceiver {

    void setCleanroom(ICleanroomTransmitter cleanroomTransmitter);

    boolean hasCleanroom();

    ICleanroomTransmitter getCleanroom();
}

package gregtech.core.visitors;

import gregtech.core.util.ObfMapping;
import gregtech.core.util.SafeMethodVisitor;
import org.objectweb.asm.MethodVisitor;

public class PacketGetInfoVisitor extends SafeMethodVisitor {
    public static final String TARGET_CLASS_NAME = "mcjty/theoneprobe/network/PacketGetInfo";
    private static final String TARGET_METHOD_SIGNATURE = "(Lnet/minecraft/entity/player/EntityPlayer;Lmcjty/theoneprobe/api/ProbeMode;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/item/ItemStack;)Lmcjty/theoneprobe/apiimpl/ProbeInfo;";
    private static final String TARGET_METHOD_NAME = "getProbeInfo";
    public static final ObfMapping TARGET_METHOD = new ObfMapping(TARGET_CLASS_NAME, TARGET_METHOD_NAME, TARGET_METHOD_SIGNATURE);

    private static final String METHOD_OWNER = "net/minecraft/world/World";
    private static final String METHOD_SIGNATURE = "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;";
    private static final String METHOD_NAME = "func_180495_p";
    private static final ObfMapping METHOD_MAPPING = new ObfMapping(METHOD_OWNER, METHOD_NAME, METHOD_SIGNATURE).toRuntime();

    public PacketGetInfoVisitor(int api, MethodVisitor mv) {
        super(api, mv);
    }


    @Override
    protected String getInjectTargetString() {
        return String.format("Patch target: %s; injection point: %s; (point not found)", TARGET_METHOD, METHOD_MAPPING);
    }
}

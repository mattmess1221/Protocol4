package mnm.mods.protocol.asm;

import com.mumfrey.liteloader.core.runtime.Obf;

public class PacketObf extends Obf {

    public static final Obf readPacketData = new PacketObf("func_148837_a", "a", "readPacketData");
    public static final Obf writePacketData = new PacketObf("func_148840_b", "b", "writePacketData");
    public static final Obf PacketBuffer = new PacketObf("net.minecraft.network.PacketBuffer", "hd");
    public static final Obf Packet = new PacketObf("net.minecraft.network.Packet", "id");
    public static final Obf MessageSerializer = new PacketObf("net.minecraft.util.MessageSerializer", "hf");
    public static final Obf MessageDeserializer = new PacketObf("net.minecraft.util.MessageDeserializer", "he");
    public static final Obf encode = new PacketObf("encode", "a");
    public static final Obf decode = new PacketObf("decode", "a");

    protected PacketObf(String seargeName, String obfName, String mcpName) {
        super(seargeName, obfName, mcpName);
    }

    protected PacketObf(String seargeName, String obfName) {
        super(seargeName, obfName);
    }

    public PacketObf(String name) {
        super(name);
    }
}
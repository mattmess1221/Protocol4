package mnm.mods.protocol;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

public class PacketIO {

    public static void readPacketData(Packet packet, PacketBuffer buffer) {
        try {
            LiteModProtocol4.instance.preReadPacketData(packet, buffer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void writePacketData(Packet packet, PacketBuffer buffer) {
        try {
            LiteModProtocol4.instance.postWritePacketData(packet, buffer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

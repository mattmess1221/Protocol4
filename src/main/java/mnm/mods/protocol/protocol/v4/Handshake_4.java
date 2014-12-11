package mnm.mods.protocol.protocol.v4;

import mnm.mods.protocol.interfaces.PacketWrite;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;

import java.io.IOException;

public class Handshake_4 implements PacketWrite {

    @Override
    public void writePacketData(PacketBuffer buffer) throws IOException {
        buffer.setByte(1, 4);
    }

    @Override
    public Class<? extends Packet> getPacket() {
        return C00Handshake.class;
    }
}

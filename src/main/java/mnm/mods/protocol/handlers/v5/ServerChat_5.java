package mnm.mods.protocol.handlers.v5;

import java.io.IOException;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S02PacketChat;

public class ServerChat_5 implements PacketRead {

    @Override
    public Class<? extends Packet> getPacket() {
        return S02PacketChat.class;
    }

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        // mark it as chat.
        buffer.writeByte(2);

    }

}

package mnm.mods.protocol.handlers.v5;

import java.io.IOException;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S01PacketJoinGame;

public class JoinGame_5 implements PacketRead {

    @Override
    public Class<? extends Packet> getPacket() {
        return S01PacketJoinGame.class;
    }

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        // debug info not hidden
        buffer.writeBoolean(false);
    }
}

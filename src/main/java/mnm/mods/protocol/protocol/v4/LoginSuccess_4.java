package mnm.mods.protocol.protocol.v4;

import com.mojang.util.UUIDTypeAdapter;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.server.S02PacketLoginSuccess;

import java.io.IOException;

public class LoginSuccess_4 implements PacketRead {

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        int start = buffer.readerIndex();
        // String string = buffer.toString(Charsets.UTF_8);
        String uuid = buffer.readStringFromBuffer(36);
        String name = buffer.readStringFromBuffer(16);
        buffer.readerIndex(start);

        uuid = UUIDTypeAdapter.fromString(uuid).toString();

        buffer.writerIndex(start);
        buffer.writeStringToBuffer(uuid);
        buffer.writeStringToBuffer(name);

    }

    @Override
    public Class<? extends Packet> getPacket() {
        return S02PacketLoginSuccess.class;
    }

}

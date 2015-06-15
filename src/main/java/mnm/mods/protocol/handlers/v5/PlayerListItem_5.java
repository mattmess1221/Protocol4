package mnm.mods.protocol.handlers.v5;

import java.io.IOException;
import java.util.UUID;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S38PacketPlayerListItem.Action;

public class PlayerListItem_5 implements PacketRead {

    @Override
    public Class<? extends Packet> getPacket() {
        // TODO Auto-generated method stub
        return S38PacketPlayerListItem.class;
    }

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        // TODO Auto-generated method stub
        int start = buffer.readerIndex();
        buffer.readStringFromBuffer(16); // name
        buffer.readBoolean(); // add/remove
        short s = buffer.readShort();

        buffer.readerIndex(start);
        buffer.writerIndex(start);

        S38PacketPlayerListItem.Action action = Action.UPDATE_LATENCY;

        buffer.writeEnumValue(action);

        // TODO workaround for other players
        int i = 1;
        buffer.writeVarIntToBuffer(i);

        buffer.writeUuid(UUID.randomUUID());
        buffer.writeVarIntToBuffer(s);

    }

}

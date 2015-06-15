package mnm.mods.protocol.handlers.v5;

import java.io.IOException;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;

public class MapChunkBulk_5 implements PacketRead {


    @Override
    public Class<? extends Packet> getPacket() {
        return S26PacketMapChunkBulk.class;
    }

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        int read = buffer.readerIndex();

        // read the old stuff
        short length = buffer.readShort();
        int size = buffer.readInt();
        boolean isOverworld = buffer.readBoolean();

        byte[] allbytes = new byte[size * length];

        buffer.readBytes(allbytes, 0, size);

        int[] xPos = new int[length];
        int[] yPos = new int[length];
        short[] dataSize = new short[length];
        short[] add = new short[length];

        for (int i = 0; i < length; i++) {
            xPos[i] = buffer.readInt();
            yPos[i] = buffer.readInt();
            dataSize[i] = buffer.readShort();
            add[i] = buffer.readShort();
        }

        buffer.readerIndex(read);
        buffer.writerIndex(read);

        // reformat
        buffer.writeBoolean(isOverworld);
        buffer.writeVarIntToBuffer(length);

        for (int i = 0; i < length; i++) {
            buffer.writeInt(xPos[i]);
            buffer.writeInt(yPos[i]);
            buffer.writeShort(0);// dataSize[i]);
        }
        buffer.writeBytes(allbytes);
    }

}

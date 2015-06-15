package mnm.mods.protocol.handlers.v5;

import java.io.IOException;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.util.BlockPos;

public class SpawnPosition_5 implements PacketRead {

    @Override
    public Class<? extends Packet> getPacket() {
        return S05PacketSpawnPosition.class;
    }

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        // convert xyz to blockpos
        int read = buffer.readerIndex();
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        buffer.readerIndex(read);
        buffer.writerIndex(read);

        BlockPos pos = new BlockPos(x, y, z);
        buffer.writeBlockPos(pos);
    }

}

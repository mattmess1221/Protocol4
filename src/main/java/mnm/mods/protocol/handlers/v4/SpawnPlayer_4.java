package mnm.mods.protocol.handlers.v4;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.entity.DataWatcher;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;

public class SpawnPlayer_4 implements PacketRead {

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        new SpawnPlayer().processPacket(buffer);
    }

    @Override
    public Class<? extends Packet> getPacket() {
        return S0CPacketSpawnPlayer.class;
    }

    private static class SpawnPlayer {

        int entityID;
        GameProfile profile;
        int x;
        int y;
        int z;
        byte yaw;
        byte pitch;
        short item;
        List<?> metadata;

        void processPacket(PacketBuffer buffer) throws IOException {
            int start = buffer.readerIndex();
            readBuffer(buffer);
            buffer.clear();
            buffer.writeVarIntToBuffer(0x0C);
            writeBuffer(buffer);
            buffer.readerIndex(start);
        }

        void readBuffer(PacketBuffer buffer) throws IOException {
            entityID = buffer.readVarIntFromBuffer();
            String struuid = buffer.readStringFromBuffer(36);
            UUID uuid = UUIDTypeAdapter.fromString(struuid);
            String name = buffer.readStringFromBuffer(16);
            profile = new GameProfile(uuid, name);
            ProfileCache.fillProfileProperties(profile, true);
            x = buffer.readInt();
            y = buffer.readInt();
            z = buffer.readInt();
            yaw = buffer.readByte();
            pitch = buffer.readByte();
            item = buffer.readShort();
            metadata = DataWatcher.readWatchedListFromPacketBuffer(buffer);
        }

        void writeBuffer(PacketBuffer buffer) throws IOException {
            buffer.writeVarIntToBuffer(this.entityID);
            UUID uuid = this.profile.getId();
            buffer.writeString(uuid == null ? "" : uuid.toString());
            buffer.writeString(this.profile.getName());
            buffer.writeVarIntToBuffer(this.profile.getProperties().size());
            Iterator<?> iterator = this.profile.getProperties().values().iterator();

            while (iterator.hasNext()) {
                Property property = (Property) iterator.next();
                buffer.writeString(property.getName());
                buffer.writeString(property.getValue());
                buffer.writeString(property.getSignature());
            }

            buffer.writeInt(this.x);
            buffer.writeInt(this.y);
            buffer.writeInt(this.z);
            buffer.writeByte(this.yaw);
            buffer.writeByte(this.pitch);
            buffer.writeShort(this.item);
            DataWatcher.writeWatchedListToPacketBuffer(this.metadata, buffer);
        }
    }
}

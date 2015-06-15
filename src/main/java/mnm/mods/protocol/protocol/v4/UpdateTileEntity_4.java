package mnm.mods.protocol.protocol.v4;

import java.io.IOException;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.StringUtils;

import com.mojang.authlib.GameProfile;

public class UpdateTileEntity_4 implements PacketRead {

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        new UpdateTileEntity().processPacket(buffer);
    }

    @Override
    public Class<? extends Packet> getPacket() {
        return S35PacketUpdateTileEntity.class;
    }

    private static class UpdateTileEntity {
        int x;
        short y;
        int z;
        short action;
        NBTTagCompound nbt;

        void processPacket(PacketBuffer buffer) throws IOException {
            readBuffer(buffer);

            if (nbt.hasKey("ExtraType") && !StringUtils.isNullOrEmpty(nbt.getString("ExtraType"))) {
                String name = nbt.getString("ExtraType");
                NBTTagCompound profile = new NBTTagCompound();
                NBTUtil.writeGameProfile(new NBTTagCompound(), new GameProfile(null, name));
                nbt.setTag("Owner", profile);
            }

            buffer.clear();
            buffer.writeVarIntToBuffer(0x35);
            writeBuffer(buffer);
        }

        void readBuffer(PacketBuffer buffer) throws IOException {
            x = buffer.readInt();
            y = buffer.readShort();
            z = buffer.readInt();
            action = buffer.readUnsignedByte();
            nbt = buffer.readNBTTagCompoundFromBuffer();
        }

        void writeBuffer(PacketBuffer buffer) throws IOException {
            buffer.writeInt(x);
            buffer.writeShort(y);
            buffer.writeInt(z);
            buffer.writeByte(action);
            buffer.writeNBTTagCompoundToBuffer(nbt);
        }
    }
}

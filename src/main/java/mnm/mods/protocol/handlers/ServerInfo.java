package mnm.mods.protocol.handlers;

import java.io.IOException;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.server.S00PacketServerInfo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ServerInfo implements PacketRead {

    private int target;
    private int value;

    public ServerInfo(int targ, int val) {
        this.target = targ;
        this.value = val;
    }

    @Override
    public Class<? extends Packet> getPacket() {
        return S00PacketServerInfo.class;
    }

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        String json = buffer.readStringFromBuffer(32767);
        JsonObject response = new Gson().fromJson(json, JsonObject.class);
        JsonObject version = response.get("version").getAsJsonObject();

        int protocol = version.get("protocol").getAsInt();
        if (protocol == target) {
            version.addProperty("protocol", value);
        } else if (protocol == value) {
            version.addProperty("protocol", target);
        }

        buffer.clear();
        buffer.writeVarIntToBuffer(0x00);
        buffer.writeString(new Gson().toJson(response));
    }

}

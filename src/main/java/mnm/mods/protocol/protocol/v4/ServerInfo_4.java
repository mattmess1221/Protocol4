package mnm.mods.protocol.protocol.v4;

import java.io.IOException;

import mnm.mods.protocol.interfaces.PacketRead;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.server.S00PacketServerInfo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ServerInfo_4 implements PacketRead {

    private Gson gson = new Gson();

    @Override
    public void readPacketData(PacketBuffer buffer) throws IOException {
        // TODO Auto-generated method stub
        String json = buffer.readStringFromBuffer(32767);
        JsonObject response = gson.fromJson(json, JsonObject.class);
        JsonObject version = response.get("version").getAsJsonObject();

        if (version.get("protocol").getAsInt() == 5) {
            version.addProperty("protocol", 4);
        } else if (version.get("protocol").getAsInt() == 4) {
            version.addProperty("protocol", 5);
        }

        buffer.clear();
        buffer.writeVarIntToBuffer(0x00);
        buffer.writeString(gson.toJson(response));
    }

    @Override
    public Class<? extends Packet> getPacket() {
        // TODO Auto-generated method stub
        return S00PacketServerInfo.class;
    }
}

package mnm.mods.protocol.interfaces;

import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public interface PacketRead extends PacketIOListener {

    void readPacketData(PacketBuffer buffer) throws IOException;
}

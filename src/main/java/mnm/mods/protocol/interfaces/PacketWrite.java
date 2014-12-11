package mnm.mods.protocol.interfaces;

import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public interface PacketWrite extends PacketIOListener {

    void writePacketData(PacketBuffer buffer) throws IOException;
}

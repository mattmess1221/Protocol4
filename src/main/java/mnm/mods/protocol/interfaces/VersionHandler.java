package mnm.mods.protocol.interfaces;

import mnm.mods.protocol.EnumProtocols;
import net.minecraft.network.Packet;

public interface VersionHandler {

    EnumProtocols getSupportedVersion();

    PacketRead getReader(Class<? extends Packet> packet);

    PacketWrite getWriter(Class<? extends Packet> packet);

    void addReadListener(PacketRead read);

    void addWriteListener(PacketWrite write);
}

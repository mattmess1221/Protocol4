package mnm.mods.protocol.interfaces;

import net.minecraft.network.Packet;

public interface PacketIOListener {

    Class<? extends Packet> getPacket();
}

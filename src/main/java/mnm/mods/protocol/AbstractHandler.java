package mnm.mods.protocol;

import java.util.Map;

import mnm.mods.protocol.interfaces.PacketRead;
import mnm.mods.protocol.interfaces.PacketWrite;
import mnm.mods.protocol.interfaces.VersionHandler;
import net.minecraft.network.Packet;

import com.google.common.collect.Maps;

public abstract class AbstractHandler implements VersionHandler {

    private Map<Class<? extends Packet>, PacketRead> readers = Maps.newHashMap();
    private Map<Class<? extends Packet>, PacketWrite> writers = Maps.newHashMap();

    @Override
    public PacketRead getReader(Class<? extends Packet> packet) {
        return readers.get(packet);
    }

    @Override
    public PacketWrite getWriter(Class<? extends Packet> packet) {
        return writers.get(packet);
    }

    @Override
    public void addReadListener(PacketRead read) {
        readers.put(read.getPacket(), read);
    }

    @Override
    public void addWriteListener(PacketWrite write) {
        writers.put(write.getPacket(), write);
    }

}

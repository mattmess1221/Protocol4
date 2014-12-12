package mnm.mods.protocol;

import com.google.common.collect.Maps;
import com.mumfrey.liteloader.RenderListener;

import mnm.mods.protocol.gui.MultiplayerMenu;
import mnm.mods.protocol.interfaces.PacketRead;
import mnm.mods.protocol.interfaces.PacketWrite;
import mnm.mods.protocol.interfaces.VersionHandler;
import mnm.mods.protocol.protocol.v4.Handler_4;
import mnm.mods.protocol.protocol.v5.Handler_5;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;

public class LiteModProtocol4 implements RenderListener {

    private static final Logger logger = LogManager.getLogger("Protocol4");

    private static final String NAME = "Protocol4";
    private static final String VERSION = "@VERSION@";

    public static LiteModProtocol4 instance;

    private Map<EnumProtocols, VersionHandler> handlers = Maps.newEnumMap(EnumProtocols.class);
    public EnumProtocols protocol = EnumProtocols.CURRENT;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public void init(File configPath) {
        instance = this;

        addHandler(new Handler_4());
        addHandler(new Handler_5());
    }

    private void addHandler(VersionHandler handler) {
        handlers.put(handler.getSupportedVersion(), handler);
    }

    public void readPacketData(Packet packet, PacketBuffer buffer) {
        int read = buffer.readerIndex();
        try {
            if (!Minecraft.getMinecraft().isSingleplayer()) {
                PacketRead reader = handlers.get(protocol).getReader(packet.getClass());
                if (reader != null)
                    reader.readPacketData(buffer);
            }
        } catch (Throwable e) {
            logger.throwing(e);
        } finally {
            buffer.readerIndex(read);
        }
    }

    public void writePacketData(Packet packet, PacketBuffer buffer) {
        try {
            if (!Minecraft.getMinecraft().isSingleplayer()) {
                PacketWrite writer = handlers.get(protocol).getWriter(packet.getClass());
                if (writer != null)
                    writer.writePacketData(buffer);
            }
        } catch (Throwable e) {
            logger.throwing(e);
        }
    }

    @Override
    public void onRenderGui(GuiScreen currentScreen) {
        if (currentScreen instanceof GuiMultiplayer) {
            MultiplayerMenu.insertButton((GuiMultiplayer) currentScreen, this.protocol);
        }
    }

    public void setProtocol(EnumProtocols protocol) {
        this.protocol = protocol;
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

    @Override
    public void onRender() {}

    @Override
    public void onRenderWorld() {}

    @Override
    public void onSetupCameraTransform() {}

}

package mnm.mods.protocol;

import java.io.File;
import java.util.Map;

import mnm.mods.protocol.gui.MultiplayerMenu;
import mnm.mods.protocol.handlers.CurrentHandler;
import mnm.mods.protocol.handlers.v4.Handler_4;
import mnm.mods.protocol.handlers.v5.Handler_5;
import mnm.mods.protocol.interfaces.PacketRead;
import mnm.mods.protocol.interfaces.PacketWrite;
import mnm.mods.protocol.interfaces.VersionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.google.common.collect.Maps;
import com.mumfrey.liteloader.RenderListener;

public class LiteModProtocol4 implements RenderListener {

    private static final Logger logger = LogManager.getLogger("Protocol4");

    private static final String NAME = "Protocol4";
    private static final String VERSION = "@VERSION@";

    public static LiteModProtocol4 instance;

    private Map<EnumProtocols, VersionHandler> handlers = Maps.newEnumMap(EnumProtocols.class);
    private EnumProtocols protocol = EnumProtocols.CURRENT;

    static {
        // enableDebug();
    }

    @SuppressWarnings("unused")
    private static void enableDebug() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(Level.DEBUG);
        ctx.updateLoggers();
    }

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
        addHandler(new CurrentHandler());
    }

    private void addHandler(VersionHandler handler) {
        handlers.put(handler.getSupportedVersion(), handler);
    }

    public void preReadPacketData(Packet packet, PacketBuffer buffer) {
        int indx = buffer.readerIndex();
        try {
            PacketRead reader = handlers.get(protocol).getReader(packet.getClass());
            if (reader != null)
                reader.readPacketData(buffer);

        } catch (Throwable e) {
            logger.throwing(e);
        } finally {
            buffer.readerIndex(indx);
        }
    }

    public void postWritePacketData(Packet packet, PacketBuffer buffer) {
        try {
            PacketWrite writer = handlers.get(protocol).getWriter(packet.getClass());
            if (writer != null)
                writer.writePacketData(buffer);

        } catch (Throwable e) {
            logger.throwing(e);
        } finally {
            buffer.readerIndex(0);
        }
    }

    @Override
    public void onRenderGui(GuiScreen currentScreen) {
        MultiplayerMenu.onRenderGui(currentScreen);
    }

    public void setProtocol(EnumProtocols protocol) {
        if (Minecraft.getMinecraft().theWorld == null) {
            this.protocol = protocol;
        }
    }

    public EnumProtocols getProtocol() {
        return protocol;
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

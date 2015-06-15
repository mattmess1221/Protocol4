package mnm.mods.protocol.handlers.v5;

import mnm.mods.protocol.AbstractHandler;
import mnm.mods.protocol.EnumProtocols;
import mnm.mods.protocol.handlers.Handshake;
import mnm.mods.protocol.handlers.ServerInfo;

/**
 * Handler for 1.7.10
 */
public class Handler_5 extends AbstractHandler {

    public Handler_5() {
        this.addWriteListener(new Handshake(5));
        this.addReadListener(new ServerInfo(5, 47));
        this.addReadListener(new JoinGame_5());
        this.addReadListener(new SpawnPosition_5());
        this.addReadListener(new ServerChat_5());
        this.addReadListener(new PlayerListItem_5());
        this.addReadListener(new MapChunkBulk_5());
        this.addReadListener(new EntityRelMove_5());
    }

    @Override
    public EnumProtocols getSupportedVersion() {
        return EnumProtocols.P_5;
    }

}

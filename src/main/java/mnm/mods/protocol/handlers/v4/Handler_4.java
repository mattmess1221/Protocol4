package mnm.mods.protocol.handlers.v4;

import mnm.mods.protocol.AbstractHandler;
import mnm.mods.protocol.EnumProtocols;
import mnm.mods.protocol.handlers.Handshake;
import mnm.mods.protocol.handlers.ServerInfo;

/**
 * Handler for 1.7.5
 */
public class Handler_4 extends AbstractHandler {

    public Handler_4() {
        this.addWriteListener(new Handshake(4));
        this.addReadListener(new ServerInfo(4, 5));
        this.addReadListener(new LoginSuccess_4());
        this.addReadListener(new SpawnPlayer_4());
        this.addReadListener(new UpdateTileEntity_4());
    }

    @Override
    public EnumProtocols getSupportedVersion() {
        return EnumProtocols.P_4;
    }

}
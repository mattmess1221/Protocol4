package mnm.mods.protocol.protocol.v4;

import mnm.mods.protocol.AbstractHandler;
import mnm.mods.protocol.EnumProtocols;

public class Handler_4 extends AbstractHandler {

    public Handler_4() {
        this.addReadListener(new LoginSuccess_4());
        this.addReadListener(new SpawnPlayer_4());
        this.addReadListener(new UpdateTileEntity_4());
        this.addWriteListener(new Handshake_4());
        this.addReadListener(new ServerInfo_4());
    }

    @Override
    public EnumProtocols getSupportedVersion() {
        return EnumProtocols.P_4;
    }

}

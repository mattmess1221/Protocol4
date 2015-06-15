package mnm.mods.protocol.protocol.v5;

import mnm.mods.protocol.AbstractHandler;
import mnm.mods.protocol.EnumProtocols;
import mnm.mods.protocol.protocol.Handshake;
import mnm.mods.protocol.protocol.ServerInfo;

public class Handler_5 extends AbstractHandler {

    public Handler_5() {
        this.addWriteListener(new Handshake(5));
        this.addReadListener(new ServerInfo(5, 47));
    }

    @Override
    public EnumProtocols getSupportedVersion() {
        return EnumProtocols.P_5;
    }

}

package mnm.mods.protocol.protocol.v5;

import mnm.mods.protocol.AbstractHandler;
import mnm.mods.protocol.EnumProtocols;

public class Handler_5 extends AbstractHandler {

    // Nothing is required to join a protocol 5 server

    @Override
    public EnumProtocols getSupportedVersion() {
        return EnumProtocols.P_5;
    }

}

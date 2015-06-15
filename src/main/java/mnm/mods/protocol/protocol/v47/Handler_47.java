package mnm.mods.protocol.protocol.v47;

import mnm.mods.protocol.AbstractHandler;
import mnm.mods.protocol.EnumProtocols;

public class Handler_47 extends AbstractHandler {

    // Nothing is required to join a protocol 47 server

    @Override
    public EnumProtocols getSupportedVersion() {
        return EnumProtocols.P_47;
    }

}

package mnm.mods.protocol;

import com.google.common.collect.Maps;

import java.util.Map;

public enum EnumProtocols {

    P_4(4, "1.7.2", "1.7.4", "1.7.5"),
    P_5(5, "1.7.9", "1.7.10"),
    P_47(47, "1.8", "1.8.1");

    public static final EnumProtocols CURRENT = P_5;

    private static final Map<Integer, EnumProtocols> PROTOCOLS = Maps.newHashMap();
    private static final Map<String, EnumProtocols> VERSIONS = Maps.newHashMap();

    private final int protocol;
    private final String[] versions;

    private EnumProtocols() {
        protocol = 0;
        versions = new String[0];
    }

    private EnumProtocols(int protocol, String... versions) {
        this.protocol = protocol;
        this.versions = versions;
    }

    public boolean supportsVersion(String target) {
        for (String version : versions) {
            if (version.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public int getProtocol() {
        return protocol;
    }

    public String[] getSupportedVersions() {
        return versions;
    }

    public boolean isCurrentVersion() {
        return this == CURRENT;
    }

    public static EnumProtocols getVersion(int protocol) {
        return PROTOCOLS.get(protocol);
    }

    public static EnumProtocols getVersion(String mcversion) {
        return VERSIONS.get(mcversion);
    }

    static {
        for (EnumProtocols proto : values()) {
            int protocol = proto.getProtocol();
            PROTOCOLS.put(protocol, proto);

            String[] versions = proto.getSupportedVersions();
            for (String version : versions) {
                VERSIONS.put(version, proto);
            }
        }
    }
}

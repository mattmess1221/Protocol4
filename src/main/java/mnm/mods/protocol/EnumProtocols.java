package mnm.mods.protocol;

public enum EnumProtocols {

    P_4(4, "1.7.2", "1.7.4", "1.7.5"),
    P_5(5, "1.7.9", "1.7.10"),
    P_47(47, "1.8", "1.8.7"),
    ;

    public static final EnumProtocols CURRENT = P_47;

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
}

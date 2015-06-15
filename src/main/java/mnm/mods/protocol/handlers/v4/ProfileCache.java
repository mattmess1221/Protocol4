package mnm.mods.protocol.handlers.v4;

import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;

/*
 * Handles the retrieving of profile properties and caches them.
 * Doesn't retrieve if it's already cached. Only used on 1.7.5
 * servers, as they don't send Profile Properties.
 */
public class ProfileCache {

    private static Map<UUID, PropertyMap> cache = Maps.newHashMap();

    public static void fillProfileProperties(GameProfile profile, boolean secure) {
        if (cache.containsKey(profile.getId())) {
            profile.getProperties().putAll(cache.get(profile.getId()));
        } else {
            Minecraft.getMinecraft().getSessionService().fillProfileProperties(profile, secure);
            if (!profile.getProperties().isEmpty()) {
                cache.put(profile.getId(), profile.getProperties());
            } else {
                LogManager.getLogger("Protocol4").warn(
                        "Profile properties for '" + profile.getName() + "' returned empty.");
            }
        }
    }

    public static void clearCache() {
        cache.clear();
    }
}

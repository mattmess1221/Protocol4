package mnm.mods.protocol.gui;

import com.google.common.collect.Lists;

import mnm.mods.protocol.EnumProtocols;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Field;
import java.util.List;

public class MultiplayerMenu {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static GuiMultiplayer screen;

    public static void insertButton(GuiMultiplayer gui, EnumProtocols proto) {
        if (gui != screen) {
            screen = gui;
            try {
                ProtocolButton button = new ProtocolButton(-1, gui.width / 2 + 165,
                        gui.height - 40, proto);
                Field fldButtons = getFieldsOfType(GuiScreen.class, List.class)[0];
                fldButtons.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<GuiButton> lstButtons = (List<GuiButton>) fldButtons.get(gui);
                lstButtons.add(button);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static Field[] getFieldsOfType(Class<?> owner, Class<?> type) {
        List<Field> fields = Lists.newArrayList();
        for (Field field : owner.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                fields.add(field);
            }
        }
        return fields.toArray(new Field[0]);
    }

    public static void refresh() {
        mc.displayGuiScreen(new GuiMultiplayer(screen));
    }

}

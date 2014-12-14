package mnm.mods.protocol.gui;

import com.google.common.collect.Lists;

import mnm.mods.protocol.EnumProtocols;
import mnm.mods.protocol.LiteModProtocol4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Field;
import java.util.List;

public class MultiplayerMenu {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static GuiScreen screen;

    public static void onRenderGui(GuiScreen screen) {
        MultiplayerMenu.screen = screen;
        if (screen instanceof GuiMultiplayer) {
            insertButton((GuiMultiplayer) screen, LiteModProtocol4.instance.getProtocol());
        }
    }

    @SuppressWarnings("unchecked")
    private static void insertButton(GuiMultiplayer gui, EnumProtocols proto) {
        try {
            ProtocolButton button = new ProtocolButton(-1, gui.width / 2 + 165, gui.height - 40,
                    proto);
            Field fldButtons = getFieldsOfType(GuiScreen.class, List.class)[0];
            fldButtons.setAccessible(true);
            List<GuiButton> lstButtons = (List<GuiButton>) fldButtons.get(gui);
            lstButtons.add(button);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
        try {
            Field field = getFieldsOfType(GuiMultiplayer.class, GuiScreen.class)[0];
            field.setAccessible(true);
            GuiScreen screen = (GuiScreen) field.get(MultiplayerMenu.screen);
            mc.displayGuiScreen(new GuiMultiplayer(screen));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

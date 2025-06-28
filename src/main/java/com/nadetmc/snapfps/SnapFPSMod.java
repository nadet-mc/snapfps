package com.nadetmc.snapfps;

import com.nadetmc.snapfps.config.ConfigData;
import com.nadetmc.snapfps.config.ConfigManager;
import com.nadetmc.snapfps.mixin.MinecraftClientAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class SnapFPSMod implements ClientModInitializer {
    public static final String MOD_ID = "snapfps";

    public static boolean ShowOverlay = true;

    public static SnapFPSHistory SnapFPSHistory = new SnapFPSHistory();

    @Override
    public void onInitializeClient() {
        ConfigData config = ConfigManager.loadConfig();

        KeyBinding toggleKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.snapfps.toggleOverlay",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_DONT_CARE,
                "key.snapfps.category"));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            int currentFps = ((MinecraftClientAccessor) client).getCurrentFps();
            SnapFPSHistory.add(currentFps);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKeybinding.wasPressed() && config.keybindMode == ConfigData.KeyMode.Toggle) {
                config.enabled = !config.enabled;
            }
            if (config.keybindMode == ConfigData.KeyMode.PushToShow) {
                ShowOverlay = toggleKeybinding.isPressed();
            }
            else {
                ShowOverlay = config.enabled;
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            ConfigManager.saveConfig();
        });
    }
}
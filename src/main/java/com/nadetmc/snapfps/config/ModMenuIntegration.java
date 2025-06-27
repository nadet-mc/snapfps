package com.nadetmc.snapfps.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.nadetmc.snapfps.gui.ClothOptionScreen;
import com.nadetmc.snapfps.gui.FallbackOptionScreen;
import com.nadetmc.snapfps.gui.YaclOptionScreen;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
            return parent -> YaclOptionScreen.generateScreen(parent);
        }
        else if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return parent -> ClothOptionScreen.generateScreen(parent);
        }
        else {
            return parent -> new FallbackOptionScreen(parent);
        }
    }
}
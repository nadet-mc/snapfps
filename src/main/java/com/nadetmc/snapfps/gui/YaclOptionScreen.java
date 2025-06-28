package com.nadetmc.snapfps.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.*;
import com.nadetmc.snapfps.config.ConfigData;
import com.nadetmc.snapfps.config.ConfigManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.Color;

// https://docs.isxander.dev/yet-another-config-lib/

public class YaclOptionScreen {
    public static Screen generateScreen(Screen parent) {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder();
        categoryBuilder.name(Text.translatable("text.snapfps.options.title"));

        final ConfigData configDefaults = new ConfigData();
        ConfigData config = ConfigManager.getConfig();

        categoryBuilder.option(Option.<Boolean>createBuilder()
                .name(Text.translatable("text.snapfps.options.enabled"))
                .binding(configDefaults.enabled, () -> config.enabled, newValue -> config.enabled = newValue)
                .controller(YaclOptionScreen::createBooleanController)
                .build()
        );
        categoryBuilder.option(Option.<Boolean>createBuilder()
                .name(Text.translatable("text.snapfps.options.advancedStats"))
                .binding(configDefaults.advancedStats, () -> config.advancedStats, newValue -> config.advancedStats = newValue)
                .controller(YaclOptionScreen::createBooleanController)
                .build()
        );

        categoryBuilder.option(Option.<Float>createBuilder()
                .name(Text.translatable("text.snapfps.options.textSize"))
                .binding(configDefaults.textSize, () -> config.textSize, newValue -> config.textSize = newValue)
                .controller(option -> FloatSliderControllerBuilder.create(option).range(0.1f, 3.0f).step(0.1f))
                .build()
        );
        categoryBuilder.option(Option.<Color>createBuilder()
                .name(Text.translatable("text.snapfps.options.textColor"))
                .binding(new Color(configDefaults.textColor), () -> new Color(config.textColor), newValue -> config.textColor = (newValue.getRGB() & 0xFFFFFF))
                .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                .build()
        );
        categoryBuilder.option(Option.<Integer>createBuilder()
                .name(Text.translatable("text.snapfps.options.textAlpha"))
                .binding(configDefaults.textAlpha, () -> config.textAlpha, newValue -> config.textAlpha = newValue)
                .controller(option -> IntegerSliderControllerBuilder.create(option).range(0, 255).step(1))
                .build()
        );
        categoryBuilder.option(Option.<Boolean>createBuilder()
                .name(Text.translatable("text.snapfps.options.textShadows"))
                .binding(configDefaults.textShadows, () -> config.textShadows, newValue -> config.textShadows = newValue)
                .controller(YaclOptionScreen::createBooleanController)
                .build()
        );

        categoryBuilder.option(Option.<Integer>createBuilder()
                .name(Text.translatable("text.snapfps.options.offsetTop"))
                .binding(configDefaults.offsetTop, () -> config.offsetTop, newValue -> config.offsetTop = newValue)
                .controller(IntegerFieldControllerBuilder::create)
                .build()
        );
        categoryBuilder.option(Option.<Integer>createBuilder()
                .name(Text.translatable("text.snapfps.options.offsetLeft"))
                .binding(configDefaults.offsetLeft, () -> config.offsetLeft, newValue -> config.offsetLeft = newValue)
                .controller(IntegerFieldControllerBuilder::create)
                .build()
        );

        categoryBuilder.option(Option.<ConfigData.KeyMode>createBuilder()
                .name(Text.translatable("text.snapfps.options.keybindMode"))
                .binding(configDefaults.keybindMode, () -> config.keybindMode, newValue -> config.keybindMode = newValue)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(ConfigData.KeyMode.class))
                .build()
        );

        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder();
        builder.title(Text.translatable("text.snapfps.options.title"));
        builder.category(categoryBuilder.build());
        builder.save(() -> ConfigManager.saveConfig());
        return builder.build().generateScreen(parent);
    }

    private static BooleanControllerBuilder createBooleanController(Option<Boolean> option) {
        return BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true);
    }
}
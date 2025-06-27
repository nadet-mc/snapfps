package com.nadetmc.snapfps.mixin;

import com.nadetmc.snapfps.SnapFPSMod;
import com.nadetmc.snapfps.config.ConfigData;
import com.nadetmc.snapfps.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ConfigData config = ConfigManager.getConfig();

        if (!client.options.hudHidden && !client.getDebugHud().shouldShowDebugHud() && config.enabled && config.textAlpha > 3 && SnapFPSMod.ShowOverlay) {
            String text;
            if (!config.advancedStats) {
                text = ((MinecraftClientAccessor) client).getCurrentFps() + " FPS";
            }
            else {
                text = String.format("%d FPS (%d min | %d avg | %d max)", ((MinecraftClientAccessor) client).getCurrentFps(),
                        SnapFPSMod.SnapFPSHistory.getMinimum(), SnapFPSMod.SnapFPSHistory.getAverage(), SnapFPSMod.SnapFPSHistory.getMaximum());
            }

            int textPosX = config.offsetLeft;
            int textPosY = config.offsetTop;

            double guiScale = client.getWindow().getScaleFactor();
            if (guiScale > 0) {
                textPosX /= guiScale;
                textPosY /= guiScale;
            }

            // Prevent text to render outside screenspace
            int maxTextPosX = client.getWindow().getScaledWidth() - client.textRenderer.getWidth(text);
            int maxTextPosY = client.getWindow().getScaledHeight() - client.textRenderer.fontHeight;
            textPosX = Math.min(textPosX, maxTextPosX);
            textPosY = Math.min(textPosY, maxTextPosY);

            int textColor = ((config.textAlpha & 0xFF) << 24) | (config.textColor & 0xFFFFFF);

            this.renderText(context, client.textRenderer, text, textPosX, textPosY, textColor, config.textSize, config.textShadows);
        }
    }

    @Unique
    private void renderText(DrawContext context, TextRenderer textRenderer, String text, int x, int y, int color, float scale, boolean shadowed) {
        if (scale != 1.0f) {
            MatrixStack matrixStack = context.getMatrices();
            matrixStack.push();
            matrixStack.translate(x, y, 0);
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(-x, -y, 0);
            context.drawText(textRenderer, text, x, y, color, shadowed);
            matrixStack.pop();
        }
        else {
            context.drawText(textRenderer, text, x, y, color, shadowed);
        }
    }
}
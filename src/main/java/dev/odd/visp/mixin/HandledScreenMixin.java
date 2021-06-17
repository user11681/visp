package dev.odd.visp.mixin;

import dev.odd.visp.Visp;
import dev.odd.visp.config.VispConfiguration;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
abstract class HandledScreenMixin extends DrawableHelper {
    @Unique
    private static final ReferenceOpenHashSet<ItemStack> highlighted = new ReferenceOpenHashSet<>(Visp.inventorySize);

    @Inject(method = "init", at = @At("RETURN"))
    public void diffItemStacks(CallbackInfo info) {
        Visp.getInventory(highlighted);

        for (final ItemStack itemStack : Visp.filteredStacks) {
            highlighted.remove(itemStack);
        }
    }

    @Inject(method = "onClose", at = @At("RETURN"))
    public void storeCurrentItemStacks(CallbackInfo info) {
        Visp.filteredStacks.clear();
        Visp.getInventory(Visp.filteredStacks);
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableDepthTest()V"))
    private void highlightSlot(MatrixStack matrixes, Slot slot, CallbackInfo info) {
        if (slot.inventory == MinecraftClient.getInstance().player.getInventory()
                && highlighted.contains(slot.getStack())
                && !slot.getStack().isEmpty()) {
            Integer rarityColor = VispConfiguration.instance.colors.get(slot.getStack().getRarity());
            int color = VispConfiguration.instance.useGlobalColor
                    ? VispConfiguration.instance.globalColor
                    : rarityColor == null ? Visp.COMMON : rarityColor;

            if (VispConfiguration.instance.fill) {
                fill(matrixes, slot.x - 1, slot.y - 1, slot.x + 16, slot.y + 16, color);
            } else {
                this.drawHorizontalLine(matrixes, slot.x - 1, slot.x + 16, slot.y - 1, color);
                this.drawHorizontalLine(matrixes, slot.x - 1, slot.x + 16, slot.y + 16, color);
                this.drawVerticalLine(matrixes, slot.x - 1, slot.y - 1, slot.y + 16, color);
                this.drawVerticalLine(matrixes, slot.x + 16, slot.y - 1, slot.y + 16, color);
            }
        }
    }
}
package user11681.visp.mixin;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import user11681.visp.Visp;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
abstract class HandledScreenMixin extends DrawableHelper {
    @Shadow
    @Final
    protected PlayerInventory playerInventory;

    @Unique
    private static final ReferenceOpenHashSet<ItemStack> highlighted = new ReferenceOpenHashSet<>(Visp.inventorySize, 1);

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

    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void highlightSlot(MatrixStack matrices, Slot slot, CallbackInfo info) {
        if (slot.inventory == this.playerInventory && highlighted.contains(slot.getStack()) && !slot.getStack().isEmpty()) {
            final Rarity rarity = slot.getStack().getRarity();
            final Integer rarityColor = rarity.formatting.getColorValue();
            final int color = rarityColor == null || rarity == Rarity.COMMON ? 0xFFA0FFA0 : 0xFF000000 | rarityColor;

            this.drawHorizontalLine(matrices, slot.x, slot.x + 16, slot.y, color);
            this.drawHorizontalLine(matrices, slot.x, slot.x + 16, slot.y + 16, color);
            this.drawVerticalLine(matrices, slot.x, slot.y, slot.y + 16, color);
            this.drawVerticalLine(matrices, slot.x + 16, slot.y, slot.y + 16, color);
        }
    }
}

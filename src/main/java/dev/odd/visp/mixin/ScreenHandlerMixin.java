package dev.odd.visp.mixin;

import dev.odd.visp.Visp;
import dev.odd.visp.config.VispConfiguration;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
abstract class ScreenHandlerMixin {
    @Inject(method = "setStackInSlot", at = @At("HEAD"))
    public void ignoreIncrementation(int slot, ItemStack stack, CallbackInfo info) {
        if ((Object) this instanceof PlayerScreenHandler
                && !VispConfiguration.instance.highlightExistingItemStacks
                && (Visp.deserializedFilter == null || Visp.deserializedFilter.size() == 0)
                && ItemStack.areItemsEqualIgnoreDamage(Visp.client.player.playerScreenHandler.getStacks().get(slot), stack)) {
            Visp.filteredStacks.add(stack);
        }
    }
}

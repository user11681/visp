package user11681.visp.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import user11681.visp.Visp;
import user11681.visp.VispConfiguration;

@Environment(EnvType.CLIENT)
@Mixin(ScreenHandler.class)
abstract class ScreenHandlerMixin {
    @Inject(method = "setStackInSlot", at = @At("HEAD"))
    public void ignoreIncrementation(int slot, ItemStack stack, CallbackInfo info) {
        if (!VispConfiguration.instance.highlightExistingItemStacks && (Object) this instanceof PlayerScreenHandler && (Visp.deserializedFilter == null || Visp.deserializedFilter.size() == 0) && ItemStack.areItemsEqualIgnoreDamage(Visp.client.player.playerScreenHandler.getStacks().get(slot), stack)) {
            Visp.filteredStacks.add(stack);
        }
    }
}

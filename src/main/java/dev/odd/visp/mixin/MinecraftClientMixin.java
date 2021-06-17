package dev.odd.visp.mixin;

import dev.odd.visp.Visp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At(value = "HEAD"))
    public void saveInventory(Screen screen, CallbackInfo info) {
        final ClientPlayerEntity player = ((MinecraftClient) (Object) this).player;

        if (player != null) {
            final NbtCompound saveTag = new NbtCompound();
            final NbtList previousTag = new NbtList();

            for (final ItemStack itemStack : Visp.filteredStacks) {
                if (!itemStack.isEmpty()) {
                    previousTag.add(itemStack.writeNbt(new NbtCompound()));
                }
            }

            saveTag.put("previous", previousTag);

            try {
                if (!Visp.saveFile.exists()) {
                    Visp.saveFile.getParentFile().mkdirs();
                    Visp.saveFile.createNewFile();
                }

                saveTag.write(new DataOutputStream(new FileOutputStream(Visp.saveFile)));

                Visp.VLogger.info("Saved visp inventory data");
            } catch (final IOException throwable) {
                throw new RuntimeException(throwable);
            }
        }
    }
}
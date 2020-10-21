package user11681.visp.asm.mixin;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import user11681.visp.Visp;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At(value = "HEAD"))
    public void saveInventory(final Screen screen, final CallbackInfo info) {
        final ClientPlayerEntity player = ((MinecraftClient) (Object) this).player;

        if (player != null) {
            final CompoundTag saveTag = new CompoundTag();
            final ListTag previousTag = new ListTag();

            for (final ItemStack itemStack : Visp.filteredStacks) {
                if (!itemStack.isEmpty()) {
                    previousTag.add(itemStack.toTag(new CompoundTag()));
                }
            }

            saveTag.put("previous", previousTag);

            try {
                if (!Visp.saveFile.exists()) {
                    Visp.saveFile.getParentFile().mkdirs();
                    Visp.saveFile.createNewFile();
                }

                saveTag.write(new DataOutputStream(new FileOutputStream(Visp.saveFile)));
            } catch (final IOException throwable) {
                throw new RuntimeException(throwable);
            }
        }
    }
}

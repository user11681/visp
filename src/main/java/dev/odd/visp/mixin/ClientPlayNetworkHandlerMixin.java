package dev.odd.visp.mixin;

import dev.odd.visp.Visp;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtTagSizeTracker;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

@Mixin(ClientPlayNetworkHandler.class)
abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At("RETURN"))
    public void loadInventory(GameJoinS2CPacket packet, CallbackInfo ci) throws Throwable {
        final IntegratedServer integratedServer = Visp.client.getServer();

        if (integratedServer == null) {
            Visp.saveFile = new File(FabricLoader.getInstance().getConfigDir().resolve(Visp.ID).resolve("remote").toFile(), Visp.client.getCurrentServerEntry().address + ".dat");
        } else {
            Visp.saveFile = new File(FabricLoader.getInstance().getConfigDir().resolve(Visp.ID).resolve("local").toFile(), integratedServer.getSaveProperties().getLevelName() + ".dat");
        }

        if (Visp.saveFile.exists()) {
            final NbtCompound saveTag = NbtCompound.TYPE.read(new DataInputStream(new FileInputStream(Visp.saveFile)), 0, new NbtTagSizeTracker(2097152L));

            Visp.deserializedFilter = (NbtList) saveTag.get("previous");
        }

        Visp.inventorySize = Visp.getInventory(ReferenceArrayList.wrap(new ItemStack[41], 0)).size();
        Visp.filteredStacks = new ReferenceOpenHashSet<>(Visp.inventorySize, 1);
    }

    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("RETURN"))
    public void filter(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
        if (packet.getSyncId() == 0) {
            if (Visp.deserializedFilter != null) {
                final int previousCount = Visp.deserializedFilter.size();

                for (int i = 0; i < previousCount; i++) {
                    if (ItemStack.areEqual(packet.getItemStack(), ItemStack.fromNbt(Visp.deserializedFilter.getCompound(i)))) {
                        Visp.filteredStacks.add(packet.getItemStack());
                        Visp.deserializedFilter.remove(i);

                        return;
                    }
                }
            }
        }
    }
}
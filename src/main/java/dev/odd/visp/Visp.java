package dev.odd.visp;

import dev.odd.visp.config.MapEntry;
import dev.odd.visp.config.VispConfiguration;
import dev.odd.visp.mixin.PlayerInventoryAccess;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Visp implements ClientModInitializer {
	public static final Logger VLogger = LogManager.getFormatterLogger("Visp");

	public static final String ID = "visp";
	public static final int COMMON = 0xFFA0FFA0;

	public static final MinecraftClient client = MinecraftClient.getInstance();

	public static int inventorySize;
	public static ReferenceOpenHashSet<ItemStack> filteredStacks;
	public static NbtList deserializedFilter;
	public static File saveFile;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		VispConfiguration.instance = AutoConfig.register(VispConfiguration.class, GsonConfigSerializer::new).getConfig();
		AutoConfig.getGuiRegistry(VispConfiguration.class).registerTypeProvider(new MapEntry(), Map.class);

		VLogger.info("Visp Initialized");
	}

	public static <T extends Collection<ItemStack>> T getInventory(T storage) {
		List<DefaultedList<ItemStack>> inventories = ((PlayerInventoryAccess) client.player.getInventory()).getCombinedInventory();

		for (DefaultedList<ItemStack> inventory : inventories) {
			storage.addAll(inventory);
		}

		return storage;
	}
}

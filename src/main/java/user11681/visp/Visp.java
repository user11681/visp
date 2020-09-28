package user11681.visp;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.io.File;
import java.util.Collection;
import java.util.List;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.collection.DefaultedList;
import user11681.visp.asm.mixin.PlayerInventoryAccess;
import user11681.visp.config.VispConfiguration;

@Environment(EnvType.CLIENT)
@SuppressWarnings("ConstantConditions")
public class Visp implements ClientModInitializer {
    public static final MinecraftClient client = MinecraftClient.getInstance();
    public static int inventorySize;
    public static ReferenceOpenHashSet<ItemStack> filteredStacks;
    public static ListTag deserializedFilter;
    public static File saveFile;

    public static <T extends Collection<ItemStack>> T getInventory(final T storage) {
        final List<DefaultedList<ItemStack>> inventories = ((PlayerInventoryAccess) client.player.inventory).getCombinedInventory();
        final int size = inventories.size();

        for (int i = 0; i < size; i++) {
            storage.addAll(inventories.get(i));
        }

        return storage;
    }

    @Override
    public void onInitializeClient() {
        VispConfiguration.instance = AutoConfig.register(VispConfiguration.class, Toml4jConfigSerializer::new).getConfig();
    }
}

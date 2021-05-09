package user11681.visp;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.collection.DefaultedList;
import user11681.visp.config.MapEntry;
import user11681.visp.config.VispConfiguration;
import user11681.visp.mixin.PlayerInventoryAccess;

@Environment(EnvType.CLIENT)
@SuppressWarnings("ConstantConditions")
public class Visp {
    public static final String ID = "visp";
    public static final int COMMON = 0xFFA0FFA0;

    public static final MinecraftClient client = MinecraftClient.getInstance();

    public static int inventorySize;
    public static ReferenceOpenHashSet<ItemStack> filteredStacks;
    public static ListTag deserializedFilter;
    public static File saveFile;

    public static <T extends Collection<ItemStack>> T getInventory(T storage) {
        List<DefaultedList<ItemStack>> inventories = ((PlayerInventoryAccess) client.player.inventory).getCombinedInventory();

        for (DefaultedList<ItemStack> inventory : inventories) {
            storage.addAll(inventory);
        }

        return storage;
    }

    public static void init() throws Throwable {
        Path config = FabricLoader.getInstance().getConfigDir().resolve("visp.toml");

        if (Files.exists(config) && Files.getLastModifiedTime(config).compareTo(FileTime.from(OffsetDateTime.of(2021, 5, 9, 1, 36, 0, 0, ZoneOffset.UTC).toInstant())) <= 0) {
            Files.delete(config);
        }

        VispConfiguration.instance = AutoConfig.register(VispConfiguration.class, Toml4jConfigSerializer::new).getConfig();
        AutoConfig.getGuiRegistry(VispConfiguration.class).registerTypeProvider(new MapEntry(), Map.class);
    }
}

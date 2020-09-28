package user11681.visp.asm.mixin;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(PlayerInventory.class)
public interface PlayerInventoryAccess {
    @Accessor("combinedInventory")
    List<DefaultedList<ItemStack>> getCombinedInventory();
}

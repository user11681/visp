package user11681.visp.config;

import java.util.EnumMap;
import java.util.Map;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config.Gui.Background;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.ColorPicker;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Excluded;
import net.minecraft.util.Rarity;
import user11681.visp.Visp;

@Config(name = Visp.ID)
@Background("textures/block/andesite.png")
public class VispConfiguration implements ConfigData {
    @Excluded
    public static transient VispConfiguration instance;

    public boolean highlightExistingItemStacks;

    public boolean fill;

    public boolean useGlobalColor;

    @ColorPicker(allowAlpha = true)
    public int globalColor = Visp.COMMON;

    public Map<Rarity, Integer> colors = new EnumMap<>(Rarity.class);

    @SuppressWarnings("ConstantConditions")
    public VispConfiguration() {
        for (Rarity rarity : Rarity.values()) {
            colors.put(rarity, rarity == Rarity.COMMON ? Visp.COMMON : 0xFF000000 | rarity.formatting.getColorValue());
        }
    }
}

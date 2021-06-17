package dev.odd.visp.config;

import dev.odd.visp.Visp;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.Rarity;

import java.util.EnumMap;
import java.util.Map;

@Config(name = Visp.ID)
public class VispConfiguration implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public static transient VispConfiguration instance;

    public boolean highlightExistingItemStacks;

    public boolean fill;

    public boolean useGlobalColor;

    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int globalColor = Visp.COMMON;

    public Map<Rarity, Integer> colors = new EnumMap<>(Rarity.class);

    @SuppressWarnings("ConstantConditions")
    public VispConfiguration() {
        for (Rarity rarity : Rarity.values()) {
            colors.put(rarity, rarity == Rarity.COMMON ? Visp.COMMON : 0xFF000000 | rarity.formatting.getColorValue());
        }
    }
}

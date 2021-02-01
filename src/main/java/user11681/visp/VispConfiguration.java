package user11681.visp;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config.Gui.Background;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Excluded;

@Config(name = Visp.ID)
@Background("textures/block/andesite.png")
public class VispConfiguration implements ConfigData {
    @Excluded
    public static transient VispConfiguration instance;

    public boolean highlightExistingItemStacks = false;
}

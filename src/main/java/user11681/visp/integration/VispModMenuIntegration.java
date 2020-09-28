package user11681.visp.integration;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.gui.screen.Screen;
import user11681.visp.config.VispConfiguration;

public class VispModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (final Screen parent) -> AutoConfig.getConfigScreen(VispConfiguration.class, parent).get();
    }
}

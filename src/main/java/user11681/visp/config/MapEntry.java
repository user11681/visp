package user11681.visp.config;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import me.sargunvohra.mcmods.autoconfig1u.gui.registry.api.GuiProvider;
import me.sargunvohra.mcmods.autoconfig1u.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.TranslatableText;

@SuppressWarnings("rawtypes")
public class MapEntry implements GuiProvider {
    @Override
    public List<AbstractConfigListEntry> get(String i18n, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        ConfigEntryBuilder builder = ConfigEntryBuilder.create();

        return Collections.singletonList(builder.startSubCategory(new TranslatableText(i18n), VispConfiguration.instance.colors.entrySet().stream()
            .map((entry) -> builder.startColorField(new TranslatableText(i18n + '.' + entry.getKey().name().toLowerCase(Locale.ROOT)), entry.getValue())
                .setDefaultValue(entry::getValue)
                .setSaveConsumer(entry::setValue)
                .setAlphaMode(true)
                .build())
            .collect(Collectors.toList())).build());
    }
}

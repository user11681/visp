package dev.odd.visp.config;

import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.TranslatableText;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MapEntry implements GuiProvider {
    @Override
    public List<AbstractConfigListEntry> get(String s, Field field, Object o, Object o1, GuiRegistryAccess guiRegistryAccess) {
        ConfigEntryBuilder builder = ConfigEntryBuilder.create();

        return Collections.singletonList(builder.startSubCategory(new TranslatableText(s), VispConfiguration.instance.colors.entrySet().stream()
                .map((entry) -> builder.startColorField(new TranslatableText(s + '.' + entry.getKey().name().toLowerCase(Locale.ROOT)), entry.getValue())
                        .setDefaultValue(entry::getValue)
                        .setSaveConsumer(entry::setValue)
                        .setAlphaMode(true)
                        .build())
                .collect(Collectors.toList())).build());
    }
}

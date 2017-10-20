package org.waterpicker.particlewings.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@ConfigSerializable
public class Config {
    @Setting(value = "update interval", comment = "Ticks between effect update:")
    public Integer interval = 5;

    @Setting(value = "use gui", comment = "Should GUI instead of command be used")
    public Boolean useGui = false;
}

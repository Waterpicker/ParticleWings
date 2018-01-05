package org.waterpicker.particlewings.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class WingConfig {
    @Setting
    public String right = "";

    @Setting
    public String left = "";

    @Setting
    public int angle1 = 0;

    @Setting
    public int angle2 = 0;
}

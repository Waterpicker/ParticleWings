package org.waterpicker.particlewings;

import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.*;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WingElement extends PatternMatchingCommandElement {

    protected WingElement(@Nullable Text key) {
        super(key);
    }


    @Override
    protected Iterable<String> getChoices(CommandSource src) {
        if(src.hasPermission("particlewings.use.all")) {
            return Wings.getAvailableWings();
        } else {
            return Wings.getAvailableWings().stream().filter(wing -> src.hasPermission("particleswings.use." + wing)).collect(Collectors.toList());
        }
    }

    @Override
    protected Object getValue(String wing) throws IllegalArgumentException {
        return wing;
    }
}

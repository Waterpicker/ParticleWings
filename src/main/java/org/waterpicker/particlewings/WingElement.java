package org.waterpicker.particlewings;

import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.*;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WingElement extends CommandElement {
    protected WingElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Wing parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String wing = args.next();
        return src.hasPermission("particlewings.use.all") || src.hasPermission("particlewings.use." + wing) ? Wings.getWing(wing) : null;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        if(src.hasPermission("particlewings.use.all")) {
            return Lists.newArrayList(Wings.getAvailableWings());
        } else {
            return Wings.getAvailableWings().stream().filter(wing -> src.hasPermission("particlewings.use." + wing)).collect(Collectors.toList());
        }
    }
}






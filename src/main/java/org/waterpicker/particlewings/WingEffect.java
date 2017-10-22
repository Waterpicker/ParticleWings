package org.waterpicker.particlewings;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.matrix.Matrix4d;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector4d;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

class WingEffect implements Consumer<Task> {
    private Wing wing;
    private Player player;
    private boolean cancel = false;

    WingEffect(Wing wing, Player player) {
        this.wing = wing;
        this.player = player;
    }

    @Override
    public void accept(Task task) {
        if(cancel) {
            task.cancel();
            return;
        }

        wing.render(player.getWorld(), player.getTransform());
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void cancel() {
        cancel = true;
    }

    public void setWing(Wing wing) {
        this.wing = wing;
    }

    public Wing getWing() {
        return wing;
    }
}
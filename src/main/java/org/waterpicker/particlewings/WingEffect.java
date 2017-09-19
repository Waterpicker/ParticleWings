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
    private static Matrix4d right = Matrix4d.createRotation(Quaterniond.fromAxesAnglesDeg(0,135,0)).translate(Vector3d.RIGHT.mul(0.25).negate().add(0,1,-0.25));
    private static Matrix4d left = Matrix4d.createRotation(Quaterniond.fromAxesAnglesDeg(0, 45,0)).translate(Vector3d.RIGHT.mul(0.25).add(0,1,-0.25));

    private String wing;
    private Player player;
    private boolean cancel = false;

    WingEffect(String wing, Player player) {
        this.wing = wing;
        this.player = player;
    }

    @Override
    public void accept(Task task) {
        if(cancel) {
            task.cancel();
            return;
        }

        Texture texture = Wings.getWing(wing);
        Transform<World> transform = player.getTransform().setRotation(new Vector3d(0, player.getTransform().getYaw(), 0));

        texture.draw(player.getWorld(), right, transform.toMatrix());
        texture.draw(player.getWorld(), left, transform.toMatrix());
    }

    public void cancel() {
        cancel = true;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getWing() {
        return wing;
    }
}
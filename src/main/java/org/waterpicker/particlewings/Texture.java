package org.waterpicker.particlewings;

import com.flowpowered.math.matrix.Matrix4d;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector4d;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.Color;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class   Texture {
    private double height, width;
    private Multimap<ParticleEffect, Vector4d> data = ArrayListMultimap.create();

    public Texture(BufferedImage image) {
        float ratio = 1f/8f;
        height = image.getHeight()*ratio;
        width = image.getWidth()*ratio;

        ParticleEffect.Builder effect = ParticleEffect.builder().type(ParticleTypes.REDSTONE_DUST).velocity(Vector3d.ZERO);

        Multimap<Color, Vector4d> temp = ArrayListMultimap.create();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Optional<Color> color = getColor(image.getRGB(x,y));
                if(color.isPresent()) {
                    temp.put(color.get(), new Vector3d((image.getWidth()-1)-x, (image.getHeight()-1)-y, 0).mul(ratio).toVector4(1));
                }
            }
        }

        for(Color color : temp.keySet()) {
            data.putAll(effect.option(ParticleOptions.COLOR, color).build(), temp.get(color));
        }
    }

    public void draw(Viewer viewer, Matrix4d... matrix) {
        for (Map.Entry<ParticleEffect, Collection<Vector4d>> entry : data.asMap().entrySet()) {
            for (Vector4d vec : entry.getValue()) {
                for (Matrix4d m : matrix) {
                    vec = m.transform(vec);
                }

                viewer.spawnParticles(entry.getKey(), vec.toVector3());
            }
        }
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    private static Optional<Color> getColor(int pixel) {
        return Optional.of(new java.awt.Color(pixel, true)).filter(c -> c.getAlpha() != 0).map(Color::of);
    }
}

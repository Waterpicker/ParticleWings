package org.waterpicker.particlewings;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.matrix.Matrix4d;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.entity.Transform;
import org.waterpicker.particlefx.PngTextureComponent;
import org.waterpicker.particlefx.RotationUtil;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class Wing {
    private static Matrix4d right_translation = Matrix4d.createTranslation(Vector3d.RIGHT.mul(0.25).negate().add(0,0,-0.25));
    private static Matrix4d left_translation = Matrix4d.createTranslation(Vector3d.RIGHT.mul(0.25).add(0,0,-0.25));
    private Sequence sequence;

    private final Optional<PngTextureComponent> right;
    private final Optional<PngTextureComponent> left;
    private final String name;

    public Wing(String name, Sequence sequence, Optional<BufferedImage> left, Optional<BufferedImage> right) {
        this.name = name;
        this.sequence = sequence;
        this.right = right.map(image -> new PngTextureComponent(image, 0.125f));
        this.left = left.map(image -> new PngTextureComponent(image, 0.125f));
    }

    public void render(Viewer viewer, Transform transform) {
        Matrix4d matrix = transform.setRotation(new Vector3d(0, transform.getYaw(), 0)).toMatrix();

        int i = sequence.get();

        left.ifPresent(effect -> effect.render(viewer, RotationUtil.getYaw(i), left_translation, matrix));
        right.ifPresent(effect -> effect.render(viewer, RotationUtil.getYaw(180-i), right_translation, matrix));
    }

    @Override
    public String toString() {
        return name;
    }

    public void increment() {
        sequence.increment();
    }
}

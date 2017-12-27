package org.waterpicker.particlewings;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.matrix.Matrix4d;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.entity.Transform;
import org.waterpicker.particlefx.PngTextureComponent;
import org.waterpicker.particlefx.RotationUtil;

import java.awt.image.BufferedImage;

public class Wing {
    private static Matrix4d right_translation = Matrix4d.createTranslation(Vector3d.RIGHT.mul(0.25).negate().add(0,0,-0.25));
    private static Matrix4d left_translation = Matrix4d.createTranslation(Vector3d.RIGHT.mul(0.25).add(0,0,-0.25));
    private static Cycle cycle = new Cycle(45, 60);

    private final PngTextureComponent right_image;
    private final PngTextureComponent left_image;
    private final String name;

    public Wing(String name, BufferedImage left, BufferedImage right) {
        this.name = name;
        right_image = new PngTextureComponent(right, 0.125f);
        left_image = new PngTextureComponent(left, 0.125f);
    }

    public void render(Viewer viewer, Transform transform) {
        Matrix4d matrix = transform.setRotation(new Vector3d(0, transform.getYaw(), 0)).toMatrix();

        int i = cycle.get();

        left_image.render(viewer, RotationUtil.getYaw(i), left_translation, matrix);
        right_image.render(viewer, RotationUtil.getYaw(180-i), right_translation, matrix);
    }

    @Override
    public String toString() {
        return name;
    }

    public static void increment() {
        cycle.increment();
    }

}

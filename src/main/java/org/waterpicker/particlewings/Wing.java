package org.waterpicker.particlewings;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.matrix.Matrix4d;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.entity.Transform;

import java.awt.image.BufferedImage;

public class Wing {
    private static Matrix4d right_matrix = Matrix4d.createRotation(Quaterniond.fromAxesAnglesDeg(0,135,0)).translate(Vector3d.RIGHT.mul(0.25).negate().add(0,0,-0.25));
    private static Matrix4d left_matrix = Matrix4d.createRotation(Quaterniond.fromAxesAnglesDeg(0, 45,0)).translate(Vector3d.RIGHT.mul(0.25).add(0,0,-0.25));

    private final Texture right_image;
    private final Texture left_image;
    private final String name;

    public Wing(String name, BufferedImage left, BufferedImage right) {
        this.name = name;
        right_image = new Texture(right);
        left_image = new Texture(left);
    }

    public void render(Viewer viewer, Transform transform) {
        Matrix4d matrix = transform.setRotation(new Vector3d(0, transform.getYaw(), 0)).toMatrix();

        left_image.draw(viewer, left_matrix, matrix);
        right_image.draw(viewer, right_matrix, matrix);
    }

    @Override
    public String toString() {
        return name;
    }
}

package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.teamcode.matrix.Mat4;

public class SampleDetection {
    /**
     * The camera space transform of the sample.
     */
    private Mat4 transform;

    /**
     * The color of the detected sample.
     */
    private Color color;

    /**
     * Constructs a SampleDetection.
     *
     * @param transform the camera space transform of the sample.
     * @param color the color of the detected sample.
     */
    public SampleDetection(Mat4 transform, Color color) {
        this.transform = transform;
        this.color = color;
    }

    /**
     * Gets the camera space transform of the sample.
     *
     * @return The camera space transform of the sample.
     */
    public Mat4 getTransform() {
        return transform;
    }

    /**
     * Gets the color of the detected sample.
     *
     * @return The color of the detected sample.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Describes a color of a sample.
     * Samples may be alliance specific (blue or red) or alliance neutral (yellow).
     */
    public enum Color {
        BLUE(237.0 / 360),
        RED(0),
        NEUTRAL(60.0 / 360);

        private double hue;

        Color(double hue) {
            this.hue = hue;
        }

        public double getHue() {
            return hue;
        }
    }
}

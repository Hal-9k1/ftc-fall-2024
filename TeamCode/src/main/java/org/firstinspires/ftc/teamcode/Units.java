package org.firstinspires.ftc.teamcode;

public class Units {
    public enum Distance {
        MM(1000),
        CM(100),
        IN(100 / 2.54),
        TILE(100 / 2.54 / 24),
        FT(100 / 2.54 / 12),
        M(1);

        private final double unitsPerMeter;

        Distance(double unitsPerMeter) {
            this.unitsPerMeter = unitsPerMeter;
        }
    }
    public enum Angle {
        DEG(180 / Math.PI),
        REV(1 / (2 * Math.PI)),
        RAD(1);

        private final double unitsPerRadian;

        Angle(double unitsPerRadian) {
            this.unitsPerRadian = unitsPerRadian;
        }
    }

    public static double convert(double value, Distance fromUnit, Distance toUnit) {
        return value * toUnit.unitsPerMeter / fromUnit.unitsPerMeter;
    }
    public static double convert(double value, Angle fromUnit, Angle toUnit) {
        return value * toUnit.unitsPerRadian / fromUnit.unitsPerRadian;
    }
}

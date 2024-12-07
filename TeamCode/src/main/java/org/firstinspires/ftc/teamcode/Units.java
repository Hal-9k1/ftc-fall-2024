package org.firstinspires.ftc.teamcode;

import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

/**
 * Utility class for performing unit conversions.
 */
public class Units {
    /**
     * Represents a unit of distance measurement.
     */
    public static enum Distance {
        /**
         * Millimeters.
         */
        MM(1000),
        /**
         * Centimeters.
         */
        CM(100),
        /**
         * Inches.
         */
        IN(100 / 2.54),
        /**
         * Field tiles.
         * Equivalent to 2 feet.
         */
        TILE(100 / 2.54 / 24),
        /**
         * Feet.
         */
        FT(100 / 2.54 / 12),
        /**
         * Meters.
         */
        M(1);

        /**
         * The number of this unit that is equivalent to one meter.
         */
        private final double unitsPerMeter;

        /**
         * Constructs a Distance enum member.
         * @param unitsPerMeter - the number of this unit that is equivalent to one meter.
         */
        Distance(double unitsPerMeter) {
            this.unitsPerMeter = unitsPerMeter;
        }
    }
    /**
     * Represents a unit of angle measurement.
     */
    public static enum Angle {
        /**
         * Degrees.
         */
        DEG(180 / Math.PI),
        /**
         * Revolutions.
         * Equivalent to 2 PI radians or 360 degrees.
         */
        REV(1 / (2 * Math.PI)),
        /**
         * Radians.
         */
        RAD(1);

        /**
         * The number of this unit that is equivalent to one radian.
         */
        private final double unitsPerRadian;

        /**
         * Constructs an Angle enum member.
         * @param unitsPerRadian - the number of this unit that is equivalent to one radian.
         */
        Angle(double unitsPerRadian) {
            this.unitsPerRadian = unitsPerRadian;
        }
    }

    public static enum Time {
        SEC(1),
        NANO(Math.pow(10, 9)),
        MSEC(1000);

        /**
         * The number of this unit that is equivalent to one second.
         */
        private final double unitsPerSecond;

        time(double unitsPerSecond) {
            this.unitsPerSecond = unitsPerSecond;
        }
    }

    /**
     * Converts between distance units.
     * @param value - the value in fromUnit units to convert.
     * @param fromUnit - the unit to convert from.
     * @param toUnit - the unit to convert to.
     * @return the given value, converted from fromUnits to toUnits.
     */
    public static double convert(double value, Distance fromUnit, Distance toUnit) {
        return value * toUnit.unitsPerMeter / fromUnit.unitsPerMeter;
    }
    /**
     * Converts between angle units.
     * @param value - the value in fromUnit units to convert.
     * @param fromUnit - the unit to convert from.
     * @param toUnit - the unit to convert to.
     * @return the given value, converted from fromUnits to toUnits.
     */
    public static double convert(double value, Angle fromUnit, Angle toUnit) {
        return value * toUnit.unitsPerRadian / fromUnit.unitsPerRadian;
    }

    public static double convert(double value, Time fromUnit, Time toUnit) {
        return value * toUnit.unitsPerSecond / fromUnit.unitsPerSecond;
    }
}

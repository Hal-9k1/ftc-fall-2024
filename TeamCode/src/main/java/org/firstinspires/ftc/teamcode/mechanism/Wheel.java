package org.firstinspires.ftc.teamcode.mechanism;

/**
 * Represents a wheel directly or indirectly driven by a motor that can calculate translation using
 * the motor's position and the wheel's radius.
 */
public class Wheel {
    /**
     * The motor driving the wheel.
     */
    private final DcMotor motor;
    /**
     * The radius of the wheel in a unit chosen by the owner.
     */
    private final double radius;

    /**
     * Constructs a Wheel.
     * @param motor The motor driving the wheel.
     * @param radius The radius of the wheel. No assumptions are made about the unit.
     */
    public Wheel(DcMotor motor, double radius) {
        this.motor = motor;
        this.radius = radius;
        this.ticksPerRot = ticksPerRot;
    }

    /**
     * Returns the linear distance traveled by the edge of the wheel.
     * Since no interface is exposed to zero the distance (other than retaining a reference to the
     * motor and zeroing its encoder) values returned by this method are best used relatively rather
     * than absolutely, i.e.:
     * <pre><code>
     * startDistance = wheel.getDistance();
     * // Later...
     * distanceTraveled = wheel.getDistance() - startDistance;
     * </code></pre>
     * The unit of the return value is in the same unit as the wheel radius passed to the
     * constructor.
     */
    public double getDistance() {
        double revs = (double)motor.getCurrentPosition() / motor.getMotorType().getTicksPerRev();
        // Does MotorConfigurationType.getGearing hold any interesting info or does it just hold a
        // value we give it?
        double angle = revs * 2 * Math.PI;
        return angle * radius;
    }

    /**
     * Sets the power of the underlying motor.
     * The allowed range of powers is [-1.0, 1.0] where positive values indicate forward movement.
     */
    public void setVelocity(double velocity) {
        motor.setPower(velocity);
    }
}

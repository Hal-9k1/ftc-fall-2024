package org.firstinspires.ftc.teamcode.task;

/**
 * Controls the intake in teleop.
 */
public class IntakeTeleopTask implements Task {
    /**
     * Whether the intake should run until a sample has been predicted to be acquired.
     * Depending on the intake implementation, this may be a simple delay or rely on a sensor to
     * detect a sample.
     */
    private final boolean acquire;

    /**
     * Whether the intake should run until a sample has been predicted to be ejected.
     * This runs on a simple delay.
     */
    private final boolean timedEject;

    /**
     * A power to directly give to the intake actuator.
     */
    private final double intakePower;

    /**
     * Constructs an IntakeTeleopTask.
     *
     * @param acquire - whether the intake should run until a sample has been predicted to be
     * acquired. Incompatible with timedEject.
     * @param timedEject - whether the intake should run until a sample has been predicted to be
     * ejected. Incompatible with acquire.
     * @param intakePower - a power to directly give to the intake actuator. Ignored if acquire or
     * timedEject are set.
     */
    public IntakeTeleopTask(boolean acquire, boolean timedEject, double intakePower) {
        this.acquire = acquire;
        this.timedEject = timedEject;
        this.intakePower = intakePower;
        if (acquire && timedEject) {
            throw new IllegalArgumentException(
                "Cannot direct the intake to simultaneously acquire a sample and eject one."
            );
        }
    }

    /**
     * Returns whether the intake should run until a sample has been predicted to be acquired.
     * Depending on the intake implementation, this may be a simple delay or rely on a sensor to
     * detect a sample.
     *
     * @return whether the intake should run until a sample has been predicted to be acquired.
     */
    public final boolean getAcquire() {
        return acquire;
    }

    /**
     * Returns whether the intake should run until a sample has been predicted to be ejected.
     * This runs on a simple delay.
     *
     * @return whether the intake should run until a sample has been predicted to be ejected.
     */
    public final boolean getTimedEject() {
        return timedEject;
    }

    /**
     * Returns a power to directly give to the intake actuator.
     *
     * @return a power to directly give to the intake actuator, which will be in the range [-1, 1].
     */
    public final double getIntakePower() {
        return intakePower;
    }
}

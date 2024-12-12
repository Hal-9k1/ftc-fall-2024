package org.firstinspires.ftc.teamcode.task;

/**
 * Controls the sample intake.
 */
public class IntakeTask {
    /**
     * Whether the intake should run until a sample has been predicted to be acquired.
     * Depending on the intake implementation, this may be a simple delay or rely on a sensor to
     * detect a sample.
     */
    public final boolean acquire;
    /**
     * Whether the intake should run until a sample has been predicted to be ejected.
     */
    public final boolean eject;

    /**
     * Constructs an IntakeTask.
     * @param acquire - whether the intake should run until a sample has been predicted to be
     * acquired. Incompatible with eject.
     * @param eject - whether the intake should run until a sample has been predicted to be
     * ejected. Incompatible with acquire.
     */
    public IntakeTask(boolean acquire, boolean eject) {
        this.acquire = acquire;
        this.eject = eject;
        if (acquire && eject) {
            throw new IllegalArgumentException(
                "Cannot direct the intake to simultaneously acquire a sample and eject one."
            );
        }
    }
}

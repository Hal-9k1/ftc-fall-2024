package org.firstinspires.ftc.teamcode.task;

public class IntakeTask {
    public final boolean acquire;
    public final boolean eject;

    public IntakeTask(boolean acquire, boolean eject) {
        this.acquire = acquire;
        this.eject = eject;
        if (acquire && eject) {
            throw new IllegalArgumentException(
                "Cannot direct the intake to simultaneously acquire a sample and eject one."
            );
    }
}

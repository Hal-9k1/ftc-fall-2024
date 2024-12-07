package org.firstinspires.ftc.teamcode.task;

public class IntakeTeleopTask {
    public final boolean acquire;
    public final boolean timedEject;
    public final double intakePower;

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
}

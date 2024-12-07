package org.firstinspires.ftc.teamcode.task;

public class IntakeTask {
    public final boolean acquire;
    public final boolean timedEject;
    public final double intakePower;

    public IntakeTask(boolean acquire, boolean timedEject, double intakePower) {
        this.acquire = acquire;
        this.timedEject = timedEject;
        this.intakePower = intakePower;
    }
}

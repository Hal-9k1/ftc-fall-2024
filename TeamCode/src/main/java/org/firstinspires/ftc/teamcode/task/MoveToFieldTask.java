package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.matrix.Mat3;

public final class MoveToFieldTask implements Task {
    private Mat3 transform;

    public MoveToFieldTask(Mat3 transform) {
        this.transform = transform;
    }

    public Mat3 getGoalTransform() {
        return transform;
    }
}

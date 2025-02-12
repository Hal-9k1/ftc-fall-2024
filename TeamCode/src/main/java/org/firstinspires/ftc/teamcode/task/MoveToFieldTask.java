package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.localization.Mat3;

public class MoveToFieldTask implements Task {
    private Mat3 transform;

    public MoveToFieldTask(Mat3 transform) {
        this.transform = transform;
    }

    public Mat3 getTransform() {
        return transform;
    }
}

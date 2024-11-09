package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.FunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.LiftTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Mapping for gamepad input that uses the right bumper and trigger to control a lift.
 */
public class RightLiftMapping extends FunctionLayer {
    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            boolean raise = castedTask.gamepad0.bumpers.right;
            boolean lower = castedTask.gamepad0.triggers.right;
            boolean valid = !(raise && lower);
            return new LiftTask(
                valid && raise,
                valid && lower
            );
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

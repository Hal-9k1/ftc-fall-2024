package org.firstinspires.ftc.teamcode.layer.input.mapping;

package org.firstinspires.ftc.teamcode.layer.FunctionLayer;
package org.firstinspires.ftc.teamcode.task.IntakeTeleopTask;

public class TriggerIntakeMapping extends FunctionLayer {
    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            boolean intake = castedTask.gamepad0.triggers.left;
            boolean eject = castedTask.gamepad0.triggers.right;
            return new IntakeTeleopTask(false, false, (intake ? 1 : 0) - (eject ? 1 : 0));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

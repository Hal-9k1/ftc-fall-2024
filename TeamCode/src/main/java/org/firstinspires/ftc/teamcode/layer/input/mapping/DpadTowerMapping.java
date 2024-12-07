package org.firstinspires.ftc.teamcode.layer.input.mapping;

public class DpadTowerMapping extends FunctionLayer {
    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            return new TowerTeleopTask((castedTask.dpad.up ? 1 : 0) - (castedTask.dpad.down ? 1 : 0));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

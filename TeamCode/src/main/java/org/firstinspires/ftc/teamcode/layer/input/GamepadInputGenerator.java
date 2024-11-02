package org.firstinspires.ftc.teamcode.layer.input;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

public class GamepadInputGenerator extends InputGenerator {
    private Gamepad gamepad;

    public void setup(LayerSetupInfo setupInfo) {
        // TODO: add gamepad property to LayerSetupInfo and shove it in this.gamepad
    }

    public Task update() {
        return new GamepadInputTask(
            gamepad.left_stick_x,
            gamepad.left_stick_y,
            gamepad.left_bumper,
            gamepad.left_trigger,
            gamepad.right_stick_x,
            gamepad.right_stick_y,
            gamepad.right_bumper,
            gamepad.right_trigger,
            gamepad.dpad_up,
            gamepad.dpad_right,
            gamepad.dpad_down,
            gamepad.dpad_left,
            gamepad.a,
            gamepad.b,
            gamepad.x,
            gamepad.y
        );
    }
}

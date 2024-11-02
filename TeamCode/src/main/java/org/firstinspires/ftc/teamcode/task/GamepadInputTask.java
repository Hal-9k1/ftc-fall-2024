package org.firstinspires.ftc.teamcode.task;

public class GamepadInputTask implements Task {
    public static class Joystick {
        public final float x;
        public final float y;
        private Joystick(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
    public static class Joysticks {
        public final Joystick left;
        public final Joystick right;
        private Joysticks(Joystick left, Joystick right) {
            this.left = left;
            this.right = right;
        }
    }
    public static class ButtonPair {
        public final boolean left;
        public final boolean right;
        private ButtonPair(boolean left, boolean right) {
            this.left = left;
            this.right = right;
        }
    }
    public static class DirectionPad {
        public final boolean up;
        public final boolean right;
        public final boolean down;
        public final boolean left;
        private DirectionPad(boolean up, boolean right, boolean down, boolean left) {
            this.up = up;
            this.right = right;
            this.down = down;
            this.left = left;
        }
    }
    public static class Buttons {
        public final boolean a;
        public final boolean b;
        public final boolean x;
        public final boolean y;
        private Buttons(boolean a, boolean b, boolean x, boolean y) {
            this.a = a;
            this.b = b;
            this.x = x;
            this.y = y;
        }
    }

    private static final float TRIGGER_MIN = 0.3f;

    public final Joysticks joysticks;
    public final ButtonPair bumpers;
    public final ButtonPair triggers;
    public final DirectionPad dpad;
    public final Buttons buttons;

    public GamepadInputTask(
        float joystickLeftX,
        float joystickLeftY,
        boolean bumperLeft,
        float triggerLeft,
        float joystickRightX,
        float joystickRightY,
        boolean bumperRight,
        float triggerRight, 
        boolean dpadUp,
        boolean dpadRight,
        boolean dpadDown,
        boolean dpadLeft,
        boolean buttonA,
        boolean buttonB,
        boolean buttonX,
        boolean buttonY
    ) {
        joysticks = new Joysticks(
            new Joystick(joystickLeftX, joystickLeftY),
            new Joystick(joystickRightX, joystickRightY)
        );
        bumpers = new ButtonPair(bumperLeft, bumperRight);
        triggers = new ButtonPair(triggerLeft >= TRIGGER_MIN, triggerRight >= TRIGGER_MIN);
        dpad = new DirectionPad(dpadUp, dpadRight, dpadDown, dpadLeft);
        buttons = new Buttons(buttonA, buttonB, buttonX, buttonY);
    }
}

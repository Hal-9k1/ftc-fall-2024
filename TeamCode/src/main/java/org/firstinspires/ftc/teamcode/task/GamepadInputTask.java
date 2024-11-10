package org.firstinspires.ftc.teamcode.task;

public class GamepadInputTask implements Task {
    /**
     * Carries information about the axes of a joystick on a gamepad.
     */
    public static class Joystick {
        /**
         * The horizontal axis of the joystick.
         * Positive values corrospond to the rightward direction.
         */
        public final float x;
        /**
         * The vertical axis of the joystick.
         * Positive values corrospond to the upward direction.
         */
        public final float y;

        private Joystick(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Carries information about a the pair of joysticks on a gamepad.
     */
    public static class Joysticks {
        /**
         * The left joystick.
         */
        public final Joystick left;
        /**
         * The right joystick.
         */
        public final Joystick right;

        private Joysticks(Joystick left, Joystick right) {
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Carries information about a pair of symmetrical buttons on a gamepad, such as the triggers or
     * bumpers.
     */
    public static class ButtonPair {
        /**
         * Whether the left button of the pair is pressed.
         */
        public final boolean left;
        /**
         * Whether the right button of the pair is pressed.
         */
        public final boolean right;

        private ButtonPair(boolean left, boolean right) {
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Caries information about a gamepad's directional pad (four buttons arranged to indicate
     * movement along x and y axes).
     */
    public static class DirectionalPad {
        /**
         * Whether the up button of the dpad is pressed.
         */
        public final boolean up;
        /**
         * Whether the right button of the dpad is pressed.
         */
        public final boolean right;
        /**
         * Whether the down button of the dpad is pressed.
         */
        public final boolean down;
        /**
         * Whether the left button of the dpad is pressed.
         */
        public final boolean left;

        private DirectionalPad(boolean up, boolean right, boolean down, boolean left) {
            this.up = up;
            this.right = right;
            this.down = down;
            this.left = left;
        }
    }

    /**
     * Carries information about miscellanous buttons on a gamepad.
     */
    public static class Buttons {
        /**
         * Whether the A gamepad button is pressed.
         */
        public final boolean a;
        /**
         * Whether the B gamepad button is pressed.
         */
        public final boolean b;
        /**
         * Whether the X gamepad button is pressed.
         */
        public final boolean x;
        /**
         * Whether the Y gamepad button is pressed.
         */
        public final boolean y;

        private Buttons(boolean a, boolean b, boolean x, boolean y) {
            this.a = a;
            this.b = b;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Carries information about a single connected gamepad.
     */
    public static class GamepadInput {
        /**
         * The pair of joysticks on the gamepad.
         */
        public final Joysticks joysticks;
        /**
         * The pair of bumpers on the gamepad.
         */
        public final ButtonPair bumpers;
        /**
         * The pair of triggers on the gamepad.
         */
        public final ButtonPair triggers;
        /**
         * The directional pad on the gamepad.
         */
        public final DirectionalPad dpad;
        /**
         * The miscellanious buttons on the gamepad.
         */
        public final Buttons buttons;

        private static final float TRIGGER_MIN = 0.3f;

        public GamepadInput(
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
            dpad = new DirectionalPad(dpadUp, dpadRight, dpadDown, dpadLeft);
            buttons = new Buttons(buttonA, buttonB, buttonX, buttonY);
        }
    }


    /**
     * Input captured from the gamepad connected to the first slot, or null if none is connected.
     */
    public final GamepadInput gamepad0;
    /**
     * Input captured from the gamepad connected to the second slot, or null if none is connected.
     */
    public final GamepadInput gamepad1;

    /**
     * Constructs a GamepadInputTask with given info about gamepad inputs.
     * @param gamepad0 Input captured from the gamepad connected to the first slot, or null if none
     * is connected.
     * @param gamepad1 Input captured from the gamepad connected to the second slot, or null if none
     * is connected.
     */
    public GamepadInputTask(GamepadInput gamepad0, GamepadInput gamepad1) {
        this.gamepad0 = gamepad0;
        this.gamepad1 = gamepad1;
    }
}

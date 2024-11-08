package org.firstinspires.ftc.teamcode.layer.drive;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.mechanism.Wheel;
import org.firstinspires.ftc.teamcode.task.AxialMovementTask;
import org.firstinspires.ftc.teamcode.task.TankDriveTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.Units;

/**
 * Drive layer for a robot using four properly-oriented mecanum wheels.
 */
public class MecanumDrive implements Layer {
    /**
     * Represents a property held by each wheel.
     */
    private static class WheelProperty<T> {
        /**
         * Denotes a wheel.
         */
        private static enum WheelKey {
            LEFT_FRONT,
            RIGHT_FRONT,
            LEFT_BACK,
            RIGHT_BACK
        }
        /**
         * A never-to-be-changed null WheelProperty used to minimize the number of objects created
         * by populate.
         */
        private static final WheelProperty<Object> NULL_PROP = new WheelProperty<>();
        /**
         * The value of the property for the left front wheel.
         */
        public T leftFront;
        /**
         * The value of the property for the right front wheel.
         */
        public T rightFront;
        /**
         * The value of the property for the left back wheel.
         */
        public T leftBack;
        /**
         * The value of the property for the right back wheel.
         */
        public T rightBack;
        /**
         * Default-constructs a WheelProperty, setting all values to null.
         */
        public WheelProperty() {
            leftFront = null;
            rightFront = null;
            leftBack = null;
            rightBack = null;
        }
        /**
         * Initializes a WheelProperty with values for each wheel..
         * @param leftFront the value of the property for the left front wheel.
         * @param rightFront the value of the property for the right front wheel.
         * @param leftBack the value of the property for the left back wheel.
         * @param rightBack the value of the property for the right back wheel.
         */
        public WheelProperty(T leftFront, T rightFront, T leftBack, T rightBack) {
            this.leftFront = leftFront;
            this.rightFront = rightFront;
            this.leftBack = leftBack;
            this.rightBack = rightBack;
        }
        /**
         * Creates a WheelProperty by applying a function to each wheel.
         */
        public static <R> WheelProperty<R> populate(Function<WheelKey, R> populator) {
            return NULL_PROP.map((key, _) -> populator.apply(key));
        }
        /**
         * Gets the value of the property for the given wheel.
         * @param key - the wheel whose value should be retrieved
         * @return The value of the property for the given wheel.
         */
        public T get(WheelKey key) {
            switch (key) {
                case WheelKey.LEFT_FRONT:
                    return leftFront;
                case WheelKey.RIGHT_FRONT:
                    return rightFront;
                case WheelKey.LEFT_BACK:
                    return leftBack;
                case WheelKey.RIGHT_BACK:
                    return rightBack;
            }
        }
        /**
         * Sets the value of the property for the given wheel.
         * @param key - the wheel whose value should be set
         * @param value - the value to set the property to
         */
        public void put(WheelKey key, T value) {
            switch (key) {
                case WheelKey.LEFT_FRONT:
                    leftFront = value;
                case WheelKey.RIGHT_FRONT:
                    rightFront = value;
                case WheelKey.LEFT_BACK:
                    leftBack = value;
                case WheelKey.RIGHT_BACK:
                    rightBack = value;
            }
        }
        /**
         * Calls a function on each of the left wheels' keys and values.
         * @param callback - the function to call
         */
        public void leftForEach(BiConsumer<WheelKey, T> callback) {
            callback.accept(WheelKey.LEFT_FRONT, leftFront);
            callback.accept(WheelKey.LEFT_BACK, leftBack);
        }
        /**
         * Calls a function on each of the right wheels' keys and values.
         * @param callback - the function to call
         */
        public void rightForEach(BiConsumer<WheelKey, T> callback) {
            callback.accept(WheelKey.RIGHT_FRONT, rightFront);
            callback.accept(WheelKey.RIGHT_BACK, rightBack);
        }
        /**
         * Calls a function on each of the wheels' keys and values.
         * @param callback - the function to call
         */
        public void forEach(BiConsumer<WheelKey, T> callback) {
            leftForEach(callback);
            rightForEach(callback);
        }
        /**
         * Returns a new WheelProperty populated with the results of calling a mapper function with
         * the keys and values of this property.
         * @param mapper - the function used to map old values to new values
         */
        public <R> WheelProperty<R> map(BiFunction<WheelKey, T, R> mapper) {
            WheelProperty<R> mapped = new WheelProperty<>();
            forEach((key, old) -> mapped.put(key, mapper.apply(key, old)));
            return mapped;
        }
    }
    /**
     * Name of the drive motors in the robot configuration.
     */
    private static final WheelProperty<String> DRIVE_MOTOR_NAMES = new WheelProperty<>(
        "left-front-drive-motor",
        "right-front-drive-motor",
        "left-back-drive-motor",
        "right-back-drive-motor"
    );
    /**
     * The radius of the drive wheels in meters.
     */
    private static final double WHEEL_RADIUS = 0.42;
    /**
     * The effective gear ratio of the wheels to the motor drive shafts.
     * Expressed as wheelTeeth / hubGearTeeth, ignoring all intermediate meshing gears as they
     * should cancel out. Differently teethed gears driven by the same axle require more
     * consideration.
     */
    private static final WheelProperty<Double> GEAR_RATIO = new WheelProperty.populate((_) -> 1.0);
    /**
     * Half the distance between the driving wheels in meters.
     */
    private static final double WHEEL_SPAN_RADIUS = 0.84;
    /**
     * Unitless, experimentally determined constant (ew) measuring lack of friction.
     * Measures lack of friction between wheels and floor material. Goal delta distances are directly
     * proportional to this.
     */
    private static final double SLIPPING_CONSTANT = 1;

    /**
     * The robot's wheels.
     */
    private final WheelProperty<Wheel> wheels;
    /**
     * The position of the wheels at the start of the currently executing task, in meters.
     */
    private final WheelProperty<Double> wheelStartPos;
    /**
     * The required delta position of the wheels to complete the currently executing task, in
     * meters.
     */
    private final WheelProperty<Double> wheelGoalDeltas;
    /**
     * Whether the currently executing task has completed.
     */
    private boolean currentTaskDone;

    @Override
    public void setup(LayerSetupInfo initInfo) {
        wheels = DRIVE_MOTOR_NAMES.map((key, motorName) -> new Wheel(
            initInfo.getHardwareMap().get(DcMotor.class, motorName),
            WHEEL_RADIUS
        ));
        wheelStartPos = WheelProperty.populate((_) -> 0);
        wheelGoalDeltas = WheelProperty.populate((_) -> 0);
        currentTaskDone = true;
    }

    @Override
    public boolean isTaskDone() {
        return currentTaskDone;
    }

    @Override
    public Task update() {
        WheelProperty<Double> deltas = wheels.map((key, wheel) -> 
            wheel.getDistance() - wheelStartPos.get(key)
        );
        WheelProperty<Boolean> deltaSignsMatch = deltas.map((key, delta) ->
            (delta < 0) == (wheelGoalDeltas.get(key) < 0)
        );
        WheelProperty<Boolean> goalDeltaExceeded = deltas.map((key, delta) ->
            Math.abs(delta) >= Math.abs(wheelGoalDeltas.get(key))
        );
        WheelProperty<Boolean> wheelDone = wheelGoalDeltas.map((key, goalDelta) ->
            (deltaSignsMatch.get(key) && goalDeltaExceeded.get(key)) || goalDelta == 0
        );

        boolean isTeleopTask = leftGoalDelta == 0 && rightGoalDelta == 0;
        currentTaskDone = true;
        wheelDone.forEach((_, done) -> currentTaskDone = done && currentTaskDone);
        if (currentTaskDone && !isTeleopTask) {
            wheels.forEach((_, wheel) -> wheel.setVelocity(0));
        }
        // Adaptive velocity control goes here.
        return null;
    }

    @Override
    public void acceptTask(Task task) {
        currentTaskDone = false;
        wheels.forEach((key, wheel) -> wheelStartPos.put(key, wheel.getDistance()));
        double deltaFac = GEAR_RATIO * SLIPPING_CONSTANT;
        if (task instanceof AxialMovementTask) {
            AxialMovementTask castedTask = (AxialMovementTask)task;
            GEAR_RATIO.forEach((key, gearRatio) ->
                wheelGoalDeltas.put(key, castedTask.distance * gearRatio * SLIPPING_CONSTANT)
            );
            wheels.forEach((key, wheel) ->
                wheel.setVelocity(Math.signum(wheelGoalDeltas.get(key))));
        } else if (task instanceof TurnTask) {
            TurnTask castedTask = (TurnTask)task;
            GEAR_RATIO.leftForEach((key, gearRatio) -> wheelGoalDeltas.put(
                key,
                -castedTask.angle * WHEEL_SPAN_RADIUS * gearRatio * SLIPPING_CONSTANT
            ));
            GEAR_RATIO.rightForEach((key, gearRatio) -> wheelGoalDeltas.put(
                key,
                castedTask.angle * WHEEL_SPAN_RADIUS * gearRatio * SLIPPING_CONSTANT
            ));
            wheels.forEach((key, wheel) ->
                wheel.setVelocity(Math.signum(wheelGoalDeltas.get(key))));
        } else if (task instanceof TankDriveTask) {
            // Teleop, set deltas to 0 to pretend we're done
            wheelGoalDeltas.forEach((key, _) -> wheelGoalDeltas.put(0));
            TankDriveTask castedTask = (TankDriveTask)task;
            double maxAbsPower = Math.max(
                Math.max(Math.abs(castedTask.left), Math.abs(castedTask.right)),
                1 // Clamp to 1 to prevent upscaling
            );
            wheels.leftForEach((_, wheel) -> wheel.setVelocity(castedTask.left / maxAbsPower));
            wheels.rightForEach((_, wheel) -> wheel.setVelocity(castedTask.right / maxAbsPower));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

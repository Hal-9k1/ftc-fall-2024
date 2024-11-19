package org.firstinspires.ftc.teamcode.layer.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.mechanism.Wheel;
import org.firstinspires.ftc.teamcode.task.AxialMovementTask;
import org.firstinspires.ftc.teamcode.task.LinearMovementTask;
import org.firstinspires.ftc.teamcode.task.HolonomicDriveTask;
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
        public enum WheelKey {
            LEFT_FRONT(true, true),
            RIGHT_FRONT(false, true),
            LEFT_BACK(true, false),
            RIGHT_BACK(false, false);

            /**
             * Whether the wheel is on the left side of the robot.
             */
            public final boolean isLeft;
            /**
             * Whether the wheel is in the front of the robot.
             */
            public final boolean isFront;

            WheelKey(boolean isLeft, boolean isFront) {
                this.isLeft = isLeft;
                this.isFront = isFront;
            }
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
            return NULL_PROP.map((key, _null) -> populator.apply(key));
        }
        /**
         * Gets the value of the property for the given wheel.
         * @param key - the wheel whose value should be retrieved
         * @return The value of the property for the given wheel.
         */
        public T get(WheelKey key) {
            switch (key) {
                case LEFT_FRONT:
                    return leftFront;
                case RIGHT_FRONT:
                    return rightFront;
                case LEFT_BACK:
                    return leftBack;
                case RIGHT_BACK:
                    return rightBack;
            }
            throw new IllegalArgumentException("Bad WheelKey to WheelProperty.get.");
        }
        /**
         * Sets the value of the property for the given wheel.
         * @param key - the wheel whose value should be set
         * @param value - the value to set the property to
         */
        public void put(WheelKey key, T value) {
            switch (key) {
                case LEFT_FRONT:
                    leftFront = value;
                case RIGHT_FRONT:
                    rightFront = value;
                case LEFT_BACK:
                    leftBack = value;
                case RIGHT_BACK:
                    rightBack = value;
            }
        }
        /**
         * Calls a function on each of the wheels' keys and values.
         * @param callback - the function to call
         */
        public void forEach(BiConsumer<WheelKey, T> callback) {
            callback.accept(WheelKey.LEFT_FRONT, leftFront);
            callback.accept(WheelKey.RIGHT_FRONT, rightFront);
            callback.accept(WheelKey.LEFT_BACK, leftBack);
            callback.accept(WheelKey.RIGHT_BACK, rightBack);
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
        /**
         * Returns whether a predicate tests true on each of the wheels' keys and values.
         * @param predicate - the predicate to test
         * @return whether the predicate tests true on each of the wheels' keys and values
         */
        public boolean all(BiPredicate<WheelKey, T> predicate) {
            return predicate.test(WheelKey.LEFT_FRONT, leftFront)
                && predicate.test(WheelKey.RIGHT_FRONT, rightFront)
                && predicate.test(WheelKey.LEFT_BACK, leftBack)
                && predicate.test(WheelKey.RIGHT_BACK, rightBack);
        }
    }
    /**
     * Name of the drive motors in the robot configuration.
     */
    private static final WheelProperty<String> DRIVE_MOTOR_NAMES = new WheelProperty<>(
        "left_front_drive",
        "right_front_drive",
        "left_back_drive",
        "right_back_drive"
    );
    /**
     * The radius of the drive wheels in meters.
     * The current value is measured from the edge, including rollers.
     */
    private static final double WHEEL_RADIUS = Units.convert(4.25, Units.Distance.CM, Units.Distance.M);
    /**
     * The effective gear ratio of the wheels to the motor drive shafts.
     * Expressed as wheelTeeth / hubGearTeeth, ignoring all intermediate meshing gears as they
     * should cancel out. Differently teethed gears driven by the same axle require more
     * consideration.
     * NOTE: The ticksPerRev read from the MotorConfigurationTypes is 28 * 20, seemingly including
     * the 20:1 gearboxes we added. Must investigate this.
     */
    private static final WheelProperty<Double> GEAR_RATIO = WheelProperty.populate((_key) -> 1.0);
    /**
     * Half the distance between the driving wheels in meters.
     */
    private static final double WHEEL_SPAN_RADIUS = Units.convert(15.0 / 2, Units.Distance.IN, Units.Distance.M);
    /**
     * Unitless, experimentally determined constant (ew) measuring lack of friction.
     * Measures lack of friction between wheels and floor material. Goal delta distances are directly
     * proportional to this.
     */
    private static final WheelProperty<Double> SLIPPING_CONSTANT = WheelProperty.populate(key -> {
        if (key.isFront) {
            return 2.4;
        } else {
            return 1.0;
        }
    });

    /**
     * The robot's wheels.
     */
    private WheelProperty<Wheel> wheels;
    /**
     * The position of the wheels at the start of the currently executing task, in meters.
     */
    private WheelProperty<Double> wheelStartPos;
    /**
     * The required delta position of the wheels to complete the currently executing task, in
     * meters.
     */
    private WheelProperty<Double> wheelGoalDeltas;
    /**
     * Whether the currently executing task has completed.
     */
    private boolean currentTaskDone;

    private Telemetry telemetry;

    @Override
    public void setup(LayerSetupInfo initInfo) {
        wheels = DRIVE_MOTOR_NAMES.map((key, motorName) -> {
            DcMotor motor = initInfo.getHardwareMap().get(DcMotor.class, motorName);
            motor.setDirection(DcMotorSimple.Direction.REVERSE);
            return new Wheel(
                motor,
                WHEEL_RADIUS
            );
        });
        wheelStartPos = WheelProperty.populate((_key) -> 0.0);
        wheelGoalDeltas = WheelProperty.populate((_key) -> 0.0);
        currentTaskDone = true;
        telemetry = initInfo.getTelemetry();
    }

    @Override
    public boolean isTaskDone() {
        return currentTaskDone;
    }

    @Override
    public Iterator<Task> update(Iterator<Task> completed) {
        WheelProperty<Double> deltas = WheelProperty.populate((key) ->
            wheels.get(key).getDistance() - wheelStartPos.get(key)
        );
        WheelProperty<Boolean> deltaSignsMatch = WheelProperty.populate((key) ->
            (deltas.get(key) < 0) == (wheelGoalDeltas.get(key) < 0)
        );
        WheelProperty<Boolean> goalDeltaExceeded = WheelProperty.populate((key) ->
            Math.abs(deltas.get(key)) >= Math.abs(wheelGoalDeltas.get(key))
        );
        WheelProperty<Boolean> wheelDone = WheelProperty.populate((key) ->
            (deltaSignsMatch.get(key) && goalDeltaExceeded.get(key))
                || wheelGoalDeltas.get(key) == 0
        );
        wheels.forEach((key, _wheel) -> {
            telemetry.addLine(key.name())
                .addData("delta", deltas.get(key))
                .addData("goalDelta", wheelGoalDeltas.get(key))
                .addData("done", wheelDone.get(key));
        });

        boolean isTeleopTask = wheelGoalDeltas.all((_key, goalDelta) -> goalDelta == 0);
        currentTaskDone = wheelDone.all((_key, done) -> done);
        if (currentTaskDone && !isTeleopTask) {
            wheels.forEach((_key, wheel) -> wheel.setVelocity(0));
        }
        return Collections.emptyIterator();
    }

    @Override
    public void acceptTask(Task task) {
        currentTaskDone = false;
        wheelStartPos = wheels.map((_key, wheel) -> wheel.getDistance());
        boolean isAuto;
        if (task instanceof AxialMovementTask) {
            isAuto = true;
            AxialMovementTask castedTask = (AxialMovementTask)task;
            wheelGoalDeltas = GEAR_RATIO.map((key, gearRatio) ->
                castedTask.distance * gearRatio * SLIPPING_CONSTANT.get(key)
            );
        } else if (task instanceof TurnTask) {
            isAuto = true;
            TurnTask castedTask = (TurnTask)task;
            wheelGoalDeltas = GEAR_RATIO.map((key, gearRatio) ->
                (key.isLeft ? -1 : 1) * castedTask.angle * WHEEL_SPAN_RADIUS * gearRatio
                    * SLIPPING_CONSTANT.get(key)
            );
        } else if (task instanceof LinearMovementTask) {
            isAuto = true;
            LinearMovementTask castedTask = (LinearMovementTask)task;
            wheelGoalDeltas = calculateAlyDeltas(castedTask.axial, castedTask.lateral, 0);
        } else if (task instanceof TankDriveTask) {
            isAuto = false;
            TankDriveTask castedTask = (TankDriveTask)task;
            normalizeVelocities(
                new WheelProperty<>(
                    castedTask.left,
                    castedTask.right,
                    castedTask.left,
                    castedTask.right
                ).map((key, velocity) -> velocity * SLIPPING_CONSTANT.get(key)),
                false
            ).forEach((key, velocity) -> wheels.get(key).setVelocity(velocity));
        } else if (task instanceof HolonomicDriveTask) {
            isAuto = false;
            HolonomicDriveTask castedTask = (HolonomicDriveTask)task;
            normalizeVelocities(
                calculateAlyDeltas(
                    castedTask.axial,
                    castedTask.lateral,
                    castedTask.yaw
                ).map((key, velocity) -> velocity * SLIPPING_CONSTANT.get(key)),
                false
            ).forEach((key, velocity) -> wheels.get(key).setVelocity(velocity));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
        if (isAuto) {
            normalizeVelocities(wheelGoalDeltas, true)
                .forEach((key, velocity) -> {
                    telemetry.addData("MecanumDrive " + key.name(), velocity);
                    wheels.get(key).setVelocity(velocity * 0.5);
                });
        } else {
            // Say teleop tasks are instantly done in isTaskDone
            wheelGoalDeltas = WheelProperty.populate((_key) -> 0.0);
        }
    }

    /**
     * Calculates motor deltas from axial, lateral, and yaw given in arbitrary units.
     * @param axial - axial value, positive forward
     * @param lateral - lateral value, positive right
     * @param yaw - yaw value, positive counterclockwise
     * @return motor deltas calculated for each wheel.
     */
    private WheelProperty<Double> calculateAlyDeltas(double axial, double lateral, double yaw) {
        return new WheelProperty<>(
            axial - lateral - yaw,
            axial + lateral + yaw,
            axial + lateral - yaw,
            axial - lateral + yaw
        );
    }

    /**
     * Scales velocities so the maximum absolute value is no more than 1.0 -- appropriate
     * for use as motor velocities.
     * @param velocities - the velocities to scale down
     * @param scaleUp - whether velocities may be scaled upwards. Should be false when handling user
     * input so drivers may be gentle.
     * @return the scaled velocities.
     */
    private WheelProperty<Double> normalizeVelocities(WheelProperty<Double> velocities, boolean scaleUp) {
        WheelProperty<Double> absolute = velocities.map((_key, velocity) -> Math.abs(velocity));
        double maxAbsVelocity = Math.max(
            Math.max(
                Math.max(
                    absolute.leftFront,
                    absolute.rightFront
                ),
                Math.max(
                    absolute.leftBack,
                    absolute.rightBack
                )
            ),
            scaleUp ? Double.MIN_VALUE : 1.0 // Clamp to 1 when scaleUp is false to prevent upscaling
        );
        return velocities.map((_key, velocity) -> velocity / maxAbsVelocity);
    }
}

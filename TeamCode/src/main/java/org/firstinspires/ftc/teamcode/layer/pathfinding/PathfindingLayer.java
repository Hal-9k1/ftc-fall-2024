package org.firstinspires.ftc.teamcode.layer.pathfinding;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.localization.Mat3;
import org.firstinspires.ftc.teamcode.localization.Vec2;
import org.firstinspires.ftc.teamcode.task.Task;

import java.util.ArrayList;
import java.util.Iterator;

public class PathfindingLayer extends Layer {
	private static final double TARGET_ANGLE_COEFF = 0.5;
	private static final double CLEARENCE_COEFF = 1.2;
	private static final double SPEED_COEFF = 0.5;
	private static final double SIGMA = 1;
	private static final double SEARCH_TIME_INCREMENT = 0.25;
    private static final double TARGET_ANGLE_SMOOTHING_C = 1000;
    private static final double TARGET_ANGLE_SMOOTHING_K = 1;
    private static final double CLEARENCE_STEP = 0.05;

    private Mat3 goal;
    private List<Obstacle> obstacles;

    public PathfindingLayer() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        obstacles = new ArrayList<>();
    }

    @Override
    public boolean isTaskDone() {

    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {

    }

    @Override
    public void acceptTask(Task task) {

    }

    private double evaluateTrajectory(Trajectory t) {
		double weightedTargetAngle = evaluateTargetAngle(t) * a;
		double weightedClearance = evaluateClearence(t) * b;
		double weightedSpeed = evaluateSpeed(t) * g;
		return SIGMA * (weightedTargetAngle + weightedClearance + weightedSpeed);
    }
	

	
    private double evaluateTargetAngle(Trajectory t) {
        Mat3 finalTransform = getTrajectoryTransform(t, 1);
        Vec2 finalDirection = finalTransform.getDirection();
        Vec2 finalDelta = finalTransform.getTranslation().mul(-1).add(goal.getTranslation());
        double angle = finalDirection.angleWith(finalDelta);
		return TARGET_ANGLE_SMOOTHING_C / (angle + TARGET_ANGLE_SMOOTHING_K);
    }

    private double evaluateClearence(Trajectory t) {
        double minClearence = Double.NEGATIVE_INFINITY;
		for (double frac = 0; frac < 1; frac += CLEARENCE_STEP) {
            Vec2 translation = getTrajectoryTransform(t, frac).getTranslation();
            for (Obstacle obstacle : obstacles) {
                double clearenceToObstacle = obstacle.getDistanceTo(translation);
                if (minClearence > clearenceToObstacle) {
                    minClearence = clearenceToObstacle;
                }
            }
        }
        return minClearence;
    }

    private double evaluateSpeed(Trajectory t) {
		return t.getTrajectoryVelocity().getTranslation().len();  
    }

    private boolean checkDynamicWindow(Trajectory t) {
		for (double frac = 0; frac < 1; frac += CLEARENCE_STEP) {
            Vec2 translation = getTrajectoryTransform(t, frac).getTranslation();
            for (Obstacle obstacle : obstacles) {
                double clearenceToObstacle = obstacle.getDistanceTo(translation);
                if (clearenceToObstacle < 0) {
                    return false;
                }
            }
        }
        return Math.abs(t.getAxial()) + Math.abs(t.getLateral()) + Math.abs(t.getYaw()) < 1;
        // axial and lateral and yaw are possible?
    }

    private void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    private Mat3 getTrajectoryTransform(Trajectory t, double frac) {
        // TODO: implement
        return new Mat3();
    }

    private Mat3 getTrajectoryVelocity(Trajectory t, double frac) {
        // TODO: implement
        return new Mat3();
    }

    private class Trajectory {
        private double axial;
        private double lateral;
        private double yaw;
        public Trajectory(double axial, double lateral, double yaw) {
            this.axial = axial;
            this.lateral = lateral;
            this.yaw = yaw;
        }

        public double getAxial() {
            return axial;
        }

        public double getLateral() {
            return lateral;
        }

        public double getYaw() {
            return yaw;
        }
    }

    private interface Obstacle {
        double getDistanceTo(Vec2 point);
    }

    private class DynObstacle implements Obstacle {
        private Mat3 transform;
        private double size;

        @Override
        public double getDistanceTo(Vec2 point) {

        }
    }

    private class StaticObstacle implements Obstacle {
        private Mat3 transform;
        private Vec2 size;

        @Override
        public double getDistanceTo(Vec2 point) {

        }
    }
}

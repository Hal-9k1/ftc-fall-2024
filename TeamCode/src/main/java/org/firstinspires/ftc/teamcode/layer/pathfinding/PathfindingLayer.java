package org.firstinspires.ftc.teamcode.layer.pathfinding;

import java.awt.geom;
import java.awt.geom.Point2D;
import java.lang.Object;
import java.lang.Math;

public class PathfindingLayer extends Layer {
	private static final double a = 0.5;
	private static final double b = 1.2;
	private static final double g = 0.5;
	private static final double sigma = 1;
	private static final double time = 0.25;
	
	
    public PathfindingLayer() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) {

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
		double weightedTargetAngle = evaluateTargetAngle(Trajectory t)*a;
		double weightedClearance = evaluateClearence(Trajectory t)*b;
		double weightedSpeed = evaluateSpeed(Trajectory t)*g;

		return sigma*(weightedTargetAngle + weightedClearance + weightedSpeed);
    }
	

	
    private double evaluateTargetAngle(Trajectory t) {
		double targetSlope = target.getXp()/target.getYp();
		double angle; 
		int c = 1000;
		int k = 1;
		if (t.getYaw() == 0){
		angle = Math.toDegrees(Math.atan((target.getYp()-0)/(target.getXp()-0)))}
//		else { 
		return c/(angle+k);
    }

    private double evaluateClearence(Trajectory t) {
		
    }

    private double evaluateSpeed(Trajectory t) {
		double speed = t.getTrajectoryVelocity;  
		return speed;
    }

    private boolean checkDynamicWindow(Trajectory t) {
        // hit something?
        // axial and lateral and yaw are possible?
    }

    private void addObstacle(Obstacle obstacle) {

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

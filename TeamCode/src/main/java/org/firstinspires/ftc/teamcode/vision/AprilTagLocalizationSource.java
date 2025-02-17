package org.firstinspires.ftc.teamcode.vision;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import org.firstinspires.ftc.teamcode.localization.LocalizationData;
import org.firstinspires.ftc.teamcode.localization.LocalizationSource;
import org.firstinspires.ftc.teamcode.localization.SqFalloffLocalizationData;
import org.firstinspires.ftc.teamcode.matrix.Mat2;
import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Mat4;
import org.firstinspires.ftc.teamcode.matrix.Vec2;
import org.firstinspires.ftc.teamcode.matrix.Vec3;

public class AprilTagLocalizationSource extends CameraModule implements LocalizationSource {
    /**
     * The VisionProcessor AprilTag detections will be read from.
     */
    private AprilTagProcessor processor;

    @Override
    public boolean canLocalizePosition() {
        return true;
    }

    @Override
    public boolean canLocalizeRotation() {
        return true;
    }

    @Override
    public LocalizationData collectData() {
        Vec2 positionSum = new Vec2(0, 0);
        double rotationSum = 0;
        double weightSum = 0;
        List<AprilTagDetection> detections = processor.getDetections();
        for (AprilTagDetection detection : detections) {
            // Get the tag's transform in robot space from pose data in meters and radians as a Mat4
            Position tagPosCameraSpace = detection.robotPose.getPosition();
            Vec3 tagPosCSConv = new Vec3(
                tagPosCameraSpace.unit.toMeters(tagPosCameraSpace.x),
                tagPosCameraSpace.unit.toMeters(tagPosCameraSpace.y),
                tagPosCameraSpace.unit.toMeters(tagPosCameraSpace.z)
            );
            YawPitchRollAngles tagRotCameraSpace = detection.getOrientation();
            Mat3 tagRotCSConv = Mat3.fromYawPitchRoll(
                tagRotCameraSpace.getYaw(AngleUnit.RADIANS),
                tagRotCameraSpace.getPitch(AngleUnit.RADIANS),
                tagRotCameraSpace.getRoll(AngleUnit.RADIANS)
            );
            Mat4 tagTfmRobotSpace = cameraToRobotSpace(
                Mat4.fromTransform(tagRotCSConv, tagPosCSConv)
            );
            // Get the tag's transform in field space from tag library metadata in meters and
            // radians as a Mat4
            VectorF tagPosFieldSpace = detection.metadata.fieldPosition;
            Vec3 tagPosFSConv = new Vec3(
                detection.metadata.distanceUnit.toMeters(tagPosFieldSpace.get(0)),
                detection.metadata.distanceUnit.toMeters(tagPosFieldSpace.get(1)),
                detection.metadata.distanceUnit.toMeters(tagPosFieldSpace.get(2))
            );
            MatrixF tagRotFieldSpace = detection.metadata.fieldOrientation.toMatrix();
            Mat3 tagRotFSConv = new Mat3(
                tagRotFieldSpace.get(0, 0),tagRotFieldSpace.get(1, 0),  tagRotFieldSpace.get(2, 0),
                tagRotFieldSpace.get(0, 1),tagRotFieldSpace.get(1, 1),  tagRotFieldSpace.get(2, 1),
                tagRotFieldSpace.get(0, 2),tagRotFieldSpace.get(1, 2),  tagRotFieldSpace.get(2, 2)
            );
            Mat4 tagTfmFieldSpace = Mat4.fromTransform(tagRotFSConv, tagPosFSConv);

            // Figure out the robot's transform in field space according to this tag
            // robot_f * tag_r = tag_f
            // robot_f = tag_f * tag_r^-1
            Mat4 robotTfmFieldSpace = tagTfmFieldSpace.mul(tagTfmRobotSpace.inv());
            Vec3 robotPosFieldSpace = robotTfmFieldSpace.getTranslation();
            Vec3 robotDirFieldSpace = robotTfmFieldSpace.getDirection();
            // Determine confidence of detection and add to sums for the weighted average at the end
            // TODO: figure out a better formula for this and what decisionMargin actually is
            double confidence = detection.decisionMargin
                * getConfidenceFromDeviance(robotPosFieldSpace.getZ())
                * getConfidenceFromDeviance(robotDirFieldSpace.getZ());
            positionSum = positionSum.add(new Vec2(
                robotPosFieldSpace.getX(),
                robotPosFieldSpace.getY()
            ).mul(confidence));
            rotationSum += new Vec2(robotDirFieldSpace.getX(), robotDirFieldSpace.getY()).getAngle()
                * confidence;
            weightSum += confidence;
        }
        // Return the weighted average transform from all tag detections, incorporating the average
        // confidence as the precision of the l10n data
        double precision = weightSum / detections.size();
        return new SqFalloffLocalizationData(
            Mat3.fromTransform(
                Mat2.fromAngle(rotationSum / weightSum),
                positionSum.mul(1 / weightSum)
            ),
            1,
            precision,
            precision
        );
    }

    @Override
    public void createVisionProcessor(VisionPortal.Builder portalBuilder) {
        processor = AprilTagProcessor.Builder()
            .setTagLibrary(AprilTagGameDatabase.getIntoTheDeepTagLibrary())
            .build();
        portalBuilder.addProcessor(processor);
    }

    /**
     * Uses a simple formula to get a "confidence" in the range (0, 1) from a "deviance."
     *
     * @param deviance the "deviance," a value that is 0 under ideal conditions.
     * @return A confidence that is higher the closer the deviance is to 0.
     */
    private double getConfidenceFromDeviance(double deviance) {
        double sqDeviance = deviance * deviance;
        return sqDeviance / (1 + sqDeviance);
    }
}

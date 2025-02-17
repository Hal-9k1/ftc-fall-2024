package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.vision.VisionPortal;

import org.firstinspires.ftc.teamcode.matrix.Mat4;

/**
 * Base class for VisionProcessor users that must compensate for a camera not being located at the
 * origin of robot space.
 * The origin of robot space lies on the ground at the center of the robot's horizontal bounds
 * facing the robot's front side.
 */
public abstract class AbstractCameraModule {
    /**
     * The robot space transform of the camera.
     */
    private Mat4 cameraTfm;

    /**
     * Sets the transform of the camera in robot space.
     *
     * @param transform the camera's robot space transform.
     */
    public final void setCameraTransform(Mat4 transform) {
        cameraTfm = transform;
    }

    /**
     * Creates the VisionProcessor interpreting image data for this module and attaches it to the
     * VisionPortal builder.
     *
     * @param portalBuilder the builder of the VisionPortal to attach the VisionProcessor to.
     */
    public abstract void createVisionProcessor(VisionPortal.Builder portalBuilder);

    /**
     * Converts a transformation in camera space to one in robot space.
     *
     * @param cameraSpaceTfm the camera space transformation to convert.
     * @return The converted transformation to robot space.
     */
    protected final Mat4 cameraToRobotSpace(Mat4 cameraSpaceTfm) {
        return cameraTfm.mul(cameraSpaceTfm);
    }
}

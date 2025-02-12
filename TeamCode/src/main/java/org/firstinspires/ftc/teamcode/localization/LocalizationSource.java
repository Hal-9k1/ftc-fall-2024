package org.firstinspires.ftc.teamcode.localization;

/**
 * Represents a source of localization data.
 * Often a sensor, but can represent calculated heuristics (such as the tendency of robots to not
 * teleport around the field, making positions closer to previous ones more likely.)
 */
public interface LocalizationSource {
    /**
     * Returns whether this source can determine the robot's position.
     *
     * @return Whether this source is a source of position information.
     */
    boolean canLocalizePosition();

    /**
     * Returns whether this source can determine the robot's rotation.
     *
     * @return Whether this source is a source of rotation information.
     */
    boolean canLocalizeRotation();

    /**
     * Collects localization data.
     *
     * @return the collected localization data.
     */
    LocalizationData collectData();
}

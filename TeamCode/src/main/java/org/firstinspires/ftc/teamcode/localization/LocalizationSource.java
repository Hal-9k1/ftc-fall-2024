package org.firstinspires.ftc.teamcode.localization;

public interface LocalizationSource {
    boolean canLocalizePosition();
    boolean canLocalizeRotation();
    LocalizationData collectData();
}

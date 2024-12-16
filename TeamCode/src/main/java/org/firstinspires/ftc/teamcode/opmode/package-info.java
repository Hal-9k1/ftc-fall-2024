/**
 * Entry points for robot controller code.
 * Control passes to our code in opmodes, annotated subclasses of
 * {@link com.qualcomm.robotcore.eventloop.opmode.OpMode} that can be selected for execution in the
 * Driver Station UI.
 *
 * <p>Using our layer system reduces an opmode to a particular stack and configuration of layers.
 * Opmodes should do this by extending {@link AbstractLayerOpMode} and overriding
 * {@link AbstractLayerOpMode#getLayers} to return the stack of layer objects that will control the
 * robot. Some opmodes here do not use the layer system, but these are for testing purposes and are
 * not meant to be used competitively.
 */
package org.firstinspires.ftc.teamcode.opmode;

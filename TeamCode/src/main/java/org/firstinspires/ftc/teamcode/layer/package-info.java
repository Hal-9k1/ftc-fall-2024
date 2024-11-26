/**
 * Layer implementations and related classes.
 * Layers are the basic unit of robot logic. They accept
 * {@link org.firstinspires.ftc.teamcode.task.Task}s from layers above and over a period, process
 * them into more concrete subtasks that they pass to layers below. Eventually tasks reach a bottom
 * layer, where they are interpreted to do some tangible work on the robot (usually controlling
 * actuators). Tasks are the only interactions between layers, and they are freely interchangable
 * with other layers that accept the same types of tasks.
 */
package org.firstinspires.ftc.teamcode.layer;

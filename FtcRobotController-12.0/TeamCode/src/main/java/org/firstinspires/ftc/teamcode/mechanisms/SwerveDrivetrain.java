package Swerve

package org.firstinspires.ftc.teamcode.mechanics;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class SwerveDrivetrain {

    // 1. DIMENSIONS (Distance from center of robot to wheels in inches)
    // Adjust these based on your actual chassis size!
    private static final double TRACK_WIDTH = 12.0;  // Distance between left and right wheels
    private static final double WHEEL_BASE = 12.0;   // Distance between front and back wheels
    private static final double R = Math.hypot(WHEEL_BASE, TRACK_WIDTH);

    // 2. HARDWARE PLACEHOLDERS
    // TODO: Define your motors, servos, or absolute encoders here once configured
    // Example: private DcMotorEx[] driveMotors = new DcMotorEx[4];
    // Example: private CRServo[] steeringServos = new CRServo[4];

    public SwerveDrivetrain() {
        // Constructor left empty for now
    }

    /**
     * Call this inside your TeleOp init() loop to pass the hardware map
     */
    public void init(HardwareMap hwMap) {
        // TODO: Initialize your physical motors, servos, and zero-offsets here
        // Example: driveMotors[0] = hwMap.get(DcMotorEx.class, "frontLeftDrive");
    }

    /**
     * Core movement method called every loop during TeleOp
     * @param gamepad The driver's controller input
     * @param headingRadians Current gyro heading (use 0 for robot-centric, or map to IMU)
     */
    public void drive(Gamepad gamepad, double headingRadians) {
        // Grab joystick inputs (In FTC, Y joystick is usually inverted)
        double joystickX = gamepad.left_stick_x;
        double joystickY = -gamepad.left_stick_y;
        double turn = gamepad.right_stick_x;

        // FIELD CENTRIC CONVERSION
        // Translates joystick inputs relative to the field instead of the front of the bot
        double cosHeading = Math.cos(headingRadians);
        double sinHeading = Math.sin(headingRadians);

        double fwd = joystickY * cosHeading + joystickX * sinHeading;
        double strafe = -joystickY * sinHeading + joystickX * cosHeading;

        // SWERVE KINEMATICS MATH
        // Intermediate vector math variables
        double a = strafe - turn * (WHEEL_BASE / R);
        double b = strafe + turn * (WHEEL_BASE / R);
        double c = fwd - turn * (TRACK_WIDTH / R);
        double d = fwd + turn * (TRACK_WIDTH / R);

        // Calculate Target Speeds for each module
        double flSpeed = Math.hypot(b, p(c)); // Front Left
        double frSpeed = Math.hypot(b, p(d)); // Front Right
        double blSpeed = Math.hypot(a, p(c)); // Back Left
        double brSpeed = Math.hypot(a, p(d)); // Back Right

        // Calculate Target Angles for each module (in Radians, -PI to +PI)
        double flAngle = Math.atan2(b, c);
        double frAngle = Math.atan2(b, d);
        double blAngle = Math.atan2(a, c);
        double brAngle = Math.atan2(a, d);

        // Normalize speeds so they never try to exceed 1.0 power scale
        double maxSpeed = Math.max(Math.max(flSpeed, frSpeed), Math.max(blSpeed, brSpeed));
        if (maxSpeed > 1.0) {
            flSpeed /= maxSpeed;
            frSpeed /= maxSpeed;
            blSpeed /= maxSpeed;
            brSpeed /= maxSpeed;
        }

        // TODO: Output these target speeds and angles to your modules!
        // Example: Set motor powers to speeds, and PID-control your servos to match target angles.
    }

    // Quick helper method to safely handle internal naming math parameters
    private double p(double value) { return value; }
}

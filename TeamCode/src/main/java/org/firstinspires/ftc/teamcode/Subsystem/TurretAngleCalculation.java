package org.firstinspires.ftc.teamcode.Subsystem;

public class TurretAngleCalculation {
    private static final double gravity = 9.80665;
    private static final double air_density = 1.225;
    private static final double drag_coefficient = 0.3;
    private static final double mass = 0.0743;
    private static final double diameter = 0.127;
    private static final double radius = diameter / 2;
    private static final double area = Math.PI * radius * radius;
    private static final double RPM = 60;
    private static final double omega = 2 * Math.PI * (RPM / 60);
    private static final double lift_center_base = 0.195;
    private static final double v0 = 8.2829;
    private static final double y0 = 0.3302;
    private static final double y_goal = 0.98425;

    private double center_of_lift(double velocity) {
        double spin = omega * radius / (velocity + 1e-6);
        return lift_center_base * spin;
    }

    private double[] acceleration(double vx, double vy) {
        double velocity = Math.sqrt(vx*vx+vy*vy) + 1e-6;
        double drag_force = 0.5 * air_density * drag_coefficient * area * velocity * velocity / mass;
        double lift_force = 0.5 * air_density * center_of_lift(velocity) * area * velocity * velocity / mass;
        double ax = -drag_force * (vx / velocity) - lift_force * (vx / velocity);
        double ay = -gravity -drag_force * (vx / velocity) - lift_force * (vx / velocity);
        return new double[]{ax, ay};
    }
    
    private double[] simulate(double theta_deg, double x_target) {
        double theta = Math.toRadians(theta_deg);
        double vx = v0 * Math.cos(theta);
        double vy = v0 * Math.sin(theta);
        double[] xy = new double[]{0.0, y0};
        double t = 0.0;
        double dt = 0.001;
        double max_T = 3;
        while (t<max_T) {
            double[] axy1 = acceleration(vx, vy);
            double[] vxy2 = new double[]{vx + 0.5 * dt * axy1[0], vy + 0.5 * dt * axy1[0]};
            double[] axy2 = acceleration(vxy2[0], vxy2[1]);
            double[] vxy3 = new double[]{vx + 0.5 * dt * axy2[0], vy + 0.5 * dt * axy2[0]};
            double[] axy3 = acceleration(vxy3[0], vxy3[1]);
            double[] vxy4 = new double[]{vx + 0.5 * dt * axy3[0], vy + 0.5 * dt * axy3[0]};
            double[] axy4 = acceleration(vxy4[0], vxy4[1]);

            xy[0] = (dt * (vx + 2 * (vxy2[0]+vxy3[0])+vxy4[0]));
            xy[1] = (dt * (vx + 2 * (vxy2[1]+vxy3[1])+vxy4[1]));
            vx += dt * (axy1[0] + 2 * (axy2[0] + axy3[0]) + vxy4[0]) / 6;
            vx += dt * (axy1[1] + 2 * (axy2[1] + axy3[1]) + vxy4[1]) / 6;

            t += dt;

            if (xy[1] < 0) {
                return new double[]{-1715.3, -1715.3, -1715.3};
            } else if (xy[0] >= x_target) {
                return new double[]{xy[1], Math.sqrt(vx*vx + vy*vy), t};
            }
        }
        return new double[]{-141.0, -141.0, -141.0};
    }

    public double[] findAngle(double x_in_inches) {
        double x_target = 0.0254 * x_in_inches;

        double low = 5.0;
        double high = 60.0;
        double best_angle = -314159;
        double best_diff = 999;
        double best_v = -314159;
        double best_t = -314159;

        boolean found = false;

        for (int i = 0; i < 25; i++) {
            double mid = (low + high) / 2;
            double[] simulation = simulate(mid, x_target);

            if (simulation[0] == -141.0 || simulation[0] == 1715.3) {
                System.out.println(simulation[0]);
                continue;
            }
            double diff = simulation[0] - y_goal;
            if (Math.abs(diff) <  Math.abs(best_diff)) {
                best_angle = mid;
                best_diff = diff;
                best_v = simulation[1];
                best_t = simulation[2];
                found = true;
            }
            if (diff > 0) {
                high = mid;
            } else {
                low = mid;
            }
        }
        if (!found) {
            return new double[]{3216.5, 3216.5, 3216.5};
        } else {
            return new double[]{best_angle, best_t, best_v};
        }
    }
}

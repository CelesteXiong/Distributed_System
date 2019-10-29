package DSPPCode.spark.pi;

import java.util.concurrent.ThreadLocalRandom;


public class PiSimulatorImpl extends PiSimulator {
    long seed = 95;
    double r = 1;
    double d = 2;
    double lower = -1;
    double upper = 1;
    @Override
    public Integer call(Object unused) {
        double x = ThreadLocalRandom.current().nextDouble(lower, upper);
        double y = ThreadLocalRandom.current().nextDouble(lower, upper);
        if ((x*x + y*y) <= (r*r))
        {
            return 1;
        }
        else
            return 0;
    }
}

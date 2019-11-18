package DSPPCode.storm.window_join;
import java.util.concurrent.TimeUnit;

import org.apache.storm.bolt.JoinBolt;
import org.apache.storm.shade.org.joda.time.Duration;
import org.apache.storm.topology.base.BaseWindowedBolt;

public class StormJoinBoltImpl extends StormJoinBolt {
    @Override
    public void setJoinBolt() {

    }

    @Override
    public JoinBolt getJoinBolt() {
        JoinBolt jbolt = new JoinBolt("genderSpout","id")
                .join("ageSpout","id","genderSpout")
                .select("id, gender, age")
                .withTumblingWindow(new BaseWindowedBolt.Duration(2,TimeUnit.SECONDS));
        return jbolt;
    }
}

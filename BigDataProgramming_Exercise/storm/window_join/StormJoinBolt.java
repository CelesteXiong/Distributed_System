package DSPPCode.storm.window_join;

import org.apache.storm.bolt.JoinBolt;

import java.io.Serializable;

/**
 * Storm joinAPI使用介绍
 * https://storm.apache.org/releases/1.2.3/Joins.html
 * @author chenqh
 * @version 1.0.0
 * @date 2019-11-04
 */
abstract public class StormJoinBolt implements Serializable {
    abstract public void  setJoinBolt();
    abstract public JoinBolt getJoinBolt();
}

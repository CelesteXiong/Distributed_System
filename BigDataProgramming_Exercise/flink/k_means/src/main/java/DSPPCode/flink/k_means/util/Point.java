package DSPPCode.flink.k_means.util;

import java.io.Serializable;

/**
 * 数据点工具类
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class Point implements Serializable {
    public double x, y;

    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /** 两点相加*/
    public Point add(Point other) {
        x += other.x;
        y += other.y;
        return this;
    }
    /** 两点相除*/
    public Point div(long val) {
        x /= val;
        y /= val;
        return this;
    }
    /** 两点欧式距离*/
    public double euclideanDistance(Point other) {
        return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
    }
    /** 清空此点*/
    public void clear() {
        x = y = 0.0;
    }
    /** 重写的打印方法*/
    @Override
    public String toString() {
        return x + " " + y;
    }
}

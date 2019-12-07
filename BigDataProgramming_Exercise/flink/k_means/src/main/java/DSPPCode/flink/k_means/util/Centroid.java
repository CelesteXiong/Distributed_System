package DSPPCode.flink.k_means.util;

/**
 * 中心点 工具类
 *
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class Centroid extends Point {
    /** 中心点 id*/
    public int id;

    public Centroid() {
    }

    public Centroid(int id, double x, double y) {
        super(x, y);
        this.id = id;
    }

    public Centroid(int id, Point p) {
        super(p.x, p.y);
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " " + super.toString();
    }
}

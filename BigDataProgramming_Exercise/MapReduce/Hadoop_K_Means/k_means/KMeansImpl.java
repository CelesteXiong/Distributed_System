package DSPPCode.hadoop.k_means;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.util.ToolRunner;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;

import static DSPPCode.hadoop.k_means.Util.*;

public class KMeansImpl extends KMeans {
    @Override
    public void kMeans(String inputPath, String oldCenterPath, String newCenterPath) throws Exception {
            while (!compareAndUpdateCenters(oldCenterPath, newCenterPath)) {
                runOneStep(inputPath, oldCenterPath, newCenterPath);
            }
    }

}
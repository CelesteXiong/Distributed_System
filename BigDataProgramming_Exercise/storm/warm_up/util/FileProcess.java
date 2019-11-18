package DSPPCode.storm.warm_up.util;

import java.io.*;

public class FileProcess implements Serializable {

    public static BufferedWriter getWriter(String toWrite) {
        BufferedWriter bw = null;
        File f = null;
        FileWriter fw = null;
        try {
            f = new File(toWrite);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            fw = new FileWriter(f);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bw;
    }

    public static BufferedReader getReader(String toRead) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(toRead)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return br;
    }

    public static void write(String str, BufferedWriter bw) {
        if (bw != null) {
            try {
                bw.write(str);
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String read(BufferedReader br) {
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        br = null;
    }

    public static void close(BufferedWriter bw) {
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bw = null;
    }

}

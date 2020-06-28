package cn.wangbowen.hexoutil;

import java.io.*;

/**
 * CmdUtils class
 * 命令行调用工具类
 *
 * @author BoWenWang
 * @date 2020/6/28 13:32
 */
public class CmdUtils {

    /**
     * 调用shell脚本（需要用到git的sh.exe）
     * @param shellPath
     */
    public static void callShell(String shellPath) {

        File shellFile = new File(shellPath);
        if (shellFile.exists()) {
            String[] split = shellPath.split(":*\\\\");
            StringBuilder sb = new StringBuilder();
            for (String s : split) {
                sb.append("/").append(s);
            }
            callCmd(HexoUtilProperties.SH_EXE_PATH + "\\bin\\sh.exe -c " + "\"" + sb.toString() + "\"");
        } else {
            System.out.println("shellPath not exist!");
        }
    }

    /**
     * 调用bat脚本
     * @param batPath
     */
    public static void callBat(String batPath) {
        File batFile = new File(batPath);
        if (batFile.exists()) {
            callCmd(batPath);
        } else {
            System.out.println("batPath not exist!");
        }
    }

    /**
     * 调用cmd
     * @param locationCmd
     */
    public static void callCmd(String locationCmd){
        StringBuilder sb = new StringBuilder();
        try {
            Process child = Runtime.getRuntime().exec(locationCmd);
            InputStream in = child.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                sb.append(line).append("\n");
            }
            in.close();
            child.waitFor();
            System.out.println("[OUTPUT INFO]");
            System.out.println(sb.toString());
            System.out.println("callCmd execute finished.");
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }
}

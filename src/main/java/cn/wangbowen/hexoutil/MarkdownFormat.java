package cn.wangbowen.hexoutil;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MarkdownFormat class
 * 一键生成Hexo文章和CSDN文章格式，并且使用码云作为图床。
 *
 * @author BoWenWang
 * @date 2020/6/28 19:51
 */
public class MarkdownFormat {

    /**
     * 文章计数器
     */
    public static int ARTICLE_COUNT = 0;

    /**
     * 如果是是目录递归遍历里面的文件
     * 会过滤掉以.和_开头的内容
     *
     * @param file 文件/目录
     */
    public static void forEachFile(File file) {
        // 文件过滤
        if (file.getName().startsWith(".") || file.getName().startsWith("_")) {
            return;
        }
        // 如果是一个目录，递归进入
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                forEachFile(file1);
            }
        } else {
            // 如果是文章，进行转换。如果是图片进行拷贝
            if (file.getName().endsWith(".md")) {
                formatMarkdown(file);
            } else {
                String img = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                if ("jpg".equalsIgnoreCase(img) || "png".equalsIgnoreCase(img)) {
                    copyFile(file, HexoUtilProperties.OUTPUT_IMAGES_PATH + "\\" + file.getName());
                }
            }
        }
    }
    private static void copyFile(File file, String outputPath) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath))) {
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对文章进行转换(如果是目录，里面的文件最好不要重名，即使是在不同目录下)
     *
     * @param file 文章
     */
    public static void formatMarkdown(File file) {

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(HexoUtilProperties.OUTPUT_PATH_CSDN + "\\" + file.getName()));
             BufferedWriter bw2 = new BufferedWriter(new FileWriter(HexoUtilProperties.OUTPUT_PATH_HEXO + "\\" + file.getName()))) {

            String line = null;
            // 正文部分（因为hexo开头的---文章属性---内容要过滤掉）
            int contentFlag = 0;
            while ((line = br.readLine()) != null) {
                if (contentFlag < 2) {
                    // 处理 hexo格式的内容
                    // 修改封面URL地址
                    if (line.startsWith("cover")) {
                        String coverImgUrl = line.substring(line.indexOf("'") + 1);
                        line = "cover: '" + HexoUtilProperties.PREFIX_URL + coverImgUrl;
                    }
                    // 删除相对路径
                    if (!line.startsWith("typora-root-url")) {
                        bw2.write(line);
                        bw2.newLine();
                    }

                    if ("---".equals(line)) {
                        contentFlag++;
                    }
                } else {
                    if (!line.startsWith("typora-root-url")) {
                        // 如果是包含图片进行转换
                        final String url = containImagesUrl(line);
                        if (null != url) {
                            line = line.replaceAll(url, HexoUtilProperties.PREFIX_URL + url);
                        }
                        // 将该行内容写入新文件
                        bw.write(line);
                        bw.newLine();
                        bw2.write(line);
                        bw2.newLine();
                    }
                }
            }
            // 刷新落盘
            bw.flush();
            bw2.flush();
            ARTICLE_COUNT++;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对图片URL进行过滤
     * @param line 一行文本
     * @return  匹配到的字符串，如果Null说明，没有找到
     */
    private static String containImagesUrl(String line) {

        // 按指定模式在字符串查找
        String pattern = "!\\[.*]\\((.*)\\)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            return m.group(1);
        }
        return null;
    }

    /**
     * 初始化文件夹，里面有内容清空，没有创建
     */
    private static void initDir() {
        String[] filePaths = {HexoUtilProperties.OUTPUT_PATH_CSDN, HexoUtilProperties.OUTPUT_PATH_HEXO};
        for (String path : filePaths) {
            final File file = new File(path);
            if (file.exists()) {
                // 存在清空
                final File[] files = file.listFiles();
                for (File file1 : files) {
                    file1.delete();
                }
            } else {
                // 不存在创建
                file.mkdirs();
            }
        }
    }

    public static void main(String[] args) {
        // 清空或创建文件夹
        initDir();
        // 获取输入文件目录进行处理
        File file = new File(HexoUtilProperties.BLOG_ARTICLE_ROOT_PATH);
        forEachFile(file);
        System.out.println("文章数：" + ARTICLE_COUNT);
        // GIT上传图床
        System.out.println("正在更新图床...");
        CmdUtils.callShell(HexoUtilProperties.GIT_SHELL_PATH);
        // SSH阿里云进行同步更新
        System.out.println("正在更新博客...");
        CmdUtils.callShell(HexoUtilProperties.BLOG_UPDATE_SHELL_PATH);
    }
}

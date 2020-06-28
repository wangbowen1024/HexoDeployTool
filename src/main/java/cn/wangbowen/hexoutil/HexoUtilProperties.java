package cn.wangbowen.hexoutil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * MyProperties class
 * 配置文件
 *
 * @author BoWenWang
 * @date 2020/6/28 13:42
 */
public class HexoUtilProperties {
    /**
     * HexoUtil所在目录
     */
    public static String HEXO_UTIL_HOME;
    /**
     * 博客文章根目录
     */
    public static String BLOG_ARTICLE_ROOT_PATH;
    /**
     * 图片生成位置（Git项目位置）
     */
    public static String OUTPUT_IMAGES_PATH;
    /**
     * URL前缀追加内容
     */
    public static String PREFIX_URL;
    /**
     * 目标输出路径（CSDN格式）
     */
    public static String OUTPUT_PATH_CSDN;
    /**
     * 目标输出路径（HEXO格式）
     */
    public static String OUTPUT_PATH_HEXO;
    /**
     * 图床更新shell脚本路径
     */
    public static String GIT_SHELL_PATH;
    /**
     * 博客更新shell脚本路径
     */
    public static String BLOG_UPDATE_SHELL_PATH;
    /**
     * git-sh.exe程序路径（需要配置环境变量GIT_HOME或者自定义全路径）
     */
    public static String SH_EXE_PATH;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("../conf/hexo-util.properties"));
            BLOG_ARTICLE_ROOT_PATH = properties.getProperty("BLOG_ARTICLE_ROOT_PATH");
            PREFIX_URL = properties.getProperty("PREFIX_URL");
            HEXO_UTIL_HOME = properties.getProperty("HEXO_UTIL_HOME");
            SH_EXE_PATH = properties.getProperty("SH_EXE_PATH");

            OUTPUT_PATH_CSDN = HEXO_UTIL_HOME + "\\output\\_posts_CSDN";
            OUTPUT_PATH_HEXO = HEXO_UTIL_HOME + "\\output\\_posts";
            GIT_SHELL_PATH = HEXO_UTIL_HOME + "\\bin\\autoGit.sh";
            BLOG_UPDATE_SHELL_PATH  = HEXO_UTIL_HOME + "\\bin\\updateBlog.sh";
            OUTPUT_IMAGES_PATH = HEXO_UTIL_HOME + "\\output\\BlogImgs\\postImages";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

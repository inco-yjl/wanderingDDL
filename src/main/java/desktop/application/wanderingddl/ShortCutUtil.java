package desktop.application.wanderingddl;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.channels.FileChannel;


public class ShortCutUtil {
    /**
     * 开机启动目录
     */
    public final static String startup = System.getProperty("user.home") +
            "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";
    /**
     * 桌面目录
     */
    public final static String desktop = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "\\";
    /**
     * 文件头，固定字段
     */
    private static byte[] headFile = {0x4c, 0x00, 0x00, 0x00,
            0x01, 0x14, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,
            (byte) 0xc0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46
    };
    /**
     * 文件头属性
     */
    private static byte[] fileAttributes = {(byte) 0x93, 0x00, 0x08, 0x00,//可选文件属性
            0x20, 0x00, 0x00, 0x00,//目标文件属性
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,//文件创建时间
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,//文件修改时间
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,//文件最后一次访问时间
            0x00, 0x00, 0x00, 0x00,//文件长度
            0x00, 0x00, 0x00, 0x00,//自定义图标个数
            0x01, 0x00, 0x00, 0x00,//打开时窗口状态
            0x00, 0x00, 0x00, 0x00,//热键
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00//未知
    };
    /**
     * 固定字段1
     */
    private static byte[] fixedValueOne = {
            (byte) 0x83, 0x00, 0x14, 0x00
            , 0x1F, 0x50, (byte) 0xE0, 0x4F
            , (byte) 0xD0, 0x20, (byte) 0xEA
            , 0x3A, 0x69, 0x10, (byte) 0xA2
            , (byte) 0xD8, 0x08, 0x00, 0x2B
            , 0x30, 0x30, (byte) 0x9D, 0x19, 0x00, 0x2f
    };

    /**
     * 固定字段2
     */
    private static byte[] fixedValueTwo = {
            0x3A, 0x5C, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
            , 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
            , 0x00, 0x54, 0x00, 0x32, 0x00, 0x04
            , 0x00, 0x00, 0x00, 0x67, 0x50, (byte) 0x91, 0x3C, 0x20, 0x00
    };

    /**
     * 生成快捷方式
     *
     * @param start  完整的文件路径
     * @param target 完整的快捷方式路径
     */
    private static void start(String start, String target) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(createDirectory(start));
            fos.write(headFile);
            fos.write(fileAttributes);
            fos.write(fixedValueOne);
            fos.write((target.toCharArray()[0] + "").getBytes());
            fos.write(fixedValueTwo);
            fos.write(target.substring(3).getBytes("gbk"));
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解决父路径问题
     */
    private static File createDirectory(String file) {
        File f = new File(file);
        //获取父路径
        File fileParent = f.getParentFile();
        //如果文件夹不存在
        if (fileParent != null && !fileParent.exists()) {
            //创建文件夹
            fileParent.mkdirs();
        }
        //快捷方式已存在,则删除原来存在的文件
        if (f.exists()) {
            f.delete();
        }
        return f;
    }

    /**
     * 复制文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * 创建快捷方式
     *
     * @param lnkFilePath    快捷方式文件路径
     * @param targetFilePath 快捷方式对应源文件的文件路径
     */
    public static void createShortCut(String lnkFilePath, String targetFilePath) {
        if (!System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            System.out.println("当前系统不是window系统,无法创建快捷方式!!");
            return;
        }
        start(lnkFilePath, targetFilePath);
    }

    /**
     * 生成快捷方式
     *
     * @param lnkFile    快捷方式文件
     * @param targetFile 快捷方式对应源文件
     */
    public static void createShortCut(File lnkFile, File targetFile) {

        if (!System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            System.out.println("当前系统不是window系统,无法创建快捷方式!!");
            return;
        }
        start(lnkFile.getPath(), targetFile.getPath());
    }

    /**
     * 设置某软件开启启动
     *
     * @param targetFile 源文件
     * @return 是否设置成功
     */
    public static boolean setAppStartup(File targetFile) {

        File lnkFile = new File(targetFile.getParent(), "temp.lnk");
        createShortCut(lnkFile, targetFile);
        try {
            //获取不带扩展名的文件名
            String name = targetFile.getName();
            int end = name.lastIndexOf(".");
            String extendName = name.substring(0, end);

            //将软件复制到软件想
            copyFileUsingFileChannels(lnkFile, new File(startup, extendName + ".lnk"));
            //删除缓存的快捷方式文件
            lnkFile.delete();
            return true;
        } catch (IOException e) {
            System.out.println("移动到startup文件夹失败");
            return false;
        }
    }

    /**
     * 设置某软件开启启动
     *
     * @param targetFilePath 源文件路径
     * @return 是否设置成功
     */
    public static boolean setAppStartup(String targetFilePath) {
        File targetFile = new File(targetFilePath);
        return setAppStartup(targetFile);
    }

    /**
     * 取消开机启动
     *
     * @param targetFile
     */
    public static void cancelAppStartup(File targetFile) {
        File startupDir = new File(startup);
        String targetFileName = targetFile.getName();
        int endIndex = targetFileName.lastIndexOf(".");
        final String targetName = targetFileName.substring(0, endIndex);

        File[] files = startupDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                //获取不带扩展名的文件名
                int end = name.lastIndexOf(".");
                String filename = name.substring(0, end);
                if (filename.equals(targetName)) {
                    return true;
                }
                return false;
            }
        });
        if (files.length > 0) {
            files[0].delete();
        }
    }

}

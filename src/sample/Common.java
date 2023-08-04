package sample;

public class Common {
    public static String[] sortTypes = {"Date", "Size", "Type"};
    public static String[] extensionsImage = {"bmp", "jpg", "png"};
    public static String[] extensionsVideo = {"avi", "mp4", "3gp"};
    public static String extensionsText = "txt";
    public static String metadataFolderName = ".ics";
    public static String childSlash = "\\";
    public static double buttonImageSize = 25;
    public static double mediaHeight = 150;
    public static double mediaWidth = 150;
    public static double iconHeight = 15;
    public static double iconWidth = 15;
    public static int locationLength = 30;

    public static String aboutString = "Thanks for your running";

    private static final String executionPath = System.getProperty("user.dir").replace("\\", "/");
    public static String configFile = executionPath + "/" + "config.txt";
    public static String iconCamera = executionPath + "/" + "src/sample/img/camera.png";
    public static String backgroundLogin = executionPath + "/" + "src/sample/img/background-main.jpg";
    public static String backgroundMain = executionPath + "/" + "src/sample/img/background-main.jpg";
    public static String iconFolder = executionPath + "/" + "src/sample/img/icon-folder.png";
    public static String iconMissMedia = executionPath + "/" + "src/sample/img/icon-miss-media.png";
    public static String iconVideo = executionPath + "/" + "src/sample/img/icon-video1.png";
    public static String iconBack = executionPath + "/" + "src/sample/img/icon-back.png";
    public static String iconNext = executionPath + "/" + "src/sample/img/icon-next.png";
    public static String iconLoading = executionPath + "/" + "src/sample/img/icon-loading.png";
}

package mcs;

public class Common {
    public static String appName = "Image Comment System";
    public static String version = "1.0";
    public static String[] sortTypes = {"Date", "Size", "Type"};
    public static String[] extensionsImage = {"bmp", "jpg", "png"};
    public static String[] extensionsImageExtra = {"heic"};
    public static String[] extensionsVideo = {"avi", "mp4", "3gp", "mov"};
    public static String extensionsText = "txt";
    public static String metadataFolderName = ".ics";
    public static String temp = "D:/temp";
    public static String childSlash = "/";
    public static String[] creationDate = {"date/time", "creation date"};
    public static double buttonImageSize = 25;
    public static double mediaHeight = 150;
    public static double mediaWidth = 150;
    public static double iconHeight = 15;
    public static double iconWidth = 15;
    public static int locationLength = 30;
    public static int editTextLength = 8000;

    public static String aboutString = "Thanks for your running";
    public static String messageForNotChecksum = "Please restore this file";
    public static String messageForLongUrl = "Too long url. Input not large than " + editTextLength;
    public static String messageForLongDescription = "Too long description. Input not large than " + editTextLength;
    public static String pathImageMagicK = "C:/Program Files/ImageMagick-7.1.1-Q16-HDRI/magick.exe";
    public static String pathVlc = "C:/Program Files/VideoLAN/VLC/vlc.exe";

    private static final String executionPath = System.getProperty("user.dir").replace("\\", "/");
    public static String configFile = executionPath + "/" + "config.txt";
    public static String iconCamera = executionPath + "/" + "img/camera.png";
    public static String backgroundLogin = executionPath + "/" + "img/background-main.jpg";
    public static String backgroundMain = executionPath + "/" + "img/background-main.jpg";
    public static String iconImage = executionPath + "/" + "img/icon-image.png";
    public static String iconFolder = executionPath + "/" + "img/icon-folder.png";
    public static String iconMissMedia = executionPath + "/" + "img/icon-miss-media.png";
    public static String iconVideo = executionPath + "/" + "img/icon-video1.png";
    public static String iconVideo2 = executionPath + "/" + "img/icon-video2.png";
    public static String iconBack = executionPath + "/" + "img/icon-back.png";
    public static String iconNext = executionPath + "/" + "img/icon-next.png";
    public static String iconSearch = executionPath + "/" + "img/icon-search.png";
    public static String iconLoadingSearch = executionPath + "/" + "img/icon-loading.png";
    public static String iconLoading = executionPath + "/" + "img/icon-loading1.png";
}

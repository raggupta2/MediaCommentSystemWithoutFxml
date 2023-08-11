package sample.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/***
 * This is a class for treatment video file.
 * You can use this class after import some library of lib/javacv folder into classpath.
 */
public class FrameUtil {
    /***
     * A method to get a snapshot from video file.
     * @param videoFilePath : A absolute path of video file.
     * @return File
     */
    public Image getFrame(String videoFilePath){
        Image ret = null;
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoFilePath);
        try {
            frameGrabber.start();
            System.out.println("Video has " + frameGrabber.getLengthInFrames() + " frames and has frame rate of "+ frameGrabber.getFrameRate());
        } catch (FFmpegFrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
        Frame frame;
        try {
            try {
                int imgNum = 1;
                Java2DFrameConverter converter = new Java2DFrameConverter();
                for(int i=0;i<=frameGrabber.getLengthInFrames();i++){
                    imgNum++;
                    frameGrabber.setFrameNumber(i);
                    frame = frameGrabber.grabImage();
                    BufferedImage bi = converter.convert(frame);
                    if(bi == null) continue;
                    String path = imgNum + ".jpg";
                    ImageIO.write(bi, "jpg", new File(path));
                    ret = SwingFXUtils.toFXImage(bi, null);;
                    break;
                }
                frameGrabber.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }
}

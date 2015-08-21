package spaghetti;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Util {
    public static BufferedImage scaleImage(BufferedImage b, int width, int height) {
        double scalex = (double)width/b.getWidth();
        double scaley = (double)height/b.getHeight();
        
        AffineTransform at = AffineTransform.getScaleInstance(scalex, scaley);
        AffineTransformOp op = new AffineTransformOp(at, null);
        
        return op.filter(b, null);
    }
    
    public static BufferedImage scaleImage(BufferedImage b, int multiplier) {
        double scalex = (double)multiplier;
        double scaley = (double)multiplier;
        
        AffineTransform at = AffineTransform.getScaleInstance(scalex, scaley);
        AffineTransformOp op = new AffineTransformOp(at, null);
        
        return op.filter(b, null);
    }
    
    public static BufferedImage rotateImage(BufferedImage b, double amount) {
        double rot = Math.toRadians(amount);
        double rotx = b.getWidth() / 2;
        double roty = b.getHeight() / 2;
        AffineTransform at = AffineTransform.getRotateInstance(rot, rotx, roty);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        
        return op.filter(b, null);
    }
    
    private static Clip loadClip(String relativePath)
    {
        try {
            Clip clip = AudioSystem.getClip();
            InputStream audioSrc = Util.class.getClass().getResourceAsStream(relativePath);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip.open(audioStream);
            return clip;        
        } 
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println("Error loading audio: " + e.getMessage());
            return null; 
        }
    }
    
    public static BufferedImage loadImage(String relativePath)
    {
        try {
            BufferedImage b = ImageIO.read(Util.class.getResource(relativePath));
            return b;
        }
        catch(IOException e) {
            System.err.println("Error loading image: " + e.getLocalizedMessage());
            return null;
        }
    }
}
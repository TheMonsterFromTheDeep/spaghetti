package fettuccine.uitl;

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

/**
 * Provides static methods for loading various types of resources, including
 * images and sounds.
 * @author TheMonsterFromTheDeep
 */
public class Resources {
    /**
     * Loads a BufferedImage from a path relative to the project root directory.
     * @param relPath The relative path to load the image from.
     * @return A BufferedImage loaded from the specified path, or null if an exception occurred.
     */
    public static BufferedImage loadImageResource(String relPath) {
        try {
            BufferedImage b = ImageIO.read(Resources.class.getResource('/' + relPath));
            return b;
        }
        catch(IOException e) {
            System.err.println("Could not load image resource! " + e.getLocalizedMessage());
            return null;
        }
    }
    
    /**
     * Loads an audio Clip from a path relative to the project root directory.
     * @param relPath The relative path to load the audio clip from.
     * @return A Clip loaded from the specified path, or null if an exception occurred.
     */
    public static Clip loadAudioResource(String relPath)
    {
        try {
            Clip clip = AudioSystem.getClip();
            InputStream audioSrc = Resources.class.getClass().getResourceAsStream('/' + relPath);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip.open(audioStream);
            return clip;        
        } 
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println("Could not load audio resource! " + e.getMessage());
            return null; 
        }
    }
}
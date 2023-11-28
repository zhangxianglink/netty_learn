package audio.v3;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * x.z
 * Create in 2023/3/13
 */
public class format {

    public static void main(String[] args) throws InterruptedException, URISyntaxException, UnsupportedAudioFileException, IOException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException {


        AudioFile audioFile = AudioFileIO.read(new File("D:\\linuxupload\\1156948300751142912_36960.mp3"));
        AudioHeader audioHeader = audioFile.getAudioHeader();
        System.out.println(audioHeader.getChannels());
        boolean stereo = (audioHeader.getChannels().equals("Stereo") || audioHeader.getChannels().equals("2"));
                       String channels = stereo ? "2" : "1";
        audioHeader.getSampleRate();
        String a = stereo ? String.valueOf(audioHeader.getTrackLength()/2) : String.valueOf(audioHeader.getTrackLength());

        System.out.println(channels);






    }

}

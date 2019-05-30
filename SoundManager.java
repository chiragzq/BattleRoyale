import java.util.HashMap;
import java.util.Map;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 * Class that manages playing sounds in the game. It saves memory by caching a loaded sound file,
 * making playing the same sound repeatedly efficient. It also supports playing at a specific volume.
 * 
 * @author Chirag Kaushik
 * 
 */
public class SoundManager {
    private static final Map<String, Sound> sounds = new HashMap<String, Sound>();

    static {
        TinySound.init();
    }

    /**
     * Plays a sound based on its name. All sound files must be wav and included the sound/ directory
     * @param name the name of the wav file in the sound/ directory
     * @param volume a value from [0.0, 1.0] representing the percentage of volume to play at
     */
    public static void playSound(String name, double volume) {
        if(!sounds.containsKey(name)) {
            String path = "sound/" + name + ".wav";
            Sound sound = TinySound.loadSound(path);
            sounds.put(name, sound);
        }
        Sound sound = sounds.get(name);
        sound.play(volume);
    }

    /**
     * Stops the audio thread, freeing up memory.
     */
    public static void shutdown() {
        TinySound.shutdown();
    }
}  
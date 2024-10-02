public class Adapter {
    // MediaPlayer.java
interface MediaPlayer {
    void play(String audioType, String fileName);
}

// AudioPlayer.java
class AudioPlayer implements MediaPlayer {
    @Override
    public void play(String audioType, String fileName) {
        System.out.println("Playing " + audioType + " file: " + fileName);
    }
}

// MediaAdapter.java
class MediaAdapter implements MediaPlayer {
    private AudioPlayer audioPlayer;

    public MediaAdapter() {
        audioPlayer = new AudioPlayer();
    }

    @Override
    public void play(String audioType, String fileName) {
        audioPlayer.play(audioType, fileName);
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        MediaAdapter mediaAdapter = new MediaAdapter();
        mediaAdapter.play("MP3", "song.mp3");
    }
}

}

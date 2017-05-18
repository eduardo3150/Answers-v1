package AndroidAudioControl;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import com.lyckan.moviles.udb.answers.R;


public class AudioControl {
    private MediaPlayer player;
    private SoundPool soundContainer;
    private int success =0, error = 0;

    public AudioControl(Context context){
        player = MediaPlayer.create(context, R.raw.song);
        player.setLooping(true);
        soundContainer = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        success = soundContainer.load(context, R.raw.success, 1);
        error = soundContainer.load(context, R.raw.error, 1);

    }

    //Reproduce cancion
    public void PlaySong(){
        player.start();
    }

    //Para cancion
    public void StopSong(){
        player.stop();
    }

    //Pausa cancion
    public void PauseSong(){
        player.stop();
    }

    //Reproduce sonido de correcto
    public void Correct(){
        player.setVolume(0.2f, 0.2f);
        soundContainer.play(success,1, 1, 0, 0 , 1);
        player.setVolume(1,1);
    }


    //Reproduce sonido de error
    public void Error(){
        player.setVolume(0.2f, 0.2f);
        soundContainer.play(error,1, 1, 0, 0 , 1);
        player.setVolume(1,1);
    }

    //TODO Falta agregar sonidos como el de ganar y el inicio aunque es extra eso
}

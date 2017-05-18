package position.Answers;

import android.util.Log;

public class RandomPosition {
    public String[] RandomTextPosition(String palabra1, String palabra2){
        String[] position = new String[2];
        int random = (int) (Math.random()* 10);
        Log.d("vvas", String.valueOf(random));
        if (random <=5){
            position[0] = palabra1;
            position[1] = palabra2;
        }else {
            position[1] = palabra1;
            position[0] = palabra2;
        }
        return  position;

    }
}

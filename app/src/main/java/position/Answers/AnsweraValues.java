package position.Answers;

import android.util.Log;
import android.widget.TextView;

public class AnsweraValues {


    public boolean CorrectAnswer(TextView answerLeftTextView, TextView answerRightTextView, String respCorrecta, int seleccion){
        String answerLeft, answerRight;
        answerLeft = answerLeftTextView.getText().toString();
        answerRight = answerRightTextView.getText().toString();
        boolean state = false;

        switch (seleccion){
            //Si es 0 la seleccio fue a la derecha, se evalua la respuesta de la izquierda con la derecha
            case 0:
                Log.d("SELECT", "Izquierda");
                if (answerLeft == respCorrecta){
                    state = true;
                }else {
                    state = false;
                }
                break;

            //Si es 0 la seleccio fue a la izquierda, se evalua la respuesta de la izquierda con la correcta

            case 10:
                Log.d("SELECT", "Derecha");
                if (answerRight == respCorrecta){
                    state = true;
                }else{
                    state = false;
                }
                break;

            default:
        }
        return state;
    }
}
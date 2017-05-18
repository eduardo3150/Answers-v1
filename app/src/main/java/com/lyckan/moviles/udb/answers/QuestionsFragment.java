package com.lyckan.moviles.udb.answers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import AndroidAudioControl.AudioControl;
import base.de.datos.DbAnswersControl;
import de.hdodenhof.circleimageview.CircleImageView;
import position.Answers.AnsweraValues;
import position.Answers.RandomPosition;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionsFragment extends Fragment {
    //private TextView debug;
    private float feedBack;

    private int checker =5;
    boolean descicionT;
    Vibrator vibrator;
    Animation fadeIn, fadeOut;




    //VALORES PARA HACER FUNCIONAR LAS PREGUNTAS
    TextView pregunta, respuestaIzquierda, respuestaDerecha,scorePrint;
    DbAnswersControl dbAnswersControl;
    RandomPosition randomPosition = new RandomPosition();
    String[] preguntaYRespuesta;
    CircleImageView imageView;
    AudioControl audioControl;
    private int preguntasCount;
    private Button skipQuestion;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public QuestionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionsFragment newInstance(String param1, String param2) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //debug = (TextView) getActivity().findViewById(R.id.debugText);

        //Cargando elementos
        dbAnswersControl = new DbAnswersControl(getContext(),"answer",1);
        pregunta = (TextView) getActivity().findViewById(R.id.preguntaField);
        respuestaIzquierda = (TextView) getActivity().findViewById(R.id.respuestaIzq);
        respuestaDerecha = (TextView) getActivity().findViewById(R.id.respuestaDer);
        imageView = (CircleImageView) getActivity().findViewById(R.id.imageField);
        skipQuestion = (Button) getActivity().findViewById(R.id.skipQuestion);
        scorePrint = (TextView) getActivity().findViewById(R.id.scoreCurrent);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        fadeIn = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        fadeOut =AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);

        audioControl=new AudioControl(getContext());

        preguntaRandom();

        audioControl.PlaySong();
        scorePrint.setText("Puntaje: "+0);
        skipQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewQuestion();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onSensorEvent(SensorEvent sensorEvent){

        feedBack = Math.round(sensorEvent.values[0]);

        if (feedBack<=2&&feedBack>=0) {
            feedBack = 0;
        }

        if (!descicionT){
            if (feedBack>=8){
                feedBack=0;
                checker = 0;
                //debug.setBackgroundColor(Color.BLUE);
                izquierda();
                return;
            }
        }
        if (!descicionT){
            if (feedBack<=-8){
                feedBack = 0;
                checker = 10;
                //debug.setBackgroundColor(Color.RED);
                derecha();
                return;
            }
        }

        //debug.setBackgroundColor(Color.TRANSPARENT);
        //debug.setText("Valores: "+ Float.toString(feedBack));
        checker = 5;
        feedBack=0;
        respuestaDerecha.setBackgroundColor(Color.TRANSPARENT);
        respuestaIzquierda.setBackgroundColor(Color.TRANSPARENT);

    }

    public void preguntaRandom(){
        if (dbAnswersControl.checkAnswers()){
                preguntaYRespuesta = dbAnswersControl.getAnswer();
                String[] respuestasTemporales = randomPosition.RandomTextPosition(preguntaYRespuesta[3],dbAnswersControl.getAnswerError());
                pregunta.setText(preguntaYRespuesta[1]);
                String mDrawableName = preguntaYRespuesta[2];
                int resId = getResources().getIdentifier(mDrawableName,"drawable",getActivity().getPackageName());
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(),resId));
                respuestaIzquierda.setText(respuestasTemporales[0]);
                respuestaDerecha.setText(respuestasTemporales[1]);
                animationFadeIn();

        } else {
            descicionT=true;
            Toast.makeText(getContext(),"No mas preguntas",Toast.LENGTH_SHORT).show();

        }
    }

    public void izquierda(){
        descicionT=true;
        AnsweraValues answeraValues = new AnsweraValues();
        boolean correcta = answeraValues.CorrectAnswer(respuestaIzquierda,respuestaDerecha,preguntaYRespuesta[3], checker);
        if (correcta){
            respuestaIzquierda.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            preguntasCount++;
            feedBack=0;
            audioControl.Correct();
            vibrateQuestion();
            scorePrint.setText("Puntaje: "+preguntasCount);
            getNewQuestion();


        } else {
            audioControl.Error();
            vibrateQuestion();
            gameOverDialog();
        }
    }

    public void derecha(){
        descicionT=true;
        AnsweraValues answeraValues = new AnsweraValues();
        boolean correcta = answeraValues.CorrectAnswer(respuestaIzquierda,respuestaDerecha,preguntaYRespuesta[3], checker);
        if (correcta){
            respuestaDerecha.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            preguntasCount++;
            feedBack=0;
            audioControl.Correct();
            vibrateQuestion();
            scorePrint.setText("Puntaje: "+preguntasCount);
            getNewQuestion();


        } else {
            audioControl.Error();
            vibrateQuestion();
            gameOverDialog();
        }
    }


    public void hideSkipButton(){
        skipQuestion.setVisibility(View.GONE);
    }


    public void onProximityDetected(SensorEvent sensorEvent){
        if (sensorEvent.values[0]<=1){
            getNewQuestion();
        } else {
        }
    }


    private void gameOverDialog(){
        final String name = dbAnswersControl.getUser();
        new AlertDialog.Builder(getContext())
                .setTitle("Juego terminado")
                .setMessage("Tu puntaje: "+ preguntasCount +"\n¿Deseas actualizar tu puntaje?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbAnswersControl.setScore(name,preguntasCount);
                        Toast.makeText(getContext(),"Puntaje Actualizado",Toast.LENGTH_SHORT);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(),"Cerrando Juego",Toast.LENGTH_SHORT);
                        getActivity().finish();
                    }
                })
                .setNeutralButton("Reiniciar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preguntasCount =0;
                        Toast.makeText(getContext(),"Reinciando Juego",Toast.LENGTH_SHORT);
                        scorePrint.setText("Puntaje: "+preguntasCount);
                        getNewQuestion();
                    }
                })
                .show();
    }


    private void getNewQuestion(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                preguntaRandom();
                descicionT=false;
            }
        },1000);
        animationFadeOut();
    }


    private void vibrateQuestion(){
        vibrator.vibrate(250);
    }

    private  void animationFadeIn(){
        pregunta.startAnimation(fadeIn);
        respuestaIzquierda.startAnimation(fadeIn);
        respuestaDerecha.startAnimation(fadeIn);
        imageView.startAnimation(fadeIn);
    }

    private void animationFadeOut(){
        pregunta.startAnimation(fadeOut);
        respuestaIzquierda.startAnimation(fadeOut);
        respuestaDerecha.startAnimation(fadeOut);
        imageView.startAnimation(fadeOut);
    }

    @Override
    public void onPause() {
        audioControl.PauseSong();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        audioControl.PlaySong();
    }


}

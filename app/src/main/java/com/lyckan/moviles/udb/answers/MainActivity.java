package com.lyckan.moviles.udb.answers;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


public class MainActivity extends FragmentActivity implements SensorEventListener,QuestionsFragment.OnFragmentInteractionListener, CanvasFragment.OnFragmentInteractionListener{
    QuestionsFragment questionsFragment;
    CanvasFragment canvasFragment;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public Sensor mProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

       questionsFragment = (QuestionsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.FrgQuestions);
       canvasFragment = (CanvasFragment) getSupportFragmentManager()
                .findFragmentById(R.id.FgrAnimation);
    }





    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mProximity,SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            canvasFragment.mShapeCustom.onSensorEvent(event);
            questionsFragment.onSensorEvent(event);

        }

        if (event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            questionsFragment.hideSkipButton();
            questionsFragment.onProximityDetected(event);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

package com.lyckan.moviles.udb.answers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.de.datos.DbAnswersControl;


public class MainMenu extends AppCompatActivity {
    final Context context = this;
    private String nombre;
    private TextView username,score,appTitle;
    EditText userInput;
    DbAnswersControl dbAnswersControl;
    Animation bounce;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar();

        dbAnswersControl = new DbAnswersControl(this,"answer",1);
        username = (TextView) findViewById(R.id.usernameField);
        score = (TextView) findViewById(R.id.lastScore);

        appTitle = (TextView) findViewById(R.id.applicationName);


        if (dbAnswersControl.getUser().equals(null)||dbAnswersControl.getUser().length()==0){
            dialogUser();
        } else {
            username.setText("Bievenido " + dbAnswersControl.getUser());
            nombre = dbAnswersControl.getUser();
            score.setText("Ultimo puntaje: "+dbAnswersControl.getScore(nombre));

        }

        bounce = AnimationUtils.loadAnimation(this,R.anim.bounce);


    }

    private void dialogUser() {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.dialog,null);

        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setView(dialogView);
        userInput = (EditText) dialogView.findViewById(R.id.editTextDialogUserInput);
        builder.setTitle("Bienvenido")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainMenu.this.finish();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button validator = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        validator.setOnClickListener(new CustomListener(alertDialog));
    }


    public void startGame(View view) {
        Intent mainGame = new Intent(MainMenu.this,MainActivity.class);
        startActivity(mainGame);
    }

    public void triggerAnimation(View view) {
            appTitle.startAnimation(bounce);
    }


    class CustomListener implements View.OnClickListener{
        private final Dialog dialog;
        public CustomListener(Dialog dialog){
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {

            validateNameField(userInput);
            /**if (!(userInput.getText().toString().equals(""))||!(userInput.getText().toString().length()==0))
            {
                nombre = userInput.getText().toString();
                dbAnswersControl.setUser(nombre);
                username.setText("Bievenido " + dbAnswersControl.getUser());
                dialog.dismiss();
            } else {
                Toast.makeText(context,"Invalid",Toast.LENGTH_LONG).show();
            }**/
        }

        private boolean validateNameField(EditText editText){
            String regexString = "^[A-Za-z\\S]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
            Pattern r = Pattern.compile(regexString);

            Matcher m =r.matcher(editText.getText());
            if (m.matches()){
                nombre = userInput.getText().toString();
                dbAnswersControl.setUser(nombre);
                username.setText("Bievenido " + dbAnswersControl.getUser());
                dialog.dismiss();

            } else {
                errorField(editText);
                editText.setError("Ingrese caracteres validos");
                return false;
            }

            return true;
        }

        void errorField(EditText editText){
            editText.setBackgroundColor(Color.rgb(255,235,238));
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        nombre = dbAnswersControl.getUser();
        score.setText("Ultimo puntaje: "+dbAnswersControl.getScore(nombre));
    }
}

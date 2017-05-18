package base.de.datos;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DbAnswersControl {
    private Context context;
    private String nombre, pregunAnte;
    private int version;
    private AnswersOpenHelper answers;
    private ArrayList<String[]> datos;
    private ArrayList<String[]> copiaDatos;

    public DbAnswersControl(Context context, String nombre, int version) {
        this.context = context;
        this.nombre= nombre;
        this.version = version;
        //Se crea la base de datos con los valores del constructor
        createDataBase();
    }

    private void createDataBase(){
        //Creamos el objeto answer que contiene la base de datos
        answers = new AnswersOpenHelper(context, nombre, null, version);
        getAnswers();
    }

    /*TODO Desconosco el efecto que tendria on resume
    Lo mas probable es que se reinicie el listado por lo que se pudiera crear un interruptor
    que solo permita el reseteo cuando se cierre e inicie la actividad.
    */
    private void getAnswers(){
        //Creamos la base de datos a partir del objeto answers
        SQLiteDatabase db = answers.getReadableDatabase();
        datos = new ArrayList<>();
        copiaDatos = new ArrayList<>();

        /*Metemos todos los datos de la base de datos en un arraylist de array
        * el arrays individuales constan de una matriz de 4 elementos
        * donde 0- ID, 1- pregunta, 2 - Imagen 3 - respuesta correcta*/
        Cursor fila = db.rawQuery("select * from answers", null);
        boolean interruptor;
        Log.d("nFilas", String.valueOf(fila.getCount()));

        // no tocar XD
        do {
            if (fila.moveToNext()) {
                String[] contenedor = new String[4];
                contenedor[0] = fila.getString(0);
                contenedor[1] = fila.getString(1);
                contenedor[2] = fila.getString(2);
                contenedor[3] = fila.getString(3);
                datos.add(contenedor);
                copiaDatos.add(contenedor);
                interruptor = true;

            } else {
                //Si no existe nos manda una notificion
                interruptor = false;
            }
        } while (interruptor);
        fila.close();
        db.close();

    }
    //TODO Hay que revisar si los numero random no dan ningun problema, no se si cuentan del 0-99 o del 1 - 100

    public boolean checkAnswers(){
        if(datos.size() > 0){
            return  true;
        }else {
            getAnswers();
            return  false;

        }
    }


    //Nos manda un arreglo aleatorio y luego lo elimina de la lista para no repetir.
    public String[] getAnswer(){
        int largoDatos = datos.size();
        Log.d("Lago", String.valueOf(datos.size()));
        int rand = (int) (Math.random() * ((largoDatos)));
        String[] answe = datos.get(rand);
        datos.remove(rand);
        pregunAnte = answe[0];
        return answe;
    }

    public String getAnswerError(){
        String[] datosT;
        Boolean interruptor;
        do {
            //Sacamos un numero random para sacar de la lista un Array
            int rand = (int) (Math.random() * ((copiaDatos.size())));
            Log.d("randomEn ERROR", String.valueOf(rand));
            datosT = copiaDatos.get(rand);

            //Comprobamos si los ID de los arreglos son iguales, si son iguales se repite el ciclo
            if (pregunAnte == datosT[0]){
                interruptor = true;
            }else{
                interruptor = false;
            }
        }while (interruptor);

        //Al encontrar id diferente nos retorna un string con la respuesta aleatoria incorrecta
        String answerError = datosT[3];
        return answerError;
    }

    public void setScore(String user, int score){
        SQLiteDatabase db = answers.getWritableDatabase();
        String consulta;
        consulta = "UPDATE users SET puntos=" + score +" WHERE nombre = '"+ user +"';";
        db.execSQL(consulta);
        db.close();
    }

    public int getScore(String user){
        SQLiteDatabase db = answers.getReadableDatabase();
        String consulta;
        int respuesta;
        consulta = "SELECT puntos FROM users where nombre ='"+ user +"' ";
        Cursor fila = db.rawQuery(consulta, null);

        if(fila.moveToFirst()){
            respuesta = fila.getInt(0);
        }else{
            Toast.makeText(context, "No existe el usuario", Toast.LENGTH_SHORT).show();
            respuesta = 0;
        }
        db.close();
        return respuesta;
    }

    public void setUser(String name){
        SQLiteDatabase db = answers.getWritableDatabase();
        String consulta = "INSERT INTO users values (1,'"+ name +"', 0);";
        try{
            db.execSQL(consulta);
            Log.d("CREAt_USERS", "Se creo el user exitosamente"); //TODO Quitar el Log al finalizar
        }catch (Exception e){
            Toast.makeText(context, "Error:" + e, Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public String getUser(){
        SQLiteDatabase db = answers.getReadableDatabase();
        String consulta = "SELECT nombre from users where id = 1";
        String respuesta = "";
        try{
            Cursor fila = db.rawQuery(consulta, null);
            if (fila.moveToFirst()){
                //Corregi el indice a tomar, tenia 1 y daba error, necesitaba el 0 al ser un valor unico en la tabla
                respuesta = fila.getString(0);
            }else {
                Log.d("GETUSERS", "Error, no existe el usuario");
            }
            fila.close();

        }catch (SQLiteException e){
            Log.d("GetUser", String.valueOf(e));
        }
        db.close();

        return respuesta;
    }

    /*TODO reiniciar el arreglo de preguntas y repuestas*/
}
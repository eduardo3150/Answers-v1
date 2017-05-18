package base.de.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class AnswersOpenHelper extends SQLiteOpenHelper {

    AnswersOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //Nos retorna una consulta para crear tabla answers

    private String answerCreate(){
        return "CREATE TABLE answers (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "pregunta TEXT,\n" +
                "imagen TEXT,\n" +
                "respuesta TEXT\n" +
                ");";
    }

    //Nos retorna una consulta para crear tabla users
    private String usersCreate(){
        return "CREATE TABLE `users` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "puntos TEXT" +
                ");";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Ejecutamos dos consulta para crear las tablas
        db.execSQL(usersCreate());
        db.execSQL(answerCreate());
        //Creamos el inflador de la base de datos y lo ejecutamos dentro de ella
        DbInflator dbInflator= new DbInflator();
        db.execSQL(dbInflator.inflatorAnswerES());
    }

    //Todo hay que crer la consulta para eliminar las preguntas pero mantener los puntajes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

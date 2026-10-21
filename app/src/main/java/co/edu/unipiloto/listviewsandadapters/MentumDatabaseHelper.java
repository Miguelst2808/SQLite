package co.edu.unipiloto.listviewsandadapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper de la base de datos SQLite "mentum".
 * Reemplaza los arreglos que antes estaban en Curso, Perfil, Evaluacion y Nota.
 *
 * Tablas:
 *   CURSO      (_id, NOMBRE, DESCRIPCION, IMAGEN)
 *   PERFIL     (_id, NOMBRE, DESCRIPCION, PAIS, IMAGEN)
 *   EVALUACION (_id, NOMBRE, DESCRIPCION, PUNTAJE, FECHA)
 *   NOTA       (_id, TITULO, ANOTACIONES)
 *
 * La llave primaria se llama "_id" porque SimpleCursorAdapter la exige.
 * IMAGEN guarda el nombre del drawable (ej. "mecanica") y no el R.drawable,
 * porque los id de recursos pueden cambiar entre compilaciones.
 */
class MentumDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mentum"; // nombre de la base de datos
    private static final int DB_VERSION = 1;        // versión de la base de datos

    MentumDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE CURSO (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NOMBRE TEXT, "
                    + "DESCRIPCION TEXT, "
                    + "IMAGEN TEXT);");
            insertCurso(db, "Mecánica avanzada", "Curso de mecánica avanzada tier 3", "mecanica");
            insertCurso(db, "IA Claude", "Curso de Prompting para Claude", "claude");
            insertCurso(db, "Estructuras de datos", "Curso de estructuras de datos tier 1", "estdatos");

            db.execSQL("CREATE TABLE PERFIL (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NOMBRE TEXT, "
                    + "DESCRIPCION TEXT, "
                    + "PAIS TEXT, "
                    + "IMAGEN TEXT);");
            insertPerfil(db, "Juan Pérez", "Estudiante de Ingeniería de Sistemas", "Colombia", "mentum_logo");
            insertPerfil(db, "María Gómez", "Profesora de Inteligencia Artificial", "España", "claude");

            db.execSQL("CREATE TABLE EVALUACION (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NOMBRE TEXT, "
                    + "DESCRIPCION TEXT, "
                    + "PUNTAJE INTEGER, "
                    + "FECHA TEXT);");
            insertEvaluacion(db, "Mecánica I", "Evaluación de primer corte de mecánica", 5, "20 de octubre de 2026");
            insertEvaluacion(db, "LLMs", "Evaluación de primer corte de IA", 3, "25 de octubre de 2026");

            db.execSQL("CREATE TABLE NOTA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "TITULO TEXT NOT NULL, "
                    + "ANOTACIONES TEXT);");
            insertNota(db, "Apuntes IA", "Revisar los conceptos de LLMs y Prompt Engineering.");
            insertNota(db, "Estructuras de Datos", "Estudiar árboles binarios y grafos para el parcial.");
        }
        // if (oldVersion < 2) { ... cambios de la versión 2 ... }
    }

    private static void insertCurso(SQLiteDatabase db, String nombre, String descripcion, String imagen) {
        ContentValues cursoValues = new ContentValues();
        cursoValues.put("NOMBRE", nombre);
        cursoValues.put("DESCRIPCION", descripcion);
        cursoValues.put("IMAGEN", imagen);
        db.insert("CURSO", null, cursoValues);
    }

    private static void insertPerfil(SQLiteDatabase db, String nombre, String descripcion,
                                     String pais, String imagen) {
        ContentValues perfilValues = new ContentValues();
        perfilValues.put("NOMBRE", nombre);
        perfilValues.put("DESCRIPCION", descripcion);
        perfilValues.put("PAIS", pais);
        perfilValues.put("IMAGEN", imagen);
        db.insert("PERFIL", null, perfilValues);
    }

    private static void insertEvaluacion(SQLiteDatabase db, String nombre, String descripcion,
                                         int puntaje, String fecha) {
        ContentValues evaluacionValues = new ContentValues();
        evaluacionValues.put("NOMBRE", nombre);
        evaluacionValues.put("DESCRIPCION", descripcion);
        evaluacionValues.put("PUNTAJE", puntaje);
        evaluacionValues.put("FECHA", fecha);
        db.insert("EVALUACION", null, evaluacionValues);
    }

    /** Inserta una nota. Devuelve el _id nuevo o -1 si no se pudo guardar. */
    static long insertNota(SQLiteDatabase db, String titulo, String anotaciones) {
        ContentValues notaValues = new ContentValues();
        notaValues.put("TITULO", titulo);
        notaValues.put("ANOTACIONES", anotaciones);
        return db.insert("NOTA", null, notaValues);
    }

    /** Actualiza las anotaciones de una nota. Devuelve cuántas filas cambiaron. */
    static int updateNota(SQLiteDatabase db, int id, String anotaciones) {
        ContentValues notaValues = new ContentValues();
        notaValues.put("ANOTACIONES", anotaciones);
        return db.update("NOTA", notaValues, "_id = ?", new String[]{Integer.toString(id)});
    }
}

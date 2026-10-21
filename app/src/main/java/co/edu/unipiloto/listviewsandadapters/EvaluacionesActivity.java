package co.edu.unipiloto.listviewsandadapters;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class EvaluacionesActivity extends Activity {

    public static final String EXTRA_EVALUACIONID = "evaluacionId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluaciones2);

        int evaluacionId = getIntent().getIntExtra(EXTRA_EVALUACIONID, 0);

        SQLiteOpenHelper mentumDatabaseHelper = new MentumDatabaseHelper(this);
        try (SQLiteDatabase db = mentumDatabaseHelper.getReadableDatabase();
             Cursor cursor = db.query("EVALUACION",
                     new String[]{"NOMBRE", "DESCRIPCION", "PUNTAJE", "FECHA"},
                     "_id = ?",
                     new String[]{Integer.toString(evaluacionId)},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                TextView nombre = findViewById(R.id.nombre);
                nombre.setText(cursor.getString(0));
                TextView descripcion = findViewById(R.id.descripcion);
                descripcion.setText(cursor.getString(1));
                TextView puntaje = findViewById(R.id.puntaje);
                puntaje.setText(String.valueOf(cursor.getInt(2)));
                TextView fecha = findViewById(R.id.fecha);
                fecha.setText(cursor.getString(3));
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }
    }
}

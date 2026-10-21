package co.edu.unipiloto.listviewsandadapters;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CursosActivity extends Activity {
    public static final String EXTRA_CURSOID = "cursoId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos2);

        // _id del curso seleccionado en la lista
        int cursoId = getIntent().getIntExtra(EXTRA_CURSOID, 0);

        SQLiteOpenHelper mentumDatabaseHelper = new MentumDatabaseHelper(this);
        try (SQLiteDatabase db = mentumDatabaseHelper.getReadableDatabase();
             Cursor cursor = db.query("CURSO",
                     new String[]{"NOMBRE", "DESCRIPCION", "IMAGEN"},
                     "_id = ?",
                     new String[]{Integer.toString(cursoId)},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                String nombreText = cursor.getString(0);
                String descripcionText = cursor.getString(1);
                String imagen = cursor.getString(2);

                TextView nombre = findViewById(R.id.nombre);
                nombre.setText(nombreText);
                TextView descripcion = findViewById(R.id.descripcion);
                descripcion.setText(descripcionText);
                ImageView foto = findViewById(R.id.foto);
                foto.setImageResource(Imagenes.drawable(this, imagen));
                foto.setContentDescription(nombreText);
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }
    }
}

package co.edu.unipiloto.listviewsandadapters;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** Detalle de una nota. Las anotaciones se pueden editar y guardar en SQLite. */
public class NotasActivity extends Activity {

    public static final String EXTRA_NOTAID = "notaId";

    private SQLiteOpenHelper mentumDatabaseHelper;
    private int notaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas2);

        notaId = getIntent().getIntExtra(EXTRA_NOTAID, 0);
        mentumDatabaseHelper = new MentumDatabaseHelper(this);

        TextView titulo = findViewById(R.id.titulo);
        EditText anotaciones = findViewById(R.id.anotaciones);

        try (SQLiteDatabase db = mentumDatabaseHelper.getReadableDatabase();
             Cursor cursor = db.query("NOTA",
                     new String[]{"TITULO", "ANOTACIONES"},
                     "_id = ?",
                     new String[]{Integer.toString(notaId)},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                titulo.setText(cursor.getString(0));
                anotaciones.setText(cursor.getString(1));
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }

        Button btnGuardar = findViewById(R.id.button_guardar_cambios);
        btnGuardar.setOnClickListener(v -> {
            try (SQLiteDatabase db = mentumDatabaseHelper.getWritableDatabase()) {
                int filas = MentumDatabaseHelper.updateNota(db, notaId,
                        anotaciones.getText().toString().trim());
                Toast.makeText(this, filas > 0 ? R.string.nota_actualizada : R.string.nota_no_guardada,
                        Toast.LENGTH_SHORT).show();
            } catch (SQLiteException e) {
                Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

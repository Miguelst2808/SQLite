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

public class PerfilActivity extends Activity {

    public static final String EXTRA_PERFILID = "perfilId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil2);

        int perfilId = getIntent().getIntExtra(EXTRA_PERFILID, 0);

        SQLiteOpenHelper mentumDatabaseHelper = new MentumDatabaseHelper(this);
        try (SQLiteDatabase db = mentumDatabaseHelper.getReadableDatabase();
             Cursor cursor = db.query("PERFIL",
                     new String[]{"NOMBRE", "DESCRIPCION", "PAIS", "IMAGEN"},
                     "_id = ?",
                     new String[]{Integer.toString(perfilId)},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                String nombreText = cursor.getString(0);

                TextView nombre = findViewById(R.id.nombre);
                nombre.setText(nombreText);
                TextView descripcion = findViewById(R.id.descripcion);
                descripcion.setText(cursor.getString(1));
                TextView pais = findViewById(R.id.pais);
                pais.setText(cursor.getString(2));
                ImageView foto = findViewById(R.id.foto);
                foto.setImageResource(Imagenes.drawable(this, cursor.getString(3)));
                foto.setContentDescription(nombreText);
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }
    }
}

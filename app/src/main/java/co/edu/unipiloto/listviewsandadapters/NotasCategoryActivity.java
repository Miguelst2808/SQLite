package co.edu.unipiloto.listviewsandadapters;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/** Lista de notas guardadas en SQLite. Desde aquí se abre el formulario para agregar una nota. */
public class NotasCategoryActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);
        ListView listNotas = findViewById(R.id.list_notas);

        SQLiteOpenHelper mentumDatabaseHelper = new MentumDatabaseHelper(this);
        try {
            db = mentumDatabaseHelper.getReadableDatabase();
            cursor = consultarNotas();
            listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"TITULO"},
                    new int[]{android.R.id.text1},
                    0);
            listNotas.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listNotas, View itemView, int position, long id) {
                Intent intent = new Intent(NotasCategoryActivity.this, NotasActivity.class);
                intent.putExtra(NotasActivity.EXTRA_NOTAID, (int) id);
                startActivity(intent);
            }
        };
        listNotas.setOnItemClickListener(itemClickListener);

        Button btnAgregar = findViewById(R.id.button_agregar_nota);
        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(NotasCategoryActivity.this, NotaFormActivity.class)));
    }

    private Cursor consultarNotas() {
        return db.query("NOTA",
                new String[]{"_id", "TITULO"},
                null, null, null, null, "_id DESC");
    }

    /** Al volver del formulario o del detalle se vuelve a consultar para ver los cambios. */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (db == null || listAdapter == null) return;
        try {
            Cursor nuevoCursor = consultarNotas();
            listAdapter.changeCursor(nuevoCursor); // cierra el cursor anterior
            cursor = nuevoCursor;
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (db != null) db.close();
    }
}

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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class CursosCategoryActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);
        ListView listCursos = findViewById(R.id.list_cursos);

        SQLiteOpenHelper mentumDatabaseHelper = new MentumDatabaseHelper(this);
        try {
            db = mentumDatabaseHelper.getReadableDatabase();
            cursor = db.query("CURSO",
                    new String[]{"_id", "NOMBRE"},
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NOMBRE"},
                    new int[]{android.R.id.text1},
                    0);
            listCursos.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listCursos, View itemView, int position, long id) {
                // id es el _id del registro en la base de datos
                Intent intent = new Intent(CursosCategoryActivity.this, CursosActivity.class);
                intent.putExtra(CursosActivity.EXTRA_CURSOID, (int) id);
                startActivity(intent);
            }
        };
        listCursos.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (db != null) db.close();
    }
}

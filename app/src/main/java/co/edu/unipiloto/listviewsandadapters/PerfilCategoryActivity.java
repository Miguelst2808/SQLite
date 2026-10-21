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

public class PerfilCategoryActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        ListView listPerfil = findViewById(R.id.list_perfil);

        SQLiteOpenHelper mentumDatabaseHelper = new MentumDatabaseHelper(this);
        try {
            db = mentumDatabaseHelper.getReadableDatabase();
            cursor = db.query("PERFIL",
                    new String[]{"_id", "NOMBRE"},
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NOMBRE"},
                    new int[]{android.R.id.text1},
                    0);
            listPerfil.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listPerfil, View itemView, int position, long id) {
                Intent intent = new Intent(PerfilCategoryActivity.this, PerfilActivity.class);
                intent.putExtra(PerfilActivity.EXTRA_PERFILID, (int) id);
                startActivity(intent);
            }
        };
        listPerfil.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (db != null) db.close();
    }
}

package co.edu.unipiloto.listviewsandadapters;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Formulario de captura de notas.
 * Botón "Guardar nota": inserta la nota en SQLite.
 * Botón "Ver notas guardadas": consulta las notas de SQLite en la lista.
 */
public class NotaFormActivity extends Activity {

    private SQLiteOpenHelper mentumDatabaseHelper;
    private EditText editTitulo;
    private EditText editAnotaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_form);

        mentumDatabaseHelper = new MentumDatabaseHelper(this);
        editTitulo = findViewById(R.id.edit_titulo);
        editAnotaciones = findViewById(R.id.edit_anotaciones);

        Button btnGuardar = findViewById(R.id.button_guardar_nota);
        btnGuardar.setOnClickListener(v -> guardarNota());

        Button btnVer = findViewById(R.id.button_ver_notas);
        btnVer.setOnClickListener(v -> {
            Intent intent = new Intent(NotaFormActivity.this, NotasCategoryActivity.class);
            // Si la lista ya está abierta, vuelve a ella en lugar de abrir otra
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void guardarNota() {
        String titulo = editTitulo.getText().toString().trim();
        String anotaciones = editAnotaciones.getText().toString().trim();
        if (TextUtils.isEmpty(titulo)) {
            editTitulo.setError(getString(R.string.error_titulo));
            return;
        }

        try (SQLiteDatabase db = mentumDatabaseHelper.getWritableDatabase()) {
            long id = MentumDatabaseHelper.insertNota(db, titulo, anotaciones);
            if (id != -1) {
                Toast.makeText(this, getString(R.string.nota_guardada, id), Toast.LENGTH_SHORT).show();
                editTitulo.setText("");
                editAnotaciones.setText("");
                editTitulo.requestFocus();
            } else {
                Toast.makeText(this, R.string.nota_no_guardada, Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.db_no_disponible, Toast.LENGTH_SHORT).show();
        }
    }
}

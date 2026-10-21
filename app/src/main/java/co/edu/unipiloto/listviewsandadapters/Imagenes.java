package co.edu.unipiloto.listviewsandadapters;

import android.content.Context;

/** Convierte el nombre guardado en la columna IMAGEN en el id del drawable. */
final class Imagenes {

    private Imagenes() {
    }

    static int drawable(Context context, String nombre) {
        int id = nombre == null ? 0
                : context.getResources().getIdentifier(nombre, "drawable", context.getPackageName());
        return id != 0 ? id : R.drawable.mentum_logo;
    }
}

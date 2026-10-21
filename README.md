# Mentum: ListViews, Adapters y SQLite

Laboratorio de base de datos SQLite: extiende el proyecto de ListView y adaptadores para que los datos salgan de una base de datos SQLite y no de arreglos en clases Java (patrón Starbuzz visto en clase).

## Qué cambió

| Antes | Ahora |
|---|---|
| Arreglos estáticos en `Curso`, `Perfil`, `Evaluacion` y `Nota` | Tablas `CURSO`, `PERFIL`, `EVALUACION` y `NOTA` en la base `mentum` |
| `ArrayAdapter` en las actividades de categoría | `SimpleCursorAdapter` con un `Cursor` de `db.query(...)` |
| El detalle tomaba el elemento por posición del arreglo | El detalle consulta la base por `_id` (`"_id = ?"`) |
| Notas de solo lectura | Formulario para agregar notas y botón para guardar cambios |

## Puntos del laboratorio

1. **Entidades que persisten en SQLite:** las cuatro tablas, creadas en `MentumDatabaseHelper` (extiende `SQLiteOpenHelper`, `DB_NAME = "mentum"`, `DB_VERSION = 1`). `onCreate` y `onUpgrade` llaman a `updateMyDatabase(db, oldVersion, newVersion)` y se cargan datos de prueba con `ContentValues` + `db.insert`.
2. **Formulario de captura:** `NotaFormActivity` (`activity_nota_form.xml`), que se abre con **Agregar nota** en la lista de notas.
3. **Botón de adición:** **Guardar nota** inserta la nota en SQLite (`MentumDatabaseHelper.insertNota`).
4. **Botón de consulta:** **Ver notas guardadas** abre la lista que consulta SQLite. Las listas de Cursos, Perfil y Evaluaciones también se consultan desde la base.

Extra: en el detalle de una nota, **Guardar cambios** actualiza las anotaciones (`db.update`).

## Detalles técnicos

- La llave primaria se llama `_id` porque `SimpleCursorAdapter` la necesita.
- La columna `IMAGEN` guarda el nombre del drawable (`"mecanica"`) y se convierte con `getIdentifier`, porque los `R.drawable` pueden cambiar entre compilaciones.
- Errores de base de datos: `catch (SQLiteException e)` con el Toast "Base de datos no disponible".
- Las actividades de lista cierran el `Cursor` y la base en `onDestroy()`.

## Ver la base de datos

Con la app corriendo en el emulador: **View → Tool Windows → Device Explorer → `/data/data/co.edu.unipiloto.listviewsandadapters/databases/mentum`**, o **App Inspection → Database Inspector**.

> Si cambian la estructura de las tablas, suban `DB_VERSION` y agreguen un bloque `if (oldVersion < 2)` en `updateMyDatabase`, o desinstalen la app del emulador para que la base se cree de nuevo.

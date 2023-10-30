package com.dispositivos_moviles.practica_32;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Actividad> actividades; //Lista con las Actividades
    private ArrayAdapter<Actividad> adapter; //Adaptador para mostrar la lista en el ListView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvActividades = (ListView) findViewById(R.id.lvCompra);
        Button bAdd = (Button) findViewById(R.id.button2);

        //Habilitamos un listener del boton para poder añadir una Actividad
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActividad();
            }
        });

        //Mostramos el ListView con las actividades
        actividades = new ArrayList<Actividad>();
        adapter = new ArrayAdapter<Actividad>(this, android.R.layout.simple_list_item_1, actividades);

        lvActividades.setAdapter(adapter);

        //Habilitamos la pulsación larga para editar las Actividades creadas
        lvActividades.setLongClickable(true);
        registerForContextMenu(lvActividades);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo cmi) {
        super.onCreateContextMenu(menu, view, cmi);

        if(view.getId() == R.id.lvCompra) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Actividad actividad = adapter.getItem(info.position);

        switch (item.getItemId()) {
            case R.id.context_borrar:
                borrarActividad(actividad);
                return true;

            case R.id.context_editar:
                editarActividad(actividad);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }




    //Metoodo para añadir una nueva Actividad
    private void addActividad() {
        final EditText edText = new EditText(this); // Para que el usuario introduzca el nombre de la nueva actividad
        final Calendar calendar = Calendar.getInstance(); // Para almacenar la fecha límite

        // Almacenamos la fecha actual para que sea la que se muestre al abrir el DatePickerDialog
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Creamos un AlertDialog para que introduzca el nombre de la nueva Actividad
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nueva Actividad");
        builder.setMessage("Escriba el nombre de la nueva actividad");

        // Agregar un EditText para que introduzca el nombre
        builder.setView(edText);

        //Creamos un DatePickerDialog para seleccionar la fecha límite, lo mostraremos al pulsar el boton de crear
        DatePickerDialog elegirFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Guardamos en el objeto calendar la fecha que ha introducido el usuario
                calendar.set(year, month, dayOfMonth);

                String nombre = edText.getText().toString();
                // Nos aseguramos de que haya introducido un nombre
                if (!nombre.isEmpty()) {
                    // Creamos la actividad y la añadimos a la lista
                    Actividad nuevaActividad = new Actividad(nombre, calendar.getTime());
                    actividades.add(nuevaActividad);
                    adapter.notifyDataSetChanged();

                    // Actualizamos el estado
                    updateStatus();
                } else {
                    Toast.makeText(getApplicationContext(), "Debes escribir un nombre", Toast.LENGTH_LONG).show();
                }
            }
        }, currentYear, currentMonth, currentDay);


        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Mostramos el DatePickerDialog
                elegirFecha.setTitle("Fecha de Caducidad");
                elegirFecha.setMessage("Selecciona la fecha de caducidad de la Actividad");
                elegirFecha.show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    //Codigo para modificar Actividad, similar al de addActividad
    private void editarActividad(Actividad actividad){
        final EditText edText = new EditText(this); // Para que el usuario introduzca el nuevo nombre
        final Calendar calendar = Calendar.getInstance(); // Para almacenar la nueva fecha límite

        // Almacenamos la fecha del objeto para que sea la que se muestre al abrir el DatePickerDialog
        calendar.setTime(actividad.getFechaLim());
        int oldYear = calendar.get(Calendar.YEAR);
        int oldMonth = calendar.get(Calendar.MONTH);
        int oldDay = calendar.get(Calendar.DAY_OF_MONTH);

        //Mostramos el nombre actual
        edText.setText(actividad.getNombre());

        // Creamos un AlertDialog para que introduzca el nuevo nombre de la Actividad
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(actividad.getNombre());
        builder.setMessage("Escriba el nuevo nombre");

        // Agregar un EditText para que introduzca el nombre
        builder.setView(edText);

        //Creamos un DatePickerDialog para seleccionar la fecha límite, lo mostraremos al pulsar el boton de crear
        DatePickerDialog elegirFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Guardamos en el objeto calendar la fecha que ha introducido el usuario
                calendar.set(year, month, dayOfMonth);

                String nombre = edText.getText().toString();
                // Nos aseguramos de que haya introducido un nombre
                if (!nombre.isEmpty()) {
                    //Actualizamos la actividad
                    actividad.setNombre(nombre);
                    actividad.setFechaLim(calendar.getTime());
                    adapter.notifyDataSetChanged();

                    // Actualizamos el estado
                    updateStatus();
                } else {
                    Toast.makeText(getApplicationContext(), "Debes escribir un nombre", Toast.LENGTH_LONG).show();
                }
            }
        }, oldYear, oldMonth, oldDay);


        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Mostramos el DatePickerDialog
                elegirFecha.setTitle("Fecha de Caducidad");
                elegirFecha.setMessage("Selecciona la fecha de caducidad de la Actividad");
                elegirFecha.show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    //Codigo para eliminar Actividad
    private void borrarActividad(Actividad actividad){
        actividades.remove(actividad);
        adapter.notifyDataSetChanged();
        updateStatus();
    }

    //Actualiza el numero de actividades
    private void updateStatus(){
        TextView tvCantidad = (TextView) findViewById(R.id.tvCantidad);
        int cantidad = adapter.getCount();

        tvCantidad.setText(Integer.toString(cantidad));
    }

    //Cuando la app pasa a segundo plano, haremos las comprobaciones de las fechas
    protected void onResume(){
        super.onResume();

        //Obtenemos la fecha actual
        Date fechaActual = new Date();

        //Recorremos la lista de las actividades para comprobar sus fechas
        for(Actividad actividad: actividades){
            Date fechaLimite = actividad.getFechaLim();
            if(fechaActual.after(fechaLimite)){
                mostrarAdvertencia(actividad);
            }
        }
    }


    //Mostraremos los cuadros de dialogo para advertir de que actividades han caducado
    private void mostrarAdvertencia(Actividad actividad){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ADVERTENCIA");
        builder.setMessage("La actividad \"" + actividad.getNombre() + "\" ha caducado");
        builder.setNegativeButton("Aceptar", null);
        builder.create().show();
    }
}
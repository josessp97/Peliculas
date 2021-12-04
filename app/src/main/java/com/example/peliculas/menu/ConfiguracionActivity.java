package com.example.peliculas.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.peliculas.R;

public class ConfiguracionActivity extends AppCompatActivity {
    private EditText editText1,editText2,editText3;
    private Button btnAceptar;
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        this.setTitle(R.string.tituloConfig);

        editText1=findViewById(R.id.editText1);
        editText2=findViewById(R.id.editText2);
        editText3=findViewById(R.id.editText3);
        btnAceptar=findViewById(R.id.btnAceptar);

        // Storing data into SharedPreferences
        preferencias = getSharedPreferences("Configuracion", MODE_PRIVATE);

        editText1.setText(preferencias.getString("API_KEY", editText1.getText().toString()));
        editText2.setText(preferencias.getString("Endpoint peliculas", editText2.getText().toString()));
        editText3.setText(preferencias.getString("Endpoint creditos", editText3.getText().toString()));

        btnAceptar.setOnClickListener(e->{
            // Guardar preferencias y salir
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("API_KEY", editText1.getText().toString());
            editor.putString("Endpoint peliculas", editText2.getText().toString());
            editor.putString("Endpoint creditos", editText3.getText().toString());
            editor.commit();

            Toast t = Toast.makeText(getApplicationContext(), "Configuración guardada correctamente.", Toast.LENGTH_SHORT);
            t.show();

            finish();
        });
    }
}
package com.example.peliculas.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peliculas.R;

public class AcercaDeActivity extends AppCompatActivity {
    private ImageView imageViewUsuario;
    private TextView textViewMiNombre,textViewMiInsti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        this.setTitle(R.string.tituloAcercaDe);

        imageViewUsuario=findViewById(R.id.imageViewUsuario);
        textViewMiInsti=findViewById(R.id.textViewInsti);
        textViewMiNombre=findViewById(R.id.textViewMiNombre);

    }
}
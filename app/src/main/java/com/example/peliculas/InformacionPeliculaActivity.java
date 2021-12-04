package com.example.peliculas;

import static com.example.peliculas.MainActivity.MOVIE_BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InformacionPeliculaActivity extends AppCompatActivity {
    private TextView lblTitulo,lblPoster_path,lblOverview;
    private ImageView imagePelicula;
    private Button btnCreditos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_pelicula);

        lblTitulo=findViewById(R.id.lblTitulo);
        imagePelicula=findViewById(R.id.imagePelicula);
        lblOverview=findViewById(R.id.lblOverview);
        btnCreditos=findViewById(R.id.btnCreditos);

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        //Construimos el mensaje a mostrar
        lblTitulo.setText(""+bundle.get("primerCampo"));
        Picasso.get().load(MOVIE_BASE_URL+bundle.get("segundoCampo")).into(imagePelicula);
        lblOverview.setText("" + bundle.get("tercerCampo"));

        btnCreditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PARA INICIAR LA OTRA ACTIVIDAD
                Intent intent = new Intent(InformacionPeliculaActivity.this, ActivityActores.class);
                //inicio la nueva actividad
                startActivity(intent);
            }
        });
    }
}
package com.example.peliculas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peliculas.menu.AcercaDeActivity;
import com.example.peliculas.menu.ConfiguracionActivity;
import com.example.peliculas.objects.Pelicula;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String endPointPeliculas="http://api.themoviedb.org/3/discover/movie?api_key=1865f43a0549ca50d341dd9ab8b29f49&language=es";
    public static final String MOVIE_BASE_URL="https://image.tmdb.org/t/p/w185";
    private ArrayList<Pelicula> listaPeliculas = new ArrayList();
    private ListView listViewPeliculas;
    private JSONObject resp = null;
    private JSONArray peliculas = null;
    private TextView prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prueba=findViewById(R.id.textViewPrueba);
        listViewPeliculas=findViewById(R.id.listViewPeliculas);

        new ObtenerPeliculasAsync().execute(endPointPeliculas);

        //Eventos
        listViewPeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Alternativa 1:
                try {
                    //por si pinchamos en la cabecera, que da excepcion
                    String titulo = ((Pelicula) a.getItemAtPosition(position)).getTitle();
                    String poster_path=((Pelicula) a.getItemAtPosition(position)).getPoster_path();
                    String overview=((Pelicula) a.getItemAtPosition(position)).getOverview();

                    //creo el intent
                    Intent intent = new Intent(MainActivity.this, InformacionPeliculaActivity.class);
                    //creo la informacion a pasar entre actividades
                    Bundle b=new Bundle();
                    b.putString("primerCampo", titulo);
                    b.putString("segundoCampo", poster_path);
                    b.putString("tercerCampo", overview);

                    //añado la informacion al intent
                    intent.putExtras(b);

                    //inicio la nueva actividad
                    startActivity(intent);
                }

                catch(Exception e){}
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //EVENTO MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOpcConfiguracion:
                //creo el intent
                Intent intent = new Intent(getApplicationContext(), ConfiguracionActivity.class);
                //inicio la nueva actividad
                startActivity(intent);
                return true;
            case R.id.menuOpcAcercaDe:
                //creo el intent
                intent = new Intent(getApplicationContext(), AcercaDeActivity.class);
                //inicio la nueva actividad
                startActivity(intent);
                return true;
            case R.id.menuOpcSalir:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ObtenerPeliculasAsync extends AsyncTask<String, Integer, String> {

        //Mostrar progress bar.
        ProgressDialog progreso = new ProgressDialog(MainActivity.this);

        protected void onPreExecute (){
            super.onPreExecute();

            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Obteniendo peliculas ...");
            progreso.setCancelable(false);
            progreso.setMax(100);
            progreso.setProgress(0);
            progreso.show();
        }

        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try{
                URL urlObj = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String linea;

                while ((linea = reader.readLine()) != null) result.append(linea);

                Log.d("test", "respuesta: " + result.toString());

            } catch (Exception e) {
                Log.d("test", "error2: " + e.toString());
            }

            return result.toString();
        }

        protected void onProgressUpdate(Integer...a){
            super.onProgressUpdate(a);
        }

        protected void onPostExecute(String result) {

                try {
                resp = new JSONObject(result);
                peliculas = resp.getJSONArray("results");

                for (int i=0; i < peliculas.length(); i++) {
                    JSONObject pelicula = peliculas.getJSONObject(i);

                    //prueba.setText(pelicula.getString("poster_path"));
                    listaPeliculas.add(new Pelicula(
                            pelicula.getString("title"),
                            pelicula.getString("backdrop_path"),
                            pelicula.getString("poster_path"),
                            pelicula.getString("overview"),
                            pelicula.getString("release_date")
                    ));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            progreso.dismiss();
            //CAMBIANDO ADAPTADOR
            AdaptadorPelicula adaptador = new AdaptadorPelicula(MainActivity.this, listaPeliculas);
            listViewPeliculas.setAdapter(adaptador);
        }

    }

    class AdaptadorPelicula extends BaseAdapter {
        Context context;
        ArrayList<Pelicula> arrayList;

        public AdaptadorPelicula(Context context, ArrayList<Pelicula> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        public int getCount() {
            return arrayList.size();
        }

        public Pelicula getItem(int position) {
            return arrayList.get(position);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView ==  null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.lista_peliculas, parent, false);
            }

            // fecha
            TextView fecha = (TextView) convertView.findViewById(R.id.tvFecha);
            fecha.setText(arrayList.get(position).getRelease_date());

            // titulo
            TextView name = (TextView) convertView.findViewById(R.id.tvTitle);
            name.setText(arrayList.get(position).getTitle());

            // descripcion
            TextView descripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
            descripcion.setText(arrayList.get(position).getOverview().substring(0,90) + " ... ");

            // Imagen.
            ImageView imagen = (ImageView) convertView.findViewById(R.id.list_image);
            Picasso.get().load(MOVIE_BASE_URL + arrayList.get(position).getBackdrop_path()).into(imagen);
            imagen.setScaleType(ImageView.ScaleType.FIT_XY);

            return convertView;
        }
    }
}




package com.example.peliculas;

import static com.example.peliculas.MainActivity.MOVIE_BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peliculas.objects.Creditos;
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

public class ActivityActores extends AppCompatActivity {
    public static final String endPointCreditos="https://api.themoviedb.org/3/movie/580489/credits?api_key=1865f43a0549ca50d341dd9ab8b29f49&language=enES&credit_id=580489";
    private ArrayList<Creditos> listaCreditos = new ArrayList();
    private ListView listViewPeliculas;
    private JSONObject resp = null;
    private JSONArray creditos = null;
    private TextView prueba;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle(R.string.actores);

        listViewPeliculas=findViewById(R.id.listViewPeliculas);
        prueba=findViewById(R.id.textViewPrueba);

        new ObtenerPeliculasAsync().execute(endPointCreditos);

        //Recuperamos la información pasada en el intent
        bundle = this.getIntent().getExtras();


        /*//Construimos el mensaje a mostrar
        lblActor1.setText(""+bundle.get("primerCampo"));
        Picasso.get().load(MOVIE_BASE_URL+bundle.get("segundoCampo")).into(imageActor);
        lblActor2.setText("" + bundle.get("tercerCampo"));*/


    }

    class ObtenerPeliculasAsync extends AsyncTask<String, Integer, String> {

        //Mostrar progress bar.
        ProgressDialog progreso = new ProgressDialog(ActivityActores.this);

        protected void onPreExecute (){
            super.onPreExecute();

            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Obteniendo Actores ...");
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
                creditos = resp.getJSONArray("cast");

                for (int i=0; i < creditos.length(); i++) {
                    JSONObject credito = creditos.getJSONObject(i);
                    //prueba.setText(credito.getString("profile_path"));
                    listaCreditos.add(new Creditos(
                            credito.getString("name"),
                            credito.getString("original_name"),
                            credito.getString("profile_path")
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
            AdaptadorCreditos adaptador = new AdaptadorCreditos(ActivityActores.this, listaCreditos);
            listViewPeliculas.setAdapter(adaptador);
        }
    }

    class AdaptadorCreditos extends BaseAdapter {
        Context context;
        ArrayList<Creditos> arrayList;

        public AdaptadorCreditos(Context context, ArrayList<Creditos> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        public int getCount() {
            return arrayList.size();
        }

        public Creditos getItem(int position) {
            return arrayList.get(position);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView ==  null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.lista_creditos, parent, false);
            }

            // nombre
            TextView name = (TextView) convertView.findViewById(R.id.tvName);
            name.setText(arrayList.get(position).getName());

            // nombre2
            TextView name2 = (TextView) convertView.findViewById(R.id.tvName2);
            name2.setText(arrayList.get(position).getOriginal_name());

            // Imagen.
            ImageView imagen = (ImageView) convertView.findViewById(R.id.list_image2);
            Picasso.get().load(MOVIE_BASE_URL + arrayList.get(position).getProfile_path()).into(imagen);
            imagen.setScaleType(ImageView.ScaleType.FIT_XY);

            return convertView;
        }
    }
}
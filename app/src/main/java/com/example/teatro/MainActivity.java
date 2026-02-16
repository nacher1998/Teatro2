package com.example.teatro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    // Tus credenciales de Supabase
    private static final String SUPABASE_URL = "https://mhofxrmxsegjzssutzru.supabase.co";
    private static final String SUPABASE_KEY = "sb_secret_Kvssja_b25MU2zlsUAumsg_sV8pqwPQ";

    private TextView tvEstado;
    private Button btnCargar;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos las vistas del XML
        tvEstado = findViewById(R.id.tvEstado);
        btnCargar = findViewById(R.id.btnCargar);

        // Inicializamos el cliente HTTP
        client = new OkHttpClient();

        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEstado.setText("Cargando...");
                obtenerDatosDeSupabase();
            }
        });
    }

    private void obtenerDatosDeSupabase() {
        // Apuntamos a la tabla "usuarios" usando la API REST de Supabase (PostgREST)
        // El "?select=*" significa que queremos traer todas las columnas
        String url = SUPABASE_URL + "/rest/v1/usuarios?select=*";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .build();

        // Hacemos la petición en un hilo secundario para no bloquear la app
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                actualizarTextoEnPantalla("Error de conexión: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String respuestaJson = response.body().string();

                    try {
                        // Supabase devuelve un Array de JSON.
                        JSONArray jsonArray = new JSONArray(respuestaJson);
                        int cantidadRegistros = jsonArray.length();
                        actualizarTextoEnPantalla("¡Éxito! Se encontraron " + cantidadRegistros + " registros.");

                        // Aquí podrías usar Gson para convertir respuestaJson en objetos de Java reales.

                    } catch (JSONException e) {
                        actualizarTextoEnPantalla("Error al leer los datos.");
                    }
                } else {
                    actualizarTextoEnPantalla("Error en Supabase: " + response.code());
                }
            }
        });
    }

    // Android requiere que los cambios visuales se hagan en el hilo principal
    private void actualizarTextoEnPantalla(final String texto) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvEstado.setText(texto);
            }
        });
    }
}
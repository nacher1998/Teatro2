package com.example.teatro; // ¡IMPORTANTE! Cambia esto por el nombre de tu paquete (ej: com.example.teatro2)

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EventosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Vinculamos esta clase con el XML que acabamos de crear
        setContentView(R.layout.activity_eventos);

        // Configuramos el botón de volver
        TextView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esto cierra esta pantalla y vuelve a la anterior (MainActivity)
                finish();
            }
        });
        // Ejemplo para el día 5
        TextView day5 = findViewById(R.id.day5);
        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí podrías cambiar el texto de "Jue, 17 Agosto" por el del día pulsado
            }
        });
    }
}
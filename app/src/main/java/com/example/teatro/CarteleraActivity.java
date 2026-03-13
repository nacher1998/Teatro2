/**package com.example.teatro;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;

public class CarteleraActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private ImageButton btnEditDate;
    private MaterialButton btnPrevMonth;
    private MaterialButton btnNextMonth;
    private TextView tvMonthYear;
    private RecyclerView recyclerViewCalendar;
    private RecyclerView recyclerViewEventos;
    private ImageButton btnBack;

    private CalendarAdapter calendarAdapter;
    private EventoAdapter eventoAdapter;

    private Calendar currentCalendar;
    private Calendar selectedCalendar;
    private List<Evento> todosLosEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartelera);

        initViews();
        initCalendar();
        loadEventos();
        setupListeners();
        updateUI();
    }

    private void initViews() {
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnEditDate = findViewById(R.id.btnEditDate);
        btnPrevMonth = findViewById(R.id.btnPrevMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        tvMonthYear = findViewById(R.id.tvMonthYear);
        recyclerViewCalendar = findViewById(R.id.recyclerViewCalendar);
        recyclerViewEventos = findViewById(R.id.recyclerViewEventos);
        btnBack = findViewById(R.id.btnBack);

        // Setup RecyclerViews
        recyclerViewCalendar.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initCalendar() {
        currentCalendar = Calendar.getInstance();
        selectedCalendar = Calendar.getInstance();

        calendarAdapter = new CalendarAdapter(this, new CalendarAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(Calendar calendar) {
                selectedCalendar = (Calendar) calendar.clone();
                updateUI();
                scrollToSelectedDate();
            }
        });

        recyclerViewCalendar.setAdapter(calendarAdapter);
    }

    private void loadEventos() {
        // Aquí deberías cargar los eventos desde tu base de datos o API
        // Este es un ejemplo con datos de prueba
        todosLosEventos = new ArrayList<>();

        Calendar cal1 = Calendar.getInstance();
        cal1.set(2025, 7, 17, 14, 0); // 17 de Agosto, 14:00
        todosLosEventos.add(new Evento(
                "El lago de los cisnes",
                "New Adventures",
                "Sala Gayarre",
                cal1.getTime(),
                "https://ejemplo.com/imagen1.jpg"
        ));

        Calendar cal2 = Calendar.getInstance();
        cal2.set(2025, 7, 17, 16, 0); // 17 de Agosto, 16:00
        todosLosEventos.add(new Evento(
                "Ludovico Einaudi",
                "Solo Piano",
                "Sala Principal",
                cal2.getTime(),
                "https://ejemplo.com/imagen2.jpg"
        ));

        Calendar cal3 = Calendar.getInstance();
        cal3.set(2025, 7, 17, 18, 30); // 17 de Agosto, 18:30
        todosLosEventos.add(new Evento(
                "Paco Ibánez",
                "Vivencias",
                "Sala Gayarre",
                cal3.getTime(),
                "https://ejemplo.com/imagen3.jpg"
        ));

        Calendar cal4 = Calendar.getInstance();
        cal4.set(2025, 7, 17, 20, 30); // 17 de Agosto, 20:30
        todosLosEventos.add(new Evento(
                "Romeo y Julieta",
                "Charles Gounod",
                "Sala Principal",
                cal4.getTime(),
                "https://ejemplo.com/imagen4.jpg"
        ));

        // Agregar más eventos para otras fechas si es necesario
        Calendar cal5 = Calendar.getInstance();
        cal5.set(2025, 7, 5, 19, 0); // 5 de Agosto, 19:00
        todosLosEventos.add(new Evento(
                "Concierto de Verano",
                "Orquesta Sinfónica",
                "Sala Principal",
                cal5.getTime(),
                "https://ejemplo.com/imagen5.jpg"
        ));

        eventoAdapter = new EventoAdapter(this, new ArrayList<>());
        recyclerViewEventos.setAdapter(eventoAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnEditDate.setOnClickListener(v -> showDatePicker());

        btnPrevMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateUI();
        });

        btnNextMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateUI();
        });
    }

    private void updateUI() {
        // Actualizar fecha seleccionada
        SimpleDateFormat sdfDate = new SimpleDateFormat("EEE, dd MMMM", new Locale("es", "ES"));
        tvSelectedDate.setText(sdfDate.format(selectedCalendar.getTime()));

        // Actualizar mes y año
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        String monthYear = sdfMonth.format(currentCalendar.getTime());
        tvMonthYear.setText(monthYear.substring(0, 1).toUpperCase() + monthYear.substring(1));

        // Actualizar calendario
        calendarAdapter.setSelectedDate(selectedCalendar);
        calendarAdapter.updateCalendar(currentCalendar);

        // Actualizar lista de eventos
        updateEventosList();
    }

    private void updateEventosList() {
        List<Evento> eventosDelDia = getEventosForDate(selectedCalendar);
        eventoAdapter.updateEventos(eventosDelDia);

        // Mostrar mensaje si no hay eventos
        if (eventosDelDia.isEmpty()) {
            Toast.makeText(this, "No hay obras programadas para este día", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Evento> getEventosForDate(Calendar calendar) {
        List<Evento> eventosDelDia = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDate = sdf.format(calendar.getTime());

        for (Evento evento : todosLosEventos) {
            Calendar eventCal = Calendar.getInstance();
            eventCal.setTime(evento.getFechaHora());
            String eventoDate = sdf.format(eventCal.getTime());

            if (selectedDate.equals(eventoDate)) {
                eventosDelDia.add(evento);
            }
        }

        return eventosDelDia;
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                R.style.DatePickerTheme, // Puedes crear un estilo personalizado
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Actualizar el mes actual si es diferente
                    if (currentCalendar.get(Calendar.YEAR) != year ||
                            currentCalendar.get(Calendar.MONTH) != month) {
                        currentCalendar.set(Calendar.YEAR, year);
                        currentCalendar.set(Calendar.MONTH, month);
                    }

                    updateUI();
                    scrollToSelectedDate();
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void scrollToSelectedDate() {
        // Hacer scroll al día seleccionado en el calendario
        int position = calendarAdapter.getPositionForDate(selectedCalendar);
        if (position >= 0) {
            recyclerViewCalendar.smoothScrollToPosition(position);
        }
    }
}**/

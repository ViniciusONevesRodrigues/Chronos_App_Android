package com.example.chronos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Tela extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private ListView listViewEventos;
    private ArrayList<Evento> listaEventos;
    private AdapImg adaptador;
    private EditText editTextNomeEvento;
    private Button buttonAdicionarImagem, buttonSelecionarData, buttonSelecionarHorario;
    private ImageView imageViewEvento;
    private String dataSelecionada, horarioSelecionado;
    private Bitmap imagemSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela);

        listViewEventos = findViewById(R.id.listViewEventos);
        editTextNomeEvento = findViewById(R.id.editTextNomeEvento);
        buttonAdicionarImagem = findViewById(R.id.buttonAdicionarImagem);
        buttonSelecionarData = findViewById(R.id.buttonSelecionarData);
        buttonSelecionarHorario = findViewById(R.id.buttonSelecionarHorario);
        imageViewEvento = findViewById(R.id.imageViewEvento);
        Button btnAdicionar = findViewById(R.id.buttonAdicionar);

        listaEventos = new ArrayList<>();
        adaptador = new AdapImg(this, listaEventos);
        listViewEventos.setAdapter(adaptador);


        buttonAdicionarImagem.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });


        buttonSelecionarData.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Tela.this, (view, year, month, dayOfMonth) -> {
                dataSelecionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                buttonSelecionarData.setText(dataSelecionada);
            }, ano, mes, dia);
            datePickerDialog.show();
        });


        buttonSelecionarHorario.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minuto = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(Tela.this, (view, hourOfDay, minute) -> {
                horarioSelecionado = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
                buttonSelecionarHorario.setText(horarioSelecionado);
            }, hora, minuto, true);
            timePickerDialog.show();
        });


        btnAdicionar.setOnClickListener(v -> {
            String nomeEvento = editTextNomeEvento.getText().toString();

            if (nomeEvento.isEmpty() || dataSelecionada == null || horarioSelecionado == null ) {
                Toast.makeText(Tela.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, Object> eventoHash = new HashMap<>();
                Evento novoEvento = new Evento(nomeEvento, dataSelecionada + " " + horarioSelecionado, imagemSelecionada);
                eventoHash.put("nome", nomeEvento);
                eventoHash.put("dataSelecionada", dataSelecionada);
                eventoHash.put("horarioSelecionado", horarioSelecionado);
                listaEventos.add(novoEvento);
                adaptador.notifyDataSetChanged();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference eventoRef = database.getReference("evento");

                String key = eventoRef.push().getKey();
                eventoHash.put("key", key);
                assert key != null;
                eventoRef.child(key).setValue(eventoHash).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        editTextNomeEvento.setText("");
                        buttonSelecionarData.setText("Selecionar Data");
                        buttonSelecionarHorario.setText("Selecionar Horário");
                        imageViewEvento.setImageResource(R.drawable.file);
                        imagemSelecionada = null;
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            try {
                imagemSelecionada = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                imageViewEvento.setImageBitmap(imagemSelecionada);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

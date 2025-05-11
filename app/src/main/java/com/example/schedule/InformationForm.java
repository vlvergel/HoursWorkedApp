package com.example.schedule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InformationForm extends AppCompatActivity {

    private EditText txt_name, txt_last_name, txt_hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_information_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txt_name = (EditText) findViewById(R.id.name);
        txt_last_name = (EditText) findViewById(R.id.last_name);
        txt_hours = (EditText) findViewById(R.id.hours);

    }

    private void clearFields() {
        txt_name.setText("");
        txt_last_name.setText("");
        txt_hours.setText("");
    }

    public void Registrar(View view) {
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this, "ScheduleDB", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String name = txt_name.getText().toString();
        String last_name =txt_last_name.getText().toString();
        String hours = txt_hours.getText().toString();
        double hoursDouble = 0;
        try {
            hoursDouble = Double.parseDouble(hours);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Horas no válidas", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!name.isEmpty() && !last_name.isEmpty() && !hours.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("name", name);
            registro.put("last_name", last_name);
            registro.put("worked_hours", hoursDouble);

            long id = db.insert("teacher", null, registro);
            System.out.println(id);
            db.close();

            clearFields();

            Toast.makeText(this, "Calculando...", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(InformationForm.this, Operation.class);
            intent.putExtra("teacher_id", id);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
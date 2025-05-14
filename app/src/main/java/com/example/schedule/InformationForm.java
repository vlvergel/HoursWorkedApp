package com.example.schedule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InformationForm extends AppCompatActivity {

    private EditText txt_name, txt_last_name, txt_hours, txt_identification;
    private TextView txt_search;

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
        txt_search = (TextView) findViewById(R.id.text_search) ;
        SpannableString spannable = new SpannableString("¿Ya estás registrada? Busca aquí");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        };
        spannable.setSpan(clickableSpan, 21, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_search.setText(spannable);
        txt_search.setMovementMethod(LinkMovementMethod.getInstance());
        txt_search.setHighlightColor(Color.TRANSPARENT);
        txt_name = (EditText) findViewById(R.id.name);
        txt_last_name = (EditText) findViewById(R.id.last_name);
        txt_hours = (EditText) findViewById(R.id.hours);
        txt_identification = (EditText) findViewById(R.id.identification);


    }

    private void clearFields() {
        txt_name.setText("");
        txt_last_name.setText("");
        txt_hours.setText("");
        txt_identification.setText("");
    }

    public void Registrar(View view) {
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this, "ScheduleDB", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String name = txt_name.getText().toString();
        String last_name = txt_last_name.getText().toString();
        String hours = txt_hours.getText().toString();
        String identification = txt_identification.getText().toString();

        double hoursDouble = 0;
        try {
            hoursDouble = Double.parseDouble(hours);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Horas no válidas", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!name.isEmpty() && !last_name.isEmpty() && !hours.isEmpty() && !identification.isEmpty()) {
            Cursor cursor = db.rawQuery("SELECT id FROM teacher WHERE identification = ?", new String[]{identification});
            if (cursor.moveToFirst()) {
                Toast.makeText(this, "Esta identificación ya está registrada", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                return;
            }
            cursor.close();

            ContentValues registro = new ContentValues();
            registro.put("name", name);
            registro.put("last_name", last_name);
            registro.put("worked_hours", hoursDouble);
            registro.put("identification", identification);

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
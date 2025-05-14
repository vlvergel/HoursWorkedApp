package com.example.schedule;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Operation extends AppCompatActivity {
    long id;
    String identification;
    private TextView message, name_message, last_name_message, hours_message;
    private static final int MINIMUM_HOURS = 8;
    private static final int MAXIMUM_HOURS = 40;

    private String messageWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_operation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        message = (TextView) findViewById(R.id.message);
        name_message = (TextView) findViewById(R.id.name_message);
        last_name_message = (TextView) findViewById(R.id.last_name_message);
        hours_message = (TextView) findViewById(R.id.hours_message);


        id = getIntent().getLongExtra("teacher_id", -1);
        identification = getIntent().getStringExtra("teacher_identification");

        System.out.println(id + "id en operation");
        if (id != -1) {
            getTeacherById(id);
        } else if (identification != null && !identification.isEmpty()) {
            getTeacherByIdentification(identification);
        } else {
            Toast.makeText(this, "No se recibió ningún identificador", Toast.LENGTH_SHORT).show();
        }

    }

    private void getTeacherById(long id) {
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this, "ScheduleDB", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name, last_name, worked_hours FROM teacher WHERE id = ?", new String[]{String.valueOf(id)});
        handleCursor(cursor);

        cursor.close();
        db.close();
    }

    private void getTeacherByIdentification(String identification) {
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this, "ScheduleDB", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name, last_name, worked_hours FROM teacher WHERE identification = ?", new String[]{identification});
        handleCursor(cursor);

        cursor.close();
        db.close();
    }

    private void handleCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String lastName = cursor.getString(1);
            double workedHours = cursor.getDouble(2);

            if (workedHours > MAXIMUM_HOURS) {
                messageWork = "Ha superado las horas trabajadas conforme a la ley";
            } else if (workedHours < MINIMUM_HOURS) {
                messageWork = "Ha trabajado lo mínimo conforme a la ley";
            } else {
                messageWork = "Ha trabajado horas exactas conforme a la ley";
            }

            SpannableString nameBold = new SpannableString("Nombre: " + name);
            nameBold.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, "Nombre: ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString lastNameBold = new SpannableString("Apellido: " + lastName);
            lastNameBold.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, "Apellido: ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString hoursBold = new SpannableString("Horas trabajadas: " + workedHours);
            hoursBold.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, "Horas trabajadas: ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            name_message.setText(nameBold);
            last_name_message.setText(lastNameBold);
            hours_message.setText(hoursBold);
            message.setText(messageWork);

        } else {
            Toast.makeText(this, "Profesor no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

}
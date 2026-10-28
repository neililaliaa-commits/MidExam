package com.example.midexam;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etStudentId;
    private CheckBox cbEnrolled;
    private Button btnAdd, btnEdit, btnDelete, btnView;
    private ListView lvStudents;

    private DatabaseHelper db;
    private List<Student> studentList = new ArrayList<>();
    private ArrayAdapter<Student> adapter;
    private int selectedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);
        cbEnrolled = findViewById(R.id.cbEnrolled);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        lvStudents = findViewById(R.id.lvStudents);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        lvStudents.setAdapter(adapter);

        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student s = studentList.get(position);
            selectedId = s.getId();
            etName.setText(s.getName());
            etStudentId.setText(s.getStudentId());
            cbEnrolled.setChecked(s.isEnrolled());
        });

        btnAdd.setOnClickListener(v -> addStudent());
        btnEdit.setOnClickListener(v -> editStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnView.setOnClickListener(v -> loadStudents());

        loadStudents();
    }

    private boolean validateInput() {
        String name = etName.getText().toString().trim();
        String sid = etStudentId.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Name is required");
            return false;
        }
        if (sid.isEmpty()) {
            etStudentId.setError("Student ID is required");
            return false;
        }
        return true;
    }

    private void addStudent() {
        if (!validateInput()) return;
        boolean ok = db.addStudent(
                etName.getText().toString().trim(),
                etStudentId.getText().toString().trim(),
                cbEnrolled.isChecked());
        if (ok) {
            Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudents();
        } else {
            Toast.makeText(this, "Failed: Student ID already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void editStudent() {
        if (selectedId == -1) {
            Toast.makeText(this, "Select a student from the list first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validateInput()) return;
        boolean ok = db.updateStudent(
                selectedId,
                etName.getText().toString().trim(),
                etStudentId.getText().toString().trim(),
                cbEnrolled.isChecked());
        if (ok) {
            Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudents();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStudent() {
        if (selectedId == -1) {
            Toast.makeText(this, "Select a student from the list first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (db.deleteStudent(selectedId)) {
            Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudents();
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStudents() {
        studentList.clear();
        studentList.addAll(db.getAllStudents());
        adapter.notifyDataSetChanged();
    }

    private void clearForm() {
        etName.setText("");
        etStudentId.setText("");
        cbEnrolled.setChecked(false);
        selectedId = -1;
    }
}
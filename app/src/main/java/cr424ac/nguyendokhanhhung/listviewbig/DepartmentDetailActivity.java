package cr424ac.nguyendokhanhhung.listviewbig;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DepartmentDetailActivity extends AppCompatActivity {

    Department currentDepartment;
    StudentDB studentDB;
    TextView tvName, tvDescription, tvPhone, tvStudentCount;
    ListView lvStudents;
    Button btnDelete;
    ArrayList<Student> studentsInDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);

        studentDB = new StudentDB(this);

        Toolbar toolbar = findViewById(R.id.toolbar_dep_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = findViewById(R.id.tv_dep_name);
        tvDescription = findViewById(R.id.tv_dep_description);
        tvPhone = findViewById(R.id.tv_dep_phone);
        tvStudentCount = findViewById(R.id.tv_student_count);
        lvStudents = findViewById(R.id.lv_students_in_dep);
        btnDelete = findViewById(R.id.btn_delete_dep);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("DEPARTMENT")) {
            currentDepartment = (Department) intent.getSerializableExtra("DEPARTMENT");
            displayDepartmentInfo();
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }

    private void displayDepartmentInfo() {
        getSupportActionBar().setTitle(currentDepartment.getName());

        tvName.setText("Tên khoa: " + currentDepartment.getName());
        tvDescription.setText("Mô tả: " + (currentDepartment.getDescription().isEmpty() ? "Chưa có" : currentDepartment.getDescription()));
        tvPhone.setText("Điện thoại: " + currentDepartment.getPhone());

        // Get students in this department
        studentsInDepartment = studentDB.getStudentsByDepartment(currentDepartment.getName());
        tvStudentCount.setText("Số sinh viên: " + studentsInDepartment.size());

        // Display student list
        StudentAdapter adapter = new StudentAdapter(this, studentsInDepartment);
        lvStudents.setAdapter(adapter);
    }

    private void confirmDelete() {
        // Build warning message
        String message;
        if (studentsInDepartment.size() > 0) {
            message = "Khoa này có " + studentsInDepartment.size() + " sinh viên.\n\n" +
                    "CHÚ Ý: Xóa khoa sẽ xóa tất cả sinh viên trong khoa này!\n\n" +
                    "Bạn có chắc chắn muốn xóa?";
        } else {
            message = "Bạn có chắc chắn muốn xóa khoa " + currentDepartment.getName() + "?";
        }

        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Xóa tất cả", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (studentsInDepartment.size() > 0) {
                            studentDB.deleteStudentsByDepartment(currentDepartment.getName());
                        }
                        studentDB.deleteDep(currentDepartment);
                        Toast.makeText(DepartmentDetailActivity.this,
                                "Đã xóa khoa và " + studentsInDepartment.size() + " sinh viên",
                                Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
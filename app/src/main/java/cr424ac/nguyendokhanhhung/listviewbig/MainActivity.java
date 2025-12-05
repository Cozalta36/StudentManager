package cr424ac.nguyendokhanhhung.listviewbig;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> mlauncher;
    ArrayList<Student> danhsachsinhvien = new ArrayList<>();
    StudentAdapter adapter;
    ListView listview;
    EditText Ptext1;
    StudentDB studentDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentDB = new StudentDB(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.tbar1);
        setSupportActionBar(toolbar);

        mlauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            Student newStudent = (Student) data.getSerializableExtra("NEW_STUDENT");
                            if (newStudent != null) {
                                studentDB.addStu(newStudent);
                                refreshStudentList();  // Reload from database
                                Toast.makeText(MainActivity.this, "Đã thêm: " + newStudent.getHoten(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        listview = findViewById(R.id.listview);
        Ptext1 = findViewById(R.id.Ptext1);

        refreshStudentList();

        Ptext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student selectedStudent = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                intent.putExtra("STUDENT_DETAIL", selectedStudent);
                startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnAdd) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            mlauncher.launch(intent);
            return true;
        }
        if (id == R.id.btn_manage_departments) {
            Intent intent = new Intent(MainActivity.this, DepartmentActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 200) {
                if (data.hasExtra("DELETED_STUDENT_ID")) {
                    String deletedId = data.getStringExtra("DELETED_STUDENT_ID");
                    studentDB.deleteStudent(deletedId);
                    refreshStudentList();
                    Toast.makeText(this, "Đã xóa sv", Toast.LENGTH_SHORT).show();
                }
                else if (data.hasExtra("UPDATED_STUDENT")) {
                    Student updatedStudent = (Student) data.getSerializableExtra("UPDATED_STUDENT");
                    studentDB.updateStudent(updatedStudent);
                    refreshStudentList();
                    Toast.makeText(this, "Đã cập nhật ttsv", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void refreshStudentList() {
        danhsachsinhvien = studentDB.getStudentsinfo();

        if (adapter == null) {
            adapter = new StudentAdapter(this, danhsachsinhvien);
            listview.setAdapter(adapter);
        } else {
            adapter.updateStudentList(danhsachsinhvien);
        }

        String currentFilter = Ptext1.getText().toString();
        if (!currentFilter.isEmpty()) {
            adapter.getFilter().filter(currentFilter);
        }
    }
}
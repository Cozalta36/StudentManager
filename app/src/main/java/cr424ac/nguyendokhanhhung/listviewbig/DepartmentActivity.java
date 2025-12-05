package cr424ac.nguyendokhanhhung.listviewbig;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class DepartmentActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> addDepartmentLauncher;
    ArrayList<Department> departmentList = new ArrayList<>();
    DepartmentAdapter adapter;
    ListView listView;
    StudentDB studentDB;
    EditText Ptext5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        studentDB = new StudentDB(this);

        Toolbar toolbar = findViewById(R.id.toolbar_department);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản lý Khoa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Ptext5 = findViewById(R.id.Ptext5);
        listView = findViewById(R.id.lv_departments);

        addDepartmentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Department newDep = (Department) result.getData().getSerializableExtra("NEW_DEPARTMENT");
                            if (newDep != null) {
                                studentDB.addDep(newDep);
                                refreshDepartmentList();
                                Toast.makeText(DepartmentActivity.this, "Đã thêm khoa: " + newDep.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        refreshDepartmentList();
        Ptext5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Department selectedDep = departmentList.get(position);
                Intent intent = new Intent(DepartmentActivity.this, DepartmentDetailActivity.class);
                intent.putExtra("DEPARTMENT", selectedDep);
                startActivityForResult(intent, 300);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_department, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.btn_add_department) {
            Intent intent = new Intent(DepartmentActivity.this, AddDepartmentActivity.class);
            addDepartmentLauncher.launch(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300 && resultCode == RESULT_OK) {
            refreshDepartmentList();
        }
    }

    private void refreshDepartmentList() {
        departmentList = studentDB.getAllDepartments();

        if (adapter == null) {
            adapter = new DepartmentAdapter(this, departmentList);
            listView.setAdapter(adapter);
        } else {
            adapter.updateList(departmentList);
        }
    }
}
//Lonasjdnasjdnaisndiandinioasndoiadoiamdoiasmdioasmosadmoiasmdioasmodsamiodmaios
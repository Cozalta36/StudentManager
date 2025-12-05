package cr424ac.nguyendokhanhhung.listviewbig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDepartmentActivity extends AppCompatActivity {

    EditText etName, etDescription, etPhone;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        Toolbar toolbar = findViewById(R.id.toolbar_add_dep);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm Khoa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = findViewById(R.id.et_dep_name);
        etDescription = findViewById(R.id.et_dep_description);
        etPhone = findViewById(R.id.et_dep_phone);
        btnSave = findViewById(R.id.btn_save_dep);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDepartment();
            }
        });
    }

    private void saveDepartment() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên khoa!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }

        Department newDep = new Department(name, description, phone);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_DEPARTMENT", newDep);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
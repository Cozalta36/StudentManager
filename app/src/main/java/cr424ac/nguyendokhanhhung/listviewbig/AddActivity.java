package cr424ac.nguyendokhanhhung.listviewbig;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText Pt_hoten, Pt_mssv, Pt_phone, Pt_mail, Pt_date;
    Spinner spinner1;
    RadioGroup rgGT;
    CheckBox cb_music, cb_book, cb_sport, cb_game;
    Button btn_save;
    ImageView Imgv2;

    Uri selectedImageUri = null;


    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Imgv2 = findViewById(R.id.Imgv2);
        Pt_hoten = findViewById(R.id.Pt_hoten);
        Pt_mssv = findViewById(R.id.Pt_mssv);
        Pt_phone = findViewById(R.id.Pt_phone);
        Pt_mail = findViewById(R.id.Pt_mail);
        Pt_date = findViewById(R.id.Pt_date);
        spinner1 = findViewById(R.id.spinner1);
        rgGT = findViewById(R.id.rgGT);
        cb_music = findViewById(R.id.cb_music);
        cb_book = findViewById(R.id.cb_book);
        cb_sport = findViewById(R.id.cb_sport);
        cb_game = findViewById(R.id.cb_game);
        btn_save = findViewById(R.id.btn_save);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            Imgv2.setImageURI(selectedImageUri);
                            try {
                                getContentResolver().takePersistableUriPermission(
                                        selectedImageUri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openImagePicker();
                    } else {
                        Toast.makeText(this, "Cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Imgv2.setOnClickListener(v -> checkPermissionAndPickImage());

        StudentDB studentDB = new StudentDB(this);
        ArrayList<Department> departments = studentDB.getAllDepartments();

        if (departments.isEmpty()) {
            studentDB.addDep(new Department("Công nghệ thông tin", "Khoa CNTT", "0901111111"));
            studentDB.addDep(new Department("Quản trị kinh doanh", "Khoa QTKD", "0902222222"));
            studentDB.addDep(new Department("Ngôn ngữ Anh", "Khoa Ngoại ngữ", "0903333333"));
            studentDB.addDep(new Department("Thiết kế đồ họa", "Khoa Thiết kế", "0904444444"));
            studentDB.addDep(new Department("Kế toán", "Khoa Kế toán", "0905555555"));
            departments = studentDB.getAllDepartments();
        }

        String[] khoaArray = new String[departments.size()];
        for (int i = 0; i < departments.size(); i++) {
            khoaArray[i] = departments.get(i).getName();
        }

        ArrayAdapter<String> khoaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, khoaArray);
        khoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(khoaAdapter);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dom) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dom);
                updateLabel();
            }
        };

        Pt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddActivity.this, AlertDialog.THEME_HOLO_LIGHT, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudent();
            }
        });
    }
    private void checkPermissionAndPickImage() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            permissionLauncher.launch(permission);
        }
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        Pt_date.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void saveStudent() {
        String hoten = Pt_hoten.getText().toString().trim();
        String masv = Pt_mssv.getText().toString().trim();
        String sdt = Pt_phone.getText().toString().trim();
        String email = Pt_mail.getText().toString().trim();
        String ngaysinh = Pt_date.getText().toString().trim();
        String khoa = spinner1.getSelectedItem().toString();

        if (hoten.isEmpty()) {
            Pt_hoten.setError("Vui lòng nhập họ tên");
            Pt_hoten.requestFocus();
            return;
        }
        if (masv.isEmpty()) {
            Pt_mssv.setError("Vui lòng nhập mã sinh viên");
            Pt_mssv.requestFocus();
            return;
        }
        if (!masv.matches("\\d{10}")) {
            Pt_mssv.setError("Mã sinh viên phải có đúng 10 chữ số");
            Pt_mssv.requestFocus();
            return;
        }
        if (sdt.isEmpty()) {
            Pt_phone.setError("Vui lòng nhập số điện thoại");
            Pt_phone.requestFocus();
            return;
        }
        if (!sdt.matches("\\d{10}")) {
            Pt_phone.setError("Số điện thoại phải có 10 số");
            Pt_phone.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Pt_mail.setError("Vui lòng nhập email");
            Pt_mail.requestFocus();
            return;
        }
        if(!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")){
            Pt_mail.setError("Email không đúng định dạng (ví dụ: 123@abc.xyz)");
            Pt_mail.requestFocus();
            return;
        }
        if (ngaysinh.isEmpty()) {
            Pt_date.setError("Vui lòng chọn ngày sinh");
            Pt_date.requestFocus();
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedGenderId = rgGT.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedGenderId);
        String gt = "";
        if (selectedRadioButton != null) {
            gt = selectedRadioButton.getText().toString();
        }

        ArrayList<String> soThichList = new ArrayList<>();
        if (cb_music.isChecked()) soThichList.add("Âm nhạc");
        if (cb_book.isChecked()) soThichList.add("Đọc sách");
        if (cb_sport.isChecked()) soThichList.add("Thể thao");
        if (cb_game.isChecked()) soThichList.add("Chơi game");

        String avatar = null;
        if (selectedImageUri != null) {
            avatar = selectedImageUri.toString();
        }

        Student newStudent = new Student(hoten, masv, sdt, email, ngaysinh, khoa, gt, avatar);
        newStudent.setSothich(soThichList);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_STUDENT", newStudent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
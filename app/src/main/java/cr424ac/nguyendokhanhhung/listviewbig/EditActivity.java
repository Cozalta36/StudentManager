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

public class EditActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText Pt_hoten, Pt_mssv, Pt_phone, Pt_mail, Pt_date;
    Spinner spinner1;
    RadioGroup rgGT;
    CheckBox cb_music, cb_book, cb_sport, cb_game;
    Button btn_save;
    Student currentStudent;
    ImageView Imgv2;

    Uri selectedImageUri = null;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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
        Imgv2 = findViewById(R.id.Imgv2);

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

        Intent intent = getIntent();
        currentStudent = (Student) intent.getSerializableExtra("STUDENT_TO_EDIT");

        if (currentStudent != null) {
            populateFields();
        }

        String[] khoaArray = {"Công nghệ thông tin", "Quản trị kinh doanh", "Ngôn ngữ Anh", "Thiết kế đồ họa", "Kế toán"};
        ArrayAdapter<String> khoaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, khoaArray);
        khoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(khoaAdapter);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        Pt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditActivity.this, AlertDialog.THEME_HOLO_LIGHT, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Disable editing student ID
        Pt_mssv.setEnabled(false);
        Pt_mssv.setFocusable(false);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
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

    private void populateFields() {
        Pt_hoten.setText(currentStudent.getHoten());
        Pt_mssv.setText(currentStudent.getMasv());
        Pt_phone.setText(currentStudent.getSdt());
        Pt_mail.setText(currentStudent.getEmail());
        Pt_date.setText(currentStudent.getNgaysinh());

        if (currentStudent.getAvatar() != null && !currentStudent.getAvatar().isEmpty()) {
            try {
                selectedImageUri = Uri.parse(currentStudent.getAvatar());
                Imgv2.setImageURI(selectedImageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] khoaArray = {"Công nghệ thông tin", "Quản trị kinh doanh", "Ngôn ngữ Anh", "Thiết kế đồ họa", "Kế toán"};
        for (int i = 0; i < khoaArray.length; i++) {
            if (khoaArray[i].equals(currentStudent.getKhoa())) {
                spinner1.setSelection(i);
                break;
            }
        }

        if (currentStudent.getGt().equalsIgnoreCase("Nam")) {
            ((RadioButton) findViewById(R.id.rb_nam)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.rb_nu)).setChecked(true);
        }

        ArrayList<String> sothich = currentStudent.getSothich();
        if (sothich != null) {
            for (String hobby : sothich) {
                if (hobby.contains("Âm nhạc")) cb_music.setChecked(true);
                if (hobby.contains("Đọc sách")) cb_book.setChecked(true);
                if (hobby.contains("Thể thao")) cb_sport.setChecked(true);
                if (hobby.contains("Chơi game") || hobby.contains("game")) cb_game.setChecked(true);
            }
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        Pt_date.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void updateStudent() {
        String hoten = Pt_hoten.getText().toString().trim();
        String masv = Pt_mssv.getText().toString().trim();
        String sdt = Pt_phone.getText().toString().trim();
        String email = Pt_mail.getText().toString().trim();
        String ngaysinh = Pt_date.getText().toString().trim();
        String khoa = spinner1.getSelectedItem().toString();

        if (hoten.isEmpty() || masv.isEmpty() || sdt.isEmpty() || email.isEmpty() || ngaysinh.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedGenderId = rgGT.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedGenderId);
        String gt = "";
        if (selectedRadioButton != null) {
            gt = selectedRadioButton.getText().toString();
        } else {
            Toast.makeText(this, "Vui lòng chọn giới tính!", Toast.LENGTH_SHORT).show();
            return;
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

        Student updatedStudent = new Student(hoten, masv, sdt, email, ngaysinh, khoa, gt, avatar);
        updatedStudent.setSothich(soThichList);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_STUDENT", updatedStudent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
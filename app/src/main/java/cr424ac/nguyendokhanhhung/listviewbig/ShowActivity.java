package cr424ac.nguyendokhanhhung.listviewbig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends AppCompatActivity {
    private Student currentStudent;
    private Button btn_delete, btn_edit, Bcall, Bmail;

    private ImageView Imgv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Toolbar toolbar = findViewById(R.id.toolbar_show);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin Sinh viên");

        TextView tvHoTen = findViewById(R.id.tv_detail_hoten);
        TextView tvMaSV = findViewById(R.id.tv_detail_masv);
        TextView tvSdt = findViewById(R.id.tv_detail_sdt);
        TextView tvEmail = findViewById(R.id.tv_detail_email);
        TextView tvNgaySinh = findViewById(R.id.tv_detail_ngaysinh);
        TextView tvKhoa = findViewById(R.id.tv_detail_khoa);
        TextView tvSex = findViewById(R.id.tv_detail_gt);
        TextView tvSoThich = findViewById(R.id.tv_detail_sothich);

        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        Bcall = findViewById(R.id.Bcall);
        Bmail = findViewById(R.id.Bmail);
        Imgv3 = findViewById(R.id.Imgv3);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("STUDENT_DETAIL")) {
            currentStudent = (Student) intent.getSerializableExtra("STUDENT_DETAIL");

            if (currentStudent != null) {
                tvHoTen.setText(currentStudent.getHoten());
                tvMaSV.setText("Mã SV: " + currentStudent.getMasv());
                tvSdt.setText("SĐT: " + currentStudent.getSdt());
                tvEmail.setText("Email: " + currentStudent.getEmail());
                tvNgaySinh.setText("Ngày sinh: " + currentStudent.getNgaysinh());
                tvKhoa.setText("Khoa: " + currentStudent.getKhoa());
                tvSex.setText("Giới tính: " + currentStudent.getGt());
                tvSoThich.setText("Sở thích: " + currentStudent.getSothich());
                if (currentStudent.getAvatar() != null && !currentStudent.getAvatar().isEmpty()) {
                    try {
                        Uri imageUri = Uri.parse(currentStudent.getAvatar());
                        Imgv3.setImageURI(imageUri);
                    } catch (Exception e) {
                        Imgv3.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                } else {
                    Imgv3.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editint = new Intent(ShowActivity.this, EditActivity.class);
                editint.putExtra("STUDENT_TO_EDIT", currentStudent);
                startActivityForResult(editint, 100);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ShowActivity.this)
                        .setMessage("Bạn có chắc chắn muốn xóa sinh viên " + currentStudent.getHoten() + "?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteStudent();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        Bmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intmail = new Intent(Intent.ACTION_SENDTO);
                intmail.setData(Uri.parse("mailto:"+currentStudent.getEmail()));
                startActivity(intmail);
            }
        });

        Bcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callint = new Intent(Intent.ACTION_DIAL);
                callint.setData(Uri.parse("tel:"+currentStudent.getSdt()));
                startActivity(callint);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("UPDATED_STUDENT")) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("UPDATED_STUDENT", data.getSerializableExtra("UPDATED_STUDENT"));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }
    private void deleteStudent() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("DELETED_STUDENT_ID", currentStudent.getMasv());
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show();
        finish();
    }
}
package cr424ac.nguyendokhanhhung.listviewbig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class StudentDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentManagement.db";
    private static final int DATABASE_VERSION = 2;

    public StudentDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
             String sqlStu = "CREATE TABLE Student (" +
                "masv TEXT PRIMARY KEY," +
                "hoten	TEXT NOT NULL," +
                "sdt	TEXT NOT NULL," +
                "email	TEXT NOT NULL,"+
                "ngaysinh	TEXT NOT NULL,"+
                "khoa	TEXT NOT NULL,"+
                "gt	TEXT NOT NULL,"+
                "sothich    TEXT NOT NULL,"+
                "avatar  TEXT)";
        db.execSQL(sqlStu);

             String sqlDep= "CREATE TABLE Department(" +
                     "depid INTEGER PRIMARY KEY," +
                     "namedep TEXT NOT NULL UNIQUE," +
                     "description TEXT," +
                     "phone TEXT NOT NULL"+")";
        db.execSQL(sqlDep);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE Student ADD COLUMN avatar TEXT");
        }
    }
    public void addDep (Department department){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("namedep", department.getName());
        contentValues.put("description", department.getDescription());
        contentValues.put("phone", department.getPhone());
        db.insert("Department", null, contentValues);
        db.close();
    }

    public void updateDep (Department department, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        db.update("Department", contentValues,"namedep == ?", new String[]{department.getName()});
        db.close();
    }

    public void deleteDep (Department department){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Department", "namedep == ?", new String[]{department.getName()});
        db.close();
    }

    public ArrayList<Department> getAllDepartments(){
        ArrayList<Department> departmentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Department ORDER BY namedep ASC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                String phone = cursor.getString(3);

                Department department = new Department(id, name, description, phone);
                departmentList.add(department);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return departmentList;
    }

    public void addStu (Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("masv", student.getMasv());
        contentValues.put("hoten", student.getHoten());
        contentValues.put("khoa", student.getKhoa());
        contentValues.put("sdt", student.getSdt());
        contentValues.put("gt", student.getGt());
        contentValues.put("ngaysinh", student.getNgaysinh());
        contentValues.put("email", student.getEmail());
        String sothichStr = TextUtils.join(",", student.getSothich());
        contentValues.put("sothich", sothichStr);
        contentValues.put("avatar", student.getAvatar());
        db.insert("Student", null, contentValues);
        db.close();
    }

    public ArrayList<Student> getStudentsinfo(){
        ArrayList<Student> studentlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Student ORDER BY hoten ASC", null);

        if (cursor.moveToFirst()) {
            do {
                String masv = cursor.getString(0);
                String hoten = cursor.getString(1);
                String sdt = cursor.getString(2);
                String email = cursor.getString(3);
                String ngaysinh = cursor.getString(4);
                String khoa = cursor.getString(5);
                String gt = cursor.getString(6);
                String sothichStr = cursor.getString(7);
                String avatar = cursor.getString(8);

                Student student = new Student(hoten, masv, sdt, email, ngaysinh, khoa, gt, avatar);

                if (sothichStr != null && !sothichStr.isEmpty()) {
                    ArrayList<String> sothichList = new ArrayList<>(Arrays.asList(sothichStr.split(",")));
                    student.setSothich(sothichList);
                }

                studentlist.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return studentlist;
    }
    public ArrayList<Student> getStudentsByDepartment(String departmentName) {
        ArrayList<Student> studentlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Student WHERE khoa = ? ORDER BY hoten ASC", new String[]{departmentName});

        if (cursor.moveToFirst()) {
            do {
                String masv = cursor.getString(0);
                String hoten = cursor.getString(1);
                String sdt = cursor.getString(2);
                String email = cursor.getString(3);
                String ngaysinh = cursor.getString(4);
                String khoa = cursor.getString(5);
                String gt = cursor.getString(6);
                String sothichStr = cursor.getString(7);
                String avatar = cursor.getString(8);

                Student student = new Student(hoten, masv, sdt, email, ngaysinh, khoa, gt, avatar);

                if (sothichStr != null && !sothichStr.isEmpty()) {
                    ArrayList<String> sothichList = new ArrayList<>(Arrays.asList(sothichStr.split(",")));
                    student.setSothich(sothichList);
                }

                studentlist.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return studentlist;
    }
    public void updateStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hoten", student.getHoten());
        contentValues.put("khoa", student.getKhoa());
        contentValues.put("sdt", student.getSdt());
        contentValues.put("gt", student.getGt());
        contentValues.put("ngaysinh", student.getNgaysinh());
        contentValues.put("email", student.getEmail());
        String sothichStr = TextUtils.join(",", student.getSothich());
        contentValues.put("sothich", sothichStr);
        contentValues.put("avatar", student.getAvatar());
        db.update("Student", contentValues, "masv = ?", new String[]{student.getMasv()});
        db.close();
    }
    public void deleteStudentsByDepartment(String departmentName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Student", "khoa = ?", new String[]{departmentName});
        db.close();
    }
    public void deleteStudent(String masv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Student", "masv = ?", new String[]{masv});
        db.close();
    }
}

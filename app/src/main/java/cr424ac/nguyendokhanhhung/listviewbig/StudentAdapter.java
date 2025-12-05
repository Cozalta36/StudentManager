package cr424ac.nguyendokhanhhung.listviewbig;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    private ArrayList<Student> studentListFull;

    public StudentAdapter(Context context, ArrayList<Student> studentList) {
        super(context, 0, studentList);
        studentListFull = new ArrayList<>(studentList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_student, parent, false);
        }

        Student currentStudent = getItem(position);

        TextView tvHoTen = convertView.findViewById(R.id.tv_hoten);
        TextView tvMaSV = convertView.findViewById(R.id.tv_masv);
        TextView tvKhoa = convertView.findViewById(R.id.tv_khoa);
        ImageView imgAVT = convertView.findViewById(R.id.Imgv1);

        if (currentStudent != null) {
            tvHoTen.setText(currentStudent.getHoten());
            tvMaSV.setText("Mã SV: " + currentStudent.getMasv());
            tvKhoa.setText("Khoa: " + currentStudent.getKhoa());

            if (currentStudent.getAvatar() != null && !currentStudent.getAvatar().isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(currentStudent.getAvatar());
                    imgAVT.setImageURI(imageUri);
                } catch (Exception e) {
                    imgAVT.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else {
                imgAVT.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return studentFilter;
    }

    private Filter studentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Student> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(studentListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Student item : studentListFull) {
                    if (item.getHoten().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    public void updateStudentList(ArrayList<Student> newList) {
        studentListFull.clear();
        studentListFull.addAll(newList);
        clear();
        addAll(newList);
        notifyDataSetChanged();
    }
}
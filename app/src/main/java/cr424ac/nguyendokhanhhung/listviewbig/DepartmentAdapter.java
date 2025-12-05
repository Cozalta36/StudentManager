package cr424ac.nguyendokhanhhung.listviewbig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DepartmentAdapter extends ArrayAdapter<Department> {

    public DepartmentAdapter(Context context, ArrayList<Department> departments) {
        super(context, 0, departments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_department, parent, false);
        }

        Department department = getItem(position);

        TextView tvName = convertView.findViewById(R.id.tv_dep_name);
        TextView tvPhone = convertView.findViewById(R.id.tv_dep_phone);

        if (department != null) {
            tvName.setText(department.getName());
            tvPhone.setText("SĐT: " + department.getPhone());
        }

        return convertView;
    }

    public void updateList(ArrayList<Department> newList) {
        clear();
        addAll(newList);
        notifyDataSetChanged();
    }
}
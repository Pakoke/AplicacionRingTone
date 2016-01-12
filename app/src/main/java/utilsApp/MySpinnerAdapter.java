package utilsApp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import com.rey.material.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Javi on 10/12/2015.
 */
public class MySpinnerAdapter extends BaseAdapter implements SpinnerAdapter
{
    private ArrayList<String> list;
    public MySpinnerAdapter(ArrayList<String> list){
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text = new TextView(convertView.getContext());
        text.setText(list.get(position));
        return text;
    }
}

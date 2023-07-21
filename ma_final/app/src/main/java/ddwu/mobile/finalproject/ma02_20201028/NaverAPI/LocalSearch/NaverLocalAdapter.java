package ddwu.mobile.finalproject.ma02_20201028.NaverAPI.LocalSearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ddwu.mobile.finalproject.ma02_20201028.R;

public class NaverLocalAdapter extends BaseAdapter {
    public static final String TAG = "NaverLocalAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<NaverLocalDTO> list;


    public NaverLocalAdapter(Context context, int resource, ArrayList<NaverLocalDTO> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NaverLocalDTO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.local_title);
            viewHolder.tvRoadAddress = view.findViewById(R.id.local_roadAddress);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        NaverLocalDTO dto = list.get(position);

        viewHolder.tvTitle.setText(dto.getTitle());
        viewHolder.tvRoadAddress.setText(dto.getRoadAddress());

        return view;
    }

    public void setList(ArrayList<NaverLocalDTO> list) {
        this.list = list;
    }

    static class ViewHolder {
        public TextView tvTitle = null;
        public TextView tvRoadAddress = null;
    }
}

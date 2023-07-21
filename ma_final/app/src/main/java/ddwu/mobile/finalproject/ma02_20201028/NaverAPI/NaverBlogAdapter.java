package ddwu.mobile.finalproject.ma02_20201028.NaverAPI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ddwu.mobile.finalproject.ma02_20201028.R;

public class NaverBlogAdapter extends BaseAdapter {

        public static final String TAG = "NaverBlogAdapter";

        private LayoutInflater inflater;
        private Context context;
        private int layout;
        private ArrayList<NaverBlogDTO> list;
        ViewHolder viewHolder = null;

    public NaverBlogAdapter(Context context, int layout, ArrayList<NaverBlogDTO> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        inflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public NaverBlogDTO getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return list.get(position).get_id();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
                viewHolder.tvDescription = convertView.findViewById(R.id.tvDescription);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            NaverBlogDTO dto = list.get(position);

            viewHolder.tvTitle.setText(dto.getTitle());
            viewHolder.tvDescription.setText(dto.getDescription());

            return convertView;
        }


        public void setList(ArrayList<NaverBlogDTO> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        static class ViewHolder {
            public TextView tvTitle = null;
            public TextView tvDescription = null;
        }
    }
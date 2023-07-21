package ddwu.mobile.finalproject.ma02_20201028.CafeRecord;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import ddwu.mobile.finalproject.ma02_20201028.R;

public class MyCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    int layout;
    ViewHolder holder;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        holder = new ViewHolder();
        view.setTag(holder);
        return view;
    }

    private void setPic(String mCurrentPhotoPath) {
        int targetW = 86;
        int targetH = 86;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        holder.image.setImageBitmap(bitmap);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder.tvName == null){
            holder.tvName = view.findViewById(R.id.tvName);
            holder.image = view.findViewById(R.id.smallImage);
            holder.rtStar = view.findViewById(R.id.rtBar);
        }

            holder.tvName.setText(cursor.getString(cursor.getColumnIndex(RecordDBHelper.CAFE_NAME)));
            setPic(cursor.getString(cursor.getColumnIndex(RecordDBHelper.IMG)));
            holder.rtStar.setRating(Float.parseFloat(cursor.getString(cursor.getColumnIndex(RecordDBHelper.STAR))));
    }

    static class ViewHolder {

        public ViewHolder(){
            tvName = null;
            image = null;
            rtStar = null;
        }

        TextView tvName;
        ImageView image;
        RatingBar rtStar;
    }
}


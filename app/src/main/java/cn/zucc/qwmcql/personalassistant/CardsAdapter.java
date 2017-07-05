package cn.zucc.qwmcql.personalassistant;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardsAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private Cursor cursor;
    private InnerItemOnclickListener mListener;

    public CardsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_card, null);
            holder = new ViewHolder();
            holder.itemText = (TextView) convertView
                    .findViewById(R.id.list_item_card_text);
            holder.cardView = (CardView) convertView.findViewById(R.id.card);
            holder.timeText = (TextView) convertView.findViewById(R.id.timetext);
            holder.cardView = (CardView) convertView.findViewById(R.id.card);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        holder.itemText.setText(content);
        holder.timeText.setText(time);
        holder.cardView.setOnClickListener(this);
        holder.cardView.setTag(position);
        return convertView;
    }

    private static class ViewHolder {
        private TextView itemText;
        private CardView cardView;
        private TextView timeText;
    }

    interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}

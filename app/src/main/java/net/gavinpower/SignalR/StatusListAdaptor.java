package net.gavinpower.SignalR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.gavinpower.Models.Status;
import net.gavinpower.Models.Statuses;
import net.gavinpower.twangr.R;

public class StatusListAdaptor extends BaseAdapter
{
    private Context context;
    private Statuses statuses;

    public StatusListAdaptor(Context context, Statuses statuses)
    {
        this.context = context;
        this.statuses = statuses;
    }

    @Override
    public int getCount() {
        return statuses.size();
    }

    @Override
    public Object getItem(int position) {
        return statuses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Status s = statuses.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.newsfeed_newsstory, null);

        TextView Title = (TextView) convertView.findViewById(R.id.NewsStoryHeader);
        TextView Body = (TextView) convertView.findViewById(R.id.NewsStoryBody);

        Title.setText("" + s.getStatusAuthor());
        Body.setText(s.getStatusContent());
        return convertView;
    }
}

package com.example.xz.weiji.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xz.weiji.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.Holder> {
    //private List<String> list = new ArrayList<String>();
    private Context context;
    private ArrayList<String> noteList;
    private ArrayList<String> dateList;
    private ArrayList<String> titleList;

    public RVAdapter(Context context, ArrayList<String> noteList, ArrayList<String> dateList, ArrayList<String> titleList) {
        this.noteList = noteList;
        this.dateList = dateList;
        this.titleList = titleList;
        this.context = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notelist, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //holder.tvContent.setText(list.get(position));
        holder.title.setText(titleList.get(position));
        holder.note.setText(noteList.get(position));
        holder.date.setText(dateList.get(position));
    }


    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        public LinearLayout llLayout;
        public LinearLayout llDelete;
        public LinearLayout llStar;
        public TextView title;
        public TextView note;
        public TextView date;

        public Holder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.tv_notetitle);
            note=(TextView)itemView.findViewById(R.id.tv_note);
            date=(TextView)itemView.findViewById(R.id.tv_date);
            llLayout= (LinearLayout) itemView.findViewById(R.id.ll_item);
            llDelete=(LinearLayout)itemView.findViewById(R.id.ll_delete);
            llStar=(LinearLayout)itemView.findViewById(R.id.ll_star);
        }
    }
}

package com.example.xz.weiji.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.StringUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        List<String> textList = StringUtils.cutStringByImgTag(noteList.get(position));
        StringBuilder note=new StringBuilder();
        String img="";
        for (String s:textList){
            if (s.contains("<img") && s.contains("src=")) {
                img=StringUtils.getImgSrc(s);
            }else {
                note.append(s);
            }
        }
        Log.i("RVAdpter","img:"+img+"note:"+note);

        //holder.tvContent.setText(list.get(position));
        holder.title.setText(titleList.get(position));
        holder.note.setText(note);
        holder.date.setText(dateList.get(position));
        if(TextUtils.isEmpty(img)){
            holder.circleImageView.setVisibility(View.GONE);
        }else {
            holder.circleImageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(img).into(holder.circleImageView);
        }
    }


    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public  class Holder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;

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
            circleImageView=(CircleImageView)itemView.findViewById(R.id.iv_note);

        }
    }
}

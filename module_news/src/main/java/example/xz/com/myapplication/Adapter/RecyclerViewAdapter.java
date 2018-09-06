package example.xz.com.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import example.xz.com.myapplication.Data.StoryViewModel;
import example.xz.com.myapplication.R;
import example.xz.com.myapplication.Utils.ScreenUtils;

/**
 * Created by Administrator on 2017/9/12.
 */

public class RecyclerViewAdapter extends ListBaseAdapter<StoryViewModel> {


    Context mContext;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context) {
        mContext = context;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
        if(viewType==0) {
            itemView = inflater.inflate(R.layout.recycler_item_view, parent, false);
            return new ViewHolder(itemView);
        }else {
            itemView = inflater.inflate(R.layout.recycler_item_date, parent, false);
            return new DateViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        StoryViewModel storyViewModel = mDataList.get(position);

        switch (getItemViewType(position)){
            case 0:
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.tv_story_title.setText(storyViewModel.getTitle());
                Picasso.with(mContext).load(storyViewModel.getImages()).resize(108,70).centerCrop().into(viewHolder.iv_story_image);
                break;
            case 1:
                DateViewHolder dateViewHolder=(DateViewHolder)holder;
                String date=storyViewModel.getTitle();
                //subString : beginIndex<=str的值<endIndex
                dateViewHolder.tv_date.setText(date.substring(0,4)+"年"+date.substring(4,6)+"月"+date.substring(6,8)+"日");
                break;
        }




    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getType();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_story_title;
        private ImageView iv_story_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_story_title = (TextView) itemView.findViewById(R.id.tv_story_title);
            iv_story_image = (ImageView) itemView.findViewById(R.id.iv_story_image);
        }
    }
    private class DateViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_date;

        public DateViewHolder(View itemView) {
            super(itemView);
            tv_date=(TextView)itemView.findViewById(R.id.tv_date);
        }
    }
}

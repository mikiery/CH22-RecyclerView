package tw.tcnr05.m2205;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>     implements
        View.OnClickListener {

    private List<Post> mData;
    private OnItemClickListener mOnItemClickListener=null;
    private Context mContext;

    //Adapter ，接受數據資料
    public RecycleViewAdapter(Context context,List<Post> data){
        mContext=context;
        mData = data;

    }

    //    -------------------------------------------------------------------
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.teacherimg = (ImageView) view.findViewById(R.id.teacherimg);
        holder.teacherName = (TextView) view.findViewById(R.id.teacherName);
        holder.teacherContent = (TextView) view.findViewById(R.id.teacherContent);
        //----------------------------------------------------
        //將創建的View註冊點擊事件
        view.setOnClickListener(this);
        return holder;
    }
    //    -------------------------------------------------------------------
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Post post = mData.get(position);
        holder.teacherName.setText(post.teacherName);
        holder.teacherContent.setText(post.content);

        Glide.with(mContext)
                .load(post.posterThumbnailUrl)
                .into(holder.teacherimg);

/*****
 options = new RequestOptions()
 .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(50)))
 .placeholder(R.mipmap.ic_launcher)
 .error(R.mipmap.ic_launcher)
 .priority(Priority.NORMAL);

 Glide.with(mContext)
 .asBitmap()
 .load(ans_Url)
 .override(30,30)
 .error(R.drawable.error_img)
 .dontAnimate()
 .apply(options)
 .placeholder(R.drawable.aa09)
 .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
 .into(new CustomTarget<Bitmap>() {
@Override
public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
VGPS = new LatLng(dLat, dLon);// 更新成欲顯示的地圖座標
map.addMarker(new MarkerOptions()
.position(VGPS)
.alpha(0.9f)
.title("." + "vtitle")
.snippet("緯度:" + String.valueOf(dLat) + "\n經度:" + String.valueOf(dLon))
//                            .infoWindowAnchor(0.5f, 0.9f)
.infoWindowAnchor(Anchor_x, Anchor_y)
.icon(BitmapDescriptorFactory.fromBitmap(resource)) // 顯示圖標文字
);
}
@Override
public void onLoadCleared(@Nullable Drawable placeholder) {
//                            int a=0;
}

@Override
public void onLoadFailed(@Nullable Drawable errorDrawable) {
super.onLoadFailed(errorDrawable);

}

@Override
public void onLoadStarted(@Nullable Drawable placeholder) {
super.onLoadStarted(placeholder);
}
});
 ******/
        //將position保存在itemView的Tag中，以便點擊時進行獲取
        holder.itemView.setTag(position);
    }

    //    -------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return mData.size();
    }
    //==========================================

    //---------------設定監聽-----------
    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }


    //--------------------------------
    //-------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    //    -------------------------------------------------------------------
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //======= sub class   ==================
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView teacherimg;
        public TextView teacherName;
        public TextView teacherContent;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    //-----------------------------------------------

    //====================================================
}
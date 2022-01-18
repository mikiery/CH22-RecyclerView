package tw.tcnr05.m2207;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
                .inflate(R.layout.cellpost, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.img);
        holder.Name = (TextView) view.findViewById(R.id.Name);
        holder.Content = (TextView) view.findViewById(R.id.Content);
        holder.Add = (TextView) view.findViewById(R.id.Addr);
        holder.Zipcode = (TextView) view.findViewById(R.id.Zipcode);
        holder.Website = (TextView) view.findViewById(R.id.mWebsite);
        holder.Parkinginfo_Px = (TextView) view.findViewById(R.id.Px);
        holder.Parkinginfo_Py = (TextView) view.findViewById(R.id.Py);
        //----------------------------------------------------
        //將創建的View註冊點擊事件   點下去顯示內容
        view.setOnClickListener(this);
        return holder;

    }
    //    -------------------------------------------------------------------
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Post post = mData.get(position);
        holder.Name.setText(post.Name);
        holder.Add.setText(post.Add);
        holder.Content.setText(post.Content);
//        if (post.Zipcode.length()>0){
            holder.Zipcode.setText("["+post.Zipcode+"]");
//        }else{
//            holder.Zipcode.setText("[000]");
//        }
        holder.Website.setText(post.Website);

        holder.Parkinginfo_Py.setText(post.Py);
        if(post.Py.length()>0){
            holder.Parkinginfo_Py.setText("[123456]");
        }
        holder.Parkinginfo_Px.setText(post.Px);
        if(post.Px.length()>0){
            holder.Parkinginfo_Px.setText("[789000]");
        }

//---------------------------------------
//        若圖片檔名是中文無法下載,可用此段檢查圖片網址且將中文解碼
//        String ans_Url = post.posterThumbnailUrl;
//        if (post.posterThumbnailUrl.getBytes().length == post.posterThumbnailUrl.length() ||
//                post.posterThumbnailUrl.getBytes().length > 100) {
//            ans_Url = post.posterThumbnailUrl;//不包含中文，不做處理
//        } else {
////    ans_Url = utf8Togb2312(post.posterThumbnailUrl);
////           ans_Url = utf8Togb2312(post.posterThumbnailUrl).replace("http://", "https://");
//        }
//        Glide.with(mContext)
//                .load(ans_Url)
//                .into(holder.img);
//----------------------------------------

//============================================
        Glide.with(mContext)//畫圖 轉圖片檔
                .load(post.posterThumbnailUrl)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(100, 75) //圖片設定大小
                .transition(withCrossFade())
                .error( //-----------------------------沒有圖檔要做什麼事
                        Glide.with(mContext)
                                .load("https://tcnr005.000webhostapp.com/opendata/nopic1.jpg"))
                .into(holder.img);

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
        public ImageView img;
        public TextView Name;
        public TextView Content;
        public TextView Add;
        public TextView Zipcode;
        public TextView Website;
        public TextView Parkinginfo_Px;
        public TextView Parkinginfo_Py;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
//-----------------------------------------------

//====================================================
}

package tw.tcnr05.m2206;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
public class M2206 extends AppCompatActivity {
    //private String ul = "https://gis.taiwan.net.tw/XMLReleaseALL_public/hotel_C_f.json";//政府網站
//    private String ul = "http://192.168.10.5/opendata/hotel_C_f.json";//自己區網網站
 private String ul = "https://tcnr005.000webhostapp.com/opendata/hotel_C_f.json";//000web
    private LinearLayout li01;
    private TextView mTxtResult;
    private TextView mDesc;
    private RecyclerView recyclerView;
    private TextView t_count;
    private SwipeRefreshLayout laySwipe;
    private ArrayList<Map<String, Object>> mList;
    private int total;
    private int t_total;
    private TextView u_loading;

    private int nowposition=0;
    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh;


    //  private String ul="https://tcnr005.000webhostapp.com/opendata/hotel_C_f.json";   //ooo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------遇到需處理大料資料加此兩行 不然會散退--------------------------
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //---------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m2206);
        setupViewConpoment();

    }


    private void setupViewConpoment() {

        li01 = (LinearLayout) findViewById(R.id.li01);
        li01.setVisibility(View.GONE);
        mTxtResult = (TextView) findViewById(R.id.m2206_name);
        mDesc = (TextView) findViewById(R.id.m2206_descr);

        mDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        mDesc.scrollTo(0, 0);//textview 回頂端
        u_loading=(TextView)findViewById(R.id.u_loading);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        t_count = (TextView) findViewById(R.id.count);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                li01.setVisibility(View.GONE);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //---------滑動的時候做什麼事-------
            }
        });

        //----------設定下載-------------
        laySwipe = (SwipeRefreshLayout) findViewById(R.id.laySwipe);
        laySwipe.setSize(SwipeRefreshLayout.LARGE);
        //------拉手機長度一半以上
        laySwipe.setDistanceToTriggerSync(300);
        laySwipe.setProgressBackgroundColorSchemeColor(getColor(R.color.green));
        //-----顏色變化
        laySwipe.setColorSchemeResources(
                R.color.red,
                R.color.blue,
                R.color.Fuchsia,
                R.color.Yellow,
                R.color.Aqua,
                R.color.Black,
                R.color.Gray,
                R.color.Teal);
        laySwipe.setOnRefreshListener(b001on);
        laySwipe.setProgressViewOffset(true, 0, 50);
        b001on.onRefresh();//開始專圈下載資料


    }

    //-----------------------------------------------------
    private final SwipeRefreshLayout.OnRefreshListener b001on = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            mTxtResult.setText("");
            xx myAltDig = new xx(M2206.this);
            myAltDig.setTitle(getString(R.string.dialog_title));
            myAltDig.setMessage(getString(R.string.dialog_t001) + getString(R.string.dialog_b001));
            myAltDig.setIcon(android.R.drawable.ic_menu_rotate);
            myAltDig.setCancelable(false);
            myAltDig.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_positive), altDlgOnClkPosiBtnLis);
            myAltDig.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.dialog_neutral), altDlgOnClkNeutBtnLis);
            myAltDig.show();


        }
    };
//==============第二步==================
    private void setDatatolist() {
        //=====================
        u_importopendata(); //下載opendata
        //====================
        //設定Adapter
        final ArrayList<Post> mData = new ArrayList<>();   //初始化設定陣列放Post設名稱mData  存著post格式 list   建立Post class
        for (Map<String, Object> m : mList){ //打 for  each 迴圈  抓取mList設定
            if(m!=null){
                String Name = m.get("Name").toString().trim(); //名稱
                String Add = m.get("Add").toString().trim(); //住址
                String Picture1 = m.get("Picture1").toString().trim(); //圖片
//-------------------------假如沒有圖片會跳入這網址-----------------------------------------------
                if (Picture1.isEmpty() || Picture1.length() < 1) {
                    Picture1 = "https://tcnr005.000webhostapp.com/opendata/nopic1.jpg";
                }
//-------------------------------------------------------------------------
                String Description = m.get("Description").toString().trim(); //描述
                String Zipcode = m.get("Zipcode").toString().trim(); //zip
                String Website = m.get("Website").toString().trim(); //商家網頁,

//************************************************************
                mData.add(new Post(Name, Picture1, Add, Description, Zipcode,Website)); //生資料給Adapter使用
//************************************************************
                int f=0;
//-------------------------------------------------------------------------
            }else{
                return;
            }
        }
        int d=0;
        RecycleViewAdapter adapter = new RecycleViewAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
// ************************************
        adapter.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                li01.setVisibility(View.VISIBLE);
//                Toast.makeText(M2205.this, "onclick" + mData.get(position).hotelName.toString(), Toast.LENGTH_SHORT).show();
                mTxtResult.setText(getString(R.string.m2206_name) + mData.get(position).Name);
                mDesc.setText(mData.get(position).Content);
                mDesc.scrollTo(0, 0); //textview 回頂端
                nowposition = position;
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");

            }
        });
//********************************* ****
        recyclerView.setAdapter(adapter);


    }
    //=========================================
    //==============第一步=================
    private void u_importopendata() { //下載opendata
        try {
            String Task_opendata = new TransTask().execute(ul).get();   //旅館民宿 - 觀光資訊資料庫
            mList = new ArrayList<Map<String, Object>>();
            JSONObject json_obj1 = new JSONObject(Task_opendata);
            JSONObject json_obj2 = json_obj1.getJSONObject("XML_Head");
            JSONObject infos = json_obj2.getJSONObject("Infos");
            JSONArray info = infos.getJSONArray("Info");
            total = 0;
            t_total = info.length(); //總筆數
            int a=0;
//------JSON 排序----------------------------------------
            info = sortJsonArray(info);
            //----(2)
            total = info.length(); //有效筆數
            t_count.setText(getString(R.string.ncount) + total + "/" + t_total);
//----------------------------------------------------------
        for (int i=0;i<info.length();i++){
            Map<String, Object> item = new HashMap<String, Object>();
            String Name = info.getJSONObject(i).getString("Name");
            String Description = info.getJSONObject(i).getString("Description");
            String Add = info.getJSONObject(i).getString("Add");
            String Picture1 = info.getJSONObject(i).getString("Picture1");
            String Zipcode = info.getJSONObject(i).getString("Zipcode"); //郵遞區號,
            String Website = info.getJSONObject(i).getString("Website"); //商家網頁,
            item.put("Name", Name);
            item.put("Description", Description);
            item.put("Add", Add);
            item.put("Picture1", Picture1);
            item.put("Zipcode", Zipcode);
            item.put("Website", Website);
            mList.add(item);
//-------------------

        }
            int c=0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//===================================
    private JSONArray sortJsonArray(JSONArray info) {
        //County自定義的排序Method
        final ArrayList<JSONObject> json = new ArrayList<>();
        for (int i = 0; i < info.length(); i++) {  //將資料存入ArrayList json中
            try {
                json.add(info.getJSONObject(i));
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }


//        XML_Head: {
//            Listname: "3",
//                    Language: "C",
//                    Orgname: "315080000H",
//                    Updatetime: "2022-01-12T01:15:04+08:00",
//                    Infos: {
//                Info: [
//                {
//                    Id: "C3_315080000H_000009",
//                            Name: "龍潭豆花",
//                        Description: "龍潭豆花只賣一種東西，也就是現點現做的古早味豆花，讓饕客把味蕾的重點回歸到豆花本身，豆花滑順綿密，比例調配得很好，帶著微微焦香，豆花出自店家自製，有著自己的特色。",
//                        Add: "臺北市中正區汀州路三段239號",
//                        Zipcode: "",
//                        Region: "臺北市",
//                        Town: "中正區",
//                        Tel: "",
//                        Opentime: "無",
//                        Website: "",
//                        Picture1: "",
//                        Picdescribe1: "",
//                        Picture2: "",
//                        Picdescribe2: "",
//                        Picture3: "",
//                        Picdescribe3: "",
//                        Px: 121.5341,
//                        Py: 25.01392,
//                        Class: "5",
//                        Map: "",
//                        Parkinginfo: "N/A"
//                },

        //---------------------------------------------------------------
        Collections.sort(json, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonOb1, JSONObject jsonOb2) {
                        // 用多重key 排序
                        String lidCounty = "", ridCounty = "";
                        String lidSiteName = "", ridSiteName = "";
//                String lidPM25="",ridPM25="";
                        try {
                            lidCounty = jsonOb1.getString("Zipcode");
                            ridCounty = jsonOb2.getString("Zipcode");
//                            lidSiteName = jsonOb1.getString("SiteName");
//                            ridSiteName = jsonOb2.getString("SiteName");
//                    整數判斷方法
//                    if(!jsonOb1.getString("PM2.5").isEmpty()&&!jsonOb2.getString("PM2.5").isEmpty()
//                            &&!jsonOb1.getString("PM2.5").equals("ND")&&!jsonOb2.getString("PM2.5").equals("ND")){
//                        lidPM25=String.format("%02d",Integer.parseInt(jsonOb1.getString("PM2.5")));
//                        ridPM25=String.format("%02d",Integer.parseInt(jsonOb2.getString("PM2.5")));
//                    }else{
//                        lidPM25="0";
//                        ridPM25="0";
//                    }
                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }
//                return lidCounty.compareTo(ridCounty)+lidSiteName.compareTo(ridSiteName);
                        return lidCounty.compareTo(ridCounty);
                    }
                }
        );
        return new JSONArray(json);//回傳排序縣市後的array



    }

    private DialogInterface.OnClickListener altDlgOnClkPosiBtnLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            laySwipe.setRefreshing(true);
            mTxtResult.setText(getString(R.string.m2206_name) + "");
            mDesc.setText("");
            mDesc.scrollTo(0, 0);//textview 回頂端
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    setDatatolist();
//----------SwipeLayout 結束 --------
//可改放到最終位置 u_importopendata()

// =================================
//----------SwipeLayout 結束 --------
//可改放到最終位置 u_importopendata()
                    u_loading.setVisibility(View.GONE);
                    laySwipe.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.loadover), Toast.LENGTH_SHORT).show();

                }
            }, 8000);
            //new Handler().postDelayed(new Runnable())
        }
    };

    private DialogInterface.OnClickListener altDlgOnClkNeutBtnLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            u_loading.setVisibility(View.GONE);
            laySwipe.setRefreshing(false);
        }

    };

//----------------------------



    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                this.finish();
                break;
            case R.id.menu_top:
                nowposition = 0;
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_next:
                nowposition = nowposition + 100;
                if (nowposition > total - 1) {
                    nowposition = total - 1;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_back:
                nowposition = nowposition - 100;
                if (nowposition < 0) {
                    nowposition = 0;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_end:
                nowposition = total - 1;
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_load:
                b001on.onRefresh();  //開始轉圈下載資料
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
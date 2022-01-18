package tw.tcnr05.m2200;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class M2200 extends AppCompatActivity {

    private Toolbar toobar;
    private ArrayList<String> mData;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m2201);
        setupViewConpomont();


    }

    private void setupViewConpomont() {
        initToolbar();
        initData();
        initRecyclerView();

    }

    //===============================================
    private void initRecyclerView() {
//1 實例化RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//2 為RecyclerView創建佈局管理器
        mLayoutManager = new LinearLayoutManager(this);
//        -------------------------------------------------
// 這裡使用的是LinearLayoutManager，表示裡面的Item排列是線性排列
//        mRecyclerView.setLayoutManager(mLayoutManager);
//                -------------------------------------------------
//        表格形式的佈局，採用GridView
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4,RecyclerView.VERTICAL,false));
// 第二個參數spanCount表示表格的行數或列數；
// 第三個參數表示是水平滑動或是垂直方向滑動；
// 最後一個參數表示從數據的尾部開始顯示。
//        -------------------------------------------------
//        Adapter 中，第一個參數表示列數或者行數，
//        第二個參數表示滑動方向
//        當然，只做這些收縮是不足夠的，因為對於每個項目視圖而言，它的高度都是一樣的，這樣就達不到瀑布流的效果了，
//        因此，我們要修改每一個項目View的高度，具體實現邏輯如下所示.
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));

//        -------------------------------------------------
        mAdapter = new MyAdapter(mData);
        //3 設置數據Adapter
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initData() {

        mData=new ArrayList<>();
        for (int i=0;i<100;i++){
            mData.add("item"+i);

        }




    }

    private void initToolbar() {
        toobar=(Toolbar)findViewById(R.id.toolbar);
        toobar.setTitle(getString(R.string.app_name)    );
         toobar.setSubtitle(getString(R.string.m2505_demo));
         setSupportActionBar(toobar);
         toobar.setNavigationIcon(R.drawable.net);

         toobar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem item) {
                 switch (item.getItemId()){
                 case R.id.action_search:
                 Toast.makeText(M2200.this, getString(R.string.action_search), Toast.LENGTH_LONG).show();
                 break;
                 case R.id.action_notifications:
                 Toast.makeText(M2200.this, getString(R.string.action_notifications), Toast.LENGTH_SHORT).show();
                 break;
                 case R.id.action_settings:
                 Toast.makeText(M2200.this, getString(R.string.action_settings), Toast.LENGTH_SHORT).show();
                 finish();
                 break;
             }

                 return false;
             }
         });
    }


    //-----------------------------------------------------------------------------------
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
        }
        return super.onOptionsItemSelected(item);
    }
}
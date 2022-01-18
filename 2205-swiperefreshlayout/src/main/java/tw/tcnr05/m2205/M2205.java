package tw.tcnr05.m2205;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class M2205 extends AppCompatActivity {

    private TextView t001, t002;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout laySwipe;
    private String[] list01 = null;
    private String[] list02 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m2205);
        setupViewConpoment();
    }

    private void setupViewConpoment() {

        t001 = (TextView) findViewById(R.id.m2505_t001);
        t002 = (TextView) findViewById(R.id.m2505_t002);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        //---------------------------------------------------
        laySwipe = (SwipeRefreshLayout) findViewById(R.id.laySwipe);
        laySwipe.setSize(SwipeRefreshLayout.LARGE);
        //------拉手機長度一半以上
        laySwipe.setDistanceToTriggerSync(200);
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
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        //---------------------------抓資料
        setimgtolist();
    }

    private void setimgtolist() {
        list01 = getResources().getStringArray(R.array.teacname);
        list02 = getResources().getStringArray(R.array.descr);

        ArrayList<Post> mData = new ArrayList<Post>();
        for (int i = 0; i < list01.length; i++) {
            mData.add(
                    new Post(
                            list01[i],
                            "https://tcnr005.000webhostapp.com/opendata/post_img/t00" + (i + 1) + ".JPG",
                            list02[i]
                    )
            );

        }
        RecycleViewAdapter adapter = new RecycleViewAdapter(this, mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //                Toast.makeText(M2205.this, "onclick" + listName[position], Toast.LENGTH_SHORT).show();
                t001.setText(getString(R.string.m2505_t002) + list01[position]);
                t002.setText(getString(R.string.m2505_descr) + list02[position]);
            }
        });

    }

    //===========================
    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            laySwipe.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "重新載入完成", Toast.LENGTH_SHORT).show();
                    setimgtolist();
                    laySwipe.setRefreshing(false);
                }
            }, 300);
        }
    };

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
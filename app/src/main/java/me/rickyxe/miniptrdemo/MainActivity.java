package me.rickyxe.miniptrdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import me.rickyxe.miniptr.AbsRecyclerAdapter;
import me.rickyxe.miniptr.MiniPtrLayout;


public class MainActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    MiniPtrLayout swipeRefreshLayout;
    View loadMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter adapter = new UserAdapter(this, UserAdapter.buildDataList());
        adapter.setOnListItemClickListener(new AbsRecyclerAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.i("test", "item click!!! : " + position);
            }
        });
        mainRecyclerView.setAdapter(adapter);

        loadMoreView = (View) findViewById(R.id.load_more);

        swipeRefreshLayout = (MiniPtrLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setHasMore(true);
        swipeRefreshLayout.setLoadMoreView(loadMoreView);
        swipeRefreshLayout.setOnLoadListener(new MiniPtrLayout.OnLoadAndRefreshListener() {
            @Override
            public void onLoadMore() {
                Log.i("test", "Load more");
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.loadFinish(false, false);
                    }
                }, 2000);
            }

            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.loadFinish(false, true);
                    }
                }, 3000);
            }
        });

    }


}

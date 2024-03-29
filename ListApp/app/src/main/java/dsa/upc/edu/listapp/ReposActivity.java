package dsa.upc.edu.listapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;
import android.support.design.widget.TextInputEditText;

import java.util.List;

import dsa.upc.edu.listapp.github.Contributor;
import dsa.upc.edu.listapp.github.GitHub;
import dsa.upc.edu.listapp.github.Repo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Based on http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
//      and https://zeroturnaround.com/rebellabs/getting-started-with-retrofit-2/

public class ReposActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReposAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final String TAG = ReposActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = findViewById(R.id.my_swipe_refresh);

        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(ReposActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the adapter
        adapter = new ReposAdapter();
        recyclerView.setAdapter(adapter);

        String nomUsuari = getIntent().getStringExtra("nomUsuari");
        if (nomUsuari == null){
            finish();
        }

        doApiCall(swipeRefreshLayout, nomUsuari);

        //Manage swipe on items
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        adapter.remove(viewHolder.getAdapterPosition());
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        doApiCall(swipeRefreshLayout, nomUsuari);
                    }
                }
        );

    }

    private void doApiCall(final SwipeRefreshLayout mySwipeRefreshLayout, String nomUsuari) {
        GitHub gitHubService = GitHub.retrofit.create(GitHub.class);
        Call<List<Repo>> call = gitHubService.repos(nomUsuari);

        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                // set the results to the adapter
                adapter.setData(response.body());

                if(mySwipeRefreshLayout!=null) mySwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                if(mySwipeRefreshLayout!=null) mySwipeRefreshLayout.setRefreshing(false);

                String msg = "Error in retrofit: "+t.toString();
                Log.d(TAG,msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
            }
        });
    }
}
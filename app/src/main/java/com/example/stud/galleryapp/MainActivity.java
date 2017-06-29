package com.example.stud.galleryapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ListAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent detailsActivity = new Intent(MainActivity.this, DetailsActivity.class);
                detailsActivity.putExtra("imageNumber", position);
                startActivity(detailsActivity);


            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    View itemView = view.getChildAt(0);
                    int top = Math.abs(itemView.getTop());
                    int bottom = Math.abs(itemView.getBottom());
                    int scrollBy = top >= bottom ? bottom : -top;
                    if (scrollBy == 0) {
                        return;
                    }
                    smoothScrollDeferred(scrollBy, (GridView) view);
                }
            }

            private void smoothScrollDeferred(final int scrollByF,
                                              final GridView viewF) {
                final Handler h = new Handler();
                h.post(new Runnable() {

                    @Override
                    public void run() {
                        viewF.smoothScrollBy(scrollByF, 300);
                    }
                });
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

    }
}

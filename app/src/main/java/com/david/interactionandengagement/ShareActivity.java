package com.david.interactionandengagement;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {
	SwipeRefreshLayout swipeRefreshLayout;
	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
		text = (TextView) findViewById(R.id.swipedText);

		swipeRefreshLayout.setOnRefreshListener(
				new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {

						// This method performs the actual data-refresh operation.
						// The method calls setRefreshing(false) when it's finished.
						updateOperation();
					}
				}
		);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.swipe, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			// Check if user triggered a refresh:
			case R.id.menu_refresh:

				// Signal SwipeRefreshLayout to start the progress indicator
				swipeRefreshLayout.setRefreshing(true);

				// Start the refresh background task.
				// This method calls setRefreshing(false) when it's finished.
				updateOperation();

				return true;
		}

		// User didn't trigger a refresh, let the superclass handle this action
		return super.onOptionsItemSelected(item);

	}


	private void updateOperation(){
		new Thread(new Runnable() {
			@Override
			synchronized public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (text != null) {
							if (text.getVisibility() == View.VISIBLE) {
								text.setVisibility(View.GONE);
							} else {
								text.setVisibility(View.VISIBLE);
							}
						}
					}
				});


				try {
					wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						swipeRefreshLayout.setRefreshing(false);
					}
				});
			}
		}).start();

	}
}

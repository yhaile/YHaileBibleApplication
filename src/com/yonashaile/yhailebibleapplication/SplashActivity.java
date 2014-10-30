package com.yonashaile.yhailebibleapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	private static String TAG = SplashActivity.class.getName();
 
	// Sleep for some time
	private static long SLEEP_TIME = 7;   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		 // Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   

		setContentView(R.layout.splash_layout);

		// Start timer and launch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	private class IntentLauncher extends Thread {
		@Override
		/**
		 * Sleep for some time and than start new activity.
		 */
		public void run() {
			try {
        // 	Sleeping
		Thread.sleep(SLEEP_TIME*1000);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			// Start main activity
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
	}
}
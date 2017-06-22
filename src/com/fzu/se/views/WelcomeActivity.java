package com.fzu.se.views;



import com.codecontacts.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

public class WelcomeActivity extends Activity {
	private TextView tv_num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		final Intent intent=new Intent(this, MainActivity.class);
		
		Thread t=new Thread();
		t.start();
		//这个线程只是让它停止3秒
		new Thread(){
			public void run(){
				try {
					this.sleep(1000);
					startActivity(intent);
					finish();
				
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}.start();
		
	}
}

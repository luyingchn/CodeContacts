package com.fzu.se.views;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.codecontacts.R;
import com.codecontacts.R.id;
import com.codecontacts.R.layout;
import com.google.zxing.WriterException;
import com.google.zxing.encoding.EncodingHandler;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CodepictureActivity extends Activity {

	private String contentString = "";
	private ImageView iv;
	private Button btSave, btBack;

	private Bitmap qrCodeBitmap;
	private Bitmap b2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codepicture);

		Intent intent = getIntent();
		contentString = intent.getStringExtra("ctstring");

		iv = (ImageView) findViewById(R.id.iv);
		btSave = (Button) findViewById(R.id.bt_save);
		btBack = (Button) findViewById(R.id.bt_back);

		try {
			qrCodeBitmap = EncodingHandler.createQRCode(contentString, 600);
			iv.setImageBitmap(qrCodeBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		btSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 保存图片到本地的方法
				// System.out.println("contentString:"+contentString);
				Toast.makeText(CodepictureActivity.this,
						"成功！请进入SD根目录查看", Toast.LENGTH_LONG).show();

				saveImage();
				
			}
		});
		btBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CodepictureActivity.this,
						MainActivity.class));
				finish();
			}
		});

	}
	
	/**
	 * 得到图片缩略图
	 * 
	 * @param bitmap
	 */
	public void saveImage() {
		//处理图片
				 b2 = BitmapFactory.decodeResource(getResources(),
				   R.drawable.white);
				   
				 if (!b2.isMutable()) {
				   //设置图片为背景为透明
				  b2 = b2.copy(Bitmap.Config.ARGB_8888, true);
				 }
				 Paint paint = new Paint();
				 Canvas canvas = new Canvas(b2);
				 canvas.drawBitmap(qrCodeBitmap, 0, 0, paint);//叠加新图qrCodeBitmap
				 canvas.save(Canvas.ALL_SAVE_FLAG);
				 canvas.restore();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.i("MyTag", "保存方法执行后2");
					File f = new File(
							Environment.getExternalStorageDirectory().getAbsolutePath() +
							File.separator + Const.getTime()+ ".png");
					Log.i("MyTag3", "保存成功"+contentString);
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
					b2.compress(CompressFormat.PNG, 100, bos);
					
					bos.flush();
					bos.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}
class Const {
	public static String getTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()).toString();
	} 
}


package com.fzu.se.views;

import com.codecontacts.R;
import com.codecontacts.R.id;
import com.codecontacts.R.layout;
import com.codecontacts.R.menu;
import com.google.zxing.WriterException;
import com.google.zxing.encoding.EncodingHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MakeActivity extends Activity {

	private EditText etName;
	private EditText etPhone1, etPhone2;
	private EditText etTel;
	private EditText etWeibo;
	private EditText etEmail;
	private EditText etAddress;
	private EditText etRemark;

	private Button etGenerate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make);

		etName = (EditText) findViewById(R.id.et_name);
		etPhone1 = (EditText) findViewById(R.id.et_phone1);

		etGenerate = (Button) findViewById(R.id.bt_generate);
		etGenerate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 获取所有数据并由*串成字符串，开头加入#；
				if(etName.getText().toString().equals("")){
					Toast.makeText(MakeActivity.this, "联系人姓名不能为空", Toast.LENGTH_SHORT).show();
					//让联系人姓名输入框获取焦点
					etName.requestFocus();
				}
				else if(etPhone1.getText().toString().equals("")){
					Toast.makeText(MakeActivity.this, "联系人电话1不能为空", Toast.LENGTH_SHORT).show();
					//让联系人姓名输入框获取焦点
					etPhone1.requestFocus();
				}else {
					String contentString = "#" + etName.getText().toString() + "*"
							+ etPhone1.getText().toString();
					Log.i("MyTag2", "contentString:"+contentString);
				
					Intent intent = new Intent(MakeActivity.this,
							CodepictureActivity.class);
					intent.putExtra("ctstring", contentString);
					startActivity(intent);
					finish();
				}
	
			}
		});

		/**/
	}

}

package com.fzu.se.views;

import java.util.ArrayList;

import com.codecontacts.R;
import com.codecontacts.R.id;
import com.codecontacts.R.layout;
import com.google.zxing.WriterException;
import com.google.zxing.encoding.EncodingHandler;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button bGetCode;
	private Button bSetCode;
	private Button bAbout;
	
	private Button pw_bt_save;
	private Button pw_bt_cancle;
	
	private PopupWindow pw;
	
	private TextView pw_tv_name;
	private TextView pw_tv_phone1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bGetCode = (Button) findViewById(R.id.bt_getcode);
		bSetCode = (Button) findViewById(R.id.bt_setcode);
		bAbout = (Button) findViewById(R.id.bt_about);

		bGetCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 打开扫描界面扫描条形码或二维码
				Intent getCodeIntent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivityForResult(getCodeIntent, 0);
			}
		});
		// 制作二维码的按钮
		bSetCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入二维码制作界面
				Intent setCodeIntent = new Intent(MainActivity.this,
						MakeActivity.class);
				startActivity(setCodeIntent);
			}
		});
		bAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aboutIntent=new Intent(MainActivity.this,AboutActivity.class);
				startActivity(aboutIntent);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			Log.i("MyLog", "Message：" + scanResult);
			// 以下应该处理这个数据，解析出之后打开联系人界面并输入，检测是否字符#开头，否则不是自己存储的联系人。
			if (scanResult.startsWith("#")) {
				// Toast.makeText(this, "扫到通讯录信息", Toast.LENGTH_SHORT).show();
				// 打开对话框(扫描以下通讯录信息，是否保存联系人？)
				
				/*final AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setIcon(R.drawable.codeico);
				builder.setTitle("扫描到联系人信息");

				// 对二维码数据字符串分割处理
				scanResult = scanResult.replace("#", "");// 先去掉头标示字符
				String[] strarray = scanResult.split("\\*");// 需要转义，否则异常。java.util.regex.PatternSyntaxException:
															// Syntax error in
															// regexp pattern
															// near index 1:

				final String name = strarray[0];// 姓名
				final String phone = strarray[1];// 手机

				builder.setMessage("姓名：" + name + "\n" + "手机：" + phone);
				builder.setPositiveButton("保 存",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface di, int wi) {
								// TODO Auto-generated method stub
								// 保存进联系人的操作
								contactsInsert(name,phone);
								Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
							}
						});
				builder.setNegativeButton("放 弃",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// 不操作
							}
						});
				builder.create().show();
				*/
				
				
				//创建popwindow
				View popview=getLayoutInflater().inflate(R.layout.pw_info, null);//popwindow的布局
				pw=new PopupWindow(popview,getWindowManager().getDefaultDisplay().getWidth(),
						getWindowManager().getDefaultDisplay().getHeight()-getStatusBarHeight());
				
				//显示popwindow前把数据填充
				// 对二维码数据字符串分割处理
				scanResult = scanResult.replace("#", "");// 先去掉头标示字符
				String[] strarray = scanResult.split("\\*");// 需要转义，否则异常。java.util.regex.PatternSyntaxException:
															// Syntax error in
															// regexp pattern
															// near index 1:

				final String name = strarray[0];// 姓名
				final String phone = strarray[1];// 手机

				pw_tv_name=(TextView)popview.findViewById(R.id.pw_tv_name);//注意这个地方
				pw_tv_phone1=(TextView)popview.findViewById(R.id.pw_tv_phone1);
				
				pw_tv_name.setText(name);
				pw_tv_phone1.setText(phone);
				
				//显示popwindow
				View parent=getLayoutInflater().inflate(R.layout.activity_main, null);//父界面布局
				pw.showAtLocation(parent, Gravity.CENTER, 0, 20);//getWindowManager().getDefaultDisplay().getWidth(); 
				
				pw_bt_save=(Button)popview.findViewById(R.id.pw_bt_save);
				pw_bt_save.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pw.dismiss();
						contactsInsert(name,phone);
						Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
					
					}
				});
				pw_bt_cancle=(Button)popview.findViewById(R.id.pw_bt_cancle);
				pw_bt_cancle.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pw.dismiss();
					}
				});

			} else {
				Toast.makeText(this, "抱歉，非通讯录信息：" + scanResult,
						Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(MainActivity.this, "扫描失败！", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	//添加联系人的方法
	public void contactsInsert(String iname,String iphone){
        ContentValues values = new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri = this.getContentResolver().insert(RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        
        //往data表入姓名数据
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        values.put(StructuredName.GIVEN_NAME, iname);
        this.getContentResolver().insert(
                android.provider.ContactsContract.Data.CONTENT_URI, values);
        
        //往data表入电话数据
        values.clear();
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, iphone);
        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
        this.getContentResolver().insert(
                android.provider.ContactsContract.Data.CONTENT_URI, values);

        //往data表入Email数据
       /* values.clear();
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
        values.put(Email.DATA, "luyingchn@163.com");
        values.put(Email.TYPE, Email.TYPE_WORK);
        this.getContentResolver().insert(
                android.provider.ContactsContract.Data.CONTENT_URI, values);*/
        //往data表中插入头像数据
        //头像需要自己通过获取的姓名来制作，即名字的最后一个字符然后放到某个二维码的前面。
        //...
    }
	//获取通知栏高度
	private int getStatusBarHeight(){
	    Rect rect = new Rect();
	    getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
	    return rect.top;
	 }

}

package com.cfkj.earthworkproj;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cfkj.earthworkproj.net.OkHttpClientManager;
import com.cfkj.earthworkproj.net.ResultCallback;
import com.cfkj.earthworkproj.util.MainConfig;
import com.cfkj.earthworkproj.util.MyPreferenceUtil;
import com.squareup.okhttp.Request;

public class LoginActivity extends Activity implements OnClickListener,
		ResultCallback {
	Button submit_btn;
	EditText phone_et, psd_et;
	TextView getpsd_tv, regist_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setContentView(R.layout.login_activity);
		initView();
	}

	private void initView() {
		submit_btn = (Button) findViewById(R.id.login_btn);
		phone_et = (EditText) findViewById(R.id.login_phone_et);
		psd_et = (EditText) findViewById(R.id.login_psd_et);
		getpsd_tv = (TextView) findViewById(R.id.login_forgetpsd_tv);
		regist_tv = (TextView) findViewById(R.id.login_regist_tv);
		submit_btn.setOnClickListener(this);
		getpsd_tv.setOnClickListener(this);
		regist_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_btn:
			if (phone_et.getText() == null
					|| phone_et.getText().toString().equals("")) {
				Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_LONG).show();
				return;
			}
			if (psd_et.getText() == null
					|| psd_et.getText().toString().equals("")) {
				Toast.makeText(this, "请输入密码！", Toast.LENGTH_LONG).show();
				return;
			}
			submit();
			break;
		case R.id.login_forgetpsd_tv:
			Intent intent = new Intent(this, GetPSD_Activity.class);
			startActivity(intent);
			break;
		case R.id.login_regist_tv:
			Intent intent1 = new Intent(this, RegistActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	private void submit() {
		HashMap<String, String> params = new HashMap<String, String>();
		String phone = phone_et.getText().toString();
		String psd = psd_et.getText().toString();
		
		params.put("type", "login");
		params.put("phone", phone);
		params.put("password", psd);
	
		try {
			OkHttpClientManager.postAsyn(MainConfig.baseUrl
					+ "/mobile_query.jsp", this, 0, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onError(Request request, Exception e, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponse(Object response, int index) {
		// TODO Auto-generated method stub
		String result = response.toString();
		Log.i("tag", "result = " + result);
		if (result != null && result.trim().equals("OK")) {
			MainConfig.userPhone = phone_et.getText().toString();
			try {
				MyPreferenceUtil util = new MyPreferenceUtil(this);
				JSONObject json = new JSONObject();
				json.put("phone", MainConfig.userPhone);
				json.put("psd", psd_et.getText().toString());
				util.saveStringVlue(MainConfig.login_info, json.toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		} else {
			Toast.makeText(this, "登陆失败！", Toast.LENGTH_LONG).show();
		}
	}
}

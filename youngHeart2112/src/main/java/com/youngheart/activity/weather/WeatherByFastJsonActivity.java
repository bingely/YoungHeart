package com.youngheart.activity.weather;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.infrastructure.net.RequestCallback;
import com.infrastructure.net.RequestParameter;
import com.youngheart.R;
import com.youngheart.base.AppBaseActivity;
import com.youngheart.engine.RemoteService;
import com.youngheart.entity.WeatherInfo;
import com.youngheart.utils.Utils;

public class WeatherByFastJsonActivity extends AppBaseActivity {
	TextView tvCity;
	TextView tvCityId;

	private RequestCallback weatherCallback = null;

	@Override
	protected void initVariables() {
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_weather);

		tvCity = (TextView) findViewById(R.id.tvCity);
		tvCityId = (TextView) findViewById(R.id.tvCityId);
	}

	@Override
	protected void loadData() {
        dlg = Utils.createProgressDialog(this, 
        		this.getString(R.string.str_loading));
        dlg.show();
        
        loadAPIData1();
	}
	
	void loadAPIData1() {
		// 示例1：共用AppBaseActivity中的onFail方法
		weatherCallback = new AbstractRequestCallback() {

			@Override
			public void onSuccess(String content) {
                WeatherInfo weatherInfo = JSON.parseObject(content,
						WeatherInfo.class);
				if (weatherInfo != null) {
					tvCity.setText(weatherInfo.getCity());
					tvCityId.setText(weatherInfo.getCityid());
				}
				
				//接下来调用MobileAPI2，由于是同一MobileAPI，所以从缓存读取
		        loadAPIData2();
			}
		};
		// 原本可以使用HashMap<String,String>这样的数据结构，但是它比较耗费内存（速度快），参数的数据一般不会多
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		RequestParameter rp3 = new RequestParameter("cityAa2", "111");
		RequestParameter rp1 = new RequestParameter("cityaA", "111");
		RequestParameter rp2 = new RequestParameter("cityName", "Beijing");
		params.add(rp1);
		params.add(rp3);
		params.add(rp2);

		RemoteService.getInstance().invoke(this, "getWeatherInfo", params,
				weatherCallback);
	}
	
	void loadAPIData2() {
		// 示例2：复写AppBaseActivity中的onFail方法
		weatherCallback = new AbstractRequestCallback() {

			@Override
			public void onSuccess(String content) {
                dlg.dismiss();

                WeatherInfo weatherInfo = JSON.parseObject(content,
						WeatherInfo.class);
				if (weatherInfo != null) {
					tvCity.setText(weatherInfo.getCity());
					tvCityId.setText(weatherInfo.getCityid());
				}
			}

			@Override
			public void onFail(String errorMessage) {
                dlg.dismiss();

				// 重启App或者啥都不做
			}
		};
		
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		RequestParameter rp1 = new RequestParameter("cityaA2", "111");
		RequestParameter rp3 = new RequestParameter("cityAa", "111");
		RequestParameter rp2 = new RequestParameter("cityName", "Beijing");
		params.add(rp1);
		params.add(rp3);
		params.add(rp2);

		RemoteService.getInstance().invoke(this, "getWeatherInfo", params,
				weatherCallback);
	}
}

/**
 * Project Name:XPGSdkV4AppBase
 * File Name:MainControlActivity.java
 * Package Name:com.gizwits.aircondition.activity.control
 * Date:2015-1-27 14:44:17
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.yj.smarthome.activity.control;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yj.common.system.IntentUtils;
import com.yj.smarthome.R;
import com.yj.smarthome.framework.activity.BaseActivity;
import com.yj.smarthome.framework.activity.account.ChangePswActivity;
import com.yj.smarthome.framework.activity.account.UserManageActivity;
import com.yj.smarthome.framework.activity.device.DeviceListActivity;
import com.yj.smarthome.framework.adapter.MenuDeviceAdapter;
import com.yj.smarthome.framework.config.JsonKeys;
import com.yj.smarthome.framework.entity.DeviceAlarm;
import com.yj.smarthome.framework.sdk.CmdCenter;
import com.yj.smarthome.framework.utils.DensityUtil;
import com.yj.smarthome.framework.utils.DialogManager;
import com.yj.smarthome.framework.utils.StringUtils;
import com.yj.smarthome.framework.utils.DialogManager.OnTimingChosenListener;
import com.yj.smarthome.framework.widget.CircularSeekBar;
import com.yj.smarthome.framework.widget.ColorArcProgressBar;
import com.yj.smarthome.framework.widget.SlideSwitchView;
import com.yj.smarthome.framework.widget.SlideSwitchView.OnSwitchChangedListener;
import com.yj.smarthome.framework.widget.WaveLoadingView;
import com.ipcamera.demo.AddCameraActivity;
import com.ipcamera.demo.StartActivity;
import com.xtremeprog.xpgconnect.XPGWifiDevice;

// TODO: Auto-generated Javadoc
/**
 * Created by Lien on 14/12/21.
 * 
 * 设备主控界面
 * 
 * @author Lien
 */
public class MainControlActivity extends BaseActivity implements OnClickListener
		 {
	private TextView tv_camera;
	private WaveLoadingView waveLoadingViewVoice;
	private SeekBar voiceMaxSeekbar;
	private TextView tv_voice;
	private LinearLayout linear_voice_max;
	
	private SeekBar airConditionSeekBar;
	private TextView tv_aircondition;
	private ColorArcProgressBar hotWaterTempBar;
	private WaveLoadingView waveLoadingView;
	private SeekBar envirmentMaxSeekbar;
	private TextView tv_temp_max;
	private LinearLayout linear_temp_max;
	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private PagerTabStrip pagerTabStrip;
	private ViewPager mViewPager;
	private MyPagerAdapter myPagerAdapter;
	//客厅里的灯开关
	private SlideSwitchView ledSwitchView;
	private static String[] titles= {"家居状态","环境状态"};
	/** The tag. */
	private final String TAG = "MainControlActivity";
	private RelativeLayout rlControlMainPage;
	/** The iv menu. */
	private ImageView ivMenu;
	/** The tv title. */
	private TextView tvTitle;
	/** The rl alarm tips. */
	private RelativeLayout rlAlarmTips;
	/** The tv alarm tips count. */
	private TextView tvAlarmTipsCount;
	/** The m adapter. */
	private MenuDeviceAdapter mAdapter;
	
	/** The is show. */
	private boolean isShow;
	/** The device data map. */
	private ConcurrentHashMap<String, Object> deviceDataMap;
	/** The statu map. */
	private ConcurrentHashMap<String, Object> statuMap;
	/** The alarm list. */
	private ArrayList<DeviceAlarm> alarmList;
	/** The alarm list has shown. */
	private ArrayList<String> alarmShowList;
	/** The m fault dialog. */
	private Dialog mFaultDialog;
	/** The progress dialog. */
	private ProgressDialog progressDialogRefreshing;
	/** The disconnect dialog. */
	private Dialog mDisconnectDialog;
	/** 是否超时标志位 */
	private boolean isTimeOut = false;
	/** 获取状态超时时间 */
	private int GetStatueTimeOut = 30000;
	/** 登陆设备超时时间 */
	private int LoginTimeOut = 5000;

	/**
	 * ClassName: Enum handler_key. <br/>
	 * <br/>
	 * date: 2014-11-26 17:51:10 <br/>
	 * 
	 * @author Lien
	 */
	private enum handler_key {

		/** 更新UI界面 */
		UPDATE_UI,

		/** 显示警告 */
		ALARM,

		/** 设备断开连接 */
		DISCONNECTED,

		/** 接收到设备的数据 */
		RECEIVED,

		/** 获取设备状态 */
		GET_STATUE,

		/** 获取设备状态超时 */
		GET_STATUE_TIMEOUT,

		/** The login start. */
		LOGIN_START,

		/**
		 * The login success.
		 */
		LOGIN_SUCCESS,

		/**
		 * The login fail.
		 */
		LOGIN_FAIL,

		/**
		 * The login timeout.
		 */
		LOGIN_TIMEOUT,
	}

	/**
	 * The handler.
	 */
private MyHandler handler = new MyHandler(this) ;
	
	
	private static class MyHandler extends Handler{
		private SoftReference<MainControlActivity> softReference;
		public MyHandler(MainControlActivity context){
			softReference = new SoftReference<MainControlActivity>(context);
		}
		public void handleMessage(Message msg) {
			MainControlActivity activity =  softReference.get();
			if(activity!=null){
				super.handleMessage(msg);
				handler_key key = handler_key.values()[msg.what];
				switch (key) {
				case RECEIVED:
					if (activity.deviceDataMap == null) {
						return;
					}
					try {
						if (activity.deviceDataMap.get("data") != null) {
							Log.i("info", (String) activity.deviceDataMap.get("data"));
							activity.inputDataToMaps(activity.statuMap, (String) activity.deviceDataMap.get("data"));
							//System.out.println(statuMap);
						}
						activity.alarmList.clear();
						if (activity.deviceDataMap.get("alters") != null) {
							Log.i("info", (String) activity.deviceDataMap.get("alters"));
							// 返回主线程处理报警数据刷新
							activity.inputAlarmToList((String) activity.deviceDataMap.get("alters"));
						}
						if (activity.deviceDataMap.get("faults") != null) {
							Log.i("info", (String) activity.deviceDataMap.get("faults"));
							// 返回主线程处理错误数据刷新
							activity.inputAlarmToList((String)activity. deviceDataMap.get("faults"));
						}
						// 返回主线程处理P0数据刷新
						sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
						sendEmptyMessage(handler_key.ALARM.ordinal());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				case UPDATE_UI:
					if (activity.statuMap != null && activity.statuMap.size() > 0) {
						Log.i("Apptest", activity.statuMap.toString());
						removeMessages(handler_key.GET_STATUE_TIMEOUT.ordinal());
						DialogManager.dismissDialog(activity, activity.progressDialogRefreshing);
						activity.upDateUi();
					}
					
					break;
				case ALARM:
					// 是否需要弹dialog判断
					boolean isNeedDialog = false;
					for (DeviceAlarm alarm : activity.alarmList) {
						if (!activity.alarmShowList.contains((String) alarm.getDesc())) {
							activity.alarmShowList.add(alarm.getDesc());
							isNeedDialog = true;
						}
					}

					activity.alarmShowList.clear();

					for (DeviceAlarm alarm : activity.alarmList) {
						activity.alarmShowList.add(alarm.getDesc());
					}
					if (activity.alarmList != null && activity.alarmList.size() > 0) {
						if (isNeedDialog) {
							DialogManager.showDialog(activity, activity.mFaultDialog);
						}
						activity.setTipsLayoutVisiblity(true, activity.alarmList.size());
					} else {
						activity.setTipsLayoutVisiblity(false, 0);
					}
					break;
				case DISCONNECTED:
					if (!activity.drawerLayout.isDrawerOpen(Gravity.LEFT )) {
					
						DialogManager.dismissDialog(activity, activity.progressDialogRefreshing);
						DialogManager.dismissDialog(activity, activity.mFaultDialog);
						
						DialogManager.showDialog(activity, activity.mDisconnectDialog);
					}
					break;
				case GET_STATUE:
					activity.mCenter.cGetStatus(mXpgWifiDevice);
					break;
				case GET_STATUE_TIMEOUT:
					sendEmptyMessage(handler_key.DISCONNECTED.ordinal());
					break;
				case LOGIN_SUCCESS:
					removeMessages(handler_key.LOGIN_TIMEOUT.ordinal());
					activity.refreshMainControl();
					break;
				case LOGIN_FAIL:
					removeMessages(handler_key.LOGIN_TIMEOUT.ordinal());
					sendEmptyMessage(handler_key.DISCONNECTED.ordinal());
					break;
				case LOGIN_TIMEOUT:
					activity.isTimeOut = true;
					sendEmptyMessage(handler_key.DISCONNECTED.ordinal());
					break;
				}
			}
		}
	
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gizwits.aircondition.activity.BaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_control);
		initViews();
		initEvents();
		initParams();
	}
	@Override
	protected void didReceiveData(XPGWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int result) {
		if (!device.getDid().equalsIgnoreCase(mXpgWifiDevice.getDid()))
			return;

		this.deviceDataMap = dataMap;
		handler.sendEmptyMessage(handler_key.RECEIVED.ordinal());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gizwits.aircondition.activity.BaseActivity#onResume()
	 */
	@Override
	public void onResume() {
		
		if (drawerLayout.isDrawerOpen(Gravity.LEFT )) {
	
			refreshMenu();
		} else {
			if (!mDisconnectDialog.isShowing())
				refreshMainControl();
		}
		super.onResume();
	}

	/**
	 * 更新菜单界面.
	 * 
	 * @return void
	 */
	private void refreshMenu() {
		initBindList();
		mAdapter.setChoosedPos(-1);
		for (int i = 0; i < bindlist.size(); i++) {
			if (bindlist.get(i).getDid().equalsIgnoreCase(mXpgWifiDevice.getDid()))
				mAdapter.setChoosedPos(i);
		}
		// 当前绑定列表没有当前操作设备
		if (mAdapter.getChoosedPos() == -1) {
			mAdapter.setChoosedPos(0);
			mXpgWifiDevice = mAdapter.getItem(0);
			alarmList.clear();
		}
		mAdapter.notifyDataSetChanged();
		int px = DensityUtil.dip2px(this, mAdapter.getCount() * 50);
		
	}

	/**
	 * 更新主控制界面.
	 * 
	 * @return void
	 */
	private void refreshMainControl() {
		mXpgWifiDevice.setListener(deviceListener);
		DialogManager.showDialog(this, progressDialogRefreshing);
		handler.sendEmptyMessageDelayed(handler_key.GET_STATUE_TIMEOUT.ordinal(), GetStatueTimeOut);
		handler.sendEmptyMessage(handler_key.GET_STATUE.ordinal());
	}

	/**
	 * Inits the views.
	 */
	private void initViews() {
		tv_camera = (TextView)findViewById(R.id.tvCamera);
		mViewPager = (ViewPager)findViewById(R.id.pager);
		
        pagerTabStrip=(PagerTabStrip) findViewById(R.id.pager_tab_strip);
        // 设置标签字体
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
        //设置标签下划线的颜色
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.indicatorcolor));
        
        myPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(myPagerAdapter);
        
		rlAlarmTips = (RelativeLayout) findViewById(R.id.rlAlarmTips);
		tvAlarmTipsCount = (TextView) findViewById(R.id.tvAlarmTipsCount);
		drawerLayout = (DrawerLayout)findViewById(R.id.main_layout);
		rlControlMainPage = (RelativeLayout) findViewById(R.id.rlControlMainPage);
		ivMenu = (ImageView) findViewById(R.id.ivMenu);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		mFaultDialog = DialogManager.getDeviceErrirDialog(MainControlActivity.this, "设备报警", new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:10010"));
				startActivity(intent);
				mFaultDialog.dismiss();
			}
		});
		mAdapter = new MenuDeviceAdapter(this, bindlist);
		
		progressDialogRefreshing = new ProgressDialog(MainControlActivity.this);
		progressDialogRefreshing.setMessage("正在更新状态,请稍后");
		progressDialogRefreshing.setCancelable(false);
		mDisconnectDialog = DialogManager.getDisconnectDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogManager.dismissDialog(MainControlActivity.this, mDisconnectDialog);
				IntentUtils.getInstance().startActivity(MainControlActivity.this, DeviceListActivity.class);
				finish();
			}
		});
		 //切换侧滑菜单的开关
		 drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer_am,
	                R.string.open_darwlayout,
	                R.string.close_darwlayout){
	            @Override
	            public void onDrawerClosed(View drawerView) {
	                super.onDrawerClosed(drawerView);
	            }
	            @Override
	            public void onDrawerOpened(View drawerView) {
	                super.onDrawerOpened(drawerView);
	            }
	        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
	}
	
	 
	/**
	 * Inits the events.
	 */
	private void initEvents() {
		
		rlAlarmTips.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
		//tvTitle.setOnClickListener(this);
		tv_camera.setOnClickListener(this);
	}
	/**
	 * Inits the params.
	 */
	private void initParams() {

		statuMap = new ConcurrentHashMap<String, Object>();
		alarmList = new ArrayList<DeviceAlarm>();
		alarmShowList = new ArrayList<String>();
		refreshMenu();
		refreshMainControl();
	}
	/**
	 * 界面更新ui操作
	 */
	private void upDateUi() {
		//客厅的开关
		ledSwitchView.setChecked((Boolean) statuMap.get(JsonKeys.LED_ON_OFF));
		//热水器开关
		boolean isHotWaterOpen = (Boolean) statuMap.get(JsonKeys.HOTWATER_ON_OFF);
		if(!isHotWaterOpen){
			hotWaterTempBar.setTitle("热水器未打开,点击打开");
			hotWaterTempBar.setCurrentValues(0);
		}else{
			hotWaterTempBar.setTitle("当前热水器温度");
			hotWaterTempBar.setCurrentValues(Float.parseFloat( (String) statuMap.get(JsonKeys.HOTWATER_TEMP)));
		}
		boolean isAirconditionOpen = (Boolean) statuMap.get(JsonKeys.AIRCONDITION_ON_OFF);
		if(!isAirconditionOpen){
			tv_aircondition.setText("空调未打开，点击打开");
			airConditionSeekBar.setProgress(0);
		}else{
			int airconditionTemp = Integer.parseInt((String)statuMap.get(JsonKeys.AIRCONDITION_TEMP));
			tv_aircondition.setText("空调已打开,设置温度为"+airconditionTemp+"摄氏度");
			airConditionSeekBar.setProgress(airconditionTemp-12);
		}
		
		
		//环境温度
		int currentTemp = Integer.parseInt((String) statuMap.get(JsonKeys.ENVIRMENT_TEMPTURE));
		int currentTempBorder = Integer.parseInt((String) statuMap.get(JsonKeys.ENVIRMENT_TEMPTURE_BORDER));
		
		if(currentTemp>=currentTempBorder){
			waveLoadingView.setProgressValue(100);
		}else{
			int progress = (int) (currentTemp*100*1.0/currentTempBorder);
			waveLoadingView.setProgressValue(progress);
		}
		
		waveLoadingView.setCenterTitle(currentTemp+"摄氏度");
		//超声波传感器
		
		float currentVoice = Float.parseFloat((String) statuMap.get(JsonKeys.VOICE));
		float currentVoiceBorder = Float.parseFloat((String) statuMap.get(JsonKeys.VOICE_BORDER));
		
		if(currentVoice>=currentVoiceBorder){
			waveLoadingViewVoice.setProgressValue(100);
		}else{
			int progress = (int) (currentVoice*100/currentVoiceBorder);
			waveLoadingViewVoice.setProgressValue(progress);
		}
		
		waveLoadingViewVoice.setCenterTitle(currentVoice+"");
		
	}
	/**
	 * 主显示界面viewpager的adapter
	 * @author Administrator
	 *
	 */
	private class MyPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			return titles.length;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titles[position];
		}
		 @Override
	     public Object instantiateItem(ViewGroup container, int position) {
	            View view ;
	            if(position==0){
	            	view = View.inflate(MainControlActivity.this, R.layout.pager_home_info, null);
	            	ledSwitchView = (SlideSwitchView)view.findViewById(R.id.mSlideSwitchView);
	            	hotWaterTempBar = (ColorArcProgressBar)view.findViewById(R.id.bar2);
	            	airConditionSeekBar = (SeekBar) view.findViewById(R.id.airconditionSeekbar);
	            	tv_aircondition = (TextView)view.findViewById(R.id.tv_aircondition);
	            	hotWaterTempBar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							boolean isOpen = (Boolean) statuMap.get(JsonKeys.HOTWATER_ON_OFF);
							if(isOpen){
								//关闭
								hotWaterTempBar.setTitle("热水器未打开,点击打开");
								hotWaterTempBar.setCurrentValues(0);
								mCenter.cHotwaterSwitchOn(mXpgWifiDevice, false);
							}else{
								//打开
								hotWaterTempBar.setTitle("当前热水器温度");
								mCenter.cHotwaterSwitchOn(mXpgWifiDevice, true);
							}
							mCenter.cGetStatus(mXpgWifiDevice);
						}
					});
	            	ledSwitchView.setOnChangeListener(new OnSwitchChangedListener() {
						@Override
						public void onSwitchChange(SlideSwitchView switchView, boolean isChecked) {
							mCenter.cLedSwitchOn(mXpgWifiDevice, isChecked);
						}
					});
	            	tv_aircondition.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							boolean isAirconditionOpen = (Boolean) statuMap.get(JsonKeys.AIRCONDITION_ON_OFF);
							if(!isAirconditionOpen){
								int airconditionTemp = Integer.parseInt((String)statuMap.get(JsonKeys.AIRCONDITION_TEMP));
								tv_aircondition.setText("空调已打开,设置温度为"+airconditionTemp+"摄氏度");
								airConditionSeekBar.setProgress(airconditionTemp-12);
								mCenter.cAirconditionSwitchOn(mXpgWifiDevice, true);
								statuMap.put(JsonKeys.AIRCONDITION_ON_OFF, true);
							}else{
								tv_aircondition.setText("空调未打开，点击打开");
								airConditionSeekBar.setProgress(0);
								mCenter.cAirconditionSwitchOn(mXpgWifiDevice, false);
								statuMap.put(JsonKeys.AIRCONDITION_ON_OFF, false);
							}
						}
					});
	            	airConditionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
						@Override
						public void onStopTrackingTouch(SeekBar arg0) {
							boolean isAirconditionOpen = (Boolean) statuMap.get(JsonKeys.AIRCONDITION_ON_OFF);
							if(!isAirconditionOpen){
								tv_aircondition.setText("空调未打开，点击打开");
								airConditionSeekBar.setProgress(0);
								Toast.makeText(MainControlActivity.this,"抱歉，您还未打开空调",Toast.LENGTH_LONG).show();
							}else{
								
								mCenter.cAirconditionTemp(mXpgWifiDevice, arg0.getProgress()+12);
								statuMap.put(JsonKeys.AIRCONDITION_TEMP, String.valueOf(arg0.getProgress()+12));
								tv_aircondition.setText("空调已打开,设置温度为"+(arg0.getProgress()+12)+"摄氏度");
							}
						}
						@Override
						public void onStartTrackingTouch(SeekBar arg0) {
							
						}
						@Override
						public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
							//tv_aircondition.setText("空调已打开,设置温度为"+(arg0.getProgress()+12)+"摄氏度");
						}
					});
	            }else{
	            	view = View.inflate(MainControlActivity.this, R.layout.pager_envirment_info, null);
	            	waveLoadingView = (WaveLoadingView)view.findViewById(R.id.waveLoadingView);
	            	envirmentMaxSeekbar = (SeekBar) view.findViewById(R.id.envirmentMaxSeekbar);
	            	tv_temp_max = (TextView)view.findViewById(R.id.tv_temp_max);
	            	linear_temp_max = (LinearLayout)view.findViewById(R.id.linear_temp_max);
	            	
	            	waveLoadingViewVoice = (WaveLoadingView)view.findViewById(R.id.waveLoadingViewVoice);
	            	voiceMaxSeekbar = (SeekBar) view.findViewById(R.id.voiceMaxSeekbar);
	            	tv_voice = (TextView)view.findViewById(R.id.tv_voice_max);
	            	linear_voice_max = (LinearLayout)view.findViewById(R.id.linear_voice_max);
	            	voiceMaxSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
						@Override
						public void onProgressChanged(SeekBar arg0, int arg1,
								boolean arg2) {
							
							int progress = arg0.getProgress();
						 	
							if(progress==7){
						 		tv_voice.setText("当前超声波报警临界值:"+2.4);
						 	}else{
						 		tv_voice.setText("当前超声波报警临界值:"+(0.2*progress+1));
							}
						}
						@Override
						public void onStartTrackingTouch(SeekBar arg0) {
							
						}
						@Override
						public void onStopTrackingTouch(SeekBar arg0) {
							mCenter.cVoiceBorder(mXpgWifiDevice,1+arg0.getProgress()*0.2);
							statuMap.put(JsonKeys.VOICE_BORDER, String.valueOf(1+arg0.getProgress()*0.2));
							float currentVoice = Float.parseFloat((String) statuMap.get(JsonKeys.VOICE));
							float currentVoiceBorder = Float.parseFloat((String) statuMap.get(JsonKeys.VOICE_BORDER));
							
							if(currentVoice>=currentVoiceBorder){
								waveLoadingViewVoice.setProgressValue(100);
							}else{
								int progress = (int) (currentVoice*100/currentVoiceBorder);
								waveLoadingViewVoice.setProgressValue(progress);
							}
						
						}
					});
	            	waveLoadingViewVoice.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							if(!linear_voice_max.isShown()){
								waveLoadingViewVoice.setBottomTitle("点击隐藏超声波临界值设置条");
								linear_voice_max.setVisibility(View.VISIBLE);
						 	
						 	int progress = (int)((Float.parseFloat((String) statuMap.get(JsonKeys.VOICE_BORDER))-1)/0.2);
						 	voiceMaxSeekbar.setProgress(progress);
						 	if(progress==7){
						 		tv_voice.setText("当前超声波报警临界值:"+2.4);
						 	}else{
						 		tv_voice.setText("当前超声波报警临界值:"+(0.2*progress+1));
							}
							}else{
								waveLoadingViewVoice.setBottomTitle("点击设置超声波传感器报警临界值");
								linear_voice_max.setVisibility(View.GONE);
							}
						}
					});
	            	
	            	envirmentMaxSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
						@Override
						public void onProgressChanged(SeekBar arg0, int arg1,
								boolean arg2) {
							tv_temp_max.setText("当前温度报警值:"+(arg1-40)+"摄氏度");
						}
						@Override
						public void onStartTrackingTouch(SeekBar arg0) {
							
						}
						@Override
						public void onStopTrackingTouch(SeekBar arg0) {
							mCenter.cEenvirTempBorder(mXpgWifiDevice, arg0.getProgress()-40);
							statuMap.put(JsonKeys.ENVIRMENT_TEMPTURE_BORDER, String.valueOf(arg0.getProgress()-40));
							int currentTemp = Integer.parseInt((String) statuMap.get(JsonKeys.ENVIRMENT_TEMPTURE));
							int currentTempBorder = Integer.parseInt((String) statuMap.get(JsonKeys.ENVIRMENT_TEMPTURE_BORDER));
							
							if(currentTemp>=currentTempBorder){
								waveLoadingView.setProgressValue(100);
							}else{
								int progress = (int) (currentTemp*100*1.0/currentTempBorder);
								waveLoadingView.setProgressValue(progress);
							}
						
						}
					});
	            	waveLoadingView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							if(!linear_temp_max.isShown()){
							waveLoadingView.setBottomTitle("点击隐藏温度临界值设置条");
							linear_temp_max.setVisibility(View.VISIBLE);
						 	
						 	int temp_max = Integer.parseInt((String) statuMap.get(JsonKeys.ENVIRMENT_TEMPTURE_BORDER));
						 	envirmentMaxSeekbar.setProgress(temp_max+40);
						 	tv_temp_max.setText("当前温度报警值:"+temp_max+"摄氏度");
							}else{
								waveLoadingView.setBottomTitle("点击设置温度报警临界值");
								linear_temp_max.setVisibility(View.GONE);
							}
						}
					});
	            }
	            container.addView(view);//一定不能少，将view加入到viewPager中
	            return view;
	        }
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (drawerLayout.isDrawerOpen(Gravity.LEFT )) {
		
			return;
		}
		switch (v.getId()) {
		case R.id.ivMenu:
			drawerLayout.openDrawer(Gravity.LEFT);
			
			break;
		
		case R.id.rlAlarmTips:
			if (alarmList != null && alarmList.size() > 0) {
				Intent intent = new Intent(MainControlActivity.this, AlarmListActicity.class);
				intent.putExtra("alarm_list", alarmList);
				startActivity(intent);
			}
			break;
		case R.id.tvCamera:
			startActivity(new Intent(this,AddCameraActivity.class));
			break;
		}
	}

	/**
	 * Login device.
	 * 
	 * @param xpgWifiDevice
	 *            the xpg wifi device
	 */
	private void loginDevice(XPGWifiDevice xpgWifiDevice) {
		mXpgWifiDevice = xpgWifiDevice;
		mXpgWifiDevice.setListener(deviceListener);
		mXpgWifiDevice.login(setmanager.getUid(), setmanager.getToken());
		isTimeOut = false;
		handler.sendEmptyMessageDelayed(handler_key.LOGIN_TIMEOUT.ordinal(), LoginTimeOut);
	}
	@Override
	protected void didLogin(XPGWifiDevice device, int result) {
		if (isTimeOut)
			return;
		if (result == 0) {
			handler.sendEmptyMessage(handler_key.LOGIN_SUCCESS.ordinal());
		} else {
			handler.sendEmptyMessage(handler_key.LOGIN_FAIL.ordinal());
		}

	}
	/**
	 * 检查出了选中device，其他device有没有连接上
	 * 
	 * @param mac
	 *            the mac
	 * @param did
	 *            the did
	 * @return the XPG wifi device
	 */
	
	private void DisconnectOtherDevice() {
		for (XPGWifiDevice theDevice : bindlist) {
			if (theDevice.isConnected() && !theDevice.getDid().equalsIgnoreCase(mXpgWifiDevice.getDid()))
				mCenter.cDisconnect(theDevice);
		}
	}
	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(Gravity.LEFT )) {
			drawerLayout.closeDrawer(Gravity.LEFT);
		}
		else {
			if (mXpgWifiDevice != null && mXpgWifiDevice.isConnected()) {
				mCenter.cDisconnect(mXpgWifiDevice);
				DisconnectOtherDevice();
			}
			finish();
		}
	}
	@Override
	protected void didDisconnected(XPGWifiDevice device) {
		if (!device.getDid().equalsIgnoreCase(mXpgWifiDevice.getDid()))
			return;

		handler.sendEmptyMessage(handler_key.DISCONNECTED.ordinal());
	}
	/**
	 * 把状态信息存入表
	 * 
	 * @param map
	 *            the map
	 * @param json
	 *            the json
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void inputDataToMaps(ConcurrentHashMap<String, Object> map, String json) throws JSONException {
		Log.i("Apptest", json);
		JSONObject receive = new JSONObject(json);
		Iterator actions = receive.keys();
		while (actions.hasNext()) {

			String action = actions.next().toString();
			Log.i("revjson", "action=" + action);
			// 忽略特殊部分
			if (action.equals("cmd") || action.equals("qos") || action.equals("seq") || action.equals("version")) {
				continue;
			}
			JSONObject params = receive.getJSONObject(action);
			Log.i("revjson", "params=" + params);
			Iterator it_params = params.keys();
			while (it_params.hasNext()) {
				String param = it_params.next().toString();
				Object value = params.get(param);
				map.put(param, value);
				Log.i(TAG, "Key:" + param + ";value" + value);
			}
		}
		//handler.sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
	}

	/**
	 * 菜单界面返回主控界面.
	 * 
	 * @return void
	 */
	private void backToMain() {
		mXpgWifiDevice = mAdapter.getItem(mAdapter.getChoosedPos());
		if (!mXpgWifiDevice.isConnected()) {
			loginDevice(mXpgWifiDevice);
			DialogManager.showDialog(this, progressDialogRefreshing);
		}
		refreshMainControl();
	}
	/**
	 * 设置提示框显示与隐藏,设置故障数量.
	 * 
	 * @param isShow
	 *            the is show
	 * @param count
	 *            the count
	 * @true 显示
	 * @false 隐藏
	 */
	private void setTipsLayoutVisiblity(boolean isShow, int count) {
		rlAlarmTips.setVisibility(isShow ? View.VISIBLE : View.GONE);
		tvAlarmTipsCount.setText(count + "");
	}
	/**
	 * 把警告信息存入列表
	 * 
	 * @param json
	 *            the json
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void inputAlarmToList(String json) throws JSONException {
		Log.i("revjson", json);
		JSONObject receive = new JSONObject(json);
		Iterator actions = receive.keys();

		while (actions.hasNext()) {

			String action = actions.next().toString();
			String value = receive.getString(action);
			Log.i("revjson", "action=" + action);
			DeviceAlarm alarm = new DeviceAlarm(getDateCN(new Date()), action);
			if (value.equals("1")) {
				alarmList.add(alarm);
			}
		}
		if (alarmList.size() != 0) {
			handler.sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
		}
	}
	
	/**
	 * 获取格式：2014年6月24日 17:23.
	 * 
	 * @param date
	 *            the date
	 * @return the date cn
	 */
	public static String getDateCN(Date date) {
		int y = date.getYear();
		int m = date.getMonth() + 1;
		int d = date.getDate();
		int h = date.getHours();
		int mt = date.getMinutes();
		return (y + 1900) + "年" + m + "月" + d + "日  " + h + ":" + mt;
	}
	public void onClickSlipBar(View view) {
		if (!drawerLayout.isDrawerOpen(Gravity.LEFT )) {
		
			return;
		}
		switch (view.getId()) {
		
		case R.id.rlAbout_Demo:
			IntentUtils.getInstance().startActivity(MainControlActivity.this, AboutVersionActivity.class);
			break;
		case R.id.rlAccount:
			IntentUtils.getInstance().startActivity(MainControlActivity.this, UserManageActivity.class);
			break;
		
		case R.id.btnDeviceList:
			mCenter.cDisconnect(mXpgWifiDevice);
			DisconnectOtherDevice();
			IntentUtils.getInstance().startActivity(MainControlActivity.this, DeviceListActivity.class);
			finish();
			break;
		}
	}
}

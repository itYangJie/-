package com.yj.smarthome.receiver;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.baidu.android.pushservice.PushMessageReceiver;



import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yj.smarthome.framework.activity.FlushActivity;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyPushReceiver extends PushMessageReceiver{

	@Override
	public void onBind(Context arg0, int arg1, String arg2, String arg3,
			String arg4, String arg5) {
		// TODO Auto-generated method stub
		//System.out.println("chanelId:"+arg4);
		//配置机智云与百度推送关联，报警时客户端能收到通知
		SharedPreferences sp = arg0.getSharedPreferences("set", Context.MODE_PRIVATE);
		String token = sp.getString("token", "");
		sendDeviceInfos(arg0,token, arg4, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				System.out.println("消息推送cid成功");
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
			}
		});
	}
	private void sendDeviceInfos(Context context, String token, String cid, AsyncHttpResponseHandler handler){
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("X-Gizwits-Application-Id", "754f6bf0563543568c09a51b0e0fcd28");
		client.addHeader("X-Gizwits-User-Token",token);
		client.addHeader("Content-Type","application/json");
		JSONObject jsonParams = new JSONObject();
	     StringEntity entity = null;
		try {
		jsonParams.put("type", "baidu-android");
		jsonParams.put("cid", cid);
		//04-07 21:38:47.710: I/System.out(10271): chanelId:4342137980500058530
		
		entity = new StringEntity(jsonParams.toString(),"UTF-8");
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		client.post(context, "http://push.gizwitsapi.com/gizwits_clound_push/push/clients", entity, "", handler);
		
	}
	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(Context arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotificationArrived(Context arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotificationClicked(Context context, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub
		// Get the Activity Manager
		 ActivityManager manager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
		 List< ActivityManager.RunningTaskInfo > tasks = manager.getRunningTasks(1);
		 ComponentName componentInfo = tasks.get(0).topActivity;
		 if(componentInfo.getPackageName().equals(context.getPackageName())) 
			 return ;
		//点击启动程序
		 Intent intent = new Intent();
	     intent.setClass(context.getApplicationContext(), FlushActivity.class);
	     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     context.getApplicationContext().startActivity(intent);
	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	

}

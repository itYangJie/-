/**
 * Project Name:XPGSdkV4AppBase
 * File Name:FlushActivity.java
 * Package Name:com.gizwits.framework.activity
 * Date:2015-1-27 14:46:31
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
package com.yj.smarthome.framework.activity;

import vstc2.nativecaller.NativeCaller;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipcamera.demo.BridgeService;
import com.ipcamera.demo.StartActivity;
import com.yj.common.system.IntentUtils;
import com.yj.smarthome.R;
import com.yj.smarthome.framework.activity.account.LoginActivity;
import com.yj.smarthome.framework.activity.device.DeviceListActivity;
import com.yj.smarthome.framework.utils.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * Created by Lien on 14/12/16.
 * 
 * 开机图界面
 * 
 * @author Lien
 */
public class FlushActivity extends BaseActivity {
	 private TextView splash_tv;
	  private RelativeLayout splash_bg;
	    private ImageView splash_img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flush);
		 splash_bg = (RelativeLayout) findViewById(R.id.splash_bg);
		 splash_tv = (TextView) findViewById(R.id.splash_tv);
	     splash_img = (ImageView) findViewById(R.id.splash_img);
	     
	     startAnimation();
		
	}
	 /**
     * 开启动画效果
     */
    private void startAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(1500);
        alpha.setFillAfter(true);
        splash_bg.startAnimation(alpha);

        TranslateAnimation ta_tv = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -0.8f);
        ta_tv.setDuration(1500);
        ta_tv.setFillAfter(true);
        splash_tv.startAnimation(ta_tv);
        
        TranslateAnimation ta_img = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF,2.0f);
        ta_img.setDuration(1500);
        ta_img.setFillAfter(true);
        splash_img.startAnimation(ta_img);

        //设置动画监听器
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束,判断是否是第一次打开应用，如果是则进入新手引导页
                //进入主界面
            	//判断是否有账号登陆
				if (StringUtils.isEmpty(setmanager.getToken())) {
					//未有账号登陆
					IntentUtils.getInstance().startActivity(FlushActivity.this,
							LoginActivity.class);
				} else {
					//已有账号登陆
					Intent intent = new Intent(FlushActivity.this,
							DeviceListActivity.class);
					intent.putExtra("autoLogin", true);
					startActivity(intent);
				}

				finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	
    }
}
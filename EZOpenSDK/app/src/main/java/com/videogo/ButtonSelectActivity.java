/* 
 * @ProjectName ezviz-openapi-android-demo
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName LoginSelectActivity.java
 * @Description 这里对文件进行描述
 * 
 * @author chenxingyf1
 * @data 2014-12-6
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.videogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.videogo.ui.cameralist.EZCameraListActivity;

import ezviz.ezopensdk.R;

/**
 * 登录选择演示
 * @author xiaxingsuo
 * @data 2015-11-6
 */
public class ButtonSelectActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_page);
        initData();
        initView();
    }
    
    private void initData() {
    }
    
    private void initView() {
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        	case R.id.jump_real_play_btn:
                if (TextUtils.isEmpty(EzvizApplication.AppKey)){
                    Toast.makeText(this,"Appkey为空",Toast.LENGTH_LONG).show();
                    return;
                }
                EzvizApplication.getOpenSDK().setAccessToken("at.d692ecsf0omd231gbbr1qekdcuexh45k-5by8s23w1g-1cpplsd-qaknlzhjb");
//                Intent toIntent = new Intent(ButtonSelectActivity.this, EZRealPlayActivity.class);
//                String serialNo = "524007615";
//                EZProbeDeviceInfo probeDeviceInfo = null;
//                try {
//                    probeDeviceInfo =  EzvizApplication.getOpenSDK().probeDeviceInfo(serialNo);
//                }catch (BaseException e) {
//                    ErrorInfo errorInfo = (ErrorInfo) e.getObject();
//                }
                /*EZDeviceInfo mDeviceInfo = new EZDeviceInfo();
                mDeviceInfo.setDeviceSerial("524007615");
                mDeviceInfo.setDeviceName("C2C(524007615)");
                mDeviceInfo.setDeviceType("CS-C2C-31WFR");
                mDeviceInfo.setIsEncrypt(1);
//                mDeviceInfo.set("1|1|1|1|0|0|1|1|1|1|1|1|3|1|1|16-9|-1|-1|-1|-1|-1|-1|-1|1|-1|0|-1|1|-1|0|0|-1|-1|-1|-1|-1|1|-1|-1|-1|-1|-1|-1|0|-1|-1|-1|-1|-1|-1|1|-1|-1|1|-1|-1|-1|-1|1|-1|1|-1|-1|-1|-1|-1|-1|1|1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|1|1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|-1|");
                mDeviceInfo.setStatus(1);
                mDeviceInfo.setDeviceVersion("V5.1.8 build 170824");
                toIntent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, mDeviceInfo);
                EZCameraInfo mCameraInfo = new EZCameraInfo();
                mCameraInfo.setDeviceSerial("524007615");
                mCameraInfo.setCameraNo(1);
                DataManager.getInstance().setDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial(), "VSESXA");
                toIntent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, mCameraInfo);
//                toIntent.putExtra(IntentConsts.EXTRA_RTSP_URL, "ezopen://AES:aHYzdz8ES6V5d1UzabS2ww@open.ys7.com/524007615/1.live");
                toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toIntent);*/
                Intent toIntent = new Intent(ButtonSelectActivity.this, EZCameraListActivity.class);
                toIntent.putExtra("deviceSerial", "524007615");
                toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toIntent);
        		break;
            default:
                break;
        }
    }
}

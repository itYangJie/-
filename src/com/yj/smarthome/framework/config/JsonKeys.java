/**
 * Project Name:XPGSdkV4AppBase
 * File Name:JsonKeys.java
 * Package Name:com.gizwits.framework.config
 * Date:2015-1-27 14:47:10
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
package com.yj.smarthome.framework.config;

// TODO: Auto-generated Javadoc
/**
 * 
 * ClassName: Class JsonKeys. <br/>
 * Json对应字段表<br/>
 * 
 * @author Lien
 */
public class JsonKeys {

	/** 产品名. */
	public final static String PRODUCT_NAME = "智能家居";

	/** 实体字段名，代表对应的项目. */
	public final static String KEY_ACTION = "entity0";

	
	
	/** Led开关 */
	public final static String LED_ON_OFF = "ledSwitch";
	/** 空调开关 */
	public final static String  AIRCONDITION_SWITCH= "airConditionSwitch";
	/** 空调温度 */
	public final static String AIRCONDITION_TEMPUTRE = "airConditionTemputre";
	/** 热水器开关 */
	public final static String HOTWATER_ON_OFF = "hotWaterSwitch";
	/** 热水器水温 */
	public final static String HOTWATER_TEMP = "hotWaterTempture";
	
	/** 超声波传感器当前数值 */
	public final static String VOICE = "voice";
	/** 超声波传感器设定门限值 */
	public final static String VOICE_BORDER = "voiceBorder";
	/** 超声波报警提醒*/
	public final static String ALERT_VOICE = "alertVoice";
	
	/** 当前环境温度数值 */
	public final static String ENVIRMENT_TEMPTURE = "envirmentTempture";
	/** 环境温度设定门限值 */
	public final static String ENVIRMENT_TEMPTURE_BORDER = "envirmentTemptureBorder";
	/** 环境温度报警提醒 */
	public final static String ALERT_ENVIRMENT_TEMPTURE = "alertEnvirmentTempture";
	
	/** 烟雾报警提醒 */
	public final static String ALERT_SMOKING = "alertSmoking";
}

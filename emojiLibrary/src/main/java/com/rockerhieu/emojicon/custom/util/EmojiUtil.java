package com.rockerhieu.emojicon.custom.util;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author way
 * 
 */
public class EmojiUtil {
	public static final String STATIC_FACE_PREFIX = "f_static_";
	public static final String DYNAMIC_FACE_PREFIX = "f";

	private EmojiUtil() {
		initEmojiMap();
	}

	private static EmojiUtil instance;

	public static EmojiUtil getInstance() {
		if (null == instance)
			instance = new EmojiUtil();
		return instance;
	}

	private Map<String, String> mEmojiMap;


	
	private void initEmojiMap() {
		mEmojiMap = new LinkedHashMap<String, String>();
		mEmojiMap.put("[1]", "0001");
		mEmojiMap.put("[2]", "0002");
		mEmojiMap.put("[3]", "0003");
		mEmojiMap.put("[4]", "0004");
		mEmojiMap.put("[5]", "0005");
		mEmojiMap.put("[6]", "0006");
		mEmojiMap.put("[7]", "0007");
		mEmojiMap.put("[8]", "0008");
		mEmojiMap.put("[9]", "0009");
		mEmojiMap.put("[10]", "0010");
		mEmojiMap.put("[11]", "0011");
		mEmojiMap.put("[12]", "0012");
		mEmojiMap.put("[13]", "0013");
		mEmojiMap.put("[14]", "0014");
		mEmojiMap.put("[15]", "0015");
		mEmojiMap.put("[16]", "0016");
		mEmojiMap.put("[17]", "0017");
		mEmojiMap.put("[18]", "0018");
		mEmojiMap.put("[19]", "0019");
		mEmojiMap.put("[20]", "0020");
		mEmojiMap.put("[21]", "0021");
		mEmojiMap.put("[22]", "0022");
		mEmojiMap.put("[23]", "0023");
		mEmojiMap.put("[24]", "0024");
		mEmojiMap.put("[25]", "0025");
		mEmojiMap.put("[26]", "0026");
		mEmojiMap.put("[27]", "0027");
		mEmojiMap.put("[28]", "0028");
		mEmojiMap.put("[29]", "0029");
		mEmojiMap.put("[30]", "0030");

//		mEmojiMap.put("[嘻嘻1]", "0001");
//		mEmojiMap.put("[哈哈1]", "0002");
//		mEmojiMap.put("[喜欢1]", "0003");
//		mEmojiMap.put("[晕1]", "0004");
//		mEmojiMap.put("[泪1]", "0005");
//		mEmojiMap.put("[嘻嘻]", "001");
//		mEmojiMap.put("[哈哈]", "002");
//		mEmojiMap.put("[喜欢]", "003");
//		mEmojiMap.put("[晕]", "004");
//		mEmojiMap.put("[泪]", "005");
//		mEmojiMap.put("[馋嘴]", "006");
//		mEmojiMap.put("[抓狂]", "007");
//		mEmojiMap.put("[哼]", "008");
//		mEmojiMap.put("[可爱]", "009");
//		mEmojiMap.put("[怒]", "010");
//		mEmojiMap.put("[汗]", "011");
//		mEmojiMap.put("[微笑]", "012");
//		mEmojiMap.put("[睡觉]", "013");
//		mEmojiMap.put("[钱]", "014");
//		mEmojiMap.put("[偷笑]", "015");
//		mEmojiMap.put("[酷]", "016");
//		mEmojiMap.put("[衰]", "017");
//		mEmojiMap.put("[吃惊]", "018");
//		mEmojiMap.put("[怒骂]", "019");
//		mEmojiMap.put("[鄙视]", "020");
//		mEmojiMap.put("[挖鼻屎]", "021");
//
//		mEmojiMap.put("[色]", "022");
//		mEmojiMap.put("[鼓掌]", "023");
//		mEmojiMap.put("[悲伤]", "024");
//		mEmojiMap.put("[思考]", "025");
//		mEmojiMap.put("[生病]", "026");
//		mEmojiMap.put("[亲亲]", "027");
//		mEmojiMap.put("[抱抱]", "028");
//		mEmojiMap.put("[白眼]", "029");
//		mEmojiMap.put("[右哼哼]", "030");
//		mEmojiMap.put("[左哼哼]", "031");
//		mEmojiMap.put("[嘘]", "032");
//		mEmojiMap.put("[委屈]", "033");
//		mEmojiMap.put("[哈欠]", "034");
//		mEmojiMap.put("[敲打]", "035");
//		mEmojiMap.put("[疑问]", "036");
//		mEmojiMap.put("[挤眼]", "037");
//		mEmojiMap.put("[害羞]", "038");
//		mEmojiMap.put("[快哭了]", "039");
//		mEmojiMap.put("[拜拜]", "040");
//		mEmojiMap.put("[黑线]", "041");
//		mEmojiMap.put("[强]", "042");
//
//		mEmojiMap.put("[弱]", "043");
//		mEmojiMap.put("[给力]", "044");
//		mEmojiMap.put("[浮云]", "045");
//		mEmojiMap.put("[围观]", "046");
//		mEmojiMap.put("[威武]", "047");
//		mEmojiMap.put("[相机]", "048");
//		mEmojiMap.put("[汽车]", "049");
//		mEmojiMap.put("[飞机]", "050");
//		mEmojiMap.put("[爱心]", "051");
//		mEmojiMap.put("[奥特曼]", "052");
//		mEmojiMap.put("[兔子]", "053");
//		mEmojiMap.put("[熊猫]", "054");
//		mEmojiMap.put("[不要]", "055");
//		mEmojiMap.put("[ok]", "056");
//		mEmojiMap.put("[赞]", "057");
//		mEmojiMap.put("[勾引]", "058");
//		mEmojiMap.put("[耶]", "059");
//		mEmojiMap.put("[爱你]", "060");
//		mEmojiMap.put("[拳头]", "061");
//		mEmojiMap.put("[差劲]", "062");
//
//		mEmojiMap.put("[握手]", "063");
//		mEmojiMap.put("[玫瑰]", "064");
//		mEmojiMap.put("[心]", "065");
//		mEmojiMap.put("[伤心]", "066");
//		mEmojiMap.put("[猪头]", "067");
//		mEmojiMap.put("[咖啡]", "068");
//		mEmojiMap.put("[麦克风]", "069");
//		mEmojiMap.put("[月亮]", "070");
//		mEmojiMap.put("[太阳]", "071");
//		mEmojiMap.put("[啤酒]", "072");
//		mEmojiMap.put("[萌]", "073");
//		mEmojiMap.put("[礼物]", "074");
//		mEmojiMap.put("[互粉]", "075");
//		mEmojiMap.put("[钟]", "076");
//		mEmojiMap.put("[自行车]", "077");
//		mEmojiMap.put("[蛋糕]", "078");
//		mEmojiMap.put("[围巾]", "079");
//		mEmojiMap.put("[手套]", "080");
//		mEmojiMap.put("[雪花]", "081");
//		mEmojiMap.put("[雪人]", "082");
//		mEmojiMap.put("[帽子]", "083");
//
//		mEmojiMap.put("[树叶]", "084");
//		mEmojiMap.put("[足球]", "085");
	}

	public Map<String, String> getFaceMap() {
		return mEmojiMap;
	}

	public String getFaceId(String faceStr) {
		if (mEmojiMap.containsKey(faceStr)) {
			return mEmojiMap.get(faceStr);
		}
		return "";
	}

	/* 屏幕dp转px */
	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int px2dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}


	public static int sp2px(Context context, float sp) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (sp * scale + 0.5f);
	}

	public static int px2sp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (px / scale + 0.5f);
	}

}

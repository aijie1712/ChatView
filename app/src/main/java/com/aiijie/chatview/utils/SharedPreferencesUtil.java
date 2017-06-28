package com.aiijie.chatview.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;


import com.aiijie.chatview.CustomApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class SharedPreferencesUtil {
    private static final String TAG = SharedPreferencesUtil.class.getName();
    private static SharedPreferencesUtil instance;
    private SharedPreferences preferences;

    private SharedPreferencesUtil() {
        preferences = PreferenceManager.getDefaultSharedPreferences(CustomApplication.getApplication());
    }

    /**
     * 单例模式的SharedPreferencesUtil
     */
    public static SharedPreferencesUtil getInstance() {
        if (instance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (instance == null) {
                    instance = new SharedPreferencesUtil();
                }
            }
        }
        return instance;
    }

    /*get set方法*/
    public void put(String key, boolean value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putBoolean(key, value);
            edit.apply();
        }
    }

    public void put(String key, String value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putString(key, value);
            edit.apply();
        }
    }

    public void put(String key, int value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putInt(key, value);
            edit.apply();
        }
    }

    public void put(String key, float value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putFloat(key, value);
            edit.apply();
        }
    }

    public void put(String key, long value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putLong(key, value);
            edit.apply();
        }
    }

    public void put(String key, Set<String> value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putStringSet(key, value);
            edit.apply();
        }
    }

    /**
     * 直接存放对象，反射将根据对象的属性作为key，并将对应的值保存。
     *
     * @param t
     */
    @SuppressWarnings("rawtypes")
    public <T> void put(T t) {
        try {
            String methodName = "";
            String saveValue = "";
            String fieldName = "";
            Editor edit = preferences.edit();
            Class cls = t.getClass();

            if (edit != null) {

                Method[] methods = cls.getDeclaredMethods();
                Field[] fields = cls.getDeclaredFields();

                for (Method method : methods) {
                    methodName = method.getName();
                    for (Field f : fields) {
                        fieldName = f.getName();
                        if (methodName.toLowerCase().contains(fieldName.toLowerCase())) {

                            Object value = method.invoke(t);
                            if (value != null && !TextUtils.isEmpty(String.valueOf(value))) {
                                saveValue = String.valueOf(value);
                            }

                            Log.e(TAG, "key: " + fieldName + " value: " + saveValue);
                            edit.putString(fieldName, String.valueOf(saveValue));
                            boolean commited = edit.commit();
                            Log.i(TAG, "commited: " + commited);
                            break;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return preferences.getString(key, "");
    }

    public String get(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public int get(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public float get(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public long get(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public Set<String> get(String key, Set<String> defValue) {
        return preferences.getStringSet(key, defValue);
    }

    /**
     * 获取整个对象，跟put(T t)对应使用， 利用反射得到对象的属性，然后从preferences获取
     *
     * @param cls
     * @return
     */
    public <T> Object get(Class<T> cls) {
        Object obj = null;
        String fieldName = "";
        try {
            obj = cls.newInstance();
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                fieldName = f.getName();
                if (!"serialVersionUID".equals(fieldName)) {
                    f.setAccessible(true);
                    f.set(obj, get(f.getName()));
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static final String SOFT_KEYBOARD_HEIGHT = "SoftKeyboardHeight";

    public void setCachedKeyboardHeight(int height) {
        put(SOFT_KEYBOARD_HEIGHT, height);
//        put(SOFT_KEYBOARD_HEIGHT, height);
    }

    public int getCachedKeyboardHeight() {
        return get(SOFT_KEYBOARD_HEIGHT, 0);
    }

}

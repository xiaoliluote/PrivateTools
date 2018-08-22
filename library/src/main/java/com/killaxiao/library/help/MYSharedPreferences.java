package com.killaxiao.library.help;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 自定义sharepreferences
 * @author KillaXiao
 *
 */
public class MYSharedPreferences {

	private SharedPreferences mySharedPreferences;
	private Context _context;
	private SharedPreferences.Editor editor;
	public static final String DEFAULT_NAME="default_name";

	public MYSharedPreferences(Context context){
		this._context = context;
		mySharedPreferences= _context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
	}

	/**
	 * 初始化sharepreferences
	 * @param context
	 * @param name 文件的名字
	 */
	public MYSharedPreferences(Context context, String name){
		this._context = context;
		mySharedPreferences= _context.getSharedPreferences(name, Context.MODE_PRIVATE); 
		editor = mySharedPreferences.edit();
	}
	
	public void putString(String key,String value){
		editor.putString(key, value);
		editor.commit(); 
	}
	
	public String getString(String key){
		return mySharedPreferences.getString(key, "");
	}
	
	public void putInt(String key,int value){
		editor.putInt(key, value);
		editor.commit();
	}
	
	public int getInt(String key){
		return mySharedPreferences.getInt(key, 0);
	}
	
	public void putLong(String key,long value){
		editor.putLong(key, value);
		editor.commit();
	}
	
	public long getLong(String key){
		return mySharedPreferences.getLong(key, 0);
	}
	
	public void putBoolean(String key,boolean value){
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public boolean getBoolean(String key){
		return mySharedPreferences.getBoolean(key, false);
	} 
	
	public void clearData(){
		editor.clear();
		editor.commit();
	}
}

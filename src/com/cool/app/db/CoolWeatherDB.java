package com.cool.app.db;

import java.util.ArrayList;
import java.util.List;

import com.cool.app.model.City;
import com.cool.app.model.County;
import com.cool.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

	private static  CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	/*
	 * 数据库名称
	 * */
	public static final String DB_NAME="cool_weather";
	/*
	 * 数据库版本号
	 * */
	public static final int VERSION=1;
	
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper ohp=new CoolWeatherOpenHelper(context,
				DB_NAME,null,VERSION);
		db=ohp.getWritableDatabase();
	}
	public synchronized static CoolWeatherDB getInstance(Context context){
		
		if(coolWeatherDB==null){
		coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	//---------------------------将Province存储到数据库----------------------------------
	public void saveProvince(Province province){
		ContentValues values=new ContentValues();
		values.put("province_name",province.getProvinceName());
		values.put("province_code", province.getProvinceCode());
		db.insert("Province", null, values);
	}
	
	//-----------------------------从省表中读取全省的值----------------------------------------
	public List<Province> getProvince(){
		List<Province>provinceList=new ArrayList<Province>();
		Cursor cursor=db.query("Province",null,null,null,null,null,null);
		if(cursor.moveToFirst()){
			do{
	Province province=new Province();
	province.setId(cursor.getInt(cursor.getColumnIndex("id")));
	province.setProvinceName(cursor.getString(cursor.getColumnIndex("provinceName")));
	province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
	provinceList.add(province);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return provinceList;
	}
	
	//---------------------------------保存进城市表----------------------------
	public void saveCity(City city){
		ContentValues values=new ContentValues();
		values.put("city_name", city.getCityName());
		values.put("city_code", city.getCityCode());
		values.put("province_id", city.getProvinceId());
		db.insert("City", null, values);
	}
	
	//--------------------------------读取所有的城市-----------------------------
		public List<City> getCities(Province province){
		List<City>cityList=new ArrayList<City>();
		
		Cursor cursor=db.query("City", null, "province_id=?",
				new String[]{String.valueOf(province.getId())},null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				City city=new City();
		city.setId(cursor.getInt(cursor.getColumnIndex("id")));		
		city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
		city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
		city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
			}while(cursor.moveToNext());
		}
		cursor.close();
		return cityList;
	}
	
	//------------------------------保存县-----------------------------
	public void saveCounty(County county){
		ContentValues values=new ContentValues();
		values.put("county_name", county.getCountyName());
		values.put("county_code", county.getCountyCode());
		values.put("city_id", county.getCityId());
		db.insert("County", null, values);
	}
	
	
	//-------------------------------读取某市下的所有县---------------------------------------
	public List<County> getCounty(City city){
		List<County>countyList=new ArrayList<County>();
		Cursor cursor=db.query("County", null, "city_id=?",
				new String[]{String.valueOf(city.getId())}, null, null, null);
		if(cursor.moveToFirst()){
			do{
	County county=new County();
	county.setId(cursor.getInt(cursor.getColumnIndex("id")));
	county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
	county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));	
	county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
			}while(cursor.moveToNext());
		}
		cursor.close();
		return countyList;
	}
	
	
	
}

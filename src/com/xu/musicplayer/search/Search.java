package com.xu.musicplayer.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Search {

	private static String json = "";
	private static String url = "";
	public static List<SearchTipsEntity> songs = new ArrayList<SearchTipsEntity>();

	public static void main(String[] args) throws MalformedURLException, IOException {

	}

	public static List<SearchTipsEntity> search(String name,String type) {
		songs.clear();
		if (type.equalsIgnoreCase("API")) {
			url = "http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword="+name+"&page=1&pagesize=20&showtype=1";
		} else if (type.equalsIgnoreCase("WEB")) {
			url="http://searchtip.kugou.com/getSearchTip?MusicTipCount=5&MVTipCount=2&albumcount=2&keyword="+name +"&callback=jQuery180014477266089871377_1523886180659&_=1523886"+RandomCode();
		}	
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			InputStreamReader reader = new InputStreamReader(connection.getInputStream());
			BufferedReader breader = new BufferedReader(reader);
			json = breader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject ojsons = JSONObject.parseObject(json);
		ojsons = JSONObject.parseObject(ojsons.getString("data"));
		JSONArray array = JSONArray.parseArray(ojsons.getString("info"));
		for (int i = 0; i < array.size(); i++) {
			SearchTipsEntity entity = JSON.toJavaObject(JSONObject.parseObject(array.get(i).toString()), SearchTipsEntity.class);
			entity.setS320hash(JSON.parseObject(array.get(i).toString()).get("320hash").toString());
			entity.setS320filesize(JSON.parseObject(array.get(i).toString()).get("320filesize").toString());
			entity.setS320privilege(JSON.parseObject(array.get(i).toString()).get("320privilege").toString());
			songs.add(entity);
		}		
		return songs;
	}

	private static String RandomCode() {
		String code="";
		for (int i = 0;i<6;i++) {
			code += new Random().nextInt(10)+"";
		}
		return code;
	}

}

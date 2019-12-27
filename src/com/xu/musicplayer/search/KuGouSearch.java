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


public class KuGouSearch {

	private static String json = "";
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		new KuGouSearch().search("不醉");
	}

	public List<String> search(String name) throws MalformedURLException, IOException {
		List<String> songs = new ArrayList<String>();

		String url="http://searchtip.kugou.com/getSearchTip?MusicTipCount=5&MVTipCount=2&albumcount=2&keyword="+name +"&callback=jQuery180014477266089871377_1523886180659&_=1523886"+RandomCode();

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		InputStreamReader reader = new InputStreamReader(connection.getInputStream());
		BufferedReader breader = new BufferedReader(reader);
		json = breader.readLine();

		return songs;
	}

	private String RandomCode() {
		String code="";
		for (int i = 0;i<6;i++) {
			code += new Random().nextInt(10)+"";
		}
		return code;
	}
}

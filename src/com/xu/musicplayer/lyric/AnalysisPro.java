package com.xu.musicplayer.lyric;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@SuppressWarnings(value="all")
public class AnalysisPro {

	public static void main(String[] args) throws IOException {
		AnalysisPro analysis=new AnalysisPro();
		List<Map<String,String>> list=analysis.getLyricList("年轮");
		for(Map<String, String> l:list){
			for(Entry<String,String> entry:l.entrySet()){
				System.out.println(entry.getKey()+"===="+entry.getValue());
			}
		}//  年轮-张碧晨====/lrc-21926-234479.html
		//  http://www.lrcgc.com/lrc-21926-234479.html
		
		//  http://www.lrcgc.com/lrc-21926-234479/张碧晨-年轮.lrc
		//                      /lrc-21926-234479/年轮-张碧晨.html
		//System.out.println(analysis.getLyric("http://www.lrcgc.com/lrc-6258-235146/年轮-汪苏泷.lrc"));
	}
	
	/**
	 * 获取歌词列表
	 * @param name 歌曲名
	 * @return     List<'Map<'String, String'>'>
	 * @throws IOException IO异常
	 */
	public static List<Map<String, String>> getLyricList(String name) throws IOException{
		List<Map<String,String>> lists=new ArrayList<Map<String,String>>();
		Map<String,String> map;
		Document htmldoc = Jsoup.connect("http://www.lrcgc.com/so/?q="+name+"&csrf_token="+getRandomString()).get();
		Elements htmllist=htmldoc.getElementsByAttributeValue("class", "so_list");
		Document parsedoc=Jsoup.parse(htmllist.toString());
		Elements parselist=parsedoc.getElementsByTag("ul");
		Elements childs=parselist.get(0).children();
		if(childs.size()>=4){
			for (int i = 0; i < childs.size(); i++) {
				String url=childs.get(i).child(0).child(0).attr("href");
				if(url.contains("lyric")){
					url=url.replace("lyric", "lrc");
					url="http://www.lrcgc.com"+url.substring(0, url.lastIndexOf("."))+"/"+childs.get(i).child(0).text()+"-"+childs.get(i).child(2).text()+".lrc";
				}
				map=new HashMap<String,String>();
				map.put(childs.get(i).child(0).text()+"-"+childs.get(i).child(2).text(),url);
				lists.add(map);
			}
		}else{			
			for (int i = 0; i < childs.size(); i++) {
				String url=childs.get(i).child(0).child(0).attr("href");
				if(url.contains("lyric")){
					url=url.replace("lyric", "lrc");
					url="http://www.lrcgc.com"+url.substring(0, url.lastIndexOf("."))+"/"+childs.get(i).child(0).text()+"-"+childs.get(i).child(2).text()+".lrc";
				}
				map=new HashMap<String,String>();
				map.put(childs.get(i).child(0).text(), url);
				lists.add(map);
			}
		}		
		return lists;
	}

	/**
	 * 获取随机码
	 * @return 随机码
	 */
	private static String getRandomString(){
		String list="1234567890qwertyuiopasdfghjklzxcvbnm";
		String answer="";
		for(int i=0;i<16;i++){
			answer+=list.charAt(new Random().nextInt(list.length()));
		}
		return answer;
	}

	/**
	 * 获取推介的歌曲
	 * @param name 歌曲名
	 * @return     List<'Map<'String, String'>'>
	 * @throws IOException IO异常
	 */
	public static List<Map<String, String>> getReferralsMusic(String name) throws IOException{
		List<Map<String,String>> lists=new ArrayList<Map<String,String>>();
		Map<String,String> map;
		Document html = Jsoup.connect("http://www.lrcgc.com/so/?q="+name+"&csrf_token="+getRandomString()).get();
		Document htmldoc = Jsoup.parse(html.toString());
		if(htmldoc.hasAttr("ul")){
			Elements htmllist=htmldoc.getElementsByTag("ul");
			Document parsedoc=Jsoup.parse(htmllist.toString());
			Elements parselist=parsedoc.getElementsByAttributeValue("class", "cc").get(0).children();
			for (int i = 0; i < parselist.size(); i++) {
				map=new HashMap<String,String>();
				map.put(parselist.get(i).text(),"q="+parselist.get(i).text());
				lists.add(map);
			}
		}
		return lists;
	}
	
	/**
	 * 在网络上爬取歌词信息
	 * @param lrcurl 歌词的URL地址
	 * @return       String
	 * @throws IOException IO异常
	 */
	public static String getLyric(String lrcurl) throws IOException{
		URL url;
		if(lrcurl.startsWith("http:")){
			url=new URL(lrcurl);
		}else{
			url=new URL("http://www.lrcgc.com"+lrcurl);
		}
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		BufferedInputStream inputStream=new BufferedInputStream(connection.getInputStream());
		byte[] bt=new byte[1024];
		int len =0;
		String html="";
		while((len=inputStream.read(bt, 0, bt.length))!=-1){
			html+=new String(bt,0,len)+"\r\n";
		}
		return html;
	}

}

package com.xu.musicplayer.lyric;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Analysis {

	/**
	 * 获取歌词列表
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public List<String> getLyricList(String name) throws IOException{
		List<String> list=new ArrayList<String>();
		Document htmldoc = Jsoup.connect("http://www.lrcgc.com/so/?q="+name+"&csrf_token="+getRandomString()).get();
		referralsMusic(htmldoc.toString());
		Elements htmllist=htmldoc.getElementsByAttributeValue("class", "so_list");
		Document parsedoc=Jsoup.parse(htmllist.toString());
		Elements parselist=parsedoc.getElementsByTag("ul");
		Elements childs=parselist.get(0).children();
		if(childs.size()>=4){
			for (int i = 0; i < childs.size(); i++) {
				String url=childs.get(i).child(0).child(0).attr("href");
				if(url.contains("lyric")){
					url=url.replace("lyric", "lrc");
				}
				list.add(childs.get(i).child(0).text()+"-"+childs.get(i).child(2).text()+"$"+url);
			}
		}else{			
			for (int i = 0; i < childs.size(); i++) {
				String url=childs.get(i).child(0).child(0).attr("href");
				if(url.contains("lyric")){
					url=url.replace("lyric", "lrc");
				}
				list.add(childs.get(i).child(0).text()+"$"+url);
			}
		}		
		return list;
	}

	/**
	 * 获取随机码
	 * @return
	 */
	private String getRandomString(){
		String list="1234567890qwertyuiopasdfghjklzxcvbnm";
		String answer="";
		for(int i=0;i<16;i++){
			answer+=list.charAt(new Random().nextInt(list.length()));
		}
		return answer;
	}

	/**
	 * 获取推介的歌曲
	 * @param html
	 * @return
	 * @throws IOException
	 */
	public List<String> referralsMusic(String html) throws IOException{
		List<String> list=new ArrayList<String>();
		Document htmldoc = Jsoup.parse(html);
		if(htmldoc.hasAttr("ul")){
			Elements htmllist=htmldoc.getElementsByTag("ul");
			Document parsedoc=Jsoup.parse(htmllist.toString());
			Elements parselist=parsedoc.getElementsByAttributeValue("class", "cc").get(0).children();
			
			for (int i = 0; i < parselist.size(); i++) {
				list.add(parselist.get(i).text()+"$q="+parselist.get(i).text());
			}
		}
		return list;
	}
	
	/**
	 * 在网络上爬取歌词信息
	 * @param lrcurl
	 * @return
	 * @throws IOException
	 */
	public String getLyric(String lrcurl) throws IOException{
		URL url=new URL("http://www.lrcgc.com"+lrcurl);
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		BufferedInputStream inputStream=new BufferedInputStream(connection.getInputStream());
		byte[] bt=new byte[1024];
		int len =0;
		String html="";
		while((len=inputStream.read(bt, 0, bt.length))!=-1){
			html+=new String(bt,0,len)+"\r\n";
		}
		
		System.out.println(html);
		
		return html;
	}

}

package com.xu.roboto.analysis;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xu.roboto.entity.MusicList;

public class Analysis {

	//http://www.lrcgc.com
	//http://www.5ilrc.com/souge.asp
	//http://www.5ilrc.com/
	//http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.catalogSug&query=秋天不回来
	//http://www.lrcgc.com/so/?q=%E7%99%BD%E7%8B%90&csrf_token=6c054a7ffc08c53a
	//QQ音乐 http://s.music.qq.com/fcgi-bin/music_search_new_platform?t=0&n=5&aggr=1&cr=1&loginUin=0&format=json&inCharset=GB2312&outCharset=utf-8&notice=0&platform=jqminiframe.json&needNewCode=0&p=1&catZhida=0&remoteplace=sizer.newclient.next_song&w=周杰伦
//	public static void main(String[] args) {
//	
//		try {
//			List<MusicList> musicLists = Analysis.getMusicList("白狐");
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}


	/**
	 * 获取歌词信息
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static List<MusicList> getMusicList(String name) throws IOException{
		URL url=new URL("http://s.music.qq.com/fcgi-bin/music_search_new_platform?t=0&n=5&aggr=1&cr=1&loginUin=0&format=json&inCharset=GB2312&outCharset=utf-8&notice=0&platform=jqminiframe.json&needNewCode=0&p=1&catZhida=0&remoteplace=sizer.newclient.next_song&w="+name);
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();

		BufferedInputStream inputStream=new BufferedInputStream(connection.getInputStream());
		int len=0;
		StringBuffer buffer=new StringBuffer();
		byte[] bt=new byte[1024];
		while((len=inputStream.read(bt, 0, bt.length))!=-1){
			buffer.append(new String(bt,0,len));
		}

		List<MusicList> musicLists=new ArrayList<MusicList>();
		System.out.println(buffer.toString());
		if(buffer.toString().contains("errno")){
			musicLists=null;
			System.out.println(buffer.toString());
		}else{
			String[] lists=buffer.toString().substring(buffer.toString().indexOf("[")+1,buffer.toString().indexOf("]")-1).split("bitrate_fee");

			for(String list:lists){
				String songname="";
				String artistname="";
				String songid="";
				if(list.contains("songname")){
					songname=list.substring(list.indexOf("songname")+11,list.indexOf("artistname")-3);
				}
				if(list.contains("artistname")){
					artistname=list.substring(list.indexOf("artistname")+13,list.indexOf("control")-3);
				}
				if(list.contains("songid")){
					songid=list.substring(list.indexOf("songid")+11,list.indexOf("has_mv")-3);
				}
				MusicList music =new MusicList(songname,artistname,songid);
				musicLists.add(music);
			}
		}
		return musicLists;
	}


	/**
	 * 获取歌词
	 * @param songid
	 * @return
	 */
	public static String[] getLyric(String songid){
		String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.lry&songid="+songid;   
		String json = loadJson(url);

		Pattern pattern = Pattern.compile("[,|]+");//正则表达式去除所有
		String[] lyric = pattern.split(json);
		System.out.println(lyric.length);
		for (int i=0;i<lyric.length;i++) {
			String regEx="[`~!@#$%^&*()+=|{}';',.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？/\"/g]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(decodeUnicode (lyric[i]));
			String lyrics=m.replaceAll("").trim();
			lyric[i]=lyrics;//lyrics.substring(1, 6)+"--"+lyrics.substring(lyrics.lastIndexOf("]")+1);

		}
		String [] backlyric;
		String[] findlyric;
		if(lyric.length==2){
			//System.out.println(lyric[1].toString());
			findlyric=lyric[1].toString().split("\n");
			backlyric=new String[findlyric.length];
			for(int i=0;i<findlyric.length;i++){
				if(findlyric[i].contains("[")){
					if(findlyric[i].contains("0")){
						backlyric[i]=findlyric[i].substring(1,6)+"--"+findlyric[i].substring(findlyric[i].lastIndexOf("]")+1);
					}
				}
			}
		}else{
			backlyric=null;
			System.out.println(lyric[0].toString());
		}

		return backlyric;
	}

	public static String loadJson (String url) {  
		StringBuilder json = new StringBuilder();  
		try {  
			URL urlObject = new URL(url);  
			URLConnection uc = urlObject.openConnection();  
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));  
			String inputLine = null;  
			while ( (inputLine = in.readLine()) != null) {  
				json.append(inputLine);  
			}  
			in.close();  
		} catch (MalformedURLException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		return json.toString();  
	} 

	//解析Unicode編碼
	public static String decodeUnicode(String theString) {      
		char aChar;      
		int len = theString.length();      
		StringBuffer outBuffer = new StringBuffer(len);      
		for (int x = 0; x < len;) {      
			aChar = theString.charAt(x++);      
			if (aChar == '\\') {      
				aChar = theString.charAt(x++);      
				if (aChar == 'u') {      
					// Read the xxxx      
					int value = 0;      
					for (int i = 0; i < 4; i++) {      
						aChar = theString.charAt(x++);      
						switch (aChar) {      
						case '0':      
						case '1':      
						case '2':      
						case '3':      
						case '4':      
						case '5':      
						case '6':      
						case '7':      
						case '8':      
						case '9':      
							value = (value << 4) + aChar - '0';      
							break;      
						case 'a':      
						case 'b':      
						case 'c':      
						case 'd':      
						case 'e':      
						case 'f':      
							value = (value << 4) + 10 + aChar - 'a';      
							break;      
						case 'A':      
						case 'B':      
						case 'C':      
						case 'D':      
						case 'E':      
						case 'F':      
							value = (value << 4) + 10 + aChar - 'A';      
							break;      
						default:      
							throw new IllegalArgumentException(      
									"Malformed   \\uxxxx   encoding.");      
						}      
					}      
					outBuffer.append((char) value);      
				} else {      
					if (aChar == 't')      
						aChar = '\t';      
					else if (aChar == 'r')      
						aChar = '\r';      
					else if (aChar == 'n')      
						aChar = '\n';      
					else if (aChar == 'f')      
						aChar = '\f';      
					outBuffer.append(aChar);      
				}      
			} else     
				outBuffer.append(aChar);      
		}      
		return outBuffer.toString();      

	}

    private Analysis() {
    }



}

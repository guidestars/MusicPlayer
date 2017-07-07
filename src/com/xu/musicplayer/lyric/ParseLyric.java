package com.xu.musicplayer.lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ParseLyric {

	public static void main(String[] args) throws IOException {
		//parsedLyric(new File("C:\\Users\\Administrator\\Desktop\\test.txt"));
		//String text=AnalysisPro.getLyric("http://www.lrcgc.com/lrc-21926-234479/年轮-张碧晨.lrc");
		//parsedLyric(text);
	}

	/**
	 * 从文件中获取歌词并调整
	 * @param file 歌词文件
	 * @return     List<'String'>
	 */
	public static List<String> parsedLyric(File file){
		List<String> lyric=new ArrayList<String>();
		if(file.exists()){
			FileReader fileReader=null;
			BufferedReader reader=null;
			try {
				fileReader=new FileReader(file);
				reader=new BufferedReader(fileReader);
				String txt="";
				String reg="\\[(\\d{2}:\\d{2}\\.\\d{2})\\]|\\[\\d{2}:\\d{2}\\]";
				while((txt=reader.readLine())!=null){
					if(txt.contains("[ti:")) {       // 歌曲信息
						lyric.add("歌曲信息:"+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[ar:")) {// 歌手信息
						lyric.add("歌手信息:"+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[al:")) {// 专辑信息
						lyric.add("专辑信息:"+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[wl:")) {// 歌词作家
						lyric.add("歌词作家:"+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[wm:")) {// 歌曲作家
						lyric.add("歌曲作家:"+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[al:")) {// 歌词制作
						lyric.add("歌词制作:"+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}
					Pattern pattern=Pattern.compile(reg);
					Matcher matcher=pattern.matcher(txt);
					while(matcher.find()){
						lyric.add(matcher.group(0).substring(1,matcher.group(0).lastIndexOf("]")).trim()+txt.substring(txt.lastIndexOf("]")+1).trim());
					}
				}
				for(int i=0;i<lyric.size();i++){
					if(Pattern.matches("^\\d+$",lyric.get(i).substring(0,2))){
						if(Pattern.matches("^\\d{2}:\\d{2}\\.\\d{1}$",lyric.get(i).substring(0,7))){
							for(int j=0;j<lyric.size();j++){
								if(Pattern.matches("^\\d+$",lyric.get(j).substring(0, 2))){
									if(Double.parseDouble(lyric.get(i).substring(0, 2))<Double.parseDouble(lyric.get(j).substring(0, 2))){
										String temp=lyric.get(i);
										lyric.set(i, lyric.get(j));
										lyric.set(j, temp);
									}
									if(Double.parseDouble(lyric.get(i).substring(0, 2))==Double.parseDouble(lyric.get(j).substring(0, 2)) && Double.parseDouble(lyric.get(i).substring(3,8))<Double.parseDouble(lyric.get(j).substring(3,8))){
										String temp=lyric.get(i);
										lyric.set(i, lyric.get(j));
										lyric.set(j, temp);
									}
								}
							}
						}else if(Pattern.matches("^\\d{2}:\\d{2}$",lyric.get(i).substring(0,5))){
							for(int j=0;j<lyric.size();j++){
								if(Pattern.matches("^\\d+$",lyric.get(j).substring(0, 2))){
									if(Integer.parseInt(lyric.get(i).substring(0, 2))<Integer.parseInt(lyric.get(j).substring(0, 2))){
										String temp=lyric.get(i);
										lyric.set(i, lyric.get(j));
										lyric.set(j, temp);
									}
									if(Integer.parseInt(lyric.get(i).substring(0, 2))==Integer.parseInt(lyric.get(j).substring(0, 2)) && Integer.parseInt(lyric.get(i).substring(3,5))<Double.parseDouble(lyric.get(j).substring(3,5))){
										String temp=lyric.get(i);
										lyric.set(i, lyric.get(j));
										lyric.set(j, temp);
									}
								}
							}
						}
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(fileReader!=null){
					try {
						fileReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(reader!=null){
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			lyric=null;
		}    

		for(String s:lyric){
			System.out.println(s);
		}
		return lyric;
	}



	/**
	 * 对歌词信息进行调整
	 * @param lyricText 歌词内容
	 * @return          List<'String'>
	 */
	public static List<String> parsedLyric(String lyricText){		
		List<String> parsedLyric=new ArrayList<String>();
		String[] originLyric=lyricText.split("\n");
		String reg="\\[(\\d{2}:\\d{2}\\.\\d{2})\\]|\\[\\d{2}:\\d{2}\\]";
		for(int i=0,len=originLyric.length;i<len;i++){
			if(originLyric[i].contains("[ti:")) {       // 歌曲信息
				parsedLyric.add("歌曲信息:"+originLyric[i].substring(originLyric[i].lastIndexOf(":")+1,originLyric[i].length()-2));
			}else if (originLyric[i].contains("[ar:")) {// 歌手信息
				parsedLyric.add("歌手信息:"+originLyric[i].substring(originLyric[i].lastIndexOf(":")+1,originLyric[i].length()-2));
			}else if (originLyric[i].contains("[al:")) {// 专辑信息
				parsedLyric.add("专辑信息:"+originLyric[i].substring(originLyric[i].lastIndexOf(":")+1,originLyric[i].length()-2));
			}else if (originLyric[i].contains("[wl:")) {// 歌词作家
				parsedLyric.add("歌词作家:"+originLyric[i].substring(originLyric[i].lastIndexOf(":")+1,originLyric[i].length()-2));
			}else if (originLyric[i].contains("[wm:")) {// 歌曲作家
				parsedLyric.add("歌曲作家:"+originLyric[i].substring(originLyric[i].lastIndexOf(":")+1,originLyric[i].length()-2));
			}else if (originLyric[i].contains("[al:")) {// 歌词制作
				parsedLyric.add("歌词制作:"+originLyric[i].substring(originLyric[i].lastIndexOf(":")+1,originLyric[i].length()-2));
			}
			Pattern pattern=Pattern.compile(reg);
			Matcher matcher=pattern.matcher(originLyric[i]);
			while(matcher.find()){
				parsedLyric.add(matcher.group(0).substring(1,matcher.group(0).lastIndexOf("]")).trim()+originLyric[i].substring(originLyric[i].lastIndexOf("]")+1).trim());
			}
		}
		for(int i=0;i<parsedLyric.size();i++){
			if(Pattern.matches("^\\d+$",parsedLyric.get(i).substring(0,2))){
				if(Pattern.matches("^\\d{2}:\\d{2}\\.\\d{1}$",parsedLyric.get(i).substring(0,7))){
					for(int j=0;j<parsedLyric.size();j++){
						if(Pattern.matches("^\\d+$",parsedLyric.get(j).substring(0, 2))){
							if(Double.parseDouble(parsedLyric.get(i).substring(0, 2))<Double.parseDouble(parsedLyric.get(j).substring(0, 2))){
								String temp=parsedLyric.get(i);
								parsedLyric.set(i, parsedLyric.get(j));
								parsedLyric.set(j, temp);
							}
							if(Double.parseDouble(parsedLyric.get(i).substring(0, 2))==Double.parseDouble(parsedLyric.get(j).substring(0, 2)) && Double.parseDouble(parsedLyric.get(i).substring(3,8))<Double.parseDouble(parsedLyric.get(j).substring(3,8))){
								String temp=parsedLyric.get(i);
								parsedLyric.set(i, parsedLyric.get(j));
								parsedLyric.set(j, temp);
							}
						}
					}
				}else if(Pattern.matches("^\\d{2}:\\d{2}$",parsedLyric.get(i).substring(0,5))){
					for(int j=0;j<parsedLyric.size();j++){
						if(Pattern.matches("^\\d+$",parsedLyric.get(j).substring(0, 2))){
							if(Integer.parseInt(parsedLyric.get(i).substring(0, 2))<Integer.parseInt(parsedLyric.get(j).substring(0, 2))){
								String temp=parsedLyric.get(i);
								parsedLyric.set(i, parsedLyric.get(j));
								parsedLyric.set(j, temp);
							}
							if(Integer.parseInt(parsedLyric.get(i).substring(0, 2))==Integer.parseInt(parsedLyric.get(j).substring(0, 2)) && Integer.parseInt(parsedLyric.get(i).substring(3,5))<Double.parseDouble(parsedLyric.get(j).substring(3,5))){
								String temp=parsedLyric.get(i);
								parsedLyric.set(i, parsedLyric.get(j));
								parsedLyric.set(j, temp);
							}
						}
					}
				}
			}
		}
		for(String s:parsedLyric){
			System.out.println(s);
		}
		return parsedLyric;
	}


}

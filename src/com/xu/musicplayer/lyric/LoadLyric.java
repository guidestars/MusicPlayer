package com.xu.musicplayer.lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xu.musicplayer.system.Constant;

public class LoadLyric {

	public static void main(String[] args) throws IOException {
		LoadLyric lyric =  new LoadLyric();
		lyric.lyric("F:\\KuGou\\丸子呦 - 广寒宫.lrc");
		for (String s:Constant.PLAY_LYRIC) {
			System.out.println(s);
		}
	}

	/**
	 * 获取本地歌曲歌词
	 * @param musicfile
	 */
	public List<String> lyric(String path){
		Constant.PLAY_LYRIC.clear();
		File file=new File(path);
		if(file.exists()){
			FileReader fReader=null;
			BufferedReader bReader=null;
			try {
				fReader = new FileReader(file);
				bReader=new BufferedReader(fReader);
				String txt="";
				String reg="\\[(\\d{2}:\\d{2}\\.\\d{2})\\]|\\[\\d{2}:\\d{2}\\]";				
				while((txt=bReader.readLine())!=null){
					if(txt.contains("[ti:")) {       // 歌曲信息
						Constant.PLAY_LYRIC.add("歌曲信息: "+Constant.SPLIT+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1)+"");
					}else if (txt.contains("[ar:")) {// 歌手信息
						Constant.PLAY_LYRIC.add("歌手信息: "+Constant.SPLIT+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1)+" ");
					}else if (txt.contains("[al:")) {// 专辑信息
						Constant.PLAY_LYRIC.add("专辑信息: "+Constant.SPLIT+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1)+" ");
					}else if (txt.contains("[wl:")) {// 歌词作家
						Constant.PLAY_LYRIC.add("歌词作家: "+Constant.SPLIT+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1)+" ");
					}else if (txt.contains("[wm:")) {// 歌曲作家
						Constant.PLAY_LYRIC.add("歌曲作家: "+Constant.SPLIT+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1)+" ");
					}else if (txt.contains("[al:")) {// 歌词制作
						Constant.PLAY_LYRIC.add("歌词制作: "+Constant.SPLIT+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1)+" ");
					} else {
						Pattern pattern=Pattern.compile(reg);
						Matcher matcher=pattern.matcher(txt);
						while(matcher.find()){
							Constant.PLAY_LYRIC.add(matcher.group(0).substring(1,matcher.group(0).lastIndexOf("]")).trim()+Constant.SPLIT+txt.substring(txt.lastIndexOf("]")+1).trim()+" ");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(fReader!=null){
					try {
						fReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(bReader!=null){
					try {
						bReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{

		}
		return Constant.PLAY_LYRIC;
	}

	@SuppressWarnings("unused")
	private List<String> sort_lyric(List<String> lyric){
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
				}else{

				}
			}
		}
		return lyric;
	}

}

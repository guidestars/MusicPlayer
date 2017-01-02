package com.xu.roboto.analysis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws Exception {

		System.out.println(ping());
		
	}


	public static String ping() throws IOException{
		Runtime runtime=Runtime.getRuntime();
		Process process=runtime.exec("ping 192.168.14.16");
		BufferedInputStream inputStream=(BufferedInputStream) process.getInputStream();
		byte [] bt =new byte[1024];
		StringBuffer buffer=new StringBuffer();
		int len=0;
		while((len=inputStream.read(bt, 0,bt.length))!=-1){
			buffer.append(new String(bt, 0, len, "GBK"));
		}
		String regex="(\\d*%)?";
		String result="";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(buffer.toString());
		while(matcher.find()){
			if(!matcher.group().equals("")){
				result=matcher.group();
			}
		}
		return result;
	}
}

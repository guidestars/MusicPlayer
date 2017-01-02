package com.xu.roboto.analysis;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.eclipse.ui.internal.handlers.WizardHandler.New;
import org.htmlparser.Parser;
import org.htmlparser.visitors.HtmlPage;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class NetUtil {

	public static void main(String[] args) throws Exception {
		//http://yinyue.cloudmeng.com/listen/song/?key=演员-薛之谦
		URL url=new URL("http://yinyue.cloudmeng.com/listen/song/");
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);

		BufferedOutputStream outputStream=new BufferedOutputStream(connection.getOutputStream());

		outputStream.write("key=演员-薛之谦".getBytes());
		outputStream.flush();
		
		BufferedInputStream inputStream=new BufferedInputStream(connection.getInputStream());
		
		int len=0;
		byte[] bt=new byte[1024];
		while((len=inputStream.read(bt, 0, bt.length))!=-1){
			//System.out.println( new String(bt,"ISO-8859-1"));
			//String iso=new String(bt,"ISO-8859-1");
			String utf=new String(bt,"utf-8");
			System.out.println(utf);
		}

	}
	
}

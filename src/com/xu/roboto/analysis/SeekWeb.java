package com.xu.roboto.analysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//这是 从网上扒取数据的方法
public class SeekWeb {
	public static void main(String[] aegs) throws IOException{
		/*Document doc = Jsoup.connect("http://www.23356.com/geci/98570.htm").get(); 
		Element singerListDiv = doc.getElementsByAttributeValue("class", "content").first(); 
		Elements links = singerListDiv.getElementsByTag("div"); 
	   
		for (Element link: links) { 
	      String linkHref = link.attr("href"); 
	      String linkText = link.text().trim(); 
	      System.out.println(linkHref);  
		} */
		
		Document doc = Jsoup.connect("http://www.23356.com/geci/98570.htm").get(); //你要抽取的地址
		Elements divs = doc.select("div.Lyric_center"); //最外层的<div class=Lyric_center></div>
     	for (Element div: divs) { 
     		//System.out.println(div.select("div[class=Lyric_right]")); //你要抽取的标签<div class=Lyric_right></div>
     		
     		//把网页获取的数据保存到txt文件
         	FileOutputStream fop = null;
         	File file;
         	String content =div.select("div[class=Lyric_right]").toString();
         	
         	try {
         		file = new File("e:/sing.txt");
         		fop = new FileOutputStream(file);

         		if (!file.exists()) {
         			file.createNewFile();
         		}

         		byte[] contentInBytes = content.getBytes(); //字节流输入

         		fop.write(contentInBytes);
         		fop.flush();
         		fop.close();
         		
         	} catch (IOException e) {
         		e.printStackTrace();
         	} finally {
         		try {
         			if (fop != null) {
         				fop.close();
         			}
         		} catch (IOException e) {
         			e.printStackTrace();
         		}
         	}
     	}
     	System.out.println("储存成功");
 		
	}
}

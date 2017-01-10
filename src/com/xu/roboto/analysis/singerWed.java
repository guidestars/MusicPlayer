package com.xu.roboto.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class singerWed {
	//http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.catalogSug&query=秋天不回来
		//http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.lry&songid=2087474
		//private static String json;  
		
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
	    
	    public static String ascii2native(String ascii) {  
	        int n = ascii.length() / 6;  
	        StringBuilder sb = new StringBuilder(n);  
	        for (int i = 0, j = 2; i < n; i++, j += 6) {  
	            String code = ascii.substring(j, j + 4);  
	            char ch = (char) Integer.parseInt(code, 16);  
	            sb.append(ch);  
	        }  
	        return sb.toString();  
	    } 
	    
	    public static void main(String[] args) throws UnsupportedEncodingException {  
	        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.catalogSug&query=秋天不回来";   
	        String json = loadJson(url);
	       
	        
	        //正则表达式去除所有
	        Pattern pattern = Pattern.compile("[,|]+");
	        String[] strs = pattern.split(json);
	        for (int i=0;i<strs.length;i++) {
	            //System.out.println( decodeUnicode (strs[i]));
	            
	            String regEx="[`~!@#$%^&*()+=|{}';',.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？/\"/g]";
	            Pattern p = Pattern.compile(regEx);
	            Matcher m = p.matcher(decodeUnicode (strs[i]));
	            System.out.println(m.replaceAll("").trim());
	            
	        }
	            
	            
	            
	          /*//把网页获取的数据保存到txt文件
	         	FileOutputStream fop = null;
	         	File file;
	         	String content =m.replaceAll("").trim();
	         	
	         	try {
	         		file = new File("e:/sing1.txt");
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
	     	//Title();
	     	
	 	*/
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
	  
	    
	    /*public static void Title(){
	    	  BaiduWed bw=new BaiduWed();
	    	  String regEx1="[`~!@#$%^&*()+=|{}';',.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？/\"/g]";
	          Pattern p1= Pattern.compile(regEx1);
	          Matcher m1 = p1.matcher(decodeUnicode (bw.json));
	          String tle=m1.toString().substring(10, 20);
	          System.out.println(m1); 
	    }*/
	}



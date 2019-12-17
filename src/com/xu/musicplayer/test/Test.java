/**  
 * 
 * @Title:  Test.java   
 * @Package com.xu.musicplayer.test   
 * @Description:    TODO   
 * @author: xuhyacinth     
 * @date:   2019骞�1鏈�13鏃� 涓嬪崍12:02:59   
 * @version V1.0 
 * @Copyright: 2019 xuhyacinth
 *
 */
package com.xu.musicplayer.test;


import com.xu.musicplayer.player.Player;
import com.xu.musicplayer.player.XMusic;

/**
 * @author xuhyacinth
 *
 */
public class Test {
	public static void main(String[] args) throws Exception {
		Player player = new XMusic();
		player.load("C:\\Users\\Administrator\\Desktop\\TEST.wav");
		player.start();
	}
}

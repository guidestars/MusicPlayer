package com.xu.musicplayer.lyric;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.xu.musicplayer.player.XMusic;

/**
 * Java MusicPlayer 音频线程
 * @Author: hyacinth
 * @ClassName: SpectrumThread   
 * @Description: TODO    
 * @Date: 2019年12月26日 下午7:58:50   
 * @Copyright: hyacinth
 */
public class SpectrumThread extends Thread {

	private Composite spectrum;
	private BufferedImage image;
	private int spectrum_height;
	private int width;
	private int height;

	public SpectrumThread(Composite spectrum,int width,int height) {
		this.spectrum = spectrum;
		this.width = width;
		this.height = height;
	}

	@Override
	public void run() {
		while (XMusic.isPlaying()) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					Graphics2D graphics = image.createGraphics();
					image = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
					graphics.dispose();
					graphics = image.createGraphics();
					graphics.setColor(Color.YELLOW);
					graphics.setStroke(new BasicStroke(1f));

					if (XMusic.deque.size()>50) {
						for (int i = 0,len = XMusic.deque.size(); i < 151; i++) {
							try {
								if (i<len) {
									spectrum_height = Integer.parseInt(XMusic.deque.get(i)+"");
								}
								spectrum_height = spectrum_height>100?100:spectrum_height;
							} catch (Exception e) {
								spectrum_height = 0;									
							}
							if (spectrum_height>0) {
								graphics.fillRect(i*5, height/2-spectrum_height, 5, spectrum_height);
							} else {
								graphics.fillRect(i*5, height/2, 5, -spectrum_height);
							}
						}							
					}
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					try {
						ImageIO.write(image, "png", stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
					InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
					if (inputStream!=null) {
						spectrum.setBackgroundImage(new Image(null, new ImageData(inputStream).scaledTo(width,height)));
					}
					graphics.dispose();
				}
			});
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

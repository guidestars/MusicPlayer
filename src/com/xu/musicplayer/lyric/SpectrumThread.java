package com.xu.musicplayer.lyric;

import com.xu.musicplayer.player.XMusic;
import com.xu.musicplayer.system.Constant;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Java MusicPlayer 音频线程
 *
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

    public SpectrumThread(Composite spectrum) {
        this.spectrum = spectrum;
    }

    @Override
    public void run() {
        while (XMusic.isPlaying()) {
            Display.getDefault().asyncExec(() -> {
                width = Constant.SPECTRUM_WIDTH;
                height = Constant.SPECTRUM_HEIGHT;
                image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                //image = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
                //graphics.dispose();
                //graphics = image.createGraphics();
                graphics.setBackground(Constant.SPECTRUM_BACKGROUND_COLOR);
                graphics.clearRect(0, 0, width, height);
                graphics.setColor(Constant.SPECTRUM_COLOR);
                graphics.setStroke(new BasicStroke(1f));
                if (XMusic.deque.size() > 50) {
                    for (int i = 0, len = XMusic.deque.size(); i < 178; i++) {
                        try {
                            if (i < len) {
                                spectrum_height = Math.abs(Integer.parseInt(XMusic.deque.get(i) + ""));
                                spectrum_height = spectrum_height > height ? height : spectrum_height;
                            }
                        } catch (Exception e) {
                            spectrum_height = 0;
                        }
                        graphics.fillRect(i * 5, height - spectrum_height, 5, spectrum_height);
                        //graphics.fillRect(i*5, height/2-spectrum_height, 5, -spectrum_height);//双谱
                    }
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
                if (inputStream != null) {
                    spectrum.setBackgroundImage(new Image(null, new ImageData(inputStream).scaledTo(width, height)));
                }
                graphics.dispose();
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

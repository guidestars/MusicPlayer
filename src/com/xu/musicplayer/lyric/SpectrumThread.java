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

    private Composite spectrum;//频谱面板

    private int twidth;// 频谱总高读
    private int theight;// 频谱总宽度
    private int sheight;// 频谱高度

    public SpectrumThread(Composite spectrum) {
        this.spectrum = spectrum;
        twidth = Constant.SPECTRUM_TOTAL_WIDTH;
        theight = Constant.SPECTRUM_TOTAL_HEIGHT;
    }

    @Override
    public void run() {
        while (XMusic.isPlaying()) {
            Display.getDefault().asyncExec(() -> {
                twidth = Constant.SPECTRUM_TOTAL_WIDTH;
                theight = Constant.SPECTRUM_TOTAL_HEIGHT;
                draw(1, twidth, theight);
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(int style, int width, int height) {

        InputStream inputStream = null;
        ByteArrayOutputStream stream = null;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();

        //image = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        //graphics.dispose();
        //graphics = image.createGraphics();
        graphics.setBackground(Constant.SPECTRUM_BACKGROUND_COLOR);
        graphics.clearRect(0, 0, width, height);
        graphics.setColor(Constant.SPECTRUM_FOREGROUND_COLOR);
        graphics.setStroke(new BasicStroke(1f));

        if (Constant.SPECTRUM_STYLE == 0) {
            if (XMusic.deque.size() >= Constant.SPECTRUM_SAVE_INIT_SIZE) {
                for (int i = 0, len = XMusic.deque.size(); i < Constant.SPECTRUM_TOTAL_NUMBER; i++) {
                    try {
                        if (i < len) {
                            sheight = Math.abs(Integer.parseInt(XMusic.deque.get(i) + ""));
                            sheight = Math.min(sheight, height);
                        }
                    } catch (Exception e) {
                        sheight = 0;
                    }
                    graphics.fillRect(i * Constant.SPECTRUM_SPLIT_WIDTH, height - sheight, Constant.SPECTRUM_SPLIT_WIDTH, sheight);
                    //graphics.fillRect(i*5, height/2-spectrum_height, 5, -spectrum_height);//双谱
                }
            }
            stream = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", stream);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            inputStream = new ByteArrayInputStream(stream.toByteArray());
            spectrum.setBackgroundImage(new Image(null, new ImageData(inputStream).scaledTo(width, height)));
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else if (Constant.SPECTRUM_STYLE == 1) {
            int indexs = 0;
            if (XMusic.deque.size() >= Constant.SPECTRUM_SAVE_INIT_SIZE) {
                for (int i = 0, len = XMusic.deque.size(); i < Constant.SPECTRUM_TOTAL_NUMBER; i++) {
                    try {
                        if (i < len) {
                            sheight = Math.abs(Integer.parseInt(XMusic.deque.get(i) + ""));
                            sheight = Math.min(sheight, height);
                        }
                    } catch (Exception e) {
                        sheight = 0;
                    }
                    int indexc = 10;
                    for (int j = 0; j < sheight; j = indexc) {
                        graphics.fillRect(indexs, height - indexc, Constant.SPECTRUM_SPLIT_WIDTH, 5);
                        indexc += 7;
                    }
                    indexs += 22;
                }
            }
            stream = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", stream);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            inputStream = new ByteArrayInputStream(stream.toByteArray());
            spectrum.setBackgroundImage(new Image(null, new ImageData(inputStream).scaledTo(width, height)));
        }
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        graphics.dispose();
    }


}

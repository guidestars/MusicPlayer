package com.xu.musicplayer.config;

import com.xu.musicplayer.system.Constant;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import java.io.File;
import java.util.LinkedList;

public class SongChoiceWindow {

    public static void update_show_list(Table table, LinkedList<String> lists) {
        table.removeAll();
        TableItem tableItem = null;
        for (int i = 0, len = lists.size(); i < len; i++) {
            tableItem = new TableItem(table, SWT.NONE);
            tableItem.setText(new String[]{(i + 1) + "", lists.get(i).split(Constant.SPLIT)[1]});
        }
    }

    public LinkedList<String> open_choise_windows(Shell shell) {
        FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
        dialog.setFilterNames(new String[]{"*.mp3", "*.MP3", "*.wav", "*.WAV", "*.flac", "*.FLAC", "*.pcm", "*.PCM"});
        dialog.open();
        String[] lists = dialog.getFileNames();
        String paths = "";
        Constant.PLAY_TEMP_LIST.clear();
        for (int i = 0, len = lists.length; i < len; i++) {
            paths = lists[i];
            if (paths.toLowerCase().endsWith(".mp3") || paths.toLowerCase().endsWith(".flac") || paths.toLowerCase().endsWith(".wav") || paths.toLowerCase().endsWith(".pcm")) {
                paths = dialog.getFilterPath() + File.separator + lists[i];
                paths = paths + Constant.SPLIT + have_lyric(paths);
                Constant.PLAY_TEMP_LIST.add(paths);
            }
        }
        new Writing().write(Constant.PLAY_TEMP_LIST);
        return Constant.PLAY_TEMP_LIST;
    }

    private String have_lyric(String path) {
        path = path.substring(0, path.lastIndexOf("."));
        path += ".lrc";
        if (!(new File(path).exists())) {
            return "N";
        } else {
            return "Y";
        }
    }

}

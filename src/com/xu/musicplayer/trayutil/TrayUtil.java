package com.xu.musicplayer.trayutil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.wb.swt.SWTResourceManager;


public class TrayUtil {
	private Shell shell;
	private Tray tray;
	private Menu menu;

	public TrayUtil(Shell shell, Tray tray) {
		super();
		this.shell = shell;
		this.tray = tray;
	}

	public void tray() {
		if (tray == null) {
			MessageDialog.openError(shell, "错误提示", "您的系统不支持托盘图标");
		} else {
			TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("登录");
			item.setImage(SWTResourceManager.getImage(TrayUtil.class, "/com/xu/musicplayer/image/music.png"));

			menu = new Menu(shell, SWT.POP_UP);

			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event arg0) {
					menu.setVisible(true);
				}
			});
			
			// 放大
			MenuItem max = new MenuItem(menu, SWT.PUSH);
			max.setText("放大");
			max.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent arg0) {
					shell.setVisible(true);
					shell.setMaximized(true);
				}
			});
			
			// 缩小
			MenuItem mini = new MenuItem(menu, SWT.PUSH);
			mini.setText("缩小");
			mini.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent arg0) {
					shell.setMaximized(true);
				}
			});
			
			// 关闭
			new MenuItem(menu, SWT.SEPARATOR);
			MenuItem close = new MenuItem(menu, SWT.PUSH);
			close.setText("关闭");
			close.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent arg0) {
					tray.dispose();
					shell.dispose();
				}
			});
			

		}
	}
}
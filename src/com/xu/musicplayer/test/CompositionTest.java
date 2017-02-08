package com.xu.musicplayer.test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;

public class CompositionTest {

	protected Shell shell;
	protected Composite composite ;
	private Display display ;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CompositionTest window = new CompositionTest();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");

		shell.setLayout(new FillLayout()); 

		Composite composite_1 = new Composite(shell, SWT.NONE);

		GC gc=new GC(composite_1);
		gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN)); 
		gc.fillOval(0,0,100,100);


		//		final Canvas canvas = new Canvas(shell,SWT.NO_REDRAW_RESIZE);
		//		canvas.addPaintListener(new PaintListener() {
		//			public void paintControl(PaintEvent e) {
		//				Rectangle clientArea = canvas.getClientArea();
		//				e.gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN)); 
		//				e.gc.fillOval(0,0,200,200);
		//			}
		//		});

		//		new color(display).start();

	}

	class color extends Thread{

		private Display display;
		public color(Display display){
			this.display=display;
		}
		public void run(){
			display.asyncExec(new Runnable() {
				public void run() {
					while(true){
						GC gc =new GC(composite);
						gc.fillOval(4, 4, 32, 32);
						gc.drawOval(112, 69, 64, 64);
						gc.setBackground(SWTResourceManager.getColor(new RGB(255, 44, 44)));
						gc.dispose();
					}
				}
			});
		}
	}

}

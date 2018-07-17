package com.wzb.game;
import com.wzb.game.GameUtil;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

/**
 * 飞机游戏的主窗口
 * @author WZB
 *
 */
public class MyGameFrame extends Frame{
	
	Image planeImg = GameUtil.getImage("images/plane.png");
	Image bg = GameUtil.getImage("images/bg.jpg");
	
	Plane plane = new Plane(planeImg,250,250);
	Shell[] shells = new Shell[50];
			
	Explode bao;
	Date startTime = new Date();
	Date endTime ;
	int period;
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		 g.drawImage(bg, 0, 0, null);	
		 
		 plane.drawSelf(g);//画飞机
		 //画出所有的炮弹
		 for(int i=0;i<shells.length;i++) {
			 shells[i].draw(g);
			//飞机和炮弹的碰撞检测 
			 boolean peng = shells[i].getRect().intersects(plane.getRect());
			 if(peng) {
				 System.out.println("GameOver.");
				 plane.live = false;
				 if(bao == null) {
				bao = new Explode(plane.x,plane.y);
				
				endTime = new Date();
				
				period = (int)((endTime.getTime()-startTime.getTime())/1000);
				
				 }
				 bao.draw(g);
			 }
			 if(!plane.live) {
				 Font f = new Font("黑体", Font.BOLD, 30);
				 g.setColor(Color.white);
				 g.setFont(f);
				 g.drawString("Survival Time:"+period+"S", (int)100,(int)250);
			 }
		 }
		 g.setColor(c);
	}
//反复重画窗口	
	
	class PaintThread extends Thread{
		@Override
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//定义键盘监听的内部类
	class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			plane.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			plane.minusDirection(e);
		}
			
	}
	
	/**
	 * 初始化窗口
	 */
	public void launchFrame() {
		this.setTitle("Programed by WZB");
		this.setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		this.setLocation(300, 300);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new PaintThread().start();//启动重画窗口的线程
		addKeyListener(new KeyMonitor());//增加键盘的监听
		
		//初始化50个炮弹
		for(int i=0;i<shells.length;i++) {
			shells[i] = new Shell();
		}
		
	}
	
public static void main(String[] args) {
	MyGameFrame f = new MyGameFrame();
	f.launchFrame();
}
	private Image offScreenImage = null;
	public void update(Graphics g) {
		if(offScreenImage == null)
			offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
		
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
}

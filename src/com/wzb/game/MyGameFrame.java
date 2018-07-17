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
 * �ɻ���Ϸ��������
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
		 
		 plane.drawSelf(g);//���ɻ�
		 //�������е��ڵ�
		 for(int i=0;i<shells.length;i++) {
			 shells[i].draw(g);
			//�ɻ����ڵ�����ײ��� 
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
				 Font f = new Font("����", Font.BOLD, 30);
				 g.setColor(Color.white);
				 g.setFont(f);
				 g.drawString("Survival Time:"+period+"S", (int)100,(int)250);
			 }
		 }
		 g.setColor(c);
	}
//�����ػ�����	
	
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
	
	//������̼������ڲ���
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
	 * ��ʼ������
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
		
		new PaintThread().start();//�����ػ����ڵ��߳�
		addKeyListener(new KeyMonitor());//���Ӽ��̵ļ���
		
		//��ʼ��50���ڵ�
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

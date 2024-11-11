package edu.mccc.cos210.bob;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BoB {
	private Score theScore = new Score();
	public BoB() {
		JFrame jf = new JFrame("BoB");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		  //BoBModel model = new BoBModel("RedSquirrel",        500, new Color(180,   0, 200)); // Red S
		//BoBModel model = new BoBModel("Zubenelgenubi",    10000, new Color(173, 216, 230)); // Daniel T
		//BoBModel model = new BoBModel("FlamingBurrito",    5000, new Color(166, 102,  38)); // Christopher M
		//BoBModel model = new BoBModel("SushiDeluxe",       5000, new Color( 48, 231, 255)); // James A
		//BoBModel model = new BoBModel("AngryPorkChop",     2500, new Color(136, 228, 235)); // Alexander M
		//BoBModel model = new BoBModel("ViralInfection",    2500, new Color( 51, 204, 255)); // Tomasz T
		  BoBModel model = new BoBModel("LethalInjection", 2500, new Color(165, 255, 112)); // Gabriel N
		//BoBModel model = new BoBModel("Intimidator",       2000, new Color(130, 130, 130)); // Nicholas C
		//BoBModel model = new BoBModel("ScreamingBanshee",  2000, new Color(  0, 148,  27)); // Simon B
		//BoBModel model = new BoBModel("Beelzebub",         2000, new Color(255, 255, 255)); // Rachana G
		//BoBModel model = new BoBModel("BoneCrusher",       2000, new Color(255,   0, 255)); // Kevin H
		//BoBModel model = new BoBModel("Undertaker",        1750, new Color( 24, 106,  24)); // Justin S
		//BoBModel model = new BoBModel("Lucifer",           1750, new Color(200, 200, 200)); // Robert J
		//BoBModel model = new BoBModel("SmokeyPitMaster",   1750, new Color(255, 255, 255)); // Simon L
		//BoBModel model = new BoBModel("*Armageddon*",      1750, new Color(255, 255, 255)); // Ethan T
		//BoBModel model = new BoBModel("GrimReaper",        1500, new Color(155, 125,  75)); // Gian Niles D G
		//BoBModel model = new BoBModel("Czyzck",            1500, new Color(  0,   0,   0)); // Fatima P
		//BoBModel model = new BoBModel("SilentNinja",       1500, new Color(216, 235, 252)); // Angela C
		//BoBModel model = new BoBModel("*WidowMaker*",      1500, new Color(  0,   0,   0)); // Damon M
		//BoBModel model = new BoBModel("ThunderStorm",      1250, new Color(124, 185, 232)); // Yanwen C
		//BoBModel model = new BoBModel("FireBall",          1250, new Color(211, 211, 211)); // Ekpreet S
		//BoBModel model = new BoBModel("Typhoon",           1250, new Color(  0, 200, 100)); // Madelin G
		//BoBModel model = new BoBModel("SpiderCricket",     1000, new Color(255, 255, 204)); // Thomas B
		//BoBModel model = new BoBModel("*UnluckyBandit*",   1000, new Color(206, 234, 255)); // Sai C
		//BoBModel model = new BoBModel("BeepBeep",            99, new Color(230, 230, 230)); // Batican E

		JPanel view = new BoBView(model);
		jf.add(view);
		jf.pack();
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		view.requestFocus();
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(BoB::new);
	}
	class Score {
		boolean gameOver = false;
		int score = 0;
		int time = 120;
		int numBolts = 0;
		int numBooms = 0;
		int numCows = 0;
		Color scoreColor = Color.BLUE;
	}
	class BoltModel {
		boolean active = false;
		boolean boom = false;
		int countDown;
		double x;
		double y = 512.0 - 32.0;
		double initX;
		double lastBx;
		double lastMx = 0.0;
		public BoltModel(int x, double rand) {
			initX = x;
			this.x = x;
			lastBx = x;
			if (rand >= 0.75) {
				boom = true;
			}
		}
		public void update(BoBModel m) {
			double dx = lastMx - m.x;
			x = lastBx + dx;
			lastBx = x;
			lastMx = m.x;
			if (active && (x > 512 - 102 && x < 512 + 102)) {
				if (boom) {
					theScore.scoreColor = Color.RED;
					theScore.numBooms++;
					theScore.gameOver = true;
				} else {
					theScore.scoreColor = Color.BLUE;
					theScore.numBolts++;
					theScore.score++;
				}
				active = false;
			}
			if (boom) {
				countDown--;
				if (countDown == 0) {
					active = false;
				}
			}
		}
	}
	class CowModel {
		boolean active = false;
		double x;
		double initX;
		double y = -80.0;
		double dy = 12.0;
		double lastCx;
		double lastMx = 0.0;
		public CowModel(int x) {
			initX = x;
			this.x = x;
			lastCx = x;
		}
		public void update(BoBModel m) {
			double dx = lastMx - m.x;
			x = lastCx + dx;
			lastCx = x;
			lastMx = m.x;
			y = y + dy;
			if (active && y  > 512) {
				active = false;
			}
			if (active && (y > 512 - 52 - 78 && (x > 512 - 102 && x < 512 + 102))) {
				theScore.scoreColor = Color.RED;
				theScore.numCows++;
				theScore.score -= 5;
				active = false;
			}
		}
	}
	class BoBModel extends KeyAdapter {
		String bob;
		double x = 0;
		double y = 0;
		double dx = 1;
		double dir = 1;
		Color background;
		int factor;
		public BoBModel(String bob, int factor, Color bg) {
			this.bob = bob;
			this.factor = factor;
			this.background = bg;
		}
		void update() {
			x += dx * dir;
		}
		@Override
		public void keyPressed(KeyEvent ke) {
			switch (ke.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (dx != 0) {
						dir = -1;
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (dx != 0) {
						dir = 1;
					}
					break;
				case KeyEvent.VK_C:					
					dx = 1;					
					break;
			    case KeyEvent.VK_SPACE:
			    	if (dx != 0) {
			    		dir *= -1;
			    	}
			    	break;
			    case KeyEvent.VK_X:
			    case KeyEvent.VK_UP:
			    	dx = dx + 1 > 15 ? dx : dx + 1;
			    	break;
			    case KeyEvent.VK_Z:
			    case KeyEvent.VK_DOWN:
			    	dx = dx - 1 < 1 ? 1 : dx - 1;
			    	break;
			    default:
			    	break;
			}
		}
	}
	class BoBView extends JPanel {
		private static final long serialVersionUID = 1L;
		BoBModel model;
		ArrayList<CowModel> cows = new ArrayList<>();
		ArrayList<BoltModel> bolts = new ArrayList<>();
		BufferedImage tornado;
		BufferedImage background;
		BufferedImage bob;
		BufferedImage bobf;
		BufferedImage bobr;
		BufferedImage cow;
		BufferedImage bolt;
		BufferedImage boom;
		boolean storm = false;
		int tick = 0;
		public BoBView(BoBModel model) {
			this.model = model;
			background = loadBackground(model.background);
			bob = loadBoB();
			loadCow();
			loadTornado();
			loadBolt();
			setPreferredSize(new Dimension(1024, 512));
			addKeyListener(model);
			new javax.swing.Timer(1000 / 30, ae -> step()).start();
		}
		void step() {
			if (theScore.gameOver) {
				return;
			}
			if (tick % 30 == 0) {
				theScore.time--;
				if (theScore.time == 0) {
					theScore.scoreColor = Color.GREEN;
					theScore.gameOver = true;
				}
			}
			model.update();
			for (CowModel cm : cows) {
				cm.update(model);
			}
			for (BoltModel bm : bolts) {
				bm.update(model);
			}
			if (model.dir == -1) {
				bob = bobr;
			} else {
				bob = bobf;
			}
			spawnBolts(tick);
			checkTheWeather(tick);
			if (storm) {
				dropCow(tick);
			}
			tick++;
			repaint();
		}
		void spawnBolts(int tick) {
			int localTick = tick % 100;
			int rand = randInt(0, bolts.size() - 1);
			if (localTick % 11 == 0) {
				BoltModel bm = bolts.get(rand);
				if (!bm.active && !(bm.initX > 512 - 102 && bm.initX < 512 + 102)) {
					bm.x = bm.initX;
					bm.lastBx = bm.initX;
					bm.active = true;
					bm.countDown = 87;
				}
			}
		}
		void dropCow(int tick) {
			int localTick = tick % 100;
			int rand = randInt(0, cows.size() - 1);
			if (localTick % 13 == 0) {
				CowModel cow = cows.get(rand);
				if (!cow.active) {
					cow.active = true;
					cow.x = cow.initX;
					cow.lastCx = cow.initX;
					cow.y = -78.0;
				}
			}
		}
		void checkTheWeather(int tick) {
			int localTick = tick % 500;
			if (localTick == 400) {
				storm = true;
			} else {
				if (localTick == 300) {
					storm = false;
				}
			}
		}
		void loadBolt() {
			try {
				bolt = ImageIO.read(new File("./images/bolt.png"));
				boom = ImageIO.read(new File("./images/boom.png"));
				int count = 0;
				while (count < 7 || count > 9) {
					bolts.clear();
					count = 0;
					for (int i = -10; i < 18; i++) {
						if (i < 3 || i > 5) {
							bolts.add(new BoltModel(i * 128 - 16, Math.random()));
						}
					}
					for (BoltModel bm : bolts) {
						if (bm.boom) {
							count++;
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}
		void loadTornado() {
			try {
				tornado = ImageIO.read(new File("./images/tornado1.jpg"));
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}
		void loadCow() {
			try {
				cow = ImageIO.read(new File("./images/cow.png"));
				for (int i = -10; i < 18; i++) {
					cows.add(new CowModel(i * 128 - 39));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}
		BufferedImage loadBoB() {
			try {
				bobf = ImageIO.read(new File("./bobs/" + model.bob + "_25.png"));
				bobr = ImageIO.read(new File("./bobs/" + model.bob + "_25_R.png"));
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			return bobf;			
		}
		BufferedImage loadBackground(Color color) {
			BufferedImage background = null;
			try {
				background = ImageIO.read(new File("./images/background.png"));
				for (int r = 0; r < background.getHeight(); r++) {
					for (int c = 0; c < background.getWidth(); c++) {
						if (background.getRGB(c, r) == 0xff0000ff) {
							background.setRGB(c, r, color.getRGB());
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			return background;
		}
		void paintBackground(Graphics2D g2d) {
			double x = model.x;
			while (x <= -1024.0) {
				x += 2048.0;
			}
			double tx =(x + 1024.0) % 2048;
			AffineTransform at = AffineTransform.getTranslateInstance(-tx, 0.0);
			g2d.drawRenderedImage(background, at);
		}
		void paintTornado(Graphics2D g2d) {
			g2d.drawRenderedImage(tornado, new AffineTransform());
		}
		void paintBoB(Graphics2D g2d) {
			AffineTransform at = AffineTransform.getTranslateInstance(getWidth() / 2.0 - bob.getWidth() / 2.0, getHeight() - bob.getHeight());
			g2d.drawRenderedImage(bob, at);
		}
		void paintCow(CowModel model, Graphics2D g2d) {
			AffineTransform at = AffineTransform.getTranslateInstance(model.x, model.y);
			g2d.drawRenderedImage(cow, at);
		}
		void paintBolt(BoltModel model, Graphics2D g2d) {
			AffineTransform at = AffineTransform.getTranslateInstance(model.x, model.y);
			g2d.drawRenderedImage(bolt, at);
		}
		void paintBoom(BoltModel model, Graphics2D g2d) {
			AffineTransform at = AffineTransform.getTranslateInstance(model.x, model.y);
			g2d.drawRenderedImage(boom, at);
		}
		RoundRectangle2D rr2d = null;
		Font font = null;
		void paintScore(Graphics2D g2d) {
			if (rr2d == null) {
				rr2d = new RoundRectangle2D.Double(768, 16, 196, 128, 32, 32);
			}
			g2d.setPaint(new Color(240, 240, 240, 128));
			g2d.fill(rr2d);
			if (font == null) {
				font = g2d.getFont().deriveFont(48.0f);
			}
			g2d.setFont(font);
			String t = String.valueOf(theScore.time);
			String s = String.valueOf(theScore.score);
			FontMetrics fm = g2d.getFontMetrics();
			int tw = SwingUtilities.computeStringWidth(fm, t);
			int sw = SwingUtilities.computeStringWidth(fm, s);
			g2d.setPaint(Color.BLACK);
			g2d.setStroke(new BasicStroke(0.0f));
			g2d.draw(rr2d);
			g2d.drawString(t, 907 - tw, 69);
			g2d.setPaint(theScore.scoreColor);
			g2d.drawString(s, 907 - sw, 128);
		}
		void paintFinalScore(Graphics2D g2d) {
			RoundRectangle2D rr2d = new RoundRectangle2D.Double(32, 32, 1024 - 64, 512 - 64, 64, 64);
			g2d.setPaint(new Color(240, 240, 240, 128));
			g2d.fill(rr2d);
			Font font1 = g2d.getFont().deriveFont(48.0f);
			Font font2 = g2d.getFont().deriveFont(48.0f * 4);
			g2d.setFont(font1);
			String t = String.valueOf(theScore.time);
			String s = String.valueOf(theScore.score);
			FontMetrics fm = g2d.getFontMetrics();
			int tw = SwingUtilities.computeStringWidth(fm, t);
			g2d.setPaint(Color.BLACK);
			g2d.setStroke(new BasicStroke(0.0f));
			g2d.draw(rr2d);
			g2d.drawString(t, 512 - tw / 2, 150);
			g2d.setFont(font2);
			fm = g2d.getFontMetrics();
			int sw = SwingUtilities.computeStringWidth(fm, s);
			g2d.setPaint(theScore.scoreColor);
			g2d.drawString(s, 512 - sw / 2, 325);
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			if (storm) {
			    paintTornado(g2d);
			}
			paintBackground(g2d);
			for (BoltModel bolt : bolts) {
				if (bolt.active) {
					if (bolt.boom) {
						paintBoom(bolt, g2d);
					} else {
						paintBolt(bolt, g2d);
					}
				}
			}
			for (CowModel cow : cows) {
				if (cow.active) {
					paintCow(cow, g2d);
				}
			}
			paintBoB(g2d);
			if (theScore.gameOver) {
				paintFinalScore(g2d);
			} else {
				paintScore(g2d);
			}
			g2d.dispose();
            Toolkit.getDefaultToolkit().sync();
		}
	}
    static int randInt(int min, int max) {
        if (max < min) {
            var tmp = min;
            min = max;
            max = tmp;
        }
        return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
    }
}

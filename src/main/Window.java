package main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import gameObjects.Constants;
import graphics.Assets;
import graphics.Loader;
import input.KeyBoard;
import input.MouseInput;
import states.LoadingState;
import states.State;
import java.awt.Image;

/**
 
Proyecto: Proyecto Aplicativo Final,
,
Integrantes del equipo:,
Roberto Vargas,
,
Jose Bernardo Noguera,
,
Alexander Otoniel Pineda,
,
Alejandro Garrido,
,
,
Fecha de entrega: 20 de mayo de 2025*/

public class Window extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	private static Window instance;
	
	private Canvas canvas;
	private Thread thread;
	private boolean running = false;
	
	private BufferStrategy bs;
	private Graphics g;
	
	private final int FPS = 60;
	private double TARGETTIME = 1000000000/FPS;
	private double delta = 0;
	private int AVERAGEFPS = FPS;
	
	private KeyBoard keyBoard;
	private MouseInput mouseInput;
	
	public static Window getInstance() {
		if (instance == null) {
			instance = new Window();
		}
		return instance;
	}
	
	public KeyBoard getKeyBoard() {
		return keyBoard;
	}
	
	public Window()
	{
		setTitle("Juego de Nave Espacial");
		setSize(Constants.WIDTH, Constants.HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (running) {
					stop();
				}
				System.exit(0);
			}
		});
		setResizable(false);
		setLocationRelativeTo(null);
		try {
			Image icon = ImageIO.read(getClass().getResource("/logo.png"));
			setIconImage(icon);
		} catch (IOException | IllegalArgumentException e) {
			System.out.println("No se pudo cargar .el icono");
		}
		
		canvas = new Canvas();
		keyBoard = new KeyBoard();
		mouseInput = new MouseInput();
		
		canvas.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		canvas.setMaximumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		canvas.setMinimumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		canvas.setFocusable(true);
		
		add(canvas);
		canvas.addKeyListener(keyBoard);
		canvas.addMouseListener(mouseInput);
		canvas.addMouseMotionListener(mouseInput);
		setVisible(true);
	}
	
	

	public static void main(String[] args) {
		Window.getInstance().start();
	}
	
	
	private void update(float dt){
		keyBoard.update();
		State.getCurrentState().update(dt);
	}

	private void draw(){
		bs = canvas.getBufferStrategy();
		
		if(bs == null)
		{
			canvas.createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		
		//-----------------------
		
		g.setColor(Color.BLACK);
		
		g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		
		State.getCurrentState().draw(g);
		
		g.setColor(Color.WHITE);
		
		g.drawString(""+AVERAGEFPS, 10, 20);
		
		//---------------------
		
		g.dispose();
		bs.show();
	}
	
	private void init()
	{
		
		Thread loadingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Assets.init();
			}
		});
		
		
		
		State.changeState(new LoadingState(loadingThread));
	}
	
	
	@Override
	public void run() {
		
		long now = 0;
		long lastTime = System.nanoTime();
		int frames = 0;
		long time = 0;
		
		init();
		
		while(running)
		{
			now = System.nanoTime();
			delta += (now - lastTime)/TARGETTIME;
			time += (now - lastTime);
			lastTime = now;
			
			if(delta >= 1)
			{	
				update((float) (delta * TARGETTIME * 0.000001f));
				draw();
				delta --;
				frames ++;
			}
			if(time >= 1000000000)
			{

				AVERAGEFPS = frames;
				frames = 0;
				time = 0;
				
			}
			
			
		}
		
		stop();
	}
	
	private void start(){
		
		thread = new Thread(this);
		thread.start();
		running = true;
		
		
	}
	private void stop(){
		if (!running) return; // Evita detener el hilo si ya está detenido
		running = false;
		try {
			if (thread != null && thread.isAlive()) {
				// Aseguramos que el estado actual se limpie antes de detener el hilo
				State.changeState(null);
				thread.join(2000); // Espera máximo 2 segundos para que el hilo termine
				if (thread.isAlive()) {
					System.out.println("El hilo del juego no se pudo detener correctamente");
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Error al detener el hilo del juego: " + e.getMessage());
		}
	}
}
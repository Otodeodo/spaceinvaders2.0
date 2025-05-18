package states;

import java.awt.Graphics;

public abstract class State {
	
	private static State currentState = null;
	
	public static State getCurrentState() {return currentState;}
	public static void changeState(State newState) {
		if (currentState != null) {
			currentState.cleanup();
		}
		currentState = newState;
	}
	
	// Método para limpiar recursos cuando se cambia de estado
	protected void cleanup() {
		// Por defecto no hace nada, las subclases pueden sobrescribir este método
	}
	
	
	public abstract void update(float dt);
	public abstract void draw(Graphics g);
	
}

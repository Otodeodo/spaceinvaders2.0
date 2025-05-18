package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import gameObjects.Constants;
import graphics.Assets;
import graphics.Text;
import math.Vector2D;
import ui.Action;
import ui.Button;

public class CreditsState extends State {
    private Button returnButton;
    private int finalScore;
    private float showTime;
    private static final float CREDITS_DURATION = 3.0f; // Duración en segundos
    
    public CreditsState(int score) {
        this.finalScore = score;
        
        returnButton = new Button(
            Assets.greyBtn,
            Assets.blueBtn,
            Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
            Constants.HEIGHT / 2 + 100,
            "VOLVER AL MENÚ",
            new Action() {
                @Override
                public void doAction() {
                    State.changeState(new MenuState());
                }
            }
        );
    }
    
    @Override
    public void update(float dt) {
        showTime += dt;
        if (showTime >= CREDITS_DURATION) {
            State.changeState(new MenuState());
            return;
        }
        returnButton.update();
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Título
        Text.drawText(g2d, "FIN DEL JUEGO", new Vector2D(Constants.WIDTH / 2, 100), true, Color.WHITE, Assets.fontBig);
        
        // Puntuación final
        Text.drawText(g2d, "Puntuación Final: " + finalScore, new Vector2D(Constants.WIDTH / 2, 200), true, Color.WHITE, Assets.fontMed);
        
        // Créditos
        Text.drawText(g2d, "Desarrollado por:", new Vector2D(Constants.WIDTH / 2, 300), true, Color.WHITE, Assets.fontMed);
        Text.drawText(g2d, "Team Fenix", new Vector2D(Constants.WIDTH / 2, 340), true, Color.WHITE, Assets.fontMed);
        
        // Agradecimientos
        Text.drawText(g2d, "Gracias por jugar", new Vector2D(Constants.WIDTH / 2, 400), true, Color.WHITE, Assets.fontMed);
        
        returnButton.draw(g);
    }
}
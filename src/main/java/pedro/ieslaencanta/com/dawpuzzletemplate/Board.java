/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.dawpuzzletemplate;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.E;
import javafx.scene.paint.Color;

/**
 * Tablero del juego, posee un fondo (imagen estática, solo se cambia cuando se
 * cambia el nivel), Una bola, un vector de niveles, un disparador y una matriz
 * de bolas gestiona la pulsación de tecla derecha e izquierda
 *
 * @author Pedro
 * @see Bubble Level Shttle BallGrid
 */
public class Board implements IKeyListener {

    private Rectangle2D game_zone;

    private GraphicsContext gc;
    private GraphicsContext bggc;
    private Dimension2D original_size;
    private Bubble ball;
    private Shuttle shuttle;
    private boolean pause;
    private boolean debug;
    private boolean left_press, right_press;

    /**
     * constructor
     *
     * @param original
     */
    public Board(Dimension2D original) {
        this.gc = null;
        this.game_zone = new Rectangle2D(95, 23, 128, 200);
        this.original_size = original;
        this.right_press = false;
        this.left_press = false;
        this.shuttle = new Shuttle(new Point2D((this.game_zone.getMaxX() - this.game_zone.getWidth() / 2), (this.game_zone.getMaxY() - 18)));

        this.debug = false;
        this.setDebug(true);
        this.pause = false;

    }

    /**
     * muestra el grid
     */
    private void Debug() {
        this.bggc.setStroke(Color.RED);
        for (int i = 0; i < 12; i++) {
            //se pinta las lineas horizontales

            this.bggc.strokeLine(this.game_zone.getMinX() * Game.SCALE,
                    (this.game_zone.getMinY() + i * 16) * Game.SCALE,
                    (this.game_zone.getMaxX()) * Game.SCALE,
                    (this.game_zone.getMinY() + i * 16) * Game.SCALE);

            //se pintan las líneas verticales desplazando en las impares
            for (int j = 0; j < 8; j++) {
                this.bggc.strokeLine(
                        //inicio x va cambiando entre pares e impares
                        (this.game_zone.getMinX() + j * 16 + ((i % 2 != 0) ? 8 : 0)) * Game.SCALE,
                        //inicio de y, siempre el mismo
                        (this.game_zone.getMinY() + i * 16) * Game.SCALE,
                        //fin de x va cambiando entre pares e impares
                        (this.game_zone.getMinX() + j * 16 + ((i % 2 != 0) ? 8 : 0)) * Game.SCALE,
                        //fin de y es igual en todas
                        (this.game_zone.getMinY() + i * 16 + 16) * Game.SCALE);
            }
        }
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
        this.shuttle.setDebug(debug);
        if (this.ball != null) {
            this.ball.setDebug(debug);
        }

    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;

    }

    public void setBackGroundGraphicsContext(GraphicsContext gc) {
        this.bggc = gc;
        this.paintBackground();
    }

    /**
     * cuando se produce un evento
     */
    public synchronized void TicTac() {
        if (!this.pause) {
            this.clear();
            this.render();
            this.process_input();
            //actualizar
            this.update();
        }
    }

    private void update() {
        //actualizar el juego
        if (this.ball != null && this.ball.getBalltype() != null) {
            this.ball.move(this.game_zone);
            this.shuttle.paint(gc);
        }

    }

    private void render() {
        if (this.ball != null && this.ball.getBalltype() != null) {
            this.ball.paint(gc);

        }
        this.shuttle.paint(gc);

    }

    private void process_input() {
        if (this.left_press) {

        } else if (this.right_press) {

        } else {

        }
    }

    /**
     * limpiar la pantalla
     */
    private void clear() {
        this.gc.restore();
        this.gc.clearRect(0, 0, this.original_size.getWidth() * Game.SCALE, this.original_size.getHeight() * Game.SCALE);
    }

    /**
     * pintar el fonodo
     */
    public void paintBackground() {
        Image imagen = Resources.getInstance().getImage("fondos");
        this.bggc.drawImage(imagen,
                //se obtiene del original
                345, 280, //dlya izmenenia ecrana
                this.original_size.getWidth(),
                this.original_size.getHeight(),
                //se pinta en el lienzo
                1, 1,
                this.original_size.getWidth() * Game.SCALE,
                this.original_size.getHeight() * Game.SCALE
        );

        //se dibuja la línea del fondo
        if (this.isDebug()) {
            this.Debug();
        }

    }

    /**
     * gestión de pulsación
     *
     * @param code
     */
    @Override
    public void onKeyPressed(KeyCode code) {
        switch (code) {
            case LEFT:
                this.left_press = true;
                this.shuttle.moveLeft();
                break;
            case RIGHT:
                this.right_press = true;
                this.shuttle.moveRight();

                break;

        }
    }

    @Override
    public void onKeyReleased(KeyCode code) {
        switch (code) {
            case LEFT:
                this.left_press = false;
                this.shuttle.moveLeft();
                break;
            case RIGHT:
                this.right_press = false;
                this.shuttle.moveRight();

                break;
            case ENTER:

                this.paintBackground();
                break;
            case SPACE:

                //se crea
                this.ball = new Bubble();
                this.ball.setDebug(debug);
                //se coloca el tipo de forma aleatorioa
                this.ball.setBalltype(BubbleType.values()[(int) (Math.random() * BubbleType.values().length)]);
                //se pone la posición (centro) y ángulo aleatorio
                this.ball.init(new Point2D((this.game_zone.getMaxX() - this.game_zone.getWidth() / 2),
                        (this.game_zone.getMaxY() - 18)), (float) (Math.random() * 360));
                this.ball.play();
                break;
            case P:
                this.pause=!this.pause;
                break;
            case E:
                break;
            case D:
                this.setDebug(!this.debug);
                this.paintBackground();

        }

    }

}

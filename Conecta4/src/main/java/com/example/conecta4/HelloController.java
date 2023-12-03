package com.example.conecta4;


import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {

    private static final int columnas =7;
    private static final int filas = 6;
    private static final int diametro = 80;
    private static final String color1 = "#f92f1a";
    private static final String color2 = "#ecf706";

    private static String jugador1 = "ROJAS";
    private static String jugador2 = "AMARILLAS";

    private boolean isPlayerOneTurn = true;

    private Disc[][] insertedDiscArray = new Disc[filas][columnas];

    @FXML
    public GridPane rootGridPane;

    @FXML
    public Pane insertedDiscsPane;

    @FXML
    public Label playerNameLabel;

    private boolean isAllowToInsert = true;

    @FXML
    private RadioButton playerVsPlayerButton;

    @FXML
    private RadioButton playerVsAIButton;

    private ToggleGroup toggleGroup;

    private boolean isPlayerVsPlayer = true;
    private boolean isPlayerVsIA = false;


    @FXML
    public TextField playerOneTextField, playerTwoTextField;

    @FXML
    public Button setNamesButton;


    @FXML
    public void startPlayerVsPlayerGame() {
        isPlayerVsPlayer = true;
        startNewGame();
    }


    @FXML
    private void startNewGame() {
        resetGame();
        isPlayerOneTurn = true;
        playerNameLabel.setText(jugador1);
        isAllowToInsert = true;
        insertedDiscsPane.getChildren().clear();
    }


    public void createPlayground(){
        setNamesButton.setOnAction(event ->
        {
            jugador1 = playerOneTextField.getText();
            jugador2 = playerTwoTextField.getText();
        });

        Shape rectangleWithHoles = createGameStructuralGrid();

        rootGridPane.add(rectangleWithHoles,0,1);
        List<Rectangle> rectangleList = createClickableColumns();

        for (Rectangle rectangle: rectangleList) {

            rootGridPane.add(rectangle,0,1);
        }

    }

    private Shape createGameStructuralGrid(){
        Shape rectangleWithHoles = new Rectangle((columnas +1) * diametro, (filas +1)* diametro);

        for (int row = 0; row < filas; row++){
            for (int col = 0; col < columnas; col++){
                Circle circle = new Circle();
                circle.setRadius(diametro /2);
                circle.setCenterX(diametro /2);
                circle.setCenterY(diametro /2);
                circle.setSmooth(true);

                circle.setTranslateX(col * (diametro +5) + diametro /4);
                circle.setTranslateY(row * (diametro +5) + diametro /4);

                rectangleWithHoles =Shape.subtract(rectangleWithHoles,circle);
            }
        }
        rectangleWithHoles.setFill(Color.valueOf("#4885e4"));

        return rectangleWithHoles;
    }

    private List<Rectangle> createClickableColumns(){

        List<Rectangle> rectangleList = new ArrayList<>();
        for (int col = 0; col< columnas; col++){

            Rectangle rectangle = new Rectangle(diametro, (filas +1)* diametro);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (diametro +5)+ diametro /4);

            rectangle.setOnMouseEntered (event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

            final int column = col;
            rectangle.setOnMouseClicked(event -> {
                if (isAllowToInsert){
                    isAllowToInsert = false;
                    insertDisc(new Disc(isPlayerOneTurn),column);
                }

            });
            rectangleList.add(rectangle);
        }

        return rectangleList;

    }
    private void insertDisc(Disc disc, int column){
        if (!isPlayerVsPlayer && !isAllowToInsert) {
            // Si es turno de la IA y no se permite insertar, salimos del método
            return;
        }

        int row = filas -1;
        while (row >= 0){
            if(getDiscIfPresent(row,column)==null)
                break;
            row--;
        }
        if (row < 0)
            return;

        insertedDiscArray[row][column] = disc;
        insertedDiscsPane.getChildren().add(disc);

        disc.setTranslateX(column*(diametro + 5) + diametro /4);
        int currentRow = row;
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row* (diametro + 5) + diametro /4);
        translateTransition.setOnFinished(actionEvent -> {

            isAllowToInsert = true;
            if (gameEnded(currentRow,column)){
                gameOver();
                return;
            }

            isPlayerOneTurn = !isPlayerOneTurn;

            if (isPlayerOneTurn) {
                playerNameLabel.setTextFill(Color.RED);
                playerNameLabel.setText(jugador1);


            } else {
                playerNameLabel.setTextFill(Color.YELLOW);
                playerNameLabel.setText(jugador2);
                if (isPlayerVsIA) {
                    playerNameLabel.setTextFill(Color.YELLOW);
                    playerNameLabel.setText(jugador2);
                    realizarMovimientoIA();
                }
            }
        });


        if ("playerVsAIButton".equals(toggleGroup.getSelectedToggle().getUserData()) && isPlayerOneTurn == false) {
            realizarMovimientoIA();
        }
        translateTransition.play();
    }
    private boolean gameEnded(int row,int column){

        List<Point2D> verticalPoints = IntStream.rangeClosed(row-3 ,row + 3)
                .mapToObj(r -> new Point2D(r,column))
                .collect(Collectors.toList());


        List<Point2D> horizontalPoints = IntStream.rangeClosed(column-3 ,column + 3)
                .mapToObj(col -> new Point2D(row,col))
                .collect(Collectors.toList());

        Point2D startPoint1 = new Point2D(row-3,column+3);
        List<Point2D> diagonalPoints = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startPoint1.add(i,-i))
                .collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row-3,column-3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startPoint2.add(i,i))
                .collect(Collectors.toList());

        boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
                || checkCombinations(diagonalPoints) || checkCombinations(diagonal2Points);

        return isEnded;

    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain =0;
        for (Point2D point: points) {


            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc = getDiscIfPresent(rowIndexForArray, columnIndexForArray);
            if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn){
                chain++;
                if (chain == 4){
                    return true;
                }
            } else {
                chain =0;
            }
        }
        return false;
    }
    private Disc getDiscIfPresent(int row, int column){

        if ((row>= filas) || row<0 || column>= columnas || column<0)
            return null;

        return insertedDiscArray[row][column];


    }

    private  void gameOver(){

        String winner = isPlayerOneTurn ? jugador1 : jugador2;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Conecta 4!");
        alert.setHeaderText("Ha ganado: "+ winner);
        alert.setContentText("¿Quieres jugar otra vez? ");

        ButtonType yesBtn = new ButtonType("Si");
        ButtonType noBtn = new ButtonType("No, salir");
        alert.getButtonTypes().setAll(yesBtn,noBtn);

        Platform.runLater(()->{
            Optional<ButtonType> BtnClicked = alert.showAndWait();
            if (BtnClicked.isPresent() && BtnClicked.get()==yesBtn){
                resetGame();
            }else {
                Platform.exit();
                System.exit(0);
            }
        });
    }
    public  void resetGame() {
        insertedDiscsPane.getChildren().clear();
        for (int row =0; row< insertedDiscArray.length; row++){
            for (int col =0; col< insertedDiscArray[row].length; col++){
                insertedDiscArray[row][col] = null;
            }
        }
        isPlayerOneTurn = true;
        playerNameLabel.setText(jugador1);
        createPlayground();

    }

    private static class Disc extends Circle{
        private final boolean isPlayerOneMove;
        public Disc(boolean isPlayerOneMove){
            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(diametro /2);
            setFill(isPlayerOneMove? Color.valueOf(color1): Color.valueOf(color2));

            setCenterX(diametro /2);
            setCenterY(diametro /2);

        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup = new ToggleGroup();
        playerVsPlayerButton.setToggleGroup(toggleGroup);
        playerVsAIButton.setToggleGroup(toggleGroup);
        // Seleccionar inicialmente el botón "Player vs Player"
        playerVsPlayerButton.setSelected(true);

        toggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                if (newToggle.equals(playerVsPlayerButton)) {
                    startPlayerVsPlayerGame();
                } else if (newToggle.equals(playerVsAIButton)) {
                    startPlayerVsPlayerGame2();
                    //startNewGame2();
                }
            }
        });
    }


    // IA
    @FXML
    public void startPlayerVsPlayerGame2() {
        isPlayerVsIA = true;
        resetGame();
        realizarMovimientoIA();
    }

    private void realizarMovimientoIA() {
        int columnaAleatoria = (int) (Math.random() * columnas);

        for (int row = filas - 1; row >= 0; row--) {
            if (getDiscIfPresent(row, columnaAleatoria) == null) {
                Disc disc = new Disc(false); // La IA siempre será el jugador 2
                insertedDiscArray[row][columnaAleatoria] = disc;
                insertedDiscsPane.getChildren().add(disc);

                disc.setTranslateX(columnaAleatoria * (diametro + 5) + diametro / 4);
                int currentRow = row;

                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
                translateTransition.setToY(row * (diametro + 5) + diametro / 4);
                translateTransition.setOnFinished(actionEvent -> {
                    isAllowToInsert = true;
                    if (gameEnded(currentRow, columnaAleatoria)) {
                        gameOver();
                        return;
                    }
                    isPlayerOneTurn = true;
                    playerNameLabel.setText(jugador1);
                });
                translateTransition.play();
                break;
            }
        }
    }
}
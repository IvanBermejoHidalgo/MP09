package com.example.conecta4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {

    private HelloController controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/conecta4/conecta4.fxml"));
        GridPane rootGridPane = fxmlLoader.load();

        controller = fxmlLoader.getController();
        controller.createPlayground();

        MenuBar menu = createMenu();
        menu.prefWidthProperty().bind(stage.widthProperty());

        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menu);

        Scene scene = new Scene(rootGridPane,632,670);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("¡Conecta 4!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    private MenuBar createMenu(){

        Menu menumenu = new Menu("Menu");

        MenuItem nuevaPartida = new MenuItem("Nueva Partida");
        nuevaPartida.setOnAction(actionEvent -> controller.resetGame());

        MenuItem reiniciarPartida = new MenuItem("Reiniciar Partida");
        reiniciarPartida.setOnAction(actionEvent -> controller.resetGame());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("Salir del juego");
        exitGame.setOnAction(actionEvent -> exitGame());

        menumenu.getItems().addAll(nuevaPartida,reiniciarPartida,separatorMenuItem,exitGame);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menumenu);

        return menuBar;
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
    }

    public static void main(String[] args) {
        launch();
    }
}
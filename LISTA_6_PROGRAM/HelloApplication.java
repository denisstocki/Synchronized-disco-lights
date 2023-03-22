//KP_LISTA_6 - DENIS STOCKI

/**
 * IMPORT BIBLIOTEK
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * KLASA MAIN - TWORZY CALY PROGRAM
 */
public class HelloApplication extends Application {

    /**
     * DEKLARACJA ZMIENNYCH OKIENKA ZBIERAJACEGO DANE
     */
    private static Stage stageInfo;
    private static Scene sceneInfo;
    private static GridPane gridPaneInfo;
    private static Label labelDown, labelAcross, labelThempo, labelProbability;
    private static TextField textFieldDown, textFieldAcross, textFieldThempo, textFieldProbability;
    private static Button buttonStart;
    private static ButtonType buttonTypeDialog;
    private static Dialog<String> dialogInfo;
    private static double screenWidth, screenHeight, probability;
    private static int down, across, thempo;

    /**
     * FUNKCJA ODPALA OKIENKO
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        /**
         * IMPLEMENTACJA ZMIENNYCH
         */
        stageInfo = new Stage();
        gridPaneInfo = new GridPane();
        sceneInfo = new Scene(gridPaneInfo, 400, 200);
        textFieldDown = new TextField("Dodatnia liczba naturalna");
        textFieldAcross = new TextField("Dodatnia liczba naturalna");
        textFieldThempo = new TextField("Dodatnia liczba naturalna");
        textFieldProbability = new TextField("Dowolona liczba od 0 do 1");
        labelDown = new Label(" WYSOKOSC PLANSZY: ");
        labelAcross = new Label(" SZEROKOSC PLANSZY: ");
        labelThempo = new Label(" TEMPO: ");
        labelProbability = new Label(" PRAWDOPODOBIENSTWO: ");
        buttonStart = new Button("START");
        buttonTypeDialog = new ButtonType("OK");
        screenWidth = Screen.getPrimary().getBounds().getWidth();
        screenHeight = Screen.getPrimary().getBounds().getHeight();

        /**
         * SYNTEZA ZMIENNYCH I USTAWIENIE OBIEKTOW W GRIDZIE
         */
        gridPaneInfo.setVgap(7);
        gridPaneInfo.setHgap(5);
        gridPaneInfo.add(labelDown, 0, 0);
        gridPaneInfo.add(textFieldDown, 1, 0);
        gridPaneInfo.add(labelAcross, 0,1);
        gridPaneInfo.add(textFieldAcross, 1,1);
        gridPaneInfo.add(labelThempo, 0, 2);
        gridPaneInfo.add(textFieldThempo, 1, 2);
        gridPaneInfo.add(labelProbability, 0,3);
        gridPaneInfo.add(textFieldProbability, 1,3);
        gridPaneInfo.add(buttonStart, 1, 4);

        /**
         * USTAWIENIE TLA OKIENKA NA ZIELONY GRADIENT
         */
        gridPaneInfo.setBackground(Background.fill(new LinearGradient(
                0, 0, 1, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#81c483")),
                new Stop(1, Color.web("#fcc200")))));

        /**
         * USTAWIENIE PARAMETROW STAGE
         */
        stageInfo.setScene(sceneInfo);
        stageInfo.setTitle("PARAMETRY");
        stageInfo.setResizable(false);
        stageInfo.show();

        /**
         * FUNKCJA WYWOLUJE SIE PO NACISNIECIU PRZYCISKU START
         */
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                /**
                 * PROBUJEMY SPRAWDZIC POPRAWNOSC WSZYSTKICH DANYCH
                 */
                try{

                    /**
                     * POPRAWNOSC DOWN
                     */
                    down = Integer.parseInt(textFieldDown.getText());
                    if(down <= 0){
                        throw new NumberFormatException();
                    }

                    /**
                     * POPRAWNOSC ACROSS
                     */
                    across = Integer.parseInt(textFieldAcross.getText());
                    if(across <= 0){
                        throw new NumberFormatException();
                    }

                    /**
                     * POPRAWNOSC THEMPO
                     */
                    thempo = Integer.parseInt(textFieldThempo.getText());
                    if(thempo <= 0){
                        throw new NumberFormatException();
                    }

                    /**
                     * POPRAWNOSC PROBABILITY
                     */
                    probability = Double.parseDouble(textFieldProbability.getText());
                    if(probability < 0 || probability > 1){
                        throw new NumberFormatException();
                    }

                    /**
                     * JESLI SPRAWDZENIE POPRAWNOSCI PRZEBIEGLO POMYSLNIE TO PROGRAM CHOWA OKIENKO I TWORZY OBIEKT FIELDOFTHREADS
                     */
                    stageInfo.hide();
                    FieldOfThreads fieldOfThreads = new FieldOfThreads(down, across, thempo, probability, screenWidth, screenHeight);
                    fieldOfThreads.start();
                }

                /**
                 * LAPIEMY NIEPOPRAWNE DANE
                 */
                catch (NumberFormatException e){

                    /**
                     * WYSWIETLENIE DIALOGU INFORMUJACEGO O WPISANIU NIEPOPRAWNYCH DANYCH
                     */
                    dialogInfo = new Dialog<String>();
                    dialogInfo.setTitle("UWAGA");
                    dialogInfo.setContentText("Podaj prawidlowe dane dla parametrow !");
                    dialogInfo.setResizable(false);
                    dialogInfo.getDialogPane().getButtonTypes().add(buttonTypeDialog);
                    dialogInfo.showAndWait();
                }
            }
        });
    }

    /**
     * FUNKCJA URUCHAMIA PROGRAM
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}
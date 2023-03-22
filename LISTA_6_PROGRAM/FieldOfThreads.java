//KP_LISTA_6 - DENIS STOCKI
/**
 * IMPORT BIBLIOTEK
 */
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;

/**
 * KLASA FIELD - ROZSZERZA GRIDPANE
 */
public class FieldOfThreads extends GridPane {

    /**
     * DEKLARACJA ZMIENNYCH
     */
    private static Random random;
    private static Stage stage;
    private static Scene scene;
    private static NewRectangle newRectangle;
    private int leftNeighbourX, rightNeighbourX, upperNeighbourY, downNeighbourY;
    private static int down, across, thempo;
    private static double probability, screenWidth, screenHeight;
    private ArrayList<ArrayList<Thread>> threadList = new ArrayList<>();
    private ArrayList<ArrayList<NewRectangle>> newRectangleList = new ArrayList<>();

    /**
     * KONSTRUKTOR KLASY FIELD
     */
    public FieldOfThreads(int down, int across, int thempo, double probability, double screenWidth, double screenHeight){
        super();
        this.down = down;
        this.across = across;
        this.thempo = thempo;
        this.probability = probability;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * FUNKCJA CREATEFIELD - TWORZY CALA PRZESTRZEN W GRIDZIE KORZYSTAJAC Z KLASY RECTANGLE
     */
    public void createField(){
        for(int i=0; i<across; i++){

            /**
             * UTWORZENIE LIST WATKOW ORAZ RECTANGLEOW
             */
            threadList.add(new ArrayList<>());
            newRectangleList.add(new ArrayList<>());
            for(int j=0; j<down; j++){

                /**
                 * UZUPELNIENIE LISTY RECTANGLEOW
                 */
                newRectangle = new NewRectangle(down, across, thempo, probability, screenWidth, screenHeight, random, i, j, this);
                newRectangleList.get(i).add(newRectangle);

                /**
                 * STWORZENIE WATKA I USTAWIENIE NA DAEMON
                 */
                Thread thread = new Thread(newRectangle);
                thread.setDaemon(true);

                /**
                 * WYPELNIANIE GRIDA
                 */
                add(newRectangle, i, j);

                /**
                 * UZUPELNIENIE LISTY THREADOW
                 */
                threadList.get(i).add(thread);
            }
        }
    }

    /**
     * FUNKCJA STARTFIELD - ROZPOCZYNA PRACE WATKOW DODANYCH DO GRIDA
     */
    public void  startField(){
        for(int i=0; i<across; i++){
            for(int j=0; j<down; j++){

                /**
                 * ZNAJDOWANIE WSPOLRZEDNYCH SASIADOW DANEGO RECTANGLEA
                 */
                rightNeighbourX = (i + 1) % across;
                downNeighbourY = (j + 1) % down;
                leftNeighbourX = i - 1;
                upperNeighbourY = j - 1;
                if (i == 0) leftNeighbourX = across - 1;
                if (j == 0) upperNeighbourY = down - 1;

                /**
                 * WYSLANIE SASIADOW DO DANEGO RECTANGLEA
                 */
                newRectangleList.get(i).get(j).setNeighbours(newRectangleList.get(i).get(upperNeighbourY), newRectangleList.get(i).get(downNeighbourY),
                        newRectangleList.get(rightNeighbourX).get(j), newRectangleList.get(leftNeighbourX).get(j));

                /**
                 * WYSTARTOWANIE DANEGO WATKU
                 */
                threadList.get(i).get(j).start();
            }
        }
    }

    /**
     * FUNKCJA START - STARTUJE PRACE TEJ KLASY
     */
    public void start(FieldOfThreads this){

        /**
         * IMPLEMENTACJA ZMIENNYCH
         */
        stage = new Stage();
        scene = new Scene(this, screenWidth, screenHeight);
        random = new Random();

        /**
         * WYWOLANIA FUNKCJI
         */
        createField();
        startField();

        /**
         * SYNTEZA ZMIENNYCH
         */
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("DISCO");
        stage.setFullScreen(true);
        stage.show();
    }
}

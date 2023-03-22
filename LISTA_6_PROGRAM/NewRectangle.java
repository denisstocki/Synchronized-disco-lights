//KP_LISTA_6 - DENIS STOCKI

/**
 * IMPORT BIBLIOTEK
 */
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

/**
 * KLASA NEWTHREAD - ROZSZERZA RECTANGLE I IMPLEMENTUJE RUNNABLE
 */
public class NewRectangle extends Rectangle implements Runnable{

    /**
     * DEKLARACJA ZMIENNYCH DANEGO RECTANGLE
     */
    private static GridPane gridPane;
    private static Random random;
    private Color color;
    private boolean threadSuspended;
    private int thempo, i, j, red, green, blue, activeNeighbourCounter;
    private static double probability;
    private  ArrayList<NewRectangle> neighbourList = new ArrayList<>();

    /**
     * KONSTRUKTOR KLASY NEWRECTANGLE - USTAWIA POCZATKOWY KOLOR RECTANGLE
     * @param down
     * @param across
     * @param screenWidth
     * @param screenHeight
     * @param probability
     * @param thempo
     * @param i
     * @param j
     */
    public NewRectangle(int down, int across, int thempo, double probability, double screenWidth, double screenHeight, Random random, int i, int j, GridPane gridPane) {
        super(screenWidth / across, screenHeight / down);
        this.thempo = thempo;
        this.probability = probability;
        this.random = random;
        this.i = i;
        this.j = j;
        this.gridPane = gridPane;

        /**
         * USTAWIENIE POCZATKOWEGO KOLORU
         */
        this.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));

        /**
         * USTAWIENIE POCZATKOWEGO STANU AKTYWNOSCI
         */
        threadSuspended = false;

        /**
         * REAKCJA NA NACISNIECIE MYSZKA NA OBIEKT
         */
        setOnMouseClicked(mouseEvent -> {
            threadSuspended = !threadSuspended;
            if(!threadSuspended){
                synchronized (this){

                    /**
                     * WATEK ZOSTAJE PRZEBUDZONY
                     */
                    notify();
                }
            }
        });
    }

    /**
     * FUNKCJA SETNEIGHBOURS - OTRZYMUJE 4 SASIADOW TEGO OBIEKTU I ZAPISUJE ICH W TABLICY
     * @param upperNeighbour
     * @param lowerNeighbour
     * @param rightNeighbour
     * @param leftNeighbour
     */
    public void setNeighbours(NewRectangle upperNeighbour, NewRectangle lowerNeighbour, NewRectangle rightNeighbour, NewRectangle leftNeighbour){
        neighbourList.add(upperNeighbour);
        neighbourList.add(lowerNeighbour);
        neighbourList.add(rightNeighbour);
        neighbourList.add(leftNeighbour);
    }

    /**
     * FUNKCJA COLORCHANGE - ZMIENIA KOLOR RECTANGLE W ZALEZNOSCI OD PROBABILITY
     */
    public void changeColor(double chance){

        /**
         * START POJEDYNCZEJ ZMIANY WATKU
         */
        System.out.println(Thread.currentThread().getName()+" --- single start");

        /**
         * BLOK IF LOSUJE RANDOMOWY KOLOR DLA RECTANGLE I USTAWIA GO
         */
        if (chance <= probability) {
            Platform.runLater(() -> {
                this.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            });

            /**
             * WYKONANIE POJEDYNCZEJ RANDOMOWEJ ZMIANY
             */
            System.out.println(Thread.currentThread().getName() + " --- single random change");
        }

        /**
         * BLOK ELSE USREDNIA KOLOR SASIADOW DLA RECTANGLE I USTAWIA GO
         */
        else {

            /**
             * POCZATKOWE WARTOSCI
             */
            red = 0;
            green = 0;
            blue = 0;
            activeNeighbourCounter = 0;

            /**
             * USTALANIE RGB
             */
            for(int i=0; i<4; i++){

                /**
                 * BLOK IF WYKONA SIE JESLI DANY SASIAD NIE JEST ZAWIESZONY
                 */
                if(!neighbourList.get(i).threadSuspended){
                    activeNeighbourCounter++;
                    color = (Color) neighbourList.get(i).getFill();
                    red += (int) (255 * color.getRed());
                    green += (int) (255 * color.getGreen());
                    blue += (int) (255 * color.getBlue());
                }
            }

            /**
             * BLOK IF ZABEZPIECZA PRZED DZIELENIEM PRZEZ 0
             */
            if(activeNeighbourCounter!=0){
                red /= activeNeighbourCounter;
                green /= activeNeighbourCounter;
                blue /= activeNeighbourCounter;
                Platform.runLater(() -> {
                    this.setFill(Color.rgb(red, green, blue));
                });

                /**
                 * WYKONANIE POJEDYNCZEJ SREDNIEJ ZMIANY
                 */
                System.out.println(Thread.currentThread().getName() + " --- single average change");
            }

            /**
             * BLOK ELSE WYKONA SIE GDY WSZYSCY SASIEDZI SA ZAWIESZENI
             */
            else System.out.println(Thread.currentThread().getName() + " --- no change");
        }

        /**
         * KONIEC POJEDYNCZEJ ZMIANY WATKU
         */
        System.out.println(Thread.currentThread().getName()+" --- single end\n");
    }

    /**
     * FUNKCJA RUN - STARTUJE DZIALANIE WATKU
     */
    public void run() {

        /**
         * START PRACY DANEGO WATKU
         */
        System.out.println(Thread.currentThread().getName()+" --- work started");

        /**
         * NIESKONCZONA PRACA WATKU
         */
        while (true) {
            try {

                /**
                 * PAUZA DLA DANEGO WATKU
                 */
                Thread.sleep((long) (thempo * (random.nextFloat(1.0F) + 0.5)));

                /**
                 * ZSYNCHRONIZOWANE ZAWIESZENIE WATKU
                 */
                synchronized (this) {
                    while (threadSuspended){
                        wait();
                    }
                }
                /**
                 * ZSYNCHRONIZOWANE KOLOROWANIE RECTANGLE
                 */
                synchronized (gridPane){
                    this.changeColor(random.nextDouble(1));
                }
            }

            /**
             * LAPANIE WYJATKOW
             */
            catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

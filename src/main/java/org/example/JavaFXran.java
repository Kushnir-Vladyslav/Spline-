package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static org.example.GlobalState.*;
import static org.example.GlobalState.getScreenWidth;
import static org.example.Main.*;

public class JavaFXran extends Application {

    private WritableImage writableImage;
    private ImageView imageView;

    private int position = 0;

    private int view = 0;

    @Override
    public void start(Stage primaryStage) {

        writableImage = new WritableImage(ScreenWidth, ScreenHeight);

        Pixels = new int [ScreenWidth * ScreenHeight];

        imageView = new ImageView(writableImage);
//        imageView.setPreserveRatio(true); // Зберігати пропорції
        imageView.setFitWidth(getScreenWidth());
        imageView.setFitHeight(getScreenHeight());

        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, getScreenWidth(), getScreenHeight());

        primaryStage.setTitle("Simple render");
        primaryStage.setScene(scene);
        primaryStage.show();


        //обробники подій зміни розміру вікна
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            setScreenWidth(newValue.intValue());
            updateImageSize();
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            setScreenHeight(newValue.intValue());
            updateImageSize();
        });

        //Обробка натискання клавіш
        scene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case UP, W -> IsUp = true;
                case DOWN, S -> IsDown = true;
                case LEFT, A -> IsLeft = true;
                case RIGHT, D -> IsRight = true;
            }
        });

        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case UP, W -> IsUp = false;
                case DOWN, S -> IsDown = false;
                case LEFT, A -> IsLeft = false;
                case RIGHT, D -> IsRight = false;
            }
        });

        //Обробка натискання лівої кнопки миші і руху
        scene.setOnMousePressed((event) -> {

        });

        //Обробка руху мишки
        scene.setOnMouseDragged((event) -> {

        });

        scene.setOnScroll((event) -> {

        });


        //запуск таймеру для анімації
        AnimationTimer animationTimer = new AnimationTimer() {
            private long last = 0;
            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }

                Time += (float) (now - last) / 1000000000;

                updatePixels((float) (now - last) / 1000000000);

                writableImage.getPixelWriter().setPixels(0, 0,
                        ScreenWidth, ScreenWidth,
                        PixelFormat.getIntArgbInstance(), Pixels, 0, ScreenWidth);

                last = now;
            }
        };

        draw();
        animationTimer.start();
    }

    float minX;
    float maxX;

    float minY;
    float maxY;

    public void draw () {
        fillBackground(0xFFFFFFFF);

        minX = x[0];
        maxX = x[0];

        minY = y[0];
        maxY = y[0];

        for (int i = 0; i < x.length; i++ ) {
            if (minX > x[i]) minX = x[i];
            if (maxX < x[i]) maxX = x[i];
        }
        for (int i = 0; i < y.length; i++ ) {
            if (minY > y[i]) minY = y[i];
            if (maxY < y[i]) maxY = y[i];
        }

        spline();

//        float dx = (maxX - minX) * 0.1f;
//        minX -= dx;
//        maxX += dx;
//
//        float dy = (maxY - minY) * 0.1f;
//        minY -= dy;
//        maxY += dy;

        for (int i = 0; i < x.length; i++) {
            int X = (int) ((x[i] - minX) / (maxX - minX) * ScreenWidth);
            int Y = (int) ((y[i] - minY) / (maxY - minY) * ScreenWidth);

            for (int xx = X - 1; xx <= X + 1; xx++) {
                for (int yy = Y - 1; yy <= Y + 1; yy++) {
                    if (xx >= 0 && xx < ScreenWidth && yy >= 0 && yy < ScreenWidth)
                    Pixels[xx + yy * ScreenWidth] = 0xFF0000FF;
                }
            }
        }
    }

    public void spline () {
        float step = (x[1] - x[0]) / 100;

        for (int i = 0; i < x.length - 1; i++) {
            for (float j = x[i]; j < x[i + 1]; j += step) {
                float newY = result[i].getY(x[i], j);

                if (minY > newY) minY = newY;
                if (maxY < newY) maxY = newY;
            }
        }

        float dx = (maxX - minX) * 0.1f;
        minX -= dx;
        maxX += dx;

        float dy = (maxY - minY) * 0.1f;
        minY -= dy;
        maxY += dy;

        for (int i = 0; i < x.length - 1; i++) {
            for (float j = x[i]; j < x[i + 1]; j += step) {
                int X = (int) ((j - minX) / (maxX - minX) * ScreenWidth);
                int Y = (int) ((result[i].getY(x[i], j) - minY) / (maxY - minY) * ScreenWidth);


                Pixels[X + Y * ScreenWidth] = 0xFF000000;
            }
        }
    }


    //оновлення вікна після зміни розміру
    private void updateImageSize() {
//        writableImage = new WritableImage( GlobalState.getScreenWidth(), GlobalState.getScreenHeight());
//        GlobalState.Pixels = new int[ GlobalState.getScreenWidth() * GlobalState.getScreenHeight()];
//        imageView.setImage(writableImage);
        imageView.setFitWidth(getScreenWidth());
        imageView.setFitHeight(getScreenHeight());
    }



    //основна функція відрисовки
    private void updatePixels(float time)  {

//        fillBackground(0xFF0000FF);



    }



    //функція для закрашування фону, та оновлення масиву глибин
    public static void fillBackground (int color) {
        for (int y = 0; y < ScreenWidth; y++) {
            for (int x = 0; x < ScreenWidth; x++) {
                Pixels[y * ScreenWidth + x] = color;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


package org.example;


public class GlobalState {
    //початковий розмір вікна, може змінюватись користувачем
    public static int ScreenWidth = 400;
    public static int ScreenHeight = 400;

    //масив пікселів що відображаються на екрані
    public static int[] Pixels;

    //Час від початку запуску програми
    public static float Time = 0.f;

    public static Result[] result;

    // швидкість преміщення
    public static float Speed = 100.f;

    // Камера


    //натисненні клавіші
    public static boolean IsUp = false;
    public static boolean IsLeft = false;
    public static boolean IsRight = false;
    public static boolean IsDown = false;

    public static int getScreenWidth() {
        return ScreenWidth;
    }

    public static int getScreenHeight() {
        return ScreenHeight;
    }

    public static void setScreenWidth(int Width) {
        ScreenWidth = Width;
    }

    public static void setScreenHeight(int Height) {
        ScreenHeight = Height;
    }

    public static void setScreenSize(int Width, int Height) {
        ScreenWidth = Width;
        ScreenHeight = Height;
    }


}

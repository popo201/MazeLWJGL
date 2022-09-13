package app;

import global.LwjglWindow;
import renderer.Renderer;

public class App {

    public static void main(String[] args) {
        new LwjglWindow(new Renderer());
    }
}

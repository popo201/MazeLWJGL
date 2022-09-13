package renderer;

import environment.*;
import maze.Maze;
import global.AbstractRenderer;
import global.GLCamera;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import transforms.Point3D;
import transforms.Vec3D;

import java.nio.DoubleBuffer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static global.GluUtils.gluPerspective;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Renderer extends AbstractRenderer {
    private float dx, ox;
    private float zenit, azimut;

    private boolean mouseButton1 = false;

    private GLCamera camera;

    private long oldmils;
    private long oldFPSmils;
    private double fps;
    private String textFPS;
    private Point3D cameraPoint3D;
    private AABB player;
    private final double playerSize = 12.5;
    private Vec3D oldCameraPoint;
    private List<AABB> aabbList;
    private AABB aabbTeleport;
    private AABB aabbFinish;
    private int level = 1;
    private Maze maze;
    private String textTeleport;
    private String textFinish;
    private long startTime;
    private String finishText;

    public Renderer() {
//        super();
        /*used default glfwWindowSizeCallback see AbstractRenderer*/
        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    // We will detect this in our rendering loop
                    glfwSetWindowShouldClose(window, true);

                if (action == GLFW_RELEASE) {
                    switch (key) {
                        case GLFW_KEY_T -> {
                            if (intersect(player, aabbTeleport)) {
                                level = (level + 1) % 4;
                                if (level == 2) {
                                    aabbList = maze.getAabbListL2();
                                    aabbTeleport = maze.getTeleportL2();
                                    camera.setPosition(new Vec3D(-175, 10, 175));
                                }
                                if (level == 3) {
                                    aabbList = maze.getAabbListL3();
                                    aabbFinish = maze.getFinishL3();
                                    camera.setPosition(new Vec3D(25, 10, 25));
                                }
                            }
                            display();
                        }
                        case GLFW_KEY_SPACE -> {
                            double y = camera.getPosition().getY();
                            double step = 1.1;
                            if (y < 20) {
                                for (int i = 0; i < 10; i++) {
                                    y *= step;
                                    camera.setPosition(camera.getPosition().withY(y));
                                }
                            } else {
                                for (int i = 0; i < 10; i++) {
                                    y /= step;
                                    camera.setPosition(camera.getPosition().withY(y));
                                }
                            }
                        }
                    }
                }
                switch (key) {
                    case GLFW_KEY_W -> {
                        checkCollisions();
                        camera.forward(5);
                    }
                    case GLFW_KEY_S -> {
                        checkCollisions();
                        camera.backward(5);
                    }
                    case GLFW_KEY_A -> {
                        checkCollisions();
                        camera.left(5);
                    }
                    case GLFW_KEY_D -> {
                        checkCollisions();
                        camera.right(5);
                    }
                    case GLFW_KEY_Q -> {
                        azimut -= 5;
                        azimut = azimut % 360;
                        camera.setAzimuth(Math.toRadians(azimut));
                    }
                    case GLFW_KEY_E -> {
                        azimut += 5;
                        azimut = azimut % 360;
                        camera.setAzimuth(Math.toRadians(azimut));
                    }
                    case GLFW_KEY_R -> {
                        level = 1;
                        aabbList = maze.getAabbListL1();
                        aabbTeleport = maze.getTeleportL1();
                        camera.setPosition(new Vec3D(75, 10, 75));
                        finishText = "";
                        textFinish = "";
                        startTime = System.currentTimeMillis();
                    }
                }
            }
        };

        glfwMouseButtonCallback = new GLFWMouseButtonCallback() {

            @Override
            public void invoke(long window, int button, int action, int mods) {
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                double x = xBuffer.get(0);

                mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    ox = (float) x;
                }
            }

        };

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                if (mouseButton1) {
                    dx = (float) x - ox;
                    ox = (float) x;
                    if (zenit > 90)
                        zenit = 90;
                    if (zenit <= -90)
                        zenit = -90;
                    azimut += dx / height * 180;
                    azimut = azimut % 360;
                    camera.setAzimuth(Math.toRadians(azimut));
                    dx = 0;
                }
            }
        };

        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                //do nothing
            }
        };
    }

    @Override
    public void init() {
        super.init();
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        camera = new GLCamera();
        camera.setPosition(new Vec3D(75, 10, 75));
        camera.setFirstPerson(true);

        SkyBox skyBox = new SkyBox();
        skyBox.createSkybox();

        startTime = System.currentTimeMillis();

        cameraPoint3D = new Point3D(camera.getPosition().getX(), camera.getPosition().getY(), camera.getPosition().getZ());
        player = new AABB(cameraPoint3D.getX() - playerSize, cameraPoint3D.getY() - playerSize, cameraPoint3D.getZ() - playerSize,
                cameraPoint3D.getX() + playerSize, cameraPoint3D.getY() + playerSize, cameraPoint3D.getZ() + playerSize);

        maze = new Maze();
        maze.createMaze();

        aabbList = maze.getAabbListL1();
        aabbTeleport = maze.getTeleportL1();
        aabbFinish = new AABB(0, -250, 0, 0, -250, 0);
    }


    private void showFPS() {
        long mils = System.currentTimeMillis();
        if ((mils - oldFPSmils) > 300) {
            fps = 1000 / (double) (mils - oldmils + 1);
            oldFPSmils = mils;
        }
        textFPS = String.format(Locale.US, "FPS %3.1f", fps);
        oldmils = mils;
    }

    private boolean intersect(AABB a, AABB b) {
        return (a.getMinX() <= b.getMaxX() && a.getMaxX() >= b.getMinX()) &&
                (a.getMinY() <= b.getMaxY() && a.getMaxY() >= b.getMinY()) &&
                (a.getMinZ() <= b.getMaxZ() && a.getMaxZ() >= b.getMinZ());
    }

    private void playerCollision() {
        cameraPoint3D = new Point3D(camera.getPosition().getX(), camera.getPosition().getY(), camera.getPosition().getZ());
        player = new AABB(cameraPoint3D.getX() - playerSize, cameraPoint3D.getY() - playerSize, cameraPoint3D.getZ() - playerSize,
                cameraPoint3D.getX() + playerSize, cameraPoint3D.getY() + playerSize, cameraPoint3D.getZ() + playerSize);
        for (AABB aabb : aabbList) {
            if (intersect(player, aabb)) camera.setPosition(oldCameraPoint);
        }
        oldCameraPoint = camera.getPosition();
    }

    private void teleportCollision() {
        cameraPoint3D = new Point3D(camera.getPosition().getX(), camera.getPosition().getY(), camera.getPosition().getZ());
        player = new AABB(cameraPoint3D.getX() - playerSize, cameraPoint3D.getY() - playerSize, cameraPoint3D.getZ() - playerSize,
                cameraPoint3D.getX() + playerSize, cameraPoint3D.getY() + playerSize, cameraPoint3D.getZ() + playerSize);
        if (intersect(player, aabbTeleport)) {
            textTeleport = "Stiskněte klávesu [T]";
        } else {
            textTeleport = "";
        }
    }

    private void finishCollision() {
        long finishTime = System.currentTimeMillis();
        cameraPoint3D = new Point3D(camera.getPosition().getX(), camera.getPosition().getY(), camera.getPosition().getZ());
        player = new AABB(cameraPoint3D.getX() - playerSize, cameraPoint3D.getY() - playerSize, cameraPoint3D.getZ() - playerSize,
                cameraPoint3D.getX() + playerSize, cameraPoint3D.getY() + playerSize, cameraPoint3D.getZ() + playerSize);
        if (intersect(player, aabbFinish)) {
            textFinish = "Vyhrál(a) jste. Stiskněte ESC pro ukončení nebo R pro opakování hry.";
            finishText = "Finished time: " + getTime(finishTime - startTime);
        } else {
            textFinish = "";
        }
    }

    private void checkCollisions() {
        playerCollision();
        teleportCollision();
        finishCollision();
    }

    private String getDirection() {
        int az = (int) azimut;
        if (az > 0) {
            if (az >= 315 && az < 360 || az <= 45) {
                return "North";
            }
            if (az < 135) {
                return "West";
            }
            if (az < 225) {
                return "South";
            }
            if (az < 315) {
                return "East";
            }
        } else {
            az = Math.abs(az);
            if (az >= 315 && az < 360 || az <= 45) {
                return "North";
            }
            if (az < 135) {
                return "East";
            }
            if (az < 225) {
                return "South";
            }
            if (az < 315) {
                return "West";
            }
        }
        return "";
    }

    private String getTime(long mills) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Timestamp(mills - 3600 * 1000));
    }

    @Override
    public void display() {
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        showFPS();
        String text = this.getClass().getName() + ": [lmb] directions" + " and [WASD] move," + " [R] reset";

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45, width / (float) height, 0.1f, 500.0f);

        teleportCollision();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glPushMatrix();
        camera.setMatrix();
        float uhel = 0;
        glRotatef(uhel, 0, 1, 0);

        glCallList(1);
        if (level == 1)
            glCallList(2);
        if (level == 2)
            glCallList(3);
        if (level == 3)
            glCallList(4);

        glPopMatrix();

        String textInfo = "position " + camera.getPosition().toString();
        textInfo += String.format(" azimuth %3.1f, zenith %3.1f", azimut, zenit);

        //create and draw text
        textRenderer.addStr2D(3, 20, text);
        textRenderer.addStr2D(3, 40, textInfo);
        textRenderer.addStr2D(3, 60, textFPS);
        textRenderer.addStr2D(3, 80, "Facing: " + getDirection());
        textRenderer.addStr2D(3, 100, "Time: " + getTime(System.currentTimeMillis() - startTime));
        textRenderer.addStr2D(3, 120, finishText);
        textRenderer.addStr2D(3, 150, textTeleport);
        textRenderer.addStr2D(3, height - (height / 2), textFinish);
        textRenderer.addStr2D(width - 160, height - 3, " (c) Pavel Hampl");
    }
}

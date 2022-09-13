package maze;

import environment.AABB;
import blocks.PathBlock;
import blocks.TeleportBlock;
import blocks.WallBlock;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Level2 {

    private final List<AABB> aabbList;
    private AABB teleportAABB;
    private final int size;

    public Level2() {
        aabbList = new ArrayList<>();
        size = 25;
        teleportAABB = new AABB();
    }

    public void createLeveL2() {
        WallBlock wallBlock = new WallBlock();
        PathBlock pathBlock = new PathBlock();
        TeleportBlock teleportBlock = new TeleportBlock();
        float x, y, z;
        int size = 50;

        glNewList(3, GL_COMPILE);
        glPushMatrix();
        glTranslatef(225, 0, -175);
        glEnable(GL_TEXTURE_2D);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);

        glColor3f(0.5f, 0.5f, 0);
        for (int i = 0; i < 8; i++) {
            glTranslatef(-50, 0, 0);
            pathBlock.createPath();
            for (int j = 0; j < 7; j++) {
                glTranslatef(0, 0, 50);
                pathBlock.createPath();
            }
            glTranslatef(0, 0, -350);
        }

        glPopMatrix();

        glPushMatrix();
        glTranslatef(275, 0, 225);
        x = 275;
        y = 0;
        z = 225;

        glColor3f(0.5f, 0.5f, 0);
        for (int i = 0; i < 10; i++) {
            x = x - size;
            glTranslatef(-50, 0, 0);
            wallBlock.createWall();
            addToAabbList(x, y, z);
        }
        glPopMatrix();

        glPushMatrix();
        glTranslatef(225, 0, 275);
        x = 225;
        y = 0;
        z = 275;

        for (int i = 0; i < 10; i++) {
            z = z - size;
            glTranslatef(0, 0, -50);
            wallBlock.createWall();
            addToAabbList(x, y, z);
        }
        glPopMatrix();

        glPushMatrix();
        glTranslatef(-275, 0, -225);
        x = -275;
        y = 0;
        z = -225;

        glColor3f(0.5f, 0.5f, 0);
        for (int i = 0; i < 10; i++) {
            x = x + size;
            glTranslatef(50, 0, 0);
            wallBlock.createWall();
            addToAabbList(x, y, z);
        }
        glPopMatrix();

        glPushMatrix();
        glTranslatef(-225, 0, -275);
        x = -225;
        y = 0;
        z = -275;

        for (int i = 0; i < 10; i++) {
            z = z + size;
            glTranslatef(0, 0, 50);
            wallBlock.createWall();
            addToAabbList(x, y, z);
        }
        glPopMatrix();

        glPushMatrix();
        glTranslatef(-175, 0.1f, -175);
        x = -175;
        y = 0.1f;
        z = -175;

        teleportBlock.createTeleport();
        teleportAABB = new AABB(x - size, y - size, z - size, x + size, y + size, z + size);
        glPopMatrix();

        glPushMatrix();
        glTranslatef(-225, 0, -125);
        x = -225;
        y = 0;
        z = -125;

        for (int i = 0; i < 7; i++) {
            x = x + size;
            glTranslatef(50, 0, 0);
            wallBlock.createWall();
            addToAabbList(x, y, z);

        }
        glPopMatrix();

        glPushMatrix();
        glTranslatef(-225, 0, -75);
        x = -225;
        y = 0;
        z = -75;

        for (int i = 0; i < 2; i++) {
            x = x + size;
            glTranslatef(50, 0, 0);
            wallBlock.createWall();
            addToAabbList(x, y, z);

        }
        x = x + 250;
        glTranslatef(250, 0, 0);
        wallBlock.createWall();
        addToAabbList(x, y, z);
        glPopMatrix();

        glPushMatrix();
        glTranslatef(-225, 0, -25);
        x = -225;
        y = 0;
        z = -25;

        for (int i = 0; i < 2; i++) {
            x = x + size;
            glTranslatef(50, 0, 0);
            wallBlock.createWall();
            addToAabbList(x, y, z);
        }
        glTranslatef(150, 0, 0);
        x = x + 150;
        wallBlock.createWall();
        addToAabbList(x, y, z);
        glTranslatef(100, 0, 0);
        x = x + 100;
        wallBlock.createWall();
        addToAabbList(x, y, z);
        glPopMatrix();

        glPushMatrix();
        glTranslatef(25, 0, 25);
        x = 25;
        y = 0;
        z = 25;

        wallBlock.createWall();
        addToAabbList(x, y, z);
        glTranslatef(100, 0, 0);
        x = x + 100;
        wallBlock.createWall();
        addToAabbList(x, y, z);
        glPopMatrix();

        glPushMatrix();
        glTranslatef(25, 0, 75);
        x = 25;
        y = 0;
        z = 75;

        wallBlock.createWall();
        addToAabbList(x, y, z);
        glTranslatef(100, 0, 0);
        x = x + 100;
        wallBlock.createWall();
        addToAabbList(x, y, z);
        glPopMatrix();

        glPushMatrix();
        glTranslatef(25, 0, 125);
        x = 25;
        y = 0;
        z = 125;

        wallBlock.createWall();
        addToAabbList(x, y, z);
        glTranslatef(100, 0, 0);
        x = x + 100;
        wallBlock.createWall();
        addToAabbList(x, y, z);
        glPopMatrix();

        glPushMatrix();
        glTranslatef(25, 0, 175);
        x = 25;
        y = 0;
        z = 175;

        wallBlock.createWall();
        addToAabbList(x, y, z);
        glPopMatrix();

        glDisable(GL_TEXTURE_2D);
        glEndList();

    }

    private void addToAabbList(float x, float y, float z) {
        aabbList.add(new AABB(x - size, y - size, z - size, x + size, y + size, z + size));
    }

    public List<AABB> getAabbList() {
        return aabbList;
    }

    public AABB getTeleportAABB() {
        return teleportAABB;
    }
}

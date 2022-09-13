package maze;

import environment.AABB;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Maze {

    private final Level1 level1;
    private final Level2 level2;
    private final Level3 level3;

    public Maze() {
        level1 = new Level1();
        level2 = new Level2();
        level3 = new Level3();

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glFrontFace(GL_CW);
        glPolygonMode(GL_FRONT, GL_FILL);
        glPolygonMode(GL_BACK, GL_FILL);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

    }

    public void createMaze() {
        level1.createLeveL1();
        level2.createLeveL2();
        level3.createLeveL3();
    }

    public List<AABB> getAabbListL1() {
        return level1.getAabbList();
    }

    public List<AABB> getAabbListL2() {
        return level2.getAabbList();
    }

    public List<AABB> getAabbListL3() {
        return level3.getAabbList();
    }

    public AABB getTeleportL1() {
        return level1.getTeleportAABB();
    }

    public AABB getTeleportL2() {
        return level2.getTeleportAABB();
    }

    public AABB getFinishL3() {
        return level3.getFinishAABB();
    }
}

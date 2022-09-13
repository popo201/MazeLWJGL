package blocks;

import lwjglutils.OGLTexture2D;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

public class FinishBlock {

    private OGLTexture2D textureBricks;

    public FinishBlock() {
        try {
            textureBricks = new OGLTexture2D("textures/mapFire.jpg");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFinish() {
        glColor3d(0.5, 0.5, 0.5);
        int size = 50 / 2;

        textureBricks.bind(); //-y bottom
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3d(-size, -size, -size);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3d(size, -size, -size);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3d(size, -size, size);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3d(-size, -size, size);
        glEnd();
    }
}

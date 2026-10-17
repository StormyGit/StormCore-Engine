
package www.stormygames.Objeto;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import www.stormygames.GameConfig;
import www.stormygames.utils.AccessType;
import www.stormygames.utils.Draws;
import www.stormygames.utils.log;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class SpriteSheet extends Access{

    private final int textureId;
    public final int width;
    public final int height;
    public final int col, row, limitFrame;
    public final float spriteWidth, spriteHeight;

    public float scaleX = 1;
    public float scaleY = 1;

    private final List<SpriteIndex> listSpriteIndex = new ArrayList<>();
    private SpriteIndex spriteIndex = null;
    private String spriteIndexSelect = "";


    public SpriteSheet(String path, int num_col, int num_row, boolean isSpriteSheet, float spriteWidth, float spriteHeight, int limitFrame, boolean isTransparent) {
        AccessIndex = AccessType.Sprite;

        try (MemoryStack stack = MemoryStack.stackPush()) {

            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = STBImage.stbi_load(path, w, h, channels, 4);

            if (image == null) throw new RuntimeException("No se pudo cargar: " + path + " - " + STBImage.stbi_failure_reason());


            width = w.get(0);       // ancho de la archivo
            height = h.get(0);      // largo de la archivo

            if (!isSpriteSheet) {
                this.limitFrame = 1;
                this.spriteWidth = width;
                this.spriteHeight = height;
                this.col = 1;
                this.row = 1;
            } else {
                if (num_col <= 0 || num_row <= 0) {
                    throw new IllegalArgumentException("Filas o columnas inválidas");
                }

                this.col = num_col;
                this.row = num_row;
                this.limitFrame = Math.max(1, Math.min(limitFrame, col * row));

                this.spriteWidth    = spriteWidth > 0 ? spriteWidth : (float) width / col;
                this.spriteHeight   = spriteHeight > 0 ? spriteHeight : (float) height / row;

                if (col * this.spriteWidth > width || row * this.spriteHeight > height) {
                    throw new IllegalArgumentException( "La cuadrícula supera las dimensiones de la textura" );
                }

            }

            textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            if (!GameConfig.PIXEL_INTERPOLATION) {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            }else {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            }
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            if (isTransparent){
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            }
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA8,
                    width, height, 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, image
            );

            STBImage.stbi_image_free(image);
            glBindTexture(GL_TEXTURE_2D, 0);
            log.LOG_INFO(AccessIndex, textureId,"Inst: " + path);
        }
    }


    public void render(float x, float y, int frame) {

        if (listSpriteIndex.isEmpty()) {
            // Mantener el frame dentro del rango permitido
            frame = frame % limitFrame;
        } else {
            int countFrame = spriteIndex.indexFinal - spriteIndex.indexInit;
            frame = spriteIndex.indexInit + (frame % countFrame);
        }

        // Calcular fila y columna del frame
        int currentCol = frame % col;
        int currentRow = frame / col;

        // Posición del sprite dentro de la textura en píxeles
        float px = currentCol * spriteWidth;
        float py = currentRow * spriteHeight;

        // Coordenadas UV según dimensiones reales de la textura
        float u0 = px / width;
        float u1 = (px + spriteWidth) / width;

        float v0 = 1.0f - (py / height);
        float v1 = 1.0f - ((py + spriteHeight) / height);

        // Dimensiones finales aplicando escala
        float w = spriteWidth * Math.abs(scaleX);
        float h = spriteHeight * Math.abs(scaleY);

        // Ajustar coordenadas de textura para invertir el sprite
        float texU0 = scaleX < 0 ? u1 : u0;
        float texU1 = scaleX < 0 ? u0 : u1;

        float texV0 = scaleY < 0 ? v1 : v0;
        float texV1 = scaleY < 0 ? v0 : v1;

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);

        glColor4f(1f, 1f, 1f, 1f);

        glBegin(GL_QUADS);

            glTexCoord2f(texU0, texV0);
            glVertex2f(x, y);

            glTexCoord2f(texU1, texV0);
            glVertex2f(x + w, y);

            glTexCoord2f(texU1, texV1);
            glVertex2f(x + w, y + h);

            glTexCoord2f(texU0, texV1);
            glVertex2f(x, y + h);

        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }

    public void setSpriteSheet(String name, int frameInit, int frameFinal){
        int newId = listSpriteIndex.size();

        int newFrameInit = Math.max(0, Math.min(limitFrame, frameInit));                // el minimo 0 y el limite
        int newFrameFrame = Math.max(frameInit, Math.min(limitFrame, frameFinal));      // el minimo del inicio y el limite

        listSpriteIndex.add( new SpriteIndex (newId, name, newFrameInit, newFrameFrame) );
        useSpriteSheet(listSpriteIndex.getFirst().name);
    }
    public void useSpriteSheet(String name){

        if (listSpriteIndex.isEmpty())
            return;

        if (name.equals(spriteIndexSelect))
            return;
        else
            spriteIndexSelect = name;


        for (SpriteIndex inx : listSpriteIndex){
            if (inx.name.equals(spriteIndexSelect)){
                spriteIndex = inx;
                return;
            }
        }

        spriteIndex = listSpriteIndex.getFirst();
    }

    public void dispose() {
        glDeleteTextures(textureId);
    }

    private class SpriteIndex{
        public final int id;
        public final String name;
        public final int indexInit;
        public final int indexFinal;

        public SpriteIndex(int id, String name, int indexInit, int indexFinal) {
            this.id = id;
            this.name = name;
            this.indexInit = indexInit;
            this.indexFinal = indexFinal;
        }
    }
}

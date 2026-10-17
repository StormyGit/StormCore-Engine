
package www.stormygames.Objeto;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import www.stormygames.GameConfig;
import www.stormygames.utils.AccessType;
import www.stormygames.utils.log;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class TileMap extends Access{

    private final int textureId;

    private final int textureWidth;
    private final int textureHeight;

    private final int tileWidth;
    private final int tileHeight;

    private final int columns;
    private final int rows;

    private int[][] tiles;

    public float x = 0;
    public float y = 0;

    public float scaleX = 1;
    public float scaleY = 1;

    private boolean interpolation = false;
    private boolean disposed = false;

    public TileMap(String path, int tileWidth, int tileHeight) {
        AccessIndex = AccessType.TileMap;
        if (tileWidth <= 0 || tileHeight <= 0) {
            throw new IllegalArgumentException(
                    "Dimensiones de tile inválidas"
            );
        }

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        ByteBuffer image;

        try (MemoryStack stack = MemoryStack.stackPush()) {

            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);

            image = STBImage.stbi_load(path, w, h, channels, 4);

            if (image == null) {
                throw new RuntimeException(
                        "No se pudo cargar tileset: " + path
                );
            }

            textureWidth = w.get(0);
            textureHeight = h.get(0);
        }

        columns = textureWidth / tileWidth;
        rows = textureHeight / tileHeight;

        if (columns == 0 || rows == 0) {
            STBImage.stbi_image_free(image);
            throw new IllegalArgumentException(
                    "El tileset es más pequeño que un tile"
            );
        }

        textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);

        interpolation = GameConfig.PIXEL_INTERPOLATION;
        applyFilter();

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA8,
                textureWidth,
                textureHeight,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                image
        );
        STBImage.stbi_image_free(image);
        glBindTexture(GL_TEXTURE_2D, 0);
        log.LOG_INFO(AccessIndex, textureId,"Inst: " + path);
    }

    private void applyFilter() {
        int filter = interpolation ? GL_LINEAR : GL_NEAREST;

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_MIN_FILTER, filter);

        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_MAG_FILTER, filter);
    }

    // Establecer el mapa de tiles
    public void setTiles(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Mapa nulo");
        }

        int width = tiles.length == 0 ? 0 : tiles[0].length;

        for (int[] row : tiles) {
            if (row == null || row.length != width) {
                throw new IllegalArgumentException(
                        "Todas las filas deben tener el mismo tamaño"
                );
            }
        }

        this.tiles = tiles;
    }

    // Obtener tile
    public int getTile(int row, int col) {
        return tiles[row][col];
    }

    // Modificar tile
    public void setTile(int row, int col, int id) {
        tiles[row][col] = id;
    }

    // Dibujar el mapa completo
    public void render() {

        if (disposed || tiles == null) return;

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Actualizar filtro solo si cambió
        if (interpolation != GameConfig.PIXEL_INTERPOLATION) {
            interpolation = GameConfig.PIXEL_INTERPOLATION;
            applyFilter();
        }

        glColor4f(1f, 1f, 1f, 1f);

        float w = tileWidth * Math.abs(scaleX);
        float h = tileHeight * Math.abs(scaleY);

        glBegin(GL_QUADS);

        for (int row = 0; row < tiles.length; row++) {

            for (int col = 0; col < tiles[row].length; col++) {

                int id = tiles[row][col];

                // -1 representa una casilla vacía
                if (id < 1 || id >= columns * rows) {
                    continue;
                }

                int tileCol = id % columns;
                int tileRow = id / columns;

                float px = tileCol * tileWidth;
                float py = tileRow * tileHeight;

                // UV de la textura completa
                float u0 = px / textureWidth;
                float u1 = (px + tileWidth) / textureWidth;

                float v0 = 1f - (py / textureHeight);
                float v1 = 1f - ((py + tileHeight) / textureHeight);

                // Inversión horizontal y vertical
                if (scaleX < 0) {
                    float tmp = u0;
                    u0 = u1;
                    u1 = tmp;
                }

                if (scaleY < 0) {
                    float tmp = v0;
                    v0 = v1;
                    v1 = tmp;
                }

                float dx = x + col * w;
                float dy = y + row * h;

                glTexCoord2f(u0, v0);
                glVertex2f(dx, dy);

                glTexCoord2f(u1, v0);
                glVertex2f(dx + w, dy);

                glTexCoord2f(u1, v1);
                glVertex2f(dx + w, dy + h);

                glTexCoord2f(u0, v1);
                glVertex2f(dx, dy + h);
            }
        }

        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }

    public void dispose() {

        if (disposed) return;

        glDeleteTextures(textureId);
        disposed = true;
    }


    private final Set<Integer> solidTiles = new HashSet<>();

    // Registrar qué IDs tienen colisión
    public void addSolidTile(int tileId) {
        solidTiles.add(tileId);
    }

    // Quitar la colisión de un tipo de tile
    public void removeSolidTile(int tileId) {
        solidTiles.remove(tileId);
    }

    // Comprobar si un ID es sólido
    public boolean isSolid(int tileId) {
        return solidTiles.contains(tileId);
    }


    // Devuelve el desplazamiento horizontal permitido
    public float resolveX(
            float actorX, float actorY,
            float actorW, float actorH,
            float dx) {

        if (tiles == null || dx == 0) return dx;

        float w = tileWidth * Math.abs(scaleX);
        float h = tileHeight * Math.abs(scaleY);

        if (w <= 0 || h <= 0) return dx;

        float left = actorX - actorW / 2f;
        float right = actorX + actorW / 2f;
        float top = actorY - actorH / 2f;
        float bottom = actorY + actorH / 2f;

        float allowed = dx;

        // Solo filas que intersectan verticalmente
        int rowStart = Math.max(0,
                (int) Math.floor((top - y) / h));

        int rowEnd = Math.min(tiles.length - 1,
                (int) Math.ceil((bottom - y) / h) - 1);

        for (int row = rowStart; row <= rowEnd; row++) {

            for (int col = 0; col < tiles[row].length; col++) {

                if (!isSolid(tiles[row][col])) continue;

                float tileLeft = x + col * w;
                float tileRight = tileLeft + w;

                if (dx > 0) {

                    if (right <= tileLeft &&
                            right + allowed >= tileLeft) {

                        allowed = Math.min(
                                allowed, tileLeft - right
                        );
                    }

                } else {

                    if (left >= tileRight &&
                            left + allowed <= tileRight) {

                        allowed = Math.max(
                                allowed, tileRight - left
                        );
                    }
                }
            }
        }

        return allowed;
    }

    // Devuelve el desplazamiento vertical permitido
    public float resolveY(
            float actorX, float actorY,
            float actorW, float actorH,
            float dy) {

        if (tiles == null || dy == 0) return dy;

        float w = tileWidth * Math.abs(scaleX);
        float h = tileHeight * Math.abs(scaleY);

        if (w <= 0 || h <= 0) return dy;

        float left = actorX - actorW / 2f;
        float right = actorX + actorW / 2f;
        float top = actorY - actorH / 2f;
        float bottom = actorY + actorH / 2f;

        float allowed = dy;

        for (int row = 0; row < tiles.length; row++) {

            float tileTop = y + row * h;
            float tileBottom = tileTop + h;

            for (int col = 0; col < tiles[row].length; col++) {

                if (!isSolid(tiles[row][col])) continue;

                float tileLeft = x + col * w;
                float tileRight = tileLeft + w;

                // Debe coincidir horizontalmente
                if (right <= tileLeft || left >= tileRight) {
                    continue;
                }

                if (dy > 0) {

                    if (bottom <= tileTop &&
                            bottom + allowed >= tileTop) {

                        allowed = Math.min(
                                allowed, tileTop - bottom
                        );
                    }

                } else {

                    if (top >= tileBottom &&
                            top + allowed <= tileBottom) {

                        allowed = Math.max(
                                allowed, tileBottom - top
                        );
                    }
                }
            }
        }

        return allowed;
    }


}

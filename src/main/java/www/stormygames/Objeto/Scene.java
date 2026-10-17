package www.stormygames.Objeto;

import www.stormygames.GameData;
import www.stormygames.access.actors.EnemySimple;
import www.stormygames.access.actors.Player;
import www.stormygames.access.actors.Solid;
import www.stormygames.utils.AccessType;
import www.stormygames.utils.ActorTipo;
import www.stormygames.utils.Draws;
import www.stormygames.utils.log;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene extends Access {
    private int id, width, height, cell_width, cell_height;
    private String Name;

    private List<IActor> listActors = new ArrayList<>();
    private final List<Actor> solidActors = new ArrayList<>();
    private boolean isStart_Actors = true;
    private boolean isStart_Scene = true;
    private TileMap tileMap;
    public Scene(int id, int width, int height, int cell_width, int cell_height, String name) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.cell_width = cell_width;
        this.cell_height = cell_height;
        this.Name = name;
        this.AccessIndex = AccessType.Scene;

        log.LOG_INFO(this.AccessIndex,this.id,"Inis Scena");
    }

    public  void run(double deltaTime){

        if (isStart_Scene){
            isStart_Scene = false;
            start();
        }
        update(deltaTime);
        render();

        for (IActor act : listActors ){
            if (isStart_Actors){
                isStart_Actors = false;
                act.start();
            }

            act.update(deltaTime);

            act.render();

        }

        //render_dev();
    }

    private void start(){
        log.LOG_INFO("Cargar TileMap");
        tileMap = new TileMap("Access/tiles.png",16,16);
        tileMap.setTiles(tilesData());
        tileMap.addSolidTile(1);
        tileMap.addSolidTile(2);
        tileMap.addSolidTile(3);
        // tubos
        tileMap.addSolidTile(4);
        tileMap.addSolidTile(5);
        tileMap.addSolidTile(6);
        tileMap.addSolidTile(7);
        tileMap.addSolidTile(8);
        tileMap.addSolidTile(9);
        log.LOG_INFO(this.AccessIndex,this.id,"instancias Actores");
        addActor(new Player(100, 0));
        addActor(new EnemySimple(300, 168));

        GameData.getCamaraCurrent().target((Actor) listActors.getFirst());
    }

    private void update(double deltaTime){

    }


    public void addActor(IActor act) {
        listActors.add(act);
        if (act instanceof Actor actor) {
            if (actor.getActorType() == ActorTipo.Solid) {
                solidActors.add(actor);
            }
        }
    }

    public void render(){
        // Dibujar mapa
        if (tileMap != null) {

            tileMap.render();
        }
    }

    public int[][] tilesData(){
        int[][] tiles = {
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 1},

                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,3,0,0,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,3,0,0,0,2,3,2,3,2,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0, 6,7, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 0,0, 0,0,0,0,0,0,0,0, 6,7, 0,0,0,0,0,0, 8,9, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 6,7, 0,0,0,0,0,0,0,0, 8,9, 0,0,0,0,0,0, 8,9, 0,0,0,0,0,0, 1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ,0,0,0,0,0,0,0,0,0,0,0,0, 8,9, 0,0,0,0,0,0,0,0, 8,9, 0,0,0,0,0,0, 8,9, 0,0,0,0,0,0, 1},

                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 ,1,1,1,1,1,1,1,1,1,1,1,1, 1,1, 1,1,1,1,1,1,1,1, 1,1, 1,1,1,1,1,1, 1,1, 1,1,1,1,1,1, 1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 ,1,1,1,1,1,1,1,1,1,1,1,1, 1,1, 1,1,1,1,1,1,1,1, 1,1, 1,1,1,1,1,1, 1,1, 1,1,1,1,1,1, 1}
        };

        return tiles;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public List<IActor> getListActors() {
        return listActors;
    }

    public List<Actor> getSolidActors() {
        return solidActors;
    }

    public void render_dev() {

        if (cell_width <= 0 || cell_height <= 0) return;

        Draws.colorReset();
        Draws.rectangle(0, 0, width, height);

        Draws.alpha(0.1f);
        for (int x = cell_width; x < width; x += cell_width) {
            Draws.line(x, 0, x, height);
        }
        for (int y = cell_height; y < height; y += cell_height) {
            Draws.line(0, y, width, y);
        }
        Draws.alpha(1);
    }
}

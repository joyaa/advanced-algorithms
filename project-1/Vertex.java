public class Vertex{
    int id;
    double x;
    double y;
    boolean active = true;
    Vertex(int id, double x, double y){
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean bool) {
        active = bool;
    }
    public int getID() {
        return id;
    }

}
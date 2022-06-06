public class Node {
    private Node prev;
    private Point point;
    private int g;
    private int h;
    public Node(Node prev, Point point, int h){
        this.prev = prev;
        g = (this.prev != null) ? this.prev.getG() + 1 : 0;
        this.point = point;
        this.h = h;
    }
    public Node getPrev(){
        return prev;
    }
    public Point getPoint(){
        return point;
    }
    public int getG(){
        return g;
    }
    public int getH(){
        return h;
    }
    public int getF(){
        return g + h;
    }
}

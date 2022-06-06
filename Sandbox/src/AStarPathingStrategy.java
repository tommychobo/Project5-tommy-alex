import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Node cur = new Node(null, start, manhattanDist(start, end));
        PriorityQueue<Node> openList = new PriorityQueue<>(255, Comparator.comparingInt(Node::getF));
        HashSet<Point> closedList = new HashSet<>();
        openList.add(cur);
        while(!openList.isEmpty() && !withinReach.test(cur.getPoint(), end)){
            cur = openList.remove();
            closedList.add(cur.getPoint());
            //System.out.println(cur.getPoint().toString());
            Object[] pts = potentialNeighbors.apply(cur.getPoint()).filter(canPassThrough)
                    .filter(Predicate.not(closedList::contains)).toArray();
            for(Object o : pts){
                if(o instanceof Point){
                    openList.add(new Node(cur, ((Point)o), manhattanDist(((Point)o), end)));
                }
            }
            if(openList.isEmpty())
                return new ArrayList<>();
        }
        List<Point> path = new ArrayList<>();
        while(cur != null) {
            path.add(cur.getPoint());
            cur = cur.getPrev();
        }
        return path;
    }

    public int manhattanDist(Point p1, Point p2){
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
}

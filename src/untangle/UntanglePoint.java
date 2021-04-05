//
// UntanglePoint.java
// Implements a point in the untangle game which includes
//     Original point in grid
//     Current point on screen
//     all points it connects to
//
package untangle;

import java.util.List;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

// class for an untangle point
public class UntanglePoint implements Iterable<UntanglePoint>, Serializable {

    // Current and original locations
    private final Point pt = new Point();
    private final Point original_pt = new Point();

    // List of points we connect to
    private final List<UntanglePoint> connections;

    // scaling factor
    public static final int DISPLAY_FACTOR = 1000;

    // Data relating to rubberband move/resize
    // These are not saved
    transient private Point rubberbandBoxStart;
    transient private Point rubberbandBoxEnd;
    transient private Point rubberbandBoxpt;

    // Constructor
    public UntanglePoint(int x, int y) {

        pt.x = x * DISPLAY_FACTOR;
        pt.y = y * DISPLAY_FACTOR;
        original_pt.x = x;
        original_pt.y = y;

        connections = new ArrayList<>();
    }

    // Start the rubber band drag
    public void StartRubberBand(Point Start, Point End) {
        rubberbandBoxpt = (Point) pt.clone();
        rubberbandBoxStart = (Point) Start.clone();
        rubberbandBoxEnd = (Point) End.clone();
    }

    // Recalculate the position when the area is changes/resized
    public void ProcessRubberBand(Point Start, Point End) {
        if (Math.abs(Start.x - End.x) < 6 || Math.abs(Start.y - End.y) < 6) {
            return;
        }
        
        double xp = (double)(rubberbandBoxpt.x - rubberbandBoxStart.x)/
                (double)(rubberbandBoxEnd.x - rubberbandBoxStart.x);
        double yp = (double)(rubberbandBoxpt.y - rubberbandBoxStart.y)/
                (double)(rubberbandBoxEnd.y - rubberbandBoxStart.y);
        
        pt.x = (int)(Start.x + (End.x - Start.x)*xp);
        pt.y = (int)(Start.y + (End.y - Start.y)*yp);
    }

    // Tidy up when rubberband moce finished
    public void ClearRubberBand() {
        rubberbandBoxpt = null;
        rubberbandBoxStart = null;
        rubberbandBoxEnd = null;
    }

    // Move the point
    public void MoveTo(int x, int y) {
        pt.x = x;
        pt.y = y;
    }

    // Get the display location of a point
    public Point GetDisplayPoint(boolean solution) {

        return (solution) ? original_pt : pt;
    }

    public Point GetPoint() {
        return original_pt;
    }

    // coonect this point to another
    public void AddPoint(UntanglePoint add_pt) {

        // Make sure we aren't duplicating points
        if (connections.contains(add_pt)) {
            return;
        }
        connections.add(add_pt);
    }

    public boolean ConnectedTo(UntanglePoint pt) {
        return connections.contains(pt);
    }

    // Remove a point
    public void RemovePoint(UntanglePoint rmv_pt) {

        connections.remove(rmv_pt);
    }

    // Get the number of points we are connected to
    public int Connections() {
        return (connections.size());
    }

    // get distance between to points (squared)
    //  dirction / sign doesn't matter
    public int Distance2(UntanglePoint comp) {
        int x_dist = (original_pt.x - comp.GetPoint().x);
        int y_dist = (original_pt.y - comp.GetPoint().y);

        return (x_dist * x_dist + y_dist * y_dist);
    }

    // Allow caller to iterate through connected points
    @Override
    public Iterator<UntanglePoint> iterator() {
        return (connections.iterator());
    }
}

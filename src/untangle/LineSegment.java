//
// LineSegment.java
// Implements the line segment, which includes the code for overlap with
// another line segment.
//
package untangle;

import java.awt.Point;

// Line Segment implementation
public class LineSegment {

    public Point end1;
    public Point end2;
    public int crosses = 0;

    // Constructor
    LineSegment(Point p1, Point p2) {
        end1 = p1;
        end2 = p2;
    }

    // Determine  if a point is on a Line segment
    boolean onLine(LineSegment l1, Point p) {
        //check whether p is on the line or not
        return p.x <= Math.max(l1.end1.x, l1.end2.x) && p.x <= Math.min(l1.end1.x, l1.end2.x)
              && (p.y <= Math.max(l1.end1.y, l1.end2.y) && p.y <= Math.min(l1.end1.y, l1.end2.y));
    }

    // Get the direction of an angle
    int direction(Point a, Point b, Point c) {
        int val = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
        if (val == 0) {
            return 0;     //colinear
        } else if (val < 0) {
            return 2;    //anti-clockwise direction
        }
        return 1;    //clockwise direction
    }

    // Determine if the rectangles defined by 2 line segments intersect
    public boolean rectanglesIntersect(LineSegment l) {
        int maxAx = Math.max(end1.x, end2.x);
        int minAx = Math.min(end1.x, end2.x);
        int maxBx = Math.max(l.end1.x, l.end2.x);
        int minBx = Math.min(l.end1.x, l.end2.x);
        int maxAy = Math.max(end1.y, end2.y);
        int minAy = Math.min(end1.y, end2.y);
        int maxBy = Math.max(l.end1.y, l.end2.y);
        int minBy = Math.min(l.end1.y, l.end2.y);

        boolean aLeftOfB = maxAx <= minBx;
        boolean aRightOfB = minAx >= maxBx;
        boolean aAboveB = minAy >= maxBy;
        boolean aBelowB = maxAy <= minBy;

        return !(aLeftOfB || aRightOfB || aAboveB || aBelowB);
    }

    // Determine if this lines intersects another
    public boolean intersect(LineSegment l) {

        if (HasCommonEndPoint(l)) {
            return false;
        }
        if (!rectanglesIntersect(l)) {
            return false;
        }

        //four direction for two lines and points of other line
        int dir1 = direction(end1, end2, l.end1);
        int dir2 = direction(end1, end2, l.end2);
        int dir3 = direction(l.end1, l.end2, end1);
        int dir4 = direction(l.end1, l.end2, end2);

        if (dir1 != dir2 && dir3 != dir4) {
            return true; //they are intersecting
        }
        if (dir1 == 0 && onLine(this, l.end1)) //when p2 of line2 are on the line1
        {
            return true;
        }

        if (dir2 == 0 && onLine(this, l.end2)) //when p1 of line2 are on the line1
        {
            return true;
        }

        if (dir3 == 0 && onLine(l, end1)) //when p2 of line1 are on the line2
        {
            return true;
        }

        if (dir4 == 0 && onLine(l, end2)) //when p1 of line1 are on the line2
        {
            return true;
        }

        return false;
    }

    // To string (useful for debugging)
    @Override
    public String toString() {
        String s = "Line (" + Integer.toString(end1.x) + ", " + Integer.toString(end1.y) + ")"
                + " (" + Integer.toString(end2.x) + ", " + Integer.toString(end2.y) + ")";
        return s;
    }

    // Determine if 2 lines have a common endpoint
    public boolean HasCommonEndPoint(LineSegment l) {
        return ((end1.x == l.end1.x && end1.y == l.end1.y)
                || (end2.x == l.end1.x && end2.y == l.end1.y)
                || (end1.x == l.end2.x && end1.y == l.end2.y)
                || (end2.x == l.end2.x && end2.y == l.end2.y));
    }

}

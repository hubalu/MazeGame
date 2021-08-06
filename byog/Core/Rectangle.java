package byog.Core;

import java.io.Serializable;
import java.util.Set;

public class Rectangle implements Serializable {
    public static String RECTANGLE = "RECTANGLE";
    public static String DOT = "DOT";
    public static String VERTICALLINE = "VERTICALLINE";
    public static String HORIZONTALLINE = "HORIZONTALLINE";
    public Position leftBottom;
    public Position rightTop;
    public int height;
    public int width;
    public String type;
    public Rectangle(Position lb,Position rt){
        leftBottom=lb;
        rightTop=rt;
        height=rightTop.getY()-leftBottom.getY()+1;
        width=rightTop.getX()-leftBottom.getX()+1;

        if (leftBottom.getX() > rightTop.getX()) {
            throw new RuntimeException("the leftBottom x should not be greater than rightTop x");
        } else if (leftBottom.getY() > rightTop.getY()) {
            throw new RuntimeException("the leftBottom y should not be greater than rightTop y");
        }
        if (isVerticalLine()) {
            type = Rectangle.VERTICALLINE;
        } else if (isHorizontalLine()) {
            type = Rectangle.HORIZONTALLINE;
        } else if (isDot()) {
            type = Rectangle.DOT;
        } else {
            type = Rectangle.RECTANGLE;
        }
    }
    //+2 to ensuring there is at least one space gap between rectangles
    public boolean checkOverlap(Rectangle other){
        return !((this.leftBottom.getX()>other.rightTop.getX()+2)
                ||(this.rightTop.getX()+2<other.leftBottom.getX())
                ||(this.leftBottom.getY()>other.rightTop.getY()+2)
                ||(this.rightTop.getY()+2<other.leftBottom.getY()));
    }
    public boolean isVerticalLine() {
        return leftBottom.getX() == rightTop.getX() && leftBottom.getY() != rightTop.getY();
    }
    public boolean isHorizontalLine() {
        return leftBottom.getY() == rightTop.getY() && leftBottom.getX() != rightTop.getX();
    }
    public boolean isDot() {
        return leftBottom.getX() == rightTop.getX() && leftBottom.getY() == rightTop.getY();
    }
}

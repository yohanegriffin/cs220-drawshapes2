package knox.drawshapes;

import java.awt.Color;
import java.awt.Point;

public abstract class AbstractShape implements IShape {
	protected Point anchorPoint;
	protected Color color;
	protected boolean selected;
	protected BoundingBox boundingBox;
	
	public AbstractShape(Point anchorPoint, Color color) {
		this.anchorPoint = anchorPoint;
		this.color = color;
	}
	
	@Override
    public void setAnchorPoint(Point p) {
        // TODO: move bounding box
        this.anchorPoint = p;
    }
    @Override
    public Color getColor() {
        return color;
    }
    @Override
    public void setColor(Color color) {
        this.color = color;
    }
    @Override
    public boolean isSelected() {
        return selected;
    }
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public Point getAnchorPoint() {
        return anchorPoint;
    }
    @Override
    public boolean intersects(IShape other) {
    	AbstractShape s = (AbstractShape)other;
        return boundingBox.intersects(s.getBoundingBox());
    }

    @Override
    public boolean contains(Point point) {
        return boundingBox.contains(point);
    }

	@Override
	public void move(int dx, int dy) {
		this.setAnchorPoint(new Point(this.anchorPoint.x + dx, this.anchorPoint.y + dy));
		this.boundingBox.move(dx, dy);
	}
	
	 /**
     * Returns the bounding box of this shape. The bounding box
     * is the max/min X and Y coordinates.
     * @return
     */
    BoundingBox getBoundingBox() {
    	return this.boundingBox;
    }

}

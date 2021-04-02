package knox.drawshapes;

public interface IMoveableShape
{
	/**
     * Move the anchor point of the shape by the given dx and dy values
     * @param dx
     * @param dy
     */
    public void move(int dx, int dy);
    
    /**
     * Scale the size of the shape by the given factor
     * @param d
     */
    public void scale(double d);
}

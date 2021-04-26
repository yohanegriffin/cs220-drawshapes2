package knox.drawshapes;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;


public class PolygonOject {
	Polygon p;
	Color c;
	
	public void PolygonObject(int[] x, int[] y, Color c) {
		p = new Polygon();
		p.xpoints = x;
		p.ypoints = y;
		p.npoints = x.length;
		this.c = c;
	}
	
	void drawPolygon(Graphics g) {
		g.setColor(c);
		g.drawPolygon(p);
	}
}

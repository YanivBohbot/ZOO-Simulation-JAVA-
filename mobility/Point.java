package mobility;

/**
 * @author baroh
 *
 */
public class Point {

	private int x; // the x value
	private int y; // the y value
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean setX(int x) {
		this.x = x;
		return true;
	}
	public boolean setY(int y) {
		this.y = y;
		return true;
	}
}

package mobility;

/**
 * @author baroh
 *
 */
public abstract class Mobile implements ILocatable {
	protected Point location;

	public Mobile(Point location) {
		this.setLocation(location);
	}
	public Point getLocation() {
		return location;
	}

	public boolean setLocation(Point newLocation) {
		this.location = newLocation;
		return true;

	}
}

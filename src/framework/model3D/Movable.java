package framework.model3D;

import framework.model3D.Placeable;
import framework.physics.Ground;

public interface Movable extends Placeable {
	public void motion(long interval, Ground ground);
}

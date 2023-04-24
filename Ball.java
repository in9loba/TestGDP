package breakout;

import java.util.stream.IntStream;
import java.awt.Color;

/**
 * Each instance of this class represents a ball in the breakout game.
 * 
 * @invar A ball's diameter is bigger than zero.
 * 	| getDiameter() > 0
 * @invar A ball's velocity is not equal to the zero vector.
 * 	| !(getVelocity().equals(new Vector(0,0)))
 * @invar | getCenter() != null
 * @invar | getVelocity() != null
 */
public abstract class Ball {
	/**
	 * @invar | center != null
	 * @invar | diameter > 0
	 * @invar | velocity != null
	 * @invar | !(velocity.equals(new Vector(0,0)))
	 */
	protected Point center;
	protected int diameter;
	protected Vector velocity;
	
	// The additional speeds of ball replicates produced by a replicator paddle
	public static final Vector[] replicateBallsSpeedDiff = {new Vector(2,-2), new Vector(-2,2), new Vector(2,2)};
	// The maximum lifetime of a supercharged ball in ms
	public static final int MAX_LIFETIME = 10000;
	
	/**
	 * Returns the center Point object of the Ball object.
	 */
	public abstract Point getCenter();
	
	/**
	 * Returns the diameter of the Ball object.
	 */
	public abstract int getDiameter();
	
	/**
	 * Returns the velocity Vector object of the Ball object.
	 */
	public abstract Vector getVelocity();
	
	/**
	 * Sets the center Point object of this Ball object to the supplied Point object.
	 * @mutates | this
	 * @pre | center != null
	 * @post | getCenter().equals(center)
	 */
	public abstract void changeCenter(Point center);
	
	/**
	 * Sets the velocity Vector object of this Ball object to the supplied Vector object.
	 * @mutates | this
	 * @pre | velocity != null
	 * @pre | !(velocity.equals(new Vector(0,0)))
	 * @post | getVelocity().equals(velocity)
	 */
	public abstract void changeVelocity(Vector velocity);
	
	/**
	 * Changes the motion of the ball depending on whether the block it hit, was destroyed.
	 * -> Different behaviour depending on the ball type.
	 * @mutates | this
	 * @pre | rect != null
	 */
	public abstract void hitBlock(Rect rect, boolean destroyed);
	
	/**
	 * Changes the state of this Ball object so that it reflects a ball that has rolled for a non-zero amount
	 *  of time into its current direction and speed, as reflected in its velocity Vector.
	 * 
	 * Assuming that speed is expressed in units of 1/ms or 1000/s, displacements can be quickly computed by scaling
	 * the velocity vector by the elapsed time in ms.
	 * 
	 * @mutates | this
	 * @pre | elapsedTime != 0
	 * @post | getCenter().equals(old(getCenter().plus(getVelocity().scaled(elapsedTime))))
	 */
	public abstract void roll(int elapsedTime);
	
	/**
	 * Changes the state of this Ball object so that is reflects a ball that has bounced
	 * on a surface represented by the supplied normal unit vector object
	 * @mutates | this
	 * @pre | direction != null && direction.getSquareLength() == 1
	 * @pre | !(direction.equals(new Vector(0,0)))
	 * @post | getVelocity().equals(old(getVelocity()).mirrorOver(direction))
	 */
	public abstract void bounce(Vector direction);
	
	/**
	 * Returns a Rectangle object that represents the rectangle surrounding the ball represented by
	 * this Ball object.
	 * @creates | result
	 * @inspects | this
	 * @post | result.getTopLeft().equals(getCenter().minus(new Vector(getDiameter()/2,getDiameter()/2)))
	 * @post | result.getBottomRight().equals(getCenter().plus(new Vector(getDiameter()/2,getDiameter()/2)))
	 */
	public abstract Rect rectangleOf();
	
	/**
	 * Returns the default Color object used to display a ball in the breakout game
	 * -> Different colour depending on the ball type.
	 */
	public abstract Color getColor();
	
	/**
	 * Returns an array with a predefined number (1 up to 3) of replicate balls, differing only
	 * in velocity by the preset replication velocity differences 'replicateBallsSpeedDiff'.
	 * @creates | result
	 * @inspects | this
	 * @pre | reps >= 1 && reps <= 3
	 * @post | result.length == reps
	 * @post | IntStream.range(0,result.length).allMatch(i -> result[i].getVelocity().equals(old(getVelocity()).plus(replicateBallsSpeedDiff[i])))
	 */
	public abstract Ball[] replicate(int reps);
	
	/**
	 * Returns a copy of this Ball object representing a ball that has been powered up.
	 * -> Different behaviour depending on the ball type
	 * @creates | result
	 * @inspects | this
	 */
	public abstract SuperBall powerup();
		
	/**
	 * Ages the ball so that its lifetime decreases.
	 * -> Different behaviour depending on the ball type
	 * @pre | elapsedTime > 0
	 * @inspects | this
	 */
	public abstract Ball age(int elapsedTime);
}

/**
 * Each instance of this class represents a normal ball in the breakout game.
 */
class NormalBall extends Ball {
	/**
	 * Returns an object representing a normal ball in the breakout game, defined by a center Point object,
	 * a positive diameter and a non-zero velocity Vector object.
	 * @pre | velocity != null
	 * @pre | center != null
	 * @pre | diameter > 0
	 * @pre | !(velocity.equals(new Vector(0,0)))
	 * @post | getCenter().equals(center)
	 * @post | getDiameter() == diameter
	 * @post | getVelocity().equals(velocity)
	 */
	public NormalBall(Point center, int diameter, Vector velocity) {
		this.center=new Point(center.getX(), center.getY());
		this.diameter=diameter;
		this.velocity=new Vector(velocity.getX(), velocity.getY());
	}
	
	public Point getCenter() {
		return new Point(center.getX(), center.getY());
	}
	
	public int getDiameter() {
		return diameter;
	}
	
	public Vector getVelocity() {
		return new Vector(velocity.getX(), velocity.getY());
	}
	
	public void changeCenter(Point center) {
		this.center=new Point(center.getX(), center.getY());
	}
	
	public void changeVelocity(Vector velocity) {
		this.velocity=new Vector(velocity.getX(), velocity.getY());
	}
	
	public void roll(int elapsedTime) {
		center=center.plus(velocity.scaled(elapsedTime));
	}
	
	public void bounce(Vector direction) {
		velocity=velocity.mirrorOver(direction);
	}

	public Rect rectangleOf() {
		return new Rect(center.minus(new Vector(diameter/2, diameter/2)), center.plus(new Vector(diameter/2, diameter/2)));
	}
	
	/**
	 * Returns the default Color object used to display a normal ball in the breakout game, i.e. red.
	 */
	public Color getColor() {
		return Color.red;
	}
	
	/**
	 * Changes the motion of the ball depending on whether the block it hit, was destroyed.
	 * For normal balls, this means it should bounce anyway.
	 */
	public void hitBlock(Rect rect, boolean destroyed) {
		Vector direction = this.rectangleOf().overlap(rect);
		this.bounce(direction);
	}
	
	/**
	 * Returns a copy of this NormallBall object converted to a SuperBall object with the default lifetime
	 * @creates | result
	 * @inspects | this
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().equals(old(getVelocity()))
	 * @post | result.getLifetime() == MAX_LIFETIME
	 */
	public SuperBall convertToSuper() {
		return new SuperBall(center, diameter, velocity, MAX_LIFETIME);
	}
	
	public NormalBall[] replicate(int reps) {
		NormalBall[] replicated = new NormalBall[reps];
		for (int i=0; i<reps; i++) {
			replicated[i] = new NormalBall(center, diameter, velocity.plus(replicateBallsSpeedDiff[i]));
		}
		return replicated;
	}
	
	/**
	 * Returns a copy of this Ball object representing a ball that has been powered up.
	 * For normal balls, this converts this NormalBall object to a SuperBall with maximum lifetime.
	 * @creates | result
	 * @inspects | this
	 * @post | result instanceof SuperBall
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getVelocity().equals(old(getVelocity()))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getLifetime() == MAX_LIFETIME
	 */
	public SuperBall powerup() {
		return this.convertToSuper();
	}
	
	/**
	 * Ages the ball so that its lifetime decreases. Lifetime is not applicable to a normal ball,
	 * so the current normal ball state is returned.
	 */
	public Ball age(int elapsedTime) {
		return this;
	}
}

/**
 * Each instance of this class represents a supercharged ball in the breakout game.
 * 
 * @invar A supercharged ball's lifetime is between 0 and the preset lifetime of 10000 ms.
 * 	| getLifetime() > 0 && getLifetime() <= MAX_LIFETIME
 */
class SuperBall extends Ball {
	/**
	 * @invar | lifetime > 0 && lifetime <= MAX_LIFETIME
	 */
	private long lifetime;
	
	/**
	 * Returns an object representing a supercharged ball in the breakout game, defined by a center Point object,
	 * a positive diameter, a non-zero velocity Vector object and a positive lifetime smaller than or equal to the preset.
	 * @pre | velocity != null
	 * @pre | center != null
	 * @pre | diameter > 0
	 * @pre | !(velocity.equals(new Vector(0,0)))
	 * @pre | lifetime > 0 && lifetime <= MAX_LIFETIME
	 * @post | getCenter().equals(center)
	 * @post | getDiameter() == diameter
	 * @post | getVelocity().equals(velocity)
	 * @post | getLifetime() == lifetime
	 */
	public SuperBall(Point center, int diameter, Vector velocity, long lifetime) {
		this.center=new Point(center.getX(), center.getY());
		this.diameter=diameter;
		this.velocity=new Vector(velocity.getX(), velocity.getY());
		this.lifetime=lifetime;
	}
	
	public Point getCenter() {
		return new Point(center.getX(), center.getY());
	}

	public int getDiameter() {
		return diameter;
	}
	
	public Vector getVelocity() {
		return new Vector(velocity.getX(), velocity.getY());
	}
	
	/**
	 * Returns the remaining lifetime in milliseconds of this SuperBall object.
	 */
	public long getLifetime() {
		return lifetime;
	}
	
	public void changeCenter(Point center) {
		this.center= new Point(center.getX(), center.getY());
	}
	
	public void changeVelocity(Vector velocity) {
		this.velocity= new Vector(velocity.getX(), velocity.getY());
	}
	
	/**
	 * Sets the remaining lifetime in milliseconds of this SuperBall object to the supplied value.
	 * @mutates | this
	 * @pre | lifetime > 0 && lifetime <= MAX_LIFETIME
	 * @post | getLifetime() == lifetime
	 */
	public void changeLifetime(long lifetime) {
		this.lifetime = lifetime;
	}
	
	/**
	 * Resets the remaining lifetime of this SuperBall object to the default, LIFETIME.
	 * @mutates | this
	 * @post | getLifetime() == MAX_LIFETIME
	 */
	public void resetLifetime() {
		this.lifetime = MAX_LIFETIME;
	}
	
	public void roll(int elapsedTime) {
		center=center.plus(velocity.scaled(elapsedTime));
	}
	
	public void bounce(Vector direction) {
		velocity=velocity.mirrorOver(direction);
	}
	
	public Rect rectangleOf() {
		return new Rect(center.minus(new Vector(diameter/2, diameter/2)), center.plus(new Vector(diameter/2, diameter/2)));
	}
	
	/**
	 * Returns the default Color object used to display a supercharged ball in the breakout game, i.e. pink.
	 */
	public Color getColor() {
		return Color.pink;
	}
	
	/**
	 * Changes the motion of the ball depending on whether the block it hit, was destroyed.
	 * For supercharged balls, this means it bounces only on blocks that were not destroyed,
	 * i.e. sturdy blocks with a lifetime bigger than 1.
	 */
	public void hitBlock(Rect rect, boolean destroyed) {
		if (!(destroyed)) {
			Vector direction = this.rectangleOf().overlap(rect);
			this.bounce(direction);
		}
	}
	
	/**
	 * Ages the ball so that its lifetime decreases with a positive amount of time elapsed.
	 * @pre | elapsedTime >= 0
	 * @inspects | this
	 * @post | result instanceof NormalBall || ((SuperBall) result).getLifetime() == old(getLifetime())-elapsedTime
	 */
	public Ball age(int elapsedTime) {
		long newlifetime = lifetime - elapsedTime;
		if (newlifetime <= 0) {
			return this.convertToNormal();
		}
		lifetime = newlifetime;
		return this;
	}
	
	/**
	 * Returns a copy of this SuperBall object converted to a NormalBall object.
	 * @creates | result
	 * @inspects | this
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().equals(old(getVelocity()))
	 */
	public NormalBall convertToNormal() {
		return new NormalBall(center, diameter, velocity); 
	}
	
	public SuperBall[] replicate(int reps) {
		SuperBall[] replicated = new SuperBall[reps];
		for (int i=0; i<reps; i++) {
			replicated[i] = new SuperBall(center, diameter, velocity.plus(replicateBallsSpeedDiff[i]),lifetime);
		}
		return replicated;
	}
	
	/**
	 * Returns a copy of this SuperBall object representing a supercharged ball that has been powered up.
	 * Powering up a supercharged ball results in resetting its lifetime.
	 * @mutates | this
	 */
	public SuperBall powerup() {
		this.resetLifetime();
		return this;
	}
}
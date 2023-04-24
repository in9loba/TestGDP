package breakout;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BallTest {
	Point p11;
	Point p05;
	Point p38;
	Point pm14;
	
	Rect r1138;
	Rect rm1438;
	
	Vector v1010;
	Vector v55;
	
	Ball b1;
	Ball b1c;
	Ball b2;
	Ball b2c;
	
	@BeforeEach
	void setUp() throws Exception {
		p11 = new Point(1,1);
		p05 = new Point(0,5);
		p38 = new Point(3,8);
		pm14 = new Point(-1,4);
		r1138 = new Rect(p11,p38);
		rm1438 = new Rect(pm14,p38);
		v1010 = new Vector(10,10);
		v55 = new Vector(5,5);
		b1 = new NormalBall(p05, 2, v1010);
		b1c = new NormalBall(p05, 2, v1010);
		b2 = new SuperBall(p05, 2, v1010, 1);
		b2c = new SuperBall(p05, 2, v1010, 1);
	}

	@Test
	void testBall() {
		assertEquals(p05, b1.getCenter());
		assertEquals(2, b1.getDiameter());
		assertEquals(v1010, b1.getVelocity());
		
		assertEquals(p05, b2.getCenter());
		assertEquals(2, b2.getDiameter());
		assertEquals(v1010, b2.getVelocity());
		assertEquals(1, ((SuperBall) b2).getLifetime());
	}

	@Test
	void testBallMovements() {
		Vector dir = r1138.overlap(b1.rectangleOf());
		b1.bounce(dir);
		assertEquals(new Vector(-10,10), b1.getVelocity());
		Point oldCenter = b1.getCenter(); 
		b1.roll(1);
		assertEquals(b1.getCenter().minus(new Vector(-10,10)), oldCenter);
		b1c.hitBlock(r1138, true);
		assertEquals(new Vector(-10,10), b1c.getVelocity());
		
		Vector dir2 = r1138.overlap(b2.rectangleOf());
		b2.bounce(dir2);
		assertEquals(new Vector(-10,10), b2.getVelocity());
		Point oldCenter2 = b2.getCenter();
		b2.roll(1);
		assertEquals(b2.getCenter().minus(new Vector(-10,10)), oldCenter2);
		b2c.hitBlock(r1138, true);
		assertEquals(v1010, b2c.getVelocity());
	}
	
	@Test
	void testChange() {
		b1.changeCenter(p11);
		assertEquals(p11, b1.getCenter());
		b2.changeCenter(p11);
		assertEquals(p11, b2.getCenter());
		
		b1.changeVelocity(v55);
		assertEquals(v55, b1.getVelocity());
		b2.changeVelocity(v55);
		assertEquals(v55, b2.getVelocity());
		
		((SuperBall) b2).changeLifetime(2);
		assertEquals(2, ((SuperBall) b2).getLifetime());
		((SuperBall) b2).resetLifetime();
		assertEquals(Ball.MAX_LIFETIME, ((SuperBall) b2).getLifetime());
	}
	
	@Test
	void testColor() {
		assertEquals(Color.red, b1.getColor());
		assertEquals(Color.pink, b2.getColor());
	}
	
	@Test
	void testRectangles() {
		assertEquals(b1c.rectangleOf(), new Rect(pm14, new Point(1,6)));
		assertEquals(b2c.rectangleOf(), new Rect(pm14, new Point(1,6)));
	}
	
	@Test
	void testTemporaryEffects() {
		b2 = b2.age(10);
		assert b2 instanceof NormalBall;
		assertEquals(b2, b2.age(10));
		
		((SuperBall) b2c).resetLifetime();
		b2c = b2c.age(1000);
		assertEquals(9000, ((SuperBall) b2c).getLifetime());
		
		b2c = b2c.powerup();
		assert b2c instanceof SuperBall;
		assertEquals(Ball.MAX_LIFETIME, ((SuperBall) b2c).getLifetime());
		
		b2c = ((SuperBall) b2c).convertToNormal();
		assert b2c instanceof NormalBall;
		
		b1 = b1.powerup();
		assert b1 instanceof SuperBall;
		
		b1c = ((NormalBall) b1c).convertToSuper();
		assert b1c instanceof SuperBall;
	}

	@Test
	void testReplicate() {
		Ball[] b1s = b1.replicate(1);
		assert (b1s[0].getVelocity().equals(b1.getVelocity().plus(Ball.replicateBallsSpeedDiff[0])) &&
				b1s[0] instanceof NormalBall);
		
		Ball[] b2s = b2.replicate(1);
		assert (b2s[0].getVelocity().equals(b2.getVelocity().plus(Ball.replicateBallsSpeedDiff[0])) &&
				b2s[0] instanceof SuperBall);
	}
}

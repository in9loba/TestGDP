package breakout;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaddleTest {
	Point p11;
	Point p00;
	Point p0249;
	Vector vsize;
	PaddleState p1;
	PaddleState p2;
	
	Ball ball;

	@BeforeEach
	void setUp() throws Exception {
		p00 = Point.ORIGIN;
		p11 = new Point(1,1);
		p0249 = new Point(0,249);
		vsize = new Vector(1500,250);
		p1 = new NormalPaddleState(p00, vsize);
		p2 = new ReplicatorPaddleState(p00, vsize, PaddleState.MAX_REPLICATOR_LIFETIME);
		ball = new NormalBall(p00, 5, new Vector(1,1));
	}

	@Test
	void testPaddle() {
		assertEquals(p00, p1.getCenter());
		assertEquals(vsize, p1.getSize());
		assertEquals(p00, p2.getCenter());
		assertEquals(vsize, p2.getSize());
		assertEquals(PaddleState.MAX_REPLICATOR_LIFETIME, ((ReplicatorPaddleState) p2).getLifetime());
		p2 = ((ReplicatorPaddleState) p2).decreaseLifetime();
		assertEquals(PaddleState.MAX_REPLICATOR_LIFETIME-1, ((ReplicatorPaddleState) p2).getLifetime());
		p2 = ((ReplicatorPaddleState) p2).resetLifetime();
		assertEquals(PaddleState.MAX_REPLICATOR_LIFETIME, ((ReplicatorPaddleState) p2).getLifetime());
		
		p1 = p1.changeCenter(p11);
		assertEquals(p11, p1.getCenter());
		p2 = p2.changeCenter(p11);
		assertEquals(p11, p2.getCenter());
	}
	
	@Test
	void testColours() {
		assertEquals(Color.green, p1.getColor());
		assertEquals(Color.yellow, p2.getColor());
	}
	
	@Test
	void testRectangles() {
		assertEquals(new Rect(p00.minus(vsize), p00.plus(vsize)), p1.rectangleOf());
		assertEquals(new Rect(p00.minus(vsize), p00.plus(vsize)), p2.rectangleOf());
	}
	
	@Test
	void testConversions() {
		PaddleState p1r = p1.powerup();
		assert p1r instanceof ReplicatorPaddleState;
		assertEquals(((ReplicatorPaddleState) p1r).getLifetime(), PaddleState.MAX_REPLICATOR_LIFETIME);
		p1r = ((ReplicatorPaddleState) p1r).decreaseLifetime();
		p1r = p1r.powerup();
		assert p1r instanceof ReplicatorPaddleState;
		assertEquals(((ReplicatorPaddleState) p1r).getLifetime(), PaddleState.MAX_REPLICATOR_LIFETIME);
		p1r = ((ReplicatorPaddleState) p1r).convertToNormal();
		assert p1r instanceof NormalPaddleState;
		p1r = ((NormalPaddleState) p1r).convertToReplicator();
		assert p1r instanceof ReplicatorPaddleState;
		p1r = ((ReplicatorPaddleState) p1r).decreaseLifetime();
		p1r = ((ReplicatorPaddleState) p1r).decreaseLifetime();
		p1r = ((ReplicatorPaddleState) p1r).decreaseLifetime();
		assert p1r instanceof NormalPaddleState;
	}
	
	@Test
	void testHits() {
		p1 = p1.changeCenter(p0249);
		ballPaddleHitResults p1hit = p1.hitBall(ball, 0);
		assertEquals(new Vector(1,-1), p1hit.ball.getVelocity());
		
		ball.changeVelocity(new Vector(1,1));
		p2 = p2.changeCenter(p0249);
		ballPaddleHitResults p2hit = p2.hitBall(ball, 0);
		assertEquals(new Vector(1,-1), p2hit.ball.getVelocity());
		assertEquals(PaddleState.MAX_REPLICATOR_LIFETIME, p2hit.reps);
		assertEquals(PaddleState.MAX_REPLICATOR_LIFETIME-1, ((ReplicatorPaddleState) p2hit.paddle).getLifetime());
	}

}

package breakout;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockTest {
	Point p11;
	Point p05;
	Point p38;
	Point pm14;
	
	Vector v1010;
	
	Rect r1138;
	Rect rm1438;
	
	NormalBlockState b1;
	SturdyBlockState b2;
	ReplicatorBlockState b3;
	PowerupBallBlockState b4;
	
	NormalBall ball;
	NormalPaddleState paddle;

	@BeforeEach
	void setUp() throws Exception {
		p11 = new Point(1,1);
		p05 = new Point(0,5);
		p38 = new Point(3,8);
		pm14 = new Point(-1,4);
		v1010 = new Vector(10,10);
		r1138 = new Rect(p11,p38);
		rm1438 = new Rect(pm14,p38);
		b1 = new NormalBlockState(p11, p38);
		b2 = new SturdyBlockState(p11, p38, BlockState.MAX_STURDY_LIFETIME);
		b3 = new ReplicatorBlockState(p11, p38);
		b4 = new PowerupBallBlockState(p11, p38);
		ball = new NormalBall(p05, 2, v1010);
		p11 = new Point(1,1);
		paddle = new NormalPaddleState(p11, new Vector(1500,250));
	}

	@Test
	void testBlocks() {
		assertEquals(r1138,b1.rectangleOf());
		assertEquals(r1138,b2.rectangleOf());
		assertEquals(r1138,b3.rectangleOf());
		assertEquals(r1138,b4.rectangleOf());
	}
	
	@Test
	void testColour() {
		assertEquals(b1.getColor(), Color.blue);
		assert (b2.getColor().equals(Color.gray) || b2.getColor().equals(Color.lightGray) || b2.getColor().equals(Color.white));
		assertEquals(b3.getColor(), Color.cyan);
		assertEquals(b4.getColor(), Color.orange);
	}
	
	@Test
	void testLifetime() {
		b2 = b2.decreaseLifetime();
		assertEquals(BlockState.MAX_STURDY_LIFETIME - 1, b2.getLifetime());
	}
	
	@Test
	void testHits() {
		ballBlockHitResults b1hit = b1.hitBy(ball, paddle);
		assertEquals(b1hit.destroyed, true);
		Vector dir = b1.rectangleOf().overlap(b1hit.ball.rectangleOf());
		assertEquals(b1hit.ball.getVelocity(), v1010.mirrorOver(dir));
		
		ball.changeVelocity(v1010);
		ballBlockHitResults b2hit = b2.hitBy(ball, paddle);
		assertEquals(b2hit.destroyed, false);
		Vector dir2 = b2.rectangleOf().overlap(b2hit.ball.rectangleOf());
		assertEquals(b2hit.ball.getVelocity(), v1010.mirrorOver(dir2));
		assertEquals(((SturdyBlockState) b2hit.block).getLifetime(), BlockState.MAX_STURDY_LIFETIME-1);
		
		ball.changeVelocity(v1010);
		ballBlockHitResults b3hit = b3.hitBy(ball, paddle);
		assertEquals(b3hit.destroyed, true);
		Vector dir3 = b3.rectangleOf().overlap(b3hit.ball.rectangleOf());
		assertEquals(b3hit.ball.getVelocity(), v1010.mirrorOver(dir3));
		assert b3hit.paddle instanceof ReplicatorPaddleState;
		
		ball.changeVelocity(v1010);
		ballBlockHitResults b4hit = b4.hitBy(ball, paddle);
		assertEquals(b4hit.destroyed, true);
		Vector dir4 = b4.rectangleOf().overlap(b4hit.ball.rectangleOf());
		assertEquals(b4hit.ball.getVelocity(), v1010.mirrorOver(dir4));
		assert b4hit.ball instanceof SuperBall;
	}

}

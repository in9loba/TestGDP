package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestStructlikeClasses {

	@Test
	void test() {
		SturdyBlockState block = new SturdyBlockState(Point.ORIGIN, new Point(10,20), BlockState.MAX_STURDY_LIFETIME);
		NormalBall ball = new NormalBall(new Point(10,50), 2, new Vector(-1,1));
		ReplicatorPaddleState paddle = new ReplicatorPaddleState(new Point(10,100), new Vector(5,2), PaddleState.MAX_REPLICATOR_LIFETIME);
		
		ballBlockHitResults ballBlock = new ballBlockHitResults(block, ball, paddle, false);
		assertEquals(ballBlock.ball, ball);
		assertEquals(ballBlock.block, block);
		assertEquals(ballBlock.destroyed, false);
		assertEquals(ballBlock.paddle, paddle);
		
		ballPaddleHitResults ballPaddle = new ballPaddleHitResults(ball, paddle, PaddleState.MAX_REPLICATOR_LIFETIME);
		assertEquals(ballPaddle.ball, ball);
		assertEquals(ballPaddle.paddle, paddle);
		assertEquals(ballPaddle.reps, PaddleState.MAX_REPLICATOR_LIFETIME);
	}

}

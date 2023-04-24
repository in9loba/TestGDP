package breakout;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class BreakoutStateTest {

	// Initialising a game state
	
	// Bottomright
	Point bottomRight = new Point(1000,1000);
	// balls
	Point ballcenter = new Point (700,500);
	int diameter = 5;
	Vector initVelocity = new Vector(1,2);
	NormalBall[] balls = {new NormalBall(ballcenter, diameter, initVelocity)};
	// paddle
	Point paddlecenter = new Point(500,900);
	Vector paddlesize = new Vector(10,4);
	PaddleState paddle = new NormalPaddleState(paddlecenter, paddlesize);
	// blocks
	Point TL = new Point(101,101);
	Point BL = new Point(151,151);
	Point TL2 = new Point(201,201);
	Point BL2 = new Point(251,251);
	BlockState[] blocks = {new NormalBlockState(TL, BL), new NormalBlockState(TL2, BL2)};
	// game state
	BreakoutState game = new BreakoutState(balls, blocks, bottomRight, paddle);
	Ball[] gameBalls = game.getBalls();
	
	@Test
	void testConstr() {
	
		// constructor asserts
		assert Stream.of(game.getBalls()).allMatch(e -> Stream.of(balls).anyMatch(f -> f.equals(e)));
		assert Stream.of(game.getBlocks()).allMatch(e -> Stream.of(blocks).anyMatch(f -> f.equals(e)));
		assertEquals(game.getBottomRight(),bottomRight);
		assertEquals(game.getPaddle(),paddle);
	}
	
	@Test
	void testMovePaddle() {
		
		// paddle moving asserts
		game.movePaddleRight(1);
		assertEquals(game.getPaddle().getCenter().getX(), 510);
		assertEquals(game.getPaddle().getCenter().getY(), 900);
		game.movePaddleLeft(1);
		assertEquals(game.getPaddle().getCenter().getX(), 500);
		assertEquals(game.getPaddle().getCenter().getY(), 900);
		PaddleState AtRight = new NormalPaddleState(new Point(990,900),paddlesize);
		// paddles at the boundary of the game field
		BreakoutState paddleAtRight = new BreakoutState(balls, blocks, bottomRight, AtRight);
		paddleAtRight.movePaddleRight(1);
		assertEquals(paddleAtRight.getPaddle().getCenter().getX(), 990);
		assertEquals(paddleAtRight.getPaddle().getCenter().getY(), 900);
		PaddleState AtLeft = new NormalPaddleState(new Point(10,900),paddlesize);
		BreakoutState paddleAtLeft = new BreakoutState(balls, blocks, bottomRight, AtLeft);
		paddleAtLeft.movePaddleLeft(1);
		assertEquals(paddleAtLeft.getPaddle().getCenter().getX(), 10);
		assertEquals(paddleAtLeft.getPaddle().getCenter().getY(), 900);
	}
	
	@Test
	void testTerminalState() {
		// terminal state asserts
		BlockState[] blocks2 = {};
		BreakoutState gameWon = new BreakoutState(balls, blocks2, bottomRight, paddle);
		assert gameWon.isWon();
		assert !(game.isWon());
		Ball[] balls2 = {};
		BreakoutState dead = new BreakoutState(balls2, blocks, bottomRight, paddle);
		assert dead.isDead();
		assert !(game.isDead());
	}
	
	@Test
	void testTickRoll() {
		
		// tick() asserts
		// roll ball
		BreakoutState rollBall = new BreakoutState(game.getBalls(),game.getBlocks(),game.getBottomRight(),game.getPaddle());
		rollBall.tick(0,1);
		gameBalls[0].roll(1);
		assert rollBall.getBalls()[0].getCenter().getX() == gameBalls[0].getCenter().getX();
		assert rollBall.getBalls()[0].getCenter().getY() == gameBalls[0].getCenter().getY();
	}
	
	@Test
	void testTickBallPaddle() {
		
		// paddle-ball bounce; no paddle movement
		Ball[] ballAtPaddle = {new NormalBall(new Point(500,888),10,new Vector(1,5))};
		BreakoutState paddleBallNoMovGame = new BreakoutState(ballAtPaddle,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = paddleBallNoMovGame.getBalls();
		paddleBallNoMovGame.tick(0,1);
		Ball paddleBallNoMovBall = gameBalls.clone()[0];
		paddleBallNoMovBall.roll(1);
		paddleBallNoMovBall.bounce(Vector.UP);
		assert paddleBallNoMovGame.getBalls()[0].getCenter().getX() == paddleBallNoMovBall.getCenter().getX();
		assert paddleBallNoMovGame.getBalls()[0].getCenter().getY() == paddleBallNoMovBall.getCenter().getY();
		assert paddleBallNoMovGame.getBalls()[0].getVelocity().equals(paddleBallNoMovBall.getVelocity());
		
		// paddle-ball bounce; paddle moves to the right
		Ball[] ballAtPaddleRightM = {new NormalBall(new Point(500,888),10,new Vector(1,5))};
		BreakoutState paddleBallRightMovGame = new BreakoutState(ballAtPaddleRightM,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = paddleBallRightMovGame.getBalls();
		paddleBallRightMovGame.tick(1,1);
		Ball paddleBallRightMovBall = gameBalls.clone()[0];
		paddleBallRightMovBall.roll(1);
		paddleBallRightMovBall.bounce(Vector.UP);
		assert paddleBallRightMovGame.getBalls()[0].getCenter().getX() == paddleBallRightMovBall.getCenter().getX();
		assert paddleBallRightMovGame.getBalls()[0].getCenter().getY() == paddleBallRightMovBall.getCenter().getY();
		Ball paddleBallRightMovBallCheck = paddleBallRightMovGame.getBalls().clone()[0];
		paddleBallRightMovBall.changeVelocity(paddleBallRightMovBall.getVelocity().plus(Vector.RIGHT.scaled(2*1)));
		assert paddleBallRightMovGame.getBalls()[0].getVelocity().equals(paddleBallRightMovBallCheck.getVelocity());
				
		
		// paddle-ball bounce; paddle moves to the left
		Ball[] ballAtPaddleLeftM = {new NormalBall(new Point(500,888),10,new Vector(1,5))};
		BreakoutState paddleBallLeftMovGame = new BreakoutState(ballAtPaddleLeftM,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = paddleBallLeftMovGame.getBalls();
		paddleBallLeftMovGame.tick(-1,1);
		Ball paddleBallLeftMovBall = gameBalls.clone()[0];
		paddleBallLeftMovBall.roll(1);
		paddleBallLeftMovBall.bounce(Vector.UP);
		assert paddleBallLeftMovGame.getBalls()[0].getCenter().getX() == paddleBallLeftMovBall.getCenter().getX();
		assert paddleBallLeftMovGame.getBalls()[0].getCenter().getY() == paddleBallLeftMovBall.getCenter().getY();
		Ball paddleBallLeftMovBallCheck = paddleBallLeftMovGame.getBalls().clone()[0];
		paddleBallLeftMovBall.changeVelocity(paddleBallLeftMovBall.getVelocity().minus(Vector.RIGHT.scaled(2*1)));
		assert paddleBallLeftMovGame.getBalls()[0].getVelocity().equals(paddleBallLeftMovBallCheck.getVelocity());
	}
	
	@Test
	void testBallBlock() {
		
		// ball bounces on a block from below
		Ball[] ballAtBlockBelow = {new NormalBall(new Point(125,165),10,new Vector(0,-15))};
		BreakoutState ballAtBlockBelowGame = new BreakoutState(ballAtBlockBelow,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = ballAtBlockBelowGame.getBalls();
		BlockState[] gameBlocksBelow = ballAtBlockBelowGame.getBlocks();
		ballAtBlockBelowGame.tick(0,1);
		Ball ballAtBlockBelowGameBall = gameBalls[0];
		ballAtBlockBelowGameBall.roll(1);
		ballAtBlockBelowGameBall.bounce(Vector.DOWN);
		assert ballAtBlockBelowGame.getBalls()[0].getCenter().getX() == ballAtBlockBelowGameBall.getCenter().getX();
		assert ballAtBlockBelowGame.getBalls()[0].getCenter().getY() == ballAtBlockBelowGameBall.getCenter().getY();
		assert ballAtBlockBelowGame.getBalls()[0].getVelocity().equals(ballAtBlockBelowGameBall.getVelocity());
		assert Stream.of(ballAtBlockBelowGame.getBlocks()).allMatch(e -> Stream.of(gameBlocksBelow).anyMatch(f -> f.equals(e)));
		assert Stream.of(ballAtBlockBelowGame.getBlocks()).allMatch(e -> !(e.equals(gameBlocksBelow[0])));
		
		// ball bounces on a block from above
		Ball[] ballAtBlockAbove = {new NormalBall(new Point(125,85),10,new Vector(0,15))};
		BreakoutState ballAtBlockAboveGame = new BreakoutState(ballAtBlockAbove,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = ballAtBlockAboveGame.getBalls();
		BlockState[] gameBlocksAbove = ballAtBlockAboveGame.getBlocks();
		ballAtBlockAboveGame.tick(0,1);
		Ball ballAtBlockAboveGameBall = gameBalls[0];
		ballAtBlockAboveGameBall.roll(1);
		ballAtBlockAboveGameBall.bounce(Vector.DOWN);
		assert ballAtBlockAboveGame.getBalls()[0].getCenter().getX() == ballAtBlockAboveGameBall.getCenter().getX();
		assert ballAtBlockAboveGame.getBalls()[0].getCenter().getY() == ballAtBlockAboveGameBall.getCenter().getY();
		assert ballAtBlockAboveGame.getBalls()[0].getVelocity().equals(ballAtBlockAboveGameBall.getVelocity());
		assert Stream.of(ballAtBlockAboveGame.getBlocks()).allMatch(e -> Stream.of(gameBlocksAbove).anyMatch(f -> f.equals(e)));
		assert Stream.of(ballAtBlockAboveGame.getBlocks()).allMatch(e -> !(e.equals(gameBlocksAbove[0])));		
		
		// ball bounces on a block at the left
		Ball[] ballAtBlockLeft = {new NormalBall(new Point(85,125),10,new Vector(15,0))};
		BreakoutState ballAtBlockLeftGame = new BreakoutState(ballAtBlockLeft,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = ballAtBlockLeftGame.getBalls();
		BlockState[] gameBlocksLeft = ballAtBlockLeftGame.getBlocks();
		ballAtBlockLeftGame.tick(0,1);
		Ball ballAtBlockLeftGameBall = gameBalls[0];
		ballAtBlockLeftGameBall.roll(1);
		ballAtBlockLeftGameBall.bounce(Vector.LEFT);
		assert ballAtBlockLeftGame.getBalls()[0].getCenter().getX() == ballAtBlockLeftGameBall.getCenter().getX();
		assert ballAtBlockLeftGame.getBalls()[0].getCenter().getY() == ballAtBlockLeftGameBall.getCenter().getY();
		assert ballAtBlockLeftGame.getBalls()[0].getVelocity().equals(ballAtBlockLeftGameBall.getVelocity());
		assert Stream.of(ballAtBlockLeftGame.getBlocks()).allMatch(e -> Stream.of(gameBlocksLeft).anyMatch(f -> f.equals(e)));
		assert Stream.of(ballAtBlockLeftGame.getBlocks()).allMatch(e -> !(e.equals(gameBlocksLeft[0])));
		
		// ball bounces on a block at the right
		Ball[] ballAtBlockRight = {new NormalBall(new Point(165,125),10,new Vector(-15,0))};
		BreakoutState ballAtBlockRightGame = new BreakoutState(ballAtBlockRight,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = ballAtBlockRightGame.getBalls();
		BlockState[] gameBlocksRight = ballAtBlockRightGame.getBlocks();
		ballAtBlockRightGame.tick(0,1);
		Ball ballAtBlockRightGameBall = gameBalls[0];
		ballAtBlockRightGameBall.roll(1);
		ballAtBlockRightGameBall.bounce(Vector.LEFT);
		assert ballAtBlockRightGame.getBalls()[0].getCenter().getX() == ballAtBlockRightGameBall.getCenter().getX();
		assert ballAtBlockRightGame.getBalls()[0].getCenter().getY() == ballAtBlockRightGameBall.getCenter().getY();
		assert ballAtBlockRightGame.getBalls()[0].getVelocity().equals(ballAtBlockRightGameBall.getVelocity());
		assert Stream.of(ballAtBlockRightGame.getBlocks()).allMatch(e -> Stream.of(gameBlocksRight).anyMatch(f -> f.equals(e)));
		assert Stream.of(ballAtBlockRightGame.getBlocks()).allMatch(e -> !(e.equals(gameBlocksRight[0])));
	}
	
	@Test
	void testBallField() {
		
		// ball bounces at the top of the game field
		Ball[] ballBounceTop = {new NormalBall(new Point(500,15),10,new Vector(1,-10))};
		BreakoutState ballBounceTopGame = new BreakoutState(ballBounceTop,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = ballBounceTopGame.getBalls();
		ballBounceTopGame.tick(0,1);
		Ball ballBounceTopGameBall = gameBalls[0];
		ballBounceTopGameBall.roll(1);
		ballBounceTopGameBall.bounce(Vector.UP);
		assert ballBounceTopGame.getBalls()[0].getCenter().getX() == ballBounceTopGameBall.getCenter().getX();
		assert ballBounceTopGame.getBalls()[0].getCenter().getY() == ballBounceTopGameBall.getCenter().getY();
		assert ballBounceTopGame.getBalls()[0].getVelocity().equals(ballBounceTopGameBall.getVelocity());
		
		// ball bounces at the left of the game field
		Ball[] ballBounceLeft = {new NormalBall(new Point(15,500),10,new Vector(-10,1))};
		BreakoutState ballBounceLeftGame = new BreakoutState(ballBounceLeft,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = ballBounceLeftGame.getBalls();
		ballBounceLeftGame.tick(0,1);
		Ball ballBounceLeftGameBall = gameBalls[0];
		ballBounceLeftGameBall.roll(1);
		ballBounceLeftGameBall.bounce(Vector.LEFT);
		assert ballBounceLeftGame.getBalls()[0].getCenter().getX() == ballBounceLeftGameBall.getCenter().getX();
		assert ballBounceLeftGame.getBalls()[0].getCenter().getY() == ballBounceLeftGameBall.getCenter().getY();
		assert ballBounceLeftGame.getBalls()[0].getVelocity().equals(ballBounceLeftGameBall.getVelocity());
		
		// ball bounces at the right of the game field
		Ball[] ballBounceRight = {new NormalBall(new Point(985,500),10,new Vector(10,1))};
		BreakoutState ballBounceRightGame = new BreakoutState(ballBounceRight,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		gameBalls = ballBounceRightGame.getBalls();
		ballBounceRightGame.tick(0,1);
		Ball ballBounceRightGameBall = gameBalls[0];
		ballBounceRightGameBall.roll(1);
		ballBounceRightGameBall.bounce(Vector.LEFT);
		assert ballBounceRightGame.getBalls()[0].getCenter().getX() == ballBounceRightGameBall.getCenter().getX();
		assert ballBounceRightGame.getBalls()[0].getCenter().getY() == ballBounceRightGameBall.getCenter().getY();
		assert ballBounceRightGame.getBalls()[0].getVelocity().equals(ballBounceRightGameBall.getVelocity());
		
		// ball reaches the bottom of the game field
		Ball[] ballBottom = {new NormalBall(new Point(500,985),10,new Vector(1,10)),
								  new NormalBall(new Point(500,100),10,new Vector(1,-1))};
		BreakoutState ballBottomGame = new BreakoutState(ballBottom,game.getBlocks(),game.getBottomRight(),game.getPaddle());
		Ball[] gameBallsBottom = ballBottomGame.getBalls();
		ballBottomGame.tick(0,1);
		assert Stream.of(ballBottomGame.getBalls()).allMatch(e -> Stream.of(gameBallsBottom).anyMatch(f -> f.equals(e)));
		assert Stream.of(ballBottomGame.getBalls()).allMatch(e -> !(e.equals(gameBallsBottom[0])));
	}
}

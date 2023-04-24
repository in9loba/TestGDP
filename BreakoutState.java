package breakout;

import java.util.stream.Stream;

/**
 * Each instance of this class represents a game state of the breakout game.
 * 
 * @invar There are no null pointers for any ball.
 * 	| getBalls() != null && Stream.of(getBalls()).allMatch(e -> e != null)
 * @invar The center of each ball is situated inside the game field.
 * 	| Stream.of(getBalls()).allMatch(e -> e.getCenter().isUpAndLeftFrom(getBottomRight()) &&
 * 	|	Point.ORIGIN.isUpAndLeftFrom(e.getCenter()))
 * @invar There are no null pointers for any block.
 * 	| getBlocks() != null && Stream.of(getBlocks()).allMatch(e -> e != null)
 * @invar Each block is situated inside the game field.
 * 	| Stream.of(getBlocks()).allMatch(e -> e.getBottomRight().isUpAndLeftFrom(getBottomRight()) && 
 * 	|	Point.ORIGIN.isUpAndLeftFrom(e.getTopLeft()))
 * @invar There is no null pointer for the paddle.
 * 	| getPaddle() != null
 * @invar The paddle is situated inside the game field.
 * 	| getPaddle().rectangleOf().getBottomRight().isUpAndLeftFrom(getBottomRight()) &&
 * 	| 	Point.ORIGIN.isUpAndLeftFrom(getPaddle().rectangleOf().getTopLeft())
 * @invar The paddle is situated below the blocks.
 *	| Stream.of(getBlocks()).allMatch(e -> e.getBottomRight().getY() < getPaddle().getCenter().getY() - getPaddle().getSize().getY())
 * @invar The size of the game field is defined.
 * 	| getBottomRight() != null
 * @invar The lower right corner of the game field is down and right from the origin of the game field.
 * 	| Point.ORIGIN.isUpAndLeftFrom(getBottomRight())
 */
public class BreakoutState {
	
	/**
	 * @invar | balls != null && Stream.of(balls).allMatch(e -> e != null)
	 * @invar | Stream.of(balls).allMatch(e -> e.getCenter().isUpAndLeftFrom(bottomRight) && 
	 * | Point.ORIGIN.isUpAndLeftFrom(e.getCenter()))
	 * @invar | blocks != null && Stream.of(blocks).allMatch(e -> e != null)
	 * @invar | Stream.of(blocks).allMatch(e -> e.getBottomRight().isUpAndLeftFrom(bottomRight) &&
	 * | Point.ORIGIN.isUpAndLeftFrom(e.getTopLeft()))
	 * @invar | paddle != null
	 * @invar | paddle.rectangleOf().getBottomRight().isUpAndLeftFrom(bottomRight) && 
	 * | Point.ORIGIN.isUpAndLeftFrom(paddle.rectangleOf().getTopLeft())
	 * @invar | Stream.of(blocks).allMatch(e -> e.getBottomRight().getY() < paddle.getCenter().getY() - paddle.getSize().getY())
	 * @invar | bottomRight != null
	 * @invar | Point.ORIGIN.isUpAndLeftFrom(bottomRight)
	 */
	
	/** @representationObject */
	private Ball[] balls;
	/** @representationObject */
	private BlockState[] blocks;
	
	private final Point bottomRight;
	private PaddleState paddle;
	
	// The maximum elapsed time in ms between two game game ticks
	public static final int MAX_ELAPSED_TIME = 50;

	/**
	 * Returns an object representing a game state of the breakout game defined by the balls, the blocks,
	 * the paddle and the lower right corner point of the game field.
	 * @creates | result
	 * @throws IllegalArgumentException if null pointers or a null object are supplied.
	 * 	| balls == null || Stream.of(balls).anyMatch(e -> e == null)
	 * @throws IllegalArgumentException if null pointers or a null object are supplied.
	 * 	| blocks == null || Stream.of(blocks).anyMatch(e -> e == null)
	 * @throws IllegalArgumentException if no paddle is supplied.
	 * 	| paddle == null
	 * @throws IllegalArgumentException if no lower right corner point is supplied.
	 * 	| bottomRight == null
	 * @throws IllegalArgumentException if the bottom-right of the game field is up and left from the top-left.
	 * 	| bottomRight.isUpAndLeftFrom(Point.ORIGIN)
	 * @post | Stream.of(getBlocks()).allMatch(e -> Stream.of(blocks).anyMatch(f -> e.equals(f)))
	 * @post | getBottomRight().equals(bottomRight)
	 * @post | getPaddle().equals(paddle)
	 */
	public BreakoutState(Ball[] balls, BlockState[] blocks, Point bottomRight, PaddleState paddle) {
		if (balls == null || Stream.of(balls).anyMatch(e -> e == null)) {
			throw new IllegalArgumentException("You have supplied an invalid ball!");
		}
		if (blocks == null || Stream.of(blocks).anyMatch(e -> e == null)) {
			throw new IllegalArgumentException("You have supplied an invalid block!");
		}
		if (paddle == null) {
			throw new IllegalArgumentException("You have not supplied a valid paddle!");
		}
		if (bottomRight == null) {
			throw new IllegalArgumentException("You have not supplied a valid game field size");
		}
		if (bottomRight.isUpAndLeftFrom(Point.ORIGIN)) {
			throw new IllegalArgumentException("You have not supplied a valid game field size");
		}
		this.balls=balls.clone();
		this.blocks=blocks.clone();
		this.bottomRight=bottomRight;
		this.paddle=paddle;
	}
	
	/**
	 * Returns the array of BallState objects contained within this BreakoutState object.
	 * @creates | result
	 * @inspects | this 
	 */
	public Ball[] getBalls() {
		return balls.clone();
	}

	/**
	 * Returns the array of BlockState objects contained within this BreakoutState object.
	 * @creates | result
	 * @inspects | this
	 */
	public BlockState[] getBlocks() {
		return blocks.clone();
	}

	/**
	 * Returns the PaddleState object contained within this BreakoutState object.
	 * @inspects | this
	 */
	public PaddleState getPaddle() {
		return paddle;
	}

	/**
	 * Returns the Point object representing the bottom right corner point of the game field,
	 * as contained within this BreakoutState object.
	 * @inspects | this
	 */
	public Point getBottomRight() {
		return bottomRight;
	}
	
	private void removeBlock(BlockState block) {
		BlockState[] blocksLeft = new BlockState[blocks.length-1];
		int found=0;
		for (int index = 0; index<blocks.length; index++) {
			if (block.equals(blocks[index])) {
				found++;
				continue;
			}
			blocksLeft[index-found]=blocks[index];
		}
		blocks=blocksLeft;
	}
	
	private void removeBall(Ball ball) {
		Ball[] ballsLeft = new Ball[balls.length-1];
		int found=0;
		for (int index = 0; index<balls.length; index++) {
			if (ball.equals(balls[index])) {
				found++;
				continue;
			}
			ballsLeft[index-found]=balls[index];
		}
		balls=ballsLeft;
	}
	
	private void replicateBall(Ball ball, int reps) {
		if (reps == 0) {
			return;
		}
		Ball[] expanded = new Ball[balls.length+reps];
		for (int i=0; i<balls.length; i++) {
			expanded[i] = balls[i];
		}
		Ball[] replicated = ball.replicate(reps);
		for (int j=0; j<reps; j++) {
			expanded[balls.length+j] = replicated[j];
		}
		balls=expanded;
	}
	
	/**
	 * Performs one movement iteration of the game based on the current position and applicable
	 * velocities of the balls, the blocks and the paddle. Removes blocks and balls if necessary.
	 * @inspects | this
	 * @mutates | this
	 * @pre paddleDir should be 0, 1 or -1.
	 * 	| paddleDir == 0 || paddleDir == 1 || paddleDir == -1
	 * @pre elapsedTime should be larger than 0 and smaller than or equal to MAX_ELAPSED_TIME.
	 * 	| elapsedTime > 0 && elapsedTime <= MAX_ELAPSED_TIME
	 * @post The new paddle's position should be identical to the old one's.
	 * 	| getPaddle().rectangleOf().equals(old(getPaddle().rectangleOf()))
	 */
	public void tick(int paddleDir, int elapsedTime) {
		for (int i=0; i<balls.length; i++) {
			
			// Retrieve the current ball state
			Ball ball=balls[i];
			
			// Age and move ball
			ball = ball.age(elapsedTime);
			ball.roll(elapsedTime);
			
			// Determine points and sizes of the ball
			Rect ballRect = ball.rectangleOf();
			int ballLeftX = ballRect.getTopLeft().getX();
			int ballRightX = ballRect.getBottomRight().getX();
			int ballTopY = ballRect.getTopLeft().getY();
			int ballBottomY = ballRect.getBottomRight().getY();
			
			// Bounce ball at the left, at the right and at the top of the game field, remove it at the bottom
			if (ballLeftX <= 0) {
				ball.bounce(Vector.LEFT);
			}
			else if (ballRightX >= bottomRight.getX()) {
				ball.bounce(Vector.RIGHT);
			}
			if (ballTopY <= 0) {
				ball.bounce(Vector.UP);
			}
			else if (ballBottomY >= bottomRight.getY()) {
				removeBall(ball);
				continue;
			}
			
			// Detecting and executing the possible effects of a ball-block hit
			for (int j=0; j<blocks.length; j++) {
				BlockState block=blocks[j];
				ballBlockHitResults blockBallHit = block.hitBy(ball, paddle);
				if (blockBallHit.destroyed) {
					removeBlock(block);
				}
				else {
					blocks[j] = blockBallHit.block;
				}
				ball = blockBallHit.ball;
				paddle = blockBallHit.paddle;
			}
			
			// Detecting and executing the possible effects of a ball-paddle hit
			ballPaddleHitResults paddleBallHit = paddle.hitBall(ball, paddleDir);
			ball = paddleBallHit.ball;
			replicateBall(ball, paddleBallHit.reps);
			paddle = paddleBallHit.paddle;
			
			// Fix ball state
			balls[i] = ball;
		}
	}
	
	/**
	 * Alters paddle such that it has moved maximum 10 units to the right in comparison with the old paddle state,
	 * while keeping it inside the game field.
	 * 
	 * Assuming that the paddle speed is expressed in units of 1/ms, the paddle moves at a speed of 10000/s,
	 * which is sufficient to move from one end of the game field to the other one in 5 seconds.
	 * 
	 * @inspects | this
	 * @mutates | this
	 * @post The paddle has moved maximum ten units to the right.
	 * 	| getPaddle().getCenter().getX() <= old(getPaddle().getCenter().getX()) + 10*elapsedTime &&
	 * 	| getPaddle().getCenter().getY() == old(getPaddle().getCenter().getY())
	 * @post The size of the paddle remained constant
	 * 	| getPaddle().getSize().equals(old(getPaddle().getSize()))	
	 */
	public void movePaddleRight(int elapsedTime) {
		Point newCenter = paddle.getCenter().plus(new Vector(10*elapsedTime,0));
		if (newCenter.plus(paddle.getSize()).getX() > bottomRight.getX()) {
			newCenter=paddle.getCenter();
		}
		paddle = paddle.changeCenter(newCenter);
	}

	/**
	 * Alters paddle such that it has moved maximum 10 units to the left in comparison with the old paddle state,
	 * while keeping it inside the game field
	 * 
	 * Assuming that the paddle speed is expressed in units of 1/ms, the paddle moves at a speed of 10000/s,
	 * which is sufficient to move from one end of the game field to the other one in 5 seconds.
	 * 
	 * @inspects | this
	 * @mutates | this
	 * @post The paddle has moved maximum ten units to the left.
	 * 	| getPaddle().getCenter().getX() >= old(getPaddle().getCenter().getX()) - 10*elapsedTime &&
	 * 	| getPaddle().getCenter().getY() == old(getPaddle().getCenter().getY())
	 * @post The size of the paddle remained constant
	 * 	| getPaddle().getSize().equals(old(getPaddle().getSize()))	
	 */
	public void movePaddleLeft(int elapsedTime) {
		Point newCenter = paddle.getCenter().minus(new Vector(10*elapsedTime,0));
		if (newCenter.minus(paddle.getSize()).getX() < 0) {
			newCenter=paddle.getCenter();
		}
		paddle = paddle.changeCenter(newCenter);
	}
	
	/**
	 * Checks whether this BreakoutState object is in a winning terminal state.
	 * @inspects | this
	 */
	public boolean isWon() {
		return (blocks.length == 0 && balls.length > 0);
	}

	/**
	 * Checks whether this BreakoutState object is in a losing terminal state.
	 * @inspects | this
	 */
	public boolean isDead() {
		return (balls.length == 0);
	}
}
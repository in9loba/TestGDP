package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RectTest {
	Point p01;
	Point p10;
	Point p38;
	Point p25;
	
	Rect r0138;
	Rect r1025;
	Rect r0025;
	Rect r2538;
	
	@BeforeEach
	void setUp() throws Exception {
		p01 = new Point(0,1);
		p10 = new Point(1,0);
		p38 = new Point(3,8);
		p25 = new Point(2,5);
		r0138 = new Rect(p01,p38);
		r1025 = new Rect(p10,p25);
	}

	@Test
	void testRect() {
		assertEquals(p01, r0138.getTopLeft());
		assertEquals(p38, r0138.getBottomRight());
		assertEquals(p10, r1025.getTopLeft());
		assertEquals(p25, r1025.getBottomRight());
	}
	
	@Test
	void testOverlap() {
		assertEquals(Vector.DOWN, r1025.overlap(r0138));
	}

}

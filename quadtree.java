import java.util.ArrayList;
import java.util.List;


class Node {
	int x, y, value;

	Node(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value; 
	}
}

class Boundry {
	public int getxMin() {
		return xMin;
	}
	public int getyMin() {
		return yMin;
	}
	public int getxMax() {
		return xMax;
	}
	public int getyMax() {
		return yMax;
	}
	public Boundry(int xMin, int yMin, int xMax, int yMax) {
		super();
	
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}

	public boolean inRange(int x, int y) {
		return (x >= this.getxMin() && x <= this.getxMax()
				&& y >= this.getyMin() && y <= this.getyMax());
	}

	int xMin, yMin, xMax, yMax;

}

public class QuadTree {
	final int MAX_CAPACITY =4;
	int level = 0;
	List<Node> nodes;
	QuadTree northWest = null;
	QuadTree northEast = null;
	QuadTree southWest = null;
	QuadTree southEast = null;
	Boundry boundry;

	QuadTree(int level, Boundry boundry) {
		this.level = level;
		nodes = new ArrayList<Node>();
		this.boundry = boundry;
	}


	static void dfs(QuadTree tree) {
		if (tree == null)
			return;

		System.out.printf("\nLevel = %d [X1=%d Y1=%d] \t[X2=%d Y2=%d] ",
				tree.level, tree.boundry.getxMin(),tree.boundry.getyMin(),
				tree.boundry.getxMax(),tree.boundry.getyMax());

		for (Node node : tree.nodes) {
			System.out.printf("\n\t  x=%d y=%d", node.x, node.y);
		}
		if (tree.nodes.size() == 0) {
			System.out.printf("\n\t  Leaf Node.");
		}
		dfs(tree.northWest);
		dfs(tree.northEast);
		dfs(tree.southWest);
		dfs(tree.southEast);
	}
	void split() {
		int xOffset = this.boundry.getxMin()
				+ (this.boundry.getxMax() - this.boundry.getxMin()) / 2;
		int yOffset = this.boundry.getyMin()
				+ (this.boundry.getyMax() - this.boundry.getyMin()) / 2;

		northWest = new QuadTree(this.level + 1, new Boundry(
				this.boundry.getxMin(), this.boundry.getyMin(), xOffset,
				yOffset));
		northEast = new QuadTree(this.level + 1, new Boundry(xOffset, this.boundry.getyMin(), this.boundry.getxMax(), yOffset));
		southWest = new QuadTree(this.level + 1, new Boundry(this.boundry.getxMin(), yOffset, xOffset, this.boundry.getyMax()));
		southEast = new QuadTree(this.level + 1, new Boundry(xOffset, yOffset,
				this.boundry.getxMax(), this.boundry.getyMax()));
	}
	void insert(int x, int y, int value) {
		if (!this.boundry.inRange(x, y)) {
			return;
		}
		Node node = new Node(x, y, value);
		if (nodes.size() < MAX_CAPACITY) {
			nodes.add(node);
			return;
		}
		if (northWest == null) {
			split();
		}
		if (this.northWest.boundry.inRange(x, y))
			this.northWest.insert(x, y, value);
		else if (this.northEast.boundry.inRange(x, y))
			this.northEast.insert(x, y, value);
		else if (this.southWest.boundry.inRange(x, y))
			this.southWest.insert(x, y, value);
		else if (this.southEast.boundry.inRange(x, y))
			this.southEast.insert(x, y, value);
		else
			System.out.printf("ERROR : Unhandled partition %d %d", x, y);
	}
	public static void main(String args[]) {
		QuadTree anySpace = new QuadTree(1, new Boundry(0, 0, 1000, 1000));
		anySpace.insert(100, 100, 1);
		anySpace.insert(500, 500, 1);
	
		QuadTree.dfs(anySpace);
	}
}

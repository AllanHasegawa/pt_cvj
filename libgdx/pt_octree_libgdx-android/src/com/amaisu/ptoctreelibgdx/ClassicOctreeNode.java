package com.amaisu.ptoctreelibgdx;

public class ClassicOctreeNode {
	private int x;
	private int y;
	private int z;
	private int size;
	private int treeMaxSize;
	private int data;
	private ClassicOctreeNode[] children;
	private ClassicOctreeNode father;

	public ClassicOctreeNode(int x, int y, int z, int size, int treeMaxSize) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;
		this.treeMaxSize = treeMaxSize;
		this.data = MOctreeCell.DATA_EMPTY;

		children = null;
		father = null;
	}

	public void InsertCell(int x, int y, int z, int data) {
		if (size == 1) {
			this.data = data;

			this.PopulateFatherCells(this);

		} else {
			if (this.data == data) {
				return;
			}
			// Make sure it represents an mixed cell;
			this.data = MOctreeCell.DATA_EMPTY;
			this.CreateChildren();
			this.children[ConvertXYZToChildNumber(x, y, z)].InsertCell(x, y, z,
					data);
		}
	}

	int GetData(int x, int y, int z) {

		// if there is data BEFORE hitting the bottom of the tree
		if (this.data != MOctreeCell.DATA_EMPTY) {
			return this.data;
		}

		// if is in the actual cell, but no data
		if (size == 1) {
			return MOctreeCell.DATA_EMPTY;
		}

		if (children == null) {
			return MOctreeCell.DATA_EMPTY;
		}

		return this.children[ConvertXYZToChildNumber(x, y, z)].GetData(x, y, z);
	}

	MOctreeCell GetCell(int x, int y, int z) {
		if (x >= treeMaxSize || y >= treeMaxSize || z >= treeMaxSize) {
			return null;
		}
		// if there is data BEFORE hitting the bottom of the tree
		// or if is the actual cell
		// or has no where to go
		if (size == 1 || children == null) {
			MOctreeCell c = new MOctreeCell();
			c.setX(x);
			c.setY(y);
			c.setZ(z);
			c.setData(data);
			c.setNode(this);
			return c;
		}

		return this.children[ConvertXYZToChildNumber(x, y, z)].GetCell(x, y, z);
	}

	MOctreeCell GetNeighbor(MOctreeCell source, int x, int y, int z) {
		ClassicOctreeNode c = source.getNode();

		while (true) {
			if (c.x >= x && c.x + c.size < x) {
				if (c.y >= y && c.y + c.size < y) {
					if (c.z >= z && c.z + c.size < z) {
						break;
					}
				}
			}
			if (c.father == null) {
				break;
			}
			c = c.father;
		}

		return c.GetCell(x + source.getX(), y + source.getY(),
				z + source.getZ());
	}

	String ToStringRecursive(int level, int child_number) {

		String t = "";
		for (int i = 0; i < level; i++) {
			t += " ";
		}
		t += "+- ";

		String sspos = "";
		sspos = "(" + x + ", " + y + ", " + z + ")";

		if (this.data == MOctreeCell.DATA_EMPTY) {
			t += "<empty> (" + child_number + ") " + sspos + "\n";
		} else {

			t += this.data + " (" + child_number + ") " + sspos + "\n";
		}

		if (size == 1 || children == null) {
			return t;
		}

		String my_string = "";
		if (level == 0) {
			my_string = "+- root\n";
		} else {
			my_string += t;
		}
		for (int i = 0; i < 8; i++) {
			my_string += children[i].ToStringRecursive(level + 1, i);
		}

		return my_string;
	}

	private void CreateChildren() {
		if (children == null && size != 1) {
			int size_father = size;
			int x_father = x;
			int y_father = y;
			int z_father = z;

			int size_children = size_father / 2;

			// Lets create the children array
			children = new ClassicOctreeNode[8];

			// then insert it :3
			int counter = 0;
			for (int z = 0; z < 2; z++) {
				for (int y = 0; y < 2; y++) {
					for (int x = 0; x < 2; x++) {
						children[counter] = new ClassicOctreeNode(x_father
								+ (x * size_children), y_father
								+ (y * size_children), z_father
								+ (z * size_children), size_children,
								treeMaxSize);
						children[counter].father = this;
						counter++;
					}
				}
			}
		}
	}

	private void KillChildren() {
		if (children != null) {
			// First delete the actual children...
			for (int i = 0; i < 8; i++) {
				children[i] = null;
			}
			// then delete its array :3
			children = null;
		}
	}

	private void PopulateFatherCells(ClassicOctreeNode cell) {
		if (cell.father != null) {
			for (int i = 0; i < 8; i++) {
				if (cell.father.children[i].data != cell.data) {
					return;
				}
			}
			if (cell.data == MOctreeCell.DATA_EMPTY) {
				cell.father.KillChildren();
			}
			cell.father.data = cell.data;
			PopulateFatherCells(cell.father);
		}
	}

	private int ConvertXYZToChildNumber(int x, int y, int z) {
		int size = this.size;
		int mid_size = size / 2;

		int bx = (x >= (mid_size + this.x)) ? 1 : 0;
		int by = (y >= (mid_size + this.y)) ? 1 : 0;
		int bz = (z >= (mid_size + this.z)) ? 1 : 0;

		int child = 0;
		child += bx;
		child += (by << 1);
		child += (bz << 2);

		return child;
	}
}

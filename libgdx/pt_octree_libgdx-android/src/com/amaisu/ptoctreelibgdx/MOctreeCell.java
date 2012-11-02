package com.amaisu.ptoctreelibgdx;

public class MOctreeCell {
	public static final int DATA_EMPTY = -1;

	public MOctreeCell() {
		x = z = y = 0;
		data = DATA_EMPTY;
	}

	private int x;
	private int y;
	private int z;
	private int data;
	private ClassicOctreeNode node;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public ClassicOctreeNode getNode() {
		return node;
	}

	public void setNode(ClassicOctreeNode node) {
		this.node = node;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}
}

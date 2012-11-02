package com.amaisu.ptoctreelibgdx;

public class ClassicOctree {

	private ClassicOctreeNode root;
	int size;

	public ClassicOctree(int size) {
		this.size = size;
		root = new ClassicOctreeNode(0, 0, 0, size, size);
	}

	void InsertCell(int x, int y, int z, int data) {
		if (x > size || y > size || z > size) {
			return;
		}

		root.InsertCell(x, y, z, data);
	}

	/*
	 * void DeleteCell(int x, int y, int z) { if (x > size || y > size || z >
	 * size) { return; } root.DeleteCell(x, y, z); }
	 */

	int GetData(int x, int y, int z) {
		if (x > size || y > size || z > size) {
			return MOctreeCell.DATA_EMPTY;
		}
		return root.GetData(x, y, z);
	}

	MOctreeCell GetCell(int x, int y, int z) {
		return root.GetCell(x, y, z);
	}

	MOctreeCell GetNeighbor(MOctreeCell source, int x, int y, int z) {
		return root.GetNeighbor(source, x, y, z);
	}

	String ToString() {
		return root.ToStringRecursive(0, 0);
	}

	int size() {
		return size;
	}

}

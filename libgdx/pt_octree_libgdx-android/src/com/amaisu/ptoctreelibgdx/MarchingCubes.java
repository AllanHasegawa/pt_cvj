package com.amaisu.ptoctreelibgdx;

import com.badlogic.gdx.Gdx;

public class MarchingCubes {

	private static MarchingCubes instance_;
	private MCTemplate uniques_[];
	private MCTemplate templates_[];
	private Vector3 corners_as_vectors_[];

	public static MarchingCubes CreateMarchingCubes() {
		if (instance_ == null) {
			instance_ = new MarchingCubes();
		}
		return instance_;
	}

	public MCTemplate GetTemplate(final int index) {
		return templates_[index];
	}

	public MCTemplate GetTemplate(final int v0, final int v1, final int v2,
			final int v3, final int v4, final int v5, final int v6, final int v7) {
		return templates_[CornersToIndex(v0, v1, v2, v3, v4, v5, v6, v7)];
	}

	public static int CornersToIndex(final int v0, final int v1, final int v2,
			final int v3, final int v4, final int v5, final int v6, final int v7) {
		return (v0 << 0) | (v1 << 1) | (v2 << 2) | (v3 << 3) | (v4 << 4)
				| (v5 << 5) | (v6 << 6) | (v7 << 7);
	}

	public int IndexRotateX(final int index, int degrees) {
		if (degrees == 0 || degrees % 360 == 0) {
			return index;
		}

		if (degrees % 90 != 0) {
			return -1;
		}

		int new_index = 0;
		for (int i = 0; i < 8; i++) {
			int single_index = index & (1 << i);
			if (single_index != 0) {

				Vector3 v = new Vector3();
				v.x_ = corners_as_vectors_[i].x_;
				v.y_ = corners_as_vectors_[i].y_;
				v.z_ = corners_as_vectors_[i].z_;
				v.Translate(-0.5f, -0.5f, 0.5f).RotateX(degrees)
						.Translate(0.5f, 0.5f, -0.5f);
				new_index = new_index | (1 << ConvertVectorToCorner(v));
			}
		}

		return new_index;
	}

	public int IndexRotateY(final int index, int degrees) {
		if (degrees == 0 || degrees % 360 == 0) {
			return index;
		}

		if (degrees % 90 != 0) {
			return -1;
		}

		int new_index = 0;
		for (int i = 0; i < 8; i++) {
			int single_index = index & (1 << i);
			if (single_index != 0) {
				Vector3 v = new Vector3();
				v.x_ = corners_as_vectors_[i].x_;
				v.y_ = corners_as_vectors_[i].y_;
				v.z_ = corners_as_vectors_[i].z_;
				
				v.Translate(-0.5f, -0.5f, 0.5f).RotateY(degrees)
						.Translate(0.5f, 0.5f, -0.5f);
				
				new_index = new_index | (1 << ConvertVectorToCorner(v));
			}
		}

		return new_index;
	}

	public int IndexRotateZ(final int index, int degrees) {
		if (degrees == 0 || degrees % 360 == 0) {
			return index;
		}

		if (degrees % 90 != 0) {
			return -1;
		}

		int new_index = 0;
		for (int i = 0; i < 8; i++) {
			int single_index = index & (1 << i);
			if (single_index != 0) {

				Vector3 v = new Vector3();
				v.x_ = corners_as_vectors_[i].x_;
				v.y_ = corners_as_vectors_[i].y_;
				v.z_ = corners_as_vectors_[i].z_;
				v.Translate(-0.5f, -0.5f, 0.5f).RotateZ(degrees)
						.Translate(0.5f, 0.5f, -0.5f);
				new_index = new_index | (1 << ConvertVectorToCorner(v));
			}
		}

		return new_index;
	}

	public int IndexMirrorX(final int index) {
		int new_index = 0;
		for (int i = 0; i < 8; i++) {
			final int single_index = index & (1 << i);
			if (single_index != 0) {
				Vector3 v = new Vector3();
				v.x_ = corners_as_vectors_[i].x_;
				v.y_ = corners_as_vectors_[i].y_;
				v.z_ = corners_as_vectors_[i].z_;
				v.Translate(-0.5f, -0.5f, 0.5f).MirrorX()
						.Translate(0.5f, 0.5f, -0.5f);
				new_index = new_index | (1 << ConvertVectorToCorner(v));
			}
		}

		return new_index;
	}

	public int IndexMirrorY(final int index) {
		int new_index = 0;
		for (int i = 0; i < 8; i++) {
			final int single_index = index & (1 << i);
			if (single_index != 0) {
				Vector3 v = new Vector3();
				v.x_ = corners_as_vectors_[i].x_;
				v.y_ = corners_as_vectors_[i].y_;
				v.z_ = corners_as_vectors_[i].z_;
				v.Translate(-0.5f, -0.5f, 0.5f).MirrorY()
						.Translate(0.5f, 0.5f, -0.5f);
				new_index = new_index | (1 << ConvertVectorToCorner(v));
			}
		}

		return new_index;
	}

	public int IndexMirrorZ(final int index) {
		int new_index = 0;
		for (int i = 0; i < 8; i++) {
			final int single_index = index & (1 << i);
			if (single_index != 0) {
				Vector3 v = new Vector3();
				v.x_ = corners_as_vectors_[i].x_;
				v.y_ = corners_as_vectors_[i].y_;
				v.z_ = corners_as_vectors_[i].z_;
				v.Translate(-0.5f, -0.5f, 0.5f).MirrorZ()
						.Translate(0.5f, 0.5f, -0.5f);
				new_index = new_index | (1 << ConvertVectorToCorner(v));
			}
		}

		return new_index;
	}

	int ConvertVectorToCorner(final Vector3 v) {

		final boolean vx = v.x_ > 0.5f;
		final boolean vy = v.y_ > 0.5f;
		final boolean vz = v.z_ < -0.5f;

		for (int i = 0; i < 8; i++) {
			if (((corners_as_vectors_[i].x_ > 0.5f && vx) || (corners_as_vectors_[i].x_ <= 0.5f && !vx))
					&& ((corners_as_vectors_[i].y_ > 0.5f && vy) || (corners_as_vectors_[i].y_ <= 0.5f && !vy))
					&& ((corners_as_vectors_[i].z_ < -0.5f && vz) || (corners_as_vectors_[i].z_ >= -0.5f && !vz))) {
				return i;
			}
		}

		Gdx.app.log("ERROR", "ConvertVectorToCorner: " + v.x_ + ", " + v.y_
				+ "," + v.z_);
		return -1;
	}

	private MarchingCubes() {
		GenerateCornersAsVectors();
		GenerateUniqueTemplates();
		GenerateTemplates();
	}

	private void GenerateCornersAsVectors() {
		corners_as_vectors_ = new Vector3[8];

		for (int i = 0; i < 8; i++) {
			corners_as_vectors_[i] = new Vector3();
		}

		int counter = 0;
		for (int z = 0; z > -2; z--) {
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 2; x++) {
					corners_as_vectors_[counter].x_ = x;
					corners_as_vectors_[counter].y_ = y;
					corners_as_vectors_[counter].z_ = z;
					counter++;
				}
			}
		}
	}

	private void GenerateTemplates() {

		// empty template
		templates_[0] = new MCTemplate(0, 0);
		templates_[CornersToIndex(1, 1, 1, 1, 1, 1, 1, 1)] = new MCTemplate(0,
				0);

		/*
		 * All the cubes rotations at:
		 * http://www.euclideanspace.com/maths/geometry
		 * /rotations/euler/examples/index.htm
		 */
		final int n_unique_templates_ = 13;
		for (int i = 0; i < n_unique_templates_; i++) {
			MCTemplate u_t = uniques_[i];

			for (int mirror = 0; mirror < 4; mirror++) {

				int mirror_index = u_t.index_;
				if (mirror == 1) {
					mirror_index = IndexMirrorX(u_t.index_);
				} else if (mirror == 2) {
					mirror_index = IndexMirrorY(u_t.index_);
				} else if (mirror == 3) {
					mirror_index = IndexMirrorZ(u_t.index_);
				}
				for (int rotx = -90; rotx <= 180; rotx += 90) {
					for (int roty = 0; roty <= 270; roty += 90) {

						// Normal combination
						int new_index = IndexRotateX(mirror_index, rotx);
						new_index = IndexRotateY(new_index, roty);

						if (templates_[new_index] == null) {
							MCTemplate t = new MCTemplate(u_t.n_triangles_,
									new_index);
							t.CopyTriangles(u_t);
							if (mirror == 1) {
								t.MirrorX();
							} else if (mirror == 2) {
								t.MirrorY();
							} else if (mirror == 3) {
								t.MirrorZ();
							}
							t.RotateX(rotx).RotateY(roty);
							templates_[new_index] = t;
						}
						// Complement combination
						new_index = ~new_index & 0xff;
						if (templates_[new_index] == null) {
							MCTemplate t = new MCTemplate(u_t.n_triangles_,
									new_index);
							t.CopyTriangles(u_t);
							if (mirror == 1) {
								t.MirrorX();
							} else if (mirror == 2) {
								t.MirrorY();
							} else if (mirror == 3) {
								t.MirrorZ();
							}
							t.RotateX(rotx).RotateY(roty);
							t.set_complement(true);
							templates_[new_index] = t;
						}
					}
				}
				for (int rotz = 90; rotz <= 180; rotz += 90) { // z only rotates
																// twice
					for (int roty = 0; roty <= 270; roty += 90) {
						int new_index = IndexRotateZ(mirror_index, rotz);
						new_index = IndexRotateY(new_index, roty);
						if (templates_[new_index] == null) {
							MCTemplate t = new MCTemplate(u_t.n_triangles_,
									new_index);
							t.CopyTriangles(u_t);
							if (mirror == 1) {
								t.MirrorX();
							} else if (mirror == 2) {
								t.MirrorY();
							} else if (mirror == 3) {
								t.MirrorZ();
							}
							t.RotateZ(rotz).RotateY(roty);
							templates_[new_index] = t;
						}
						// Complement combination
						new_index = ~new_index & 0xff;
						if (templates_[new_index] == null) {
							MCTemplate t = new MCTemplate(u_t.n_triangles_,
									new_index);
							t.CopyTriangles(u_t);
							if (mirror == 1) {
								t.MirrorX();
							} else if (mirror == 2) {
								t.MirrorY();
							} else if (mirror == 3) {
								t.MirrorZ();
							}
							t.RotateZ(rotz).RotateY(roty);
							t.set_complement(true);
							templates_[new_index] = t;
						}
					}
				}
			}

		}
	}

	private void GenerateUniqueTemplates() {
		uniques_ = new MCTemplate[14];
		templates_ = new MCTemplate[256];

		Triangle tri;
		int tric = 0;
		MCTemplate t;
		int tc = 0;
		int index;
		/*
		 * Reference is the cube center
		 * 
		 * s Template 1 (Numbers follows (Lorensen,1987))
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 0, 0, 0, 0, 0, 0, 0);
		t = new MCTemplate(1, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.0f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();
		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		uniques_[0] = t;
		templates_[index] = t;
		/*
		 * e Template 1
		 */

		/*
		 * s Template 2
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 1, 0, 0, 0, 0, 0, 0);
		t = new MCTemplate(2, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		uniques_[1] = t;
		templates_[index] = t;
		/*
		 * e Template 2
		 */

		/*
		 * s Template 3
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 0, 0, 1, 0, 0, 0, 0);
		t = new MCTemplate(2, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		uniques_[2] = t;
		templates_[index] = t;
		/*
		 * e Template 3
		 */

		/*
		 * s Template 4
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 0, 0, 0, 0, 0, 0, 1);
		t = new MCTemplate(2, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		uniques_[3] = t;
		templates_[index] = t;
		/*
		 * e Template 4
		 */

		/*
		 * s Template 5
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(0, 1, 0, 0, 1, 1, 0, 0);
		t = new MCTemplate(3, index);

		// s tri1
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		uniques_[4] = t;
		templates_[index] = t;
		/*
		 * e Template 5
		 */

		/*
		 * s Template 6
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 1, 0, 0, 0, 0, 0, 1);
		t = new MCTemplate(3, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		uniques_[5] = t;
		templates_[index] = t;
		/*
		 * e Template 6
		 */

		/*
		 * s Template 7
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(0, 1, 1, 0, 0, 0, 0, 1);
		t = new MCTemplate(3, index);

		// s tri1
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.0f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		uniques_[6] = t;
		templates_[index] = t;
		/*
		 * e Template 7
		 */

		/*
		 * s Template 8
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 1, 0, 0, 1, 1, 0, 0);
		t = new MCTemplate(2, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.0f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		uniques_[7] = t;
		templates_[index] = t;
		/*
		 * e Template 8
		 */

		/*
		 * s Template 9
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 0, 0, 0, 1, 1, 1, 0);
		t = new MCTemplate(4, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		// s tri4
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri4

		uniques_[8] = t;
		templates_[index] = t;
		/*
		 * e Template 9
		 */

		/*
		 * s Template 10
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 0, 1, 0, 0, 1, 0, 1);
		t = new MCTemplate(4, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		// s tri4
		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri4

		uniques_[9] = t;
		templates_[index] = t;
		/*
		 * e Template 10
		 */

		/*
		 * s Template 11
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 0, 0, 0, 1, 1, 0, 1);
		t = new MCTemplate(4, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		// s tri4
		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri4

		uniques_[10] = t;
		templates_[index] = t;
		/*
		 * e Template 11
		 */

		/*
		 * s Template 12
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(0, 1, 1, 0, 1, 1, 0, 0);
		t = new MCTemplate(4, index);

		// s tri1
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		// s tri4
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri4

		uniques_[11] = t;
		templates_[index] = t;
		/*
		 * e Template 12
		 */

		/*
		 * s Template 13
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(1, 0, 0, 1, 0, 1, 1, 0);
		t = new MCTemplate(4, index);

		// s tri1
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		// s tri4
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri4

		uniques_[12] = t;
		templates_[index] = t;
		/*
		 * e Template 13
		 */

		/*
		 * s Template 14
		 */
		tric = 0;
		tri = new Triangle();
		tc = 0;
		tri = new Triangle();
		index = CornersToIndex(0, 1, 0, 0, 1, 1, 1, 0);
		t = new MCTemplate(4, index);

		// s tri1
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri1

		// s tri2
		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri2

		// s tri3
		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = -0.5f;
		tri.vertices_[tc].y_ = 0.5f;
		tri.vertices_[tc].z_ = 0.f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri3

		// s tri4
		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = -0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.f;
		tri.vertices_[tc].y_ = -0.5f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.vertices_[tc].x_ = 0.5f;
		tri.vertices_[tc].y_ = 0.f;
		tri.vertices_[tc].z_ = 0.5f;
		tc++;

		tri.CalculateNormal();

		t.triangles_[tric++] = tri;
		tc = 0;
		tri = new Triangle();
		// e tri4

		uniques_[13] = t;
		templates_[index] = t;
		/*
		 * e Template 14
		 */
	}
}

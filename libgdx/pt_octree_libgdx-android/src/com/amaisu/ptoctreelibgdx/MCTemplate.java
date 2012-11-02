package com.amaisu.ptoctreelibgdx;

public class MCTemplate {
	public Triangle triangles_[];
	public int n_triangles_;
	public int index_; // See (Lorensen, 1987)
	public boolean complement_;

	public MCTemplate(final int n_triangles, final int index) {
		triangles_ = new Triangle[n_triangles];
		for (int i = 0; i < n_triangles; i++) {
			triangles_[i] = new Triangle();
		}
		this.index_ = index;
		this.n_triangles_ = n_triangles;
		complement_ = false;
	}

	public void Render() {
		for (int i = 0; i < n_triangles_; i++) {
			triangles_[i].Render(0, 1);
		}
	}

	public void CopyTriangles(final MCTemplate source) {
		if (source.n_triangles_ != n_triangles_) {
			return;
		}
		for (int i = 0; i < n_triangles_; i++) {
			triangles_[i].normal_.x_ = source.triangles_[i].normal_.x_;
			triangles_[i].normal_.y_ = source.triangles_[i].normal_.y_;
			triangles_[i].normal_.z_ = source.triangles_[i].normal_.z_;
			for (int j = 0; j < 3; j++) {
				triangles_[i].vertices_[j].x_ = source.triangles_[i].vertices_[j].x_;
				triangles_[i].vertices_[j].y_ = source.triangles_[i].vertices_[j].y_;
				triangles_[i].vertices_[j].z_ = source.triangles_[i].vertices_[j].z_;
			}
		}
	}

	public MCTemplate RotateX(final double degrees) {
		for (int i = 0; i < n_triangles_; i++) {
			triangles_[i].RotateX(degrees);
		}
		return this;
	}

	public MCTemplate RotateY(final double degrees) {
		for (int i = 0; i < n_triangles_; i++) {
			triangles_[i].RotateY(degrees);
		}
		return this;
	}

	public MCTemplate RotateZ(final double degrees) {
		for (int i = 0; i < n_triangles_; i++) {
			triangles_[i].RotateZ(degrees);
		}
		return this;
	}

	public MCTemplate MirrorX() {
		for (int i = 0; i < n_triangles_; i++) {
			triangles_[i].MirrorX();
		}
		return this;
	}

	public MCTemplate MirrorY() {
		for (int i = 0; i < n_triangles_; i++) {
			triangles_[i].MirrorY();
		}
		return this;
	}

	public MCTemplate MirrorZ() {
		for (int i = 0; i < n_triangles_; i++) {
			triangles_[i].MirrorZ();
		}
		return this;
	}

	public void set_complement(final boolean complement) {
		if (complement_ != complement) {
			for (int i = 0; i < n_triangles_; i++) {
				Vector3 v = new Vector3();

				v.x_ = triangles_[i].vertices_[1].x_;
				v.y_ = triangles_[i].vertices_[1].y_;
				v.z_ = triangles_[i].vertices_[1].z_;

				triangles_[i].vertices_[1].x_ = triangles_[i].vertices_[2].x_;
				triangles_[i].vertices_[1].y_ = triangles_[i].vertices_[2].y_;
				triangles_[i].vertices_[1].z_ = triangles_[i].vertices_[2].z_;

				triangles_[i].vertices_[2].x_ = v.x_;
				triangles_[i].vertices_[2].y_ = v.y_;
				triangles_[i].vertices_[2].z_ = v.z_;

				triangles_[i].normal_.MirrorX().MirrorY().MirrorZ();
			}
		}
		complement_ = complement;
	}
}

package com.amaisu.ptoctreelibgdx;

public class Vector3 {
	public double x_;
	public double y_;
	public double z_;

	public Vector3() {
		x_ = y_ = z_ = 0;
	}

	String ToString() {
		String ss = "";
		ss = "(" + x_ + "," + y_ + "," + z_ + ")";
		return ss;
	}

	public Vector3 RotateX(final double degree) {
		final double radians = degree * Math.PI / 180.0f;
		final double kC = Math.cos(radians);
		final double kS = Math.sin(radians);
		final double oy = y_;
		y_ = (oy * kC) + (z_ * -kS);
		z_ = (oy * kS) + (z_ * kC);
		return this;
	}

	public Vector3 RotateY(final double degree) {
		final double radians = degree * Math.PI / 180.0f;
		final double kC = Math.cos(radians);
		final double kS = Math.sin(radians);
		final double ox = x_;
		x_ = (ox * kC) + (z_ * kS);
		z_ = (ox * -kS) + (z_ * kC);
		return this;
	}

	public Vector3 RotateZ(final double degree) {
		final double radians = degree * Math.PI / 180.0f;
		final double kC = Math.cos(radians);
		final double kS = Math.sin(radians);
		final double ox = x_;
		x_ = (ox * kC) + (y_ * -kS);
		y_ = (ox * kS) + (y_ * kC);
		return this;
	}

	public Vector3 Translate(final double x, final double y, final double z) {
		x_ += x;
		y_ += y;
		z_ += z;
		return this;
	}

	Vector3 Subtract(final Vector3 v) {
		Vector3 nv = new Vector3();
		nv.x_ = x_ - v.x_;
		nv.y_ = y_ - v.y_;
		nv.z_ = z_ - v.z_;
		return nv;
	}

	Vector3 Add(final Vector3 v) {
		Vector3 nv = new Vector3();
		nv.x_ = x_ + v.x_;
		nv.y_ = y_ + v.y_;
		nv.z_ = z_ + v.z_;
		return nv;
	}

	Vector3 CrossProduct(final Vector3 v) {
		Vector3 nv = new Vector3();
		nv.x_ = (y_ * v.z_) - (z_ * v.y_);
		nv.y_ = (z_ * v.x_) - (x_ * v.z_);
		nv.z_ = (x_ * v.y_) - (y_ * v.x_);
		return nv;
	}

	public Vector3 MirrorX() {
		x_ = -x_;
		return this;
	}

	public Vector3 MirrorY() {
		y_ = -y_;
		return this;
	}

	public Vector3 MirrorZ() {
		z_ = -z_;
		return this;
	}
}

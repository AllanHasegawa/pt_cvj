package com.amaisu.ptoctreelibgdx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Triangle {
	public Vector3[] vertices_;
	public Vector3 normal_;

	public Triangle() {
		normal_ = new Vector3();
		vertices_ = new Vector3[3];
		for (int i = 0; i < 3; i++) {
			vertices_[i] = new Vector3();
		}
	}

	public void CalculateNormal() {
		Vector3 u = vertices_[1].Subtract(vertices_[0]);
		Vector3 v = vertices_[2].Subtract(vertices_[0]);
		normal_ = v.CrossProduct(u);
	}

	public void Render(int posLoc, int normalLoc) {
		/*
		 * glBegin(GL_POLYGON); glColor3f(1, 0, 0); glNormal3f(normal_.x_,
		 * normal_.y_, normal_.z_); glVertex3f(vertices_[0].x_, vertices_[0].y_,
		 * vertices_[0].z_); glColor3f(0, 1, 0); glNormal3f(normal_.x_,
		 * normal_.y_, normal_.z_); glVertex3f(vertices_[1].x_, vertices_[1].y_,
		 * vertices_[1].z_); glColor3f(0, 0, 1); //glNormal3f(normal_.x_,
		 * normal_.y_, normal_.z_); glVertex3f(vertices_[2].x_, vertices_[2].y_,
		 * vertices_[2].z_); glEnd();
		 */

		float vVerticesFloat[] = { (float) vertices_[0].x_,
				(float) vertices_[0].y_, (float) vertices_[0].z_,
				(float) vertices_[1].x_, (float) vertices_[1].y_,
				(float) vertices_[1].z_, (float) vertices_[2].x_,
				(float) vertices_[2].y_, (float) vertices_[2].z_ };

		float verticesNormal[] = { (float) normal_.x_, (float) normal_.y_,
				(float) normal_.z_, (float) normal_.x_, (float) normal_.y_,
				(float) normal_.z_, (float) normal_.x_, (float) normal_.y_,
				(float) normal_.z_ };

		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 9);
		buffer.order(ByteOrder.nativeOrder());

		FloatBuffer vVertices = buffer.asFloatBuffer();
		vVertices.put(vVerticesFloat);
		vVertices.position(0);

		// Gdx.app.log("I", this.toString());

		// Load the vertex data
		Gdx.gl20.glVertexAttribPointer(posLoc, 3, GL20.GL_FLOAT, false, 0,
				buffer);
		Gdx.gl20.glEnableVertexAttribArray(posLoc);

		ByteBuffer buffer2 = ByteBuffer.allocateDirect(4 * 9);
		buffer.order(ByteOrder.nativeOrder());
		FloatBuffer vVertices2 = buffer2.asFloatBuffer();
		vVertices2.put(verticesNormal);
		vVertices2.position(0);
		Gdx.gl20.glVertexAttribPointer(normalLoc, 3, GL20.GL_FLOAT, false, 0,
				buffer);
		Gdx.gl20.glEnableVertexAttribArray(normalLoc);

		Gdx.gl20.glDrawArrays(GL20.GL_TRIANGLES, 0, 3);
	}

	public Triangle RotateX(final double degrees) {
		for (int i = 0; i < 3; i++) {
			vertices_[i].RotateX(degrees);
		}
		normal_.RotateX(degrees);
		return this;
	}

	public Triangle RotateY(final double degrees) {
		for (int i = 0; i < 3; i++) {
			vertices_[i].RotateY(degrees);
		}
		normal_.RotateY(degrees);
		return this;
	}

	public Triangle RotateZ(final double degrees) {
		for (int i = 0; i < 3; i++) {
			vertices_[i].RotateZ(degrees);
		}
		normal_.RotateZ(degrees);
		return this;
	}

	public Triangle Translate(final double x, final double y, final double z) {
		for (int i = 0; i < 3; i++) {
			vertices_[i].Translate(x, y, z);
		}
		return this;
	}

	public Triangle MirrorX() {
		for (int i = 0; i < 3; i++) {
			vertices_[i].MirrorX();
		}
		normal_.MirrorX();
		return this;
	}

	public Triangle MirrorY() {
		for (int i = 0; i < 3; i++) {
			vertices_[i].MirrorY();
		}
		normal_.MirrorY();
		return this;
	}

	public Triangle MirrorZ() {
		for (int i = 0; i < 3; i++) {
			vertices_[i].MirrorZ();
		}
		normal_.MirrorZ();
		return this;
	}

	public void CopyVertices(Triangle source) {
		for (int j = 0; j < 3; j++) {
			this.vertices_[j].x_ = source.vertices_[j].x_;
			this.vertices_[j].y_ = source.vertices_[j].y_;
			this.vertices_[j].z_ = source.vertices_[j].z_;
		}
		normal_.x_ = source.normal_.x_;
		normal_.y_ = source.normal_.y_;
		normal_.z_ = source.normal_.z_;
	}

	public String toString() {
		return "T[" + vertices_[0].ToString() + "," + vertices_[1].ToString()
				+ "," + vertices_[2].ToString() + "]";
	}
}

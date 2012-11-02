package com.amaisu.ptoctreelibgdx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;

public class OctreeLibgdx implements ApplicationListener {
	private int programObject;
	private ClassicOctree moctree_;
	private ArrayList<Triangle> mesh_;
	private float aspect;
	private ESTransform mMVPMatrix = new ESTransform();// Attribute locations
	private int mPositionLoc;
	// Uniform locations
	private int mMVPLoc;
	private int mNormalLoc;
	private int bc_y = 0;
	private int bc_y_d = 1;
	private long octreeCreationTime = 0;
	private long cornersMatrixTime = 0;
	private long meshCreationTime = 0;
	private long totalTimeStart = 0;
	private boolean finishApp = false;
	private int iterations = 0;
	private int octreeSize = 64;

	@Override
	public void create() {
		totalTimeStart = System.currentTimeMillis();
		
		Gdx.app.log("I", "Starting with Octree size " + octreeSize);
		mesh_ = new ArrayList<Triangle>();
		
		MarchingCubes mc = MarchingCubes.CreateMarchingCubes();

		String vShaderStr = "struct directional_light {"
				+ "vec3 direction;"
				+ "vec3 halfplane;"
				+ "vec4 ambient_color;"
				+ "vec4 diffuse_color;"
				+ "vec4 specular_color;"
				+ "};"
				+ "struct material_properties {"
				+ "vec4 ambient_color;"
				+ "vec4 diffuse_color;"
				+ "vec4 specular_color;"
				+ "float specular_exponent;"
				+ "};"
				+ "const float c_zero = 0.0;"
				+ "const float c_one = 1.0;"
				+ "uniform material_properties material;"
				+ "uniform directional_light light;"
				+ "uniform mat4 u_mvpMatrix;"
				+ "attribute vec4 a_position;"
				+ "attribute vec3 a_normal;"
				+ "varying vec4 a_color;"
				+ "vec4 calc_directional_light(vec3 normal) {"
				+ "vec4 computed_color = vec4(c_zero, c_zero, c_zero, c_zero);"
				+ "float ndotl;"
				+ "float ndoth;"
				+ "ndotl = max(c_zero, dot(normal, light.direction));"
				+ "ndoth = max(c_zero, dot(normal, light.halfplane));"
				+ "computed_color += (light.ambient_color * material.ambient_color);"
				+ "computed_color += (ndotl * light.diffuse_color * material.diffuse_color);"
				+ "if (ndoth > c_zero) {"
				+ "computed_color += (pow(ndoth, material.specular_exponent) * material.specular_color * light.specular_color);"
				+ "}" + "return computed_color;" + "}" + "void main() {"
				+ "gl_Position = u_mvpMatrix * a_position;"
				+ "a_color = vec4(1,0,0,1) * calc_directional_light(a_normal);"
				+ "}";
		String fShaderStr = "precision mediump float;"
				+ "varying vec4 a_color;" + "void main() \n" + "{ \n"
				+ " gl_FragColor = a_color; \n" + "} \n";
		int vertexShader;
		int fragmentShader;
		int programObject;

		// Load the vertex/fragment shaders
		vertexShader = LoadShader(vShaderStr, GL20.GL_VERTEX_SHADER);
		fragmentShader = LoadShader(fShaderStr, GL20.GL_FRAGMENT_SHADER);
		// Create the program object
		programObject = Gdx.gl20.glCreateProgram();
		if (programObject == 0)
			return; // 0;
		Gdx.gl20.glAttachShader(programObject, vertexShader);
		Gdx.gl20.glAttachShader(programObject, fragmentShader);
		// Bind vPosition to attribute 0
		// Gdx.gl20.glBindAttribLocation(programObject, 0, "vPosition");
		// Link the program
		Gdx.gl20.glLinkProgram(programObject);
		// Check the link status
		ByteBuffer b = ByteBuffer.allocateDirect(4);
		b.order(ByteOrder.nativeOrder());
		IntBuffer linked = b.asIntBuffer();
		Gdx.gl20.glGetProgramiv(programObject, GL20.GL_LINK_STATUS, linked);
		if (linked.get(0) == 0) {
			Gdx.app.log(
					"O",
					"Error Loading Shaders: "
							+ Gdx.gl20.glGetProgramInfoLog(programObject));

			/*
			 * GLint infoLen = 0; glGetProgramiv(programObject,
			 * GL_INFO_LOG_LENGTH, &infoLen); if (infoLen > 1) { char* infoLog =
			 * (char*) malloc(sizeof(char) * infoLen);
			 * glGetProgramInfoLog(programObject, infoLen, NULL, infoLog);
			 * Furai::LOG->LogE("Error linking program:\n%s\n", infoLog);
			 * free(infoLog); } Gdx.gl20.glDeleteProgram(programObject); return;
			 */
		}
		// Store the program object
		this.programObject = programObject;

		Gdx.gl20.glUseProgram(programObject);

		mPositionLoc = Gdx.gl20
				.glGetAttribLocation(programObject, "a_position");
		mNormalLoc = Gdx.gl20.glGetAttribLocation(programObject, "a_normal");

		if (mPositionLoc == -1) {
			System.exit(1);
		}
		if (mNormalLoc == -1) {
			System.exit(1);
		}
		mMVPLoc = Gdx.gl20.glGetUniformLocation(programObject, "u_mvpMatrix");

		// Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		// Gdx.gl20.glCullFace(GL20.GL_BACK);
		int lDirectionLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"light.direction");

		float lDirectionV[] = { 0, 0, 1 };

		ByteBuffer lDirectionB = ByteBuffer.allocateDirect(4 * 9);
		lDirectionB.order(ByteOrder.nativeOrder());

		FloatBuffer lDirectionF = lDirectionB.asFloatBuffer();
		lDirectionF.put(lDirectionV);
		lDirectionF.position(0);

		// Gdx.gl20.glUniformMatrix3fv(lDirectionLoc, 1, false, lDirectionF);

		Gdx.gl20.glUniform3f(lDirectionLoc, -0.25f, -0.25f, -0.25f);
		if (lDirectionLoc == -1) {
			System.exit(1);
		}

		int lHalfPlaneLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"light.halfplane");
		Gdx.gl20.glUniform3f(lHalfPlaneLoc, 0.25f, 0.25f, 0.25f);
		int lAmbientColorLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"light.ambient_color");
		Gdx.gl20.glUniform4f(lAmbientColorLoc, 0.0f, 0.0f, 0.0f, 1.0f);

		int lDiffuseColorLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"light.diffuse_color");
		Gdx.gl20.glUniform4f(lDiffuseColorLoc, 1.f, 1.f, 1.f, 1.f);
		int lSpecularColorLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"light.specular_color");
		Gdx.gl20.glUniform4f(lSpecularColorLoc, 1.f, 1.f, 1.f, 1.f);

		int matAmbientColorLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"material.ambient_color");
		Gdx.gl20.glUniform4f(matAmbientColorLoc, 0.f, 0.f, 0.f, 1.f);

		int matDiffuseColorLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"material.diffuse_color");
		Gdx.gl20.glUniform4f(matDiffuseColorLoc, 1.f, 1.f, 1.f, 1.f);
		int matSpecularColorLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"material.specular_color");
		Gdx.gl20.glUniform4f(matSpecularColorLoc, 1.f, 1.f, 1.f, 1.f);
		int matSpecularExpLoc = Gdx.gl20.glGetUniformLocation(programObject,
				"material.specular_exponent");
		Gdx.gl20.glUniform1f(matSpecularExpLoc, 1f);

		// OpenGL Setup
		Gdx.gl20.glClearColor(1f, 1f, 1f, 1);
		// Use the program object
	}

	@Override
	public void dispose() {
		long end = System.currentTimeMillis();
		Gdx.app.log("I", "Iterations: " + iterations);
		Gdx.app.log("I", "Octree created in " + octreeCreationTime);
		Gdx.app.log("I", "Corners Matrix created in " + cornersMatrixTime);
		Gdx.app.log("I", "Mesh created in " + meshCreationTime);
		Gdx.app.log("I", "Total Time: " + (end - totalTimeStart));
	}

	@Override
	public void render() {
		if (finishApp) {
			Gdx.app.exit();
			return;
		}

		ESTransform perspective = new ESTransform();
		ESTransform modelview = new ESTransform();

		// Compute a rotation angle based on time to rotate the cube

		// Compute the window aspect ratio

		// Generate a perspective matrix with a 60 degree FOV
		perspective.matrixLoadIdentity();
		perspective.perspective(60.0f, aspect, 1.0f, 2000.0f);

		// Generate a model view matrix to rotate/translate the cube
		modelview.matrixLoadIdentity();

		// Translate away from the viewer
		modelview.translate(-40.0f, -35.0f, -50.0f);

		// Rotate the cube
		// modelview.rotate(35, 1.0f, 0.0f, 0.0f);
		// modelview.rotate(35, 0.0f, 1.0f, 0.0f);

		// Compute the final MVP by multiplying the
		// modevleiw and perspective matrices together
		mMVPMatrix.matrixMultiply(modelview.get(), perspective.get());

		Gdx.gl20.glUniformMatrix4fv(mMVPLoc, 1, false,
				mMVPMatrix.getAsFloatBuffer());

		Gdx.gl20.glClear(GL10.GL_COLOR_BUFFER_BIT);

		this.mesh_ = new ArrayList<Triangle>();
		Gdx.gl20.glUseProgram(this.programObject);

		SetupOctree();
		CreateMesh();

		for (Triangle T : mesh_) {
			T.Render(mPositionLoc, mNormalLoc);
		}

		++iterations;

		if (iterations == 128) {
			finishApp = true;
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl20.glViewport(0, 0, width, height);
		aspect = (float) width / (float) height;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	private int LoadShader(String shaderSrc, int type) {
		int shader;
		// Create the shader object
		shader = Gdx.gl20.glCreateShader(type);
		if (shader == 0)
			return 0;
		// Load the shader source
		Gdx.gl20.glShaderSource(shader, shaderSrc);
		// Compile the shader
		Gdx.gl20.glCompileShader(shader);
		// Check the compile status
		ByteBuffer b = ByteBuffer.allocateDirect(4);
		b.order(ByteOrder.nativeOrder());
		IntBuffer compiled = b.asIntBuffer();
		Gdx.gl20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, compiled);
		if (compiled.get(0) == 0) {
			Gdx.app.log("O", "Error Compiling Shaders");
			b = ByteBuffer.allocateDirect(500);
			b.order(ByteOrder.nativeOrder());
			IntBuffer infoLen = b.asIntBuffer();
			Gdx.gl20.glGetShaderiv(shader, GL20.GL_INFO_LOG_LENGTH, infoLen);
			if (infoLen.get(0) != 0) {

				Gdx.app.log("E", "C: " + Gdx.gl20.glGetShaderInfoLog(shader));

				// char* infoLog = (char*) malloc(sizeof(char) * infoLen);
				// glGetShaderInfoLog(shader, infoLen, NULL, infoLog);
				// Furai::LOG->LogE("Error compiling shader:\n%s\n", infoLog);
				// free(infoLog);
			}
			Gdx.gl20.glDeleteShader(shader);
			return 0;
		}
		return shader;
	}

	
	private void SetupOctree() {
		long begin = System.currentTimeMillis();
		int t_size = octreeSize;

		moctree_ = new ClassicOctree(t_size);

		Vector3 p0 = new Vector3();
		Vector3 p1 = new Vector3();
		Vector3 p2 = new Vector3();
		Vector3 bc = new Vector3();
		Vector3 sphere = new Vector3();

		p0.x_ = 0;
		p0.y_ = 0;
		p0.z_ = 0;
		p1.x_ = t_size / 4;
		p1.y_ = bc_y - 1;
		p1.z_ = t_size - 1;
		p2.x_ = t_size - 1;
		p2.y_ = 0;
		p2.z_ = 0;

		int n_cells = 0;

		for (double t = 0; t <= 1; t += 0.001) {
			bc.x_ = (1 - t) * ((1 - t) * p0.x_ + t * p1.x_) + t
					* ((1 - t) * p1.x_ + t * p2.x_);
			bc.y_ = (1 - t) * ((1 - t) * p0.y_ + t * p1.y_) + t
					* ((1 - t) * p1.y_ + t * p2.y_);
			bc.z_ = (1 - t) * ((1 - t) * p0.z_ + t * p1.z_) + t
					* ((1 - t) * p1.z_ + t * p2.z_);

			int radius_size = 3;
			// for (double r = 0; r < radius_size; r += 1) {
			for (double alpha = 0; alpha < 2 * Math.PI; alpha += 0.3) {
				for (double beta = 0; beta < Math.PI; beta += 0.3) {
					sphere.x_ = radius_size * Math.cos(alpha) * Math.sin(beta);
					sphere.y_ = radius_size * Math.sin(alpha) * Math.sin(beta);
					sphere.z_ = radius_size * Math.cos(beta);
					moctree_.InsertCell((int) (sphere.x_ + bc.x_),
							(int) (sphere.y_ + bc.y_),
							(int) (sphere.z_ + bc.z_), 1);
					n_cells++;
				}
			}
		}

		bc_y += bc_y_d;
		if (bc_y >= t_size || bc_y <= 0) {
			bc_y_d *= -1;
			bc_y += bc_y_d;
		}

		long end = System.currentTimeMillis();
		octreeCreationTime += (end - begin);
	}

	private void CreateMesh() {
		long begin = System.currentTimeMillis();
		MarchingCubes mc = MarchingCubes.CreateMarchingCubes();

		// Gdx.app.log("I", mc.GetTemplate(1).triangles_[0].toString());

		int size = moctree_.size();

		// Gdx.app.log("I", "Creating Corners Matrix...\n");
		// +2 == Border
		// Each cell is displaced +1 in each axis
		// bool corners[size + 2][size + 2][size + 2];
		int corners[][][] = new int[size + 2][size + 2][size + 2];
		for (int x = 0; x < size + 2; x++) {
			for (int y = 0; y < size + 2; y++) {
				for (int z = 0; z < size + 2; z++) {
					corners[x][y][z] = 0;
				}
			}
		}

		// EACH OCTREE CELL == 1 CORNER!
		MOctreeCell o_cell = moctree_.GetCell(0, 0, 0);

		int x = 0;
		int y = 0;
		int z = 0;
		int d_x = 1;
		int d_y = 1;

		for (z = 0; z < size - 1; z++) {
			// if o_cell.z_ is power of two
			/*
			 * if (!(((int) (o_cell.z_ - 1)) & (int) (o_cell.z_))) {
			 * printf("Meshing: (%d/%d)\n", o_cell.z_, size); }
			 */
			while (true) {
				while (true) {
					corners[x + 1][y + 1][z + 1] = (o_cell.getData() != MOctreeCell.DATA_EMPTY) ? 1
							: 0;
					x += d_x;
					if (x >= 0 && x < size) {
						o_cell = moctree_.GetNeighbor(o_cell, d_x, 0, 0);
					} else {
						d_x *= -1;
						x += d_x;
						break;
					}
				}
				y += d_y;
				if (y >= 0 && y < size) {
					o_cell = moctree_.GetNeighbor(o_cell, 0, d_y, 0);
				} else {
					d_y *= -1;
					y += d_y;
					break;
				}
			}
			o_cell = moctree_.GetNeighbor(o_cell, 0, 0, 1);
		}

		long end = System.currentTimeMillis();
		cornersMatrixTime += (end - begin);
		begin = System.currentTimeMillis();

		// Corners Matrix To Mesh
		for (z = 0; z < size + 1; z += 1) {
			for (y = 0; y < size + 1; y += 1) {
				for (x = 0; x < size + 1; x += 1) {
					MCTemplate cube = mc.GetTemplate(corners[x][y][z],
							corners[x + 1][y][z], corners[x][y + 1][z],
							corners[x + 1][y + 1][z], corners[x][y][z + 1],
							corners[x + 1][y][z + 1], corners[x][y + 1][z + 1],
							corners[x + 1][y + 1][z + 1]);
					if (cube == null) {
						/*
						 * printf("FUUUUUUUUU %d %d %d %d %d %d %d %d\n",
						 * corners[x][y][z], corners[x + 1][y][z], corners[x][y
						 * + 1][z], corners[x + 1][y + 1][z], corners[x][y][z +
						 * 1], corners[x + 1][y][z + 1], corners[x][y + 1][z +
						 * 1], corners[x + 1][y + 1][z + 1]);
						 */
						Gdx.app.log(
								"I",
								"FU: "
										+ MarchingCubes.CornersToIndex(
												corners[x][y][z],
												corners[x + 1][y][z],
												corners[x][y + 1][z],
												corners[x + 1][y + 1][z],
												corners[x][y][z + 1],
												corners[x + 1][y][z + 1],
												corners[x][y + 1][z + 1],
												corners[x + 1][y + 1][z + 1]));
						continue;
					}
					for (int t_n = 0; t_n < cube.n_triangles_; t_n++) {
						Triangle t = new Triangle();
						t.CopyVertices(cube.triangles_[t_n]);
						t.Translate(x * 1.0 - 0.5f, y * 1.0 - 0.5f, z * (-1.0));
						// Gdx.app.log("I", t.toString());
						mesh_.add(t);
					}
				}
			}
		}
		/*
		 * end = std::clock();
		 * printf("Mesh completed in %f seconds with %d triangles...\n",
		 * double(end - begin) / CLOCKS_PER_SEC, mesh_.size());
		 */
		end = System.currentTimeMillis();
		meshCreationTime += (end - begin);
	}
}

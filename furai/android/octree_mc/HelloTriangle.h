/*
 -----------------------------------------------------------------------------
 This source file is part of the Project Furai.
 For more information, go to:
 http://code.google.com/p/furai/
 -----------------------------------------------------------------------------

 Copyright 2012 Allan Yoshio Hasegawa

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 -----------------------------------------------------------------------------
 */

#ifndef FURAI_HELLOTRI_H_
#define FURAI_HELLOTRI_H_

#include <stdio.h>
#include <vector>

#include <GLES2/gl2.h>
#include <furai/backends/android/core/AndroidApplication.h>
#include <furai/core/WindowListener.h>
#include <furai/core/Furai.h>
#include <furai/storage/InternalFile.h>
#include <moctree/MOctree.h>
#include <moctree/classic/ClassicOctree.h>
#include <MarchingCubes.h>
#include <esTransform.h>

class HelloTriangle : public furai::WindowListener {
 private:
  int programObject;
  moctree::MOctree<int>* moctree_;
  int moctree_data_;
  std::vector<Triangle> mesh_;
  float aspect;
  ESMatrix mMVPMatrix;
  // Attribute locations
  int mPositionLoc;
  int mNormalLoc;
  // Uniform locations
  int mMVPLoc;
  int bc_y;
  int bc_y_d;
  double octreeCreationTime;
  double cornersMatrixTime;
  double meshCreationTime;
  double totalTimeStart;
  bool finishApp;
  int iterations;
  int octreeSize;
 public:
  HelloTriangle() {
    moctree_ = NULL;
    moctree_data_ = 1;
    bc_y = 0;
    bc_y_d = 1;
    octreeCreationTime = 0;
    cornersMatrixTime = 0;
    meshCreationTime = 0;
    totalTimeStart = 0;
    finishApp = false;
    iterations = 0;
    octreeSize = 64;
  }

  ~HelloTriangle() {

  }

  virtual void OnStart() {
    using namespace furai;

    totalTimeStart = Furai::CLOCK->NowMS();


    MarchingCubes* mc = MarchingCubes::CreateMarchingCubes();

    this->counter_ = 0;

    GLchar vShaderStr[] =
        "struct directional_light {"
            "vec3 direction;"
            "vec3 halfplane;"
            "vec4 ambient_color;"
            "vec4 diffuse_color;"
            "vec4 specular_color;"
            "};"
            "struct material_properties {"
            "vec4 ambient_color;"
            "vec4 diffuse_color;"
            "vec4 specular_color;"
            "float specular_exponent;"
            "};"
            "const float c_zero = 0.0;"
            "const float c_one = 1.0;"
            "uniform material_properties material;"
            "uniform directional_light light;"
            "uniform mat4 u_mvpMatrix;"
            "attribute vec4 a_position;"
            "attribute vec3 a_normal;"
            "varying vec4 a_color;"
            "vec4 calc_directional_light(vec3 normal) {"
            "vec4 computed_color = vec4(c_zero, c_zero, c_zero, c_zero);"
            "float ndotl;"
            "float ndoth;"
            "ndotl = max(c_zero, dot(normal, light.direction));"
            "ndoth = max(c_zero, dot(normal, light.halfplane));"
            "computed_color += (light.ambient_color * material.ambient_color);"
            "computed_color += (ndotl * light.diffuse_color * material.diffuse_color);"
            "if (ndoth > c_zero) {"
            "computed_color += (pow(ndoth, material.specular_exponent) * material.specular_color * light.specular_color);"
            "}" "return computed_color;" "}" "void main() {"
            "gl_Position = u_mvpMatrix * a_position;"
            "a_color = vec4(1,0,0,1) * calc_directional_light(a_normal);"
            "}";
    GLchar fShaderStr[] = "precision mediump float; varying vec4 a_color;\n"
        "void main() \n"
        "{ \n"
        " gl_FragColor = a_color; \n"
        "} \n";
    GLuint vertexShader;
    GLuint fragmentShader;
    GLuint programObject;
    GLint linked;

    // Load the vertex/fragment shaders
    vertexShader = this->LoadShader(vShaderStr, GL_VERTEX_SHADER);
    fragmentShader = this->LoadShader(fShaderStr, GL_FRAGMENT_SHADER);
    // Create the program object
    programObject = glCreateProgram();
    if (programObject == 0)
      return;  // 0;
    glAttachShader(programObject, vertexShader);
    glAttachShader(programObject, fragmentShader);
    // Bind vPosition to attribute 0
    glBindAttribLocation(programObject, 0, "vPosition");
    // Link the program
    glLinkProgram(programObject);
    // Check the link status
    glGetProgramiv(programObject, GL_LINK_STATUS, &linked);
    if (!linked) {
      GLint infoLen = 0;
      glGetProgramiv(programObject, GL_INFO_LOG_LENGTH, &infoLen);
      if (infoLen > 1) {
        char* infoLog = (char*) malloc(sizeof(char) * infoLen);
        glGetProgramInfoLog(programObject, infoLen, NULL, infoLog);
        Furai::LOG->LogE("Error linking program:\n%s\n", infoLog);
        free(infoLog);
      }
      glDeleteProgram(programObject);
      return;
    }
    // Store the program object
    this->programObject_ = programObject;

    glUseProgram(programObject);
    glDisable(GL_CULL_FACE);

    mPositionLoc = glGetAttribLocation(programObject, "a_position");
    mNormalLoc = glGetAttribLocation(programObject, "a_normal");

    if (mPositionLoc == -1) {
      Furai::LOG->LogE("mPosLoc");
    }
    if (mNormalLoc == -1) {
      Furai::LOG->LogE("mNormLoc");
    }
    mMVPLoc = glGetUniformLocation(programObject, "u_mvpMatrix");

    //glEnable(GL_CULL_FACE);
    //glCullFace(GL_BACK);
    int lDirectionLoc = glGetUniformLocation(programObject, "light.direction");

    // Gdx.gl20.glUniformMatrix3fv(lDirectionLoc, 1, false, lDirectionF);

    glUniform3f(lDirectionLoc, -0.25f, -0.25f, -0.25f);
    if (lDirectionLoc == -1) {
      Furai::LOG->LogE("lDireLoc");
    }

    int lHalfPlaneLoc = glGetUniformLocation(programObject, "light.halfplane");
    glUniform3f(lHalfPlaneLoc, 0.25f, 0.25f, 0.25f);
    int lAmbientColorLoc = glGetUniformLocation(programObject,
                                                "light.ambient_color");
    glUniform4f(lAmbientColorLoc, 0.0f, 0.0f, 0.0f, 1.0f);

    int lDiffuseColorLoc = glGetUniformLocation(programObject,
                                                "light.diffuse_color");
    glUniform4f(lDiffuseColorLoc, 1.f, 1.f, 1.f, 1.f);
    int lSpecularColorLoc = glGetUniformLocation(programObject,
                                                 "light.specular_color");
    glUniform4f(lSpecularColorLoc, 1.f, 1.f, 1.f, 1.f);

    int matAmbientColorLoc = glGetUniformLocation(programObject,
                                                  "material.ambient_color");
    glUniform4f(matAmbientColorLoc, 0.f, 0.f, 0.f, 1.f);

    int matDiffuseColorLoc = glGetUniformLocation(programObject,
                                                  "material.diffuse_color");
    glUniform4f(matDiffuseColorLoc, 1.f, 1.f, 1.f, 1.f);
    int matSpecularColorLoc = glGetUniformLocation(programObject,
                                                   "material.specular_color");
    glUniform4f(matSpecularColorLoc, 1.f, 1.f, 1.f, 1.f);
    int matSpecularExpLoc = glGetUniformLocation(programObject,
                                                 "material.specular_exponent");
    glUniform1f(matSpecularExpLoc, 1.f);

    // OpenGL Setup
    glClearColor(1.f, 1.f, 1.f, 1);

  }

  virtual void OnDraw(const double delta_time) {
    using namespace furai;
    if (finishApp) {
      (static_cast<AndroidApplication*>(Furai::APP))->Exit();
      return;
    }

    MarchingCubes* mc = MarchingCubes::CreateMarchingCubes();
    Furai::LOG->LogI("%d", mc->IndexMirrorX(mc->IndexRotateY(mc->CornersToIndex(1, 1, 0, 0, 0, 0, 0,0), 90)));

    ESMatrix perspective;
    ESMatrix modelview;

    esMatrixLoadIdentity(&perspective);
    esPerspective(&perspective, 60.0f, aspect, 1.0f, 2000.0f);

    // Generate a model view matrix to rotate/translate the cube
    esMatrixLoadIdentity(&modelview);

    // Translate away from the viewer
    esTranslate(&modelview, -40.0f, -35.0f, -50.0f);

    // Rotate the cube
    // modelview.rotate(35, 1.0f, 0.0f, 0.0f);
    // modelview.rotate(35, 0.0f, 1.0f, 0.0f);

    // Compute the final MVP by multiplying the
    // modevleiw and perspective matrices together
    esMatrixMultiply(&mMVPMatrix, &modelview, &perspective);

    glUniformMatrix4fv(mMVPLoc, 1, GL_FALSE, (GLfloat*) &(mMVPMatrix.m[0][0]));

    glClear(GL_COLOR_BUFFER_BIT);

    mesh_.clear();

    SetupOctree();
    CreateMesh();

    for (std::vector<Triangle>::iterator i = mesh_.begin(); i != mesh_.end();
        i++) {
      i->Render(mPositionLoc, mNormalLoc);
    }

    ++iterations;

    if (iterations == 128) {
      finishApp = true;
    }
  }

  virtual void OnResize(const GLint width, const GLint height) {
    glViewport(0, 0, width, height);
    aspect = (float) width / (float) height;
  }

  virtual void OnDestroy() {
    using namespace furai;
    double end = Furai::CLOCK->NowMS();
    Furai::LOG->LogI("Size: %d", octreeSize);
    Furai::LOG->LogI("Octree created in %g", octreeCreationTime);
    Furai::LOG->LogI("Corners created in %g", cornersMatrixTime);
    Furai::LOG->LogI("Mesh created in %g", meshCreationTime);
    Furai::LOG->LogI("TotalTime in %g", (end-totalTimeStart));
  }

  virtual void OnFocusGained() {
    furai::Furai::LOG->LogV("OnFocusGained\n");
  }
  virtual void OnFocusLost() {
    furai::Furai::LOG->LogV("OnFocusLost\n");
  }

 private:
  GLfloat counter_;
  GLuint programObject_;

  GLuint LoadShader(const char *shaderSrc, GLenum type) {
    using namespace furai;

    GLuint shader;
    GLint compiled;
    // Create the shader object
    shader = glCreateShader(type);
    if (shader == 0)
      return 0;
    // Load the shader source
    glShaderSource(shader, 1, &shaderSrc, NULL);
    // Compile the shader
    glCompileShader(shader);
    // Check the compile status
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
    if (!compiled) {
      GLint infoLen = 0;
      glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
      if (infoLen > 1) {
        char* infoLog = (char*) malloc(sizeof(char) * infoLen);
        glGetShaderInfoLog(shader, infoLen, NULL, infoLog);
        Furai::LOG->LogE("Error compiling shader:\n%s\n", infoLog);
        free(infoLog);
      }
      glDeleteShader(shader);
      return 0;
    }
    return shader;
  }

 private:
  void SetupOctree() {
    using namespace furai;

    double begin = Furai::CLOCK->NowMS();

    int t_size = octreeSize;

    moctree_ = new moctree::ClassicOctree<int>(t_size);

    Vector3 p0;
    Vector3 p1;
    Vector3 p2;
    Vector3 bc;
    Vector3 sphere;

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
      bc.x_ = (1 - t) * ((1 - t) * p0.x_ + t * p1.x_)
          + t * ((1 - t) * p1.x_ + t * p2.x_);
      bc.y_ = (1 - t) * ((1 - t) * p0.y_ + t * p1.y_)
          + t * ((1 - t) * p1.y_ + t * p2.y_);
      bc.z_ = (1 - t) * ((1 - t) * p0.z_ + t * p1.z_)
          + t * ((1 - t) * p1.z_ + t * p2.z_);

      int radius_size = 3;
      // for (double r = 0; r < radius_size; r += 1) {
      for (double alpha = 0; alpha < 2 * M_PI; alpha += 0.3) {
        for (double beta = 0; beta < M_PI; beta += 0.3) {
          sphere.x_ = radius_size * cos(alpha) * sin(beta);
          sphere.y_ = radius_size * sin(alpha) * sin(beta);
          sphere.z_ = radius_size * cos(beta);
          moctree_->InsertCell((int) (sphere.x_ + bc.x_),
                               (int) (sphere.y_ + bc.y_),
                               (int) (sphere.z_ + bc.z_), &moctree_data_);
          n_cells++;
        }
      }
    }

    bc_y += bc_y_d;
    if (bc_y >= t_size || bc_y <= 0) {
      bc_y_d *= -1;
      bc_y += bc_y_d;
    }

    double end = Furai::CLOCK->NowMS();
    octreeCreationTime += (end - begin);
  }

  void CreateMesh() {
    using namespace furai;

    double begin = Furai::CLOCK->NowMS();
    MarchingCubes* mc = MarchingCubes::CreateMarchingCubes();

    // Gdx.app.log("I", mc.GetTemplate(1).triangles_[0].toString());

    int size = moctree_->size();

    // +2 == Border
    // Each cell is displaced +1 in each axis
    // bool corners[size + 2][size + 2][size + 2];
    bool ***corners;
    corners = new bool**[size + 2];
    for (int i = 0; i < size + 2; i++) {
      corners[i] = new bool*[size + 2];
      for (int j = 0; j < size + 2; j++) {
        corners[i][j] = new bool[size + 2];
        for (int k = 0; k < size + 2; k++) {
          corners[i][j][k] = false;
        }
      }
    }

    // EACH OCTREE CELL == 1 CORNER!
    moctree::MOctreeCell<int> o_cell = moctree_->GetCell(0, 0, 0);

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
          corners[x + 1][y + 1][z + 1] = o_cell.data_ != NULL;
          x += d_x;
          if (x >= 0 && x < size) {
            o_cell = moctree_->GetNeighbor(o_cell, d_x, 0, 0);
          } else {
            d_x *= -1;
            x += d_x;
            break;
          }
        }
        y += d_y;
        if (y >= 0 && y < size) {
          o_cell = moctree_->GetNeighbor(o_cell, 0, d_y, 0);
        } else {
          d_y *= -1;
          y += d_y;
          break;
        }
      }
      o_cell = moctree_->GetNeighbor(o_cell, 0, 0, 1);
    }

    delete moctree_;
    moctree_ = NULL;

    double end = Furai::CLOCK->NowMS();
    cornersMatrixTime += (end - begin);
    begin = Furai::CLOCK->NowMS();

    // Corners Matrix To Mesh
    for (z = 0; z < size + 1; z += 1) {
      for (y = 0; y < size + 1; y += 1) {
        for (x = 0; x < size + 1; x += 1) {
          MCTemplate* cube = mc->GetTemplate(corners[x][y][z],
                                             corners[x + 1][y][z],
                                             corners[x][y + 1][z],
                                             corners[x + 1][y + 1][z],
                                             corners[x][y][z + 1],
                                             corners[x + 1][y][z + 1],
                                             corners[x][y + 1][z + 1],
                                             corners[x + 1][y + 1][z + 1]);
          if (cube == NULL) {
            /*
             * printf("FUUUUUUUUU %d %d %d %d %d %d %d %d\n",
             * corners[x][y][z], corners[x + 1][y][z], corners[x][y
             * + 1][z], corners[x + 1][y + 1][z], corners[x][y][z +
             * 1], corners[x + 1][y][z + 1], corners[x][y + 1][z +
             * 1], corners[x + 1][y + 1][z + 1]);
             */
            Furai::LOG->LogE(
                "FU: %d",
                MarchingCubes::CornersToIndex(corners[x][y][z],
                                              corners[x + 1][y][z],
                                              corners[x][y + 1][z],
                                              corners[x + 1][y + 1][z],
                                              corners[x][y][z + 1],
                                              corners[x + 1][y][z + 1],
                                              corners[x][y + 1][z + 1],
                                              corners[x + 1][y + 1][z + 1]));
            continue;
          }
          for (int t_n = 0; t_n < cube->n_triangles_; t_n++) {
            Triangle t;
            t = cube->triangles_[t_n];
            t.Translate(x * 1.0, y * 1.0, z * (-1.0));
            // Gdx.app.log("I", t.toString());
            mesh_.push_back(t);
          }
        }
      }
    }
    /*
     * end = std::clock();
     * printf("Mesh completed in %f seconds with %d triangles...\n",
     * double(end - begin) / CLOCKS_PER_SEC, mesh_.size());
     */
    for (int i = 0; i < size + 2; i++) {
      for (int j = 0; j < size + 2; j++) {
        delete[] (corners[i][j]);
      }
      delete[] (corners[i]);
    }
    delete[] corners;

    end = Furai::CLOCK->NowMS();
    meshCreationTime += (end - begin);
  }
};

#endif /* FURAI_HELLOTRI_H_ */

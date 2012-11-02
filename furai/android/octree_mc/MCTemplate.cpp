/*
 * Copyright (c) 2012 Allan Yoshio Hasegawa
 * 
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall
 * be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

#include <MCTemplate.h>

MCTemplate::MCTemplate(const int n_triangles, const int index)
    : n_triangles_(n_triangles),
      index_(index),
      complement_(false) {
  triangles_ = new Triangle[n_triangles];
}

MCTemplate::~MCTemplate() {
  delete[] triangles_;
}

void MCTemplate::Render() {
  for (int i = 0; i < n_triangles_; i++) {
    //triangles_[i].Render();
  }
}

void MCTemplate::CopyTriangles(const MCTemplate& source) {
  if (source.n_triangles_ != n_triangles_) {
    return;
  }
  for (int i = 0; i < n_triangles_; i++) {
    triangles_[i] = source.triangles_[i];
  }
}

MCTemplate* MCTemplate::RotateX(const double degrees) {
  for (int i = 0; i < n_triangles_; i++) {
    triangles_[i].RotateX(degrees);
  }
  return this;
}

MCTemplate* MCTemplate::RotateY(const double degrees) {
  for (int i = 0; i < n_triangles_; i++) {
    triangles_[i].RotateY(degrees);
  }
  return this;
}

MCTemplate* MCTemplate::RotateZ(const double degrees) {
  for (int i = 0; i < n_triangles_; i++) {
    triangles_[i].RotateZ(degrees);
  }
  return this;
}

MCTemplate* MCTemplate::MirrorX() {
  for (int i = 0; i < n_triangles_; i++) {
    triangles_[i].MirrorX();
  }
  return this;
}

MCTemplate* MCTemplate::MirrorY() {
  for (int i = 0; i < n_triangles_; i++) {
    triangles_[i].MirrorY();
  }
  return this;
}

MCTemplate* MCTemplate::MirrorZ() {
  for (int i = 0; i < n_triangles_; i++) {
    triangles_[i].MirrorZ();
  }
  return this;
}

void MCTemplate::set_complement(const bool complement) {
  if (complement_ != complement) {
    for (int i = 0; i < n_triangles_; i++) {
      triangles_[i].normal_.MirrorX()->MirrorY()->MirrorZ();
    }
  }
  complement_ = complement;
}

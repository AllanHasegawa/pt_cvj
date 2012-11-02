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

#ifndef FURAI_ESTRANSFORM_H_
#define FURAI_ESTRANSFORM_H_

#include <GLES2/gl2.h>

#define ESUTIL_API

typedef struct
{
    GLfloat   m[4][4];
} ESMatrix;

void esScale(ESMatrix *result, GLfloat sx, GLfloat sy, GLfloat sz);

void
esTranslate(ESMatrix *result, GLfloat tx, GLfloat ty, GLfloat tz);

void ESUTIL_API
esRotate(ESMatrix *result, GLfloat angle, GLfloat x, GLfloat y, GLfloat z);

void ESUTIL_API
esFrustum(ESMatrix *result, float left, float right, float bottom, float top,
          float nearZ, float farZ);

void ESUTIL_API
esPerspective(ESMatrix *result, float fovy, float aspect, float nearZ,
              float farZ);

void ESUTIL_API
esOrtho(ESMatrix *result, float left, float right, float bottom, float top,
        float nearZ, float farZ);

void ESUTIL_API
esMatrixMultiply(ESMatrix *result, ESMatrix *srcA, ESMatrix *srcB);

void ESUTIL_API
esMatrixLoadIdentity(ESMatrix *result);

#endif /* FURAI_ESTRANSFORM_H_ */

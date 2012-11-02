cmake_minimum_required(VERSION 2.8)

project(furai-octree)

###
# Using precompiled furai_backend_android library!
###
add_library(furai_backend_android STATIC IMPORTED)
set_property(TARGET furai_backend_android PROPERTY
	IMPORTED_LOCATION
	${CMAKE_BINARY_DIR}/libs/armeabi-v7a/libfurai_backend_android.a
	)


set(JNIGLUE_ROOT "${CMAKE_SOURCE_DIR}/../../../external/android")

include_directories(
    ${CMAKE_SOURCE_DIR}
	${CMAKE_SOURCE_DIR}/../../../sourcetree
	${CMAKE_SOURCE_DIR}/../../../external/boost/boost-1_49_0_android/build/include/boost-1_49
	${JNIGLUE_ROOT}
	)

set(SRCS
	android_main.cpp
	${JNIGLUE_ROOT}/native_app_glue/android_native_app_glue.c
	MarchingCubes.cpp
	MCTemplate.cpp
	Triangle.cpp
	MarchingCubesUniques.cpp
    esTransform.cpp
	)

set(LIBRARY_DEPS
        log
        android
        EGL
        #GLESv1_CM
        GLESv2
        stdc++
        m
        furai_backend_android
        )
        
        #-mthumb -O3 -fomit-frame-pointer -DNDEBUG
set(CMAKE_BUILD_TYPE "Release")
set(CMAKE_CXX_FLAGS_RELEASE "-mthumb -O3 -fomit-frame-pointer -DNDEBUG") 

add_library(furai_sample SHARED ${SRCS})
target_link_libraries(
	furai_sample
	${LIBRARY_DEPS}
	)

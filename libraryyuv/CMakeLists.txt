# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add.library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

add_library( # Specifies the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

include_directories(src/main/cpp/include)
set(distribution_DIR ${CMAKE_SOURCE_DIR}/../../../../src/main/jniLibs)
#### libyuv  start###
 add_library( # Sets the name of the library.
              yuv

              # Sets the library as ashared library.
              SHARED

              # Provides a relative pathto your source file(s).
              IMPORTED )

 set_target_properties(
             yuv
             PROPERTIES IMPORTED_LOCATION

            ../../../../src/main/jniLibs/${ANDROID_ABI}/libyuv.so
            )
#### libyuv  end ###


 target_link_libraries( # Specifies the target library.
                        native-lib  yuv

                        # Links the targetlibrary to the log library
                        # included in theNDK.
                        ${log-lib} )
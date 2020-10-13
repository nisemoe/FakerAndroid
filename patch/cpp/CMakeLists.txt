# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
add_definitions(-DENABLE_DEBUG -DABC)
cmake_minimum_required(VERSION 3.5)
include_directories(src/main/cpp/include)

if(${ANDROID_ABI} MATCHES "arm64-v8a")
    file(GLOB Scaffolding "${CMAKE_SOURCE_DIR}/Scaffolding-ARM64/*.cpp")
elseif(${ANDROID_ABI} MATCHES  "armeabi-v7a")
    file(GLOB Scaffolding "${CMAKE_SOURCE_DIR}/Scaffolding-ARM/*.cpp")
endif()

file(GLOB native_srcc "${CMAKE_SOURCE_DIR}/*.cpp")

add_library(
        swap #最终在build中生成的so名字
        SHARED
        IMPORTED)
set_target_properties(
        swap #最终在build中生成的so名字
        PROPERTIES IMPORTED_LOCATION
        ../../../../src/main/jniLibs/${ANDROID_ABI}/libswap.so) #so
add_library(
        faker #最终在build中生成的so名字
        SHARED
        IMPORTED)
set_target_properties(
        faker #最终在build中生成的so名字
        PROPERTIES IMPORTED_LOCATION
        ../../../../src/main/jniLibs/${ANDROID_ABI}/libfaker.so) #so

add_library( # Sets the name of the library.
        native-lib
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        ${native_srcc}
        ${Scaffolding}
        ${Utils}
        )
# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)
# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib
        swap
        faker
        android
        ${log-lib})
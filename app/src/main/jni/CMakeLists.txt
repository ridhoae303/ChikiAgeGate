cmake_minimum_required(VERSION 3.10)

project("ChikiAgeGate")

set(CMAKE_C_STANDARD 11)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

set(COMMON_C_FLAGS
    -Os
    -w
    -s
    -fdata-sections
    -ffunction-sections
    -fvisibility=hidden
    -fno-unwind-tables
    -fno-asynchronous-unwind-tables
    -fomit-frame-pointer
    -fmerge-all-constants
    -fstack-protector-strong
    -fpermissive
    -fexceptions
    -Wno-error=format-security
)

set(COMMON_CXX_FLAGS
    ${COMMON_C_FLAGS}
    -fvisibility-inlines-hidden
    -std=c++17
    -Werror
    -Wno-error=c++11-narrowing
    -Wall
)

add_compile_options(
    $<$<COMPILE_LANGUAGE:C>:${COMMON_C_FLAGS}>
    $<$<COMPILE_LANGUAGE:CXX>:${COMMON_CXX_FLAGS}>
)

add_library(makigate SHARED
    ChikiVerification.cpp
)

target_include_directories(makigate PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(makigate
    android
    dl
    EGL
    GLESv2
    z
)

target_link_options(makigate PRIVATE
    -Wl,--gc-sections
    -Wl,--strip-all
    -Wl,--exclude-libs,ALL
    -Wl,-z,relro
    -Wl,-z,now
    -Wl,-z,noexecstack
    -Wl,-z,nodlopen
    -Wl,-z,nodump
    -Wl,-z,max-page-size=16384
)

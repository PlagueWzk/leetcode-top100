#include "io_github_plaguewzk_leetplague_learn_jni_NativeDemo.h"
JNIEXPORT jint JNICALL Java_io_github_plaguewzk_leetplague_learn_jni_NativeDemo_addCpp(
    JNIEnv* env,
    jclass clazz,
    jint a,
    jint b
) {
    return a + b;
}
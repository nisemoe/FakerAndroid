#include <jni.h>
#include <string>
#include "Common.h"
#include <fstream>
#include <sstream>
#include "Constant.h"
using namespace std;
#include "include/faker.h"
JavaVM *global_jvm;

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if ((*vm).GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) == JNI_OK) {
        LOGI("JNI_OnLoad %s","sucess");
    }else{
        LOGI("JNI_OnLoad %s","erro");
    }
    global_jvm = vm;
    onJniLoad(vm,reserved);
    return JNI_VERSION_1_6;
}

static jobject tmpAct;

void callJava(const char *event) {
    JNIEnv* env;
    global_jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_4);
    jclass jclass1 = env->FindClass("com/faker/android/FakerActivity");
    jmethodID jmethodID1 = env->GetMethodID(jclass1, "onCall", "(Ljava/lang/String;)V");
    jstring enventStr = env->NewStringUTF(event);
    env->CallVoidMethod(tmpAct, jmethodID1, enventStr);
    env->DeleteLocalRef(enventStr);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_faker_android_FakerApp_fakeApp(JNIEnv *env, jobject thiz, jobject application,jboolean b) {
    return fakeApp(env,thiz,application,b);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_faker_android_FakerApp_fakeDex(JNIEnv *env, jobject thiz, jobject base) {
    fakeDex(env,thiz,base,"target.dfk");//安装DEX
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_faker_android_FakerActivity_init(JNIEnv *env, jobject thiz) {//替换函数
    tmpAct = env->NewGlobalRef(thiz);
    long base = baseAddr("libil2cpp.so");
    //对指定的so 函数进行hook
    //fakeCpp((void *) Behaviour_get_isActiveAndEnabled, (void *)HookedBehaviour_get_isActiveAndEnabled ,reinterpret_cast<void **>(&Behaviour_get_isActiveAndEnabled));
    std::string fackOk = "Fack_cpp_Success";
    return env->NewStringUTF(fackOk.c_str());
}
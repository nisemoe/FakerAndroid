#include <jni.h>
#include <string>
#include "Common.h"
#include <fstream>
#include <sstream>
#include "Constant.h"
#if defined(__aarch64__)
#include "Il2cpp-Scaffolding-ARM64/il2cpp-init.h"
#include "Il2cpp-Scaffolding-ARM64/il2cpp-appdata.h"
#elif defined(__arm__)
#include "Il2cpp-Scaffolding-ARM/il2cpp-init.h"
#include "Il2cpp-Scaffolding-ARM/il2cpp-appdata.h"
#endif
using namespace std;
#include "include/faker.h"
using namespace app;
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

const char* coverIl2cppString2Char(Il2CppString *str){
    MonoString *monoString = reinterpret_cast<MonoString *>(str);
    const char *s = monoString->toChars();
    return s;
}
static jobject tmpAct;

void callJava(const char *event) {
    JNIEnv* env;
    global_jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_4);
    jclass jclass1 = env->FindClass("com/faker/android/FakerUnityActivity");
    jmethodID jmethodID1 = env->GetMethodID(jclass1, "onCall", "(Ljava/lang/String;)V");
    jstring enventStr = env->NewStringUTF(event);
    env->CallVoidMethod(tmpAct, jmethodID1, enventStr);
    env->DeleteLocalRef(enventStr);
}


/**
 * note 该方法位于unity thread
 * @param klass
 * @return
 */
bool HookedBehaviour_get_isActiveAndEnabled(Behaviour *klass) {
    bool  b = Behaviour_get_isActiveAndEnabled(klass, NULL);
    if(!b){
        return b;
    }
    GameObject *gameObject = NULL;//Component_get_gameObject(reinterpret_cast<Component *>(klass), NULL);
    if(gameObject==nullptr){
        return b;
    }
    String *name = Object_1_get_name(reinterpret_cast<Object_1 *>(gameObject), NULL);

    if(name== nullptr){
        return b;
    }

    const char *s = coverIl2cppString2Char(reinterpret_cast<Il2CppString *>(name));

    LOGI("class %p---- HookedBehaviour_get_isActiveAndEnabled GameObject Mame: %s",klass,s);
    if(strcmp(s,"Share")==0){
    }
    return b;
}
Il2CppObject *HookedIl2cpp_runtime_invoke(const MethodInfo * method, void *obj, void **params, Il2CppException **exc){
    LOGE("class %p---- HookedIl2cpp_runtime_invoke GameObject Mame: %s",method,method->name);
    return il2cpp_runtime_invoke(method,obj,params,exc);
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
Java_com_faker_android_FakerUnityActivity_init(JNIEnv *env, jobject thiz) {//替换函数
    tmpAct = env->NewGlobalRef(thiz);
    long base = baseAddr("libil2cpp.so");

    init_il2cpp(base);

    //对指定的so 函数进行hook
    //fakeCpp((void *) Behaviour_get_isActiveAndEnabled, (void *)HookedBehaviour_get_isActiveAndEnabled ,reinterpret_cast<void **>(&Behaviour_get_isActiveAndEnabled));
    std::string fackOk = "Fack_cpp_Success";
    return env->NewStringUTF(fackOk.c_str());
}
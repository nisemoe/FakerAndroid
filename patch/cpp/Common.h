//
// Created by Yang on 2020/9/6.
//
#include <android/log.h>
#define LOG_TAG "xNative"
#define DEBUG

#ifdef DEBUG
    #define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
    #define LOGD(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
    #define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#else
    #define  LOGI(...) NULL;
    #define LOGD(...)  NULL;
    #define LOGE(...)  NULL;
#endif



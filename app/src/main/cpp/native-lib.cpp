#include "jni.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_transcribeapp_client_Keys_getUlrChat(JNIEnv *env, jobject thiz) {
    return env ->NewStringUTF("https://chatbotsf-vzt2zxsi7q-uc.a.run.app/");
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_transcribeapp_client_Keys_getSummaryUrl(JNIEnv *env, jobject thiz) {
    return env ->NewStringUTF("http://104.155.183.228:65475//");
}
//
// Created by Specter on 05-Jun-18.
//

#include "nativeEncryption.h"
#include "string.h"
#include "stdlib.h"

extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_specter_mishagram_NativeEncryption_cryption
  (JNIEnv *env, jobject obj, jstring value)
  {
        const char key[16] = "This is the key";
        const char *input = env->GetStringUTFChars(value, JNI_FALSE);

        int keyLen = strlen(key);
        int inputLen = strlen(input);

        char *output = (char*)malloc(inputLen + 1);
        jstring joutput;

        for(int i = 0; i < inputLen; i++)
        {
            output[i] = input[i] ^ key[i % keyLen];
        }
        output[inputLen] = '\0';

        joutput = env->NewStringUTF(output);

        env->ReleaseStringUTFChars(value, input);
        free(output);

        return joutput;
  }
}

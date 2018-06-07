LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := nativeEncryption
LOCAL_MODULE_FILENAME := libNativeEncryption
LOCAL_SRC_FILES := nativeEncryption.cpp
include $(BUILD_SHARED_LIBRARY)
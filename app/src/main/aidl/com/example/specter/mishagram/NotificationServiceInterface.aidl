// NotificationServiceInterface.aidl
package com.example.specter.mishagram;

import com.example.specter.mishagram.CallbackInterface;

// Declare any non-default types here with import statements

interface NotificationServiceInterface {
    void setCallback(in CallbackInterface callback);
}

package com.example.daltascallapp.Interfaces;

import android.webkit.JavascriptInterface;

import com.example.daltascallapp.CallActivity;

public class InterfaceJava
{
    CallActivity callActivity;

    public InterfaceJava(CallActivity callActivity) {
        this.callActivity = callActivity;
    }

    @JavascriptInterface
public void onPeerConnected()
    {
callActivity.OnPeerConnected();
    }
}

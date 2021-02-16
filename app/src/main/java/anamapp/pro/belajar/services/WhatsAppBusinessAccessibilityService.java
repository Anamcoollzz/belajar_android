package anamapp.pro.belajar.services;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Objects;

public class WhatsAppBusinessAccessibilityService extends WhatAppAccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            Log.e("kunnam", event.getPackageName().toString());
            for (int i = 0; i < event.getSource().getChildCount(); i++) {
                Log.e("kunnam", event.getSource().getChild(i).toString());
            }

//            if (getRootInActiveWindow() == null) {
//                return;
//            }
//            AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
//            List<AccessibilityNodeInfoCompat> messageNodeList = new ArrayList<>();

            // WA BUSTINESS
            // trigger send click button
            event.getSource().getChild(9).performAction(AccessibilityNodeInfo.ACTION_CLICK);

            // WA BIASA
            // trigger send click
//            event.getSource().getChild(9).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            // Whatsapp Message EditText id
//            messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId(PACKAGE_NAME+":id/entry");
//            if (messageNodeList == null || messageNodeList.isEmpty()) {
//                return;
//            }
//            // check if the whatsapp message EditText field is filled with text and ending with your suffix (explanation above)
//            AccessibilityNodeInfoCompat messageField = messageNodeList.get(0);
//            if (messageField.getText() == null || messageField.getText().length() == 0
//                    || !messageField.getText().toString().endsWith("   ")) { // So your service doesn't process any message, but the ones ending your apps suffix
//                return;
//            }
//            // Whatsapp send button id
//            List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId(PACKAGE_NAME+":id/send");
//            if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty()) {
//                return;
//            }
//            AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get(0);
//            if (!sendMessageButton.isVisibleToUser()) {
//                return;
//            }


//            Bundle arguments = new Bundle();
//            arguments.putCharSequence(AccessibilityNodeInfo
//                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "android");
//            event.getSource().getChild(7).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);

//            Log.e("kunnam", getRootInActiveWindow().getActionList().get(0).toString());

            // Now fire a click on the send button
//            sendMessageButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            event.getSource().getChild(9).performAction(AccessibilityNodeInfo.ACTION_CLICK);

            // Now go back to your app by clicking on the Android back button twice:
            // First one to leave the conversation screen
            // Second one to leave whatsapp
            try {
                Thread.sleep(800); // hack for certain devices in which the immediate back click is too fast to handle
                performGlobalAction(GLOBAL_ACTION_BACK);
                Thread.sleep(800);  // same hack as above
                performGlobalAction(GLOBAL_ACTION_BACK);
            } catch (InterruptedException ignored) {
            }

        } catch (Exception e) {
            Log.e("kunnam", Objects.requireNonNull(e.getMessage()));
        }


    }

    @Override
    public void onInterrupt() {
        Log.e("kunnam", "HALO");
    }
}
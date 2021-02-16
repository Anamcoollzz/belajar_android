package anamapp.pro.belajar.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.net.URLEncoder;

import anamapp.pro.belajar.MainActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SMSService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SMS = "com.geniobits.autosmssender.action.SMS";
    private static final String ACTION_WHATSAPP = "com.geniobits.autosmssender.action.WHATSAPP";

    // TODO: Rename parameters
    private static final String MESSAGE = "com.geniobits.autosmssender.extra.PARAM1";
    private static final String COUNT = "com.geniobits.autosmssender.extra.PARAM2";
    private static final String MOBILE_NUMBER = "com.geniobits.autosmssender.extra.PARAM3";
    private static final String IS_EACH_WORD = "com.geniobits.autosmssender.extra.PARAM4";

    private static final String IS_lAST = "IS_LAST";
    private static final String IS_WA_ORIGINAL = "IS_WA_ORIGINAL";
    private static final String MAIN_ACTIVITY = "MainActivity";

        private String PACKAGE_NAME = "com.whatsapp.w4b";
//    private String PACKAGE_NAME = "com.whatsapp";
//    private String PACKAGE_NAME = "com.google.android.youtube";

    private boolean isLast = false;

    public SMSService() {
        super("SMSservice");
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionWHATSAPP(Context context, String message, String count, String[] numbersArray, Boolean isEachWord, boolean isLast, MainActivity mainActivity) {
        Intent intent = new Intent(context, SMSService.class);
        intent.setAction(ACTION_WHATSAPP);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(COUNT, count);
        intent.putExtra(MOBILE_NUMBER, numbersArray);
        intent.putExtra(IS_EACH_WORD, isEachWord);
        intent.putExtra(IS_lAST, isLast);
//        intent.putExtra(MAIN_ACTIVITY, mainActivity);
        context.startService(intent);
    }

    public static void startActionWHATSAPP(Context context, String message, String count, String[] numbersArray, Boolean isEachWord, boolean isWaOriginal) {
        Intent intent = new Intent(context, SMSService.class);
        intent.setAction(ACTION_WHATSAPP);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(COUNT, count);
        intent.putExtra(MOBILE_NUMBER, numbersArray);
        intent.putExtra(IS_EACH_WORD, isEachWord);
        intent.putExtra(IS_WA_ORIGINAL, isWaOriginal);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String message = intent.getStringExtra(MESSAGE);
            final String count = intent.getStringExtra(COUNT);
            final String[] mobile_number = intent.getStringArrayExtra(MOBILE_NUMBER);
            final boolean isEachWord = intent.getBooleanExtra(IS_EACH_WORD, false);
            final boolean isWaOriginal = intent.getBooleanExtra(IS_WA_ORIGINAL, false);
            handleActionWHATSAPP(message, count, mobile_number, isEachWord, isWaOriginal);
        }
    }


    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionWHATSAPP(String message, String count, String[] mobileNumbers, boolean isEachWord, boolean isWaOriginal) {

        if (isEachWord) {
            try {
                PackageManager packageManager = getApplicationContext().getPackageManager();
                if (mobileNumbers.length != 0) {
                    for (String mobileNumber : mobileNumbers) {

                        for (int i = 0; i < Integer.parseInt(count.toString()); i++) {
                            String[] words = message.split(" ");
                            for (int k = 0; k < words.length; k++) {
                                String url = "https://api.whatsapp.com/send?phone=" + mobileNumber + "&text=" + URLEncoder.encode(words[k] + "   ", "UTF-8");
                                Intent whatappIntent = new Intent(Intent.ACTION_VIEW);
                                whatappIntent.setPackage("com.whatsapp.w4b");
                                whatappIntent.setData(Uri.parse(url));
                                whatappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (whatappIntent.resolveActivity(packageManager) != null) {
                                    getApplicationContext().startActivity(whatappIntent);
                                    Thread.sleep(4000);
                                    sendBroadcastMessage("Result: " + k);
                                } else {
                                    sendBroadcastMessage("Result: WhatsApp Not installed");
                                }
                            }
                        }

                    }
                }
            } catch (Exception e) {
                sendBroadcastMessage("Result: " + e.toString());
            }
        } else {
            try {
                PackageManager packageManager = getApplicationContext().getPackageManager();
                if (mobileNumbers.length != 0) {
                    for (String mobileNumber : mobileNumbers) {

                        for (int i = 0; i < Integer.parseInt(count); i++) {
                            String url = "https://api.whatsapp.com/send?phone=" + mobileNumber + "&text=" + URLEncoder.encode(message + "   ", "UTF-8");
                            Intent whatappIntent = new Intent(Intent.ACTION_VIEW);
                            if (isWaOriginal)
                                whatappIntent.setPackage("com.whatsapp");
                            else
                                whatappIntent.setPackage("com.whatsapp.w4b");
                            whatappIntent.setData(Uri.parse(url));
                            whatappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (whatappIntent.resolveActivity(packageManager) != null) {
                                getApplicationContext().startActivity(whatappIntent);
                                Thread.sleep(8000);
                                sendBroadcastMessage("Result: " + mobileNumber);
                            } else {
                                sendBroadcastMessage("Result: WhatsApp Not installed");
                            }
                        }

                    }
                }
            } catch (Exception e) {
                sendBroadcastMessage("Result: " + e.toString());
            }
        }


    }


    private void sendBroadcastMessage(String message) {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("result", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
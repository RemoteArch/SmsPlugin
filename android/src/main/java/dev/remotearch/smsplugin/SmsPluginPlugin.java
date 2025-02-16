package dev.remotearch.smsplugin;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
  name = "SmsPlugin",
  permissions = {
    @Permission(strings = { Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS }, alias = "sms")
  }
)
public class SmsPluginPlugin extends Plugin {

  @PluginMethod()
  public void start (PluginCall call)  {
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
      requestPermissionForAlias("sms",call , "smsPermissionCallback");
    } else {
      registerSmsReceiver();
      call.resolve();
    }
  }

  @PluginMethod()
  public void send(PluginCall call) {
    String phone = call.getString("phone");
    String message = call.getString("message");

    if (phone == null || phone.isEmpty()) {
      call.reject("Le numéro de téléphone est requis");
      return;
    }
    if (message == null || message.isEmpty()) {
      call.reject("Le message ne peut pas être vide");
      return;
    }

    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
      requestPermissionForAlias("sms", call, "smsPermissionCallbackForSend");
    } else {
      sendSms(phone, message, call);
    }
  }

  @PermissionCallback
  private void smsPermissionCallbackForSend(PluginCall call) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
        String phone = call.getString("phone");
        String message = call.getString("message");
        sendSms(phone, message, call);
      } else {
        call.reject("Permission refusée pour envoyer des SMS");
      }
    }
  }

  private void sendSms(String phone, String message, PluginCall call) {
    try {
      SmsManager smsManager = SmsManager.getDefault();
      smsManager.sendTextMessage(phone, null, message, null, null);
      call.resolve();
    } catch (Exception e) {
      call.reject("Échec de l'envoi du SMS : " + e.getMessage());
    }
  }

  @PermissionCallback
  private void smsPermissionCallback(PluginCall call) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (getActivity().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(getContext(), "Permission accordée!", Toast.LENGTH_SHORT).show();
        registerSmsReceiver();
        call.resolve();
      } else {
        Toast.makeText(getContext(), "Permission refusée!", Toast.LENGTH_SHORT).show();
        call.reject("Permission refusée pour recevoir des SMS");
      }
    }
  }

  private void registerSmsReceiver() {
    SmsReceiver smsReceiver = new SmsReceiver();
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    getContext().registerReceiver(smsReceiver, filter);
  }

  private class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle bundle = intent.getExtras();
      SmsMessage[] msgs = null;
      String str = "";

      if (bundle != null) {
        Object[] pdus = (Object[]) bundle.get("pdus");
        msgs = new SmsMessage[pdus.length];
        for (int i = 0; i < msgs.length; i++) {
          msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
          str += msgs[i].getOriginatingAddress();
          str += " : " + msgs[i].getMessageBody() + "\n";
        }

         Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

        notifyListeners("onSmsReceived", new JSObject().put("message", str));
      }
    }
  }

}

import { PluginListenerHandle } from '@capacitor/core';

export interface SmsSendOptions {
  phone: string;
  message: string;
}

export interface SmsPluginPlugin {
  start(): Promise<void>;
  send(options: SmsSendOptions): Promise<void>;
  addListener(eventName: 'onSmsReceived', listenerFunc: (data: { message: string }) => void): PluginListenerHandle;
}
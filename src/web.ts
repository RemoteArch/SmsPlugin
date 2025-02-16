import { WebPlugin , PluginListenerHandle } from '@capacitor/core';
import type { SmsPluginPlugin, SmsSendOptions } from './definitions';

export class SmsPluginWeb extends WebPlugin implements SmsPluginPlugin {
  
  async start(): Promise<void> {
    console.warn('SmsPlugin : La réception des SMS n’est pas disponible sur le Web.');
    return;
  }

  async send(options: SmsSendOptions): Promise<void> {
    console.warn('SmsPlugin : L’envoi de SMS n’est pas pris en charge sur le Web.');
    throw new Error('L’envoi de SMS n’est pas pris en charge sur les navigateurs.');
  }

  addListener(eventName: 'onSmsReceived', listenerFunc: (data: { message: string }) => void) {
    console.warn('SmsPlugin : L’écoute des SMS n’est pas disponible sur le Web.');
    return {
      remove: () => console.warn('Listener supprimé'),
    } as PluginListenerHandle;
  }
}

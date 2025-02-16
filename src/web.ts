import { WebPlugin } from '@capacitor/core';

import type { SmsPluginPlugin } from './definitions';

export class SmsPluginWeb extends WebPlugin implements SmsPluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

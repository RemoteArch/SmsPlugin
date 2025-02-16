import { registerPlugin } from '@capacitor/core';

import type { SmsPluginPlugin } from './definitions';

const SmsPlugin = registerPlugin<SmsPluginPlugin>('SmsPlugin', {
  web: () => import('./web').then((m) => new m.SmsPluginWeb()),
});

export * from './definitions';
export { SmsPlugin };

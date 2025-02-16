export interface SmsPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

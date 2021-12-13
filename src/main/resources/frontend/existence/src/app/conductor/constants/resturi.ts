export class RestURI {
  public static SERVER: string = "http://localhost:8080";
  public static GET_INIT_HANDSHAKE: string = this.SERVER + "/conductor/init";
  public static GET_INIT_LOGLEVEL: string = this.SERVER + "/conductor/loglevel";
  public static LOGIN: string = this.SERVER + "/concerto/login";
  public static AUTO_LOGIN: string = this.SERVER + "/concerto/autologin";
  public static GET_SETTING_INDEX: string = this.SERVER + "/concerto/login";
  public static GET_SETTING_ID: string = this.SERVER + "/concerto/login";
  public static GET_SETTING_KEY: string = this.SERVER + "/concerto/login";
  public static GET_USER_INDEX: string = this.SERVER + "/concerto/login";
  public static GET_USER_USERNAME: string = this.SERVER + "/concerto/login";
}

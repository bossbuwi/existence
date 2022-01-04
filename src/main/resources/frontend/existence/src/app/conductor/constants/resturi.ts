export class RestURI {
  public static SERVER: string = "http://localhost:8080";

  public static GET_INIT_HANDSHAKE: string = this.SERVER + "/conductor/init";
  public static GET_INIT_LOGLEVEL: string = this.SERVER + "/conductor/loglevel";

  public static LOGIN: string = this.SERVER + "/concerto/login";
  public static LOGOUT: string = this.SERVER + "/concerto/logout";
  public static AUTO_LOGIN: string = this.SERVER + "/concerto/autologin";

  public static GET_SETTING_INDEX: string = this.SERVER + "/symphony/settings/index";
  public static PUT_SETTING: string = this.SERVER + "/symphony/settings/setting";
  public static GET_SETTING_KEY: string = this.SERVER + "/concerto/login";

  public static GET_USER_INDEX: string = this.SERVER + "/concerto/login";
  public static GET_USER_USERNAME: string = this.SERVER + "/concerto/login";

  public static GET_EVENTS_OF_DATE: string = this.SERVER + "/sonata/events/date";
  public static POST_EVENT: string = this.SERVER + "/sonata/events/event";

  public static GET_SYSTEMS_INDEX: string = this.SERVER + "/sonata/systems/index";
  public static GET_EVENT_TYPES_INDEX: string = this.SERVER + "/sonata/event-types/index";
}

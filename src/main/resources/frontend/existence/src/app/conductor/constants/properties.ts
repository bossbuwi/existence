export class SettingNames {
  public static LOGGING_LEVEL: string = "";
  public static TABS_DESIGN: string = "";
}

export class RequestStatus {
  public static NONE: number = 0;
  public static OK: number = 1;
  public static PENDING: number = 2;
  public static ERROR: number = 3;
  public static DONE: number = 4;
}

export class FormStatus {
  public static UNKNOWN: number = 0;
  public static INSERT: number = 1;
  public static LOADING: number = 2;
  public static ERROR: number = 3;
  public static DONE: number = 4;
}

export class LoggingLevel {
  public static NONE: string = 'N';
  public static VERBOSE: string = 'V';
  public static INFO: string = 'I';
  public static WARNINGS: string = 'W';
  public static ERRORS: string = 'E';
}

export class AuthProps {
  public static TOKEN_NULL: string = 'tundra';
  public static JWT_KEY: string = 'vanuatu';
  public static HEADER_AUTH: string = 'Authorization';
  public static HEADER_BEARER: string = 'Bearer ';
}

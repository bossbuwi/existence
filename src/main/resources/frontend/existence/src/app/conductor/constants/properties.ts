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

export class LoggingLevel {
  public static NONE: string = 'N';
  public static VERBOSE: string = 'V';
  public static INFO: string = 'I';
  public static WARNINGS: string = 'W';
  public static ERRORS: string = 'E';
}

export class FormMode {
  public static INSERT: string = 'I';
  public static EDIT: string = 'E';
  public static CONFIRM: string = 'C';
  public static ERROR: string = 'X';
}

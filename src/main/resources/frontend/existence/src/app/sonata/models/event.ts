export class Event {
  id!: number;
  system!: string;
  zones!: string[];
  zonesStr!: string;
  event_types!: string[];
  event_typesStr!: string;
  start_date!: string;
  end_date!: string;
  jira_case!: string;
  features_on!: string;
  features_off!: string;
  compiled_sources!: string;
  api_used!: string;
  created_by!: string;
  creation_date!: string;
  last_changed_by!: string;
  last_changed_date!: string;

  constructor(object?: any) {
    if (object != undefined) {
      this.id = object.id;
      this.system = object.system;
      this.zones = object.zones;
      this.event_types = object.event_types;
      this.start_date = object.start_date;
      this.end_date = object.end_date;
      this.jira_case = object.jira_case;
      this.features_on = object.features_on;
      this.features_off = object.features_off;
      this.compiled_sources = object.compiled_sources;
      this.api_used = object.api_used;
      this.created_by = object.created_by;
      this.creation_date = object.creation_date;
      this.last_changed_by = object.last_changed_by;
      this.last_changed_date = object.last_changed_date;
    }
  }

  public stringify(): Event {
    if (this.zones.length > 0) {
      for (let i = 0; i < this.zones.length; i++) {
        if (i == 0) {
          this.zonesStr = this.zones[i]
        } else {
          this.zonesStr = this.zonesStr + '-' + this.zones[i];
        }
      }
    }

    if (this.event_types.length > 0) {
      for (let i = 0; i < this.event_types.length; i++) {
        if (i == 0) {
          this.event_typesStr = this.event_types[i]
        } else {
          this.event_typesStr = this.event_typesStr + '-' + this.event_types[i];
        }
      }
    }

    return this;
  }
}

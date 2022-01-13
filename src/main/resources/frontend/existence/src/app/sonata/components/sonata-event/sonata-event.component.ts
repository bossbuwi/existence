import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { BackendService } from 'src/app/conductor/services/backend.service';
import { LoggerService } from 'src/app/conductor/services/logger.service';
import { FormStatus } from 'src/app/conductor/constants/properties';
import { System } from '../../models/system';
import { EventType } from '../../models/eventtype';
import { Event } from '../../models/event';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-sonata-event',
  templateUrl: './sonata-event.component.html',
  styleUrls: ['./sonata-event.component.css']
})
export class SonataEventComponent implements OnInit {
  private className: string = 'SonataEventComponent';
  event!: Event;
  eventForm!: FormGroup;
  formStatus!: number;
  systems!: System[];
  eventTypes!: EventType[];
  zonePrefixes!: string[];
  private zoneArr: string[] = [];
  private eventTypeArr: string[] = [];

  constructor(private logger: LoggerService, private backend: BackendService,
    private builder: FormBuilder) { }

  ngOnInit(): void {
    this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");
    this.formStatus = FormStatus.UNKNOWN;
    this.getData();
    this.eventForm = this.createEventForm();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  private getData(): void {
    this.eventTypes = [];
    this.systems = [];
    this.backend.getEventTypes().subscribe({
      next: res => {
        this.eventTypes = res;
        console.log(this.eventTypes)
        this.populateEventTypes();
      }
    });
    this.backend.getSystems().subscribe({
      next: res => {
        this.systems = res;
        console.log(this.systems)
      }
    });
  }

  private createEventForm(): FormGroup {
    return this.builder.group({
      system: ['',[Validators.required]],
      zone: ['',[Validators.required]],
      type: ['',[Validators.required]],
      zoneText: [{value: '', disabled: true}],
      typeText: [{value: '', disabled: true}],
      zones: this.builder.array([]),
      types: this.builder.array([]),
      jiraCase: [],
      startDate: ['',[Validators.required]],
      endDate: ['',[Validators.required]],
      featureOn: [],
      featureOff: [],
      apiUsed: [],
      compiledSources: [],
      createdBy: [],
      creationDate: [],
      lastModifiedBy: [],
      lastModifiedDate: []
    });
  }

  get types(): FormArray {
    return this.eventForm.get('types') as FormArray;
  }

  newType(eventType: EventType): FormGroup {
    let eventTypeGroup: FormGroup = this.builder.group({
      id: [eventType.id],
      code: [eventType.code],
      name: [eventType.name],
      description: [eventType.description],
      exclusive: [eventType.exclusive],
      checked: [false]
    });
    return eventTypeGroup;
  }

  addType(eventType: EventType) {
    this.types.push(this.newType(eventType));
  }

  private populateEventTypes(): void {
    this.eventTypes.sort((a, b) => (a.id < b.id ? -1 : 1));
    for (let eventType in this.eventTypes) {
      this.addType(this.eventTypes[eventType]);
    }
  }

  get zones(): FormArray {
    return this.eventForm.get('zones') as FormArray;
  }

  newZone(zone: string): FormGroup {
    let zoneGroup: FormGroup = this.builder.group({
      zonal_prefix: [zone],
      checked: [false]
    });
    return zoneGroup;
  }

  addZone(zone: string) {
    this.zones.push(this.newZone(zone));
  }

  private populateZones(zones: string[]): void {
    this.zones.clear();
    if (zones != undefined && zones.length > 0) {
      zones.forEach(element => {
        this.addZone(element);
      });
    } else {

    }
  }

  onSystemChange(event: any): void {
    const element = event as HTMLInputElement;
    const value = element.value;
    let selectedSystem: System = this.systems.find(x => x.global_prefix == value) || new System();
    if(Object.keys(selectedSystem).length > 0) {
      this.zonePrefixes = selectedSystem.zones;
      this.populateZones(selectedSystem.zones);
    }
    this.eventForm.get('zone')?.setValue('');
  }

  onZoneChange(event: any): void {
    const controlId: number = event.target.id*1
    let zoneId: number = controlId + 1;
    let zoneVal: string = '';
    for (let i = 0; i < this.zones.value.length; i++) {
      if (this.zones.value[i].checked == true) {
        if (zoneVal == '') {
          zoneVal = this.zones.value[i].zonal_prefix;
        } else {
          zoneVal = zoneVal + ', ' + this.zones.value[i].zonal_prefix;
        }
      }
    }
    this.eventForm.get('zoneText')?.setValue(zoneVal);
    this.eventForm.get('zone')?.setValue(zoneVal);
  }

  onEventTypeChange(event: any): void {
    const controlId: number = event.target.id*1
    let id: number = controlId + 1;
    const element = event as HTMLInputElement;
    let selectedEventType: EventType = this.eventTypes.find(x => x.id == id) || new EventType();
    let eventTypeVal: string = '';
    for (let i = 0; i < this.types.value.length; i++) {
      if (this.types.value[i].checked == true) {
        if (eventTypeVal == '') {
          eventTypeVal = this.types.value[i].code
        } else {
          eventTypeVal = eventTypeVal + ', ' + this.types.value[i].code;
        }
      }
    }
    this.eventForm.get('typeText')?.setValue(eventTypeVal);
    this.eventForm.get('type')?.setValue(eventTypeVal);
  }

  onSubmit(): void {
    let data: Event = new Event();
    data.system = this.eventForm.get('system')?.value;
    data.jira_case = this.eventForm.get('jiraCase')?.value;
    data.features_on = this.eventForm.get('featureOn')?.value;
    data.features_off = this.eventForm.get('featureOff')?.value;
    data.compiled_sources = this.eventForm.get('compiledSources')?.value;
    data.api_used = this.eventForm.get('apiUsed')?.value;

    let startDateObj: NgbDate =  this.eventForm.get('startDate')?.value;
    let startDate: string = this.getStringDate(startDateObj);
    data.start_date = startDate;

    let endDateObj: NgbDate =  this.eventForm.get('endDate')?.value;
    let endDate: string = this.getStringDate(endDateObj);
    data.end_date = endDate;

    let zoneVal: string = this.eventForm.get('zone')?.value as string;
    data.zones = zoneVal.split(', ');

    let eventTypeVal: string = this.eventForm.get('type')?.value as string;
    data.event_types = eventTypeVal.split(', ');

    this.formPending();
    this.backend.postEvent(data).subscribe({
      next: response => {
        this.formComplete(response);
      },
      error: error => {

      }
    });
  }

  private formPending(): void {
    this.formStatus = FormStatus.LOADING;
    this.eventForm.disable();
  }

  private formInsert(): void {
    this.formStatus = FormStatus.INSERT
    this.eventForm.enable();
    this.eventForm.get('zoneText')?.disable();
    this.eventForm.get('typeText')?.disable();
  }

  private formError(): void {

  }

  private formComplete(data: Event): void {
    this.eventForm.disable();
  }

  private getStringDate(datepicker: NgbDate): string {
    let pipe: DatePipe = new DatePipe('en-US');
    let wDate: Date = new Date(datepicker.year, datepicker.month-1, datepicker.day);
    return pipe.transform(wDate, 'yyyy-MM-dd')!;
  }
}

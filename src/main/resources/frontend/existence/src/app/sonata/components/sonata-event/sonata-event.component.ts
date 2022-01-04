import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { BackendService } from 'src/app/conductor/services/backend.service';
import { LoggerService } from 'src/app/conductor/services/logger.service';
import { FormStatus } from 'src/app/conductor/constants/properties';
import { System } from '../../models/system';
import { EventType } from '../../models/eventtype';

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
  zones!: string[];

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
      }
    });
    this.backend.getSystems().subscribe({
      next: res => {
        this.systems = res;
        console.log(this.systems)
      }
    });
  }

  private populateForm(): void {

  }

  private createEventForm(): FormGroup {
    return this.builder.group({
      system: ['',[Validators.required]],
      zone: ['',[Validators.required]],
      type: ['',[Validators.required]],
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

  onSystemChange(event: any): void {
    const element = event as HTMLInputElement;
    const value = element.value;
    let selectedSystem: System = this.systems.find(x => x.global_prefix == value) || new System();
    console.log(selectedSystem)
    if(Object.keys(selectedSystem).length > 0) {
      this.zones = selectedSystem.zones;
    }
  }

  onEventChange(event: any): void {
    const element = event as HTMLInputElement;
    const value = element.value;
    let selectedEventType: EventType = this.eventTypes.find(x => x.code == value) || new EventType();
    console.log(selectedEventType)
  }

  onSubmit(): void {

  }
}

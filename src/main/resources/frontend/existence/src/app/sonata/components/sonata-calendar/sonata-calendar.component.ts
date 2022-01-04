import { Component, OnInit } from '@angular/core';
import {NgbDateStruct, NgbCalendar, NgbDate, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { BackendService } from 'src/app/conductor/services/backend.service';
import { LoggerService } from 'src/app/conductor/services/logger.service';
import { Event } from 'src/app/sonata/models/event';

@Component({
  selector: 'app-sonata-calendar',
  templateUrl: './sonata-calendar.component.html',
  styleUrls: ['./sonata-calendar.component.css']
})
export class SonataCalendarComponent implements OnInit {
  private className: string = 'SonataCalendarComponent';
  hasEvents: boolean = false;
  events!: Event[];
  eventDetail!: Event;
  calendar!: NgbDateStruct;
  testEvents!: Observable<Event[]>;

  constructor(private cal: NgbCalendar, private logger: LoggerService,
    private backend: BackendService) { }

  ngOnInit(): void {
  this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");
    this.selectToday();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  onDateSelection(date: NgbDate): void {
    this.logger.logVerbose(this.className, "onDateSelection", "Date: " + date.year + "-"
      + date.month + "-" + date.day + " selected.");
    //create a new object of type Date and supply the object's parameters
    //from the received NgbDate object
    let nDate: Date = new Date(date.year, date.month-1, date.day);
    this.logger.logVerbose(this.className, "onDateSelection", "Sending date to BackendService.");
    this.backend.getEventsOnDate(nDate).subscribe({
      next: data => {
        this.logger.logVerbose(this.className, "onDateSelection", "Events received.");
        if (data.length > 0) {
          this.events = data;
          this.eventDetail = this.events[0];
          this.hasEvents = true;
        } else {
          this.hasEvents = false;
        }
      },
      complete: () => {
        this.logger.logVerbose(this.className, "onDateSelection", "Event fetching complete.");
      }
    });
  }

  selectToday(): void {
    this.calendar = this.cal.getToday();
    let today: NgbDate = this.cal.getToday();
    this.onDateSelection(today);
  }

  rowClicked(index: any): void {
    this.eventDetail = this.events[index];
  }
}

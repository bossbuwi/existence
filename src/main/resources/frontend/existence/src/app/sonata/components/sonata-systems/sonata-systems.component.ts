import { Component, OnInit } from '@angular/core';
import { BackendService } from 'src/app/conductor/services/backend.service';
import { LoggerService } from 'src/app/conductor/services/logger.service';
import { System } from '../../models/system';

@Component({
  selector: 'app-sonata-systems',
  templateUrl: './sonata-systems.component.html',
  styleUrls: ['./sonata-systems.component.css']
})
export class SonataSystemsComponent implements OnInit {
  private className: string = 'SonataSystemsComponent';
  systems!: System[];
  systemLoaded!: System;

  constructor(private logger: LoggerService, private backend: BackendService) { }

  ngOnInit(): void {
    this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");
    this.systems = [];
    this.systemLoaded = new System();
    this.loadSystems();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  private loadSystems(): void {
    this.backend.getSystems().subscribe({
      next: response => {
        this.systems = response;

      }
    });
  }

  onSystemChange($event: any) {
    const element = $event as HTMLInputElement;
    const value = element.value;
    let selectedSystem: System = this.systems.find(x => x.global_prefix == value) || new System();
    if(Object.keys(selectedSystem).length > 0) {
      console.log(selectedSystem)
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { LoggerService } from 'src/app/conductor/services/logger.service';

@Component({
  selector: 'app-setting-display',
  templateUrl: './setting-display.component.html',
  styleUrls: ['./setting-display.component.css']
})
export class SettingDisplayComponent implements OnInit {
  private className: string = 'SettingDisplayComponent'

  constructor(private logger: LoggerService) { }

  ngOnInit(): void {
    this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");

    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }
}

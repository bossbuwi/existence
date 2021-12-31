import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoggerService } from 'src/app/conductor/services/logger.service';

@Component({
  selector: 'app-setting-navtabs',
  templateUrl: './setting-navtabs.component.html',
  styleUrls: ['./setting-navtabs.component.css']
})
export class SettingNavtabsComponent implements OnInit {
  private className: string = 'SettingNavtabsComponent';
  active: number = 1;

  constructor(private router: Router, private route: ActivatedRoute,
    private logger: LoggerService) { }

  ngOnInit(): void {
    this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");

    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  navigate(tabNumber: number): void {
    switch (tabNumber) {
      case 1:
        this.router.navigate(['systemconfig'], {relativeTo:this.route});
        break;
      case 2:
        this.router.navigate(['users'], {relativeTo:this.route});
        break;
      case 3:
        this.router.navigate(['display'], {relativeTo:this.route});
        break;
      default:
        break;
    }
  }
}

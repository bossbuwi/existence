import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { LoggerService } from 'src/app/conductor/services/logger.service';

@Component({
  selector: 'app-sonata-navtabs',
  templateUrl: './sonata-navtabs.component.html',
  styleUrls: ['./sonata-navtabs.component.css']
})
export class SonataNavtabsComponent implements OnInit {
  private className: string = 'SonataNavtabsComponent';
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
        this.router.navigate(['home'], {relativeTo:this.route});
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

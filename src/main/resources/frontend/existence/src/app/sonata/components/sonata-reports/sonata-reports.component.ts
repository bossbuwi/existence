import { Component, OnInit } from '@angular/core';
import { BackendService } from 'src/app/conductor/services/backend.service';

@Component({
  selector: 'app-sonata-reports',
  templateUrl: './sonata-reports.component.html',
  styleUrls: ['./sonata-reports.component.css']
})
export class SonataReportsComponent implements OnInit {

  constructor(private backend: BackendService) { }

  ngOnInit(): void {
  }

  generateReport(): void {
    this.backend.getReport().subscribe({
      next: response => {
        const targetHeader: string = response.headers.get('Content-Disposition')!;
        const filename: string = targetHeader.substring(targetHeader.indexOf('=') + 1)

        let file: Blob = new Blob([response.body!],{ type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        let downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(file);
        downloadLink.setAttribute('download', filename);
        document.body.appendChild(downloadLink);
        downloadLink.click();
      }
    });
  }
}

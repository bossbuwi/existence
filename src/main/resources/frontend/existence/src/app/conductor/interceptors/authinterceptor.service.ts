import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

import { LoggerService } from 'src/app/conductor/services/logger.service';
import { AuthService } from 'src/app/conductor/services/auth.service';

import { AuthProps } from 'src/app/conductor/constants/properties';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {
  private className: string = "AuthInterceptorService";

  constructor(private logger: LoggerService, private auth: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.logger.logVerbose(this.className, "intercept", "Intercepting http request.");
    if (this.auth.getIsUserOnline()) {
      this.logger.logVerbose(this.className, "intercept", "A user is online.");
      this.logger.logVerbose(this.className, "intercept", "Adding authentication headers.");
      let token: string = this.auth.getToken();
      const authRequest: HttpRequest<any> = req.clone({ headers: req.headers.set(AuthProps.HEADER_AUTH, AuthProps.HEADER_BEARER + token) });
      return next.handle(authRequest);
    } else if (this.auth.getIsTokenAvailable()){
      this.logger.logVerbose(this.className, "intercept", "A token is detected.");
      this.logger.logVerbose(this.className, "intercept", "Adding authentication headers.");
      let token: string = this.auth.getToken();
      const authRequest: HttpRequest<any> = req.clone({ headers: req.headers.set(AuthProps.HEADER_AUTH, AuthProps.HEADER_BEARER + token) });
      return next.handle(authRequest);
    } else {
      this.logger.logVerbose(this.className, "intercept", "Either no user is online or no token is detected.");
      this.logger.logVerbose(this.className, "intercept", "HTTP request untouched.");
      return next.handle(req);
    }
  }
}

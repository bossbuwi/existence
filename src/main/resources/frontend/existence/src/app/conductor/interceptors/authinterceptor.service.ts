import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthService } from 'src/app/conductor/services/auth.service';
import { AuthProps } from 'src/app/conductor/constants/properties';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {
  private className: string = "AuthInterceptorService";

  constructor(private auth: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.auth.getIsUserOnline()) {
      let token: string = this.auth.getToken();
      const authRequest: HttpRequest<any> = req.clone({ headers: req.headers.set(AuthProps.HEADER_AUTH, AuthProps.HEADER_BEARER + token) });
      return next.handle(authRequest);
    } else if (this.auth.getIsTokenAvailable()){
      let token: string = this.auth.getToken();
      const authRequest: HttpRequest<any> = req.clone({ headers: req.headers.set(AuthProps.HEADER_AUTH, AuthProps.HEADER_BEARER + token) });
      return next.handle(authRequest);
    } else {
      return next.handle(req);
    }
  }
}

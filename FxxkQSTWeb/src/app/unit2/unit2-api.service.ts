import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Unit2ApiService {

  baseUrl = "http://127.0.0.1:8080/"

  constructor() {
  }

  getUrl(path: string) {
    return this.baseUrl + path
  }
}

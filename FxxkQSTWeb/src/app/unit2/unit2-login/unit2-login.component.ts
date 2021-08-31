import {Component, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {Title} from "@angular/platform-browser";
import {HttpClient} from "@angular/common/http";
import {Unit2ApiService} from "../unit2-api.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-unit2-login',
  templateUrl: './unit2-login.component.html',
  styleUrls: ['./unit2-login.component.scss']
})
export class Unit2LoginComponent implements OnInit {
  userName = new FormControl('');
  password = new FormControl('');

  constructor(private title: Title,
              private http: HttpClient,
              private apiService: Unit2ApiService,
              private route:Router) {
  }

  ngOnInit(): void {
    this.title.setTitle("登录")
  }

  submit() {
    let data = {
      "userName": this.userName.value,
      "password": this.userName.value
    }
    if (data.userName == '' || data.password == '') {
      alert("用户名或密码为空！")
    } else {
      this.http.post<status>(this.apiService.getUrl("/unit2/login"), data)
        .subscribe(data => {
          if (data.status) {
            localStorage.setItem("token", data.message)
            alert("登录成功")
            this.route.navigate(['/unit2/status'])
          } else {
            alert(data.message)
          }
        })
    }
  }

}

export interface status {
  "status": boolean
  "message": string
}

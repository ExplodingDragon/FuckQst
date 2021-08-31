import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {switchMap} from "rxjs/operators";
import {FormControl} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Unit2ApiService} from "../unit2-api.service";
import {status} from "../unit2-login/unit2-login.component";
import {UserInfo} from "../unit2-index/unit2-index.component";

@Component({
  selector: 'app-unit2-add',
  templateUrl: './unit2-add.component.html',
  styleUrls: ['./unit2-add.component.scss']
})
export class Unit2AddComponent implements OnInit {

  id = '-1';
  user = '';
  content = new FormControl('');
  date = new FormControl('');
  money = new FormControl('');
  dept = new FormControl('');

  items: string[] = [];


  constructor(private router: ActivatedRoute,
              private route: Router, private http: HttpClient, private config: Unit2ApiService) {
    this.http.get<string[]>(this.config.getUrl("/unit2/department"))
      .subscribe(data => {
        this.items = data
        this.dept.setValue(data[0])
      })
    this.id = this.router.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    const token = localStorage.getItem("token")!;

    if (this.id != '-1') {
      this.http.get<table>(this.config.getUrl("/unit2/" + token + "/tables/" + this.id)).subscribe(data => {
        this.id = data.id.toString();
        this.content.setValue(data.content);
        this.user = data.userName;
        this.date.setValue(data.dateTime);
        this.dept.setValue(data.groupName);
        this.money.setValue(data.money);

      })
    }
    this.http.get<UserInfo>(this.config.getUrl("/unit2/" + token + "/user"))
      .subscribe(data => {
        this.user = data.userName;
      })
  }

  update() {
    let data = {
      "id": this.id,
      "userName": this.user,
      "content": this.content.value,
      "groupName": this.dept.value,
      "dateTime": this.date.value,
      "money": this.money.value
    }
    const token = localStorage.getItem("token")!;
    if (this.id == '-1') {
      this.http.post<status>(this.config.getUrl("/unit2/" + token + "/tables"), data).subscribe(data => {
        if (data.status) {
          alert("成功！")
          this.route.navigate(["/unit2/status"])
        } else {
          alert(data.message)
        }
      })
    } else {
      this.http.post<status>(this.config.getUrl("/unit2/" + token + "/tables/" + this.id), data).subscribe(data => {
        if (data.status) {
          alert("成功！")
          this.route.navigate(["/unit2/status"])
        } else {
          alert(data.message)
        }
      })
    }
    console.log(data)
  }

}

export interface pageTable {
  items: table[],
  index: number,
  size: number
}

export interface table {
  "id": number,
  "userName": string,
  "content": string,
  "groupName": string,
  "dateTime": string,
  "money": number,
  "canEdit": boolean
}

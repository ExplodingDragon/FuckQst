import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {Unit2ApiService} from "../unit2-api.service";
import {FormControl} from "@angular/forms";
import {pageTable, table} from "../unit2-add/unit2-add.component";
import {status} from "../unit2-login/unit2-login.component";

@Component({
  selector: 'app-unit2-index',
  templateUrl: './unit2-index.component.html',
  styleUrls: ['./unit2-index.component.scss']
})
export class Unit2IndexComponent implements OnInit {
  searchName = new FormControl('');

  searchDept = new FormControl('');

  items: string[] = [];

  tables: table[] = [];

  userName = ''
  photo = 'favicon.ico'

  pageIndex = 0;
  pageSize = 0;
  itemSize = 0;
  allUrl: (index: number) => string = index => {
    return "/unit2/" + localStorage.getItem("token") + "/tables/page/" + index
  };
  searchUrl: (index: number) => string = this.allUrl;

  update(index: number = 0) {
    this.http.get<pageTable>(this.config.getUrl(this.searchUrl(index)))
      .subscribe(data => {
        this.tables = data.items;
        this.pageIndex = data.index;
        this.pageSize = Math.ceil(data.size / 10);
        this.itemSize = data.size;
      })
  }


  constructor(private route: Router, private http: HttpClient, private config: Unit2ApiService) {
    this.http.get<string[]>(this.config.getUrl("/unit2/department"))
      .subscribe(data => {
        this.items = data
        this.searchDept.setValue(data[0])
      })
  }

  ngOnInit(): void {
    if (localStorage.getItem("token") == null) {
      alert("请登录！")
      this.route.navigate(["/unit2/login"]).then()
    }
    const token = localStorage.getItem("token")!;
    this.http.get<UserInfo>(this.config.getUrl("/unit2/" + token + "/user"))
      .subscribe(data => {
        this.userName = data.userName;
        this.photo = this.config.getUrl("/unit2/static/" + data.imageId)
      })


    this.update()
  }

  exit() {
    alert("已退出！")
    localStorage.removeItem("token")
    this.route.navigate(["/unit2/login"]).then()
  }

  delete(id: number) {
    if (confirm("确认删除id为" + id + "的数据吗？")) {
      const token = localStorage.getItem("token")!;
      this.http.get<status>(this.config.getUrl("/unit2/" + token + "/tables/" + id + "/delete"))
        .subscribe(data => {
          if (data.status) {
            this.update()
          }
        });
    }
  }

  clearSearch() {
    this.searchUrl = this.allUrl;
    this.searchName.setValue('')
    this.searchDept.setValue(this.items[0])
    this.update(0)

  }

  search() {
    this.searchUrl = function (index: number) {
      return "/unit2/" + localStorage.getItem("token") + "/tables/search/" +
        this.searchName.value
        + "/" + this.searchDept.value + "/" + index
    }
    this.update(0)
  }
}

export interface UserInfo {
  userName: string
  imageId: string
}

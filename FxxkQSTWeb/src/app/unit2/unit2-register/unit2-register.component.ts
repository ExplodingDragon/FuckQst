import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Unit2ApiService} from "../unit2-api.service";
import {FormControl} from "@angular/forms";
import {status} from "../unit2-login/unit2-login.component";
import {Router} from "@angular/router";

@Component({
  selector: 'app-unit2-register',
  templateUrl: './unit2-register.component.html',
  styleUrls: ['./unit2-register.component.scss']
})
export class Unit2RegisterComponent implements OnInit {
  items: string[] = [];
  imgId = '';
  userName = new FormControl('');
  password = new FormControl('');
  password2 = new FormControl('');
  dept = new FormControl('');


  constructor(private http: HttpClient,
              private config: Unit2ApiService,
              private route:Router) {
  }

  ngOnInit(): void {
    this.http.get<string[]>(this.config.getUrl("/unit2/department"))
      .subscribe(data => {
        this.items = data
        this.dept.setValue(data[0])
      })
  }

  submit() {
    let data = {
      "userName": this.userName.value,
      "password": this.password.value,
      "deptName": this.dept.value,
      "urlId": this.imgId
    }
    if (data.password != this.password2.value || data.password == '') {
      alert("两次密码输入不一致！")
    } else {
      this.http.post<status>(this.config.getUrl("/unit2/register"), data).subscribe(data => {
        if (!data.status) {
          alert(data.message)
        } else {
          alert("注册成功,请登录！")
          this.route.navigate(["/unit2/login"])
        }
      })
    }
  }

  upload(files: any) {
    if (!files.files.isEmpty) {
      let file = files.files.item(0);
      let update = new FormData();
      update.append("file", file, file.name)
      this.http.post<upload>(this.config.getUrl('/unit2/upload'), update, {
        headers: {
          ContentType: 'multipart/form-data'
        }
      }).subscribe(data => {
        console.log(data.id)
        alert("上传成功！")
        this.imgId = data.id;
      })
    }

  }
}

interface upload {
  id: string
}

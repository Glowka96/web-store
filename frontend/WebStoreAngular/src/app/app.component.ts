import { Component, OnInit } from '@angular/core';
import { __values } from 'tslib';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'WebStore';

  constructor(){}

  ngOnInit(): void {
    console.log('started component');
  } 
}




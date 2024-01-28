import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CategoryRequest } from 'src/app/models/category-request';
import { ProducerResponse } from 'src/app/models/producer-response';
import { ProducerService } from 'src/app/services/products/producer.service';

@Component({
  selector: 'app-mod-producer',
  templateUrl: './mod-producer.component.html',
  styleUrls: ['./mod-producer.component.scss'],
})
export class ModProducerComponent implements OnInit {
  private producers: ProducerResponse[] = [];
  private errorAddMsg = '';
  private errorUpdateMsg = '';
  private errorDeleteMsg = '';

  public addForm = new FormGroup({
    name: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
  });

  public updateForm = new FormGroup({
    choiceProducer: new FormControl('', {
      updateOn: 'change',
    }),
    name: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
  });

  public deleteForm = new FormGroup({
    choiceProducer: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
  });

  constructor(private producerService: ProducerService) {
    producerService.producers$.subscribe((producers) => {
      this.producers = producers;
    });
  }

  ngOnInit(): void {}

  onSumbitAdd() {
    if (this.addForm.valid) {
      const request: CategoryRequest = {
        name: this.addForm.controls['name']?.value ?? '',
      };
      this.producerService.addProducer(request).subscribe({
        next: () => window.location.reload(),
        error: (e) => {
          this.errorAddMsg = e.error.errors.join('<br>');
        },
      });
    }
  }

  onSumbitUpdate() {
    if (this.updateForm.valid) {
      const request: CategoryRequest = {
        name: this.updateForm.controls['name']?.value ?? '',
      };
      const id = this.updateForm.controls['choiceProducer']?.value;
      if (id) {
        this.producerService.updateProducer(id, request).subscribe({
          next: () => window.location.reload(),
          error: (e) => {
            this.errorAddMsg = e.error.errors.join('<br>');
          },
        });
      }
    }
  }

  onSumbitDelete() {
    if (this.deleteForm.valid) {
      const id = this.deleteForm.controls['choiceProducer']?.value;
      if (id) {
        this.producerService.deleteProducer(id).subscribe({
          next: () => window.location.reload(),
          error: (e) => {
            this.errorDeleteMsg = e.error.errors.join('<br>');
          },
        });
      }
    }
  }

  public get listProducer() {
    return this.producers;
  }

  public get errorAddMessage() {
    return this.errorAddMsg;
  }

  public get errorUpdateMessage() {
    return this.errorUpdateMsg;
  }

  public get errorDeleteMessage() {
    return this.errorDeleteMsg;
  }
}

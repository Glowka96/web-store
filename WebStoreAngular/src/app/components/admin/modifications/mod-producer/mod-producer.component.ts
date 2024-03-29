import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { take } from 'rxjs';
import { ProducerRequest } from 'src/app/models/products/producer-request';
import { ProducerResponse } from 'src/app/models/products/producer-response';
import { EntityFormBuilderService } from 'src/app/services/forms/admins/entity-form-builder.service';
import { ProducerService } from 'src/app/services/products/producer.service';

@Component({
  selector: 'app-mod-producer',
  templateUrl: './mod-producer.component.html',
  styleUrls: ['./mod-producer.component.scss'],
})
export class ModProducerComponent implements OnInit {
  @Input()
  producers!: ProducerResponse[];
  private errorAddMsg = '';
  private errorUpdateMsg = '';
  private errorDeleteMsg = '';

  public addForm!: FormGroup;
  public updateForm!: FormGroup;
  public deleteForm!: FormGroup;

  constructor(
    private producerService: ProducerService,
    private entityFormService: EntityFormBuilderService
  ) {}

  ngOnInit(): void {
    this.addForm = this.entityFormService.createAddFormGroup();
    this.updateForm = this.entityFormService.createUpdateFormGroup();
    this.deleteForm = this.entityFormService.createDeleteFormGroup();
  }

  onSumbitAdd() {
    if (this.addForm.valid) {
      const request: ProducerRequest = {
        name: this.addForm.controls['name']?.value ?? '',
      };
      this.producerService
        .addProducer(request)
        .pipe(take(1))
        .subscribe({
          next: () => window.location.reload(),
          error: (e) => {
            this.errorAddMsg = e.error.errors.join('<br>');
          },
        });
    }
  }

  onSumbitUpdate() {
    if (this.updateForm.valid) {
      const request: ProducerRequest = {
        name: this.updateForm.controls['name']?.value ?? '',
      };
      const id = this.updateForm.controls['choice']?.value;
      if (id) {
        this.producerService
          .updateProducer(id, request)
          .pipe(take(1))
          .subscribe({
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
      const id = this.deleteForm.controls['choice']?.value;
      if (id) {
        this.producerService
          .deleteProducer(id)
          .pipe(take(1))
          .subscribe({
            next: () => window.location.reload(),
            error: (e) => {
              this.errorDeleteMsg = e.error.errors.join('<br>');
            },
          });
      }
    }
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

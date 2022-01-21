import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RequestStatus, ValidationConstants } from 'src/app/conductor/constants/properties';
import { AuthService } from 'src/app/conductor/services/auth.service';
import { BackendService } from 'src/app/conductor/services/backend.service';
import { LoggerService } from 'src/app/conductor/services/logger.service';
import { Setting } from 'src/app/symphony/models/setting';

@Component({
  selector: 'app-setting-sysconfig',
  templateUrl: './setting-sysconfig.component.html',
  styleUrls: ['./setting-sysconfig.component.css']
})
export class SettingSysconfigComponent implements OnInit {
  private className: string = 'SettingSysconfigComponent';
  settingsData!: Setting[];
  settingsForm!: FormGroup;
  private settingsLoaded: boolean = false;
  private controlIndex: number = 0;

  constructor(private logger: LoggerService, private backend: BackendService,
    private auth: AuthService, private  builder: FormBuilder) { }

  ngOnInit(): void {
    this.initialize();
  }

  private initialize() {
    this.logger.logVerbose(this.className, "initialize", "Initializing component.");
    this.createSettingsForm();
    this.backend.getSettings();
    this.initializeSubscriptions();
    this.logger.logVerbose(this.className, "initialize", "Initialization complete.");
  }

  private initializeSubscriptions(): void {
    this.backend.subSettingsStatus().subscribe({
      next: response => {
        switch (response) {
          case RequestStatus.OK:
            if (!this.settingsLoaded) {
              this.loadSettings();
            } else {
              this.changeDone(this.controlIndex);
            }
            break;
          case RequestStatus.PENDING:
            if (!this.settingsLoaded) {

            } else {

            }
            break;
          case RequestStatus.ERROR:
            if (!this.settingsLoaded) {

            } else {
              this.changeError(this.controlIndex);
            }
            break;
          case RequestStatus.DONE:
            break;
          default:
            break;
        }
      }
    });
  }

  private createSettingsForm(): void {
    this.settingsForm = this.builder.group({
      settings: this.builder.array([])
    });
  }

  get settings(): FormArray {
    return this.settingsForm.get('settings') as FormArray;
  }

  addSetting(setting: Setting) {
    this.settings.push(this.newSetting(setting));
  }

  newSetting(setting: Setting): FormGroup {
    let settingGroup: FormGroup = this.builder.group({
      id: [setting.id],
      key: [setting.key],
      length: [setting.length],
      type: [setting.type],
      desc: [setting.desc],
      defaultValue: [setting.default_value],
      validValues: [setting.valid_values],
      addedBy: [setting.added_by],
      lastChangedBy: [setting.last_changed_by],
      lastChangedDate: [setting.last_changed_date],
      value: [setting.value,
        {validators: [Validators.required, Validators.maxLength(setting.length)],
        updateOn: 'change'}],
      submit: 'Save'
    })

    return settingGroup;
  }

  private loadSettings(): void {
    this.settingsData = this.backend.provideData();
    this.settingsData.sort((a, b) => (a.id < b.id ? -1 : 1));
    for (let setting in this.settingsData) {
      this.addSetting(this.settingsData[setting]);
    }
    this.settingsLoaded = true;
  }

  onSubmit(index: number): void {
    this.logger.logVerbose(this.className, "onSubmit", "Submitting changes on control with index " + index + ".");
    let setting: Setting = this.settingsData[index];
    setting.value = this.settings.controls[index].get('value')?.value;
    setting.last_changed_by = this.auth.getUser().username;
    this.controlIndex = index;
    this.backend.putSettings(setting);
    this.changePending(this.controlIndex);
  }

  onChange(index: number): void {
    const validValuesCntrl = this.settings.controls[index].get('validValues');
    const valueCntrl = this.settings.controls[index].get('value');
    const submitBtn = this.settings.controls[index].get('submit');
    let validValues: string = validValuesCntrl?.value;

    if (validValues === ValidationConstants.ALPHANUMERIC || validValues === ValidationConstants.NUMERIC) {
      if((valueCntrl?.value as string).indexOf(' ') >= 0){
        valueCntrl?.setErrors({ invalidValue: true });
      }
    } else {
      let validValuesArr: string[] = validValues.split(',');
      let inputValue: string = valueCntrl?.value;

      if (!validValuesArr.some(x => x === inputValue)) {
        valueCntrl?.setErrors({ invalidValue: true });
      } else {
        submitBtn?.setValue('Save');
        submitBtn?.setErrors(null);
        valueCntrl?.setErrors(null);
      }
    }
  }

  private changePending(index: number): void {
    this.logger.logVerbose(this.className, "changePending", "Putting form in a waiting state.");
    this.settings.controls[index].get('submit')?.setValue('Saving..');
  }

  private changeDone(index: number): void {
    let setting: Setting = this.backend.provideData();
    this.settingsForm.enable();
    this.settings.controls[index].get('submit')?.setValue('Save');
    this.settings.controls[index].get('value')?.setValue(setting.value);
    this.settings.controls[index].get('lastChangedBy')?.setValue(setting.last_changed_by);
    this.settings.controls[index].get('lastChangedDate')?.setValue(setting.last_changed_date);
    this.settings.controls[index].markAsPristine();
  }

  private changeError(index: number): void {
    this.settings.controls[index].get('submit')?.setValue('Failed');
    this.settings.controls[index].get('submit')?.setErrors({ serverError: true });
  }
}

function validValuesValidator() {
  return (formGroup: FormGroup) => {
    const validValuesCntrl = formGroup.get('validValues');
    const valueCntrl = formGroup.get('value');
    let validValues: string = validValuesCntrl?.value;

    if (validValues === ValidationConstants.ALPHANUMERIC || validValues === ValidationConstants.NUMERIC) {
      if((valueCntrl?.value as string).indexOf(' ') >= 0){
        valueCntrl?.setErrors({ invalidValue: true });
      }
    } else {
      let validValuesArr: string[] = validValues.split(',');
      let inputValue: string = valueCntrl?.value;

      if (!validValuesArr.some(x => x === inputValue)) {
        valueCntrl?.setErrors({ invalidValue: true });
      } else {
        valueCntrl?.setErrors(null);
      }
    }
  }
}

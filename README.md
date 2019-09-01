[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-contact--picker-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7814) [ ![Download](https://api.bintray.com/packages/farhad/maven/contactpicker/images/download.svg) ](https://bintray.com/farhad/maven/contactpicker/_latestVersion) [![HitCount](http://hits.dwyl.io/farhad/contact-picker.svg)](http://hits.dwyl.io/farhad/contact-picker)

```groovy
repositories {
	jcenter()
}
	
implementation 'io.github.farhad:contactpicker:[latest-version]'
```
---

This library enables you to add `Choose from contacts` functionality to your apps, without adding `READ_CONTACS` permission and having to deal with contact content provider. 

You can use this library in all Activities and Fragments. Please be advised that this library is built upon `AndroidX` artifacts and is not guaranteed to work with projects that use `appcompat`support package.

To use in your Activity/Fragment :

```kotlin
          val picker: ContactPicker? = ContactPicker.create(
            activity = this,
            onContactPicked = { Log.d("TAG", it.name + ": " + it.number) },
            onFailure = { Log.d("TAG", it.localizedMessage) })
        
          picker?.pick()      
```

If the user chooses a contact from the picker window, an instance of `PickedContact` class is returned to the first callback/lambda passed to `ContactPicker.create()` which has `number` and `name` properties.
 

### License

    Copyright (C) 2019  Farhad Faghihi

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

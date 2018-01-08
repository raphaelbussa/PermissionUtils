# Permission Utils
[![Download](https://api.bintray.com/packages/raphaelbussa/maven/permission-utils/images/download.svg) ](https://bintray.com/raphaelbussa/maven/permission-utils/_latestVersion) [![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14) [![GitHub issues](https://img.shields.io/github/issues/rebus007/PermissionUtils.svg)](https://github.com/rebus007/PermissionUtils/issues) [![GitHub forks](https://img.shields.io/github/forks/rebus007/PermissionUtils.svg)](https://github.com/rebus007/PermissionUtils/network) [![GitHub stars](https://img.shields.io/github/stars/rebus007/PermissionUtils.svg)](https://github.com/rebus007/PermissionUtils/stargazers) [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/rebus007/PermissionUtils/master/LICENSE) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PermissionUtils-green.svg?style=true)](https://android-arsenal.com/details/1/3951)

![Logo](https://raw.githubusercontent.com/rebus007/PermissionUtils/master/sample/src/main/ic_launcher-web.png)

Check marshmallow permission easily

### Import
At the moment the library is in my personal maven repo
```Gradle
repositories {
    maven {
        url 'http://dl.bintray.com/raphaelbussa/maven'
    }
}
```
```Gradle
dependencies {
    compile 'rebus:permission-utils:2.0.5'
}
```
### How to use
#### Request a permission

First, override onRequestPermissionsResult and call PermissionManager.handleResult(requestCode, permissions, grantResults);
```Java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    PermissionManager.handleResult(this, requestCode, permissions, grantResults);
}
```

Now you can ask permission :D
```Java
PermissionManager.Builder()
        .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
        .askAgain(true)
        .askAgainCallback(new AskAgainCallback() {
            @Override
            public void showRequestPermission(UserResponse response) {
                    showDialog(response);
                }
            })
        .callback(new FullCallback() {
            @Override
            public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
                }
            })
        .ask(this);
```
or more permission
```Java
.permission(PermissionEnum.GET_ACCOUNTS, PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.READ_SMS)
```
or
```Java
ArrayList<PermissionEnum> permissionEnumArrayList = new ArrayList<>();
permissionEnumArrayList.add(PermissionEnum.ACCESS_FINE_LOCATION);
permissionEnumArrayList.add(PermissionEnum.GET_ACCOUNTS);
permissionEnumArrayList.add(PermissionEnum.READ_CONTACTS);

.permissions(permissionEnumArrayList)
```

if you need to simply check a permission, just call utils
```Java
PermissionEnum permissionEnum = PermissionEnum.WRITE_EXTERNAL_STORAGE;
boolean granted = PermissionUtils.isGranted(MainActivity.this, PermissionEnum.WRITE_EXTERNAL_STORAGE);
Toast.makeText(MainActivity.this, permissionEnum.toString() + " isGranted [" + granted + "]", Toast.LENGTH_SHORT).show()
```

#### Callbacks
You can use three different callback, it depends of your needs.

* FullCallback: gives you all the information on permission requested by you
* SimpleCallback: returns a boolean that says if all permission requests were permitted
* SmartCallback: returns a boolean that says if all permission requests were permitted and a boolean that says if some permissions are denied forever

#### Little extra
If user answer "Never ask again" to a request for permission, you can redirect user to app settings, with an utils
```Java
PermissionUtils.openApplicationSettings(MainActivity.this, R.class.getPackage().getName());
```
# That's all folks!

### Sample
Browse the sample code [here](https://github.com/rebus007/PermissionUtils/tree/master/sample)

### Javadoc
Browse Javadoc [here](https://rebus007.github.io/PermissionUtils/javadoc/)

### App using Permission Utils
If you use this lib [contact me](mailto:raphaelbussa@gmail.com?subject=PermissionUtils) and I will add it to the list below:

### Developed By
Raphaël Bussa - [raphaelbussa@gmail.com](mailto:raphaelbussa@gmail.com)

[ ![Twitter](https://raw.githubusercontent.com/rebus007/PermissionUtils/master/img/social/twitter-icon.png) ](https://twitter.com/rebus_007)[ ![Google Plus](https://raw.githubusercontent.com/rebus007/PermissionUtils/master/img/social/google-plus-icon.png) ](https://plus.google.com/+RaphaelBussa/posts)[ ![Linkedin](https://raw.githubusercontent.com/rebus007/PermissionUtils/master/img/social/linkedin-icon.png) ](https://www.linkedin.com/in/rebus007)

### License
```
The MIT License (MIT)

Copyright (c) 2017 Raphaël Bussa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

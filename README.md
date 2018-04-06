Milano
--------
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Milano-blue.svg?style=flat)](http://android-arsenal.com/details/1/4459) [ ![Download](https://api.bintray.com/packages/intrudershanky/maven/milano/images/download.svg) ](https://bintray.com/intrudershanky/maven/milano/_latestVersion) [![API](https://img.shields.io/badge/API-9%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=9) [![](https://jitpack.io/v/IntruderShanky/Milano.svg)](https://jitpack.io/#IntruderShanky/Milano)
--------
Easy to make HTTP Request.
Easy to manage cookies with HTTP Request and HTTP Response.
An automated cookies manager library for android.

[Demo App - IS Library](https://play.google.com/store/apps/details?id=com.intrusoft.islibrarydemo)

<a href='https://play.google.com/store/apps/details?id=com.intrusoft.islibrarydemo&utm_source=global_co&utm_small=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="193" height="75"/></a>

# Download
### Download via JitPack
##### Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
    ...
    maven { url "https://jitpack.io" }
    }
}
```
##### Step 2. Add the dependency
```groovy
compile 'com.github.IntruderShanky:Milano:2.0.2'
```
#### **OR**
### Download via Gradle
```groovy
compile 'com.intrusoft.milano:milano:2.0.2'
```

Features
--------
- Easy to make HTTP Request.
- Easy to manage cookies.
- Automatic Mangage Cookies.
- Customizable HTTP Request

Implementation
-----------

#### To make fluent request using singleton instance
```java
Milano.with(MainActivity.this)
      .fromURL("https://your_api.com/data")
      .doGet()
      .shouldManageCookies(true)        //if false then will not set cookies to request or retrieve cookies from response.
      .execute(new OnRequestComplete() {
            @Override
            public void onSuccess(String response, int responseCode) {
                   //Do whatever you want to do
                   addText("\nResponse: " + response);
            }
            
            @Override
            public void onError(String error, int errorCode) {
                   //Do whatever you want to do
                   addText("\nError: " + error);
            }
       });
```

#### To make customized HTTP request use Milano.Builder
```java
Milano.Builder builder = new Milano.Builder(MainActivity.this);
//This is the part of url which will remain same in future request
String defaultURLPrefix = "https://www.yourdomain.com";
String request = "{\"username\": \"IntruderShanky\",\"password\": \"asd54fbg\"}";
builder.setCookieTag("Login Cookies");
builder.shouldDisplayDialog(true);
builder.setDialogMessage("Fetching Info");
builder.setDialogTitle("Loging In");
builder.shouldManageCookies(true);
builder.setConnectTimeOut(6000);
builder.setReadTimeOut(5500);
builder.setNetworkErrorMessage("Internet not connected");
builder.setDefaultURLPrefix(defaultURLPrefix);

//Create an instance of Milano, use this instance for future request
Milano milano = builder.build();

//Make HTTP request with customized request properties
milano.fromURL("/user_login")
      .doPost(request)
      .execute(new OnRequestComplete() {
            @Override
            public void onSuccess(String response, int responseCode) {
                   //Do whatever you want to do
                   addText("\nResponse: " + response);
            }
            
            @Override
            public void onError(String error, int errorCode) {
                   //Do whatever you want to do
                   addText("\nError: " + error);
            }
       });
```

Progaurd Rules:
-----------
Add these lines to your progaurd file for release build.
```
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** w(...);
    public static *** v(...);
    public static *** i(...);
}
```

Licence
--------

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

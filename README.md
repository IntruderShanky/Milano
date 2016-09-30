Milano
--------
Easy HTTP Request.
An automated cookies manager library for android.



Download
--------

Grab via Gradle:
```groovy
compile 'com.intrusoft.milano:milano:1.0.0'
```



Features
--------
- Automatic Mangage Cookies.
- Customizable



Implementation
-----------
###Initially:
- manageCookies method set to false.
- Request method is set to "GET"

If manageCookies() is not set to true, then this will not manage cookies with request and response.

#### To make GET request

```java
Milano.along(MainActivity.this)
            .manageCookies(true)
            .fromUrl(url)
            .execute(new OnRequestComplete() {
                @Override
                public void onComplete(String response) {
                    // Do whatever you want to do with response
                }
            });
    
```



#### To make POST request

```java
Milano.along(MainActivity.this)
            .manageCookies(true)
            .fromUrl(url)
            .doPost(request)
            .execute(new OnRequestComplete() {
                @Override
                public void onComplete(String response) {
                    // Do whatever you want to do with response
                }
            });
    
```


#### To Customize request
- Set custom loading message
- Set network error


```java
Milano.along(MainActivity.this)
            .fromUrl(url)
            .manageCookies(true)
            .displayLoadingTitle("Loading")
            .displayLoadingMessage("User Data")
            .setNetworkError("Internet connection is unavailable")
            .execute(new OnRequestComplete() {
                    @Override
                    public void onComplete(String response) {
                        // Do whatever you want to do with response
                    }
            });
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

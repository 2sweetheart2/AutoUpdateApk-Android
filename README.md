# AutoUpdateApk-Android

Android library for updating your project by downloading the latest apk file of your program from the server

# Install 

## Gradle 

Add it in your root build.gradle at the end of repositories:

```Gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
 Add the dependency **(CHECK REPO TAG AND REPLACE "Tag" WITH CURRENT REPO LATEST RELEASE TAG)**
```Gradle
	dependencies {
	        implementation 'com.github.2sweetheart2:AutoUpdateApk-Android:Tag'
	}
```

## Maven

Add the JitPack repository to your build file

```Maven
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Add the dependency **(CHECK REPO TAG AND REPLACE "Tag" WITH CURRENT REPO LATEST RELEASE TAG)**
```Maven
	<dependency>
	    <groupId>com.github.2sweetheart2</groupId>
	    <artifactId>AutoUpdateApk-Android</artifactId>
	    <version>Tag</version>
	</dependency>
```

# Usage

## Permissions

add this in your AndroidManifest file:

```Xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WAKE_LOCK"/>
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

    <application
      ...
      android:requestLegacyExternalStorage="true"
    </application>
```

## Code


first, you must to check user permissions:
```Kotlin

private val REQUEST_EXTERNAL_PERMISSION_CODE = 666
    private var PERMISSIONS_EXTERNAL_STORAGE =
        arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

    private fun checkPermission(permissions: Array<String>) {
        if (ContextCompat.checkSelfPermission(
                this@AutoUpdateExample,
                permissions[0]
            ) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(
                this@AutoUpdateExample,
                permissions[1]
            ) == PackageManager.PERMISSION_DENIED
        ) {

            ActivityCompat.requestPermissions(
                this@AutoUpdateExample,
                PERMISSIONS_EXTERNAL_STORAGE,
                REQUEST_EXTERNAL_PERMISSION_CODE
            )
        } else {
            Toast.makeText(this@AutoUpdateExample, "Permission already granted", Toast.LENGTH_SHORT).show()
            runUpdater()
        }
    }

override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_PERMISSION_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            runUpdater()
        }
    }
```

runUpdater():

```Kotlin

    private fun runUpdater(){
        val autoUpdate = AutoUpdate("url", BuildConfig.VERSION_CODE, null, null)
        autoUpdate.getUpdate(supportFragmentManager,false)
    }


```

for run check function and runUpdater(), call 
` checkPermission(PERMISSIONS_EXTERNAL_STORAGE) `

## Modify

you can replace old dialog. For do this, you need put your dialog layout id in constructor
``` AutoUpdate("url", BuildConfig.VERSION_CODE, YOUR DIALOG'S LAYOUT ID, "response") ``

** BE CAREFUL **. Your custom dialog must include this:
1. TextView (title of update) with id - `update_update_title`
2. TextView (version_str) with id - `update_update_version`
3. LinearLayout (layout with all change log texts) with id - `update_update_change_log_list`
4. Button (button update) with id - `update_update_update_btn`
5. Button (button skip) with id - `update_update_skip_btn`

## Explanations

the first parameter takes the URL of your server, which should return such a json-string
```Python
{
      "version_str":"NEW VERSION, DOWNLOAD NOW", # - this text will be displayed under the title of the dialog
      "version":YOUR APP WERSION (1,2,19 .etc), # - this need for check build version client and apk on server, check versionCode in Gradle
      "title":"New Update", # - title of update
      "change_log":[{"title":"Title 1","text":"title 1 text"},{"title":"Title 2","text":"title 2 text"}], # - update log. The new object in the array will be shown as a new block
      "update_link":"url to new apk" 
}
```

last parameter takes json key, if your server send json like this:
```Python
{"response":
  {"version_str":"123",...},
...
}
```
using such an example of a server response, you need to specify such parameters in the constructor 
`AutoUpdate("url", BuildConfig.VERSION_CODE, null, "response")`

if your server response only 
```Python
{"version_str":"123"...}
```
`AutoUpdate("url", BuildConfig.VERSION_CODE, null, null)`

# ViewPagerIndicator [![Release](https://jitpack.io/v/vivchar/ViewPagerIndicator.svg)](https://jitpack.io/#vivchar/ViewPagerIndicator) [![API](https://img.shields.io/badge/API-14%2B-yellow.svg?style=flat)](https://android-arsenal.com/api?level=14) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.me/vvivchar)

A Simple View Pager Indicator with animations

## Gradle

* Step 1. Add the JitPack repository to your build file

```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

* Step 2. Add the dependency

```gradle
	dependencies {
	        implementation 'com.github.vivchar:ViewPagerIndicator:${LATEST_VERSION}'
	}
```

## Usage

* Step 1. Add to your layout

```

	<com.github.vivchar.viewpagerindicator.ViewPagerIndicator
		android:id = "@+id/view_pager_indicator"
		android:layout_width = "wrap_content"
		android:layout_height = "wrap_content"
		android:layout_gravity = "center_horizontal|bottom"
		android:layout_marginBottom = "20dp"
		app:delimiterSize = "8dp"
		app:itemIcon = "@drawable/ic_insert_emoticon_black_24dp"
		app:itemScale = "1.5"
		app:itemSelectedTint = "@color/colorAccent"
		app:itemSize = "16dp"
		app:itemTint = "@color/colorPrimary"
		/>
```

* Step 2. Set ViewPager

``` java
		viewPagerIndicator.setupWithViewPager(mViewPager);
		viewPagerIndicator.addOnPageChangeListener(mOnPageChangeListener);
```

## Example

![Example](https://github.com/vivchar/ViewPagerIndicator/blob/master/example.gif)

## License

    Copyright 2017 Vitaly Vivchar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

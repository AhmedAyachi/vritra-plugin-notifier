# <img width="20"  src="https://raw.githubusercontent.com/AhmedAyachi/RepoIllustrations/f7ee069a965d3558e0e7e2b7e6733d1a642c78c2/Vritra/Icon.svg"> ![GitHub license](https://img.shields.io/badge/vritra--plugin--notifier-e03065) &middot; ![GitHub license](https://img.shields.io/badge/cordova--android-10.1.2-2eca55.svg) ![GitHub license](https://img.shields.io/badge/cordova--iOS-7-2eca55.svg) ![GitHub license](https://img.shields.io/badge/license-MIT-e03065.svg)

A cordova plugin for toast messages and local notifications for android and iOS platforms.
Defines a global **Notifier** object.

# Installation
After installing globally the cordova cli, execute:
```
cordova plugin add vritra-plugin-notifier
```

Example of use:
```js
Notifier.notify({
	title: 'Name of the app',
	body: 'Some message',
})
```

```js
Notifier.toast({
	text: 'Some message',
})
```

[See documentation](https://vritrajs.github.io/#cordovaplugins#notifier)
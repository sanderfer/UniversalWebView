# UniversalWebView
可以预览PDF，DOC的webview

添加依赖

1.jitpack
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2.控件依赖
```
implementation 'com.github.sanderfer:UniversalWebView:1.0.1'
```

## 问题
doc使用微软的在线office进行简单在线预览，只能通过使用域名的请求，ip请求会无法预览。

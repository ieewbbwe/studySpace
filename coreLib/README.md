# 库文件申明
### OkHttp
网络请求库 https://github.com/square/okhttps
Copyright (C) 2012 Square, Inc.
Apache License, Version 2.0

### Refreshlayout
List刷新库 https://github.com/bingoogolapple/BGARefreshLayout-Android
Copyright 2015 bingoogolapple
Apache License, Version 2.0

### Universalimageloader
图片加载库 https://github.com/dodola/Android-Universal-Image-Loader
Copyright 2011-2013 Sergey Tarasevich
Apache License, Version 2.0

### AndPermission
6.0以上的权限申请库 https://github.com/yanzhenjie/AndPermission
Copyright 2016 Yan Zhenjie
Apache License, Version 2.0

```
AndPermission.with(this)
    .requestCode(101)
    .permission(Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_SMS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    .send();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 只需要调用这一句，剩下的AndPermission自动完成。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(100)
    private void getLocationYes() {
        // 申请权限成功，可以去做点什么了。
        Toast.makeText(this, "获取定位权限成功", Toast.LENGTH_SHORT).show();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(100)
    private void getLocationNo() {
        // 申请权限失败，可以提醒一下用户。
        Toast.makeText(this, "获取定位权限失败", Toast.LENGTH_SHORT).show();
    }
```



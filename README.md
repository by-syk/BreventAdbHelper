# BreventAdbHelper

这是针对**未ROOT**设备上使用「[黑域](http://www.coolapk.com/apk/me.piebridge.brevent)」的辅助工具，**一键启动黑域服务**。


### 假定

+ 已在Windows电脑上配置`Java`、`adb`环境变量

+ 设备已启用**USB调试**

+ 设备已安装「[黑域](http://www.coolapk.com/apk/me.piebridge.brevent)」`v0.8.7`

+ 设备已安装「[ADB Clipboard GetSet](out/)」
  
  > 用于支持通过`adb`命令获取设备剪切板内容的APP


### 一般过程

1. 设备开机

2. 启动「黑域」
  
  ![Brevent](art/brevent_0.png)

3. 连接电脑，对照敲入adb命令并执行（好烦）
  
  ![Command](art/manual.png)

成功后「黑域」截图：

![Brevent](art/brevent_1.png)


### 一键过程

一般过程中2、3步比较繁琐，有自动化的必要。因此用`Java`写了自动化逻辑，并结合`.bat`实现一键操作。

1. 设备开机

2. 双击执行[`BreventAdbHelper.bat`](out/)
  
  ![Command](art/breventadbhelper.png)

搞定。


### 一键背后的步骤

1. 检查设备是否处于调试模式并通过USB连接电脑

2. 检查设备是否已安装「ADB Clipboard GetSet」

3. 启动设备上的「黑域」

4. 从设备获取黑域复制到剪切板的ADB命令并执行


### License

    Copyright 2017 By_syk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


*Copyright &#169; 2017 By_syk. All rights reserved.*




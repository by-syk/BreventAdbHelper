/*
 * Copyright 2017 By_syk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.by_syk.breventadbhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BreventAdbHelper {
    public static void main(String[] args) {
        // 检查设备是否处于调试模式并通过USB连接电脑
        
        final String CMD_CHECK_DEVICE = "adb devices";
        System.out.println("> executing:\n" + CMD_CHECK_DEVICE);
        String result = execAdbCmd(CMD_CHECK_DEVICE);
        if (result.trim().split("\n").length != 2) {
            System.out.println("> result:\nmore than one device/emulator");
            return;
        }
        
        // 检查设备是否已安装「ADB Clipboard GetSet」
        
        final String CMD_CHECK_APK_ADB = "adb shell dumpsys package com.by_syk.adbclipboard";
        System.out.println("> executing:\n" + CMD_CHECK_APK_ADB);
        result = execAdbCmd(CMD_CHECK_APK_ADB);
        if (!result.contains("versionName=")) {
            System.out.println("> result:\ncom.by_syk.adbclipboard not installed");
            return;
        }
        
        // 启动设备上的「黑域」
        
        final String CMD_CHECK_BREVENT = "adb shell am start -n me.piebridge.brevent/me.piebridge.brevent.ui.BreventActivity";
        System.out.println("> executing:\n" + CMD_CHECK_BREVENT);
        result = execAdbCmd(CMD_CHECK_BREVENT);
        if (!result.contains("Starting:")) {
            System.out.println("> result:\nme.piebridge.brevent not installed");
            return;
        }
        
        // 从设备获取黑域复制到剪切板的ADB命令并执行
        
        final String CMD_CLIP_GET = "adb shell am broadcast -a adbclipget";
        System.out.println("> executing:\n" + CMD_CLIP_GET);
        result = execAdbCmd(CMD_CLIP_GET);
        // result=-1: Activity.RESULT_OK, result=0: Activity.RESULT_CANCELED 
        Matcher matcher = Pattern.compile("result=-1, data=\"(.+?)\"").matcher(result);
        if (matcher.find()) {
            String breventCmd = matcher.group(1);
            if (breventCmd.contains("adb")) {
                System.out.println("> executing:\n" + breventCmd);
                result = execAdbCmd(breventCmd);
                if (result.contains("brevent_server started")) {
                    System.out.println("> result:\nall done");
                } else {
                    System.out.println("> result:\n" + result);
                }
                return;
            }
        }
        System.out.println("> result:\nno Brevent command found in clipboard");
    }
    
    private static String execAdbCmd(String cmd) {
        String result = "";
        
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        BufferedReader bufferedReader = null;
        try {
            process = runtime.exec(cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            String buffer;
            while ((buffer = bufferedReader.readLine()) != null) {
                result += buffer + "\n";
            }
            if (result.length() > 0) {
                result = result.substring(0, result.length() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
                process = null;
            }
        }
        
        return result;
    }
}

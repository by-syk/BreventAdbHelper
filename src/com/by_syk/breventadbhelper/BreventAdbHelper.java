package com.by_syk.breventadbhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BreventAdbHelper {
    public static void main(String[] args) {
        final String CMD_CHECK_BREVENT = "adb shell dumpsys package me.piebridge.brevent";
        System.out.println("> executing:\n" + CMD_CHECK_BREVENT);
        String result = execAdbCmd(CMD_CHECK_BREVENT);
        if (!result.contains("versionName=")) {
            System.out.println("> failed:\nme.piebridge.brevent not installed.");
            return;
        }
        final String CMD_CHECK_APK_ADB = "adb shell dumpsys package com.by_syk.adbclipboard";
        System.out.println("> executing:\n" + CMD_CHECK_APK_ADB);
        result = execAdbCmd(CMD_CHECK_APK_ADB);
        if (!result.contains("versionName=")) {
            System.out.println("> failed:\ncom.by_syk.adbclipboard not installed.");
            return;
        }
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
                System.out.println("> result:\n" + result);
                return;
            }
        }
        System.out.println("> failed:\nBrevent command not found in clipboard.");
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
                result += buffer;
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

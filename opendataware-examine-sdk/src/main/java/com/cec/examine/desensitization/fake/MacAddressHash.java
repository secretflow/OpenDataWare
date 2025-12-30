package com.cec.examine.desensitization.fake;

import com.cec.examine.util.encryption.MD5Util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MacAddressHash {

    public static void main(String[] args) {
        String macAddress = getMacAddress();
        String hashCode = hashMacAddress(macAddress);
        System.out.println("MAC地址：" + macAddress);
        System.out.println("哈希值：" + hashCode);
    }

    // 获取本地计算机的MAC地址
    public static String getMacAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(localHost);
            byte[] mac = network.getHardwareAddress();
            StringBuilder macAddress = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                macAddress.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
            }
            return macAddress.toString();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 使用哈希函数将MAC地址映射为整数
    public static String hashMacAddress(String macAddress) {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hashBytes = digest.digest(macAddress.getBytes());
//            ByteBuffer buffer = ByteBuffer.wrap(hashBytes);
//            return buffer.getInt();
            return MD5Util.textToMD5L32(macAddress);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
    }
}

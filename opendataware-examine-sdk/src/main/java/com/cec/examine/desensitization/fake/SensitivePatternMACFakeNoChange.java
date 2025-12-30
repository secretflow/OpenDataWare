package com.cec.examine.desensitization.fake;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName SensitivePatternMACFake.java
 * @Description MAC地址假名化
 * @createTime 2023/07/12
 */
public class SensitivePatternMACFakeNoChange extends SensitivePatternRegex {
    private static final String[] HEX_DIGITS = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
    };
    @Override
    public String pattern() {
//        String pattern = "/((([a-f0-9]{2}:){5})|(([a-f0-9]{2}-){5}))[a-f0-9]{2}/gi";
        String pattern2 = "^[A-F0-9]{2}(-[A-F0-9]{2}){5}$|^[A-F0-9]{2}(:[A-F0-9]{2}){5}$";
        return pattern2;
    }

    @Override
    public String target(String source) {
        return generateRandomMACAddress(source);
    }
    private static String generateRandomMACAddress(String text) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(HEX_DIGITS[random.nextInt(16)]);
            sb.append(HEX_DIGITS[random.nextInt(16)]);

            if (i != 5) {
                sb.append(":");
            }
        }

        return sb.toString();
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
    public static int hashMacAddress(String macAddress) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(macAddress.getBytes());
            ByteBuffer buffer = ByteBuffer.wrap(hashBytes);
            return buffer.getInt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

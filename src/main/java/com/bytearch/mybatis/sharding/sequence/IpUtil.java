package com.bytearch.mybatis.sharding.sequence;

import java.net.*;
import java.util.Enumeration;

/**
 * @author yarw
 */
public class IpUtil {
    /**
     * 直接根据第一个网卡地址作为其内网ipv4地址，避免返回 127.0.0.1
     */
    public static String getLocalIpByNetcard() {
        try {
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (item.isLoopback() || !item.isUp()) {
                        continue;
                    }
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        return inet4Address.getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取ip后三位
     * @return
     */
    public static Integer getIpSuffix() {
        try {
            String localIp = getLocalIpByNetcard();
            if (localIp != null) {
                int lastIndex = localIp.lastIndexOf('.');
                if (lastIndex == -1) {
                    throw new Exception("local ip is  invalid!");
                }
                String suffixNoStr = localIp.substring(lastIndex + 1);
                return Integer.parseInt(suffixNoStr);
            }
        } catch (Exception e) {
            System.err.println("get local ip error, emsg:{}" + e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(getIpSuffix());
    }
}

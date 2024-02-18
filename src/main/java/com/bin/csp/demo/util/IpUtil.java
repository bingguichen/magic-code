package com.bin.csp.demo.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * IP工具包
 *
 * @author BinChan
 */
public class IpUtil {



    /**
     * 获取本地真正的IP地址，即获得有线或者无线WiFi地址。
     * 过滤虚拟机、蓝牙等地址
     *
     * @return
     */
    public static String[] getRealLocalIP() {
        List<String> ips = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();

                // 去除回环接口，子接口，未运行和接口
                if (netInterface.isLoopback()
                        || netInterface.isVirtual()
                        || !netInterface.isUp()
                        || netInterface.getDisplayName().startsWith("VirtualBox")
                        || netInterface.getDisplayName().startsWith("Vmware")) {
                    continue;
                }
                //特定网卡的去除掉
                /*
                if (!netInterface.getDisplayName().contains("Intel")
                        && !netInterface.getDisplayName().contains("Realtek")) {
                    continue;
                }
                 */
                Enumeration<InetAddress> addresses = netInterface
                        .getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    ip.isSiteLocalAddress();
                    if (ip != null) {
// ipv4
                        if (ip instanceof Inet4Address) {
                            ips.add(ip.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error when getting host ip address"
                    + e.getMessage());
        }
        return ips.toArray(new String[ips.size()]);
    }

    /**
     * 获取本地真正的IP地址，即获得有线或者无线WiFi地址，仅限于局域网地址。
     * 过滤虚拟机、蓝牙等地址
     *
     * @return
     */
    public static String[] getLocalIP() {
        List<String> ips = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();

                // 去除回环接口，子接口，未运行和接口
                if (netInterface.isLoopback()
                        || netInterface.isVirtual()
                        || !netInterface.isUp()
                        || netInterface.getDisplayName().startsWith("VirtualBox")
                        || netInterface.getDisplayName().startsWith("Vmware")) {
                    continue;
                }
                //特定网卡的去除掉
                /*
                if (!netInterface.getDisplayName().contains("Intel")
                        && !netInterface.getDisplayName().contains("Realtek")) {
                    continue;
                }
                 */
                Enumeration<InetAddress> addresses = netInterface
                        .getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null) {
// ipv4
                        if (ip instanceof Inet4Address && ip.isSiteLocalAddress()) {
                            ips.add(ip.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error when getting host ip address"
                    + e.getMessage());
        }
        return ips.toArray(new String[ips.size()]);
    }
}

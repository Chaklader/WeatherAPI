package com.weather.api.weatherapi.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class IpAddressFinder {

    public static String getClientIpAddressIfServletRequestExist() {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return Parameters.WILDCARD_IP_ADDRESS;
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for (String header : Parameters.IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);

            if (ipList != null && ipList.length() != 0 && !Parameters.UNKNOWN.equalsIgnoreCase(ipList)) {
                return ipList.split(",")[0];
            }
        }

        return request.getRemoteAddr();
    }
}

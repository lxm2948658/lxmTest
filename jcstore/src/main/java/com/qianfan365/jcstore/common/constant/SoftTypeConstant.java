package com.qianfan365.jcstore.common.constant;

import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.pojo.SoftType;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.service.SoftTypeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoftTypeConstant {

    public static SoftTypeService softTypeService;

    public interface SoftTypeStatus {
        Integer ENABLE = 1;
        Integer DISABLE = 0;
    }

    private static Map<Integer, List<String>> softMap = new HashMap<Integer, List<String>>() {
        private static final long serialVersionUID = 1L;
    };

    public static void setSoftList(List<SoftType> list) {
        list.stream().forEach(l -> softMap.put(l.getId(), Lists.newArrayList(l.getModuleId().split(","))));
    }

    public static Map<Integer, List<String>> softMap() {
        return softMap;
    }

    public static boolean checkMoudule(User user, String type) {
        List<String> userModuleids = SoftTypeConstant.softMap().get(user.getSoftId());
        return userModuleids.contains(type);
    }


}

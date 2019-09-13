package com.bath.tcpviz.dis.thymeleafTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ThymeleafInfoToDisplay {


    HashMap<Integer, List<String>> createMockHashMap() {
        HashMap<Integer, List<String>> map = new HashMap<>();
        List<String> list1 = new ArrayList<>();
        list1.add("ITEM1.1");
        list1.add("ITEM1.2");
        list1.add("ITEM1.3");
        list1.add("ITEM1.4");
        List<String> list2 = new ArrayList<>();
        list2.add("ITEM2.1");
        list2.add("ITEM2.2");
        list2.add("ITEM2.3");
        map.put(1, list1);
        map.put(2, list2);
        return map;
    }


    List<Integer> getKeys() {
        // System.out.println("GET KEYS! ");
        List<Integer> listOfKeys = new ArrayList<>();
        HashMap<Integer, List<String>> mockHashMap = createMockHashMap();
        listOfKeys.addAll(mockHashMap.keySet());
        return listOfKeys;
    }


}

package com.bath.tcpviz.dis.thymeleafTest;

import org.junit.Test;

public class ThymeleafInfoToDisplayTest {

    @Test
    public void canGetKeyList() {
        ThymeleafInfoToDisplay th = new ThymeleafInfoToDisplay();
        System.out.println(th.getKeys());
    }

}
package com.project.demo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SampleDemoMainTest
{
    @Test
    public void testGetResponse() {
        SampleDemoMain.RequestHandler rh = new SampleDemoMain.RequestHandler();
        String response = rh.getHTMLResponse();
        assertTrue(response.contains("Hello! The date and time now is:") ||
                response.contains("<a href=\"https://imgflip.com/i/3zrrvy\"><img src=\"https://i.imgflip.com/3zrrvy.jpg\" title=\"made at imgflip.com\"/></a>"));
    }
}

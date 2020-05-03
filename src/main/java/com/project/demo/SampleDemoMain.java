package com.project.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class SampleDemoMain {

    private static int versionNumber = 1;
    private static int numberOfRequests = 0;
    private static Object lock = new Object();

    public static class RequestHandler extends AbstractHandler {

        public String getHTMLResponse(){
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());

            String content;
            synchronized (lock) {
                if (numberOfRequests >= 5) {
                    content = "<a href=\"https://imgflip.com/i/3zrrvy\"><img src=\"https://i.imgflip.com/3zrrvy.jpg\" title=\"made at imgflip.com\"/></a>";
                    numberOfRequests = 0;
                } else {
                    content = String.format("<p>Hello! The date and time now is: %s.</p>",formatter.format(date));
                    numberOfRequests++;
                }
            }

            String HTMLResponse = String.format(
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<title>Date and Time</title>\n" +
                "\t</head>\n" +
                "\n" +
                "\t<body>\n" +
                "\t\t%s\n" +
                "\t\t<p>version: %d</p>\n" +
                "\t</body>\n" +
                "</html>", content, versionNumber);

            return HTMLResponse;
        }

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException {

            System.out.println(target);

            if (!request.getMethod().equals("GET") || !target.equals("/")) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
                return;
            }

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);

            String HTMLResponse = getHTMLResponse();
            response.getWriter().println(HTMLResponse);
            response.getWriter().flush();
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ContextHandler mainContext = new ContextHandler("/");
        mainContext.setHandler(new RequestHandler());
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { mainContext });
        server.setHandler(contexts);

        server.start();
        server.join();
    }
}

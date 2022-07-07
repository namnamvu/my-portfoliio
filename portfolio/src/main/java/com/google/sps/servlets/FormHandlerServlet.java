package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@WebServlet("/form-handler")
public class FormHandlerServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // getParameter only works if the parameter is a name in html, otherwise null
    String name = sanitizeUI(request, "name-input");
    String phoneNum = sanitizeUI(request, "phone-number");
    String messages = sanitizeUI(request, "message-input");
    long timestamp = System.currentTimeMillis();
    
    Datastore dataStore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = dataStore.newKeyFactory().setKind("Contact");

    FullEntity linkEntity = 
        Entity.newBuilder(keyFactory.newKey())
            .set("Name", name)
            .set("Phone number", phoneNum)
            .set("Messages", messages)
            .set("Timestamp", timestamp)
            .build();
    dataStore.put(linkEntity);

    // Print the value so you can see it in the server logs.
    System.out.println("You submitted: " + name);
    System.out.println("You submitted: " + phoneNum);
    System.out.println("You submitted: " + messages);

    // Write the value to the response so the user can see it.
    response.getWriter().println("You submitted: " + name);
    response.getWriter().println("You submitted: " + phoneNum);
    response.getWriter().println("You submitted: " + phoneNum);

    // Redirect user to another page
    response.sendRedirect("https://nvu-sps-summer22.appspot.com/#link-to-contact");
  }

  /**
   * Sanitize user input to remove HTML tags and Javascript
   */
  private String sanitizeUI (HttpServletRequest request, String input){
    return Jsoup.clean(request.getParameter(input), Safelist.none());
  }
}

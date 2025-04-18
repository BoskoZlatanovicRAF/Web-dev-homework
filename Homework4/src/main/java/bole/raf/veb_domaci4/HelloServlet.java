package bole.raf.veb_domaci4;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    private boolean buttonClicked = false;

    public void init() {

        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");


        // Check if the user session cookie exists
        HttpSession session = request.getSession();
        Map<String, Integer> mealOrders = (Map<String, Integer>) session.getAttribute("mealOrders");
        if (mealOrders == null) {
            // If the mealOrders attribute does not exist, create a new map
            mealOrders = new HashMap<>();
            session.setAttribute("mealOrders", mealOrders);
        }

        boolean sessionExists = session.getAttribute(session.getId()) != null;

        String fileName = null;
        StringBuilder html = new StringBuilder();
        PrintWriter out = response.getWriter();
        List<String> meals = null;

        List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");


        out.println("<html><body>");
        // ukoliko postoje parametri username i password, proveravamo da li su ispravni
        if(username != null && password != null) {
            if(username.equals("admin") && password.equals("admin")) {

                for(String day : days){
                    html.append("<table>");
                    html.append("<tr><th>Dish</th><th>Quantity</th></tr>");
                    fileName = checkDay(day);
                    meals = getMeals(fileName);
                    for (String meal : meals) {
                        // Get the number of orders for this meal
                        Integer orders = mealOrders.get(meal);
                        if (orders == null) {
                            // If this meal has not been ordered yet, set the number of orders to 0
                            orders = 0;
                        }

                        html.append("<tr><td>" + meal + "</td><td>" + orders + "</td></tr>");
                    }
                    html.append("</table>");
                }
                html.append("<form method=\"POST\" action=\"/hello-servlet\">");
                html.append("<button type=\"submit\" name=\"reset\" value=\"reset\">Reset</button>");
                html.append("</form>");
                out.println(html.toString());
            }
            else{
                out.println("<p> Ne postoji takav korisnik </p>");
            }

        }
        /// ukoliko ne postoje parametri onda se prikazuju opcije za izbor obroka
        else if (!sessionExists || (sessionExists && buttonClicked == false)) {
            // Set the new user session cookie
            session.setAttribute(request.getSession().getId(), true);


            for(String day : days) {
                fileName = checkDay(day);

                meals = getMeals(fileName);
                html.append("<form method = \"POST\" action = \"/hello-servlet\">");
                html.append("<label>" + day + "   " + "</label>");
                html.append("<select name = \"" + day + "\">");
                for (String meal : meals) {
                    html.append("<option value = \"" + meal + "\">" + meal + "</option>");
                }
                html.append("</select>");
                html.append("<br>");

            }
            html.append("<button>Save Options</button>");
            html.append("</form>");
            html.append("<br>");
            out.println(html.toString());


        } else if(sessionExists && buttonClicked == true){
            // If the session exists, inform the user they've already made their selection
            out.println("<p>You have already made your selection.</p>");
        }

        out.println("</body></html>");
        out.close();
    }




    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("POST request received AAAAAAAAAAAAAAAA");
        HttpSession session = request.getSession();
        String reset = request.getParameter("reset");
        Map<String, Integer> mealOrders = (Map<String, Integer>) session.getAttribute("mealOrders");
        if ("reset".equals(reset)) {
            // The "Reset" button was clicked

            if (mealOrders != null) {
                // Reset the quantities for all meals
                for (String meal : mealOrders.keySet()) {
                    mealOrders.put(meal, 0);
                }
            }

            // Remove the session attributes for the user's selected meals
            List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
            for (String day : days) {
                session.removeAttribute(day);
            }
            session.invalidate();
            // Redirect the user to the /hello-servlet page
            response.sendRedirect("/hello-servlet");
        }
        else {

            List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
            for (String day : days) {
                String selectedMeal = request.getParameter(day);
                session.setAttribute(day, selectedMeal);

                Integer orders = mealOrders.get(selectedMeal);
                if (orders == null) {
                    orders = 1;
                }else{
                    orders++;
                }
                mealOrders.put(selectedMeal, orders);
            }
            buttonClicked = true;
            // Redirect the user to the /save-meal page
            response.sendRedirect("/save-meal");
        }

    }

    public void destroy() {

    }



    private List<String> getMeals(String fileName) {
        String filepath = "C:\\Users\\User\\Desktop\\Web programiranje\\Veb_Domaci4\\src\\main\\resources\\" + fileName;
        List<String> meals = new ArrayList<>();
        meals = getMealsFromFile(filepath);
        return meals;
    }

    private List<String> getMealsFromFile(String filepath) {
        List<String> meals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                meals.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions such as file not found
        }
        return meals;
    }

    private String checkDay(String day) {
        String fileName = null;
        if(day.equals("Monday")){
            fileName = "1ponedeljak.txt";
        }else if(day.equals("Tuesday")) {
            fileName = "2utorak.txt";
        }else if(day.equals("Wednesday")) {
            fileName = "3sreda.txt";
        }else if(day.equals("Thursday")) {
            fileName = "4cetvrtak.txt";
        } else if (day.equals("Friday")) {
            fileName = "5petak.txt";
        }

        return fileName;
    }
}
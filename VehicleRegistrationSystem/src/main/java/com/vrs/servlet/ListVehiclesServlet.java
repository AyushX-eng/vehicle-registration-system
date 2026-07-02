package com.vrs.servlet;

import com.vrs.entity.Vehicle;
import com.vrs.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/list")
public class ListVehiclesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // JOIN FETCH to load associated customer
            List<Vehicle> vehicles = session
                    .createQuery("SELECT v FROM Vehicle v JOIN FETCH v.customer ORDER BY v.id DESC", Vehicle.class)
                    .list();

            out.println("<html><head><title>All Vehicles</title>");
            out.println("<style>");
            out.println("body { font-family: Arial; background: #f4f4f4; padding: 20px; }");
            out.println("table { width: 100%; border-collapse: collapse; background: #fff; }");
            out.println("th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }");
            out.println("th { background: #3498db; color: white; }");
            out.println(
                    "a.button { text-decoration: none; background: #3498db; color: white; padding: 10px; border-radius: 5px; }");
            out.println("</style></head><body>");
            out.println("<h2>All Registered Vehicles</h2>");

            if (vehicles.isEmpty()) {
                out.println("<p>No vehicles found.</p>");
            } else {
                out.println("<table>");
                out.println("<tr><th>Model</th><th>Number</th><th>City</th><th>Owner</th></tr>");
                for (Vehicle v : vehicles) {
                    out.println("<tr>");
                    out.println("<td>" + v.getModel() + "</td>");
                    out.println("<td>" + v.getNumber() + "</td>");
                    out.println("<td>" + v.getCity() + "</td>");
                    out.println("<td>" + v.getCustomer().getName() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }

            out.println("<br><a href='index.html' class='button'>Back to Home</a>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Error occurred while fetching vehicles.</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}

package com.vrs.servlet;

import com.vrs.entity.Vehicle;
import com.vrs.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String number = request.getParameter("number");
        String model = request.getParameter("model");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Validate input
        if ((number == null || number.trim().isEmpty()) &&
                (model == null || model.trim().isEmpty())) {
            out.println("<html><body>");
            out.println("<h2>Please enter at least Vehicle Number or Model to search.</h2>");
            out.println("<a href='search_vehicle.html'>Go Back</a>");
            out.println("</body></html>");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM Vehicle v WHERE 1=1");
            if (number != null && !number.trim().isEmpty()) {
                hql.append(" AND LOWER(v.number) = :number");
            }
            if (model != null && !model.trim().isEmpty()) {
                hql.append(" AND LOWER(v.model) = :model");
            }

            Query<Vehicle> query = session.createQuery(hql.toString(), Vehicle.class);
            if (number != null && !number.trim().isEmpty()) {
                query.setParameter("number", number.trim().toLowerCase());
            }
            if (model != null && !model.trim().isEmpty()) {
                query.setParameter("model", model.trim().toLowerCase());
            }

            List<Vehicle> vehicles = query.list();

            // Begin HTML response
            out.println("<html><head><title>Search Results</title>");
            out.println("<style>");
            out.println("body { font-family: Arial; background: #f4f4f4; padding: 20px; }");
            out.println("table { width: 100%; border-collapse: collapse; background: #fff; }");
            out.println("th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }");
            out.println("th { background: #3498db; color: white; }");
            out.println(
                    "a.button { text-decoration: none; background: #3498db; color: white; padding: 10px; border-radius: 5px; display: inline-block; margin-top: 10px; }");
            out.println("</style></head><body>");
            out.println("<h2>Search Results</h2>");

            if (vehicles.isEmpty()) {
                out.println("<p>No matching vehicles found.</p>");
            } else {
                out.println("<table>");
                out.println("<tr><th>Model</th><th>Number</th><th>City</th><th>Owner</th></tr>");
                for (Vehicle v : vehicles) {
                    out.println("<tr>");
                    out.println("<td>" + escapeHtml(v.getModel()) + "</td>");
                    out.println("<td>" + escapeHtml(v.getNumber()) + "</td>");
                    out.println("<td>" + escapeHtml(v.getCity()) + "</td>");
                    out.println("<td>" + escapeHtml(v.getCustomer().getName()) + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }

            out.println("<a href='search_vehicle.html' class='button'>Search Again</a>");
            out.println("<a href='index.html' class='button'>Back to Home</a>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace(); // log error in console for debugging
            out.println("<html><body><h2>Error occurred while searching:</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            out.println("<a href='search_vehicle.html'>Try Again</a></body></html>");
        }
    }

    // Simple HTML escape to avoid malformed output if values contain "<", "&", etc.
    private String escapeHtml(String input) {
        if (input == null)
            return "";
        return input.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}

package com.vrs.servlet;

import com.vrs.entity.SparePart;
import com.vrs.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/list-spares")
public class ListSparePartsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use JOIN FETCH to avoid LazyInitializationException
            List<SparePart> spareParts = session.createQuery(
                    "SELECT sp FROM SparePart sp JOIN FETCH sp.vehicle v JOIN FETCH v.customer",
                    SparePart.class).list();

            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>All Spare Parts</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }");
            out.println("h2 { color: #2c3e50; }");
            out.println(
                    "div.part { background: #ffffff; padding: 15px; border: 1px solid #ccc; margin-bottom: 15px; border-radius: 8px; }");
            out.println(
                    "a.button { text-decoration: none; background: #3498db; color: white; padding: 10px 20px; border-radius: 5px; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<h2>All Registered Spare Parts</h2>");

            if (spareParts.isEmpty()) {
                out.println("<p>No spare parts found in the system.</p>");
            } else {
                for (SparePart part : spareParts) {
                    out.println("<div class='part'>");
                    out.println("<strong>Spare Part Name:</strong> " + part.getName() + "<br>");
                    out.println("<strong>Part Number:</strong> " + part.getPartNumber() + "<br>");
                    out.println("<strong>Vehicle Number:</strong> " + part.getVehicle().getNumber() + "<br>");
                    out.println("<strong>Vehicle Model:</strong> " + part.getVehicle().getModel() + "<br>");
                    out.println("<strong>Owner Name:</strong> " + part.getVehicle().getCustomer().getName() + "<br>");
                    out.println("</div>");
                }
            }

            out.println("<br><a href=\"index.html\" class=\"button\">🔙 Back to Home</a>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Error occurred while fetching spare parts.</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}

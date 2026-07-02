package com.vrs.servlet;

import com.vrs.entity.SparePart;
import com.vrs.entity.Vehicle;
import com.vrs.util.HibernateUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/register-spare")
public class SparePartRegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String partName = request.getParameter("partName");
        String partNumber = request.getParameter("partNumber");
        String vehicleNumber = request.getParameter("vehicleNumber");

        if (partName == null || partNumber == null || vehicleNumber == null ||
                partName.trim().isEmpty() || partNumber.trim().isEmpty() || vehicleNumber.trim().isEmpty()) {
            out.println("<h2>All fields are required!</h2>");
            out.println("<a href='register_spare.html'>Go Back</a>");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Query<Vehicle> query = session.createQuery("FROM Vehicle WHERE number = :number", Vehicle.class);
            query.setParameter("number", vehicleNumber.trim());

            List<Vehicle> vehicles = query.list();

            if (!vehicles.isEmpty()) {
                Vehicle vehicle = vehicles.get(0);

                SparePart part = new SparePart();
                part.setName(partName.trim());
                part.setPartNumber(partNumber.trim());
                part.setVehicle(vehicle);

                session.persist(part);
                tx.commit();

                response.sendRedirect("success.html");
            } else {
                out.println("<h2>Vehicle not found with number: " + vehicleNumber + "</h2>");
                out.println("<a href='register_spare.html'>Try Again</a>");
            }

        } catch (Exception e) {
            e.printStackTrace(); // For Tomcat log debugging
            out.println("<h2>Error while registering spare part.</h2>");
            out.println("<p style='color:red;'>" + e.getMessage() + "</p>");
            out.println("<a href='register_spare.html'>Go Back</a>");
        }
    }
}

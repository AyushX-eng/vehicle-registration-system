package com.vrs.servlet;

import com.vrs.entity.Customer;
import com.vrs.entity.Vehicle;
import com.vrs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Fetch form data
        String name = request.getParameter("name");
        String city = request.getParameter("city");
        String model = request.getParameter("model");
        String number = request.getParameter("number");

        // Prepare entity objects
        Customer customer = new Customer();
        customer.setName(name);
        customer.setCity(city);

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(model);
        vehicle.setNumber(number);
        vehicle.setCity(city);
        vehicle.setCustomer(customer);

        // Establish relationship
        customer.getVehicles().add(vehicle);

        // Hibernate session
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(customer); // Cascade saves vehicle too
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            return;
        }

        // Redirect to success page after registration
        response.sendRedirect("success.html");
    }
}

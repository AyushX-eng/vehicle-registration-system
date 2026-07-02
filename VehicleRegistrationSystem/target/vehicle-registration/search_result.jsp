<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.vrs.entity.Vehicle" %>
<%
    Vehicle vehicle = (Vehicle) request.getAttribute("vehicle");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Vehicle Details</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <div class="result-container">
        <h2>🔍 Search Result</h2>

        <%
            if (vehicle != null) {
        %>
            <div class="vehicle-info">
                <p><strong>🚘 Vehicle Model:</strong> <%= vehicle.getModel() %></p>
                <p><strong>🔢 Vehicle Number:</strong> <%= vehicle.getNumber() %></p>
                <p><strong>📍 Registered City:</strong> <%= vehicle.getCity() %></p>
            </div>
        <%
            } else {
        %>
            <p class="error">❌ No vehicle found with the given details.</p>
        <%
            }
        %>

        <br>
        <a href="index.html" class="link">🔙 Back to Welcome Page</a>
    </div>
</body>
</html>

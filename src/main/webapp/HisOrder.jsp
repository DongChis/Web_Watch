<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="css/productListAdmin.css" rel="stylesheet">
    <title>Lịch sử đơn hàng</title>
</head>
<body>
    <jsp:include page="Header.jsp" />

    <div class="containerr" style="max-width: 1200px; height: auto; margin: 0 auto 20px;">
        <div class="product-list-ad">

            <h1 style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
                Lịch sử đơn hàng
            </h1>

            <table border="1" style="width: 100%; border-collapse: collapse; text-align: left;">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Khách hàng</th>
                        <th>Số lượng</th>
                        <th>Giá trị đơn hàng</th>
                        <th>Ngày đặt hàng</th>
                        <th>Thông tin đơn hàng</th>
                        <th>Chỉnh sửa</th>
                        <th>Trạng thái</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${hisOrder}">
                        <tr>
                            <td>${order.orderID}</td>
                            <td>${order.customerName}</td>
                            <td>${order.cartItems.size()}</td>
                            <td>
                                <c:set var="totalOrderPrice" value="0" />
                                <c:forEach var="item" items="${order.cartItems}">
                                    <c:set var="totalOrderPrice" value="${totalOrderPrice + item.totalPrice}" />
                                </c:forEach>
                                ${totalOrderPrice} VNĐ
                            </td>
                            <td>${order.orderDate}</td>
                            <td>
                                <!-- Nút Xem chi tiết -->
                               <a href="orderDetail?orderID=${order.orderID}">Xem chi tiết</a>

                            </td>
                            <td>
                                <a href="editOrderByUser?orderID=${order.orderID}">Chỉnh sửa</a>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${editStatusMap[order.orderID] == true}">
                                        <span style="color: red;">Bị chỉnh sửa</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: green;">Chờ xử lý</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>

                        <!-- Hiển thị bảng chi tiết chỉnh sửa nếu đơn hàng được chọn -->
                        <c:if test="${param.selectedOrderID == order.orderID}">
                            <tr>
                                <td colspan="8">
                                    <div class="audit-log-container" style="margin-top: 10px;">
                                        <h2>Chi tiết thay đổi</h2>
                                        <table border="1" style="width: 100%; border-collapse: collapse; text-align: left;">
                                            <thead>
                                                <tr>
                                                    <th>Cột bị thay đổi</th>
                                                    <th>Giá trị cũ</th>
                                                    <th>Giá trị mới</th>
                                                    <th>Người thay đổi</th>
                                                    <th>Thời gian thay đổi</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="log" items="${auditLogs}">
                                                    <c:if test="${log.orderID == order.orderID}">
                                                        <tr>
                                                            <td><c:out value="${log.changedColumn}" /></td>
                                                            <td><c:out value="${log.oldValue}" /></td>
                                                            <td><c:out value="${log.newValue}" /></td>
                                                            <td><c:out value="${log.changedBy}" /></td>
                                                            <td><c:out value="${log.changeTime}" /></td>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="Footer.jsp" />
</body>
</html>

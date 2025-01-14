
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>


<!------ Include the above in your HEAD tag ---------->
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Register</title>
        
         <link href="css/register.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div id="logreg-forms">
            <form action="signup" method="post" class="form-signup">
                <h1 class="h3 mb-3 font-weight-normal" style="text-align: center">Sign up</h1>
                <div class="text-danger">${mess}</div>
                <input name="user" type="text" id="user-name" class="form-control" placeholder="User name" required autofocus>
                <input name="pass" type="password" id="user-pass" class="form-control" placeholder="Password" required>
                <input name="repass" type="password" id="user-repeatpass" class="form-control" placeholder="Repeat Password" required>
                <input name="email" type="email" id="user-email" class="form-control" placeholder="Email" required>
                <button class="btn btn-primary btn-block" type="submit"><i class="fas fa-user-plus"></i> Sign Up</button>
                <a href="login.jsp" id="cancel_signup" class="btn btn-secondary btn-block"><i class="fas fa-angle-left"></i> Back</a>
            </form>
        </div>
    </body>
</html>

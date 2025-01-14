
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Login</title>
             <link href="css/login.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div id="logreg-forms">
            <form class="form-signin" action="login" method="post">
                <h1 class="h3 mb-3 font-weight-normal" style="text-align: center">Sign in</h1>
                <div class="text-danger">${mess}</div>
                <input name="user" type="text" id="inputEmail" class="form-control" placeholder="Username" required autofocus>
                <input name="pass" type="password" id="inputPassword" class="form-control" placeholder="Password" required>
                <div class="form-group form-check">
                    <input name="remember" value="1" type="checkbox" class="form-check-input" id="exampleCheck1">
                    <label class="form-check-label" for="exampleCheck1">Remember me</label>
                </div>
                <button class="btn btn-success btn-block" type="submit"><i class="fas fa-sign-in-alt"></i> Sign in</button>
                <hr>
                <a href="Register.jsp" class="btn btn-primary btn-block" id="btn-signup"><i class="fas fa-user-plus"></i> Sign up New Account</a>
            </form>
        </div>
      
    </body>
</html>

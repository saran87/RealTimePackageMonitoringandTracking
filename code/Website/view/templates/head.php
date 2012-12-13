<!DOCTYPE html>
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title><?php echo $title?></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="connect 4 game">
    <meta name="author" content="Saravana kumar">

    <!-- Le styles -->
    <link href= "view/css/bootstrap.css" rel="stylesheet"> 
	<link href= "view/css/site.css" rel="stylesheet">
	<link href= "http://twitter.github.com/bootstrap/assets/css/bootstrap-responsive.css" rel="stylesheet">
    <style type="text/css">
	@media (min-width: 768px){
		  body {
			padding-top: 60px;
			padding-bottom: 40px;
		  }
		  .sidebar-nav {
			padding: 9px 0;
		  }
	  }
    </style>
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Le fav and touch icons -->
  <!--  <link rel="shortcut icon" href="http://twitter.github.com/bootstrap/assets/ico/favicon.ico"> -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="http://twitter.github.com/bootstrap/assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="http://twitter.github.com/bootstrap/assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="http://twitter.github.com/bootstrap/assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="http://twitter.github.com/bootstrap/assets/ico/apple-touch-icon-57-precomposed.png">
  </head>

  <body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner border_orange">
        <div class="container">
		 <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="#">Real Time Package Tracking and Monitoring</a>
          <div class="nav-collapse collapse">
            <div class="navbar-text pull-right">
				<?php if(isset($_SESSION['name'])):?>
					<div class="btn-group">
					<button class="btn"><i class="icon-user"></i><?php echo $_SESSION['name'] ?></button>
				<?php else :?>
					<div class="btn-group hide">
					<button class="btn userName" id="userName"><i class="icon-user"></i></button>
				 <?php endif; ?>
				  <button class="btn dropdown-toggle" data-toggle="dropdown">
					<span class="caret"></span>
				  </button>
				  <ul class="dropdown-menu">
					<!-- dropdown menu links -->
					<li class="divider"></li>
					<li><a tabindex="-1" href="logout.php" >Log out</a></li>
				  </ul>
				</div>
				<div id="loginButton" class="<?php if(isset($_SESSION['name'])) { echo "hide";} ?>">
				 <button type="button" class="btn btn-link" onClick="showLoginModal()">Login</button>
				</div>
            </div>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
	
	<div class="modal hide" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	 
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	 
	  <div class="modal-body">
	  <div id="ajaxProgress" class="ajaxloader hide">
	  </div>
	  <div id="login_message" class="alert alert-error hide">
	  </div>
	  <div id="login">
		<form class="form-horizontal" id="login_form">
		  <div class="control-group">
			<label class="control-label" for="inputEmail">Email</label>
			<div class="controls">
			  <input type="text" id="inputEmail" class="email" name="email" placeholder="Email">
			  <span  class="label  label-important hide">Enter proper email address</span>
			</div>
		  </div>
		  <div class="control-group">
			<label class="control-label" for="inputPassword">Password</label>
			<div class="controls">
			  <input type="password" id="inputPassword" class="password" name="password" placeholder="Password">
			  <span  class="label  label-important hide">Password should have minimum of 8 characters</span>
			</div>
		  </div>
		  <div class="control-group">
			<div class="controls">
			  <button type="submit" class="btn">Sign in</button>
			   <a href="#" onclick="showRegisterForm()">Create New Account </a>
			</div>
		  </div>
		</form>
	 </div>
	 <div id="register" class="hide">
			<form id="register_form" class="form-horizontal" >
			  <div class="control-group">
				  <label class="control-label" for="name">Name</label>
				  <div class="controls">
					<input type="text" name="name" class="name" placeholder="Name">
					<span class="label  label-important hide">No numeric characters allowed</span>	
				  </div>
			  </div>
			   <div class="control-group">
				<label class="control-label" for="email">Email</label>
				<div class="controls">
					<input type="text" name="email" class="email" placeholder="Email">
					<span  class="label  label-important hide">Enter proper email address</span>
				</div>
			   </div>
			   <div class="control-group">
				  <label class="control-label" for="password">Password</label>
				  <div class="controls">
					<input type="password" name="password" class="password" placeholder="Password">
					<span  class="label  label-important hide">Password should have minimum of 8 characters</span>
				</div>
			  </div>
			  <div class="control-group">
				<div class="controls">
					<button type="submit" class="btn" style="display:block">Register</button>
					  <a href="#" onclick="showLogin()">Already Have Account</div>
			  </div>
			</form>
		</div>
	  </div>
	  <div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal">Close</a>
	  </div>
	</div>
	
	<div class="modal hide" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<div class="modal-body">
		  <div id="error_message" class="alert alert-error">
		  </div>
	    </div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">Close</a>
		</div>
	</div>
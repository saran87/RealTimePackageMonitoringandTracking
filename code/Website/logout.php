<?php
session_start();
if(isset($_SESSION['name']))
  unset($_SESSION['name']);

//unset the cookie
setcookie("token", "", time()-3600);
echo "successfully logged out";
?>

<?php

// Getting the root excution  path
define('ROOT_PATH',getcwd());

//banner file info constant
define('BANNER_PATH',ROOT_PATH . '/banner/');
//Modules file path
define('MODULE_PATH',ROOT_PATH . '/modules/');
//Theme file path
define('THEME_PATH',ROOT_PATH . '/themes/');
//Controller Path 
define('CONTROLLER_PATH', ROOT_PATH . '/controller/');
//View Path 
define('VIEW_PATH', ROOT_PATH . '/view/');
define('VIEW_TEMPLATE_PATH', VIEW_PATH . '/templates/');
//Sets the site base url
define('SITE_URL',$_SERVER["PHP_SELF"]);
define('HOST',$_SERVER["HTTP_HOST"]);


/* ****************************************************************************** 
* Front page Configuration
********************************************************************************* */
define('FRONT_PAGE', 'home/index.php');
define('LOGIN_PAGE', 'index.php?s=login');

//Error Indicators
define('PAGE_NOT_FOUND', '404');
define('DB_ERROR', 'DB Error');
define('LOGIN', 'Login');

//Error page
define('PAGE_404','404.html');

/* ****************************************************************************** 
* Error Handler Configuration
********************************************************************************* */
define('IS_LOG_ENABLED', true);
define('ERROR_LOG_FILE', ROOT_PATH . '/logs/error.log');

?>
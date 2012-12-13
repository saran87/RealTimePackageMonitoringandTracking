<?php
include(VIEW_TEMPLATE_PATH . 'head.php');
?>
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span2">
      <!--Sidebar content-->
	  <div class="dropdown">
	 <ul class="nav  nav-list sidenav" role="menu" aria-labelledby="dropdownMenu" style="display:block">
		  <li><a tabindex="-1" href="#"><i class="icon-chevron-right pull-right"></i>Home</a>
		  </li>
		  <li><a tabindex="2" href="#"><i class="icon-chevron-right pull-right"></i>Track Package</a></li>
		  <li><a tabindex="3" href="#"><i class="icon-chevron-right pull-right"></i>Map</a></li>
		
		  <li><a tabindex="5" href="#"><i class="icon-chevron-right pull-right"></i>Alert</a></li>
	  </ul>
	  </div>
    </div>
    <div class="span8">
      <!--Body content-->
	  <section id="main_content">
		 <!--Body content-->
	  <section id="main_content">

		  <div id="map_canvas" class="box" style="height:500px" >map div</div>
		
		</section>
	<section id="challengeSection" >
		  </section>
		   <section id="scoreSection" >
		  </section>
	
	 </section>
	</div>
	<div class="span2">
	<!--chat content-->
	</div>
  </div>
</div>

<?php
include(VIEW_TEMPLATE_PATH . 'footer.php');
?>
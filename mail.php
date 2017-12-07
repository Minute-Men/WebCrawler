<?php
	mail($argv[1], 'Web Crawler', utf8_decode(urldecode($argv[2])));
?>

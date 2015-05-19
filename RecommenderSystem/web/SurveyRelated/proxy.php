<?php

function fetchURL() {

  if (isset($_GET["url"])) { // Check for the presence of our expected POST variable.

    $url = filter_var($_GET["url"], FILTER_SANITIZE_URL); // Be careful with posting variables.
    $cache_file = "cache/".hash('md5', $url).".html"; // Create a unique name for the cache file using a quick md5 hash.

    // If the file exists and was cached in the last 24 hours...
    if (file_exists($cache_file) && (filemtime($cache_file) > (time() - 8640000 ))) { // 8,600,400 seconds = 2400 hours.

      $file = file_get_contents($cache_file); // Get the file from the cache.
      print $file; // echo the file out to the browser.
    }

    else {

      $file = file_get_contents($url); // Fetch the file.
      file_put_contents($cache_file, $file, LOCK_EX); // Save it for the next requestor.
      print $file; // echo the file out to the browser.
    }
  }

}

fetchURL(); // Execute the function

?>
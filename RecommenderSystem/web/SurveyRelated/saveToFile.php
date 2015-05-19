<?php
//$myfile = fopen("saveData", "w+") or die("Unable to open file!");
if (isset($_GET["data"]) && isset($_GET["file"])) { // Check for the presence of our expected POST variable.

    $txt = $_GET["data"]; // Be careful with posting variables.
    file_put_contents($_GET["file"], $txt . "\n", FILE_APPEND | LOCK_EX);
}
?>
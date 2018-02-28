<?php
    $myFile = "comms/toRobot.json";
    $fh = fopen($myFile, "w") or die("Can't use file...");
    $stringData = $_POST["data"];
    fwrite($fh, $stringData);
    fclose($fh)
?>

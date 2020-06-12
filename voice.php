<?php
   define('DB_SERVER','localhost');
   define('DB_USER','');
   define('DB_PASS' ,'');
   define('DB_NAME', 'id14005179_data');
   $con = mysqli_connect(DB_SERVER,DB_USER,DB_PASS,DB_NAME);
   // Check connection
   if (mysqli_connect_errno())
   {
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }

   $name =$_GET['name'];
   $department=$_GET['department'];
   $result = mysqli_query($con,"SELECT Number FROM Directory where Name='$name'");
   $row = mysqli_fetch_all($result);
   $data = $row[0][0];
    if($data);
    
    else
    {
    if($row[1][0])
   {
           if($department){
       $result2 = mysqli_query($con,"SELECT Number FROM Directory where Name='$name' and Department='$department'");
       $row2= mysqli_fetch_array($result2);
          $data =$row2[0];
          }
          else{
           $data="More than a user with that name exists.Please enter the corresponding department code";
           }
   }
    }
   if($data){
      echo $data;
   }
   else
   {
       echo 'No user with those credentials exists.';
   }
   mysqli_close($con);
?>


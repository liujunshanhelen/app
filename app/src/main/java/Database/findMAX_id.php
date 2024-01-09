<?php
/**
 *找数据库某个表中最大的id
 * @param tableName     数据库的表名及参数名   eg:table(id,name,age)
 * @return  0代表成功 1代表失败
 */
require_once __DIR__ . '/connect.php';//引用connect.php
require_once __DIR__ . '/header.php';//引用connect.php

//等待传入的参数
//$type = $_POST["id"];
$type="get_max";

//建立连接
$link = connectToDB();
//查看是否连接失败
if ($link->connect_error) {
    die($link->connect_error);
}
$result = returnClassBytype($type);
//$id=$result->getName(1);
//构造SQL语句
$sql="select id from person where id=(SELECT max(id) FROM person)";//定义一个查询语句
//echo $sql;//显示插入语句
$res = $link->query($sql);
$data=array();
if($res){
//echo "查询成功";
    echo json_encode(mysqli_fetch_assoc($res));
//    while ($row = mysqli_fetch_assoc($res))
//    {
//        $result = returnClassBytype($type);
//        error_reporting(0);
//        $result->setData(1,$row[$result->getName(1)]);
//        $data[]=$result;
//    }
//    $json = json_encode($data);//把数据转换为JSON数据.
//    echo "{".$type.":".$json."}";
}else{
    echo "查询失败";
}
closeDB();

//    $result->setData(1, "1");
//    $data[] = $result;
//    $json = json_encode($data);//把数据转换为JSON数据.
//    echo "{" . "insert" . ":" . $json . "}";
//}
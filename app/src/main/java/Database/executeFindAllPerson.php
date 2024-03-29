<?php
/**
 *查询数据库中全部数据
 * @param value      想要查询的值
 * @param tableName     想要查询的数据库的表名
 * @param index      想要查询数据库的表的哪一列的名字
 * @return   返回查询到的所有行
 */
require_once __DIR__ . '/connect.php';//引用connect.php
require_once __DIR__ . '/header.php';//引用connect.php

//等待传入的参数
$tableName=$_POST["tableName"];
//$type=$_POST["type"];
//$tableName="record_all";
$type="search_all";

//建立连接
$link = connectToDB();
//查看是否连接失败
if($link->connect_error){
    die($link->connect_error);
}
//构造SQL语句
$sql = "select * from $tableName";
$res=$link->query($sql);
//构造成JSON语言格式
$data=array();
if($res){
//echo "查询成功";
    while ($row = mysqli_fetch_assoc($res))
    {
        $result = returnClassBytype($type);
        error_reporting(0);
        for ($i=0;$i<2;$i++){
            $result->setData($i+1,$row[$result->getName($i+1)]);
        }

        $data[]=$result;
    }
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{".$type.":".$json."}";
}else{
    echo "查询失败";
}
closeDB();

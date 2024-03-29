<?php
/**
 *查询数据库中数据
 * @param value      想要查询的值
 * @param tableName     想要查询的数据库的表名
 * @param index      想要查询数据库的表的哪一列的名字
 * @return   返回查询到的所有行
 */
require_once __DIR__ . '/connect.php';//引用connect.php
require_once __DIR__ . '/header.php';//引用connect.php

//等待传入的参数
$index = $_POST["index"];

//$index=1;
$type="power";

$link = connectToDB();

if ($link->connect_error) {
    die($link->connect_error);
}

$sql = "select power from person where id=$index";
//echo $sql;//显示查询语句
$res = $link->query($sql);
//构造成JSON语言格式
$data = array();
if ($res != null) {
//echo "查询成功";
    while ($row = mysqli_fetch_assoc($res)) {
        $result = returnClassBytype($type);
        error_reporting(0);
        $result->setData(1, $row[$result->getName(1)]);

        $data[] = $result;
    }
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{" . $type . ":" . $json . "}";
} else {
    echo "false";
}
closeDB();

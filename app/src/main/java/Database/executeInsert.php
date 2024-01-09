<?php

/**
 *向数据库中插入数据
 * @param value      想要查询的值
 * @param tableName     数据库的表名及参数名   eg:table(id,name,age)
 * @param value      要传入的值   字符串需要打单引号          eg:1,'sxz',20
 * eg:dbBean.execQuery("nameandpassword(user_name,user_password,age)","'yzj','654321',10");
 * @return  0代表成功 1代表失败
 */
require_once __DIR__ . '/connect.php';//引用connect.php
require_once __DIR__ . '/header.php';//引用connect.php

//等待传入的参数
$value=$_POST["value"];
$tableName = "person(name,tel,id_number,secret,power,face,home_location)";
//$value = "'dongzhaohui','152','230123','654321',1,2,'heilongjiang'";
$type="sign_up";

//建立连接
$link = connectToDB();
//查看是否连接失败
if ($link->connect_error) {
    die($link->connect_error);
}
//构造SQL语句
$sql = "insert into safe.$tableName values($value)";
echo $sql;//显示插入语句
$res = $link->query($sql);
//构造成JSON语言格式
if($res)
    echo "true";
else
    echo "false";
closeDB();
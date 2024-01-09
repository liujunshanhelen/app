<?php
function returnClassBytype($type){
    switch ($type){
        case "search_name"://根据姓名查询出行记录
            return new record_one();
        case "search_all"://全部出行信息
            return new record_all();
        case "search_shan"://扇形图数据
            return new shan_data();
        case "sign_up"://注册
            return new sign_up();
        case "login":
            return new login();
        case "power":
            return new limit();
    }
}


class record_one implements header{
    public $time="";
    public $tem="";
    public function setData($idNum, $value)
    {
        switch ($idNum){
            case 1:
                $this->time=$value;
                break;
            case 2:
                $this->tem=$value;
                break;
            default:break;
        }
    }
    function getName($idNum)
    {
        switch ($idNum){
            case 1:
                return"time";
            case 2:
                return"tem";
            default:break;
        }

    }
}
class record_all implements header{
    public $time="";
    public $name="";
    public function setData($idNum, $value)
    {
        switch ($idNum){
            case 1:
                $this->time=$value;
                break;
            case 2:
                $this->name=$value;
                break;
            default:break;
        }
    }
    function getName($idNum)
    {
        switch ($idNum){
            case 1:
                return"time";
            case 2:
                return"name";
            default:break;
        }
    }
}
class shan_data implements header{

    public $time="";
    public function setData($idNum, $value)
    {
        switch ($idNum){
            case 1:
                $this->time=$value;
                break;
            default:break;
        }
    }
    function getName($idNum)
    {
        switch ($idNum){
            case 1:
                return"time";
            default:break;
        }

    }
}
class sign_up implements header{

    function setData($idNum, $value)
    {
        // TODO: Implement setData() method.
    }

    function getName($idNum)
    {
        // TODO: Implement getName() method.
    }
}
class login implements header{

    public $power="";
    public $name="";
    public function setData($idNum, $value)
    {
        switch ($idNum){
            case 1:
                $this->power=$value;
                break;
            case 2:
                $this->name=$value;
                break;
            default:break;
        }
    }
    function getName($idNum)
    {
        switch ($idNum){
            case 1:
                return"power";
            case 2:
                return"name";
            default:break;
        }

    }
}
class limit implements header{
    public $power="";
    public function setData($idNum, $value)
    {
        switch ($idNum){
            case 1:
                $this->power=$value;
                break;
            default:break;
        }
    }
    function getName($idNum)
    {
        switch ($idNum){
            case 1:
                return"power";
            default:break;
        }
    }
}
interface header{
    function setData($idNum,$value);
    function getName($idNum);
}
<?php

class Servicemodel extends CI_Model
{
	function __construct()
	{
		parent::__construct();
    }
		
    function addServiceToDB($data){
        $dbHandle = $this->db;
        if(empty($data)){
            return;
        }
        $dbHandle->insert('alertsConfig',$data);
        $insert_id = $dbHandle->insert_id();
        return $insert_id;
    }


    function fetchListOfServices(){
        // $dbHandle = $this->getReadHandle();
        $dbHandle = $this->db;
        $sql = "SELECT * from alertsConfig where status = 'live'";
        $query = $dbHandle->query($sql);
        return $query->result_array();

    }

    function deleteService($id=null){
        if(empty($id)) return;
        // $dbHandle = $this->getWriteHandle();
        $dbHandle = $this->db;
        $sql = "update alertsConfig set status = 'history' where id = $id";
        $query = $dbHandle->query($sql);
    }

    function fetchServiceData($id=null){
        if(empty($id)) return;
        // $dbHandle = $this->getReadHandle();
        $dbHandle = $this->db;
        $sql = "select * from alertsConfig where status = 'live' and id=$id";
        $query = $dbHandle->query($sql);
        $result_array = $query->row_array();
        return $result_array;
    }

    
}
<?php
class Configure extends CI_Controller{


    public function __construct(){
        parent::__construct();
        $this->load->model('Servicemodel');
        $this->serviceModel = new ServiceModel();
    }

    public function index(){

        $data['servicesList'] = $this->serviceModel->fetchListOfServices();
        $this->load->view('configure',$data);
    }

    public function formGeneratorForService($serviceName=""){
        switch ($serviceName) {
            case 'solr':
                $this->load->view('solr');
                break;
            case 'web_api':
                $this->load->view('web_api');
                break;
            case 'redis':
                $this->load->view('redis');
                break;
            case 'memcache':
                $this->load->view('memcache');
                break;
            case 'elasticsearch':
                $this->load->view('elasticsearch');
                break;
            case 'mysql':
                $this->load->view('mysql');
                break;
            case 'rabbitmq_server':
                $this->load->view('rabbitmq_server');
                break;
            default:
                break;
        }
    }

    public function addServiceDataToDB(){

        $result = $this->validateServiceAddPostData($this->input->post());
        $response = array();
        if(is_array($result)){
            $insertResult = $this->serviceModel->addServiceToDB($result);
            if(empty($insertResult)){
                $response['error'] = 1;
                $response['msg'] = "Some error occured. Please try again";    
            }else{
                $response['success'] = 1;
            }

        }else{
            $response['error'] = 1;
            $response['msg'] = $result;
        }
        echo json_encode($response);
    }

    public function validateServiceAddPostData($postData){

        // Fetch Data & store in an Array
        $formattedData = array();
        $formattedData['serviceName'] = $postData['serviceName'];
        $formattedData['host'] = $postData['host'];
        $formattedData['port'] = $postData['port'];        
        $formattedData['serviceType'] = $postData['serviceList'];        
        $formattedData['unique_id'] = $postData['host']."_".$postData['port']."_".$postData['serviceList']."_".time();
        $formattedData['port_monitoring']['status'] = ($postData['port_monitoring'] == "true") ? "on" : "off";        
        $formattedData['data_monitoring']['status'] = ($postData['data_monitoring'] == "true") ? "on" : "off";
        
        unset($postData['serviceName']);
        unset($postData['host']);
        unset($postData['port']);
        unset($postData['serviceList']);
        unset($postData['port_monitoring']);
        unset($postData['data_monitoring']);
        
        foreach ($postData as $key => $value) {
            $formattedData['data_monitoring'][$key] = $value;
        }

        // Verify Data

        if(empty($formattedData['serviceType']) || $formattedData['serviceType'] == "-1"){
            return "Please choose service name";
        }

        if(empty($formattedData['host'])){
            return "Host Cannot be empty";
        }
        if(empty($formattedData['port'])){
            return "Port Cannot be empty";
        }
        if(!is_numeric($formattedData['port'])){
            return "Port should be numeric";
        }
        if(empty($formattedData['serviceType'])){
            return "Service Type is empty";
        }

        
        if($formattedData['data_monitoring']['status'] == "on"){
            if(isset($formattedData['data_monitoring']['url']) && trim(empty($formattedData['data_monitoring']['url']))){
                return ucfirst($formattedData['serviceType'])." URL is empty";
            }

            if(isset($formattedData['data_monitoring']['query']) && trim(empty($formattedData['data_monitoring']['query']))){
                return ucfirst($formattedData['serviceType'])." query is empty";
            }

            if(isset($formattedData['data_monitoring']['responseTimeCheck']) && $formattedData['data_monitoring']['responseTimeCheck'] == "true"){
                if(empty($formattedData['data_monitoring']['responseTime'])){
                        return "Response time is empty";
                }
            }

            if(isset($formattedData['data_monitoring']['responseCodeCheck']) && $formattedData['data_monitoring']['responseCodeCheck'] == "true"){
                if(empty($formattedData['data_monitoring']['responseCode'])){
                        return "Response Code is empty";
                }
            }

            if(empty($formattedData['data_monitoring']['exceptionCheckCount'])){
                return "Failure count is empty";
            }

            if(empty($formattedData['data_monitoring']['timeout'])){
                return "Timeout is empty";
            }

            if(isset($formattedData['data_monitoring']['dbconn']) && trim(empty($formattedData['data_monitoring']['dbconn']))){
                return ucfirst($formattedData['serviceType'])." DB Connection String is empty";
            }            

            if(isset($formattedData['data_monitoring']['username']) && trim(empty($formattedData['data_monitoring']['username']))){
                return ucfirst($formattedData['serviceType'])." username is empty";
            }            

            if(isset($formattedData['data_monitoring']['password']) && trim(empty($formattedData['data_monitoring']['password']))){
                return ucfirst($formattedData['serviceType'])." password is empty";
            }            

            if(isset($formattedData['data_monitoring']['sql']) && trim(empty($formattedData['data_monitoring']['sql']))){
                return ucfirst($formattedData['serviceType'])." Query is empty";
            }            

            if(isset($formattedData['data_monitoring']['queueName']) && trim(empty($formattedData['data_monitoring']['queueName']))){
                return ucfirst($formattedData['serviceType'])." queue name is empty";
            }            

            
        }

        if(isset($formattedData['data_monitoring']['postRequestCheck']) && $formattedData['data_monitoring']['postRequestCheck'] == "true"){
            unset($formattedData['data_monitoring']['postRequestCheck']);
            $customPostData = array();
            foreach ($formattedData['data_monitoring']['postKey'] as $key => $value) {
                $customPostData[$value] = $formattedData['data_monitoring']['postValue'][$key];
            }
            $formattedData['data_monitoring']['postRequestCheck'] = $customPostData;
        }
        unset($formattedData['data_monitoring']['postKey']);
        unset($formattedData['data_monitoring']['postValue']);

        $formattedData['port_monitoring'] = json_encode($formattedData['port_monitoring']);
        $formattedData['data_monitoring'] = json_encode($formattedData['data_monitoring']);

        return $formattedData;

    }

    function fetchListOfServices(){
        $data['servicesList'] = $this->serviceModel->fetchListOfServices();
        $this->load->view('rightPanel',$data);
    }

    function deleteService(){
        $id = $this->input->post('id');

        if(empty($id)){
            echo "Some Error occured";
        }
        $this->serviceModel->deleteService($id);
        echo "success";
    }

    function viewService($id=0){
        if(empty($id)){
            return "error";
        }
        $data['serviceData'] = $this->serviceModel->fetchServiceData($id);
        $this->load->view('serviceDetail',$data);
    }

}

//http://svntrac.infoedge.com:8080/Shiksha/changeset/81429

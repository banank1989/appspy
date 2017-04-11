<style type="text/css">

#wrap {width:100%;margin:0 auto;height: 80%;}
#left_col {float:left;width:50%;background-color: #fff;margin-top: 10px;min-height: 90%;margin-left: 10px;}
#right_col {float:right;width:46%; background-color: #fff;margin-top: 10px;min-height: 90%;margin-right: 10px;}
.erm{margin-left: 30px;}
.error{color: red;font-size:10px;}
.loader{}
.header{background-color: #E6EBF0;height: 60px;padding: 5px;text-align: center; }
.header > .head, .serviceDetail > .head{color: #1C426A; text-transform: uppercase;}
#left_col .head, #right_col .head{background-color: #1D204B;height: 20px;color: #fff;text-align: center; padding-top:7px;padding-bottom: 5px;font-family: "Times New Roman", Times, serif; font-size: 18px;}
body{padding: 0,margin:0;background-color: #fbfbfb;font-family: "Times New Roman", Times, serif;}
.overlay{display: none; position: absolute;top: 0%;left: 0%;width: 100%;height: 100%;background-color: black;z-index:101;-moz-opacity: 0.8;opacity:.80;	filter: alpha(opacity=80);}
.overlayMidContentClass{background-color: white;height: 9%;left: 45%; overflow: auto; padding: 12px;position: absolute;top: 43%;width: 5%;z-index: 102;display: none;border-radius: 25px;}
.overlayMidContentDynamic{top:10% !important; left: 10% !important; width: 80% !important; height: 80% !important;}
.icon_act img{cursor: pointer;}
tr,td{height: 30px;padding: 5px;height: 40px;}
td{padding-left: 10px !important}
</style>
<?php
	$clientIP=$_SERVER['SERVER_NAME'];
	define("CSSURL", $clientIP);
	define("JSURL", $clientIP);
?>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href=<?php echo base_url()?>css/pure-min.css />	
	<title>App SPY</title>
</head>
<body>
<div class="header">
	<h2 class="head">Third party Services Monitoring</h2>
</div>
<div class="overlay"></div>
<div class="overlayMidContent overlayMidContentClass"><img src=<?php echo base_url();?>/images/loader.gif></div>
<div class="overlayMidContentDynamic overlayMidContentClass"><img src=<?php echo base_url();?>/images/loader.gif></div>
<div id="wrap">
    <div id="left_col">
   	<?php $this->load->view('leftPanel');?>
    </form>
    </div>
    <div id="right_col">
        <?php $this->load->view('rightPanel');	?>
    </div>
</div>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script type="text/javascript" src=<?php echo base_url()?>/js/main.js></script>

<script type="text/javascript">
	base_url = '<?=base_url()?>';
	var appSpyConfig = new AppSpyConfig();
	appSpyConfig.bindElementsAndInitialize();
	//spearConfig.fetchServicesList();
	//showLoader();
</script>

</html>
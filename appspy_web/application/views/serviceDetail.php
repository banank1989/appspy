<div class="ic_close" style="float: right;cursor: pointer;"><img src=<?php echo base_url();?>images/ic_close.png class="closeService"></div>
<div class="serviceDetail">
	<h1 class="head"><?php echo $serviceData['serviceName'];?> ( <?php echo $serviceData['serviceType'];?> ) </h1>
	<table border="1" cellpadding="10" cellspacing="0">
		<tr>
			<th style="width: 25%;">Host</th><td><?php echo $serviceData['host'];?></td>
		</tr>
		<tr style="background-color:#f1f1f1 ">
			<th>Port</th><td><?php echo $serviceData['port'];?></td>
		</tr>
		<tr>
			<th>Port Monitoring</th>
			<td><?php
				 $port_monitoring = (array)json_decode($serviceData['port_monitoring']);
				 if($port_monitoring['status'] == "on"){
				 	echo "Enabled";
				 }else{
				 	echo "Disabled";
				 }
				 ?>
			 </td>
		</tr>
		<tr style="background-color:#f1f1f1 ">
			<th>Data Monitoring</th>
			<td><?php
				 $data_monitoring = (array)json_decode($serviceData['data_monitoring']);
				 if($data_monitoring['status'] == "on"){
				 	echo "Enabled";
				 }else{
				 	echo "Disabled";
				 }
				 
				?>

			 </td>
		</tr>
		<?php
			if($data_monitoring['status'] == "on"){
				$cnt = 0;
				unset($data_monitoring['status']);
				foreach ($data_monitoring as $key => $value) {
					$cnt++;
					$bk = "";
					if($cnt % 2 == 0){
						$bk = "background-color:#f1f1f1";
					}
					?>
					<tr style="<?php echo $bk;?>">
						<?php if($key == "postRequestCheck"){
							?>
							<th>Post</th>
							<td>
								<?php if(gettype($value) == "string"){
									echo "None";
								  }else if(gettype($value) == "object"){
								  	   $value = (array)	$value;
								  	   ?>
								  	   <table>
								  	   <?php
								  	   foreach ($value as $postKey => $postValue) {
								  	   		?>
								  	   			<tr>
								  	   				<th style="width: 15%;text-align: left;"><?php echo $postKey;?></th>
								  	   				<td><?php echo $postValue;?></td>
								  	   			</tr>
								  	   		<?php
								  	   }
								  	   ?>
						  	   			</table>
								  	   <?php
								  }
								  ?>
							</td>
							<?php
						}else{
							?>
							<th>
							<?php echo $key;?>
						</th>
						<td>
							<?php echo $value;?>
						</td>
							<?php
						}
						?>
					</tr>
					<?php
				}
			}
		?>
	</table>
</div>

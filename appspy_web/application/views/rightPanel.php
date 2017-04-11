<div class="head">Services List</div>
<table border="1" style="width: 100%;margin-top: 30px;" cellpadding="10" cellspacing="0">
<tr style="background-color: #E6EBF0;"><th>Name</th><th>Service</th><th>&nbsp;</th></tr>
<?php
	$count = 0;
	foreach ($servicesList as $key => $value) {
		$count++;
		$background_color = "";
		if($count % 2 == 0){
			$background_color = "background-color:#f1f1f1";
		}
		?>
			<tr style="<?php echo $background_color?> ">
				<td style="width: 40%; padding-left: 10px;"><?php echo ucfirst($value['serviceName']);?></td>
				<td style="width: 40%; padding-left: 10px;"><?php 
				$u = $value['unique_id'];
				$u_arr = explode("_", $u);
				unset($u_arr[count($u_arr)-1]);
				echo implode("_", $u_arr);
				?></td>
				
				<td style="width: 20%;text-align: center;">
					<div class="icon_act">
						<img src=<?php echo base_url()?>/images/ic_view.png class="viewService" serviceId = <?php echo $value['id'];?>>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<img src=<?php echo base_url()?>images/ic_delete.png class="deleteService" serviceId = <?php echo $value['id'];?>>
					</div>
				</td>
			</tr>		
		<?php
	}
	if($count == 0){
		?>
			<td colspan="4" style="text-align: center;"> No Service added </td>
		<?php
	}
?>
</table>



<?php
$tilkobling = mysqli_connect("localhost","root","","attraksjoner");
$sql = "SELECT attraksjonNavn, tekstOm, aapningstider, priser FROM attraksjoner.attraksjon";
$datasett = $tilkobling->query($sql);

if(isset($_POST["submit"]))
{
    $sql = sprintf("INSERT INTO attraksjon(attraksjonNavn, tekstOm, aapningstider, priser) VALUES('%s','%s','%s','%s')",
	                $tilkobling->real_escape_string($_POST["attraksjonNavn"]),
	                $tilkobling->real_escape_string($_POST["tekstOm"]),
					$tilkobling->real_escape_string($_POST["aapningstider"]),
					$tilkobling->real_escape_string($_POST["priser"])
	                );
    $tilkobling->query($sql);
	
	header("Location: attraksjon_lagt_til.html");
    
}
?>
<!DOCKTYPE html>
<html>
    <head>
		<meta charset="utf-8" />
		<link rel="stylesheet" type="text/css" href="stil.css" />
	</head>
	<body>
        <div id="wrapper">
            <header id="header" title="">
				<img style="display: block; margin-left: auto; margin-right: auto;" src="bilder/banner.jpg" width="720" height="200" alt="Ротур"/>
				
				<div class="topnav">
				    <a href="index.html">Дома</a>
					<a href="hotel.html">Отель</a>
					<a href="restaurant.html">Ресторан</a>
                    <a class="active" href="attraksjoner.php">Аттракцион</a>
                    <a href="informasjon.html">Информация</a>
                </div>
		    </header>
			<main>
			    <table>
				    <tr>
						<th>Аттракцион</th>
						<th>Tекст</th>
						<th>Время работы</th>
						<th>Прейскурант</th>
					</tr>
					<?php while($rad = mysqli_fetch_array($datasett)) { ?>
					<tr>
						<td><?php echo $rad["attraksjonNavn"]; ?></td>
						<td><?php echo $rad["tekstOm"]; ?></td>
						<td><?php echo $rad["aapningstider"]; ?></td>
						<td><?php echo $rad["priser"]; ?></td>
				    </td>
					<?php } ?>
				</table>
				 
			    <form method="post">
		            <label for="attraksjonNavn">Аттракцион:</label>
		            <input type="VARCHAR(45)" name="attraksjonNavn" id="attraksjonNavn" />
		            <br />
		            <label for="tekstOm">Tекст:</label>
		            <input type="text(400)" name="txtOm" id="txtOm" />
		            <br />
					<label for="aapningstider">Время работы:</label>
		            <input type="text(400)" name="aapningstider" id="aapningstider" />
		            <br />
					<label for="priser">Прейскурант:</label>
		            <input type="text(400)" name="priser" id="priser" />
		            <br />
		            <button type="submit" name="submit">Добавить аттракцион</button>   
	            </form>
		    </main>
	    </div>
	</body>
</html>
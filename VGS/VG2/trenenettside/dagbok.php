<?php
    $tilkobling = mysqli_connect("localhost","root","","trening");
	$tilkobling->set_charset("utf8");
    $sql = "SELECT *
	FROM aktivitet;";
	$datasett = $tilkobling->query($sql);
?>

<!DOCKTYPE html>
<html>
    <head>
	    <title>Trening med mening</title>
		<link rel="stylesheet" type="text/css" href="stil.css" />
		<meta charset="utf-8" />
	</head>
	
	<body>
        <div id="wrapper">
            <nav>
                <ul>
                    <li><a href="index.html">Om meg</a></li>
                    <li><a href="styrke.html">Styrke</a></li>
                    <li><a href="utholdenhet.html">Utholdenhet</a></li>	
                    <li><a href="kosthold.html">Kosthold</a></li>	
                    <li><a href="dagbok.php">Dagbok</a></li>
                    <li><a href="registrere_aktivitet.php">Registrere aktivitet</a></li>
                </ul>
		    </nav>
			<header>
			    <h1>Trening for alle</h1>
			</header>
			
			<main>
			    <table>
				    <tr>
					    <th>Aktivitetsid</th>
						<th>Trening</th>
						<th>Dato</th>
					</tr>
					<?php while($rad = mysqli_fetch_array($datasett)) { ?>
					    <tr>
						    <td><?php echo $rad["aktivitetsid"]; ?></td>
							<td><?php echo $rad["trening"]; ?></td>
							<td><?php echo $rad["dato"]; ?></td>
					    </tr>
					<?php } ?>
				</table>
			</main>
		</div>	
	</body>
</html>

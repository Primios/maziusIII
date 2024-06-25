<?php
$objetPdo = new PDO('mysql:host=mysql21; dbname=serveurmazius_bdd', '305082', 'mazius3');

if (isset($_POST['niveau'])) {
    $niveau = $_POST['niveau'];
    if (isset($_POST['search']) && !empty($_POST['search'])) { // Vérifier si le champ de recherche n'est pas vide
        $search = $_POST['search'];
        $pdoStat = $objetPdo->prepare('SELECT * FROM mazius WHERE level = :niveau AND seed = :search ORDER BY step ASC');
        $pdoStat->bindParam(':niveau', $niveau);
        $pdoStat->bindParam(':search', $search);
    } else {
        $pdoStat = $objetPdo->prepare('SELECT * FROM mazius WHERE level = :niveau ORDER BY step ASC');
        $pdoStat->bindParam(':niveau', $niveau);
    }
} else {
    if (isset($_POST['search']) && !empty($_POST['search'])) { // Vérifier si le champ de recherche n'est pas vide
        $search = $_POST['search'];
        $pdoStat = $objetPdo->prepare('SELECT * FROM mazius WHERE seed = :search ORDER BY step ASC');
        $pdoStat->bindParam(':search', $search);
    } else {
        $pdoStat = $objetPdo->prepare('SELECT * FROM mazius ');
    }
}
$executeIsOk = $pdoStat->execute();
$Mazius = $pdoStat->fetchAll();
?>

<!DOCTYPE html>
<html>

<head>
  <title>Stats for mazius</title>
    <link rel="stylesheet" href="index.css">
    <link rel ="shortcut icon" href = "image/singe.png" type="images/png"/>
</head>

<body>
    

    <div class='bg-image'></div>
    <div class='bg-black'></div>
    <div class="container">
    <div>
    <div class="button-dl">
    <form  method="get" action="file/MaziusIII.jar">
        <button type="submit">Download Game !</button>
    </form>
    <div>
        <div class="container_title">
            <div class="bg-titre">
                <h1 class>MAZIUS III</h1> 
            </div>
            <div class="titre"><h1>MAZIUS III</h1></div>
        </div>

        <div  class="image-singe"><img src="image/singe.png" alt="illustration singe de mazius" width=300px ></img></div>
        <div class="tableau">
            <div class="handler-button"> 
           

                <form method="POST" action="">
                    
                    <select class="button-level" name="niveau">
                        <option value="Easy">Easy</option>
                        <option value="Normal">Normal</option>
                        <option value="Hard">Hard</option>
                    </select>

                    <input type="text" name="search" placeholder=" Search seed...">
                    
                    <button class="button-filter" type="submit">Filter</button>
                </form>

                <form class="content-reset" method="POST" action="">
                    <button class="button-reset" type="submit" name="reset">Reset</button>
                </form>
            </div>
            <table>
            <thead>
                <th>Pseudo</th>
                <th>Seed</th>
                <th>Level</th>
                <th>Step</th>
                <th>Date</th>
                
            </thead>
            <?php
                $rowNum = 0;
                foreach($Mazius as $mazius):
                    $rowNum++;
                    if ($rowNum % 2 == 0) {
                        $rowClass = 'even-row';
                    } else {
                        $rowClass = 'odd-row';
                    }
                ?>
                    <tr class="ligne <?php echo $rowClass; ?>">
                        <td class="name"><?php echo $mazius['name']; ?></td>
                        <td class="side"><?php echo $mazius['seed']; ?></td>
                        <td class="level"><?php echo $mazius['level']; ?></td>
                        <td class="step"><?php echo $mazius['step']; ?></td>
                        <td class="date"><?php echo $mazius['date']; ?></td>
                        
                    </tr>
            <?php endforeach; ?>
            </table>
        </div>
    </div>
    
</body>
</html>

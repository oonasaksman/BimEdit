/* 
 * Oona Saksman oona.saksman@tuni.fi 439468
 * Harjoitustyö
 */
 
 /*1.print2d
  *2.invertPic
  *3.picLoad 
  *4.invalidImage
  *5.picDilateOorErode
  *6.picInfo
  *7.whatCommand 
  *8.main
  */
  
import java.util.Scanner;
import java.io.*;

public class BimEdit{
   
   //Tulostaa näytölle muutosten mukaisen merkkitaulukon.
   public static void print2d(char[][] pic){
         
         //Käy läpi rivit.
         for (int ind = 0; ind < pic.length; ind++){
            //Käy läpi kolumnit.
            for (int und = 0; und < pic[ind].length; und++) {
               //Tulostaa näytölle oikean merkin.
               System.out.print(pic[ind][und]);
            }
            //Vaihtaa riviä.
            System.out.println();
         }
   }
   
   //Vaihtaa taulukon edustamerkit taustamerkkien paikalle.
   public static void invertPic(char[][] loadedPic, char[] charArray) {
      char first = charArray[0];
      char second = charArray[1];
      //Käy läpi merkkitaulukon.
      for (int ind = 0; ind < loadedPic.length; ind++){
         for (int und = 0; und < loadedPic[ind].length; und++) {
            //Tunnistetaan mikä merkki on kyseessä ja vaihdetaan se toiseksi.
            if (loadedPic[ind][und] == first) {
               loadedPic[ind][und] = second;
            }
            else if (loadedPic[ind][und] == second) {
               loadedPic[ind][und]= first;
            }
         }
      }
   }
   
   //Lataa kuvan tiedostosta ja tarkistaa, että kuva ja tiedot ovat oikeanlaiset
   public static char[][] picLoad(String fileName, char[] charArray, int[] sizeArray) {
      //Luodaan tiedostonlukija.
      Scanner fileReader = null;
      String image = "image";
      //yritetään lukeä tiedosto
      try{
         File file = new File(fileName);
         fileReader = new Scanner(file);
         //Skipataan neljä ekaa riviä, koska ne luettiin jo.
         fileReader.nextLine();
         fileReader.nextLine();
         fileReader.nextLine();
         fileReader.nextLine();
         int offset = 0;
         //Nimetään muuttujat joita käytetään myöhemmin
         char characterFirst = charArray[0];
         char characterSecond = charArray[1];
         int row = sizeArray[0];
         int col = sizeArray[1];
         //Luodaan oikean kokoinen merkkitaulukko kuvalle.
         char[][] loadedPic = new char[row][col];
         //Luuppi kopioi merkkejä tiedostosta loadedPic-muuttujaan, muuta tätä
         //niin kauan kunnes tallennettavaa ei enää ole.
            for (int u = 0; fileReader.hasNextLine(); u++) {
             String line = fileReader.nextLine();
               for (int i = 0; i < line.length(); i++){
                  loadedPic[u][i] = line.charAt(offset++);
               }
               offset = 0;
            }
            //Lukija suljetaan ja muuttuja, jossa taulukko on palautetaan.
            fileReader.close();
            boolean check = false;
            //Tarkistetaan, että taulukossa ei ole muita merkkejä kuin ne kaksi jotka tiedoissa ilmoitetaan.
            for(int h = 0; h < loadedPic.length; h++){
               for(int w = 0; w < loadedPic[0].length; w++){
                  if(loadedPic[h][w] != characterFirst && loadedPic[h][w] != characterSecond){
                     invalidImage(image);
                  }
               }
            }
            //Jos pituudet ovat väärät 
            if (row != loadedPic.length || col != loadedPic[0].length){
               invalidImage(image);
            }
            //Jos käyttäjän antama numero on yksi.
            else{
               return loadedPic;
            }
         }
      //Poikkeuksen sattuessa tulostetaan virheviesti ja suljetaan ohjelma.
      //Voi tapahtua, jos esimerkiksi mitat tiedoston alussa ovat väärät, jolloin tiedostoa
      //lukiessa tapahtuu indeksivirhe.
      catch(Exception e){
         invalidImage(image);
         return null;
      }
      return null;
   }
   //Metodi, jota kutsutaan, kun tiedosto on vääränlainen 
   public static void invalidImage(String imageOrCommand){
      System.out.println("-----------------------");
      System.out.println("| Binary image editor |");
      System.out.println("-----------------------");
      //Valitaan oikea teksti metodin saaman parametrin perusteella.
      if (imageOrCommand == "image"){
         System.out.println("Invalid image file!");
      }
      else{
         System.out.println("Invalid command-line argument!");
      }
      System.out.println("Bye, see you soon.");
      //Suljetaan ohjelma.
      System.exit(0);
   }
   
   //Käy taulukon läpi ikkunalla, jonka sivu on käyttäjän antaman numeron kokoinen. Jos ikkunassa 
   //havaitaan haettu merkki, muutetaan ikkunan keskimmäinen merkki siksi. Haettu merkki riippuu 
   //siitä onko kyseessä dilate vai erode toiminto ja onko kuva käännetty invert-toiminnolla.
   public static char[][] picDilateOrErode(char[][] loadedPic, int number, char edusta, char tausta, char[] charArray){
      //Jos käyttäjän antama numero on yksi, palautetaan kuva sellaisenaan.
      if (number != 1){
         if (loadedPic[0][0] == charArray[1]){
            char edusta2 = tausta;
            char tausta2 = edusta;
            edusta = edusta2;
            tausta = tausta2;
         }
         //Alustetaan muuttujat.
         int x;
         int y;
         int c;
         int row = loadedPic.length; 
         int col = loadedPic[0].length; 
         char[][] copyLoadedPic = new char[row][col];
         for (int ind = 0; ind < row; ind++){
            for (int und = 0; und < loadedPic[ind].length; und++) {
               copyLoadedPic[ind][und] = loadedPic[ind][und];
            }
         }
         
         //Offset laskee kuinka monta kertaa ikkunaa on liikutettu.
         int offset = 0;
         //Side on sen kokoinen kuin merkkitaulukon reunus johon muutoksia ei voida tehdä.
         int side = number/2; 
         //col2 on sen alueen leveys, johon muutoksia voi tehdä.
         //Offsetin, siden ja col2 perusteella päätellään mikä merkki taulukossa muutetaan,
         //kun haettava merkki havaitaan.
         int col2 = col - side * 2; 
         //Siirtää ikkunaa alas.
         for(int r = 0; r <= row-1-side*2; r++){ 
            //Siirtää ikkunaa oikealle.
            for(c = 0; c <= col-1-side*2; c++){ 
   
               //Käy läpi ikkunan rivit.
               for (x = 0 + r; x < number+r; x++){ 
                  //Käy läpi ikkunan kolumnit.
                  for (y = 0 + c; y < number+c; y++){ 
                      
                     //Jos valittu merkki on se merkki jota haetaan, muutetaan ikkunan keskimmäinen merkki.
                     if (loadedPic[x][y] == edusta){
                        //Jos offset on 0, eli ikkuna ei ole vielä liikkunut.
                        if (offset == 0){
                           copyLoadedPic [side][side] = edusta;
                        }
                        //Jos jos offsetin ja col2 jakojäännös ei ole 0. eli se ei ole vaihdettavien 
                        //merkkien alueen vasemmalla sivulla
                        else if (offset % col2 != 0){ 
                           //Muutetaan merkki riviltä, jonka indeksi lasketaan jakamalla 
                           //offset col2:lla ja lisäämällä side
                           //side lisää riveihin ne alueet joita ei voi muuttaa, offset/col2 antaa
                           //muutettavien merkkien alueella olevan oikean rivin.
                           int whatRow = offset / col2 + side; 
                           //Kolumnin indeksi on offsetin ja col2:n jakojäännös plus side.
                           int whatColumn = offset % col2 + side; 
                           //Muutetaan merkki kopioon.
                           copyLoadedPic[whatRow][whatColumn] = edusta;
                        }
                        //Jos jakojäännös on nolla.
                        else {
                           //Rivin indeksi on offsetin ja col2:n osamäärän plus side.
                           //side lisää riveihin ne alueet joita ei voi muuttaa, offset/col2 antaa
                           //muutettavien merkkien alueella olevan oikean rivin.
                           int whatRow = offset / col2 + side; 
                           //Kolumnin indeksi on sama kuin side.
                           int whatColumn = side;
                           copyLoadedPic[whatRow][whatColumn] = edusta;
                        }
                     }
                  }
               }
               //offset kasvaa yhden aina kun ikkuna siirtyy eteenpäin.
               offset++;
               //Ikkunan muuttujat nollataan
               x = 0;
               y = 0;
            }
            //Nollaataan toiseksi uloimman for luupin muuttuja.
            c = 0;
         }
         //Palautetaan muutettu kuva.
         return copyLoadedPic;
      }
      else {
         //Palautetaan kuva muuttumattomana.
         return loadedPic;
      }
   }
   
   //Tulostaa näytölle taulukon tiedot, eli sen koon ja merkkien lukumäärät.
   public static void picInfo(char[][] loadedPic, char[] charArray, int[] sizeArray){
      //Tulostetaan mitat.
      System.out.println(loadedPic.length + " x " + loadedPic[0].length);
      int howManyFirstChars = 0;
      int howManySecondChars = 0;
      //Käy taulukon läpi ja laskee kuinka monta kutakin merkkiä on.
      for (int ind = 0; ind < loadedPic.length; ind++){
         for (int und = 0; und < loadedPic[0].length; und++){
            if (loadedPic[ind][und] == charArray[1]){
               howManyFirstChars++;
 
            } 
            else{
               howManySecondChars++;
            } 
         }
      }
      //Tarkastetaan kumpi merkeistä on taustamerkki ja tulostetaan sen määrä ensin.
      if(loadedPic[0][0] == charArray[0]){
         System.out.println(charArray[0] + " " + howManySecondChars);
         System.out.println(charArray[1] + " " + howManyFirstChars);
      }
      else{
         System.out.println(charArray[1] + " " + howManyFirstChars);
         System.out.println(charArray[0] + " " + howManySecondChars);
      }
   }
      
   //Kutsuu eri metodeja oikeilla parametreillä sen perusteella minkä komennon käyttäjä on antanut.
   public static void whatCommand(char[][] loadedPic, String fileName, String command, 
                                  char[] charArray, int[] sizeArray){
   
      if (command.equals("info")){
         picInfo(loadedPic, charArray, sizeArray);
      }
      else if (command.equals("invert")){
         invertPic(loadedPic, charArray);
      }
      else if (command.equals("echo")){
         System.out.println(command);
      }
      else {
         print2d(loadedPic);
      }
   }
   
   public static void main (String[] args){
      
      //Myöhemmin tarvittavien muuttujien pohjustus.
      String com = "command";
      String image = "image";
      //Luetaan komentoriviparametrit
      try{
         File fileCalled =  new File(args[0]);
         Scanner reader1 = new Scanner(System.in);
      }
      //Jos parametrien lukemisessa on ongelma, printataan virheviesti ja suljetaan ohjelma.
      catch (Exception e){
         invalidImage(com);
      }
      //Jos parametrit ovat vääränlaiset, printataan virheviesti ja suljetaan ohjelma.
      if (args.length < 1 || args.length > 2 || (args.length == 2 && !args[1].equals("echo"))
          || args == null){
          invalidImage(com);
      }
      else {
         String fileName = args[0];
         //jos echo on totta, ohjelma toistaa aina käyttäjän antamat syötteet.
         boolean echo = false;
         if (args.length == 2 && args[1].equals("echo")){
            echo = true;
         }
         //Alustetaan muuttujat, jotta niitä voidaan käyttää muuallakin.
         Scanner fileReader = null;
         char mark1;
         char mark2;
         int row;
         int col;
         char[] charsArray = new char[2];
         int[] sizesArray = new int[2];
         try{
            File marks = new File(fileName);
            fileReader = new Scanner(marks);
            //Luetaan kuvan mitat, muuta tätä
            row = Integer.parseInt(fileReader.nextLine());
            col = Integer.parseInt(fileReader.nextLine());
            //Otetaan merkit 3. ja 4. riviltä.
            String mark3 = fileReader.nextLine();
            String mark4 = fileReader.nextLine();
            //Muunnetaan ne merkeiksi.
            mark1 = mark3.charAt(0);
            mark2 = mark4.charAt(0);
            //Tallennetaan merkit ja mitat oikeisiin taulukoihin
            charsArray [0]= mark1;  
            charsArray [1] = mark2;
            sizesArray [0] = row;
            sizesArray [1] = col;
            //fileReader.close();, kerro miksi ei
         }
         //Jos kuvan tietojen lukemisessa oli ongelma
         catch(Exception e){
            invalidImage(image);
         }
         //Tallennetaan ladattu taulukko loadedPic muuttujaan.
         char[][] loadedPic = picLoad(fileName, charsArray, sizesArray);
         System.out.println("-----------------------");
         System.out.println("| Binary image editor |");
         System.out.println("-----------------------");
         boolean contin = true;
         Scanner reader = new Scanner(System.in);
         
         do{
            System.out.println("print/info/invert/dilate/erode/load/quit?");
            String command = reader.nextLine();
            //Jos boolean arvo echo on tosi, ohjelma tulostaa komennon aina uudestaan sen antamisen jälkeen.
            if (echo){
               System.out.println(command);
            }
         
            String[] commands = command.split(" ");
            //Luuppi pyörii, kunnes käyttäjä antaa "quit"-komennon.
            if (command.equals("quit")){
               contin = false;
            }
            //Jos komento on "load", kutsutaan loadedPic-metodia, joka lataa kuvan tiedostosta uudestaan.
            else if (command.equals("load")){
               loadedPic = picLoad(fileName, charsArray, sizesArray);
            }
            //Jos komentorivi parametrejä on yksi.//Jos komento on "print", "info" tai "invert".
            else if (commands.length == 1 && (command.equals("print") || command.equals("info")
                   || command.equals("invert"))){
               whatCommand(loadedPic, fileName, command, charsArray, sizesArray);
            }
           //Jos komentoriviparametrejä on enemmän kuin kaksi tai ei ollenkaan
            else if (commands.length != 2){
               System.out.println("Invalid command!");
            }  
            //else {
               //Jos ensimmäinen komentoriviparametri on erode ja toinen on numero,
               //joka ei ole suurempi kuin kuvan rivien tai kolumnien määrät.
            else if (commands[0].equals("erode") && commands[1].matches("-?\\d+(.\\d+)?")
                && Integer.parseInt(commands[1]) % 2 != 0 
                && Integer.parseInt(commands[1]) <= loadedPic.length 
                && Integer.parseInt(commands[1]) <= loadedPic[0].length
                && Integer.parseInt(commands[1]) > 0){
               int number = Integer.parseInt(commands[1]);
               char edusta = charsArray[0];
               char tausta = charsArray[1];
               loadedPic = picDilateOrErode(loadedPic, number, edusta, tausta, charsArray);
            }
               //Jos ensimmäinen komentoriviparametri on dilate ja toinen on numero,
               //joka ei ole suurempi kuin kuvan rivien tai kolumnien määrät.
            else if (commands[0].equals("dilate") && commands[1].matches("-?\\d+(.\\d+)?") 
                 && Integer.parseInt(commands[1]) % 2 != 0
                 && Integer.parseInt(commands[1]) <= loadedPic.length 
                 && Integer.parseInt(commands[1]) <= loadedPic[0].length
                 && Integer.parseInt(commands[1]) > 0){
                int number = Integer.parseInt(commands[1]);
               char tausta = charsArray[0];
               char edusta = charsArray[1];
               loadedPic = picDilateOrErode(loadedPic, number, edusta, tausta, charsArray);
            }
            //Jos mikään ehdoista ei täyty, komento on vääränlainen ja printataan virheviesti.
            else{
               System.out.println("Invalid command!");
            }
         }
         while (contin); 
         //Suljetaan lukija.
         reader.close();
      }
      //Viesti kun ohjelma suljetaan.
      System.out.println("Bye, see you soon.");
   }
}
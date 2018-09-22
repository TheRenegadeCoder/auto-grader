/*Project04.java
 * This program will ask the user to pick a type of dragon and will randomly choose a type of dragon and determine who the winner is.
 * 
 * @author Marta Bugen
 * @version 20180918
 * 
 * 
 */



import java.util.Scanner;

public class Project04 {

 public static void main(String[] args) {
  
 
  String Fire = "Fire";
  String fire = "fire" ;
  String f = "f";
  String F = "F";
  String FIRE = "FIRE";
  
  String Plant = "Plant";
  String plant = "plant" ;
  String p = "p";
  String P = "P";
  String PLANT = "PLANT";
  
  String Water = "Water";
  String water = "water" ;
  String w = "w";
  String W = "W";
  String WATER = "WATER";
  
   
  Scanner keyboard = new Scanner(System.in);
  System.out.print("Please select one of your dragons [Fire/Plant/Water]: ");
  String dragon = keyboard.nextLine();
   
  int randomDragon = (int)(3*Math.random()) + 1;  
   
  if (dragon.equals(fire) || dragon.equals(f) || dragon.equals(Fire) || dragon.equals(F) || dragon.equals(FIRE) )
  {
   System.out.println("You chose: Fire dragon");
   if (randomDragon == 1)
   {
    System.out.println("I chose: Fire dragon");
    System.out.println("A tie!");
   }
   else if (randomDragon == 2)
   {
    System.out.println("I chose: Pant dragon");
    System.out.println("Fire defeats Plant - you win!");
   }
   else if (randomDragon == 3)
   {
    System.out.println("I chose: Water dragon");
    System.out.println("Water defeats Fire - you lose!");
   }
  }
    
  else if (dragon.equals(plant) || dragon.equals(p) || dragon.equals(Plant) || dragon.equals(P) || dragon.equals(PLANT))
  {
   System.out.println("You chose: Plant dragon");
   if (randomDragon == 1)
   {
    System.out.println("I chose: Fire dragon");
    System.out.println("Fire defeats plant - you lose!");
   }
   else if (randomDragon == 2)
   {
    System.out.println("I chose: Pant dragon");
    System.out.println("A tie!");     
   }
   else if (randomDragon == 3)
   {
    System.out.println("I chose: Water dragon");
    System.out.println("Plant defeats Water - you win!"); 
   }
  }
    
  else if (dragon.equals(water) || dragon.equals(w) || dragon.equals(Water) || dragon.equals(W) || dragon.equals(WATER))
  {
   System.out.println("You chose: Water dragon");
   if (randomDragon == 1)
   {
    System.out.println("I chose: Fire dragon");
    System.out.println("Water defeats Fire - you win!");     
   }
   else if (randomDragon == 2)
   {
    System.out.println("I chose: Plant dragon");
    System.out.println("Plant defeats Water - you lose!");
   }
   else if (randomDragon == 3)
   {
    System.out.println("I chose: Water dragon");
    System.out.println("A tie!");
   }
    
  }
  
  else
  {
   System.out.println("You do not have a " + dragon + " dragon, so you choose no dragons.");
   if (randomDragon == 1)
   {
    System.out.println("I chose: Fire dragon");
    System.out.println("You lose by default!");     
   }
   else if (randomDragon == 2)
   {
    System.out.println("I chose: Plant dragon");
    System.out.println("You lose by default!"); 
   }
   else if (randomDragon == 3)
   {
    System.out.println("I chose: Water dragon");
    System.out.println("You lose by default!");
   }
  }
 }
}


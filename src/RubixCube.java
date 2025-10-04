/*
 * Author: Joseph Carter
 * Major Project: Rubix Cube
 * Date: 10/03/25
 * Note: I apologize if this is one big ball of mud, 
 * I want to make this more cohesive in the future when I grasp packages. 
 * Movement stuff can be its own file. 
 * Faces can be its own classes with a centralized array in the main file. 
*/


// Necessary imports
import java.util.Scanner;
import java.util.ArrayList; //Given how Java arrays function, random and normal mode would work better with a 
// dynamic and not static array.


// Centralized movement
enum Movement {
              U("0123", "row", 0, 4), // U
              D("3210", "row", 2, 5), // D
              R("1435", "column", 2, 0), // R
              L("4153", "column", 0, 2), // L
              F("4052", "column", 0, 1), // F
              B("0425", "column", 2, 3); // B 
	      
	      // Necessary movement info
              public String move_sequence; 
              public String move_type;
              public int move_location;
              public int  transpose_face_location;
              public boolean is_inverse;
               
	      // Constructor for enum
	      Movement(String move_seq, String move_t, int move_loc, int t_f) 
	      {
		      this.move_sequence = move_seq;
		      this.move_type = move_t;
		      this.move_location = move_loc;
		      this.transpose_face_location = t_f;
		      this.is_inverse = false;
	      }
              
	      // Handling inverse judging by pattern
	      Movement inverse() 
	      {
		   
                   switch (this) 
		   {
			   case U: return D;
			   case D: return U;
			   case R: return L;
			   case L: return R;
			   case F: return B;
			   case B: return F;
		   }
                   
		   
		   return this;
	      }
}

// Main class. Saying it again, sorry for the ball of mud Sam, you deserve better.
class RubixCube 
{
      static Scanner scanner = new Scanner(System.in); // For I/O
      static String[][][] faces = { // Centralized approach for easy representation 
	      {				
		      {"r", "r", "r"},
		      {"r", "r", "r"}, // Red face
		      {"r", "r", "r"}
	      },
	      {
	      	      {"b", "b", "b"},
		      {"b", "b", "b"}, // blue face
		      {"b", "b", "b"}
	      },
	      {
              	      {"o", "o", "o"},
		      {"o", "o", "o"}, // orange face
		      {"o", "o", "o"}
	      },
	      {
                      {"g", "g", "g"},
		      {"g", "g", "g"}, // green face
		      {"g", "g", "g"}
	      },
	      {
		      {"y", "y", "y"},
		      {"y", "y", "y"}, // yellow face
		      {"y", "y", "y"}
	      },
	      {
                      {"w", "w", "w"},
		      {"w", "w", "w"}, // white face
		      {"w", "w", "w"}
	      }
      };
     
      // Prints cube in desired format
      static void printFaces() 
      {
            for (int i = 0; i < faces.length; i++)
	    {
		    for (int j = 0; j < faces[i].length; j++) 
	            {
	                for (int t = 0; t < faces[i][j].length; t++) 
			{
			      if ((t+1) % 2 == 0)
				System.out.printf("|%s|", faces[i][j][t]);
			      else
				System.out.print(faces[i][j][t]);
			}
			System.out.println();
	         }
	         System.out.println();
	    }
	
      }
      //
      static Movement setup_move(String move) 
      {
	    move = move.toUpperCase();
            Movement movement_statement = null;

           
	    if (move.length() > 2)
		    throw new IllegalArgumentException("");
	     movement_statement = Movement.valueOf(String.valueOf(move.charAt(0)));
             
	     if (move.length() == 2) {
		     if (Character.compare(move.charAt(1), '\'') == 0) {
		          Movement inverse_statement = movement_statement.inverse();
                          movement_statement.move_sequence = inverse_statement.move_sequence;
			  movement_statement.is_inverse = true; 
		     }
		     else
		           throw new IllegalArgumentException("");
	    }

	    return movement_statement;
      }
      static void transpose_face(int face, boolean is_inverse) {
	    // SPAGHETTI
            String[][] new_face = new String[3][3];
	    if (is_inverse)  {
	         for (int j = 0; j < 3; j++) {
		    for (int i = 0; i < 3; i++)
		        new_face[2-j][i] = faces[face][i][j];
	         }
	    }
	    else
	    {
		    for(int i =0; i < 3; i++) 
		    {
			    for (int j = 0; j < 3; j++) 
			        new_face[j][2-i] = faces[face][i][j];  
		    }
	    }
	    faces[face] = new_face;
      }
      static void execute_move(Movement move) 
      {
	   // Mapping enum attributes to abbreviated variables for readability
	   String move_seq = move.move_sequence;
	   String move_type = move.move_type;
           int move_loc = move.move_location;
	   int transpose_face_loc = move.transpose_face_location;
           
	   // For rotations
	   int last_index = Character.getNumericValue(move_seq.charAt(move_seq.length() - 1));

	   // Checks movement type, is it supposed to move rows or columns?
	   if (move_type.compareTo("row") == 0) 
	   {
                  
                   // Rotation implementation
                   String[] temp_row = faces[last_index][move_loc].clone();  
		   for (int i = 0; i < move_seq.length(); i++) 
		   {
			 int loc = Character.getNumericValue(move_seq.charAt(i));
                         String[] current_row = faces[loc][move_loc].clone();
			 faces[loc][move_loc] = temp_row;
			 temp_row = current_row; 
		   }
		   transpose_face(transpose_face_loc, move.is_inverse);
	   }
	   else if (move_type.compareTo("column") == 0) 
	   {
	       // Rotation implementation
	       String[] temp_column = new String[3];

	       // Something that easily made sense
	       for (int i = 0; i < faces[last_index].length; i++) 
                   temp_column[i] = faces[last_index][i][move_loc];

	       for (int i = 0; i < move_seq.length(); i++) 
	       {
		       int current = Character.getNumericValue(move_seq.charAt(i));
		       String temp = "";
		       for (int j = 0; j < faces[current].length; j++) 
		       {
			      temp = faces[current][j][move_loc];
                              faces[current][j][move_loc] = temp_column[j];
			      temp_column[j] = temp;
		       }
	       }
	       transpose_face(transpose_face_loc, move.is_inverse);
	       
	   } 
      }
      // Test mode for command line argument usage
      static void test_mode(String[] args) {
          
	  // Initializes structure for solution
          String[] solution = new String[args.length];
	  
	  // Iterates args
	  for (int i = 0; i < args.length; i++) 
	  {
               // Handles movements
	       Movement movement_statement = null;
	       try {
		       movement_statement = setup_move(args[i]); 
	       }
	       catch (IllegalArgumentException e) { // Invalid movement handling
		       System.out.println("Invalid move: " + args[i]);
		       continue;
	       }
               
	       // Executing move
	       execute_move(movement_statement);
            
	  }
	  
      }
      // Implementing normal mode for user input
      static String[] normal_mode() 
      {
	  // Dynamic array for easy input implementation
          ArrayList<String> input = new ArrayList<>();
          while (true) 
          {
	       // Print faces for good detail
               printFaces();
	       System.out.println("Make a move, enter finish if you are done: ");

	       // Handles I/O
               String prompt = scanner.nextLine();
	       prompt = prompt.toLowerCase();
               
	       // If user wants to exit and get their solution
	       if (prompt.compareTo("finish") == 0 )
		       break;
	       // Sets up move
               Movement move_statement = null;
               try {
		       move_statement = setup_move(prompt);
	       }
	       catch (IllegalArgumentException e) // Handler for invalid move
	       {
		       System.out.println("Invalid move: " + prompt);
		       continue;
	       }

	       // Executing move and adding prompt to dynamic input array
	       execute_move(move_statement);
	       input.add(prompt);
	 
          }
	  // Turning dynamic input array to static array for solution
          return input.toArray(new String[input.size()]);
      }
      // Implementation of random mode
      static String[] random_mode(int how_many_moves) 
      {
	      // Same reason, dynamic array for easy implementation
	      ArrayList<String> input = new ArrayList<>();

	      // Handling moves
              String[] moves = {"U", "D", "L", "R", "F", "B"};
              
	      // Easy solution for nearly infinite iterations with randomness
	      for (int i = 0; i < how_many_moves; i++) {
		      // Setting up moves
		      Movement random_statement = null;
                      
		      // Index for random move
		      int move_choice = (int)(Math.random() * (moves.length -1));
		      
		      // Handling inverse
		      int is_inverse = Math.random() < 0.5 ? 1 : 0;

		      String move = moves[move_choice] + (is_inverse == 0 ? "" : "'"); 

		      // Setting up move
		      try {
			      random_statement = setup_move(move);			     
		      }
		      catch (IllegalArgumentException e) { // Just in case if something ever went wrong
			      System.out.println("Something went wrong in random mode");
			      continue;
		      }

		      // Executing and adding move to dynamic array
		      execute_move(random_statement);
		      input.add(move);
	      }

	      // Printing resulting face
	      printFaces();
	      // Returning dynamic array as static array
	      return input.toArray(new String[input.size()]);
      }
      // Attempt at extra credit - Shorter solution
      static void  check_solution(String[] solution) 
      {

	      int l = solution.length - 1;
	      for (int i = l; i > -1; i--) 
	      {
		      String inverse_variant = "";
		      if (solution[i].length() == 1)
			      inverse_variant = solution[i] + "'";
		      else if (solution[i].length() == 2)
			      inverse_variant = String.valueOf(solution[i].charAt(0));
		      int compare = (i -1 >=0 ? i - 1 : i + 1);
		      
		      if (solution[compare].compareTo(inverse_variant) == 0)
			      continue;
		      else
			      System.out.println(solution[i]);
	      }
      }

      public static void main(String[] c_args) 
      {
          
          if (c_args.length != 0)
                 test_mode(c_args);
          else {
	       String mode = "";
	       while (true) {
		       System.out.println("Which mode (random/normal)?: ");
		       String input = scanner.nextLine();
		       input = input.toLowerCase();
		       mode = input;
		       if (mode.compareTo("random") == 0)
		            break;
		       else if (mode.compareTo("normal") == 0)
			    break;
	       }
               
	       
               String[] input = null;
               
	       if (mode.compareTo("random") == 0) {
		       System.out.println("How many moves? ");
		       int how_many_moves = scanner.nextInt();
		       input = random_mode(how_many_moves);
	       }
	       else if (mode.compareTo("normal") == 0)
		       input = normal_mode();

               System.out.println("Solution: ");
	       String[] solution = new String[input.length];
               for (int i = input.length -1; i > -1; i--) {
                      if (input[i].length() == 1)
			      solution[i] = String.valueOf(input[i].charAt(0)) + "'";
		      else
			      solution[i] = String.valueOf(input[i].charAt(0)); 
	       }
	       check_solution(solution);

          }         
      } 
      

}

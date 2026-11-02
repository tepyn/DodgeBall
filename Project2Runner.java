import javax.swing.*;

/* Program Description: The program is a simple Java game called DodgeBall,
except you, the player, will be dodging balls falling from the sky. The user
can move left and right by pressing on the 'A' and 'D' key on the KeyBoard.

*Technical Details: The program create 2 main graphic component, a custom
character, ground and balls (animated to drop randomly from the top of the screen).
The player can move the character by pressing 'A' for Left and 'D' for right
If the player fails to avoid the ball, they would lose the game and could either
choose to restart. The player wins if they manage to dodge 15 balls. 
Files details:
- Project2Runner.java: where to run the game
- Dodgeball.java handles the mechanics of the game
- DodgeballClasses.java consist of the custom graphics and game assets*/

class Project2Runner {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new DodgeBall());
    }
}
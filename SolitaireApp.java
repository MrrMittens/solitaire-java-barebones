public class SolitaireApp {
    public static void main(String[] args) {
        SolitaireMaster game = new SolitaireMaster();
        SolitaireGUI GUI = new SolitaireGUI(game);
        GUI.setUpGUI();
        GUI.setUpButtonListener();
    }
}

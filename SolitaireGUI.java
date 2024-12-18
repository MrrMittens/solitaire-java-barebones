import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;


public class SolitaireGUI {
    private final SolitaireMaster game;
    private JFrame frame;
    private JLayeredPane[] stackPanel, columnPanel;
    private JLayeredPane deckPanel, viewPanel;
    private JButton[] spade, clove, heart, diamond;
    private JButton deckButton;
    private ImageIcon cardBack, stack, view, deck;
    private Map<JButton,Card> cardMap;
    private Map<Card,JButton> buttonMap;


// constructor
    public SolitaireGUI(SolitaireMaster game) {
        this.game = game;
        frame = new JFrame();
        stackPanel = new JLayeredPane[4];
        columnPanel = new JLayeredPane[7];
        deckPanel = new JLayeredPane();
        viewPanel = new JLayeredPane();
        spade = new JButton[13];
        clove = new JButton[13];
        heart = new JButton[13];
        diamond = new JButton[13];
        deckButton = new JButton();
        cardBack = new ImageIcon("back.png");
        stack = new ImageIcon("stack.png");
        view = new ImageIcon("view.png");
        deck = new ImageIcon("deck.png");
        cardMap = new HashMap<>();
        buttonMap = new HashMap<>();
        createComponents();
        createMaps();
        game.initializeGame();
    }


// initialize
    private void createComponents() {
        JButton[][] symbolButtons = {spade, clove, heart, diamond};
        for (int i = 0; i < 4; i++) {
            stackPanel[i] = new JLayeredPane();
            for (int j = 0; j < 13; j++) {
                symbolButtons[i][j] = new JButton();
            }
        }
        for (int i = 0; i < 7; i++) {
            columnPanel[i] = new JLayeredPane();
        }
    }
    private void createMaps() {
        JButton[][] symbolButtons = {spade, clove, heart, diamond};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                cardMap.put(symbolButtons[i][j], game.getDeck().get(13*i+j));
                buttonMap.put(game.getDeck().get(13*i+j), symbolButtons[i][j]);
            }
        }
    }


// GUI setup
    public void setUpGUI() {
        Container c = frame.getContentPane();
        c.setLayout(null);
        c.setBackground(new java.awt.Color(13, 102, 30));

        setUpPanels(c);
        setUpGameState();

        frame.setSize(1400, 850);
        frame.setTitle("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private void setUpPanels(Container c) {
        deckPanel.setBounds(150, 25, 100, 145);
        deckPanel.setLayout(null);
        c.add(deckPanel);

        viewPanel.setBounds(315, 25, 140, 145); // 145 + 2*20 pixels
        viewPanel.setLayout(null);
        c.add(viewPanel);

        for (int i = 0; i < stackPanel.length; i++) {
            stackPanel[i].setBounds(595+181*i, 25, 100, 145);
            stackPanel[i].setLayout(null);
            c.add(stackPanel[i]);
        }

        for (int i = 0; i < columnPanel.length; i++) {
            columnPanel[i].setBounds(150+165*i, 200, 100, 650); // 145 + 19*40
            columnPanel[i].setLayout(null);
            c.add(columnPanel[i]);
        }
    }
    private void setUpGameState() {
        deckPanel.add(deckButton);
        deckButton.setBounds(0, 0, 100, 145);
        deckButton.setIcon(cardBack);

        JLabel viewIcon = new JLabel();
        viewIcon.setIcon(view);
        viewPanel.add(viewIcon);
        viewIcon.setBounds(0, 0, 140, 145);
        viewPanel.setLayer(viewIcon, -1);

        for (int i = 0; i < 7; i++) {
            JLabel columnIcon = new JLabel();
            columnIcon.setIcon(deck);
            columnPanel[i].add(columnIcon);
            columnIcon.setBounds(0, 0, 140, 145);
            columnPanel[i].setLayer(columnIcon, -1);

            ArrayList<Card> currentColumn = game.getColumn(i);
            for (int j = 0; j < currentColumn.size(); j++) {
                columnPanel[i].add(buttonMap.get(currentColumn.get(j)));
                buttonMap.get(currentColumn.get(j)).setBounds(0, 25*j, 100, 145);
                if (currentColumn.get(j).isHidden()) {
                    buttonMap.get(currentColumn.get(j)).setIcon(cardBack);
                } else {
                    buttonMap.get(currentColumn.get(j)).setIcon(currentColumn.get(j).getIcon());
                }
                columnPanel[i].setLayer(buttonMap.get(currentColumn.get(j)), j);
            }
        }
        for (int i = 0; i < 4; i++) {
            JLabel stackIcon = new JLabel();
            stackIcon.setIcon(stack);
            stackPanel[i].add(stackIcon);
            stackIcon.setBounds(0, 0, 100, 145);
            stackPanel[i].setLayer(stackIcon, -1);
        }
    }


// ButtonListener setup
    public void setUpButtonListener() {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent p ) {
                Object button = p.getSource();
                if (button == deckButton) {
                    game.deckActions();
                    updateAfterDraw();
                } else if (true) {
                    JButton[][] symbolButtons = {spade, clove, heart, diamond};
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 13; j++) {
                            if (button == symbolButtons[i][j]) {
                                if (!cardMap.get(symbolButtons[i][j]).isHidden()) {
                                    game.moveCard(cardMap.get(symbolButtons[i][j]));
                                    updateAfterMove();
                                }
                            }
                        }
                    }
                }
                if (game.winner()) {
                    System.out.println("You Win!");
                }
            }
        };
        deckButton.addActionListener(buttonListener);
        for (int i = 0; i < 13; i++) {
            spade[i].addActionListener(buttonListener);
            clove[i].addActionListener(buttonListener);
            heart[i].addActionListener(buttonListener);
            diamond[i].addActionListener(buttonListener);
        }
    }


// update
    private void updateAfterDraw() {
        updateDeckState();
        updateViewState();
    }
    private void updateAfterMove() {
        updateViewState();
        updateStackState();
        updateColumnState();
    }

    private void updateDeckState() {
        if (game.getDeck().isEmpty()) {
            deckButton.setIcon(deck);
        } else {
            deckButton.setIcon(cardBack);
        }
    }
    private void updateStackState() {
        for (int i = 0; i < 4; i++) {
            for (Component c : stackPanel[i].getComponents()) {
                if (c instanceof JButton) {
                    stackPanel[i].remove(c);
                }
                stackPanel[i].repaint();
            }
            if (!game.getStack(i).isEmpty()) {
                JButton currentButton = buttonMap.get(game.getStack(i).get(game.getStack(i).size()-1));
                stackPanel[i].add(currentButton);
                currentButton.setBounds(0, 0, 100, 145);
                currentButton.setIcon(game.getStack(i).get(game.getStack(i).size()-1).getIcon());
            }
        }
    }

    private void updateViewState() {
        for (Component c : viewPanel.getComponents()) {
            if (c instanceof JButton) {
                viewPanel.remove(c);
            }
        }
        viewPanel.repaint();
        int j = 0;
        ArrayList<Card> viewCards = new ArrayList<>();
        for (int i = game.getView().size()-1; i >= 0 ; i--) {
            if (j < 3) {
                viewCards.add(game.getView().get(i));
                j++;
            }
        }
        for (int i = viewCards.size()-1; i >= 0; i--) {
            viewPanel.add(buttonMap.get(viewCards.get(i)));
            buttonMap.get(viewCards.get(i)).setBounds(40-20*(viewCards.size()-1-i), 0, 100, 145);
            viewPanel.setLayer(buttonMap.get(viewCards.get(i)), viewCards.size()-1-i);
            buttonMap.get(viewCards.get(i)).setIcon(viewCards.get(i).getIcon());
        }
    }
    private void updateColumnState() {
        for (int i = 0; i < 7; i++) {
            for (Component c : columnPanel[i].getComponents()) {
                if (c instanceof JButton) {
                    columnPanel[i].remove(c);
                }
            }
            columnPanel[i].repaint();
            ArrayList<Card> currentColumn = game.getColumn(i);
            for (int j = 0; j < currentColumn.size(); j++) {
                columnPanel[i].add(buttonMap.get(currentColumn.get(j)));
                buttonMap.get(currentColumn.get(j)).setBounds(0, 25*j, 100, 145);
                if (currentColumn.get(j).isHidden()) {
                    buttonMap.get(currentColumn.get(j)).setIcon(cardBack);
                } else {
                    buttonMap.get(currentColumn.get(j)).setIcon(currentColumn.get(j).getIcon());
                }
                columnPanel[i].setLayer(buttonMap.get(currentColumn.get(j)), j);
            }
        }

    }
}

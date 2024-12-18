import java.util.ArrayList;
import java.util.Collections;


public class SolitaireMaster {
    private ArrayList<Card> deck;
    private ArrayList<Card> view;
    private ArrayList<ArrayList<Card>> stack;
    private ArrayList<ArrayList<Card>> column;


// constructor
    public SolitaireMaster() {
        deck = new ArrayList<>();
        view = new ArrayList<>();
        stack = new ArrayList<>(4);
        column = new ArrayList<>(7);
        for (int i = 0; i < 4; i++) {
            stack.add(new ArrayList<>());
        }

        for (int i = 0; i < 7; i++) {
            column.add(new ArrayList<>());
        }
        assembleDeck();
    }


// initialize
    public void initializeGame() {
        shuffleDeck();
        dealColumns();
    }
    private void assembleDeck() {
        String[] symbols = {"spade", "clove", "heart", "diamond"};
        for (String symbol : symbols) {
            for (int value = 1; value <= 13; value++) {
                deck.add( new Card(symbol, value) );
            }
        }
    }
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }
    private void dealColumns() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < i+1; j++) {
                if (j != i) {
                    deck.get(0).toggleHidden();
                }
                column.get(i).add(deck.get(0));
                deck.remove(0);
            }
        }
    }


// accessors
    public ArrayList<Card> getDeck() {
        return deck;
    }
    public ArrayList<Card> getView() {
        return view;
    }
    public ArrayList<Card> getStack(int i) {
        return stack.get(i);
    }
    public ArrayList<Card> getColumn(int i) {
        return column.get(i);
    }


// mutators
    public void deckActions() {
        if (!deck.isEmpty()) {
            view.add(deck.get(0));
            deck.remove(0);
        } else {
            for (int i = view.size()-1; i >= 0; i--) {
                deck.add(view.get(0));
                view.remove(0);
            }
        }
    }
    public void moveCard(Card clicked) {
        if (cardFit(clicked)) {
            ArrayList<Card> cardList = locateCard(clicked);
            ArrayList<Card> cardDestination = locateCardFit(clicked);
            int cardIndex = locateCardIndex(clicked, cardList);
            int cardSize = cardList.size();
            if (column.contains(cardList)) {
                for (int i = cardIndex; i < cardSize; i++) {
                    cardDestination.add(cardList.get(cardIndex));
                    cardList.remove(cardIndex);
                }
                revealCard(cardList);
            } else {
                cardDestination.add(cardList.get(cardIndex));
                cardList.remove(cardIndex);
            }
        }
    }


// support
    private ArrayList<Card> locateCard(Card clicked) {
        for (int i = 0; i < 4; i++) {
            if (stack.get(i).contains(clicked)) {
                return stack.get(i);
            }
        }
        for (int i = 0; i < 7; i++) {
            if (column.get(i).contains(clicked)) {
                return column.get(i);
            }
        }
        return view;
    }
    private int locateCardIndex(Card clicked, ArrayList<Card> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(clicked)) {
                return i;
            }
        }
        return -1;
    }
    private boolean cardFit(Card clicked) {
        if (stackFit(clicked)) {
            return true;
        } else if (columnFit(clicked)) {
            return true;
        }
        return false;
    }
    private ArrayList<Card> locateCardFit(Card clicked) {
        if (stackFit(clicked)) {
            String[] symbolOrder = {"spade", "clove", "heart", "diamond"};
            if (locateCardIndex(clicked, locateCard(clicked)) == locateCard(clicked).size()-1) {
                for (int i = 0; i < 4; i++) {
                    if (stack.get(i).isEmpty()) {
                        if (clicked.getValue() == 1 && clicked.getSymbol().equals(symbolOrder[i])) {
                            return stack.get(i);
                        }
                    } else {
                        Card topCard = stack.get(i).get(stack.get(i).size()-1);
                        if (clicked.getValue()-1 == topCard.getValue() && clicked.getSymbol().equals(topCard.getSymbol())) {
                            return stack.get(i);
                        }
                    }
                }
            }
        } else if (columnFit(clicked)) {
            for (int i = 0; i < 7; i++) {
                if (column.get(i).isEmpty()) {
                    if (clicked.getValue() == 13) {
                        return column.get(i);
                    }
                } else {
                    Card topCard = column.get(i).get(column.get(i).size()-1);
                    if (clicked.getValue()+1 == topCard.getValue() && altColor(clicked) == topCard.getColor()) {
                        return column.get(i);
                    }
                }
            }
        }
        return null;
    }
    private void revealCard(ArrayList<Card> columnList) {
        if (columnList.size() >= 1) {
            if (columnList.get(columnList.size()-1).isHidden()) {
                columnList.get(columnList.size()-1).toggleHidden();
            }
        }
    }


// support operations
    private char altColor(Card clicked) {
        if (clicked.getColor() == 'b') {
            return 'r';
        } else {
            return 'b';
        }
    }
    private boolean stackFit(Card clicked) {
        String[] symbolOrder = {"spade", "clove", "heart", "diamond"};
        if (locateCardIndex(clicked, locateCard(clicked)) == locateCard(clicked).size()-1) {
            for (int i = 0; i < 4; i++) {
                if (stack.get(i).isEmpty()) {
                    if (clicked.getValue() == 1 && clicked.getSymbol().equals(symbolOrder[i])) {
                        return true;
                    }
                } else {
                    Card topCard = stack.get(i).get(stack.get(i).size()-1);
                    if (clicked.getValue()-1 == topCard.getValue() && clicked.getSymbol().equals(topCard.getSymbol())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean columnFit(Card clicked) {
        for (int i = 0; i < 7; i++) {
            if (column.get(i).isEmpty()) {
                if (clicked.getValue() == 13) {
                    return true;
                }
            } else {
                Card topCard = column.get(i).get(column.get(i).size()-1);
                if (clicked.getValue()+1 == topCard.getValue() && altColor(clicked) == topCard.getColor()) {
                    return true;
                }
            }
        }
        return false;
    }


// additional
    public boolean winner() {
        boolean[] conditions = {false, false, false, false};
        for (int i = 0; i < 4; i++) {
            conditions[i] = (stack.get(i).size() == 13);
        }
        return (conditions[0] && conditions[1] && conditions[2] && conditions[3]);
    }
}
import javax.swing.ImageIcon;


public class Card {
    private final String symbol;
    private final int value;
    private final char color;
    private final ImageIcon cardIcon;
    private boolean hidden;


// constructor
    public Card( String symbol, int value ) {
        this.symbol = symbol;
        this.value = value;
        String fileName = String.format("%s%d.png", symbol, value);
        cardIcon = new ImageIcon( fileName );
        if (symbol.equals("spade") || symbol.equals("clove")) {
            color = 'b';
        } else {
            color = 'r';
        }
        hidden = false;
    }


// accessors
    public String getSymbol() {
        return symbol;
    }
    public int getValue() {
        return value;
    }
    public char getColor() {
        return color;
    }
    public ImageIcon getIcon() {
        return cardIcon;
    }
    public boolean isHidden() {
        return hidden;
    }


// mutators
    public void toggleHidden() {
        hidden = hidden == false;
    }
}
import java.util.Arrays;

public enum Keyword {

    //294

    BOOK(new String[][]{{"adjective", null},
                        {"adverb", null},
                        {"conjunction", null},
                        {"interjection", null},
                        {"noun", "A set of pages.", "A written work published in printed or electronic form."},
                        {"preposition", null},
                        {"pronoun", null},
                        {"verb", "To arrange for someone to have a seat on a plane.", "To arrange something on a particular date."}}),

    DISTINCT(new String[][]{{"adjective", "Familiar, worked in java.", "Unique. No duplicates."},
                            {"adverb", "Uniquely. Written 'distinctly'. "},
                            {"conjunction", null},
                            {"interjection", null},
                            {"noun", "A keyword in this assignment.", "A keyword in this assignment.", "A keyword in this assignment.",
                                     "An advanced search option. ", "Distinct is a parameter in this assignment."},
                            {"preposition", null},
                            {"pronoun", null},
                            {"verb", null}}),

    PLACEHOLDER(new String[][]{ {"adjective", "To be updated...", "To be updated..."},
                                {"adverb", "To be updated..."},
                                {"conjunction", "To be updated..."},
                                {"interjection", "To be updated..."},
                                {"noun", "To be updated...","To be updated...", "To be updated..."},
                                {"preposition", "To be updated..."},
                                {"pronoun", "To be updated..."},
                                {"verb", "To be updated..."}}),

    REVERSE(new String[][]{{"adjective", "On back side."},
                           {"adverb", "To be updated..."},
                           {"conjunction", null},
                           {"interjection", null},
                           {"noun", "A dictionary program's parameter.", "Change to opposite direction.", "The opposite.",
                                    "To be updated...", "To be updated...", "To be updated...", "To be updated..."},
                           {"preposition", null},
                           {"pronoun", null},
                           {"verb", "Change something to opposite. ", "Go back.", "Revoke ruling.", "To be updated...",
                                    "To be updated...", "Turn something inside out."}}),
    ARROW(new String[][] {{"adjective", null},
            {"adverb", null},
            {"conjunction", null},
            {"interjection", null},
            {"noun", "Here is one arrow: <IMG> -=>> </IMG>"},
            {"preposition", null},
            {"pronoun", null},
            {"verb", null}});

    String[][] data;
    String pos;

    Keyword(String[][] data){
        this.data = data;
    }

   @Override
    public String toString(){
        return Arrays.deepToString(data);
    }

    public String getIndex(int i, int j){
        return data[i][j];
    }

    public int getColLen(int i){
        return data[i].length;
    }
    public int getLen(){
        return data.length;
    }

    public String getPOS(int i){
        switch (i){
            case(0):
                pos = data[0][0];
                break;
            case(1):
                pos = data[1][0];
                break;
            case(2):
                pos = data[2][0];
                break;
            case(3):
                pos = data[3][0];
                break;
            case(4):
                pos = data[4][0];
                break;
            case(5):
                pos = data[5][0];
                break;
            case(6):
                pos = data[6][0];
                break;
            case(7):
                pos = data[7][0];
                break;
        }
        return pos;
    }

    public static void main(String[] args) {
        System.out.println(BOOK.getIndex(0,0));
    }

}

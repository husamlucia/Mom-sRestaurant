package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class PurpleLetterEvent implements Serializable {

    List<PurpleLetter> purpleLetters;

    public PurpleLetterEvent(List<PurpleLetter> purpleLetters) {
        this.purpleLetters = purpleLetters;
    }

    PurpleLetterEvent(){

    }

    public List<PurpleLetter> getPurpleLetters() {
        return purpleLetters;
    }

    public void setPurpleLetters(List<PurpleLetter> purpleLetters) {
        this.purpleLetters = purpleLetters;
    }
}

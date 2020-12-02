package fr.delcey.mediatorlivedataexample;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Le ViewState sert à représenter l'état de la vue.<br/>
 *
 * Il contient toutes les données "dynamiques" de la vue ( = les données qui peuvent changer pendant l'utilisation de l'app)
 */
public class MainViewState {

    private final String numberToDisplay;
    private final String sentence;

    public MainViewState(String numberToDisplay, String sentence) {
        this.numberToDisplay = numberToDisplay;
        this.sentence = sentence;
    }

    public String getNumberToDisplay() {
        return numberToDisplay;
    }

    public String getSentence() {
        return sentence;
    }

    @NonNull
    @Override
    public String toString() {
        return "ViewState{" +
            "numberToDisplay='" + numberToDisplay + '\'' +
            ", sentence='" + sentence + '\'' +
            '}';
    }

    // Les fonctions equals(), hashcode() et sont utiles pour les tests unitaires (dans les assertions)
    // et peuvent être autogénérées avec Alt + Inser
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainViewState mainViewState = (MainViewState) o;
        return Objects.equals(numberToDisplay, mainViewState.numberToDisplay) &&
            Objects.equals(sentence, mainViewState.sentence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberToDisplay, sentence);
    }
}

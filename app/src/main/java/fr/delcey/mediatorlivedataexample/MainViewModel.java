package fr.delcey.mediatorlivedataexample;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import fr.delcey.mediatorlivedataexample.repository.NumberRepository;
import fr.delcey.mediatorlivedataexample.repository.RandomRepository;

/**
 * Cette classe est le "coeur" de notre application, elle est l'intermédiaire entre la View (MainActivity)
 * et le stockage des données (NumberRepository et RandomRepository).
 *
 * C'est elle qui va avoir toute "l'intelligence" de notre application. Par "intelligence", on entend "fonctionnalités". Pour cette
 * application, ça concerne donc le fait de construire une phrase complexe suivant 2 nombres (un qui s'incrémente 1 à 1 ou se double, un
 * autre qui est aléatoire).
 *
 * Cette classe va être testée unitairement puisque c'est elle qui va contenir toutes nos règles de gestion.
 */
public class MainViewModel extends ViewModel {

    private final NumberRepository numberRepository;
    private final RandomRepository randomRepository;

    private final MediatorLiveData<MainViewState> mainViewStateMediatorLiveData = new MediatorLiveData<>();

    // Injection de dépendance depuis la Factory
    public MainViewModel(
        NumberRepository numberRepository,
        RandomRepository randomRepository
    ) {
        this.numberRepository = numberRepository;
        this.randomRepository = randomRepository;

        // Attention au bug !! Quand on utilise un mediatorLiveData, lorsqu'on fait les "addSource", il faut bien utiliser la même variable
        // dans le "addSource" et dans le onChanged des autres sources (quand on fait "numberLiveData.getValue()" par exemple)
        LiveData<Integer> numberLiveData = numberRepository.getNumberLiveData();
        LiveData<Integer> randomNumberLiveData = randomRepository.getRandomNumberLiveData();

        mainViewStateMediatorLiveData.addSource(numberLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer number) {
                combine(number, randomNumberLiveData.getValue());
            }
        });

        mainViewStateMediatorLiveData.addSource(randomNumberLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer randomNumber) {
                combine(numberLiveData.getValue(), randomNumber);
            }
        });
    }

    // Attention ici, les paramètres de la méthode "combine" (ce n'est pas un mot clef mais on l'utilise beaucoup avec le MediatorLiveData)
    // doivent toujours être considérés comme "nullables". En effet, "randomNumberLiveData.getValue()" (ou "numberLiveData.getValue()") peut
    // renvoyer une valeur null si jamais on n'a pas encore exposé de donnée dans cette LiveData.
    private void combine(@Nullable Integer number, @Nullable Integer randomNumber) {
        // On ne peut pas calculer le produit de ces 2 nombres si l'un d'eux est null.
        if (number == null || randomNumber == null) {
            return;
        }

        int result = number * randomNumber;
        String isEvenOrOdd;

        if (result % 2 == 0) {
            isEvenOrOdd = "pair";
        } else {
            isEvenOrOdd = "impair";
        }

        // C'est une mauvaise pratique que d'avoir une String en dur dans le ViewModel, il faut la récupérer avec les Resources !
        // C'est juste par simplicité. :)
        String sentence = "Le premier nombre est " + number +
            ", le nombre aléatoire est " + randomNumber +
            "\nLeur multiplication donne " + result + ", qui est " + isEvenOrOdd + ".";

        mainViewStateMediatorLiveData.setValue(
            new MainViewState(
                // On affiche toujours dans une TextView des Strings ("0"), pas des int (0).
                // Le ViewState ne doit donc avoir que des Strings (sauf pour des id de base de donnée par exemple, vu qu'ils ne seront pas affichés)
                // Les int qui peuvent être utilisés sont tous les identifiants de Resource Android. 
                // Exemple : @DrawableRes (R.drawable.my_super_icon), @StringRes (R.string.my_great_text), @ColorInt et @ColorRes (R.color.my_shiny_color), etc...
                String.valueOf(result),
                sentence
            )
        );
    }

    // Getter typé en LiveData (et pas MediatorLiveData pour éviter la modification de la valeur de la LiveData dans la View)
    public LiveData<MainViewState> getViewStateLiveData() {
        return mainViewStateMediatorLiveData;
    }

    // Les méthodes publiques ici représentent les différentes actions que l'utilisateur peut faire sur l'interface, le ViewModel se charge
    // de modifier les données comme nécessaire.
    public void onAddButtonClicked() {
        numberRepository.addToNumber(1);
    }

    public void onRandomButtonClicked() {
        randomRepository.rollNewRandom();
    }
}

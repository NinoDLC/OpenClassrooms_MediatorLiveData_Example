package fr.delcey.mediatorlivedataexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * Le rôle de la partie "View" (ici, l'Activity ou le Fragment) de l'architecture MVVM est d'afficher les informations du ViewState à
 * l'utilisateur, et de faire transiter les actions utilisateur au ViewModel, rien de plus.<br/>
 *
 * Tout formattage de donnée (concaténation de String, manipulation de chiffre, association de données, etc...) doit être fait dans le
 * ViemModel, pas dans la View.
 */
public class MainActivity extends AppCompatActivity {

    // Pas de variable de class (property) nécessaire ici. Moins il y en a, mieux c'est. Il est parfois possible de ne jamais en utiliser.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button buttonAdd = findViewById(R.id.button_add);
        Button buttonRandom = findViewById(R.id.button_random);

        TextView textViewNumber = findViewById(R.id.number_textview);
        TextView textViewSentence = findViewById(R.id.sentence_textview);

        final MainViewModel mainViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);
        mainViewModel.getViewStateLiveData().observe(this, new Observer<MainViewState>() {
            @Override
            public void onChanged(MainViewState mainViewState) {
                textViewNumber.setText(mainViewState.getNumberToDisplay());
                textViewSentence.setText(mainViewState.getSentence());
            }
        });

        // Pas de code / intelligence ici ! On "rapporte" juste au ViewModel ce qui se passe sur la View, rien d'autre !
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.onAddButtonClicked();
            }
        });

        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.onRandomButtonClicked();
            }
        });
    }
}

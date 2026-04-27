# Toolbox / Cheat-sheet — Page Informations (Spinner dynamique)

> Ce fichier est **pour toi uniquement** — ne pas commiter dans git.
> Ajoute `TOOLBOX_SPINNER.md` dans ton `.gitignore`.

Branche : `feature/page-informations-spinner-dynamic`
Package : `edu.polytech.filrouge_tp3`
Cours de référence : **Cours4_Adapters**

---

## 1. Vue d'ensemble de ce qui a été ajouté

À partir du squelette (MainActivity + ControlActivity + MenuFragment + Screen1..Screen7), deux fichiers ont été **ajoutés** et un seul a été **modifié d'une ligne** :

| Fichier | Action | Rôle |
|---|---|---|
| `app/src/main/res/layout/fragment_informations.xml` | **créé** | UI : Spinner + RadioGroup gravité + bouton VALIDER |
| `app/src/main/java/.../InformationsFragment.java` | **créé** | Logique Java : peuplement dynamique du Spinner + écouteur + validation |
| `app/src/main/java/.../ControlActivity.java` | **1 ligne modifiée** | `tabFragments[2] = new InformationsFragment()` (remplace `Screen3Fragment`) |

Aucun autre fichier du squelette n'a été touché : le comportement existant (Signaler → menu → autres écrans) est identique. Seul l'item index **2** du menu affiche maintenant notre page.

---

## 2. Les deux approches Spinner du cours

Le cours présente **deux façons** de remplir un Spinner. On a choisi la seconde parce qu'elle est plus puissante et plus réaliste (données venant d'une BDD / API / fichier).

### Approche A — Statique, sans ArrayAdapter (slides 7-10)

```xml
<!-- values/arrays.xml -->
<string-array name="types_accident">
    <item>Collision voiture - moto</item>
    ...
</string-array>
```

```xml
<Spinner android:entries="@array/types_accident" ... />
```

> Limite : liste figée au moment du build. On ne peut pas la modifier, la trier, la filtrer, ou la remplir depuis ailleurs.

### Approche B — Dynamique, avec ArrayAdapter (slides 22-23) ← c'est celle qu'on utilise

```java
List<String> typesAccident = new ArrayList<>();
typesAccident.add("Collision voiture - moto");
typesAccident.add("Collision voiture - voiture");
// ...

ArrayAdapter<String> adapter = new ArrayAdapter<>(
        requireContext(),
        android.R.layout.simple_spinner_item,   // layout item affiché quand fermé
        typesAccident);
adapter.setDropDownViewResource(
        android.R.layout.simple_spinner_dropdown_item); // layout item dans la liste déroulante

spinnerTypeAccident.setAdapter(adapter);
```

> Dans le XML, **ne PAS mettre** `android:entries="..."` : c'est l'adapter qui fournit les données.

---

## 3. Concepts-clés du cours, reliés au code

### 3.1 Le pattern Adapter (slides 2-6)

Un **Adapter** = un traducteur entre une **source de données** (List, Array, Cursor SQLite, JSON...) et une **AdapterView** (Spinner, ListView, GridView, RecyclerView...).

```
[List<String>] ─── ArrayAdapter<String> ───▶ [Spinner]
    données               adaptateur            vue
```

Dans notre code :
- **Données** : `List<String> typesAccident` (construite en Java)
- **Adapter** : `ArrayAdapter<String>`
- **Vue** : `Spinner spinnerTypeAccident`

### 3.2 ArrayAdapter — les 3 paramètres (slide 23)

```java
new ArrayAdapter<>(context, layoutItem, collection)
```

| Paramètre | Dans notre code | Rôle |
|---|---|---|
| `context` | `requireContext()` | contexte du Fragment |
| `layoutItem` | `android.R.layout.simple_spinner_item` | rendu d'un item (TextView par défaut Android) |
| `collection` | `typesAccident` (List<String>) | source de données |

Puis :
```java
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
```
→ Layout **différent** pour les items affichés quand la liste s'ouvre (plus d'espace, padding adapté).

### 3.3 L'écouteur OnItemSelectedListener (slides 11, 27)

Le Spinner prévient le code quand l'utilisateur choisit un item :

```java
spinnerTypeAccident.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // position : rang de l'item (0, 1, 2...)
        selectedType = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedType = "";
    }
});
```

Deux méthodes à implémenter **obligatoirement** :
- `onItemSelected(...)` : un item a été choisi → on stocke sa valeur.
- `onNothingSelected(...)` : rare mais requis par l'interface.

---

## 4. Intégration dans l'architecture existante (cours Fragments)

`ControlActivity` est l'orchestrateur : il contient un **tableau de 7 fragments** que le `MenuFragment` sélectionne. Pour que notre fragment s'y intègre proprement, il doit suivre **exactement le même contrat** que `Screen1Fragment..Screen7Fragment`.

### 4.1 Le contrat : interface `Notifiable`

```java
public interface Notifiable {
    void onClick(int numFragment);
    void onDataChange(int numFragment, Object object);
    void onFragmentDisplayed(int fragmentId);
}
```

`ControlActivity implements Notifiable` → tout fragment affiché peut le prévenir via ces méthodes. Cela permet au menu de mettre à jour l'icône active quand on change d'écran.

### 4.2 Pattern appliqué à InformationsFragment

```java
public class InformationsFragment extends Fragment {

    public final static int FRAGMENT_ID = 2;    // index dans tabFragments[]
    private Notifiable notifiable;              // pont vers l'Activity

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        } else {
            throw new AssertionError(...);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        notifiable.onFragmentDisplayed(FRAGMENT_ID); // "je suis affiché !"
    }
}
```

C'est **le même pattern exact** que dans `Screen1Fragment` — on reste cohérent avec l'existant.

### 4.3 Câblage dans ControlActivity

```java
private Fragment[] tabFragments = {
    new Screen1Fragment(),
    new Screen2Fragment(),
    new InformationsFragment(),   // ← index 2 (ex-Screen3Fragment)
    new Screen4Fragment(),
    new Screen5Fragment(),
    new Screen6Fragment(),
    new Screen7Fragment()
};
```

---

## 5. Cycle de vie utilisé (rappel Fragments)

```
onAttach()      → on récupère le Notifiable (Activity hôte)
onCreateView()  → on inflate fragment_informations.xml + findViewById + setup Spinner/Listener/Button
onStart()       → on prévient l'Activity : "FRAGMENT_ID=2 est affiché"
[utilisation]
onStop() / onDestroyView() / onDestroy() / onDetach() : rien de spécial ici
```

`onAttach` **avant** `onCreateView` : c'est pour ça qu'on peut utiliser `notifiable` dans `onStart` sans risque de NullPointer.

---

## 6. requireContext() vs getContext() / requireActivity() vs getActivity()

| Appel | Retour | Si pas attaché |
|---|---|---|
| `getContext()` | `Context` | `null` (risque NPE) |
| `requireContext()` | `Context` | lève `IllegalStateException` explicite |
| `getActivity()` | `FragmentActivity` | `null` |
| `requireActivity()` | `FragmentActivity` | lève exception explicite |

> On utilise les versions `require...()` : si on les appelle au mauvais moment, l'erreur est claire (stacktrace parlante) plutôt qu'un NullPointer obscur.

---

## 7. UI — fragment_informations.xml (résumé)

```
ConstraintLayout (ou LinearLayout)
├── TextView   "Type d'accident"
├── Spinner    id=spinnerTypeAccident   (PAS d'android:entries → dynamique)
├── TextView   "Gravité"
├── RadioGroup id=radioGroupGravite
│   ├── RadioButton "Légère"
│   ├── RadioButton "Modérée"
│   └── RadioButton "Grave"
└── Button     id=buttonValider  "VALIDER"
```

### Récupération de la gravité choisie :

```java
int checkedId = radioGroupGravite.getCheckedRadioButtonId();
if (checkedId == -1) return "non renseignée";         // aucun coché
RadioButton rb = radioGroupGravite.findViewById(checkedId);
return rb.getText().toString();
```

### Feedback utilisateur — Toast :

```java
Toast.makeText(requireContext(),
    "Type : " + selectedType + "\nGravité : " + gravite,
    Toast.LENGTH_LONG).show();
```

---

## 8. Flux utilisateur complet

1. **MainActivity** → bouton "Signaler" → `Intent` avec extra `index = 0` → lance `ControlActivity`.
2. **ControlActivity** → affiche `MenuFragment` (bas) + `tabFragments[0]` = `Screen1Fragment` (haut).
3. L'utilisateur tape sur la **3ᵉ icône** du menu (index 2).
4. `MenuFragment` → `onMenuChange(2)` → ControlActivity remplace le fragment haut par `tabFragments[2]` = **`InformationsFragment`**.
5. `InformationsFragment.onStart()` → `notifiable.onFragmentDisplayed(2)` → le menu active visuellement la 3ᵉ icône.
6. L'utilisateur choisit un type dans le Spinner → `onItemSelected` → `selectedType` mis à jour.
7. L'utilisateur coche une gravité.
8. L'utilisateur tape VALIDER → Toast récapitulatif.

---

## 9. Checklist "ce que le correcteur va regarder" (mapping cours → code)

| Point du cours | Présent ? | Où |
|---|---|---|
| Spinner en XML (pas d'`android:entries`) | oui | fragment_informations.xml |
| Création d'un `ArrayAdapter<String>` | oui | `setupSpinnerDynamic()` |
| Collection peuplée en Java (pas via `@array/...`) | oui | `typesAccident.add(...)` |
| `setDropDownViewResource(...)` | oui | `setupSpinnerDynamic()` |
| `OnItemSelectedListener` avec `onItemSelected` + `onNothingSelected` | oui | `setupSpinnerListener()` |
| Utilisation de `position` reçu par le listener | oui | `parent.getItemAtPosition(position)` |
| Intégration cohérente avec l'architecture Fragments existante | oui | pattern Notifiable + FRAGMENT_ID |

---

## 10. Pour aller plus loin (si on te pose la question)

- **Adapter custom** : créer une classe `MyAdapter extends ArrayAdapter<MonType>` et override `getView()` / `getDropDownView()` pour afficher par exemple une **icône + texte** par item (équivalent de ce que fait le cours pour ListView avec un layout personnalisé).
- **Source de données externe** : remplacer `typesAccident.add(...)` par un appel à une BDD Room ou à un fichier JSON — le reste du code (Spinner, Adapter, Listener) **ne change pas**. C'est l'intérêt du pattern Adapter.
- **Notifier l'Activity** avec le choix : au clic VALIDER, appeler `notifiable.onDataChange(FRAGMENT_ID, selectedType + "/" + gravite)` pour remonter l'info vers ControlActivity.

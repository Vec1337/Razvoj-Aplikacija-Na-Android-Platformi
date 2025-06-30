# ListDetailApp - Lab3

Ova Android aplikacija se nastavlja na projekt iz **Lab2**, proširujući ga dodatnim funkcionalnostima kao dio zadatka **Lab3**.

## 📱 Funkcionalnosti

### ✅ Nastavak na Lab2
Aplikacija koristi princip **Master-Detail** prikaza implementiran u **Lab2**, gdje se prikazuje lista automobila te se klikom na stavku otvara prikaz detalja.

### ✅ Fragmenti
- Implementacija koristi **`Fragment`** za prikaz liste (`CarListFragment`) i detalja automobila (`CarDetailFragment`).
- Fragmenti omogućuju lakšu prilagodbu dizajna za različite uređaje (npr. telefoni vs tableti).
- Komunikacija između fragmenta i aktivnosti ostvarena je putem callback funkcije.

### ✅ Room baza podataka
- Automobili se pohranjuju i dohvaćaju iz **lokalne Room baze podataka**.
- Prilikom prvog pokretanja aplikacije podaci se inicijalno zapisuju ako je baza prazna.
- Korištena je klasa `CarEntity` za entitet, `CarDao` za pristup podacima i `AppDatabase` za inicijalizaciju baze.

### ✅ Firebase Cloud Messaging (FCM)
- Aplikacija koristi **Firebase Cloud Messaging** za primanje push notifikacija.
- Kada stigne push notifikacija, prikazuje se **klasična Android notifikacija**.
- Klik na notifikaciju otvara unaprijed definiranu aktivnost unutar aplikacije.

## ⚙️ Tehnologije i biblioteke

- Kotlin
- Room Database
- Firebase Cloud Messaging
- Android Fragments
- ViewModel, LiveData
- RecyclerView
- Parcelable
- KSP 
- Material Design
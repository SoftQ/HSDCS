Po stronie bazy danych

Należy dodać odpowiednie widoki (ich nazwy można podejrzeć w pliku Querries.java) które zwracają dane do eksportu.
Nalezy dodać odpowiednie tabele do bazy danych, które kojarzą id obiektów wrzucanych do HubSpota (kontakty, opportunities itd)
z unikalnym vid (które powstaje po utworzeniu rekordu w HubSpocie).

Widok do kontaktów:

CREATE OR REPLACE VIEW getAllContract
AS
SELECT
	id,
	business_owner_firstname firstname,
    business_owner_lastname lastname,
	email,
    phone,
    createDate,
    email_verified verified,
    userVid
FROM first2trade.idev_user CON LEFT JOIN first2trade.userId_vid VID on CON.id = VID.userId;

Należy pamiętać o nazwach kolumn (firstname zamiast business_owner_firstname). Łączenie z tabelą userId_vid
w celu decyzji któy kontakt jest do dodania, a który do edycji. Taki widok "karmi" obie operacje.
Wybiera dane do dodania (userVid is null) oraz do  edycji (userVid is not null) - patrz plik Querries.java.

Przepływ działania eksportu (na przykładzie kontaktów).
Widok zawiera wszystkie kolumny które powinny być imporotwane do Hubspota - jeśli zalezy nam na importowaniu
jedynie imienia i nazwiska, zbiór kolumn w widoku będzie następujący ( id, firstname, lastname, vid ).
Kolumna id jest kluczem po stronie bazy, i nie jest brana pod uwagę przy imporcie. Kolumna vid jest niezbędna przy
aktualizacji obiektu po stronie HubSpota (aby go jednoznacznie ziydentyfikowac).
Obiekty które nalezy zaktualizować mają w widoku uzupełnioną kolumnę vid (mozna je znaleźć w HubSpocie), obiekty
które nalezy utworzyć mają tam wartość null.

Nazwy kolumn w widokach muszą być takie same jak nazwy odpowiadającym ich propercji po stronie HubSpota.
Nie moze zajśc sytuacja gdzie pole do nazwiska w HubSpocie nazywa się lastname, a kolumna w widoku surname -
nazwy propercji są kojarzone na podstawie nazw kolumn.
Z ResulSeta z danym są wybierane te kolumny które mają być wyeksportowane przy danej operacji.
Załóżmy że nie bierzemy pod uwagę aby edytować imię i nazwisko, więc kolumny firstname, lastname nie powinny znaleźć się
w żądaniu rest. Deklaracja kolumn potrzebnych w konkretnym zadaniu są definiowane w pliku config.properties
(contactPropertiesColumnNamesUpdate dla kolumn które mają być uwzględnione w aktualizacji oraz
contactPropertiesColumnNames dla kolumn które mają być uwzględniane przy kontaktu).
Na podstawie odpowiednich kolumn jest tworzeony JSON i wysyłanie żądanie REST.
W przypadku błedów, inofmacja o błędzie jest wyświetlana na konsoli.

Po stronie hubspota

Niektóre propercje które chcielibysmy eksoprtować z bazy mogą po stronie HubSpota nie istnieć. W tym przypadku należy je
utworzyć ręcznie, pamiętając aby przy deklaracji odpowiedniego widoku nazwa wewnętrzna nowej propercji była taka sama
jak nazwa nowej kolumny w widoku.

Połączenie z bazą danych, wszystkie adresy URL, apikey HubSpota są definiowane w pliku config.properties
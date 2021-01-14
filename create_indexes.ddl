CREATE INDEX index_kandydat_Imie_Nazwisko
ON Kandydat (Imie, Nazwisko);

CREATE INDEX index_kandydat_PESEL
ON Kandydat (PESEL);

CREATE INDEX index_pracownik_Imie_Nazwisko
ON Pracownik (Imie, Nazwisko);

CREATE INDEX index_k_s_Nazwa
ON Kierunek_studiow (Nazwa);

CREATE INDEX index_wydzial_Nazwa
ON Wydzial (Nazwa);

CREATE INDEX index_konkurs_Nazwa
ON Konkurs (Nazwa);

CREATE INDEX index_realizacja_Id_kierunku
ON Realizacja (Id_kierunku);

CREATE INDEX index_pracownik_Login
ON Pracownik (login);

CREATE INDEX index_l_kod_realizacji_id_tury
ON Lista (Kod_realizacji, Id_tury);

CREATE INDEX index_kraj_Nazwa
ON Kraj (Nazwa);

CREATE INDEX index_miasto_Ulica
ON Miasto (Nazwa);

CREATE INDEX index_miasto_Id_kraju
ON Miasto (Id_kraju);

CREATE INDEX index_adres_Ulica
ON Adres (Ulica);

CREATE INDEX index_adres_Id_miasta
ON Adres (Id_miasta);

CREATE INDEX index_aplikacja_Id_kandydata
ON Aplikacja (Id_kandydata);

CREATE INDEX index_tura_Data_rozp
ON Tura (Data_rozp);
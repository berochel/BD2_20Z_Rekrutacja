

CREATE TABLE adres (
    id_adresu      NUMBER(7) NOT NULL,
    id_miasta      NUMBER(5) NOT NULL,
    kod_pocztowy   VARCHAR2(10) NOT NULL,
    ulica          VARCHAR2(30) NOT NULL,
    nr_domu        NUMBER(5) NOT NULL,
    nr_mieszkania  NUMBER(5),
    PRIMARY KEY (id_adresu),
    FOREIGN KEY ( id_miasta ) REFERENCES miasto ( id_miasta )
);


CREATE TABLE aplikacja (
    id_aplikacji   NUMBER(5) NOT NULL,
    data_zlozenia  DATE NOT NULL,
    czy_oplacona   CHAR(1),
    id_kandydata   NUMBER(7) NOT NULL,
    PRIMARY KEY ( id_aplikacji ),
    FOREIGN KEY ( id_kandydata ) REFERENCES kandydat ( id_kandydata )
);


CREATE TABLE chec_uczestnictwa (
    id_kandydata  NUMBER(7) NOT NULL,
    id_tury       NUMBER(5) NOT NULL,
    chetny        CHAR(1) NOT NULL,
    PRIMARY KEY ( id_kandydata, id_tury ),
    FOREIGN KEY ( id_kandydata ) REFERENCES kandydat ( id_kandydata ),
    FOREIGN KEY ( id_tury ) REFERENCES tura ( id_tury )
);


CREATE TABLE kandydat (
    id_kandydata          NUMBER(7) NOT NULL,
    imie                  VARCHAR2(40) NOT NULL,
    nazwisko              VARCHAR2(40) NOT NULL,
    pesel                 VARCHAR2(12) NOT NULL,
    adres                 NUMBER(7) NOT NULL,
    data_urodzenia        DATE NOT NULL,
    haslo                 VARCHAR2(20) NOT NULL,
    czy_zlozyl_dokumenty  CHAR(1),
    zdjecie               BLOB,
    PRIMARY KEY ( id_kandydata ),
    FOREIGN KEY ( adres ) REFERENCES adres ( id_adresu )
);


CREATE TABLE kierunek_studiow (
    id_kierunku  NUMBER(5) NOT NULL,
    nazwa        VARCHAR2(50) NOT NULL,
    id_wydzialu  NUMBER(5) NOT NULL,
    PRIMARY KEY ( id_kierunku ),
    FOREIGN KEY ( id_wydzialu ) REFERENCES wydzial ( id_wydzialu )
);


CREATE TABLE konkurs (
    id_konkursu  NUMBER(5) NOT NULL,
    nazwa        VARCHAR2(100) NOT NULL,
    PRIMARY KEY ( id_konkursu )
);


CREATE TABLE kraj (
    id_kraju  NUMBER(5) NOT NULL,
    nazwa     VARCHAR2(20) NOT NULL,
    PRIMARY KEY ( id_kraju )
);


CREATE TABLE lista (
    id_listy        NUMBER(5) NOT NULL,
    kod_realizacji  VARCHAR2(5) NOT NULL,
    id_tury         NUMBER(5) NOT NULL,
    rezerwowa       CHAR(1),
    PRIMARY KEY ( id_listy ),
    FOREIGN KEY ( kod_realizacji ) REFERENCES realizacja ( kod ),
    FOREIGN KEY ( id_tury ) REFERENCES tura ( id_tury )
);


CREATE TABLE lista_akceptowanych_konkursow (
    id_kierunku                NUMBER(5) NOT NULL,
    id_konkursu                NUMBER(5) NOT NULL,
    liczba_punktow_za_konkurs  NUMBER(5) NOT NULL,
    PRIMARY KEY ( id_kierunku, id_konkursu ),
    FOREIGN KEY ( id_kierunku ) REFERENCES kierunek_studiow ( id_kierunku ),
    FOREIGN KEY ( id_konkursu ) REFERENCES konkurs ( id_konkursu )
);


CREATE TABLE lista_kandydat (
    id_listy      NUMBER(5) NOT NULL,
    id_kandydata  NUMBER(7) NOT NULL,
    PRIMARY KEY ( id_listy, id_kandydata ),
    FOREIGN KEY ( id_kandydata ) REFERENCES kandydat ( id_kandydata ),
    FOREIGN KEY ( id_listy ) REFERENCES lista ( id_listy )
);



CREATE TABLE lista_laureatow (
    id_konkursu   NUMBER(5) NOT NULL,
    id_kandydata  NUMBER(7) NOT NULL,
    PRIMARY KEY ( id_kandydata, id_konkursu ),
    FOREIGN KEY ( id_kandydata ) REFERENCES kandydat ( id_kandydata ),
    FOREIGN KEY ( id_konkursu ) REFERENCES konkurs ( id_konkursu )
);



CREATE TABLE lista_wyborow (
    id_kierunku   NUMBER(5) NOT NULL,
    id_aplikacji  NUMBER(5) NOT NULL,
    priorytet     NUMBER(3) NOT NULL,
    PRIMARY KEY ( id_kierunku, id_aplikacji ),
    FOREIGN KEY ( id_aplikacji ) REFERENCES aplikacja ( id_aplikacji ),
    FOREIGN KEY ( id_kierunku ) REFERENCES kierunek_studiow ( id_kierunku )
);



CREATE TABLE lista_wydzialow (
    id_wydzialu    NUMBER(5) NOT NULL,
    id_rekrutacji  NUMBER(5) NOT NULL,
    PRIMARY KEY ( id_wydzialu, id_rekrutacji ),
    FOREIGN KEY ( id_rekrutacji ) REFERENCES rekrutacja ( id_rekrutacji ),
    FOREIGN KEY ( id_wydzialu ) REFERENCES wydzial ( id_wydzialu )
);


CREATE TABLE miasto (
    id_miasta  NUMBER(5) NOT NULL,
    nazwa      VARCHAR2(20) NOT NULL,
    id_kraju   NUMBER(5) NOT NULL,
    PRIMARY KEY ( id_miasta ),
    FOREIGN KEY ( id_kraju ) REFERENCES kraj ( id_kraju )
);


CREATE TABLE pracownik (
    id_pracownika  NUMBER(5) NOT NULL,
    imie           VARCHAR2(40) NOT NULL,
    nazwisko       VARCHAR2(40) NOT NULL,
    login          VARCHAR2(40) NOT NULL,
    haslo          VARCHAR2(40) NOT NULL,
    id_wydzialu    NUMBER(5) NOT NULL,
    PRIMARY KEY ( id_pracownika ),
    FOREIGN KEY ( id_wydzialu ) REFERENCES wydzial ( id_wydzialu )
);


CREATE TABLE przedmiot (
    id_przedmiotu  NUMBER(5) NOT NULL,
    nazwa          VARCHAR2(50) NOT NULL,
    PRIMARY KEY ( id_przedmiotu )
);


CREATE TABLE przelicznik_punktow (
    id_kierunku    NUMBER(5) NOT NULL,
    id_przedmiotu  NUMBER(5) NOT NULL,
    waga           NUMBER(5, 3) NOT NULL,
    PRIMARY KEY ( id_kierunku, id_przedmiotu ),
    FOREIGN KEY ( id_kierunku ) REFERENCES kierunek_studiow ( id_kierunku ),
    FOREIGN KEY ( id_przedmiotu ) REFERENCES przedmiot ( id_przedmiotu )
);


CREATE TABLE realizacja (
    kod            VARCHAR2(5) NOT NULL,
    id_kierunku    NUMBER(5) NOT NULL,
    liczba_miejsc  NUMBER(4),
    PRIMARY KEY ( kod ),
    FOREIGN KEY ( id_kierunku ) REFERENCES kierunek_studiow ( id_kierunku )
);


CREATE TABLE rekrutacja (
    id_rekrutacji  NUMBER(5) NOT NULL,
    data_rozp      DATE,
    data_zak       DATE,
    PRIMARY KEY ( id_rekrutacji )
);


CREATE TABLE tura (
    id_tury    NUMBER(5) NOT NULL,
    nr_tury    NUMBER(3) NOT NULL,
    data_zak   DATE,
    data_rozp  DATE,
    PRIMARY KEY ( id_tury )
);


CREATE TABLE wydzial (
    id_wydzialu  NUMBER(5) NOT NULL,
    nazwa        VARCHAR2(50) NOT NULL,
    adres        NUMBER(7) NOT NULL,
    PRIMARY KEY ( id_wydzialu ),
    FOREIGN KEY ( adres ) REFERENCES adres ( id_adresu )
);


CREATE TABLE wynik_z_przedmiotu (
    id_kandydata    NUMBER(7) NOT NULL,
    id_przedmiotu   NUMBER(5) NOT NULL,
    liczba_punktow  NUMBER(5, 2) NOT NULL,
    PRIMARY KEY ( id_kandydata, id_przedmiotu ),
    FOREIGN KEY ( id_kandydata ) REFERENCES kandydat ( id_kandydata ),
    FOREIGN KEY ( id_przedmiotu ) REFERENCES przedmiot ( id_przedmiotu )
);




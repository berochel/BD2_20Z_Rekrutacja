CREATE OR replace FUNCTION oblicz_punkty_kandydata(kan_id NUMBER, kierunek_id NUMBER) 
RETURN NUMBER 
AS 
    punkty NUMBER := 0;
    z_przedm NUMBER := 0;
    z_konk NUMBER :=0;
    przel_punktow przelicznik_punktow%rowtype;
    konkurs_kandydata lista_laureatow%rowtype;
    CURSOR liczone_przedmioty(kier_id NUMBER)
        IS
        SELECT *
        FROM przelicznik_punktow
        WHERE id_kierunku = kier_id;
        
        CURSOR konkursy_kandydata(kand_id NUMBER)
        IS
        SELECT *
        FROM lista_laureatow
        WHERE id_kandydata = kand_id;
        
BEGIN 
    OPEN liczone_przedmioty(kierunek_id);
    LOOP
        FETCH liczone_przedmioty INTO przel_punktow;
        EXIT WHEN liczone_przedmioty%notfound;
        select liczba_punktow into z_przedm from wynik_z_przedmiotu where id_przedmiotu = przel_punktow.id_przedmiotu and id_kandydata = kan_id;
        IF z_przedm != null THEN
        punkty := punkty + z_przedm*przel_punktow.waga;
        END IF;
    END LOOP;
    CLOSE liczone_przedmioty;
    
    OPEN konkursy_kandydata(kan_id);
    LOOP
        FETCH konkursy_kandydata INTO konkurs_kandydata;
        EXIT WHEN konkursy_kandydata%notfound;
        select liczba_punktow_za_konkurs into z_konk from lista_akceptowanych_konkursow where konkurs_kandydata.id_konkursu = id_konkursu and id_kierunku = kierunek_id;
        IF z_konk != null THEN
        punkty := punkty + z_konk;
        END IF;
    END LOOP;
    CLOSE liczone_przedmioty;

    RETURN punkty; 
END; 

CREATE OR REPLACE FUNCTION zwroc_id_najnowszej_apl_kandydata(kand_id NUMBER)
RETURN NUMBER
AS
apl_id NUMBER;
BEGIN
    select id_aplikacji into apl_id from aplikacja ap1 where ap1.id_kandydata = kand_id and ap1.data_zlozenia = (select max(ap2.data_zlozenia) from aplikacja ap2);
    RETURN apl_id;
END;

CREATE OR REPLACE FUNCTION zwroc_id_aktualnej_rekrutacji
RETURN NUMBER
AS
rek_id NUMBER;
BEGIN
    select id_rekrutacji into rek_id from rekrutacja rek where rek.data_rozp < current_date and rek.data_zak > current_date;
    RETURN rek_id;
END;

CREATE OR REPLACE PROCEDURE zglos_realizacje_do_tury(tura_id NUMBER, kod_real VARCHAR2)
AS
l_id NUMBER;
BEGIN
    select max(id_listy)+1 into l_id from lista;
    INSERT INTO Lista (id_listy, kod_realizacji, id_tury, rezerwowa)
    VALUES (l_id, kod_real, tura_id, 0);
END;

CREATE OR REPLACE PROCEDURE zglos_kandydata_do_tury(tura_id NUMBER, kand_id NUMBER)
AS
BEGIN
    INSERT INTO Chec_uczestnictwa(id_kandydata, id_tury, chetny)
    VALUES (kand_id, tura_id, 1);
END;

CREATE OR REPLACE PROCEDURE zloz_aplikacje(kand_id NUMBER, kier1 NUMBER, kier2 NUMBER, kier3 NUMBER, kier4 NUMBER, kier5 NUMBER)
AS
apl_id NUMBER;
BEGIN
    select max(id_aplikacji)+1 into apl_id from aplikacja;
    INSERT INTO aplikacja (id_aplikacji, data_zlozenia, czy_oplacona, id_kandydata)
    VALUES (apl_id, CURRENT_DATE, 0, kand_id );
    
    INSERT INTO lista_wyborow (id_kierunku, id_aplikacji, priorytet)
    VALUES (kier1, apl_id, 1);
    INSERT INTO lista_wyborow (id_kierunku, id_aplikacji, priorytet)
    VALUES (kier2, apl_id, 2);
    INSERT INTO lista_wyborow (id_kierunku, id_aplikacji, priorytet)
    VALUES (kier3, apl_id, 3);
    INSERT INTO lista_wyborow (id_kierunku, id_aplikacji, priorytet)
    VALUES (kier4, apl_id, 4);
    INSERT INTO lista_wyborow (id_kierunku, id_aplikacji, priorytet)
    VALUES (kier5, apl_id, 5);
END;

CREATE OR REPLACE TRIGGER tg_wybor
AFTER INSERT ON lista_wyborow
FOR EACH ROW
DECLARE
akt_rek_id NUMBER; 
wydzial_id NUMBER;
count_help NUMBER;
BEGIN
    akt_rek_id := zwroc_id_aktualnej_rekrutacji();
    select ks.id_wydzialu into wydzial_id from kierunek_studiow ks where ks.id_kierunku = :new.id_kierunku;
    select count(*) into count_help from lista_wydzialow where id_wydzialu = wydzial_id and id_rekrutacji = akt_rek_id;
    IF count_help = 0 THEN
        dbms_output.put_line('rekrutacja na dany kierunek nie jest teraz prowadzona');
        ROLLBACK;
    END IF;
END;


CREATE OR REPLACE TRIGGER tg_tura
BEFORE INSERT ON tura
FOR EACH ROW
WHEN (new.data_rozp > new.data_zak)
DECLARE 
buf DATE;
BEGIN
    buf := :new.data_zak;
    :new.data_zak := :new.data_rozp;
    :new.data_rozp := buf; 
END;


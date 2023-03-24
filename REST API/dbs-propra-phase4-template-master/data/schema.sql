PRAGMA auto_vacuum = 1;
PRAGMA encoding = "UTF-8";
PRAGMA foreign_keys = 1;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;



DROP TABLE IF EXISTS Nutzer;
CREATE TABLE IF NOT EXISTS "Nutzer" (

	EMailAdresse VARCHAR NOT NULL  COLLATE NOCASE  CHECK(EMailAdresse is not "" AND EMailAdresse GLOB '?*[@]*?[.]*?'
	AND (SUBSTR(LOWER(EMailAdresse), 1, INSTR(EMailAdresse, '@')-1) NOT GLOB '*[^0-9a-z]*')
	AND(SUBSTR(LOWER(EMailAdresse), INSTR(EMailAdresse, '@')+1, ((INSTR(EMailAdresse,'.')-1) - (INSTR(EMailAdresse, '@')+1))+1)NOT GLOB '*[^0-9a-z]*')
	AND  (SUBSTR(LOWER(EMailAdresse), INSTR(EMailAdresse, '.')+1)NOT GLOB '*[^a-z]*')
	),
	"Password"  VARCHAR NOT NULL CHECK ( (LENGTH(Password) BETWEEN 4 AND 9) 
	AND (Password GLOB '*[0-9]*[0-9]*')
	AND (Password GLOB '*[A-Z]*')
	AND (LOWER(Password) NOT GLOB '*[aeiou][13579]*')
	AND (Password NOT GLOB '*[^ -~]*')),
	PRIMARY KEY(EMailAdresse)
); 


DROP TABLE IF EXISTS Projektleiter;
CREATE TABLE IF NOT EXISTS "Projektleiter" (
EMailAdresse VARCHAR NOT NULL COLLATE NOCASE,
Gehalt FLOAT NOT NULL CHECK (Gehalt IS ROUND(Gehalt ,2) AND Gehalt>0 ), 
FOREIGN KEY(EMailAdresse) REFERENCES Nutzer(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE, 
PRIMARY KEY(EMailAdresse) 
);




DROP TABLE IF EXISTS Projekt;
CREATE TABLE IF NOT EXISTS "Projekt" (

ProjektID INTEGER CHECK(ProjektID != 0),
Projektdeadline DATE NOT NULL  CHECK (Projektdeadline is date(Projektdeadline)),
Projektname TEXT NOT NULL CHECK ( Projektname is not "" AND  (Projektname NOT GLOB '*[^ -~]*')),
Telefonnumer VARCHAR(11) NOT NULL ,
EMailAdresse VARCHAR NOT NULL COLLATE NOCASE,

FOREIGN KEY(Telefonnumer) REFERENCES Kunde (Telefonnumer) ON UPDATE CASCADE ON DELETE CASCADE, 
FOREIGN KEY(EMailAdresse) REFERENCES Projektleiter(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(ProjektID) 
);



DROP TABLE IF EXISTS Aufgabe;
CREATE TABLE IF NOT EXISTS "Aufgabe" (

AufgabeID INTEGER CHECK(AufgabeID != 0),
Vermerk TEXT NOT NULL CHECK ( Vermerk is not "" AND  (Vermerk NOT GLOB '*[^ -~]*')),
Deadline DATE NOT NULL CHECK (Deadline is date(Deadline)),
Status TEXT NOT NULL CHECK ( Status is not "" AND  (Status NOT GLOB '*[^ -~]*')),
Beschreibung TEXT NOT NULL CHECK ( Beschreibung is not "" AND  (Status NOT GLOB '*[^ -~]*')),
ProjektID INTEGER ,

FOREIGN KEY(ProjektID) REFERENCES Projekt(ProjektID) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(AufgabeID)
);


DROP TABLE IF EXISTS Text;
CREATE TABLE IF NOT EXISTS "Text" (

TextID INTEGER CHECK(TextID != 0),
Inhalt  TEXT NOT NULL CHECK ( Inhalt is not "" AND  (Inhalt NOT GLOB '*[^ -~]*')),
BewertungID INTEGER NOT NULL,

FOREIGN KEY(BewertungID) REFERENCES Bewertung(BewertungID) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(TextID)
);



DROP TABLE IF EXISTS Bewertung;
CREATE TABLE IF NOT EXISTS "Bewertung" (

BewertungID INTEGER NOT NULL CHECK (BewertungID != 0),
Bepunktung INTEGER  NOT NULL CHECK( Bepunktung IN (1,2,3,4,5,6,7,8,9)),
Telefonnumer VARCHAR(11) NOT NULL,
ProjektID INTEGER NOT NULL,

FOREIGN KEY(Telefonnumer) REFERENCES Kunde (Telefonnumer) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(ProjektID) REFERENCES Projekt(ProjektID) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(BewertungID) 
);



DROP TABLE IF EXISTS Kunde;
CREATE TABLE IF NOT EXISTS "Kunde" (
EMailAdresse VARCHAR  UNIQUE NOT NULL  COLLATE NOCASE,
Telefonnumer VARCHAR(11)  NOT NULL CHECK ( LENGTH(Telefonnumer)=11 AND (Telefonnumer GLOB '0*') AND (Telefonnumer NOT GLOB '*[^0-9]*')),
FOREIGN KEY("EMailAdresse") REFERENCES Nutzer(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY("Telefonnumer")
);


DROP TABLE IF EXISTS Programmiersprache ;
CREATE TABLE IF NOT EXISTS "Programmiersprache" (

ProgrammierspracheID INTEGER CHECK (ProgrammierspracheID != 0),
Name  TEXT NOT NULL CHECK ( Name is not "" AND  (Name NOT GLOB '*[^ -~]*')),
Erfahrungsstufe INTEGER NOT NULL CHECK(Erfahrungsstufe in(1,2,3)),

PRIMARY KEY(ProgrammierspracheID)
);




DROP TABLE IF EXISTS Spezialist;
CREATE TABLE IF NOT EXISTS "Spezialist" (

EMailAdresse VARCHAR NOT NULL COLLATE NOCASE,
Verfuegbarkeitsstatus TEXT NOT NULL CHECK ( Verfuegbarkeitsstatus is not "" AND  (Verfuegbarkeitsstatus NOT GLOB '*[^ -~]*')),

FOREIGN KEY(EMailAdresse) REFERENCES Nutzer(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(EMailAdresse)
);

DROP TABLE IF EXISTS Entwickler;
CREATE TABLE IF NOT EXISTS "Entwickler" (
EMailAdresse VARCHAR NOT NULL UNIQUE  COLLATE NOCASE,
Kuerzel TEXT  COLLATE NOCASE CHECK ( LENGTH(Kuerzel)=8 AND (lower(Kuerzel) GLOB '[a-z][a-z][a-z][a-z][a-z][0-9][0-9][0-9]') ),
FOREIGN KEY(EMailAdresse) REFERENCES Spezialist(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(Kuerzel) 
);


DROP TABLE IF EXISTS Fachlicher_Kompetenz;
CREATE TABLE Fachlicher_Kompetenz (

    FachlicheKompetenzID INTEGER CHECK (FachlicheKompetenzID != 0),
    NameVonKompetenz    TEXT NOT NULL CHECK ( NameVonKompetenz is not "" AND  (NameVonKompetenz NOT GLOB '*[^a-zA-Z]*')),
    PRIMARY KEY(FachlicheKompetenzID)
);


DROP TABLE IF EXISTS Alias;
CREATE TABLE IF NOT EXISTS "Alias" (

AliasID INTEGER NOT NULL CHECK (AliasID != 0),
AliasName TEXT NOT NULL CHECK ( AliasName is not "" AND  (AliasName NOT GLOB '*[^ -~]*')),
EMailAdresse VARCHAR NOT NULL COLLATE NOCASE ,
FOREIGN KEY(EMailAdresse) REFERENCES Designer(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(AliasID)
);


DROP TABLE IF EXISTS Designer ;
CREATE TABLE IF NOT EXISTS "Designer" (
EMailAdresse VARCHAR  COLLATE NOCASE ,
Spezifikation TEXT NOT NULL CHECK ( Spezifikation is not "" AND  (Spezifikation NOT GLOB '*[^ -~]*')),
FOREIGN KEY(EMailAdresse) REFERENCES Spezialist(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(EMailAdresse) 
);



DROP TABLE IF EXISTS Weitere_Kompetenzen ;
CREATE TABLE IF NOT EXISTS "Weitere_Kompetenzen" (

WeitereKompetenzenID INTEGER NOT NULL   CHECK (WeitereKompetenzenID != 0),
Benennung TEXT  NOT NULL CHECK ( Benennung is not "" AND ( lower(Benennung) NOT GLOB '*[^a-z]*')),
PRIMARY KEY(WeitereKompetenzenID)
);


DROP TABLE IF EXISTS Designer_hat_WeitereKompetenzen;
CREATE TABLE IF NOT EXISTS "Designer_hat_WeitereKompetenzen" (
EMailAdresse VARCHAR COLLATE NOCASE NOT NULL ,
WeitereKompetenzenID INTEGER NOT NULL,
FOREIGN KEY(EMailAdresse) REFERENCES Designer(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(WeitereKompetenzenID) REFERENCES Weitere_Kompetenzen (WeitereKompetenzenID) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(EMailAdresse,"WeitereKompetenzenID")
);

DROP TABLE IF EXISTS Spezialist_hat_fachlicheKompetenz;
CREATE TABLE IF NOT EXISTS "Spezialist_hat_fachlicheKompetenz" (
EMailAdresse VARCHAR NOT NULL COLLATE NOCASE  ,
FachlicheKompetenzID INTEGER NOT NULL ,
FOREIGN KEY(EMailAdresse) REFERENCES Spezialist(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(FachlicheKompetenzID) REFERENCES Fachlicher_Kompetenz(FachlicheKompetenzID) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(EMailAdresse,FachlicheKompetenzID)
);



DROP TABLE IF EXISTS beherrscht;
CREATE TABLE IF NOT EXISTS "beherrscht" (
Kuerzel TEXT NOT NULL COLLATE NOCASE,
ProgrammierspracheID INTEGER NOT NULL ,
FOREIGN KEY(Kuerzel) REFERENCES Entwickler(Kuerzel) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(ProgrammierspracheID) REFERENCES Programmiersprache(ProgrammierspracheID) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(Kuerzel,ProgrammierspracheID)
);


DROP TABLE IF EXISTS arbeitet_an;
CREATE TABLE IF NOT EXISTS "arbeitet_an" (
EMailAdresse VARCHAR NOT NULL COLLATE NOCASE ,
ProjektID INTEGER  NOT NULL ,
FOREIGN KEY(ProjektID) REFERENCES Projekt(ProjektID) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(EMailAdresse) REFERENCES Spezialist(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(EMailAdresse,"ProjektID")
);



-- Ein Menntee kann höchstens ein Mentor haben
DROP TABLE IF EXISTS Mentoring;
CREATE TABLE IF NOT EXISTS "Mentoring" (
Mentor VARCHAR NOT NULL COLLATE NOCASE  ,
Mentee VARCHAR  NOT NULL COLLATE NOCASE ,
FOREIGN KEY(Mentor) REFERENCES Spezialist(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(Mentee) REFERENCES Spezialist(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(Mentee)
);





--Ein Kunde darf das gleiche Projekt höchstens 3 Mal bewerten
CREATE TRIGGER limitOfBewertung
AFTER INSERT on Bewertung
BEGIN
    SELECT CASE
    WHEN(        SELECT count(*)
                FROM Bewertung
                WHERE NEW.Telefonnumer=Telefonnumer 
                AND NEW.ProjektID=ProjektID )>=4
    THEN RAISE(ABORT, 'Ein Kunde darf das gleiche Projekt höchstens 3 Mal bewerten')
END;
END;
--TRIGGER INSERT INTO Bewertung VALUES (100, 8,'01364567891',2);





--Ein Mentor darf nicht (von einem anderen Mentor) betreut werden.
CREATE TRIGGER mentorin
AFTER INSERT on Mentoring
BEGIN
    SELECT CASE
        WHEN(    SELECT count(*)
                FROM Mentoring
                WHERE NEW."Mentee" = "Mentor")>=1
        THEN     RAISE(ABORT, 'Ein Mentor darf nicht (von einem anderen Mentor) betreut werden')
    END;
END;
--INSERT INTO Mentoring VALUES ('alexanAea@yahoo.fr' , 'alex@gmail.com');





--eine Programmiersprache kann nicht gelöscht werden, solange diese von jemandem beherrscht wird
CREATE TRIGGER delete_Programmiersprache
BEFORE DELETE on Programmiersprache
BEGIN
    SELECT CASE
        WHEN EXISTS(    SELECT * FROM beherrscht
                WHERE OLD.ProgrammierspracheID = beherrscht.ProgrammierspracheID )
        THEN     RAISE(ABORT, 'eine Programmiersprache kann nicht gelöscht werden, solange diese von jemandem beherrscht wird')
    END;
END;
--DELETE FROM Programmiersprache WHERE ProgrammierspracheID=1






--Ein Spezialist kann entweder ein  Entwickler oder ein Designer sein!
-- wenn ein Spezialist ein Entwickler ist,kann nicht zugleich ein Designer sein.
CREATE TRIGGER ValidatingDesigner BEFORE INSERT ON Designer
BEGIN
    SELECT
        CASE
            WHEN EXISTS(
                    SELECT * FROM entwickler
                    WHERE NEW.EMailAdresse = entwickler.EMailAdresse
                )
            THEN RAISE(ABORT,'Dieser Benutzer ist bereits ein Entwickler.')
        END;
END;


--Ein Spezialist kann entweder ein  Entwickler oder ein Designer sein!
-- wenn ein Spezialist ein Designer, ist,kann nicht zugleich ein Entwickler sein.

CREATE TRIGGER ValidatingEntwickler BEFORE INSERT ON Entwickler
BEGIN
    SELECT
        CASE
            WHEN EXISTS(
                    SELECT * FROM designer
                    WHERE NEW.EMailAdresse = designer.EMailAdresse
                )
            THEN RAISE(ABORT,'Dieser Benutzer ist bereits ein Designer.')
        END;
END;

--Beispiele, die nicht gültig sind :
--INSERT INTO Entwickler VALUES ('alex@gmail.com','aeert892');
--INSERT INTO Designer VALUES('6asss6dd@yahoo.fr','digital');











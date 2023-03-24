PRAGMA auto_vacuum = 1;
PRAGMA encoding = "UTF-8";
PRAGMA foreign_keys = 1;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;




INSERT INTO Nutzer VALUES ('alex@gmail.com' , 'adD89d');
INSERT INTO Nutzer VALUES ('mark69@gmail.com' , 'adD89p');
INSERT INTO Nutzer VALUES ('alexandra21@yahoo21.fr' , 'Adz89p');
INSERT INTO Nutzer VALUES ('alexandra@yahoo56.fr' , 'aBb63b');
INSERT INTO Nutzer VALUES ('alexandra123@yahoo.fr' , 'azB65t');
INSERT INTO Nutzer VALUES ('alexa96@yahoo.fr' , 'aUb23p');
INSERT INTO Nutzer VALUES ('alexanAea@yahoo.fr' , 'aKb78p');
INSERT INTO Nutzer VALUES ('awae@yahoo.fr' , 'bKb76p');
INSERT INTO Nutzer VALUES ('bcwae@yahoo.fr' , 'xKb38k');
INSERT INTO Nutzer VALUES ('ccddae@yahoo.fr' , 'nKl78l');

INSERT INTO Nutzer VALUES ('123ae@gmail.com' , 'nKl72l');
INSERT INTO Nutzer VALUES ('456eooe@yahoo.fr' , 'lKl789');
INSERT INTO Nutzer VALUES ('6asss6dd@yahoo.fr' , 'nKl78l');




INSERT INTO Projektleiter VALUES ('alex@gmail.com' , 36666.66);
INSERT INTO Projektleiter VALUES ('mark69@gmail.com' , 85566.21);
INSERT INTO Projektleiter VALUES ('alexandra21@yahoo21.fr' , 50566.66);
INSERT INTO Projektleiter VALUES ('alexandra@yahoo56.fr' , 30566.65);
INSERT INTO Projektleiter VALUES ('alexandra123@yahoo.fr' , 17266.66);
INSERT INTO Projektleiter VALUES ('alexa96@yahoo.fr' , 99566.66);

--case insensitiv :  INSERT INTO Projektleiter VALUES ('Alexa96@yahoo.fr' , 99566.66);


INSERT INTO Kunde VALUES ('alex@gmail.com' , '01234567891');
INSERT INTO Kunde VALUES ('mark69@gmail.com' , '01364567891');
INSERT INTO Kunde VALUES ('alexandra21@yahoo21.fr' , '06666567891');
INSERT INTO Kunde VALUES ('alexandra@yahoo56.fr' , '01234888891');
INSERT INTO Kunde VALUES ('alexandra123@yahoo.fr' , '01234567999');
INSERT INTO Kunde VALUES ('alexa96@yahoo.fr' , '01111167891');
INSERT INTO Kunde VALUES ('alexanAea@yahoo.fr' , '02222267891');






INSERT INTO Projekt VALUES (1 , '1999-09-06','aeae','01234567891','alex@gmail.com');
INSERT INTO Projekt VALUES (2 , '2000-07-03','arar','01364567891','mark69@gmail.com');
INSERT INTO Projekt VALUES (3 , '2009-06-12','rrr','06666567891','alexandra21@yahoo21.fr');
INSERT INTO Projekt VALUES (4 , '2020-03-09','tt','01234888891','alexandra@yahoo56.fr');
INSERT INTO Projekt VALUES (5 , '2022-02-03','zzz','01234567999','alexandra123@yahoo.fr');
INSERT INTO Projekt VALUES (6 , '2021-01-05','uuuu','01111167891','alexa96@yahoo.fr');



INSERT INTO Bewertung VALUES (1, 9,'01234567891',1);
INSERT INTO Bewertung VALUES (2, 8,'01234567891',1);
INSERT INTO Bewertung VALUES (3, 8,'01234567891',1);
INSERT INTO Bewertung VALUES (4, 8,'01234567891',4);

INSERT INTO Bewertung VALUES (5, 6,'01364567891',2);
INSERT INTO Bewertung VALUES (6, 2,'01364567891',2);
INSERT INTO Bewertung VALUES (7, 2,'01364567891',2);

INSERT INTO Bewertung VALUES (8, 6,'06666567891',3);
INSERT INTO Bewertung VALUES (9, 2,'06666567891',3);
INSERT INTO Bewertung VALUES (10, 6,'06666567891',3);
INSERT INTO Bewertung VALUES (11, 9,'06666567891',4);
INSERT INTO Bewertung VALUES (12, 8,'06666567891',5);




INSERT INTO Aufgabe VALUES (1, 'hoch','1999-09-06','verfgb', 'aaaaa',1);
INSERT INTO Aufgabe VALUES (10, 'hoch','1999-09-06','verfgb', 'wtwt',1);
INSERT INTO Aufgabe VALUES (11, 'hoch','2000-09-06','veraf', 'aeae',1);
INSERT INTO Aufgabe VALUES (12, 'hoch','2001-09-06','aea', 'avdde',1);


INSERT INTO Aufgabe VALUES (2, 'niedrig','2000-07-03','verfgb', 'bbb',2);
INSERT INTO Aufgabe VALUES (14, 'niedrig','2005-09-03','verfgub', 'bbbb',2);
INSERT INTO Aufgabe VALUES (22, 'niedrig','2006-07-03','verfgub', 'bbnmb',2);

INSERT INTO Aufgabe VALUES (3, 'hoch','2009-06-12','verfgb', 'ccc',3);
INSERT INTO Aufgabe VALUES (4, 'niedrig','2020-03-09','verfgb', 'ddd',4);
INSERT INTO Aufgabe VALUES (5, 'hoch','2022-02-03','verfgb', 'eee',5);
INSERT INTO Aufgabe VALUES (6, 'niedrig','2021-01-05','verfgb', 'rrrr',6);


INSERT INTO Text VALUES (1, 'eee',1);
INSERT INTO Text VALUES (2, 'aa',2);
INSERT INTO Text VALUES (3, 'eeq',3);
INSERT INTO Text VALUES (4, 'rq',5);
INSERT INTO Text VALUES (5, 'v',4);
INSERT INTO Text VALUES (6, 'ae',6);
INSERT INTO Text VALUES (7, 'rr',7);



INSERT INTO Programmiersprache VALUES (1, 'java',1);
INSERT INTO Programmiersprache VALUES (2, 'python',2);
INSERT INTO Programmiersprache VALUES (3, 'c',3);
INSERT INTO Programmiersprache VALUES (4, 'c++',1);
INSERT INTO Programmiersprache VALUES (5, 'pascal',2);





INSERT INTO Spezialist VALUES('alex@gmail.com','verg');
INSERT INTO Spezialist VALUES ('mark69@gmail.com','verg');
INSERT INTO Spezialist VALUES ('alexandra21@yahoo21.fr','nichtVerg');
INSERT INTO Spezialist VALUES ('alexandra@yahoo56.fr','verg');
INSERT INTO Spezialist VALUES ('alexandra123@yahoo.fr','nein');
INSERT INTO Spezialist VALUES ('alexa96@yahoo.fr','ja');
INSERT INTO Spezialist VALUES ('alexanAea@yahoo.fr','ja');

INSERT INTO Spezialist VALUES('123ae@gmail.com','jaaaaa');
INSERT INTO Spezialist VALUES ('456eooe@yahoo.fr','vergd');
INSERT INTO Spezialist VALUES ('6asss6dd@yahoo.fr','nichtVerg');


INSERT INTO Entwickler VALUES('123ae@gmail.com','abcde123');
INSERT INTO Entwickler VALUES ('456eooe@yahoo.fr','qwert563');
INSERT INTO Entwickler VALUES ('6asss6dd@yahoo.fr','ertzu789');





INSERT INTO Designer VALUES('alex@gmail.com','digital');
INSERT INTO Designer VALUES ('mark69@gmail.com','grafik');
INSERT INTO Designer VALUES ('alexandra21@yahoo21.fr','digital');
INSERT INTO Designer VALUES ('alexandra@yahoo56.fr','grafik');
INSERT INTO Designer VALUES ('alexandra123@yahoo.fr','grafik');
INSERT INTO Designer VALUES ('alexa96@yahoo.fr','digital');
INSERT INTO Designer VALUES ('alexanAea@yahoo.fr','digital');



INSERT INTO Fachlicher_Kompetenz  VALUES(1,'FrameworkErfahrung');
INSERT INTO Fachlicher_Kompetenz  VALUES(2,'PythonErfahrung');
INSERT INTO Fachlicher_Kompetenz  VALUES(3,'java');
INSERT INTO Fachlicher_Kompetenz  VALUES(4,'excel');
INSERT INTO Fachlicher_Kompetenz  VALUES(5,'Machinelearning');
INSERT INTO Fachlicher_Kompetenz  VALUES(6,'Deeplearning');





INSERT INTO Alias VALUES (1 ,'alx','alex@gmail.com');
INSERT INTO Alias VALUES (2,'mark','mark69@gmail.com');
INSERT INTO Alias VALUES (3,'acacv','alexandra21@yahoo21.fr');
INSERT INTO Alias VALUES (4,'ae','alexandra@yahoo56.fr');
INSERT INTO Alias VALUES (5,'alex','alexandra123@yahoo.fr');
INSERT INTO Alias VALUES (6,'ale','alexa96@yahoo.fr');
INSERT INTO Alias VALUES (7,'alexan','alexanAea@yahoo.fr');



INSERT INTO Weitere_Kompetenzen VALUES  (1,'PowerBI');
INSERT INTO Weitere_Kompetenzen VALUES  (2,'Tableau');
INSERT INTO Weitere_Kompetenzen VALUES  (3,'Sql');
INSERT INTO Weitere_Kompetenzen VALUES  (4,'NoSql');
INSERT INTO Weitere_Kompetenzen VALUES  (5,'Cpop');





INSERT INTO Mentoring VALUES ('alex@gmail.com' , 'alexandra@yahoo56.fr');	
INSERT INTO Mentoring VALUES ('alexanAea@yahoo.fr' , 'mark69@gmail.com');	
INSERT INTO Mentoring VALUES ('alexanAea@yahoo.fr' , 'alexandra21@yahoo21.fr');	
INSERT INTO Mentoring VALUES ('alexa96@yahoo.fr' , 'alexandra123@yahoo.fr');	


INSERT INTO Designer_hat_WeitereKompetenzen VALUES('alex@gmail.com',1);
INSERT INTO Designer_hat_WeitereKompetenzen VALUES ('mark69@gmail.com',2);
INSERT INTO Designer_hat_WeitereKompetenzen VALUES ('alexandra21@yahoo21.fr',3);
INSERT INTO Designer_hat_WeitereKompetenzen VALUES ('alexandra@yahoo56.fr',4);
INSERT INTO Designer_hat_WeitereKompetenzen VALUES ('alexandra123@yahoo.fr',5);

INSERT INTO Spezialist_hat_fachlicheKompetenz VALUES('alex@gmail.com',1);
INSERT INTO Spezialist_hat_fachlicheKompetenz VALUES ('mark69@gmail.com',2);
INSERT INTO Spezialist_hat_fachlicheKompetenz VALUES ('alexandra21@yahoo21.fr',3);
INSERT INTO Spezialist_hat_fachlicheKompetenz VALUES ('alexandra@yahoo56.fr',4);
INSERT INTO Spezialist_hat_fachlicheKompetenz VALUES ('alexandra123@yahoo.fr',5);
INSERT INTO Spezialist_hat_fachlicheKompetenz VALUES ('alexa96@yahoo.fr',6);



INSERT INTO beherrscht VALUES('abcde123',1);
INSERT INTO beherrscht VALUES ('qwert563',2);
INSERT INTO beherrscht VALUES ('ertzu789',3);





INSERT INTO arbeitet_an VALUES('alex@gmail.com',1);
INSERT INTO arbeitet_an VALUES ('mark69@gmail.com',2);
INSERT INTO arbeitet_an VALUES ('alexandra21@yahoo21.fr',3);
INSERT INTO arbeitet_an VALUES ('alexandra@yahoo56.fr',4);
INSERT INTO arbeitet_an VALUES ('alexandra123@yahoo.fr',5);
INSERT INTO arbeitet_an VALUES ('alexa96@yahoo.fr',6);







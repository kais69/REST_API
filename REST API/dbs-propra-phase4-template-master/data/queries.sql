--Geben Sie genau die Projekte (alle Attribute) aus, die mindestens drei Aufgaben mit hoher Priorit¨at haben.
-- Vermerk uber eine besondere Priorisierung (z.B. ¨ hoch).

SELECT p.* from Projekt p 
   INNER JOIN (select ProjektID From Aufgabe WHERE lower(Vermerk)='hoch' GROUP by ProjektID  having count(*)  >=3 ) as a
   on p.ProjektID=a.ProjektID ;



--Geben Sie genau das Projekt mit der besten Durchschnittsbewertung aus.


SELECT p.* from Projekt p 
   INNER JOIN 
  
   (SELECT av.ProjektID ,Max(avg_Bepunktung)
     from 
         ( select  ProjektID ,avg(Bepunktung) as avg_Bepunktung
          from Bewertung Group by ProjektID ) as av
		  ) as b
   on p.ProjektID=b.ProjektID  ;
   

 
   
-- Geben Sie in alphabetischer Reihenfolge die E-Mail-Adressen genau der Nutzer aus, die noch kein Projekt in Auftrag gegeben haben.
SELECT EMailAdresse from Nutzer WHERE EMailAdresse NOT in 

 (SELECT Kunde.EMailAdresse from Kunde 
  INNER JOIN Projekt on Projekt.Telefonnumer= Kunde.Telefonnumer)
  
ORDER by EMailAdresse;

   
   
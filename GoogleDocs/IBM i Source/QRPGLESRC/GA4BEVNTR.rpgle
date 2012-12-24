      //---------------------------------------------------------------------
      // Prototypes
      //---------------------------------------------------------------------

     D GA4BEVNTR       PR                  EXTPGM('GA4BEVNTR')
     D  account                     256
     D  password                    256
     D  title                       128
     D  desc                       1024
     D  strdt                         8
     D  strtm                         6
     D  enddt                         8
     D  endtm                         6
     D  remmth                        4
     D  remmin                        5
     D  location                    128

     D main            PR                  STATIC
     D                                     EXTPROC(*JAVA
     D                                           : 'com.csdainc.gfi.GA4BEvntJ'
     D                                           : 'main')
     D                                 O   CLASS(*JAVA:'java.lang.String')
     D                                     DIM(11)
     D                                     CONST

     D String          PR              O   EXTPROC(
     D                                       *JAVA:
     D                                       'java.lang.String':
     D                                       *CONSTRUCTOR)
     D                                     CLASS(
     D                                       *JAVA:
     D                                       'java.lang.String')
     D bytes                         50A    CONST VARYING

     Dargs             S               O   CLASS(
     D                                       *JAVA:
     D                                       'java.lang.String')
     D                                     DIM(11)

      //---------------------------------------------------------------------
      // *ENTRY Parameters
      //---------------------------------------------------------------------

     D GA4BEVNTR       PI
     D  account                     256
     D  password                    256
     D  title                       128
     D  desc                       1024
     D  strdt                         8
     D  strtm                         6
     D  enddt                         8
     D  endtm                         6
     D  remmth                        4
     D  remmin                        5
     D  location                    128

      /free

       args(1) = String(%trim(account));
       args(2) = String(%trim(password));
       args(3) = String(%trim(title));
       args(4) = String(%trim(desc));
       args(5) = String(%trim(strdt));
       args(6) = String(%trim(strtm));
       args(7) = String(%trim(enddt));
       args(8) = String(%trim(endtm));
       args(9) = String(%trim(remmth));
       args(10) = String(%trim(remmin));
       args(11)= String(%trim(location));

       main(args);

       *Inlr = *on;

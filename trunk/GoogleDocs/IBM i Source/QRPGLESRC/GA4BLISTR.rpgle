      // *-----------------------------------------------------------------*/
      // * Create Instructions:                                            */
      // *                                                                 */
      // *  CRTBNDRPG PGM(GA4BLISTR) SRCFILE(QRPGLESRC) DFTACTGRP(*NO)     */
      // *-----------------------------------------------------------------*/

      // *-----------------------------------------------------------------*/
      // *Prototypes                                                       */
      // *-----------------------------------------------------------------*/
     D GA4BLISTR       PR                  EXTPGM('GA4BLISTR')
     D  Email                       256
     D  Password                    256
     D  Document                    128
     D  Directory                   128
     D  IBMiName                      8

     D main            PR                  STATIC
     D                                     EXTPROC(*JAVA
     D                                        : 'com.csdainc.gfi.GA4BListJ'
     D                                        : 'main')
     D                                 O   CLASS(*JAVA:'java.lang.String')
     D                                     DIM(5)
     D                                     CONST

     D String          PR              O   EXTPROC(
     D                                     *JAVA:
     D                                     'java.lang.String':
     D                                     *CONSTRUCTOR)
     D                                     CLASS(
     D                                      *JAVA:
     D                                     'java.lang.String')
     D bytes                         50A   CONST VARYING

     D args            s               O   CLASS(
     D                                     *JAVA:
     D                                     'java.lang.String')
     D                                     DIM(5)

      // *-----------------------------------------------------------------*/
      // *Entry Parameters                                                */
      // *-----------------------------------------------------------------*/
     D GA4BLISTR       PI
     D  Email                       256
     D  Password                    256
     D  Document                    128
     D  Directory                   128
     D  IBMiName                      8

      /free

       args(1) = String(%trim(Email   ));
       args(2) = String(%trim(Password));
       args(3) = String(%trim(Document));
       args(4) = String(%trim(Directory));
       args(5) = String(%trim(IBMiName));

       main(args);

       *Inlr = *on;


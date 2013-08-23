000100130821      //---------------------------------------------------------------------
000101130821      // Prototypes
000102130821      //---------------------------------------------------------------------
000103130821
000104130821     D GA4BEMAILR      PR                  EXTPGM('GA4BEMAILR')
000105130821     D  email                       256
000106130821     D  password                    256
000107130821     D  toemail                     256
000108130821     D  subject                     128
000109130821     D  body                        512
000110130821     D  document                    128
000111130821     D  directory                   128
000112130821
000113130821     D main            PR                  STATIC
000114130821     D                                     EXTPROC(*JAVA
000115130821     D                                           : 'com.csdainc.gfi.GA4BEmailJ'
000116130821     D                                           : 'main')
000117130821     D                                 O   CLASS(*JAVA:'java.lang.String')
000118130821     D                                     DIM(7)
000119130821     D                                     CONST
000120130821
000121130821     D String          PR              O   EXTPROC(
000122130821     D                                       *JAVA:
000123130821     D                                       'java.lang.String':
000124130821     D                                       *CONSTRUCTOR)
000125130821     D                                     CLASS(
000126130821     D                                       *JAVA:
000127130821     D                                       'java.lang.String')
000128130822     D bytes                        512A   CONST VARYING
000129130821
000130130821     Dargs             S               O   CLASS(
000131130821     D                                       *JAVA:
000132130821     D                                       'java.lang.String')
000133130821     D                                     DIM(7)
000134130821
000135130821      //---------------------------------------------------------------------
000136130821      // *ENTRY Parameters
000137130821      //---------------------------------------------------------------------
000138130821
000139130821     D GA4BEMAILR      PI
000140130821     D  email                       256
000141130821     D  password                    256
000142130821     D  toemail                     256
000143130821     D  subject                     128
000144130821     D  body                        512
000145130821     D  document                    128
000146130821     D  directory                   128
000147130821
000148130821      /free
000149130821
000150130821       args(1) = String(%trim(email));
000151130821       args(2) = String(%trim(password));
000152130821       args(3) = String(%trim(toemail));
000153130821       args(4) = String(%trim(subject));
000154130821       args(5) = String(%trim(body));
000156130821       args(6) = String(%trim(document));
000157130821       args(7) = String(%trim(directory));
000158130821
000159130821       main(args);
000160130821
000161130821       *Inlr = *on;

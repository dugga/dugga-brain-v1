     Hdftactgrp(*no) actgrp(*caller) bnddir('ORUBND')

     D sendErrorMessage...
     D                 PR          1024A   Varying
     D  message                    1024A   Varying

     D GetProjects     PR
     D  parmDS                             Likeds(dsProjs) Dim(9999)

     D GetTasks        PR
     D  selectedProject...
     D                             1024A   Varying Const
     D  parmDS                             Likeds(dsTasks) Dim(9999)

     D GetTaskDetails...
     D                 PR
     D  selectedProject...
     D                             1024A   Varying Const
     D  selectedTask...
     D                             1024A   Varying Const
     D  selectedSubtask...
     D                             1024A   Varying Const
     D  parmDS                             Likeds(dsTaskDetails) Dim(9999)

     D getTaskDescription...
     D                 PR          1024a   Varying
     D  selectedProject...
     D                             1024A   Varying Const
     D  selectedTask...
     D                             1024A   Varying Const
     D  selectedSubtask...
     D                             1024A   Varying Const

     D escapeString    PR          1024a   varying
     D  string                     1024a   varying value

      /include qrpglecpy,LLIST_H
      /include qrpglecpy,HTTP
      /include qrpglecpy,JSON_H
      /include qrpglecpy,JSON_INT_H

     D dsProjs         ds                  Qualified  Dim(9999)
     D  fProj                         4
     D  fDesc                        80

     D dsTasks         ds                  Qualified  Dim(9999)
     D  fTproj                        4
     D  fTtask                        4
     D  fTsubt                        2
     D  fTDesc                       80

     D dsTaskDetails   ds                  Qualified  Dim(9999)
     D  fDproj                        4
     D  fDtask                        4
     D  fDsubt                        2
     D  fDdta                        80

     D inHTTP          ds                  Qualified
     D  inAction                     30A   Varying
     D  inInput                    1024A   Varying

      // variables
     D stdIn           s          65535a   varying
     D jsonRequest     s               *
     D jsonAction      s             20a   varying
     D jsonResponse    s               *

     D msgTxt          s           1024a   varying

     D index           s              9  0
     D PrjLst          s          65535A   Varying
     D TskLst          s          65535A   Varying
     D TskDtl          s          65535A   Varying
     D values          s          65535A   Varying


      /free

       // ***********************************************
       // Initialization
       // ***********************************************

       jsonResponse = json_create();
       stdIn = http_inStr();

       // If the input was blanks then we dont' want to try and parse it.
       if stdIn = *blanks;
         msgTxt = 'No HTTP input request';
         http_outStr(sendErrorMessage(msgTxt));
       else;
         jsonRequest = json_parse(%addr(stdIn)+2);
         jsonAction = %str(json_getString(jsonRequest: 'action'));
       endif;

         // **********
         // Get projects
         // **********
       select;
         when jsonAction = 'getProjects';
           GetProjects(dsProjs);

           for index = 1 TO 9999;
             if (dsProjs(index).fProj) = *Blanks;
               leave;
             endif;
             if (index <> 1);
               PrjLst += ', ';
             endif;
             PrjLst += '{"NAME" : "' + %trim(dsProjs(index).fProj) + '", ' +
                       '"TYPE" : "LIST", ' +
                       '"VALUE" : "' +  %trim(dsProjs(index).fProj) + '<br>' +
                       '<small>' +
                       escapeString(dsProjs(index).fDesc) +
                       '</small>' +
                       '", ' +
                       '"X" : 20, ' +
                       '"Y" : 20}';
           endfor;

           http_outStr(
            'Content-Type: text/plain' + x'1515' +
            '{"FORM": {"ACTION" : "TASKINQUIRY",' +
                      '"FLDLST" : [' + prjLst + '], ' +
                      '"NAME" : "projectInquiry"}, ' +
                      '"msg" : " ", ' +
                      '"success" : true}');

         // **********
         // get tasks
         // **********
         when jsonAction = 'getTasks';
            GetTasks(%str(json_getString(jsonRequest: 'selectedProject')):
                     dsTasks);

           for index = 1 TO 9999;
             if (dsTasks(index).fTproj) = *Blanks;
               leave;
             endif;
             if (index <> 1);
               TskLst += ', ';
             endif;

             TskLst += '{"NAME" : "' + dsTasks(index).fTProj +
                                       dsTasks(index).fTtask +
                                       dsTasks(index).fTsubt + '", ' +
                       '"TYPE" : "LIST", ' +
                       '"VALUE" : "' + dsTasks(index).fTProj +
                                       dsTasks(index).fTtask +
                                       dsTasks(index).fTsubt + '<br>' +
                       '<small>' +
                       escapeString(dsTasks(index).fTdesc) +
                       '</small>' +
                       '", ' +
                       '"X" : 20, ' +
                       '"Y" : 20}';

           endfor;

           http_outStr(
            'Content-Type: text/plain' + x'1515' +
            '{"FORM": {"ACTION" : "TASKINQUIRY",' +
                      '"FLDLST" : [' + TskLst + '], ' +
                      '"NAME" : "taskInquiry"}, ' +
                      '"msg" : " ", ' +
                      '"success" : true}');

         // **********
         // get task details
         // **********
         when jsonAction = 'getTaskDetails';
           GetTaskDetails(%str(json_getString(jsonRequest:'selectedProject')):
                          %str(json_getString(jsonRequest:'selectedTask')):
                          %str(json_getString(jsonRequest:'selectedSubtask')):
                          dsTaskDetails);

           for index = 1 TO 9999;
             if (index = 1);  // write header
               TskDtl = '{"NAME" : "' +
                         %str(json_getString(jsonRequest:'selectedProject')) +
                         %str(json_getString(jsonRequest:'selectedTask')) +
                         %str(json_getString(jsonRequest:'selectedSubtask')) +
                         '", ' +
                         '"TYPE" : "LBL", ' +
                         '"VALUE" : "' +
                         %str(json_getString(jsonRequest:'selectedProject')) +
                         %str(json_getString(jsonRequest:'selectedTask')) +
                         %str(json_getString(jsonRequest:'selectedSubtask')) +
                         '<br>' +
                         '<small>' +
                         escapeString(getTaskDescription(
                         %str(json_getString(jsonRequest:'selectedProject')):
                         %str(json_getString(jsonRequest:'selectedTask')):
                         %str(json_getString(jsonRequest:'selectedSubtask')))) +
                         '</small>' +
                         '", ' +
                         '"X" : 20, ' +
                         '"Y" : 20}, ' +
                        '{"NAME" : "divider", ' +
                         '"TYPE" : "DIV", ' +
                         '"VALUE" : " ", ' +
                         '"X" : 20, ' +
                         '"Y" : 20}';
             endif;

             if (dsTaskDetails(index).fDproj) = *Blanks;
       //         TskDtl += '{"NAME" : "' + dsTaskDetails(index).fDproj +
       //                                   dsTaskDetails(index).fDtask +
       //                                   dsTaskDetails(index).fDsubt + '", ' +
       //               '"TYPE" : "VIEWTXT", ' +
       //               '"VALUE" : " ***no task details found***", ' +
       //               '"X" : 20, ' +
       //               '"Y" : 20}';
               leave;
             endif;

             TskDtl += ', ';
             TskDtl += '{"NAME" : "' + dsTaskDetails(index).fDproj +
                                       dsTaskDetails(index).fDtask +
                                       dsTaskDetails(index).fDsubt + '", ' +
                      '"TYPE" : "VIEWTXT", ' +
                      '"VALUE" : "' + escapeString(dsTaskDetails(index).fDdta) +
                      '", ' +
                      '"X" : 20, ' +
                      '"Y" : 20}';
           endfor;

           http_outStr(
            'Content-Type: text/plain' + x'1515' +
            '{"FORM": {"ACTION" : "TASKINQUIRY",' +
                      '"FLDLST" : [' + TskDtl + '], ' +
                      '"NAME" : "taskDetails"}, ' +
                      '"msg" : " ", ' +
                      '"success" : true}');
         other;
           msgTxt = 'Invalid HTTP action request';
           http_outStr(sendErrorMessage(msgTxt));
       endsl;


       *Inlr = *on;


      /End-free

       // ***********************************************
       // Get Projects
       // ***********************************************

     P GetProjects     B
     D GetProjects     PI
     D  piDS                               Likeds(pidsProjs) DIM(9999)
     D

     D pidsProjs       ds                  Qualified  dim(9999)
     D  pifProj                       4
     D  pifDesc                      80

      /Free

       EXEC SQL
         Declare C1 cursor for
           select JPROJ, JDESC from TODXDD/TPRJPRJ
           order by JPROJ;
       EXEC SQL
         Open C1;
       EXEC SQL
         Fetch C1 for 9999 rows into :pidsProjs;
       EXEC SQL
         Close C1;

       // if no records (sqlca <> 02?) then write a message here

       piDS = pidsProjs;

       return;

      /End-free

     P GetProjects     E

       // ***********************************************
       // Get Tasks for Project
       // ***********************************************

     P GetTasks        B
     D GetTasks        PI
     D  piSltPrj                   1024A   Varying Const
     D  piDS                               Likeds(pidsTasks) Dim(9999)

     D pidsTasks       ds                  Qualified  Dim(9999)
     D  piTproj                       4
     D  piTtask                       4
     D  piTsubt                       2
     D  piTdesc                      80

      /Free

       EXEC SQL
         Declare C2 cursor for
           select TPROJ,TTASK,TSUBT,TDESC from TODXDD/TPRJTSK
           where TPROJ = :piSltPrj
           order by TPROJ,TTASK,TSUBT;
       EXEC SQL
         Open C2;
       EXEC SQL
         Fetch C2 for 9999 rows into :pidsTasks;
       EXEC SQL
         Close C2;

       // if no records (sqlca <> 02?) then write a message here

       piDS = pidsTasks;

       return;

      /End-free

     P GetTasks        E

       // ***********************************************
       // Get Tasks Details
       // ***********************************************

     P GetTaskDetails...
     P                 B
     D GetTaskDetails...
     D                 PI
     D  selectedProject...
     D                             1024A   Varying Const
     D  selectedTask...
     D                             1024A   Varying Const
     D  selectedSubtask...
     D                             1024A   Varying Const
     D  piDS                               Likeds(pidsTaskDetails) Dim(9999)

     D pidsTaskDetails...
     D                 ds                  Qualified  Dim(9999)
     D  piDproj                       4
     D  piDtask                       4
     D  piDsubt                       2
     D  piDdta                       80

      /Free

       EXEC SQL
         Declare C3 cursor for
           select PDPROJ,PDTASK,PDSUBT,SRCDTA from TODXDD/TPRJDSC
           where PDPROJ = :selectedProject and
                 PDTASK = :selectedTask and
                 PDSUBT = :selectedSubtask
           order by PDPROJ,PDTASK,PDSUBT,SRCSEQ;
       EXEC SQL
         Open C3;
       EXEC SQL
         Fetch C3 for 9999 rows into :pidsTaskDetails;
       EXEC SQL
         Close C3;

       // if no records (sqlca <> 02?) then write a message here

       piDS = pidsTaskDetails;

       return;

      /End-free

     P GetTaskDetails...
     P                 E

       // ***********************************************
       // Escape String
       // ***********************************************

     P escapeString    B
     D escapeString    PR          1024a   varying
     D  string                     1024a   varying value
     D escapeString    PI          1024a   varying
     D  string                     1024a   varying value

     D workString      S           1024a   varying
      /Free

       workString = %scanrpl('\':'\\':string);
       workString = %scanrpl('"':'\"':workString);

       return workString;

      /End-free


     P escapeString    E

       // ***********************************************
       // get Task Description
       // ***********************************************

     P getTaskDescription...
     P                 B

     D getTaskDescription...
     D                 PI          1024A   Varying
     D  selectedProject...
     D                             1024A   Varying Const
     D  selectedTask...
     D                             1024A   Varying Const
     D  selectedSubtask...
     D                             1024A   Varying Const

     D Desc            S             80
      /Free

       Desc = 'not found';

       Exec sql
         select TDESC into :Desc
           from TODXDD/TPRJTSK
           where TPROJ = :selectedProject and
                 TTASK = :selectedTask and
                 TSUBT = :selectedSubtask
           fetch first row only;

       // if no records (sqlca <> 02?) then write a message here

       return Desc;


      /End-free


     P getTaskDescription...
     P                 E

       // ***********************************************
       // Invalid Input
       // ***********************************************

     P sendErrorMessage...
     P                 B
     D sendErrorMessage...
     D                 PR          1024A   Varying
     D  message                    1024A   Varying
     D sendErrorMessage...
     D                 PI          1024A   Varying
     D  message                    1024A   Varying

      /Free

       return  ('Content-Type: text/plain' + x'1515' +
        '{"FORM": {"ACTION" : "VLDLOGIN",' +
                  '"FLDLST" : "NAME" : "error" ' +
                               '}, ' +
                               '"msg" : "' + message + '", ' +
                               '"success" : true ' +
                               '}');

      /End-free


     P sendErrorMessage...
     P                 E


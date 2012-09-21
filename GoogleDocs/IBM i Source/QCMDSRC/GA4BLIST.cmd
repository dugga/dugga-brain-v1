/* *-----------------------------------------------------------------*/
/* *  Create Instructions:                                           */
/* *                                                                 */
/* *  CRTCMD CMD(GA4BLIST) PGM*LIBL/GA4BLISTC) SRCFILE(QCMDSRC)      */
/* *         HLPPNLGRP(GA4BLISTP) HLPID(*CMD)                        */
/* *-----------------------------------------------------------------*/
             CMD        PROMPT('GA4B API list documents')

             PARM       KWD(EMAIL) TYPE(*CHAR) LEN(256) MIN(1) EXPR(*YES) CASE(*MIXED) CHOICE('EX:  +
                          MYACCOUNT@GMAIL.COM') PROMPT('Email')
             PARM       KWD(PASSWORD) TYPE(*CHAR) LEN(256) MIN(1) EXPR(*YES) CASE(*MIXED) DSPINPUT(*NO) +
                          PROMPT('Password')
             PARM       KWD(DOCUMENT) TYPE(*CHAR) LEN(128) DFT('Document_List.txt') EXPR(*YES) CASE(*MIXED) +
                          PROMPT('IFS Document')
             PARM       KWD(DIRECTORY) TYPE(*CHAR) LEN(128) DFT('/Google_Docs/output') EXPR(*YES) CASE(*MIXED) +
                          PROMPT('IFS Directory')

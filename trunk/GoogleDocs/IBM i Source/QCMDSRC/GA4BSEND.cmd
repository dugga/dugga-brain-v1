/*-----------------------------------------------------------------*/
/*  Create Instructions:                                           */
/*                                                                 */
/*  CRTCMD CMD(GA4BSEND) PGM*LIBL/GA4BSENDC) SRCFILE(QCMDSRC)      */
/*         HLPPNLGRP(GA4BSENDP) HLPID(*CMD)                        */
/*-----------------------------------------------------------------*/
             CMD        PROMPT('GA4B API send documents')

             PARM       KWD(EMAIL) TYPE(*CHAR) LEN(256) MIN(1) EXPR(*YES) CASE(*MIXED) CHOICE('EX:  +
                          MYACCOUNT@GMAIL.COM') PROMPT('Email')
             PARM       KWD(PASSWORD) TYPE(*CHAR) LEN(256) MIN(1) EXPR(*YES) CASE(*MIXED) DSPINPUT(*NO) +
                          PROMPT('Password')
             PARM       KWD(DOCUMENT) TYPE(*CHAR) LEN(128) MIN(1) EXPR(*YES) CASE(*MIXED) PROMPT('IFS Document')
             PARM       KWD(DIRECTORY) TYPE(*CHAR) LEN(128) MIN(1) EXPR(*YES) CASE(*MIXED) PROMPT('IFS Directory')


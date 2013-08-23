000100130821/* *-----------------------------------------------------------------*/
000101130821/* *  CREATE INSTRUCTIONS:                                           */
000102130821/* *                                                                 */
000103130821/* *  CRTCMD CMD(GA4BEMAIL) PGM(*LIBL/GA4BEMAILC) SRCFILE(QCMDSRC)   */
000104130821/* *         HLPPNLGRP(GA4BEMAIL) HLPID(*CMD)                        */
000105130821/* *-----------------------------------------------------------------*/
000106130821             CMD        PROMPT('GA4B API SEND DOCUMENTS')
000107130821
000108130821             PARM       KWD(EMAIL) TYPE(*CHAR) LEN(256) MIN(1) +
000109130821                          EXPR(*YES) CASE(*MIXED) CHOICE('ex:  +
000110130821                          MyAccount@Gmail.com') PROMPT('Email')
000111130821             PARM       KWD(PASSWORD) TYPE(*CHAR) LEN(256) MIN(1) +
000112130821                          EXPR(*YES) CASE(*MIXED) DSPINPUT(*NO) +
000113130821                          PROMPT('Password')
000114130821             PARM       KWD(TOEMAIL) TYPE(*CHAR) LEN(256) MIN(1) +
000115130821                          EXPR(*YES) CASE(*MIXED) CHOICE('ex:  +
000116130821                          MyAccount@GMAIL.COM') PROMPT('To Email +
000117130821                          Address')
000118130821             PARM       KWD(SUBJECT) TYPE(*CHAR) LEN(128) EXPR(*YES) +
000119130821                          CASE(*MIXED) PROMPT('Email Subject')
000120130821             PARM       KWD(BODY) TYPE(*CHAR) LEN(512) EXPR(*YES) +
000121130821                          CASE(*MIXED) PROMPT('Email Body')
000122130821             PARM       KWD(DOCUMENT) TYPE(*CHAR) LEN(128) +
000123130821                          EXPR(*YES) CASE(*MIXED) PROMPT('IFS +
000124130821                          Document')
000125130821             PARM       KWD(DIRECTORY) TYPE(*CHAR) LEN(128) +
000126130821                          EXPR(*YES) CASE(*MIXED) PROMPT('IFS +
000127130821                          Directory')
000128130821

             CMD        PROMPT('GA4B API put event')

             PARM       KWD(EMAIL) TYPE(*CHAR) LEN(256) MIN(1) EXPR(*YES) CASE(*MIXED) CHOICE('ex: +
                          MyAccount@Gmail.com') PROMPT('Email')
             PARM       KWD(PASSWORD) TYPE(*CHAR) LEN(256) MIN(1) EXPR(*YES) CASE(*MIXED) DSPINPUT(*NO) +
                          PROMPT('Password')
             PARM       KWD(TITLE) TYPE(*CHAR) LEN(128) MIN(1) EXPR(*YES) CASE(*MIXED) PROMPT('Title')
             PARM       KWD(DESC) TYPE(*CHAR) LEN(1024) EXPR(*YES) MIN(1) CASE(*MIXED) PROMPT('Description')
             PARM       KWD(STRDT) TYPE(*DATE) MIN(1) EXPR(*YES) PROMPT('Start date')
             PARM       KWD(STRTM) TYPE(*TIME) MIN(1) EXPR(*YES) PROMPT('Start time')
             PARM       KWD(ENDDT) TYPE(*DATE) MIN(1) EXPR(*YES) PROMPT('End date')
             PARM       KWD(ENDTM) TYPE(*TIME) MIN(1) EXPR(*YES) PROMPT('End time')
             PARM       KWD(REM) TYPE(*CHAR) LEN(4) RSTD(*YES) DFT(*YES) VALUES(*YES *NO) MIN(0) EXPR(*YES) +
                          PROMPT('Reminder')
             PARM       KWD(REMMIN) TYPE(*DEC) LEN(5) EXPR(*YES) PROMPT('Reminder period in minutes')
             PARM       KWD(LOC) TYPE(*CHAR) LEN(128) EXPR(*YES) CASE(*MIXED) PROMPT('Location')

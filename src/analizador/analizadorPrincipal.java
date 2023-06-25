
package analizador;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;


public class analizadorPrincipal extends javax.swing.JFrame {

    NumeroLinea nl;
    
    Stack<String> pilaSintactico = new Stack<String>();
    Stack<String> aux = new Stack<String>();
    String prueba = "", temp = "", res, err, pilares = "$ prog",alf = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\"'+-*/%(){},;. \n=<>!&|$"; 
    String reser[] = {"InicioProgra", "ent", "deci", "car", "cad", "decicion", "si", "sn", "mientras", "hacer", "para", "VERDAD", "FALSO", "Fun", "Proc", "Finprograma"};
    String terminales[] = {"InicioProgra", "ent", "deci", "car", "cad", "decicion", "Proc", "Fun", ",", "=", ";", "{", "}", "(", ")", "id", "num", "litcar", "litcad", "VERDAD", "FALSO", "+", "-", "*", "/", "%", "<", ">", "@", "#", "?", "¡", "Y", "O", "!", "si", "mientras", "hacer", "sn", "Finprograma","$"};
    String noTerminales[] = {"prog", "dec", "dec2", "sigdec", "modulo", "list_arg", "sigarg", "bloque", "sentencias", "sigsi", "L", "L'", "R", "R'", "E", "E'", "T", "T'", "F"};
    String sintactico[][] = {
               //                     InicioProgra                            ent                         deci                              car                             cad                           decicion                               Proc                                Fun                                ,                  =                     ;              {                    }             (            )            id                      num        litcar     litcad     VERDAD     FALSO         +           -           *           /          %           <           >          @           #             ?           ¡            Y           O            !            si                                  mientras                                hacer                                         sn                           Finprograma        $  
/*prog*/       {"InicioProgra id ; dec modulo sentencias Finprograma",	   "saltar",	                "saltar",	                 "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "saltar",	       "saltar",             "saltar",      "saltar",            "saltar",     "saltar",    "saltar",     "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*dec*/        {"saltar",	                                           "ent id dec2 ; dec",	        "deci id dec2 ; dec",	         "car id dec2 ; dec",            "cad id dec2 ; dec",            "decicion id dec2 ; dec",	       "0",	                          "0",	                            "saltar",	       "saltar",             "saltar",      "saltar",            "sacar",     "saltar",    "saltar",     "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*dec2*/       {"saltar",	                                           "saltar",	                "saltar",	                 "saltar",                       "saltar",                       "saltar",                             "saltar",	                  "saltar",	                    ", id dec2",       "= L sigdec dec2",    "0",           "saltar",            "sacar",     "saltar",    "saltar",     "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},   
/*sigdec*/     {"saltar",	                                           "saltar",	                "saltar",	                 "saltar",                       "saltar",                       "saltar",                             "saltar",	                  "saltar",	                    ", id dec2",       "saltar",             "0",           "saltar",            "sacar",     "saltar",    "saltar",     "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*modulo*/     {"saltar",	                                           "saltar",	                "saltar",	                 "saltar",                       "saltar",                       "saltar",                             "Proc id ( list_arg ) bloque",	  "Fun id ( list_arg ) bloque",     "saltar",	       "saltar",             "saltar",      "saltar",            "sacar",     "saltar",    "saltar",     "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"}, 
/*list_arg*/   {"saltar",	                                           "ent id sigarg , list_arg",	"deci id sigarg , list_arg",	 "car id sigarg , list_arg",   	 "cad id sigarg , list_arg",   	 "decicion id sigarg , list_arg",      "saltar",	                  "saltar",	                    "saltar",          "saltar",             "saltar",      "saltar",            "sacar",     "saltar",    "0",          "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"}, 
/*sigarg*/     {"saltar",	                                           "saltar",	                "saltar",	                 "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "= L",                "saltar",      "saltar",            "sacar",     "saltar",    "saltar",     "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*bloque*/     {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "saltar",          "saltar",             "saltar",      "{ sentencias }",    "sacar",     "saltar",    "saltar",     "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",	                             "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*sentencias*/ {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "saltar",          "saltar",             "saltar",      "saltar",            "0",         "saltar",    "saltar",     "id = L ; sentencias",   "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "si ( L ) bloque sigsi sentencias",    "mientras ( L ) bloque sentencias",    "hacer bloque mientras ( L ) sentencias",   "saltar",                        "0",           "sacar"},
/*sigsi*/      {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "saltar",          "saltar",             "saltar",      "saltar",            "0",         "saltar",    "saltar",     "0",                     "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "0",                                   "0",                                   "0",                                        "sn bloque sentencias",          "0",           "sacar"},  
/*L*/          {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "saltar",             "sacar",       "saltar",            "sacar",     "R L'",      "saltar",     "R L'",                  "R L'",     "R L'",    "R L'",    "R L'",    "R L'",    "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "! L",       "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*L'*/         {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "saltar",             "0",           "saltar",            "sacar",     "saltar",    "0",          "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "0",        "0",        "0",        "0",        "0",        "0",        "0",       "0",        "0",          "0",        "0",        "Y R L'",    "O R L'",    "0",         "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*R*/          {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "saltar",             "0",           "saltar",            "sacar",     "E R'",      "saltar",     "E R'",                  "E R'",     "E R'",    "E R'",    "E R'",    "E R'",    "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*R'*/         {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "saltar",             "0",           "saltar",            "sacar",     "saltar",    "0",          "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "0",        "0",        "0",        "0",        "0",        "< E R'",   "> E R'",  "@ E R'",   "# E R'",	"? E R'",   "¡ E R'",	"0",         "0",         "0",         "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*E*/          {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "saltar",             "0",           "saltar",            "sacar",     "T E'",      "saltar",     "T E'",                  "T E'",     "T E'",    "T E'",    "T E'",    "T E'",    "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},                                                                        
/*E'*/         {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "saltar",             "0",           "saltar",            "sacar",     "saltar",    "0",          "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "+ T E'",   "- T E'",   "0",        "0",        "0",        "0",        "0",       "0",        "0",          "0",        "0",	"0",         "0",         "0",         "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*T*/          {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "saltar",             "0",           "saltar",            "sacar",     "F T'",      "saltar",     "F T'",                  "F T'",     "F T'",    "F T'",    "F T'",    "F T'",    "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",	"saltar",   "saltar",	"saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*T'*/         {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "0",               "saltar",             "0",           "saltar",            "sacar",     "saltar",    "0",          "saltar",                "saltar",   "saltar",  "saltar",  "saltar",  "saltar",  "0",        "0",        "* F T'",   "/ F T'",   "% F T'",   "0",        "0",       "0",        "0",          "0",        "0",        "0",         "0",         "0",         "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},
/*F*/          {"saltar",	                                           "saltar",	                "saltar",                        "saltar",                       "saltar",                       "saltar",	                       "saltar",	                  "saltar",	                    "saltar",          "saltar",             "saltar",      "saltar",            "sacar",     "( L )",     "saltar",     "id",                    "num",      "litcar",  "litcad",  "VERDAD",  "FALSO",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",   "saltar",  "saltar",   "saltar",     "saltar",   "saltar",   "saltar",    "saltar",    "saltar",    "saltar",                              "saltar",                              "saltar",                                   "saltar",                        "saltar",      "sacar"},   
    };
    
    int pos = 0, est = 0, pri = 0, cade = 0, linea = 1, ind = 0, nerr = 0, concuerda = 0, recuperarErrorLexico = 0;    
    int acep[] = {1, 3, 4, 5, 6, 7, 9, 12, 13, 14, 16, 18};
    int tab[][] = {
       //  1      2      3     4      5       6       7      8       9       10     11    12    13    14     15    16     17    18     19     20     21     22     23    24     25     26     27     28     29     30    31     32    33     34     35     36     37     38     39     40     41     42     43     44     45     46    47    48     49    50     51     52     53      54    55      56     57     58    59    60     61    62     63    64     65     66     67     68    69     70    71    72    73    74    75    76     77     78      79    80     81     82     83      84     85
 /*1*/  {  5,     5,     5,    5,     5,      5,      5,     5,      5,      5,      5,    5,    5,    5,     5,    5,     5,    5,     5,     5,     5,     5,     5,    5,     5,     5,     5,     5,     5,     5,    5,     5,    5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,    5,    5,     5,    5,     5,     5,     4,      1,    1,      1,     1,     1,    1,    1,     1,    1,     8,   10,    13,    13,    13,    13,   13,     7,    7,    7,    7,    7,    7,   -1,     0,     0,      6,    6,     6,     6,    15,     17,    50}, 
 /*2*/  { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,     1,      1,    1,      1,     1,     1,    1,    1,     1,    1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,    2,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50}, 
 /*3*/  { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,     2,      3,    3,      3,     3,     3,    3,    3,     3,    3,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,    -1,    -1,     -1,   -1,    -1,    -1,    -1,     -1,    50}, 
 /*4*/  { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,     2,      3,    3,      3,     3,     3,    3,    3,     3,    3,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50},  
 /*5*/  { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,    2,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50}, 
 /*6*/  {  5,     5,     5,    5,     5,      5,      5,     5,      5,      5,      5,    5,    5,    5,     5,    5,     5,    5,     5,     5,     5,     5,     5,    5,     5,     5,     5,     5,     5,     5,    5,     5,    5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,    5,    5,     5,    5,     5,     5,     5,      5,    5,      5,     5,     5,    5,    5,     5,    5,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50}, 
 /*7*/  { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     14,   -1,    -1,    -1,    -1,     -1,    50}, 
 /*8*/  { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50},
 /*9*/  {  8,     8,     8,    8,     8,      8,      8,     8,      8,      8,      8,    8,    8,    8,     8,    8,     8,    8,     8,     8,     8,     8,     8,    8,     8,     8,     8,     8,     8,     8,    8,     8,    8,     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,     8,    8,    8,     8,    8,     8,     8,     8,      8,    8,      8,     8,     8,    8,    8,     8,    8,     9,    8,     8,     8,     8,     8,    8,     8,    8,    8,    8,    8,    8,    8,     8,     8,      8,    8,     8,     8,     8,      8,    50},
 /*10*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50},
 /*11*/ { 11,    11,    11,   11,    11,     11,     11,    11,     11,     11,     11,   11,   11,   11,    11,   11,    11,   11,    11,    11,    11,    11,    11,   11,    11,    11,    11,    11,    11,    11,   11,    11,   11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,    11,   11,   11,    11,   11,    11,    11,    11,     11,   11,     11,    11,    11,   11,   11,    11,   11,    11,   12,    11,    11,    11,    11,   11,    11,   11,   11,   11,   11,   11,   11,    11,    11,     11,   11,    11,    11,    11,     11,    50},
 /*12*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   12,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,    -1,    -1,     -1,   -1,    -1,    -1,    -1,     -1,    50},
 /*13*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50},
 /*14*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50},
 /*15*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50},
 /*16*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    16,     -1,    50},
 /*17*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50},
 /*18*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     18,    50},
 /*19*/ { -1,    -1,    -1,   -1,    -1,     -1,     -1,    -1,     -1,     -1,     -1,   -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,    -1,    -1,     -1,   -1,     -1,    -1,    -1,   -1,   -1,    -1,   -1,    -1,   -1,    -1,    -1,    -1,    -1,   -1,    -1,   -1,   -1,   -1,   -1,   -1,   -1,     0,     0,     -1,   -1,    -1,    -1,    -1,     -1,    50},
    };
    
    public analizadorPrincipal() 
    {
        initComponents();
        
        setIconImage(getIconImage());
                
        nl = new NumeroLinea(inText);
        inPadre.setRowHeaderView(nl);
        
        this.setLocationRelativeTo(null);
    }
    
    @Override
    public Image getIconImage()
    {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("imagenes/axolotl.png"));
        
        return retValue;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inPadre = new javax.swing.JScrollPane();
        inText = new javax.swing.JTextArea();
        nuevo = new javax.swing.JButton();
        saveAs = new javax.swing.JButton();
        open = new javax.swing.JButton();
        run = new javax.swing.JButton();
        outPadre = new javax.swing.JScrollPane();
        outText = new javax.swing.JTextArea();
        pachPadre = new javax.swing.JPanel();
        outPach = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtError = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        SintacticoText = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Axolotl Compilador");

        inText.setColumns(20);
        inText.setRows(5);
        inText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inTextKeyReleased(evt);
            }
        });
        inPadre.setViewportView(inText);

        nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/new.png"))); // NOI18N
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });

        saveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/save_as.png"))); // NOI18N
        saveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsActionPerformed(evt);
            }
        });

        open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/open.png"))); // NOI18N
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });

        run.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/run.png"))); // NOI18N
        run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runActionPerformed(evt);
            }
        });

        outText.setColumns(20);
        outText.setRows(5);
        outPadre.setViewportView(outText);

        pachPadre.setBackground(new java.awt.Color(175, 231, 226));

        javax.swing.GroupLayout pachPadreLayout = new javax.swing.GroupLayout(pachPadre);
        pachPadre.setLayout(pachPadreLayout);
        pachPadreLayout.setHorizontalGroup(
            pachPadreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pachPadreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outPach, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pachPadreLayout.setVerticalGroup(
            pachPadreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pachPadreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outPach, javax.swing.GroupLayout.DEFAULT_SIZE, 13, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtError.setBackground(new java.awt.Color(0, 0, 0));
        txtError.setColumns(20);
        txtError.setRows(5);
        jScrollPane2.setViewportView(txtError);

        SintacticoText.setColumns(20);
        SintacticoText.setRows(5);
        jScrollPane1.setViewportView(SintacticoText);

        jLabel1.setText("Lexico");

        jLabel2.setText("Pila");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pachPadre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(nuevo)
                .addGap(18, 18, 18)
                .addComponent(saveAs)
                .addGap(18, 18, 18)
                .addComponent(open)
                .addGap(18, 18, 18)
                .addComponent(run)
                .addGap(113, 113, 113)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                .addGap(59, 59, 59))
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inPadre, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(outPadre, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(saveAs)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(open)
                                    .addComponent(run))))))
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outPadre, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inPadre, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pachPadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
        if (inText.getText().isEmpty()) 
        {
            inText.setText("");
        } 
        else 
        {
            int dialogResult = JOptionPane.showConfirmDialog(null, "¿Desea guardar los cambios?", "Advertencia", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (dialogResult)
            {
                case JOptionPane.YES_OPTION:
                    JFileChooser obf = new JFileChooser();
                    FileNameExtensionFilter fil = new FileNameExtensionFilter("*.gaj", "gaj");

                    obf.setFileFilter(fil);

                    int val = obf.showSaveDialog(this);

                    if(val == JFileChooser.APPROVE_OPTION)
                    {
                        File chivo = obf.getSelectedFile();

                        outPach.setText(chivo.getPath());

                        FileWriter obe;

                        try 
                        {
                            obe = new FileWriter(chivo);
                            obe.write(this.inText.getText());

                            obe.close();
                        } 
                        catch (IOException ex) 
                        {
                            Logger.getLogger(analizadorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        }                
                    }
                    // Aquí va tu código para guardar los cambios
                    // Por ejemplo, podrías escribir el contenido de inText en un archivo
                    break;

                case JOptionPane.NO_OPTION:
                    inText.setText("");
                    break;

                case JOptionPane.CANCEL_OPTION:
                    // No hagas nada
                    break;
            }
        }
    }//GEN-LAST:event_nuevoActionPerformed

    private void saveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsActionPerformed
                
        JFileChooser obf = new JFileChooser();
        FileNameExtensionFilter fil = new FileNameExtensionFilter("*.gaj", "gaj");
        
        obf.setFileFilter(fil);
        
        int val = obf.showSaveDialog(this);
        
        if(val == JFileChooser.APPROVE_OPTION)
        {
            File chivo = obf.getSelectedFile();

            outPach.setText(chivo.getPath());

            FileWriter obe;

            try 
            {
                obe = new FileWriter(chivo);
                obe.write(this.inText.getText());

                obe.close();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(analizadorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }                
        }
    }//GEN-LAST:event_saveAsActionPerformed

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        
        inText.setText("");
        
        JFileChooser obf = new JFileChooser();
        FileNameExtensionFilter fil = new FileNameExtensionFilter("*.gaj", "gaj");
        
        obf.setFileFilter(fil);
        
        int val = obf.showOpenDialog(this);
        
        if(val == JFileChooser.APPROVE_OPTION)
        {
            File chivo = obf.getSelectedFile();
            
            outPach.setText(chivo.getPath());
            try 
            {
                BufferedReader obr = new BufferedReader(new FileReader(chivo));
                
                String line = obr.readLine();
                
                while(line != null)
                {
                    inText.append(line + "\n");
                    line = obr.readLine();
                }
            
            } catch (FileNotFoundException ex) {
                Logger.getLogger(analizadorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(analizadorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
              
        }
    }//GEN-LAST:event_openActionPerformed

    // Metodo para reiniciar variables
    public void reiniciarVariables()
    {
        pri = 1;
        cade = 1;
        nerr = 1;
        linea = 1;
        est = 0;
        concuerda = 0;
        res = "";
        err = "";
        txtError.setText("");
        SintacticoText.setText("");
        pilaSintactico.clear();
        aux.clear();
        pilaSintactico.add("$");
        pilaSintactico.add("prog");
    }
    
    // Accion para iniciar los analizadores
    private void runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runActionPerformed
                
        // Llamada del metodo para reiniciar variables
        this.reiniciarVariables();

        // La variable de cadena obtiene el texto del documento a analizar
        prueba = inText.getText();
        
        // Agrega el terminador de cadena
        prueba += "$";

        // Llama el metodo del analizador lexico
        this.automataLexer();
        
        // Después de analizar el texto lo pinta en la pantalla transformado en lexemas
        outText.setText(res);
        
        // De encontrar errores los pinta en otro cuadro
        txtError.setText(err);
        // Lo pinta de color rojo
        txtError.setForeground(Color.red);
    }//GEN-LAST:event_runActionPerformed

    private void inTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inTextKeyReleased
        // Llamada del metodo para reiniciar variables
//        this.reiniciarVariables();

        // La variable de cadena obtiene el texto del documento a analizar
//        prueba = inText.getText();
        
        // Agrega el terminador de cadena
//        prueba = prueba + "$";

        // Llama el metodo del analizador lexico
//        this.automataLexer();
        
        // Después de analizar el texto lo pinta en la pantalla transformado en lexemas
//        outText.setText(res);
        
        // De encontrar errores los pinta en otro cuadro
//        txtError.setText(err);
        // Lo pinta de color rojo
//        txtError.setForeground(Color.red);
    }//GEN-LAST:event_inTextKeyReleased
  
    // Analizador Lexico
    public void automataLexer()
    {
        // Ciclo for para validar toda la cadena
        for(int i = 0; i < prueba.length(); i++)
        {
            // Obtiene la posicion del alfabeto del caracter que reciba en ese momento
            pos = alf.indexOf(prueba.charAt(i));

            // valida si se encuentra en el alfabeto O no 
            if(pos == -1)
            {
                // si no la encuentra verifica que si esta entre " o ' de lo contrario es error
                this.caracter(i);
            }
            else
            {      
                // si esta en el alfabeto se mueve al estado en el que debe moverse
                est = tab[est][pos];
            }

            // En caso de que el estado sea de error genera un error lexico
            if(est == -1)
                this.errorLexico(i);
            
            // Encaso de que sea una palabra reserbada guarda los caracteres en una variable
            if(est == 5)
                if(cade == 1)
                {
                    temp = prueba.charAt(i) + "";
                    cade = 0;
                }
                else
                    temp += prueba.charAt(i);
            
            // valida si lo que sigue es un espacio o salto de linea o final de cadena
            if(recuperarErrorLexico != 1)
            {
                if(prueba.charAt(i) != ' ')
                    if(prueba.charAt(i) != '\n')
                        if(i + 1 < prueba.length())
                            if(alf.indexOf(prueba.charAt(i + 1)) != -1)
                                if(prueba.charAt(i + 1) == ' ' || prueba.charAt(i + 1) == '\n' || tab[est][alf.indexOf(prueba.charAt(i + 1))] == 50)
                                    // Comprobar que el estado sea un estado de aceptacion para generar el lexema
                                    this.comprobarEstado(i);
            }
            else
                if(prueba.charAt(i) == ' ' || prueba.charAt(i) == '\n')
                    recuperarErrorLexico = 0;
            
            if(est == 50 && i == (prueba.length() - 1))
                this.analizadorSintactico("$");
            // Contabiliza los renglones que pasan en la cadena
            this.saltoLinea(i); 
        }
    }
    
    // Metodo que genera los distintos lexemas
    public void estados(int i)
    {
        switch(est)
        {
            case 50: 
                if(i != prueba.length())
                    this.errorLexico(i);
            break;
                
            case 1:
                this.comprobarToken("num");
            break;
                
            case 3:
                this.comprobarToken("num");
            break;
                
            case 4:
                this.comprobarToken("num");
            break;
                
            case 5:
                this.tokenId();
            break;
                
            case 6:
                this.comprobarToken(prueba.charAt(i) + "");
            break;
                
            case 7:
                this.comprobarToken(prueba.charAt(i) + "");
            break;
                
            case 9:
                this.comprobarToken("litcad");
            break;
                
            case 12:
                this.comprobarToken("litcar");
            break;
                
            case 13:
                this.comprobarToken(prueba.charAt(i) + "");
            break;
                
            case 14:
                this.estadoCatorce(i);
            break;
                
            case 16:
                this.comprobarToken("Y");
            break;
                
            case 18:
                this.comprobarToken("O");
            break;
        }
    }
   
    public void estadoCatorce(int i)
    {
       if(i > 0)
       {
           if(prueba.charAt(i - 1) == '<')
                this.comprobarToken("#");
           
           if(prueba.charAt(i - 1) == '>')
                this.comprobarToken("?");
           
           if(prueba.charAt(i - 1) == '!')
                this.comprobarToken("@");
           
           if(prueba.charAt(i - 1) == '=')
                this.comprobarToken("¡");
       }
    }
  
    public void tokenId()
    {
        int x = 0;
        
        for (String reser1 : reser)
            if(temp.compareTo(reser1) == 0)
                x = 1;
         
        if(x == 1)
            this.comprobarToken(temp);
        else
            this.comprobarToken("id");
        temp = "";
        cade = 1;
    }
   
    public void errorLexico(int i)
    {
        if(nerr == 1)
        {
            err = "Error Lexico en linea: " + linea + " Caracter no valido " + prueba.charAt(i) + "\n";
            nerr = 0;
        }
        else
            err = err + "Error Lexico en linea: " + linea + " Caracter no valido " + prueba.charAt(i) + "\n";
        recuperarErrorLexico = 1;
        est = 0;
    }
    
    public void caracter(int i)
    {
        if(tab[est][alf.indexOf(prueba.charAt(i + 1))] != 8)
            this.errorLexico(i);
        else
            if(tab[est][alf.indexOf(prueba.charAt(i + 1))] != 10)
                this.errorLexico(i);
            else
                 if(tab[est][alf.indexOf(prueba.charAt(i + 1))] == 10)
                     est = 11;
    }
    
    public void comprobarToken(String token)
    {
        if(pri == 1)
        {
            res = token + " ";
            pri = 0;
        }
        else
            res += token + " ";
        
        this.analizadorSintactico(token);
    }
    
    public void saltoLinea(int i)
    {
        if(prueba.charAt(i) == '\n')
        {
            if(pri == 1)
            {
                res = "\n";
                pri = 0;
            }
            else
                res += "\n";
            linea++;
        }
    }
    
    public void comprobarEstado(int i)
    {
        if(est != 8)
        {
            for(int j = 0; j < acep.length; j++)
                if(est == acep[j])
                    ind = 1;

            if(ind == 0)
                this.errorLexico(i);
            else
                this.estados(i);
        }
        else
            if(i + 1 < prueba.length())
                if(alf.indexOf(prueba.charAt(i + 1)) != -1)
                    if(tab[est][alf.indexOf(prueba.charAt(i + 1))] == 50)
                        this.errorLexico(i);
        ind = 0;
    }
    
    public void analizadorSintactico(String token)
    {
        int x = 0;
        int y = 0;
        concuerda = 0;
        
        for(x = 0; x < terminales.length && !terminales[x].equals(token); x++);
        
        // ciclo que continua hasta que encuentra el lexema o un error dependiendo la accion
        while(concuerda != 1)
        {
            if(this.terminaPila(pilaSintactico.peek()) == 0)
            {
                    for(y = 0; y < noTerminales.length && !noTerminales[y].equals(pilaSintactico.peek()); y++);

                    if(sintactico[y][x].equals("0"))
                        pilaSintactico.pop();
                    else
                        if(this.verificarError(sintactico[y][x], token) != 1)
                            this.llenarPila(sintactico[y][x]);
            }
            else
                this.compararTerminales(token);
        }
    }
    
    public int verificarError(String accion, String token)
    {
        int var = 0;
        
        if(accion.equals("saltar"))
        {
            var = 1;
            concuerda = 1;
            this.mensajeError(token);
        }
        
        if(accion.equals("sacar"))
        {
            var = 1;
            pilaSintactico.pop();
            this.mensajeError(token);
        }
        
        return var;
    }
    
    public void mensajeError(String token)
    {
        
        switch(pilaSintactico.peek())
        {
            case "prog":
                this.errorSintactico(" Se esperaba el inicio de programa");
            break;
            case "dec":
                this.errorSintactico(" Se esperaba una declaracion");
            break;
            case "dec2":
                this.errorSintactico(" Se esperaba el siguiente identificador");
            break;
            case "sigdec":
                this.errorSintactico(" Se esperaba el siguiente identificador");
            break;
            case "modulo":
                this.errorSintactico(" Se esperaba una funcion o un procedimiento");
            break;
            case "list_arg":
                this.errorSintactico(" Se esperaba uno o mas argumentos");
            break;
            case "sigarg":
                this.errorSintactico(" Se esperaba una asignacion");
            break;
            case "bloque":
                this.errorSintactico(" Se esperaba sentencias entrea llaves }");
            break;
            case "sentencias":
                this.errorSintactico(" Se esperaban sentencia o asignaciones");
            break;
            case "sigsi":
                this.errorSintactico(" Se esperaba la continuacion del \"si\"");
            break;
        }
        
        if(pilaSintactico.peek().equals("L") || pilaSintactico.peek().equals("R") || pilaSintactico.peek().equals("E") || pilaSintactico.peek().equals("T") || pilaSintactico.peek().equals("F"))
            this.errorSintactico(" Se esperaba un operando");
        
        if(pilaSintactico.peek().equals("L'") || pilaSintactico.peek().equals("R'") || pilaSintactico.peek().equals("E'") || pilaSintactico.peek().equals("T'") || pilaSintactico.peek().equals("F'"))
            this.errorSintactico(" Se esperaba un operador");
        
        if(!pilaSintactico.peek().equals("prog") && token.equals("InicioProgra"))
            this.errorSintactico(" No se puede comensar otra vez el programa");
    }
    
    public int terminaPila(String topePila)
    {
        
        SintacticoText.append(String.valueOf(pilaSintactico) + "\n");
        System.out.println(String.valueOf(pilaSintactico));
        int var = 0;
        for(int x = 0; x < terminales.length; x++)
            if(terminales[x].equals(topePila))
                var = 1;
        return var;
    }
    
    public void llenarPila(String accion)
    {
        
        String temporal = "";
        pilaSintactico.pop();
        
        for(int x = 0; x < accion.length(); x++)
            if(accion.charAt(x) != ' ')
                temporal += accion.charAt(x);
            else
            {
                aux.add(temporal);
                temporal = "";
            }
        
        aux.add(temporal);
        temporal = "";
        
        int pila = aux.size();
        for(int i = 0; i < pila; i++)
            pilaSintactico.push(aux.pop());
        
    }
    
    public void compararTerminales(String entrada)
    {
        if(pilaSintactico.peek().equals(entrada))
        {
            pilaSintactico.pop();
            concuerda = 1;
        }
        else
            if(!pilaSintactico.peek().equals("$"))
            {
                this.errorSintactico(" Se esperaba " + pilaSintactico.peek());
                pilaSintactico.pop();
            }
            else
            {
                this.errorSintactico(" Se esperaba " + pilaSintactico.peek());
                concuerda = 1;
            }
                
   }
    
    public void errorSintactico(String tipoError)
    {
        if(nerr == 1)
        {
            err = "Error sintactico en la linea " + linea + ": "+ tipoError + "\n";
            nerr = 0;
        }
        else
            err += "Error sintactico en la linea " + linea + ": " + tipoError + "\n";
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(analizadorPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(analizadorPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(analizadorPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(analizadorPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new analizadorPrincipal().setVisible(true);
                
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(analizadorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(analizadorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(analizadorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(analizadorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea SintacticoText;
    private javax.swing.JScrollPane inPadre;
    private javax.swing.JTextArea inText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton nuevo;
    private javax.swing.JButton open;
    private javax.swing.JLabel outPach;
    private javax.swing.JScrollPane outPadre;
    private javax.swing.JTextArea outText;
    private javax.swing.JPanel pachPadre;
    private javax.swing.JButton run;
    private javax.swing.JButton saveAs;
    private javax.swing.JTextArea txtError;
    // End of variables declaration//GEN-END:variables

}

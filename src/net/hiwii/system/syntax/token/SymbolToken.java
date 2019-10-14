package net.hiwii.system.syntax.token;

import net.hiwii.reg.RegularExpression;
/**
 *SEPARATORS
 *TOKEN :
  < LPAREN: "(" >
| < RPAREN: ")" >
| < LBRACE: "{" >
| < RBRACE: "}" >
| < LBRACKET: "[" >
| < RBRACKET: "]" >
| < SEMICOLON: ";" >
| < COMMA: "," >
| < DOT: "." >
| < AT: "@" >
| < DOLLAR: "$" >
| < POUND: "#" >
}

OPERATORS

TOKEN :
{
  < ASSIGN: "=" >
| < LT: "<" >
| < BANG: "!" >
| < TILDE: "~" >
| < HOOK: "?" >
| < COLON: ":" >
| < PLUS: "+" >
| < MINUS: "-" >
| < STAR: "*" >
| < SLASH: "/" >
| < BACKSLASH: "\\" >
| < BIT_AND: "&" >
| < BIT_OR: "|" >
| < XOR: "^" >
| < REM: "%" >
| < GT: ">" >
| < EQ: "==" >
| < LE: "<=" >
| < GE: ">=" >
| < NE: "!=" >
| < SC_OR: "||" >
| < SC_AND: "&&" >
| < LSHIFT: "<<" >
| < RSHIFT: ">>" >
| < CONTAINSET: "+>" >
| < BEGLONGSET: "<+" >
| < NOTCONTAINBEGLOG: "<>" >
| < CONTAIN: "->" >
| < BEGLONG: "<-" >
| < NOTBEGLONG: "<!" >
| < NOTCONTAIN: "!>" >
| < PLUSASSIGN: "+=" >
| < MINUSASSIGN: "-=" >
| < NOTIDENTITY: "^=" >
| < ISCLASS: "%=" >
| < ISNOTCLASS: "~=" >
| < ELLIPSIS: "..." >
 * @author Administrator
 *
 */
public class SymbolToken extends RegularExpression {

}

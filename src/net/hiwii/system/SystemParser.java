package net.hiwii.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.AssignmentExpression;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.BracketExpression;
import net.hiwii.expr.CharExpression;
import net.hiwii.expr.DotFormatExpression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.FunctionMapping;
import net.hiwii.expr.FunctionMappingBrace;
import net.hiwii.expr.IdentifierBracket;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.expr.NullExpression;
import net.hiwii.expr.NumberUnit;
import net.hiwii.expr.ParenExpression;
import net.hiwii.expr.Parentheses;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.expr.SubjectOperation;
import net.hiwii.expr.SubjectStatus;
import net.hiwii.expr.SubjectVerb;
import net.hiwii.expr.SubjectVerbAtom;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.expr.adv.FunctionBrace;
import net.hiwii.expr.adv.IdentifierBrace;
import net.hiwii.expr.adv.MappingBrace;
import net.hiwii.expr.sent.ConditionExpression;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.parser.JavaCharStream;
import net.hiwii.system.parser.MyToken;
import net.hiwii.system.parser.ParseException;
import net.hiwii.system.parser.ScriptParserConstants;
import net.hiwii.system.parser.ScriptParserTokenManager;
import net.hiwii.system.parser.Token;
import net.hiwii.system.parser.TokenMgrError;
import net.hiwii.system.runtime.OperationMap;
import net.hiwii.system.runtime.OrderArgument;
import net.hiwii.system.runtime.VariablePosition;
import net.hiwii.system.syntax.bin.BinaryFormat;
import net.hiwii.system.syntax.bin.HexFormat;
import net.hiwii.system.syntax.bin.OctalFormat;
import net.hiwii.system.syntax.logic.JudgeExpression;
import net.hiwii.system.syntax.logic.JudgeOperators;
import net.hiwii.system.syntax.logic.LogicExpression;
import net.hiwii.system.syntax.logic.NotExpression;
import net.hiwii.system.syntax.number.DecimalNumber;
import net.hiwii.system.syntax.number.FractionNumber;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.RealNumber;
import net.hiwii.system.syntax.number.ScientificNotation;
import net.hiwii.system.syntax.sent.ExpressSentence;
import net.hiwii.system.syntax.sent.SetSentence;
import net.hiwii.system.util.StringUtil;

public class SystemParser implements ScriptParserConstants{

	/** Generated Token Manager. */
	public ScriptParserTokenManager token_source;
	JavaCharStream jj_input_stream;
	/** Current token. */
	public Token token;
	/** Next token. */
	public Token jj_nt;
	private int jj_ntk;
	
//	private RuntimePool pool;

//	public SystemParser(java.io.InputStream stream, RuntimePool pool) {
//		this(stream, pool, null);
//		this.pool = pool;
//	}
//	
//	public SystemParser(java.io.InputStream stream, String encoding, RuntimePool pool) {
//		
//	}
	/** Constructor with InputStream. */
	public SystemParser(java.io.InputStream stream) {
		this(stream, null);
	}
	/** Constructor with InputStream and supplied encoding */
	public SystemParser(java.io.InputStream stream, String encoding) {
		try {
			jj_input_stream = new JavaCharStream(stream, encoding, 1, 1);
		} catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
		token_source = new ScriptParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
//		this.pool = pool;s
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream stream) {
		ReInit(stream, null);
	}
	/** Reinitialise. */
	public void ReInit(java.io.InputStream stream, String encoding) {
		try { 
		jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
	}

	/** Constructor. */
	public SystemParser(java.io.Reader stream) {
		jj_input_stream = new JavaCharStream(stream, 1, 1);
		token_source = new ScriptParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
	}

	/** Constructor with generated Token Manager. */
	public SystemParser(ScriptParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
	}

	/** Reinitialise. */
	public void ReInit(ScriptParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
	}

	/*****************************************
	 * THE JAVA LANGUAGE GRAMMAR STARTS HERE 
	 * @throws ApplicationException *
	 *****************************************/
	public BraceExpression getProgram() throws ApplicationException{
		MyToken begin, end = null;
		begin = (MyToken) token_source.getNextToken();//first token
		return getProgram(begin, end);
	}
	
	public Expression getExpression() throws ApplicationException{
		MyToken begin, end = null;
		begin = (MyToken) token_source.getNextToken();//first token
		end = begin;
		MyToken a = fetchNext(begin);
		//TODO 等待优化，把寻找endToken放在matchToken中
		while(true){
			if(a.kind == 0 || a == null){
				break;
			}
			end = a;
			a = fetchNext(end);
		}
		return getExpression(begin, end);
	}
	
	public Expression getJudgment() throws ApplicationException{
		MyToken begin, end = null;
		begin = (MyToken) token_source.getNextToken();//first token
		end = begin;
		MyToken a = fetchNext(begin);
		while(true){
			if(a.kind == 0 || a == null){
				break;
			}
			end = a;
			a = fetchNext(end);
		}
		return getLogic(begin, end);
	}
	
	/**
	 * {block}允许最后一句没有";"
	 * @param firstToken
	 * @param lastToken
	 * @return
	 * @throws ApplicationException
	 */
	public List<Expression> getSentenceList(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken a;
		MyToken begin, end;
		begin = firstToken;
		List<Expression> list = new ArrayList<Expression>();
		while(true){
			if(begin.kind == 0 || begin == null){
				break;
			}
			a = matchToken(begin, lastToken, COMMA);
			if(a != null){
				end = fetchPrevious(a);
				Expression item = getExpression(begin, end);
				list.add(item);
			}else{
				end = lastToken;
				Expression item = getExpression(begin, end);
				list.add(item);
				break;
			}
			if(a == lastToken){
				break;
			}else{
				begin = fetchNext(a);
			}
		}
		return list;
	}
	
	

	
	public Expression getLogic(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken sep = matchLogicToken(firstToken, lastToken, 'l');
		if(sep != null){
			if(sep == firstToken || sep == lastToken){
				throw new ApplicationException("err");
			}
			Expression left = getJudgment(firstToken, fetchPrevious(sep));
			LogicExpression le = new LogicExpression();
			le.setOperator(sep.image);
			le.setLeft(left);
			assertEnd(sep, lastToken);
			Expression right = getLogic(fetchNext(sep), lastToken);
			le.setRight(right);
			return le;
		}else{
			return getJudgment(firstToken, lastToken);
		}
	}
	
	public Expression getJudgment(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		//judgment是not 或 判断表达式。
		if(firstToken.kind == BANG){
			//一般情况下，！后要求必须跟(),内部包含判断表达式。！表示判断的非。
			if(firstToken == lastToken){
				throw new ApplicationException("err");
			}
			MyToken a = fetchNext(firstToken);
			if(a.kind != LPAREN || lastToken.kind != RPAREN){//本句有问题，没有考虑lastToken=null情况
				throw new ApplicationException("err");
			}else{
				MyToken m = matchToken(fetchNext(a), lastToken, RPAREN);
				if(m == null || m != lastToken){
					throw new ApplicationException("err");
				}
			}
			if(fetchNext(a) == lastToken){
				throw new ApplicationException("err");
			}
			Expression exp = getLogic(fetchNext(a), fetchPrevious(lastToken));
			return new NotExpression(exp);
		}else{
			//判断表达式使用operator或?格式
			//?格式：expr1 ?match& expr2
			MyToken que = matchToken(firstToken, lastToken, HOOK);
			if(que != null){
				if(que == firstToken || que == lastToken){
					throw new ApplicationException("err");
				}
				JudgeExpression je = new JudgeExpression();
				Expression left = getExpression(firstToken, fetchPrevious(que));
				MyToken a = fetchNext(que);
				if(a.kind == IDENTIFIER){
					je.setOperator(a.image);//当前只做expr1 ?match& expr2解析，judge动词为函数/没有宾语情况TODO
				}else{
					throw new ApplicationException("err");
				}
				if(a == lastToken){
					throw new ApplicationException("err");
				}
				MyToken b = fetchNext(a);
				assertEnd(b, lastToken);
				if(b.kind != BIT_AND){
					throw new ApplicationException("err");
				}
				Expression right = getExpression(fetchNext(b), lastToken);
				je.setLeft(left);
				je.setRight(right);
				return je;
			}
			MyToken sep = matchLogicToken(firstToken, lastToken, 'j');
			if(sep != null){
				if(sep == firstToken || sep == lastToken){
					throw new ApplicationException("err");
				}
				Expression left = getExpression(firstToken, fetchPrevious(sep));
				Expression right = getExpression(fetchNext(sep), lastToken);
				BinaryOperation je = new BinaryOperation();
				je.setOperator(sep.image);
				je.setLeft(left);
				je.setRight(right);
				return je;
			}else{
				Expression exp = getExpression(firstToken, lastToken);
				return exp;
			}
		}
	}
	
	public BraceExpression getProgram(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		BraceExpression ret = new BraceExpression();
		List<Expression> list = getSentenceList(firstToken, lastToken);

		ret.setArray(list);
		return ret;
	}
	
	public boolean isStringCognition() throws TokenMgrError{
		MyToken a = (MyToken) token_source.getNextToken();//first token
		if(a.kind == STRING_LITERAL){
			MyToken b = fetchNext(a);
			if(b.kind == 0 || b == null){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public StringExpression getStringExpression() throws TokenMgrError{
		MyToken begin, end = null;
		begin = (MyToken) token_source.getNextToken();//first token
		return getStringExpression(begin, end);
	}
	
	
	public StringExpression getStringExpression(MyToken firstToken, MyToken lastToken){
		String str = firstToken.image;
		str = str.substring(1, str.length() - 1);//前提：value表达式为"..."
		return new StringExpression(str);
	}
	
	public CharExpression getCharExpression(MyToken firstToken, MyToken lastToken){
		String str = firstToken.image;
		char ch = str.charAt(1);//前提，表达式必须为\'char\'
		return new CharExpression(ch);
	}
	public boolean isIdentifier() throws ApplicationException {
		MyToken a = (MyToken) token_source.getNextToken();//first token
		if(a.kind == IDENTIFIER){
			MyToken b = fetchNext(a);
			if(b.kind == 0 || b == null){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean isIdentifier(MyToken firstToken, MyToken lastToken){
		if(firstToken.kind != IDENTIFIER){
			return false;
		}else{
			if(firstToken == lastToken){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public boolean isIdentifySentence(MyToken firstToken, MyToken lastToken){
		if(firstToken.kind == IDENTIFIER && firstToken.image.equals("identify")){
			return true;
		}else{
			return false;
		}
	}

	public boolean isBlock(MyToken firstToken, MyToken lastToken){
		if(firstToken.kind == LBRACE){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isObjectAction(MyToken firstToken, MyToken lastToken){
		try {
			MyToken a = matchToken(firstToken, lastToken, POUND);
			if(a == null){
				return false;
			}else{
				MyToken b = matchToken(firstToken, lastToken, AT);
				if(b == null){
					return true;
				}else{
					return false;
				}
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isAssignSentence(MyToken firstToken, MyToken lastToken){
		if(firstToken.kind == IDENTIFIER && firstToken.image.equals("assign")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDeclareSentence(MyToken firstToken, MyToken lastToken){
		if(firstToken.kind == IDENTIFIER && firstToken.image.equals("Declare")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isAssignmentExpression(MyToken firstToken, MyToken lastToken){
		try {
			MyToken a = matchToken(firstToken, lastToken, ASSIGN);
			if(a == firstToken){
				return false;
			}else{
				MyToken b = fetchPrevious(a);
				if(!isIdentifier(firstToken, b) && !isNameIndexExpression(firstToken, b)){
					return false;
				}
			}
			if(a != null && a != lastToken){
				return true;
			}else{
				return false;
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isSortExpression(MyToken firstToken, MyToken lastToken){
		if(firstToken.kind == IDENTIFIER && firstToken.image.equals("sort")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDoesStatement(MyToken firstToken, MyToken lastToken){
		if(firstToken.kind == IDENTIFIER && firstToken.image.equals("does")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isEchoExpression(MyToken firstToken, MyToken lastToken){
		if(firstToken.kind == IDENTIFIER && firstToken.image.equals("echo")){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * AbstractionFormat:name(expression list);
	 * @throws ApplicationException 
	 */
	public boolean isFunctionExpression(MyToken firstToken, MyToken lastToken){
		try {
			MyToken a = firstToken;//first token
			if(a.kind == IDENTIFIER ){
				MyToken b = fetchNext(a);
				if(b.kind != LPAREN){
					return false;
				}else{
					if(a.image.equals("Range") || a.image.equals("if") || a.image.equals("while")
							|| a.image.equals("for")){
						return false;
					}
					MyToken c = matchToken(fetchNext(b), lastToken, RPAREN);
					if(lastToken == c){
						return true;
					}else{
						return false;
					}
				}
			}else{
				return false;
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isNameIndexExpression(MyToken firstToken, MyToken lastToken){
		try {
			MyToken a = firstToken;//first token
			if(a.kind == IDENTIFIER){
				MyToken b = fetchNext(a);
				if(b.kind != LBRACKET){
					return false;
				}else{
					MyToken c = matchToken(b, lastToken, RBRACKET);
					if(lastToken == c){
						return true;
					}else{
						return false;
					}
				}
			}else{
				return false;
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isNotExpression(MyToken firstToken, MyToken lastToken) throws TokenMgrError{
		if(firstToken == lastToken){
			return false;
		}
		if(firstToken.kind == BANG){
			MyToken a = fetchNext(firstToken);
			if(a == lastToken && a.kind == IDENTIFIER){
				return true;
			}else if(isFunctionExpression(a, lastToken)){
				return true;
			}else if(isNameIndexExpression(a, lastToken)){
				return true;
			}else if(a.kind == LPAREN){
				if(a == lastToken){
					return false;
				}
				try {
					MyToken b = matchToken(fetchNext(a), lastToken, RPAREN);
					if(b != null && b == lastToken){
						return true;
					}else{
						return false;
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	public boolean isDotFormatExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken a = matchToken(firstToken, lastToken, DOT);
		if(a != null){
			if(a != firstToken && a != lastToken){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean isSetStatement(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		if(firstToken.image.equals("set")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isDoSentence(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		if(firstToken.image.equals("do")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isRangeExpression(MyToken begin, MyToken end) throws ApplicationException {
		if(begin.kind == IDENTIFIER && begin.image.equals("Range")){
			assertEnd(begin, end);
			MyToken a = fetchNext(begin);
			if(a.kind != LPAREN && a.kind != LBRACKET){
				return false;
			}
			assertEnd(a, end);
			MyToken b = matchToken(fetchNext(a), end, COMMA);
			if(b == null || fetchNext(a) == b){
				return false;
			}
			assertEnd(b, end);
			a = fetchNext(b);
			assertEnd(a, end);//a is not end token.
			b = matchLogicToken(a, end, 'r');
			if(b == null){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * List= [Cognition|Object|Action|Operation]{}
	 * @param begin
	 * @param end
	 * @return
	 * @throws ApplicationException
	 */
	public boolean isListExpression(MyToken begin, MyToken end) throws ApplicationException {
		if(begin.kind == IDENTIFIER && (begin.image).equals("Cognition") ||
				begin.image.equals("Object") ||
				begin.image.equals("Action") ||
				begin.image.equals("Operation")){
			assertEnd(begin, end);
			MyToken a = fetchNext(begin);
			if(a.kind == LBRACE){
				assertEnd(a, end);
				MyToken b = matchToken(fetchNext(a), end, LBRACE);
				if(b != null){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public DotFormatExpression getDotFormatExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		DotFormatExpression dfe = new DotFormatExpression();
		MyToken a;
		MyToken begin, end;
		
		begin = firstToken;
		List<Expression> list = new ArrayList<Expression>();
		while(true){
			if(begin.kind == 0 || begin == null){
				break;
			}
			a = matchToken(begin, lastToken, DOT);
			if(a != null){
				end = fetchPrevious(a);
				Expression item = getExpression(begin, end);
				list.add(item);
				
				begin = fetchNext(a);
			}else{
				Expression item = getExpression(begin, lastToken);
				list.add(item);
				break;
			}
		}
		dfe.setExpressions(list);
		return dfe;
	}
	
	public FunctionExpression getFunctionExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		FunctionExpression expr = new FunctionExpression();
		expr.setName(firstToken.image);
		assertEnd(firstToken, lastToken);
		MyToken a = fetchNext(firstToken);//skip (
		assertEnd(a, lastToken);
		a = fetchNext(a);
		if( a == lastToken){
			List<Expression> list = new ArrayList<Expression>();
			list.add(new NullExpression());
			expr.setArguments(list);
		}else{
			MyToken b = fetchPrevious(lastToken);//skip )
			List<Expression> list = getArguments(a, b);
			expr.setArguments(list);
		}
		return expr;
	}
	
	public IdentifierBracket getIdentifierBracket(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		IdentifierBracket expr = new IdentifierBracket();
		expr.setName(firstToken.image);
		assertEnd(firstToken, lastToken);
		MyToken a = fetchNext(firstToken);//skip (
		assertEnd(a, lastToken);
		a = fetchNext(a);
		if( a == lastToken){
			List<Expression> list = new ArrayList<Expression>();
			list.add(new NullExpression());
			expr.setArguments(list);
		}else{
			MyToken b = fetchPrevious(lastToken);//skip )
			List<Expression> list = getArguments(a, b);
			expr.setArguments(list);
		}
		return expr;
	}
	
	public MappingExpression getMappingExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MappingExpression expr = new MappingExpression();
		expr.setName(firstToken.image);
		assertEnd(firstToken, lastToken);
		MyToken a = fetchNext(firstToken);//skip (
		assertEnd(a, lastToken);
		a = fetchNext(a);
		if( a == lastToken){
			List<Expression> list = new ArrayList<Expression>();
			list.add(new NullExpression());
			expr.setArguments(list);
		}else{
			MyToken b = fetchPrevious(lastToken);//skip )
			List<Expression> list = getArguments(a, b);
			expr.setArguments(list);
		}
		return expr;
	}
	
	public ConditionExpression getIdentifierBrace(MyToken firstToken, MyToken lastToken) throws ApplicationException{
//		ConditionExpression expr = new ConditionExpression();
		IdentifierBrace ib = new IdentifierBrace();
		IdentifierExpression ie = new IdentifierExpression(firstToken.image);
		ib.setName(ie.getName());
		assertEnd(firstToken, lastToken);
		MyToken a = fetchNext(firstToken);//skip {
		assertEnd(a, lastToken);
		a = fetchNext(a);
		if( a == lastToken){
			List<Expression> list = new ArrayList<Expression>();
			list.add(new NullExpression());
			ib.setConditions(list);
		}else{
			MyToken b = fetchPrevious(lastToken);//skip }
			List<Expression> list = getSentenceList(a, b);
			ib.setConditions(list);
		}
		return ib;
	}
	
	public ConditionExpression getFunctionBrace(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		ConditionExpression expr = new ConditionExpression();
		FunctionExpression fe = new FunctionExpression();
		fe.setName(firstToken.image);
		MyToken a = fetchNext(firstToken);//skip (
		a = fetchNext(a);
		MyToken b = matchToken(a, lastToken, RPAREN);
		if( a == b){
			List<Expression> list = new ArrayList<Expression>();
			list.add(new NullExpression());
			fe.setArguments(list);
		}else{
			MyToken e = fetchPrevious(b);//skip )
			List<Expression> list = getArguments(a, e);
			fe.setArguments(list);
		}
		expr.setBody(fe);
		MyToken c = fetchNext(b);
		c = fetchNext(c); //skip {
		if( c == lastToken){
			List<Expression> list = new ArrayList<Expression>();
			list.add(new NullExpression());
			expr.setConditions(list);
		}else{
			MyToken e = fetchPrevious(lastToken);//skip }
			List<Expression> list = getSentenceList(c, e);
			expr.setConditions(list);
		}
		return expr;
	}
	
	public Expression getEntityDescription(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		return null;
	}
	
	public boolean isSymbol(MyToken begin){
		if(SystemOperators.isOperator(begin.image)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 返回active #verb type(#,".",$,!)
	 * id/function
	 * unary/binary
	 * @param firstToken
	 * @param lastToken
	 * @return
	 * @throws ApplicationException
	 */
	public Expression getOperationExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken begin, end;
		
		begin = firstToken;
		end = begin;		
		
		ParsedExpression pe = getParsedExpression(firstToken, lastToken);
		Expression formula = pe.getExpression();
		end = pe.getEnd();
		if(end == lastToken){
			return formula;
		}
		begin = fetchNext(end);
		while(true){
			if(begin.kind == 0 || begin == null){
				break;
			}
			if(isSymbol(begin)){
				String op = begin.image;
				if(begin == lastToken){
					break;
				}
				begin = fetchNext(begin);
				ParsedExpression pe1 = getParsedExpression(begin, lastToken);
				Expression operand = pe1.getExpression();
				if(formula instanceof BinaryOperation){
					BinaryOperation bo = (BinaryOperation) formula;
					if(SystemOperators.compare(bo.getOperator(), op) >= 0){
						BinaryOperation tmp = new BinaryOperation();
						tmp.setLeft(formula);
						tmp.setOperator(op);
						tmp.setRight(operand);
						formula = tmp;
					}else{
						//if当前操作符优先级高于原binary operator，不改变原formula，只改变右侧元素。
						BinaryOperation top = bo;
						while(true){
							Expression right = top.getRight();
							if(right instanceof BinaryOperation){
								BinaryOperation rnode = (BinaryOperation)right;
								if(SystemOperators.compare(rnode.getOperator(), op) >= 0){
									BinaryOperation tmp = new BinaryOperation();
									tmp.setLeft(right);
									tmp.setOperator(op);
									tmp.setRight(operand);
									top.setRight(tmp);
									break;
								}else{
									top = (BinaryOperation) right;
									continue;
								}
							}else{
								BinaryOperation node = new BinaryOperation();
								node.setLeft(top.getRight());
								node.setOperator(op);
								node.setRight(operand);
								top.setRight(node);
								break;
							}
						}
					}
				}else{
					Expression left = formula;
					BinaryOperation bin = new BinaryOperation();
					bin.setLeft(left);
					bin.setRight(operand);
					bin.setOperator(op);
					formula = bin;
				}
				end = pe1.getEnd();
			}else if(begin.kind == POUND){
				if(begin == lastToken){
					throw new ApplicationException("err");
				}
				ParsedExpression pe0 = getSufix(fetchNext(begin), lastToken);
				end = pe0.getEnd();
				SubjectVerb av = new SubjectVerb();//ActiveVerb表示object.operation
				av.setSubject(formula);
				av.setAction(pe0.getExpression());
				//20160402 added
				if(end != lastToken){
					MyToken next = fetchNext(end);					
					if(next.kind == LBRACE){
						ConditionExpression ce = new ConditionExpression();
						ce.setBody(av);
						MyToken d = matchToken(fetchNext(next), lastToken, RBRACE);
						if(d == null){
							throw new ApplicationException("err");
						}
						BraceExpression prg = getBlock(next, d);
						ce.setConditions(prg.getArray());
						formula = ce;
						end = d;
					}
				}else{
					formula = av;
				}
			}else if(begin.kind == DOLLAR){//! positive
				if(begin == lastToken){
					throw new ApplicationException("err");
				}
				ParsedExpression pe0 = getSufix(fetchNext(begin), lastToken);
				end = pe0.getEnd();
				SubjectStatus sp = new SubjectStatus();//ActiveVerb表示object.operation
				sp.setSubject(formula);
				sp.setAction(pe0.getExpression());
				sp.setRight(true);
				//20160402 added
				if(end != lastToken){
					MyToken next = fetchNext(end);					
					if(next.kind == LBRACE){
						ConditionExpression ce = new ConditionExpression();
						ce.setBody(sp);
						MyToken d = matchToken(fetchNext(next), lastToken, RBRACE);
						if(d == null){
							throw new ApplicationException("err");
						}
						BraceExpression prg = getBlock(next, d);
						ce.setConditions(prg.getArray());
						formula = ce;
						end = d;
					}
				}else{
					formula = sp;
				}
			}else if(begin.kind == BANG){
				//$!用于连接否定的主语和状态谓语。$!与!不同，!是逻辑运算符
				if(begin == lastToken){
					throw new ApplicationException("err");
				}
				ParsedExpression pe0 = getSufix(fetchNext(begin), lastToken);
				end = pe0.getEnd();
				SubjectStatus sp = new SubjectStatus();//ActiveVerb表示object.operation
				sp.setSubject(formula);
				sp.setAction(pe0.getExpression());
				sp.setRight(true);
				//20160402 added
				if(end != lastToken){
					MyToken next = fetchNext(end);					
					if(next.kind == LBRACE){
						ConditionExpression ce = new ConditionExpression();
						ce.setBody(sp);
						MyToken d = matchToken(fetchNext(next), lastToken, RBRACE);
						if(d == null){
							throw new ApplicationException("err");
						}
						BraceExpression prg = getBlock(next, d);
						ce.setConditions(prg.getArray());
						formula = ce;
						end = d;
					}
				}else{
					formula = sp;
				}
			}else if(begin.kind == INNERRUN){
				if(begin == lastToken){
					throw new ApplicationException("err");
				}
				ParsedExpression pe0 = getSufix(fetchNext(begin), lastToken);
				end = pe0.getEnd();
				SubjectVerbAtom av = new SubjectVerbAtom();//ActiveVerb表示object.operation
				av.setSubject(formula);
				av.setAction(pe0.getExpression());
				if(end != lastToken){
					MyToken next = fetchNext(end);					
					if(next.kind == LBRACE){
						ConditionExpression ce = new ConditionExpression();
						ce.setBody(av);
						MyToken d = matchToken(fetchNext(next), lastToken, RBRACE);
						if(d == null){
							throw new ApplicationException("err");
						}
						BraceExpression prg = getBlock(next, d);
						ce.setConditions(prg.getArray());
						formula = ce;
						end = d;
					}
				}else{
					formula = av;
				}
			}else if(begin.kind == NOTRUN){
//				if(begin == lastToken){
//					throw new ApplicationException("err");
//				}
//				ParsedExpression pe0 = getSufix(fetchNext(begin), lastToken);
//				end = pe0.getEnd();
//				ActiveStopVerb av = new ActiveStopVerb();//ActiveVerb表示object.operation
//				av.setActive(formula);
//				av.setVerb(pe0.getExpression());
//				formula = av;
			}else{
				throw new ApplicationException("err");
			}
			
			if(end == lastToken){
				break;
			}else{
				begin = fetchNext(end);
			}
		}
		return formula;
	}
	
	/**
	 * Number/String/Char
	 * Identifier/Function
	 * subject.operation/subjectPositive/subjectNegative/activeVerb
	 * 数字是最终属性，不能有其它属性
	 * "."优先于symbol
	 * "$/!/#"低于symbol
	 * "="表示等于，$表示 is
	 * 如：-3$LT(-2);-3!equals(2);-a.b
	 * @param firstToken
	 * @param lastToken
	 * @return
	 * @throws ApplicationException
	 */
	public ParsedExpression getParsedExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken begin, end = null;
		Expression ret = null;
		
		begin = firstToken;
		if(begin.kind == INTEGER_LITERAL || begin.kind == FLOATING_POINT_LITERAL ||
				begin.kind == FRACTION_LITERAL || begin.kind == SCIENTIFIC_NOTATION){
			RealNumber ne = null;
			if(begin.kind == INTEGER_LITERAL){
				ne = new IntegerNumber(begin.image);				
			}else if(begin.kind == FLOATING_POINT_LITERAL){
				ne = new DecimalNumber(begin.image);
//				end = begin;
			}else if(begin.kind == FRACTION_LITERAL){
				ne = new FractionNumber(StringUtil.getFractionUp(begin.image),
						StringUtil.getFractionLow(firstToken.image));
//				end = begin;
			}else if(begin.kind == SCIENTIFIC_NOTATION){
				ne = new ScientificNotation(StringUtil.getScientificValue(begin.image),
						StringUtil.getScientificPower(firstToken.image));
//				end = begin;
			}
			if(begin == lastToken){
				end = begin;
				ret = ne;
			}else{
				Expression unit = null;
				MyToken a = fetchNext(begin);
				if(a.kind == IDENTIFIER){
					if(a == lastToken){
						unit = new IdentifierExpression(a.image);
						NumberUnit nu = new NumberUnit();
						nu.setNumber(ne);
						nu.setUnit(unit);
						ret = nu;
						end = a;
					}else{
						MyToken lb = fetchNext(a);
						if(lb == lastToken){
							throw new ApplicationException("err");
						}
						MyToken b = matchToken(fetchNext(lb), lastToken, RPAREN);
						if(b == null){
							throw new ApplicationException("err");
						}
						unit = getFunctionExpression(a, b);
						NumberUnit nu = new NumberUnit();
						nu.setNumber(ne);
						nu.setUnit(unit);
						ret = nu;
						end = b;
					}
				}else if(a.kind == LPAREN){
					if(a == lastToken){
						throw new ApplicationException("err");
					}
					MyToken b = matchToken(fetchNext(a), lastToken, RPAREN);
					if(b == null){
						throw new ApplicationException("err");
					}
					Expression pe = null;
					if(fetchNext(a) == b){
						//()参数为空
						ParenExpression p0 = new ParenExpression();
						p0.setExpression(new NullExpression());
						pe = p0;
					}
//					ParenExpression pe = new ParenExpression();
//					pe.setExpression(getExpression(fetchNext(a), fetchPrevious(b)));
					
					pe = getParentheses(fetchNext(a), fetchPrevious(b));
					
					end = b;
					NumberUnit nu = new NumberUnit();
					nu.setNumber(ne);
					nu.setUnit(pe);
					ret = nu;
				}else{
					ret = ne;
					end = begin;
				}
			}
		}else if(begin.kind == HEX_LITERAL){
			ret = new HexFormat(begin.image);
			end = begin;
		}else if(begin.kind == OCTAL_LITERAL){
			ret = new OctalFormat(begin.image);
			end = begin;
		}else if(begin.kind == BINARY_LITERAL){
			ret = new BinaryFormat(begin.image);
			end = begin;
		}else if(begin.kind == STRING_LITERAL){
			if(begin.image.length() > 2){
				ret = new StringExpression(begin.image.substring(1, begin.image.length() - 1));
			}else{
				ret = new StringExpression("");
			}
			end = begin;
		}else if(begin.kind == CHARACTER_LITERAL){
			if(begin.image.length() > 2){
				ret = new CharExpression(begin.image.charAt(1));
			}else{
				throw new ApplicationException("err");
			}
			end = begin;
		}else if(begin.kind == IDENTIFIER){
			if(begin == lastToken){
				ret = new IdentifierExpression(begin.image);
				end = begin;
			}else{
				MyToken a = fetchNext(begin);
				if(a.kind == LPAREN){
					assertEnd(a, lastToken);
					MyToken b = matchToken(fetchNext(a), lastToken, RPAREN);
					if(b == null){
						throw new ApplicationException("err");
					}
					Parentheses pt = getParentheses(a, b);
					boolean fmapping = false;
					boolean fbrace = false;
					boolean brace = false;
					MyToken d = null;
					MyToken e, f = null;
					List<Expression> fargs, margs = null, comms = null;
					fargs = pt.getArray();
					if(b != lastToken){
						MyToken c = fetchNext(b);
						if(c.kind == LBRACE){						
							if(c == lastToken){
								throw new ApplicationException("err");
							}
							d = matchToken(fetchNext(c), lastToken, RBRACE);
							if(d == null){
								throw new ApplicationException("err");
							}
							fbrace = true;
						}else if(c.kind == LBRACKET){
							if(c == lastToken){
								throw new ApplicationException("err");
							}
							d = matchToken(fetchNext(c), lastToken, RBRACKET);
							if(d == null){
								throw new ApplicationException("err");
							}
							BracketExpression be = getBracket(c, d);
							margs = be.getArray();
							if(d != lastToken){
								e = fetchNext(d);
								if(e.kind == LBRACE){
									if(e == lastToken){
										throw new ApplicationException("err");
									}
									f = matchToken(fetchNext(e), lastToken, RBRACE);
									if(f == null){
										throw new ApplicationException("err");
									}
									if(fetchNext(e) != f){
										BraceExpression prg = getBlock(e, f);
										comms = prg.getArray();
									}
									brace = true;
								}								
							}
							fmapping = true;
						}
					}
					if(fmapping){
						if(brace){
							FunctionMappingBrace fm = new FunctionMappingBrace();
							fm.setName(begin.image);
							fm.setArguments(fargs);
							fm.setExpressions(margs);
							fm.setStatements(comms);
							ret = fm;
							end = f;
						}else{
							FunctionMapping fm = new FunctionMapping();
							fm.setName(begin.image);
							fm.setArguments(fargs);
							fm.setExpressions(margs);
							ret = fm;
							end = d;
						}
					}else if(fbrace){
						FunctionBrace fb = new FunctionBrace();
						fb.setName(begin.image);
						fb.setArguments(fargs);
						fb.setStatements(margs);
						ret = fb;
						end = d;
					}else{
						FunctionExpression fe = new FunctionExpression();
						fe.setName(begin.image);
						fe.setArguments(fargs);
//						Expression exp = getFunctionExpression(begin, b);
						ret = fe;
						end = b;
					}
				}else if(a.kind == LBRACKET){
					assertEnd(a, lastToken);
					MyToken b = matchToken(fetchNext(a), lastToken, RBRACKET);
					if(b == null){
						throw new ApplicationException("err");
					}
					MappingExpression exp = getMappingExpression(begin, b);

					boolean mbrace = false;
					MyToken d = null;
					BraceExpression prg = null;
					if(b != lastToken){
						MyToken c = fetchNext(b);
						if(c.kind == LBRACE){						
							if(c == lastToken){
								throw new ApplicationException("err");
							}
							d = matchToken(fetchNext(c), lastToken, RBRACE);
							if(d == null){
								throw new ApplicationException("err");
							}
							mbrace = true;
							prg = getBlock(c, d);
						}
					}
					if(mbrace){
						MappingBrace mb = new MappingBrace();
						mb.setName(exp.getName());
						mb.setArguments(exp.getArguments());
						mb.setStatements(prg.getArray());
						ret = mb;
						end = d;
//						ConditionExpression ce = new ConditionExpression();
//						ce.setBody(exp);
//						ce.setConditions(prg.getArray());
//						ret = ce;
					}else{
						ret = exp;
						end = b;
					}
				}else if(a.kind == LBRACE){
					assertEnd(a, lastToken);
					MyToken b = matchToken(fetchNext(a), lastToken, RBRACE);
					if(b == null){
						throw new ApplicationException("err");
					}
					Expression exp = getIdentifierBrace(begin, b);
					ret = exp;
					end = b;
				}else{
					ret = new IdentifierExpression(begin.image);
					end = begin;
				}
			}
		}else if(begin.kind == LPAREN){
			if(begin == lastToken){
				throw new ApplicationException("err");
			}
			MyToken b = matchToken(fetchNext(begin), lastToken, RPAREN);
			if(b == null){
				throw new ApplicationException("err");
			}
//			if(fetchNext(begin) == b){
//				throw new ApplicationException("err");
//			}
//			ParenExpression pe = new ParenExpression();
//			pe.setExpression(getExpression(fetchNext(begin), fetchPrevious(b)));
			Expression pe = null;
			if(fetchNext(begin) == b){
				//()参数为空
				ParenExpression p0 = new ParenExpression();
				p0.setExpression(new NullExpression());
				pe = p0;
			}
			
			pe = getParentheses(fetchNext(begin), fetchPrevious(b));
			end = b;
			ret = pe;
		}else if(begin.kind == LBRACE){
			if(begin == lastToken){
				throw new ApplicationException("err");
			}
			MyToken b = matchToken(fetchNext(begin), lastToken, RBRACE);
			if(b == null){
				throw new ApplicationException("err");
			}
			Expression pe = getBlock(begin, b);
			end = b;
			ret = pe;
		}else if(begin.kind == LBRACKET){
			if(begin == lastToken){
				throw new ApplicationException("err");
			}
			MyToken b = matchToken(fetchNext(begin), lastToken, RBRACKET);
			if(b == null){
				throw new ApplicationException("err");
			}
			Expression pe = getBracket(begin, b);
			end = b;
			ret = pe;
		}else if(isSymbol(begin)){//symbol
			if(begin == lastToken){
				throw new ApplicationException("err");
			}
//			symbol = true;
			MyToken a = fetchNext(begin);
			ParsedExpression pe = getParsedExpression(a, lastToken);
			end = pe.getEnd();
			Expression exp = pe.getExpression();
			ret = new UnaryOperation(begin.image, exp);
		}else{
			throw new ApplicationException("err");
		}
//		if(!symbol){
		while(end != lastToken){
			MyToken a = fetchNext(end);
			//has some format like aa.bb.cc
			if(a.kind == DOT){
				if(a == lastToken){
					throw new ApplicationException("err");
				}
				MyToken b = fetchNext(a);
				ParsedExpression pe = getSufix(b, lastToken);
				end = pe.getEnd();
				SubjectOperation so = new SubjectOperation();
				so.setSubject(ret);
				so.setAction(pe.getExpression());
				ret = so;
			}else{
				break;
			}
		}
		
		ParsedExpression exp = new ParsedExpression(ret, end);
		return exp;
	}
	
	/**
	 * 返回identifier或function表达式
	 * @param firstToken
	 * @param lastToken
	 * @return
	 * @throws ApplicationException
	 */
	public ParsedExpression getSufix(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken begin, end = null;
		Expression ret = null;
		
		begin = firstToken;
		if(begin.kind == IDENTIFIER){
			if(begin == lastToken){
				ret = new IdentifierExpression(begin.image);
				end = begin;
				ParsedExpression exp = new ParsedExpression(ret, end);
				return exp;
			}else{
				MyToken a = fetchNext(begin);
				if(a.kind == LPAREN){
					assertEnd(a, lastToken);
					MyToken b = matchToken(fetchNext(a), lastToken, RPAREN);
					if(b == null){
						throw new ApplicationException("err");
					}
					Parentheses pt = getParentheses(a, b);
					boolean fmapping = false;
					boolean fbrace = false;
					boolean brace = false;
					MyToken d = null;
					MyToken e, f = null;
					List<Expression> fargs, margs = null, comms = null;
					fargs = pt.getArray();
					if(b != lastToken){
						MyToken c = fetchNext(b);
						if(c.kind == LBRACE){						
							if(c == lastToken){
								throw new ApplicationException("err");
							}
							d = matchToken(fetchNext(c), lastToken, RBRACE);
							if(d == null){
								throw new ApplicationException("err");
							}
							fbrace = true;
						}else if(c.kind == LBRACKET){
							if(c == lastToken){
								throw new ApplicationException("err");
							}
							d = matchToken(fetchNext(c), lastToken, RBRACKET);
							if(d == null){
								throw new ApplicationException("err");
							}
							BracketExpression be = getBracket(c, d);
							margs = be.getArray();
							if(d != lastToken){
								e = fetchNext(d);
								if(e.kind == LBRACE){
									if(e == lastToken){
										throw new ApplicationException("err");
									}
									f = matchToken(fetchNext(e), lastToken, RBRACE);
									if(f == null){
										throw new ApplicationException("err");
									}
									if(fetchNext(e) != f){
										BraceExpression prg = getBlock(e, f);
										comms = prg.getArray();
									}
									brace = true;
								}								
							}
							fmapping = true;
						}
					}
					if(fmapping){
						if(brace){
							FunctionMappingBrace fm = new FunctionMappingBrace();
							fm.setName(begin.image);
							fm.setArguments(fargs);
							fm.setExpressions(margs);
							fm.setStatements(comms);
							ret = fm;
							end = f;
						}else{
							FunctionMapping fm = new FunctionMapping();
							fm.setName(begin.image);
							fm.setArguments(fargs);
							fm.setExpressions(margs);
							ret = fm;
							end = d;
						}
					}else if(fbrace){
						FunctionBrace fb = new FunctionBrace();
						fb.setName(begin.image);
						fb.setArguments(fargs);
						fb.setStatements(margs);
						ret = fb;
						end = d;
					}else{
						FunctionExpression fe = new FunctionExpression();
						fe.setName(begin.image);
						fe.setArguments(fargs);
//						Expression exp = getFunctionExpression(begin, b);
						ret = fe;
						end = b;
					}
				}else if(a.kind == LBRACKET){
					assertEnd(a, lastToken);
					MyToken b = matchToken(fetchNext(a), lastToken, RBRACKET);
					if(b == null){
						throw new ApplicationException("err");
					}
					MappingExpression exp = getMappingExpression(begin, b);

					boolean mbrace = false;
					MyToken d = null;
					BraceExpression prg = null;
					if(b != lastToken){
						MyToken c = fetchNext(b);
						if(c.kind == LBRACE){						
							if(c == lastToken){
								throw new ApplicationException("err");
							}
							d = matchToken(fetchNext(c), lastToken, RBRACE);
							if(d == null){
								throw new ApplicationException("err");
							}
							mbrace = true;
							prg = getBlock(c, d);
						}
					}
					if(mbrace){
						MappingBrace mb = new MappingBrace();
						mb.setName(exp.getName());
						mb.setArguments(exp.getArguments());
						mb.setStatements(prg.getArray());
						ret = mb;
						end = d;
					}else{
						ret = exp;
						end = b;
					}
				}else if(a.kind == LBRACE){
					assertEnd(a, lastToken);
					MyToken b = matchToken(fetchNext(a), lastToken, RBRACE);
					if(b == null){
						throw new ApplicationException("err");
					}
					Expression exp = getIdentifierBrace(begin, b);
					ret = exp;
					end = b;
				}else{
					ret = new IdentifierExpression(begin.image);
					end = begin;
				}
				ParsedExpression exp = new ParsedExpression(ret, end);
				return exp;
			}
		}else{
			throw new ApplicationException("err");
		}
	}
//	public ParsedExpression getSufix(MyToken firstToken, MyToken lastToken) throws ApplicationException{
//		MyToken begin, end = null;
//		Expression ret = null;
//		
//		begin = firstToken;
//		if(begin.kind == IDENTIFIER){
//			if(begin == lastToken){
//				ret = new IdentifierExpression(begin.image);
//				end = begin;
//				ParsedExpression exp = new ParsedExpression(ret, end);
//				return exp;
//			}
//			MyToken a = fetchNext(begin);
//			if(a.kind == LPAREN){
//				assertEnd(a, lastToken);
//				MyToken b = matchToken(fetchNext(a), lastToken, RPAREN);
//				if(b == null){
//					throw new ApplicationException("err");
//				}
//				if(b == lastToken) {
//					Expression exp = getFunctionExpression(begin, b);
//					ret = exp;
//					end = b;
//				}else {
//					MyToken c = fetchNext(b);
//					if(c.kind == LBRACE) {
//						MyToken d = matchToken(fetchNext(c), lastToken, RBRACE);
//						FunctionBrace fb = new FunctionBrace();
//						Parentheses paren = getParentheses(a, b);
//						BraceExpression brace = getBlock(c,d);
//						fb.setName(begin.image);
//						fb.setArguments(paren.getArray());
//						fb.setStatements(brace.getArray());
//						ret = fb;
//						end = d;
//					}else {
//						Expression exp = getFunctionExpression(begin, b);
//						ret = exp;
//						end = b;
//					}
//				}
//			}else if(a.kind == LBRACKET){
//				assertEnd(a, lastToken);
//				MyToken b = matchToken(fetchNext(a), lastToken, RBRACKET);
//				if(b == null){
//					throw new ApplicationException("err");
//				}
//				if(b == lastToken) {
//					Expression exp = getMappingExpression(begin, b);
//					ret = exp;
//					end = b;
//				}else {
//					MyToken c = fetchNext(b);
//					if(c.kind == LBRACE) {
//						MyToken d = matchToken(fetchNext(c), lastToken, RBRACE);
//						MappingBrace mb = new MappingBrace();
//						BracketExpression paren = getBracket(a, b);
//						BraceExpression brace = getBlock(c,d);
//						mb.setName(begin.image);
//						mb.setArguments(paren.getArray());
//						mb.setStatements(brace.getArray());
//						ret = mb;
//						end = d;
//					}else {
//						Expression exp = getMappingExpression(begin, b);
//						ret = exp;
//						end = b;
//					}
//				}
//			}else if(a.kind == LBRACE){
//				assertEnd(a, lastToken);
//				MyToken b = matchToken(fetchNext(a), lastToken, RBRACE);
//				if(b == null){
//					throw new ApplicationException("err");
//				}
//				Expression exp = getIdentifierBrace(begin, b);
//				ret = exp;
//				end = b;
//			}else{
//				ret = new IdentifierExpression(begin.image);
//				end = begin;
//			}
//			ParsedExpression exp = new ParsedExpression(ret, end);
//			return exp;
//		}else{
//			throw new ApplicationException("err");
//		}
//	}
	
	public class ParsedExpression{
		private Expression expression;
		private MyToken end;
		public ParsedExpression(Expression expression, MyToken end){
			this.expression = expression;
			this.end = end;
		}
		public Expression getExpression() {
			return expression;
		}
		public void setExpression(Expression expression) {
			this.expression = expression;
		}
		public MyToken getEnd() {
			return end;
		}
		public void setEnd(MyToken end) {
			this.end = end;
		}
	}
	
	public List<Expression> getArguments(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken a;
		MyToken begin, end;
		
		begin = firstToken;
		
		List<Expression> list = new ArrayList<Expression>();
		while(true){
			if(begin.kind == 0 || begin == null){
				break;
			}
			a = matchToken(begin, lastToken, COMMA);
			if(a != null){
				if(begin == a){
					list.add(new NullExpression());
				}else{
					end = fetchPrevious(a);
					Expression item = getExpression(begin, end);
					list.add(item);
				}
				begin = fetchNext(a);
			}else{
//				if(begin == lastToken){
//					list.add(new NullExpression());
//				}else{
				Expression item = getExpression(begin, lastToken);
				list.add(item);
//				}
				break;
			}
			if(a == lastToken){
				list.add(new NullExpression());
				break;
			}
		}
		return list;
	}
	
	public SetSentence getSetSentence(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		SetSentence ref = new SetSentence();
		List<String> names = new ArrayList<String>();
		List<AssignmentExpression> assigns = new ArrayList<AssignmentExpression>();
		MyToken begin = fetchNext(firstToken), end = null;
		MyToken a = null;
		while(true){
			if(begin.kind == 0 || begin == null){
				break;
			}
			if(begin == lastToken){
				end = lastToken;
				a = lastToken;
			}else{
				a = matchToken(fetchNext(begin), lastToken, COMMA);
				if(a == null){
					end = lastToken;
					a = lastToken;
				}else{
					end = fetchPrevious(a);
				}
			}
			
			if(begin.kind == IDENTIFIER){
				if(begin == end){
					String name = begin.image;
					for(String nn:names){
						if(nn.equals(name)){
							throw new ApplicationException("err!");
						}
					}
					for(AssignmentExpression item:assigns){
						String s = item.getName();
						if(s.equals(name)){
							throw new ApplicationException("err!");
						}
					}
					names.add(name);
				}else{
//					AssignmentExpression ae = getAssignmentExpression(begin, end);
					String name = begin.image;
					for(String nn:names){
						if(nn.equals(name)){
							throw new ApplicationException("err!");
						}
					}
					for(AssignmentExpression item:assigns){
						String s = item.getName();
						if(s.equals(name)){
							throw new ApplicationException("err!");
						}
					}
					MyToken b = fetchNext(begin);
					if(b.kind != ASSIGN){
						throw new ApplicationException("err!");
					}
					if(b == end){
						throw new ApplicationException("err!");
					}
					MyToken c = fetchNext(b);
					Expression exp = getCognitionExpression(c, end);
					AssignmentExpression ae = new AssignmentExpression();
					ae.setName(name);
					ae.setExpression(exp);
					assigns.add(ae);
				}
			}else{
				throw new ApplicationException("err!");
			}
			if(a == lastToken){
				break;
			}else{
				begin = fetchNext(a);
			}
		}
		ref.setAssigns(assigns);
		ref.setNames(names);
		return ref;
	}
	
	public Expression getCognitionExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		return getExpression(firstToken, lastToken);
	}
	/**
	 * set(Type) name/function:name/function
	 * Type=Action or Cognition
	 * 
	 * @param firstToken
	 * @param lastToken
	 * @return
	 * @throws ApplicationException
	 */
	public ExpressSentence getExpressSentence(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		//firstToken is "set"
		assertEnd(firstToken, lastToken);
		ExpressSentence stn = new ExpressSentence();
		MyToken a, b, c, d, e;
		a = fetchNext(firstToken);
		assertEnd(a, lastToken);
		if(a.kind != LPAREN){
			throw new ApplicationException("err format!");
		}
		b = fetchNext(a);
		if(b.kind != IDENTIFIER){
			throw new ApplicationException("err format!");
		}
		if(b.image.equals("Action") || //cancel equals("Object" or "Cognition")
				b.image.equals("Operation")){
			stn.setType(b.image);
		}else{
			throw new ApplicationException("err format!");
		}

		assertEnd(b, lastToken);
		c = fetchNext(b);
		if(c.kind != RPAREN){
			throw new ApplicationException("err format!");
		}
		assertEnd(c, lastToken);
		d = fetchNext(c);
		assertEnd(d, lastToken);
		if(d.kind != IDENTIFIER){//name
			throw new ApplicationException("err format!");
		}

		e = matchToken(d, lastToken, COLON);
		if(d == fetchPrevious(e)){
			stn.setLeft(new IdentifierExpression(d.image));
		}else if(isFunctionExpression(d, fetchPrevious(e))){
			Expression expr = getFunctionExpression(d, fetchPrevious(e));
			stn.setLeft(expr);
		}else{
			throw new ApplicationException("err format!");
		}
		
		assertEnd(e, lastToken);
		if(fetchNext(e) == lastToken){
			stn.setRight(new IdentifierExpression(lastToken.image));
		}else if(isFunctionExpression(fetchNext(e), lastToken)){
			Expression expr = getFunctionExpression(fetchNext(e), lastToken);
			stn.setRight(expr);
		}else{
			throw new ApplicationException("err format!");
		}
		return stn;
	}
	
	

	/**
	 * return identifier/functionExpression/DotFormatExpression/notExpression/
	 * 		judgeExpression/nameIndex/logicExpression
	 * Expression of NotExpression:identifier/functionExpression/PAREN/
	 * @param firstToken
	 * @param lastToken
	 * @return
	 * @throws ApplicationException
	 */
	public Expression getLogicExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken a;
		MyToken begin, end;
		begin = firstToken;
		a = matchLogicToken(begin, lastToken, 'l');
		if(a == firstToken || a == lastToken){
			throw new ApplicationException("err!");
		}
		if(a == null){
			return getJudgeExpression(firstToken, lastToken);
		}else{
			LogicExpression logic = new LogicExpression();
			end = fetchPrevious(a);
			Expression left = getJudgeExpression(begin, end);
			logic.setLeft(left);
			String op;
			if(a.kind == SC_AND){
				op = "&&";
			}else{
				op = "||";
			}
			logic.setOperator(op);
			
			begin = fetchNext(a);
			while(true){
				if(begin.kind == 0 || begin == null){
					break;
				}
				a = matchLogicToken(begin, lastToken, 'l');

				Expression right = null;
				if(a != null){
					if(a == lastToken){
						throw new ApplicationException("err!");
					}
					
					if(a.kind == SC_AND){
						op = "&&";;
					}else{
						op = "||";
					}
					end = fetchPrevious(a);
					right = getJudgeExpression(begin, end);
					begin = fetchNext(a);
					
					logic.setRight(right);
					LogicExpression tmp = new LogicExpression();
					tmp.setLeft(logic);
					tmp.setOperator(op);
					logic = tmp;
					
					end = a;
				}else{
					right = getJudgeExpression(begin, lastToken);
					logic.setRight(right);
					end = lastToken;
				}
				
				if(end == lastToken){
					break;
				}else{
					begin = fetchNext(end);
				}
			}
			return logic;
		}
	}
	
	/**
	 * 判断表达式必定包含(>,<,>=,<=,==)判断符号之一，且只包含一个
	 * expression1 operator@judgeWord(adverb) expression2
	 * (==,>=,<=)包含=，暂时不支持adverb
	 * @param firstToken
	 * @param lastToken
	 * @return
	 * @throws ApplicationException
	 */
	public Expression getJudgeExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{

		return null;
	}
	
	public BraceExpression getBlock(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		BraceExpression ret = new BraceExpression();
		List<Expression> list = new ArrayList<Expression>();
		if(fetchNext(firstToken) != lastToken){
			list = getSentenceList(fetchNext(firstToken), fetchPrevious(lastToken));
		}
		ret.setArray(list);
		return ret;
	}
	
	public BracketExpression getBracket(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		BracketExpression ret = new BracketExpression();
		List<Expression> list = new ArrayList<Expression>();
		if(fetchNext(firstToken) != lastToken){
			list = getArguments(fetchNext(firstToken), fetchPrevious(lastToken));
		}
//		else {
//			list.add(new NullExpression());
//		}
		ret.setArray(list);
		return ret;
	}
	
	public Parentheses getParentheses(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		Parentheses ret = new Parentheses();
		List<Expression> list = new ArrayList<Expression>();
		if(fetchNext(firstToken) != lastToken){
			list = getArguments(fetchNext(firstToken), fetchPrevious(lastToken));
		}else {
			list.add(new NullExpression());
		}
		ret.setArray(list);
		return ret;
	}
	
	public Expression getExpression(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		if(firstToken == lastToken){
			if(firstToken.kind == STRING_LITERAL){
				return getStringExpression(firstToken, firstToken);
			}else if(firstToken.kind == IDENTIFIER){
				return new IdentifierExpression(firstToken.image);
			}else if(firstToken.kind == INTEGER_LITERAL){
				return new IntegerNumber(firstToken.image);
			}else if(firstToken.kind == HEX_LITERAL){
				return new HexFormat(firstToken.image);
			}else if(firstToken.kind == OCTAL_LITERAL){
				return new OctalFormat(firstToken.image);
			}else if(firstToken.kind == BINARY_LITERAL){
				return new BinaryFormat(firstToken.image);
			}else if(firstToken.kind == FLOATING_POINT_LITERAL){
				return new DecimalNumber(firstToken.image);
			}else if(firstToken.kind == FRACTION_LITERAL){
				return new FractionNumber(StringUtil.getFractionUp(firstToken.image),
						StringUtil.getFractionLow(firstToken.image));
			}else if(firstToken.kind == SCIENTIFIC_NOTATION){
				return new ScientificNotation(StringUtil.getScientificValue(firstToken.image),
						StringUtil.getScientificPower(firstToken.image));
			}else if(firstToken.kind == CHARACTER_LITERAL){
				return getCharExpression(firstToken, firstToken);
			}
		}else{
			return getOperationExpression(firstToken, lastToken);
		}
		throw new ApplicationException("err!");
	}

	/*****************begin db parse****************/
	public OperationMap toOperationMap() throws ApplicationException{
		MyToken begin, end = null;
		begin = (MyToken) token_source.getNextToken();//first token
		end = begin;
		MyToken a = fetchNext(begin);
		while(true){
			if(a.kind == 0 || a == null){
				break;
			}
			end = a;
			a = fetchNext(end);
		}
		return toOperationMap(begin, end);
	}
	
	/**
	 * format:[list<varPosition>]expression
	 * list<varPosition>:type%position,...
	 * @param firstToken
	 * @param lastToken
	 * @return
	 */
	public OperationMap toOperationMap(MyToken firstToken, MyToken lastToken) throws ApplicationException{
		MyToken a, b, c;
		MyToken begin, end;
		begin = firstToken;
		if(begin.kind != LBRACKET){
			return null;//exception
		}
		if(begin == lastToken){
			return null;//assert end
		}
		end = matchToken(fetchNext(begin), lastToken, RBRACKET);
		if(end == null){
			return null;
		}
		OperationMap ret = new OperationMap();
		List<VariablePosition> list = new ArrayList<VariablePosition>();
		if(fetchNext(begin) == end){
			ret.setArgs(list);
		}else{
			begin = fetchNext(begin);
			b = fetchPrevious(end);//b is end of list of variable position
			while(true){
				if(begin.kind == 0 || begin == null){
					break;
				}
				a = matchToken(begin, b, COMMA);
				if(a != null){
					c = fetchPrevious(a);
				}else{
					c = b;
				}

				if(begin == c || fetchNext(begin) != c){
					throw new ApplicationException("err");
				}
				if(begin.kind != IDENTIFIER || c.kind != INTEGER_LITERAL){
					throw new ApplicationException("err");
				}
				VariablePosition vp = new VariablePosition(begin.image, Integer.parseInt(c.image));
				list.add(vp);
				if(c == b){
					break;
				}else{
					begin = fetchNext(a);
				}
			}
			ret.setArgs(list);
		}
		if(end == lastToken){
			throw new ApplicationException("err");
		}
		a = fetchNext(end);
		if(a.kind != LPAREN){
			throw new ApplicationException("err");
		}
		if(a == lastToken){
			throw new ApplicationException("err");
		}
		b = matchToken(fetchNext(a), lastToken, RPAREN);
		if(b == null || b != lastToken){
			throw new ApplicationException("err");
		}
		if(fetchNext(a) == b){
			throw new ApplicationException("err");
		}
		Expression expr = getExpression(fetchNext(a), fetchPrevious(b));
		ret.setExpression(expr);
		return ret;
	}
	/*****************end db parse*****************/
	public MyToken matchToken(int kind) throws ApplicationException {
		MyToken a = (MyToken) token_source.getNextToken();//first token
		return matchToken(a, null, kind);
	}
	
	//return null when can't find token
	public MyToken matchToken(MyToken begin, MyToken end, int kind) throws ApplicationException {
		MyToken a;
		a = begin;
		if (a == null){
			return null;
		}
		Stack<MyToken> stack = new Stack<MyToken>();
		while(true){
			if(a.kind == IDENTIFIER && a.image.equals("Range")){
				MyToken b = skipRange(a, end);
				if(b == end){
					break;
				}else{
					a = fetchNext(b);
				}
			}
			
			if ( a.kind == kind && stack.empty()){
				return a;
			}
			
			if (a.kind == LBRACE || a.kind == LPAREN || a.kind == LBRACKET){
				stack.push(a);
			}else if ( a.kind == RBRACE ){
				if (!stack.empty() && stack.peek().kind == LBRACE){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}else if ( a.kind == RPAREN ){
				if (!stack.empty() && stack.peek().kind == LPAREN){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}else if ( a.kind == RBRACKET ){
				if (!stack.empty() && stack.peek().kind == LBRACKET){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}

			if (a == end){
				break;
			}else{
				a = fetchNext(a);
			}
			if (a.kind == 0 || a == null){ //in the end, can't find ";"
				break;
			}
		}
		
		if (stack.empty()){
			return null;
		}else{
			throw new ApplicationException("can not match .");
		}
	}
	
	//return null when can't find token
	public MyToken matchWordToken(MyToken begin, MyToken end, String word) throws ApplicationException {
		MyToken a;
		a = begin;
		if (a == null){
			return null;
		}
		Stack<MyToken> stack = new Stack<MyToken>();
		while(true){
			if(a.kind == IDENTIFIER){
				if(a.image.equals(word) && stack.empty()){
					return a;
				}
			}
			if(a.kind == IDENTIFIER && a.image.equals("Range")){
				MyToken b = skipRange(a, end);
				if(b == end){
					break;
				}else{
					a = fetchNext(b);
				}
			}

			if (a.kind == LBRACE || a.kind == LPAREN || a.kind == LBRACKET){
				stack.push(a);
			}else if ( a.kind == RBRACE ){
				if (!stack.empty() && stack.peek().kind == LBRACE){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}else if ( a.kind == RPAREN ){
				if (!stack.empty() && stack.peek().kind == LPAREN){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}else if ( a.kind == RBRACKET ){
				if (!stack.empty() && stack.peek().kind == LBRACKET){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}

			if (a == end){
				break;
			}else{
				a = fetchNext(a);
			}
			if (a.kind == 0 || a == null){ //in the end, can't find ";"
				break;
			}
		}

		if (stack.empty()){
			return null;
		}else{
			throw new ApplicationException("can not match .");
		}
	}

	//return null when can't find token
	/**
	 * Logic: match || or &&
	 * Judge: match > < >= <= == !=
	 * Range: match ) and ]
	 * char l for logic
	 * j for judge
	 * r for range right side(")" or "]")
	 */
	public MyToken matchLogicToken(MyToken begin, MyToken end, char type) throws ApplicationException {
		MyToken a;
		a = begin;
		if (a == null){
			return null;
		}
		Stack<MyToken> stack = new Stack<MyToken>();
		while(true){
			if(a.kind == IDENTIFIER && a.image.equals("Range")){
				MyToken b = skipRange(a, end);
				if(b == end){
					break;
				}else{
					a = fetchNext(b);
				}
			}
			
			if(type == 'l'){
				if ( a.kind == SC_AND || a.kind == SC_OR && stack.empty()){
					return a;
				}
			}else if(type == 'j'){
				if ( JudgeOperators.isOperator(a.image) && stack.empty()){
					return a;
				}
			}else if(type == 'r'){
				if(a.kind == RPAREN || a.kind == RBRACKET){
					return a;
				}
			}else{
				throw new ApplicationException("type err!");
			}
			
			if (a.kind == LBRACE || a.kind == LPAREN || a.kind == LBRACKET){
				stack.push(a);
			}else if ( a.kind == RBRACE ){
				if (!stack.empty() && stack.peek().kind == LBRACE){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}else if ( a.kind == RPAREN ){
				if (!stack.empty() && stack.peek().kind == LPAREN){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}else if ( a.kind == RBRACKET ){
				if (!stack.empty() && stack.peek().kind == LBRACKET){
					stack.pop(); 
				}else {
					throw new ApplicationException("can not match }");
				}
			}

			if (a == end){
				break;
			}else{
				a = fetchNext(a);
			}
			if (a.kind == 0 || a == null){ //in the end, can't find ";"
				break;
			}
		}
		
		if (stack.empty()){
			return null;
		}else{
			throw new ApplicationException("can not match .");
		}
	}
	
	/**
	 * format = "Range" ["("|"["] "," [")"|"]"]
	 * "(" and ")"表示不包含，"[" and "]"表示包含
	 * 结果返回最后一个rangeToken，即[")"|"]"]
	 * @param begin
	 * @param end
	 * @return
	 * @throws ApplicationException
	 */
	public MyToken skipRange(MyToken begin, MyToken end) throws ApplicationException {
		if(begin.kind == IDENTIFIER && begin.image.equals("Range")){
			assertEnd(begin, end);
			MyToken a = fetchNext(begin);
			if(a.kind != LPAREN && a.kind != LBRACKET){
				throw new ApplicationException("err!");
			}
			assertEnd(a, end);
			MyToken b = matchToken(fetchNext(a), end, COMMA);
			if(b == null){
				throw new ApplicationException("err!");
			}
			assertEnd(b, end);
			a = fetchNext(b);
			assertEnd(a, end);//a is not end token.
			b = matchLogicToken(a, end, 'r');
			if(b == null){
				throw new ApplicationException("err!");
			}else{
				return b;
			}
		}else{
			throw new ApplicationException("err!");
		}
	}
	
	public int getExpressionLen(MyToken begin, MyToken end) throws TokenMgrError{
		int i = 1;
		MyToken t;
		if (begin == null) 
			return 0;
		else{
			t = begin;
			do{
				t = fetchNext(t);
				if (t == null) break;
				i++;
			}while (t != end);
			return i;
		}
	}

	public MyToken fetchNext(MyToken t) throws TokenMgrError{
		if (t.next != null) return (MyToken) t.next;
		else {
			t.next = token_source.getNextToken();
			t.next.previous = t;
			return (MyToken) t.next;
		}
	}

	public MyToken fetchPrevious(MyToken t){
		if (t.previous != null) return (MyToken) t.previous;
		return null;
	}
	
	public MyToken fetchEndToken(MyToken t) throws ApplicationException {
		if(t == null || t.kind == 0){
			throw new ApplicationException("fetch end token err!");
		}
		MyToken a, b;
		a = t;
		b = fetchNext(a);
		while (b != null){
			if(b.kind  != 0){
				a = b;
				b = fetchNext(a);
			}else{
				return a;
			}
		}
		throw new ApplicationException("fetch end token err!");
	}
	
	public void assertEnd(MyToken t, MyToken end) throws ApplicationException{
		if(t.kind == 0 || t == null){
			throw new ApplicationException("token error!");
		}else if(fetchNext(t).kind == 0 || t == end){
			throw new ApplicationException("token end!");
		}
	}

	/** Get the next Token. 
	 * @throws TokenMgrError */
	final public Token getNextToken() throws TokenMgrError {
		if (token.next != null) token = token.next;
		else {
			token.next = token_source.getNextToken();
			if (token != null){
				token.next.previous = token;
			}
			token = token.next;
		}
		jj_ntk = -1;
		return token;
	}

	/** Get the specific Token. 
	 * @throws TokenMgrError */
	final public Token getToken(int index) throws TokenMgrError {
		Token t = token;
		for (int i = 0; i < index; i++) {
			if (t.next != null) t = t.next;
			else t = t.next = token_source.getNextToken();
		}
		return t;
	}

	/** Generate ParseException. */
	public ParseException generateParseException() {
		Token errortok = token.next;
		int line = errortok.beginLine, column = errortok.beginColumn;
		String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
		return new ParseException("Parse error at line " + line + ", column " + column + ".  Encountered: " + mess);
	}

	/** Enable tracing. */
	final public void enable_tracing() {
	}

	/** Disable tracing. */
	final public void disable_tracing() {
	}
	
	public void testSyntax(){
		int a = EOF;//如何获得最后一个token
	}
}

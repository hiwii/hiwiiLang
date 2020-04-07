package net.hiwii.db;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.sleepycat.bind.tuple.BooleanBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.Transaction;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.NullValue;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.collection.EntityList;
import net.hiwii.collection.TypedEntityList;
import net.hiwii.context.HiwiiContext;
import net.hiwii.context.RuntimeContext;
import net.hiwii.context.SessionContext;
import net.hiwii.db.bind.AssignmentBinding;
import net.hiwii.db.bind.DeclarationBinding;
import net.hiwii.db.bind.DefinitionBinding;
import net.hiwii.db.bind.FunctionAssignBinding;
import net.hiwii.db.bind.FunctionHeadBinding;
import net.hiwii.db.bind.ListExpressionBinding;
import net.hiwii.db.bind.MessageBinding;
import net.hiwii.db.bind.PropertyBinding;
import net.hiwii.db.bind.ValueBinding;
import net.hiwii.db.bind.VariableBinding;
import net.hiwii.db.bind.func.FunctionDeclarationBinding;
import net.hiwii.db.bind.func.MappingDeclarationBinding;
import net.hiwii.db.ent.FunctionAssign;
import net.hiwii.db.ent.FunctionHead;
import net.hiwii.db.ent.StoredValue;
import net.hiwii.db.key.AssignmentIDKeyCreater;
import net.hiwii.db.key.DefinitionFunctionKeyCreater;
import net.hiwii.db.key.DefinitionSignKeyCreater;
import net.hiwii.db.key.EntityPartKeyCreater;
import net.hiwii.db.key.FunctionHeadKeyCreater;
import net.hiwii.db.key.InstanceFunctionKeyCreater;
import net.hiwii.db.key.InstanceNameKeyCreater;
import net.hiwii.db.key.InstanceTypeKeyCreater;
import net.hiwii.db.key.MultiFunctionKeyCreater;
import net.hiwii.db.key.PropertyTypeKeyCreater;
import net.hiwii.db.key.RelationEntityHostKeyCreater;
import net.hiwii.def.Assignment;
import net.hiwii.def.Declaration;
import net.hiwii.def.Definition;
import net.hiwii.def.decl.ConditionDeclaration;
import net.hiwii.def.decl.FunctionDeclaration;
import net.hiwii.def.decl.MappingDeclaration;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.BracketExpression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.expr.ParenExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.expr.sent.SubjectAction;
import net.hiwii.message.HiwiiException;
import net.hiwii.msg.Message;
import net.hiwii.obj.comp.MultiObjectList;
import net.hiwii.prop.Property;
import net.hiwii.prop.VariableStore;
import net.hiwii.system.LocalHost;
import net.hiwii.system.SystemDefinition;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.obj.InstanceObject;
import net.hiwii.system.syntax.number.NumberExpression;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.user.Group;
import net.hiwii.user.User;
import net.hiwii.user.bind.GroupBinding;
import net.hiwii.user.key.UserGroupKeyCreater1;
import net.hiwii.user.key.UserGroupKeyCreater2;
import net.hiwii.view.Entity;
import net.hiwii.view.HiwiiInstance;
import net.hiwii.view.ProjectedObject;

/**
 * "@" Link Id and definitionSignature.
 * "&" Link Id and InstanceUUID
 * "#" Link function or mapping and argumentNumber. e.g.f#3%000123 //one function:f(x,y,z)
 * "%" Link function or mapping and randomUUID
 * "^" Link function definition and declaration randomUUID
 * DBList
 * 1,对象 entityDatabase
 * 2,Link/State/Switch,PropertyDef
 * 3,statement:assignment/judgment/switchResult,分别从属于对象、定义、string/Number等。
 * 4,
 * @author WangZhenhai
 *
 */
public class HiwiiDB {
	Environment myDbEnvironment = null;
	Database myDatabase = null;
	DatabaseConfig dbConfig = null;
	
	SecondaryConfig mySecConfig = null;
	SecondaryDatabase mySecDb = null;

	Database refers = null;
	Database entityDatabase = null;//single identification识别
	Database defDatabase = null;
	Database idState = null;
	
	Database idLink = null;//Link or Property
	
	Database functionLink = null;
	SecondaryDatabase indexFunctionLink = null;
	
	Database functionState = null;
	SecondaryDatabase indexFunctionState = null;
	
	//action define
	Database functionAction = null;
	SecondaryDatabase indexFunctionAction = null;
	
	Database mappingLink = null;
	
	Database idAssign = null;
	Database functionAssign = null;
	SecondaryDatabase indexFunctionAssign = null;
	Database listAssignDatabase = null;
	
	Database judgeDatabase = null;
	
	Database actionDatabase = null;
	
	Database switchDef = null;
	Database switchResult = null;
	
	SecondaryDatabase indexDefinitionSign = null;	
	SecondaryDatabase indexPropDef = null;
	
	SecondaryDatabase indexAssignHost = null;
	SecondaryDatabase indexJudgeHost = null;
	SecondaryDatabase indexSwitchHost = null;
	
	Database processDB = null;
	
	Database decides = null;
	Database variables = null;
	
	SecondaryDatabase indexInstType = null;
	SecondaryDatabase indexEntityPart = null;
	
	Database idCalculation = null;
	Database idDecision = null;
	Database idAction = null;
	
	Database inst_idCalculation = null;
	Database inst_idDecision = null;
	Database inst_idAction = null;
	
	//declaration
	Database fCalculation = null;
	Database fDecision = null;
	Database fAction = null;
	
	SecondaryDatabase indexfCalculation = null;	
	SecondaryDatabase indexfDecision = null;	
	SecondaryDatabase indexfAction = null;
	
	Database fCalculationDef = null;
	SecondaryDatabase indexfCalculationDef = null;
	
	Database fCalculationInst = null;
	SecondaryDatabase indexfCalculationInst = null;
	
	Database fActionDef = null;
	SecondaryDatabase indexfActionDef = null;
	
	Database fActionInst = null;
	SecondaryDatabase indexfActionInst = null;
	
	Database mCalculation = null;
	Database mDecision = null;
	Database mAction = null;
	
	Database defInDef = null;
	Database propInDef = null;
	
	Database userDatabase = null;
	Database groupDatabase = null;
	Database roleDatabase = null;
	
	Database childEntity = null;
	SecondaryDatabase indexHostChild = null;
	
	Database mapUserGroup = null;
	SecondaryDatabase indexUserGroup = null;
	SecondaryDatabase indexGroupUser = null;
	
	Database rightIdAction = null;
	Database rightIdCalculation = null;
	SecondaryDatabase indexIdActionUser = null;
	
	//非开放权限
	Database closinIdCalculation = null;
	Database closinIdAction = null;
	
	//非开放权限
	Database spaceExclude = null;
	
	Database messageDB = null;
	
	Database nameDatabase = null;
	SecondaryDatabase indexInstanceName = null;
	
	/*
	 * 所有的属性值全部做索引。
	 * 建立一个索引database，key=propertyName + signature, value = instanceId + propertyValue
	 * 同时建立一个index，index=propertyValue + propertyName + signature;
	 * propertyValue = charType + stringValue
	 * 当value是一个instanceId, charType = 'i' 
	 * 当值发生变化，则删除索引，重新建立。
	 * instanceDatabase需要有用户定义的index，user、group等特殊对象需要单独建立index.
	 * 
	 */
//	Database entityIndexValue = null;
//	SecondaryDatabase indexEntityIndex = null;
//	SecondaryDatabase indexUser = null;
//	Database entityIndexUser = null;
	
	
	public Environment getDbEnvironment() {
		return myDbEnvironment;
	}

	public void setDbEnvironment(Environment myDbEnvironment) {
		this.myDbEnvironment = myDbEnvironment;
	}

	public DatabaseConfig getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	public Database getIdCalculation() {
		return idCalculation;
	}

	public void setIdCalculation(Database idCalculation) {
		this.idCalculation = idCalculation;
	}

	public Database getfAction() {
		return fAction;
	}

	public Database getFunctionLink() {
		return functionLink;
	}

	public void setFunctionLink(Database functionLink) {
		this.functionLink = functionLink;
	}

	public Database getMappingLink() {
		return mappingLink;
	}

	public void setMappingLink(Database mappingLink) {
		this.mappingLink = mappingLink;
	}

	public Database getfCalculation() {
		return fCalculation;
	}

	public void setfCalculation(Database fCalculation) {
		this.fCalculation = fCalculation;
	}

	public Database getfCalculationInst() {
		return fCalculationInst;
	}

	public void setfCalculationInst(Database fCalculationInst) {
		this.fCalculationInst = fCalculationInst;
	}

	public Database getmCalculation() {
		return mCalculation;
	}

	public void setmCalculation(Database mCalculation) {
		this.mCalculation = mCalculation;
	}

	public void setfAction(Database fAction) {
		this.fAction = fAction;
	}

	public SecondaryDatabase getIndexJudgeHost() {
		return indexJudgeHost;
	}

	public void setIndexJudgeHost(SecondaryDatabase indexJudgeHost) {
		this.indexJudgeHost = indexJudgeHost;
	}

	public Database getRefers() {
		return refers;
	}

	public void setRefers(Database refers) {
		this.refers = refers;
	}

	public Database getVariables() {
		return variables;
	}

	public void setVariables(Database variables) {
		this.variables = variables;
	}

	public Database getRoleDatabase() {
		return roleDatabase;
	}

	public void setRoleDatabase(Database roleDatabase) {
		this.roleDatabase = roleDatabase;
	}

	public void open(){
		try {
			// Open the environment. Create it if it does not already exist.
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			envConfig.setTransactional(true);
			File dir = new File("dat");
			if(!dir.exists()){
				dir.mkdirs();
			}
			myDbEnvironment = new Environment(new File("dat"), envConfig);
			
			// Open the database. Create it if it does not already exist.
			DatabaseConfig dbconf = new DatabaseConfig();
			dbconf.setAllowCreate(true);
			dbconf.setTransactional(true);
			
			setDbEnvironment(myDbEnvironment);
			setDbConfig(dbconf);
						
			mySecConfig = new SecondaryConfig();
			mySecConfig.setAllowCreate(true);
			mySecConfig.setSortedDuplicates(true);
			mySecConfig.setTransactional(true);
			
			myDatabase = myDbEnvironment.openDatabase(null, "sampleDatabase", dbconf);
			
			defDatabase = myDbEnvironment.openDatabase(null, "definitionDatabase", dbconf);
			idLink = myDbEnvironment.openDatabase(null, "idLink", dbconf);//new
			functionLink = myDbEnvironment.openDatabase(null, "functionLink", dbconf);//new
			functionState = myDbEnvironment.openDatabase(null, "functionState", dbconf);
			functionAction = myDbEnvironment.openDatabase(null, "functionAction", dbconf);
			
			mappingLink = myDbEnvironment.openDatabase(null, "mappingLink", dbconf);//new
			
			mySecConfig.setKeyCreator(new FunctionHeadKeyCreater());
			indexFunctionLink = myDbEnvironment.openSecondaryDatabase(null, "indexFunctionLink", functionLink, 
					mySecConfig);

			mySecConfig.setKeyCreator(new FunctionHeadKeyCreater());
			indexFunctionState = myDbEnvironment.openSecondaryDatabase(null, "indexFunctionState", functionState, 
					mySecConfig);
			
			mySecConfig.setKeyCreator(new FunctionHeadKeyCreater());
			indexFunctionAction = myDbEnvironment.openSecondaryDatabase(null, "indexFunctionAction", functionAction, 
					mySecConfig);
			
			idAssign = myDbEnvironment.openDatabase(null, "idAssign", dbconf);
			functionAssign = myDbEnvironment.openDatabase(null, "functionAssign", dbconf);
			
			listAssignDatabase = myDbEnvironment.openDatabase(null, "listAssignDatabase", dbconf);
			
			idState = myDbEnvironment.openDatabase(null, "idState", dbconf);
			judgeDatabase = myDbEnvironment.openDatabase(null, "judgementDatabase", dbconf);
			
			actionDatabase = myDbEnvironment.openDatabase(null, "actionDatabase", dbconf);
			
			switchDef = myDbEnvironment.openDatabase(null, "switchDef", dbconf);
			switchResult = myDbEnvironment.openDatabase(null, "switchResult", dbconf);
						
			processDB = myDbEnvironment.openDatabase(null, "processDB", dbconf);
			
			refers = myDbEnvironment.openDatabase(null, "refers", dbconf);
			variables = myDbEnvironment.openDatabase(null, "variables", dbconf);
			decides = myDbEnvironment.openDatabase(null, "decides", dbconf);
			
			spaceExclude = myDbEnvironment.openDatabase(null, "spaceExclude", dbconf);
			messageDB = myDbEnvironment.openDatabase(null, "messageDB", dbconf);
			/**
			 * key:uuid
			 * data:type signature
			 */
			entityDatabase = myDbEnvironment.openDatabase(null, "entityDatabase", dbconf);
			mySecConfig.setKeyCreator(new EntityPartKeyCreater());
			indexEntityPart = myDbEnvironment.openSecondaryDatabase(null, "indexEntityPart", entityDatabase, 
		    		mySecConfig);
			
			idCalculation = myDbEnvironment.openDatabase(null, "calcuDatabase", dbconf);
			idDecision = myDbEnvironment.openDatabase(null, "decisionDatabase", dbconf);
			idAction = myDbEnvironment.openDatabase(null, "actionDatabase", dbconf);
			
			inst_idCalculation = myDbEnvironment.openDatabase(null, "inst_idCalculation", dbconf);
			inst_idDecision = myDbEnvironment.openDatabase(null, "inst_idDecision", dbconf);
			inst_idAction = myDbEnvironment.openDatabase(null, "inst_idAction", dbconf);
			
			fCalculation = myDbEnvironment.openDatabase(null, "fcalcuDatabase", dbconf);
			fDecision = myDbEnvironment.openDatabase(null, "fdecisionDatabase", dbconf);
			fAction = myDbEnvironment.openDatabase(null, "factionDatabase", dbconf);
			
			mCalculation = myDbEnvironment.openDatabase(null, "mCalculation", dbconf);
			mDecision = myDbEnvironment.openDatabase(null, "mDecision", dbconf);
			mAction = myDbEnvironment.openDatabase(null, "mAction", dbconf);
			
			fCalculationDef = myDbEnvironment.openDatabase(null, "fCalculationDef", dbconf);
			mySecConfig.setKeyCreator(new DefinitionFunctionKeyCreater());
			indexfCalculationDef = myDbEnvironment.openSecondaryDatabase(null, "indexfCalculationDef", fCalculationDef, 
					mySecConfig);
			
			fActionDef = myDbEnvironment.openDatabase(null, "fActionDef", dbconf);
			mySecConfig.setKeyCreator(new DefinitionFunctionKeyCreater());
			indexfActionDef = myDbEnvironment.openSecondaryDatabase(null, "indexfActionDef", fActionDef, 
					mySecConfig);
			
			fActionInst = myDbEnvironment.openDatabase(null, "fActionInst", dbconf);
			mySecConfig.setKeyCreator(new InstanceFunctionKeyCreater());
			indexfActionInst = myDbEnvironment.openSecondaryDatabase(null, "indexfActionInst", fActionInst, 
					mySecConfig);
			
			
			userDatabase = myDbEnvironment.openDatabase(null, "userDatabase", dbconf);
			groupDatabase = myDbEnvironment.openDatabase(null, "groupDatabase", dbconf);
			roleDatabase = myDbEnvironment.openDatabase(null, "roleDatabase", dbconf);
			
			mapUserGroup = myDbEnvironment.openDatabase(null, "userGroup", dbconf);
			
			defInDef = myDbEnvironment.openDatabase(null, "defInDef", dbconf);
			propInDef = myDbEnvironment.openDatabase(null, "propInDef", dbconf);
			
			MyKeyCreator keyCreator = new MyKeyCreator();
			mySecConfig.setKeyCreator(keyCreator);
		    String secDbName = "mySecondaryDatabase";
		    mySecDb = myDbEnvironment.openSecondaryDatabase(null, secDbName, myDatabase, 
		                                          mySecConfig);
		    
		    mySecConfig.setKeyCreator(new DefinitionSignKeyCreater());
		    indexDefinitionSign = myDbEnvironment.openSecondaryDatabase(null, "indexDefinitionSign", defDatabase, 
		    		mySecConfig);
//		    SecondaryConfig typeSecConfig = new SecondaryConfig();
//		    typeSecConfig.setAllowCreate(true);
//		    typeSecConfig.setSortedDuplicates(true);
		    mySecConfig.setKeyCreator(new InstanceTypeKeyCreater());
		    indexInstType = myDbEnvironment.openSecondaryDatabase(null, "instTypeIndex", entityDatabase, 
		    		mySecConfig);
		    
//		    SecondaryConfig propSecConfig = new SecondaryConfig();
//		    propSecConfig.setAllowCreate(true);
//		    propSecConfig.setSortedDuplicates(true);
		    mySecConfig.setKeyCreator(new PropertyTypeKeyCreater());
		    indexPropDef = myDbEnvironment.openSecondaryDatabase(null, "typePropIndex", idLink, 
		    		mySecConfig);
		    
//		    SecondaryConfig assignSecConfig = new SecondaryConfig();
//		    assignSecConfig.setAllowCreate(true);
//		    assignSecConfig.setSortedDuplicates(true);
		    mySecConfig.setKeyCreator(new AssignmentIDKeyCreater());
		    indexAssignHost = myDbEnvironment.openSecondaryDatabase(null, "instAssignHost", idAssign, 
		    		mySecConfig);
		    indexJudgeHost = myDbEnvironment.openSecondaryDatabase(null, "indexJudgeHost", judgeDatabase, 
		    		mySecConfig);
		    indexSwitchHost = myDbEnvironment.openSecondaryDatabase(null, "indexSwitchHost", switchResult, 
		    		mySecConfig);
		    
		    rightIdAction = myDbEnvironment.openDatabase(null, "rightIdAction", dbconf);
		    rightIdCalculation = myDbEnvironment.openDatabase(null, "rightIdCalculation", dbconf);
//		    mySecConfig.setKeyCreator(new IdActionUserKeyCreater());
//		    indexIdActionUser = myDbEnvironment.openSecondaryDatabase(null, "indexIdActionUser", rightIdAction, 
//		    		mySecConfig);
		    
		    closinIdCalculation = myDbEnvironment.openDatabase(null, "closeIdCalculation", dbconf);
		    closinIdAction = myDbEnvironment.openDatabase(null, "closinIdAction", dbconf);
		    
		    mySecConfig.setKeyCreator(new MultiFunctionKeyCreater());
		    indexfCalculation = myDbEnvironment.openSecondaryDatabase(null, "indexfCalculation", fCalculation, 
		    		mySecConfig);
		    indexfDecision = myDbEnvironment.openSecondaryDatabase(null, "indexfDecision", fDecision, 
		    		mySecConfig);
		    indexfAction = myDbEnvironment.openSecondaryDatabase(null, "indexfAction", fAction, 
		    		mySecConfig);
		    
		    mySecConfig.setKeyCreator(new UserGroupKeyCreater1());
		    indexUserGroup = myDbEnvironment.openSecondaryDatabase(null, "indexUserGroup", mapUserGroup, 
		    		mySecConfig);
		    mySecConfig.setKeyCreator(new UserGroupKeyCreater2());
		    indexGroupUser = myDbEnvironment.openSecondaryDatabase(null, "indexGroupUser", mapUserGroup, 
		    		mySecConfig);
		    
		    childEntity = myDbEnvironment.openDatabase(null, "childEntity", dbconf);
		    mySecConfig.setKeyCreator(new RelationEntityHostKeyCreater());
		    indexHostChild = myDbEnvironment.openSecondaryDatabase(null, "indexHostChild", childEntity, 
		    		mySecConfig);
		    
		    //每一个entity有且只有一个名字，其它形式的引用通过refer实现。
		    nameDatabase = myDbEnvironment.openDatabase(null, "nameDatabase", dbconf);
		    mySecConfig.setKeyCreator(new InstanceNameKeyCreater());
		    indexInstanceName = myDbEnvironment.openSecondaryDatabase(null, "indexInstanceName", nameDatabase, 
		    		mySecConfig);
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
	}
	
	public SecondaryConfig getMySecConfig() {
		return mySecConfig;
	}

	public void setMySecConfig(SecondaryConfig mySecConfig) {
		this.mySecConfig = mySecConfig;
	}

	public void close(){
		try {			
			if (mySecDb != null) {
				mySecDb.close();
			}
			if (indexInstanceName != null) {
				indexInstanceName.close();
			}
			if (nameDatabase != null) {
				nameDatabase.close();
			}
			if (indexHostChild != null) {
				indexHostChild.close();
			}
			if (childEntity != null) {
				childEntity.close();
			}
			
			if (indexGroupUser != null) {
				indexGroupUser.close();
			}
			if (indexUserGroup != null) {
				indexUserGroup.close();
			}
			if (indexfCalculation != null) {
				indexfCalculation.close();
			}
			if (indexfDecision != null) {
				indexfDecision.close();
			}
			if (indexfAction != null) {
				indexfAction.close();
			}
			
			if (closinIdAction != null) {
				closinIdAction.close();
			}
			
			if (closinIdCalculation != null) {
				closinIdCalculation.close();
			}
			
			if (indexIdActionUser != null) {
				indexIdActionUser.close();
			}
			
			if (rightIdAction != null) {
				rightIdAction.close();
			}
			
			if (rightIdCalculation != null) {
				rightIdCalculation.close();
			}
			
			if (indexDefinitionSign != null) {
				indexDefinitionSign.close();
			}
			if (indexInstType != null) {
				indexInstType.close();
			}
			
			if (indexPropDef != null) {
				indexPropDef.close();
			}
			if (indexAssignHost != null) {
				indexAssignHost.close();
			}
			if (indexJudgeHost != null) {
				indexJudgeHost.close();
			}
			if (indexSwitchHost != null) {
				indexSwitchHost.close();
			}
			if (fCalculation != null) {
				fCalculation.close();
			}
			
			if (fDecision != null) {
				fDecision.close();
			}
			
			if (fAction != null) {
				fAction.close();
			}
			
			if (mCalculation != null) {
				mCalculation.close();
			}
			
			if (mDecision != null) {
				mDecision.close();
			}
			
			if (mAction != null) {
				mAction.close();
			}
			if (indexfCalculationDef != null) {
				indexfCalculationDef.close();
			}
			if (fCalculationDef != null) {
				fCalculationDef.close();
			}
			
			if (indexfCalculationInst != null) {
				indexfCalculationInst.close();
			}
			if (fCalculationInst != null) {
				fCalculationInst.close();
			}
			
			if (indexfActionDef != null) {
				indexfActionDef.close();
			}
			if (fActionDef != null) {
				fActionDef.close();
			}
			
			if (indexfActionInst != null) {
				indexfActionInst.close();
			}
			if (fCalculationInst != null) {
				fCalculationInst.close();
			}
			
			if (idCalculation != null) {
				idCalculation.close();
			}
			
			if (idDecision != null) {
				idDecision.close();
			}
			
			if (idAction != null) {
				idAction.close();
			}			
			if (inst_idCalculation != null) {
				inst_idCalculation.close();
			}			
			if (inst_idDecision != null) {
				inst_idDecision.close();
			}			
			if (inst_idAction != null) {
				inst_idAction.close();
			}
			
			if (myDatabase != null) {
				myDatabase.close();
			}

			if (processDB != null) {
				processDB.close();
			}
			
			if (refers != null) {
				refers.close();
			}
			if (decides != null) {
				decides.close();
			}
			
			if (variables != null) {
				variables.close();
			}
			
			if (judgeDatabase != null) {
				judgeDatabase.close();
			}
			
			if (idState != null) {
				idState.close();
			}
			
			if (actionDatabase != null) {
				actionDatabase.close();
			}
			
			if (switchDef != null) {
				switchDef.close();
			}
			
			if (switchResult != null) {
				switchResult.close();
			}
			
			if (idLink != null) {
				idLink.close();
			}
			
			if (indexFunctionLink != null) {
				indexFunctionLink.close();
			}
			
			if (functionLink != null) {
				functionLink.close();
			}
			
			if (indexFunctionState != null) {
				indexFunctionState.close();
			}
			
			if (functionState != null) {
				functionState.close();
			}
			
			if (indexFunctionAction != null) {
				indexFunctionAction.close();
			}
			
			if (functionAction != null) {
				functionAction.close();
			}
			
			if (mappingLink != null) {
				mappingLink.close();
			}
			
			if (listAssignDatabase != null) {
				listAssignDatabase.close();
			}
			
			if (idAssign != null) {
				idAssign.close();
			}
			
			if (functionAssign != null) {
				functionAssign.close();
			}
			
			if (defDatabase != null) {
				defDatabase.close();
			}
			
			if (userDatabase != null) {
				userDatabase.close();
			}
			
			if (groupDatabase != null) {
				groupDatabase.close();
			}
			
			if (roleDatabase != null) {
				roleDatabase.close();
			}
			
			if (mapUserGroup != null) {
				mapUserGroup.close();
			}
			
			if (indexEntityPart != null) {
				indexEntityPart.close();
			}
			
			if (entityDatabase != null) {
				entityDatabase.close();
			}
			
			if (defInDef != null) {
				defInDef.close();
			}
			
			if (propInDef != null) {
				propInDef.close();
			}
			
			if (spaceExclude != null) {
				spaceExclude.close();
			}
			
			if (messageDB != null) {
				messageDB.close();
			}

			if (myDbEnvironment != null) {
				myDbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			dbe.printStackTrace();
		} 
	}
	
	/**
	 * every time database start, it will be initialized.
	 */
	public void init() throws IOException, DatabaseException, ApplicationException, Exception{
		Transaction txn = beginTransaction();	
		try {
			boolean exist = hasUser("admin", null);
			if(!exist){
				User admin = new User();
				admin.setUserid("admin");
//				admin.setUserName("admin");
				admin.setPassword("admin");
				this.putUser(admin, txn);
//				HiwiiInstance inst = EntityUtil.userToInstance(admin);
//						
//				String id = this.putInstance(inst, txn);
//				this.putInstanceName(admin.getUserid(), id, txn);
				txn.commit();
				//lock,unlock,lockRemote等初始状态是lock的，且不能unlock。
				//admin用户可以把这些方法的lock权限赋予其它用户。
			}
		} catch (Exception e) {
			System.out.println("user init err!");
			return;
		} finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
	}
	public Transaction beginTransaction(){
		return myDbEnvironment.beginTransaction(null, null);
	}
	
	public void insert(byte[] key, byte[] data){
		try {
		    DatabaseEntry theKey = new DatabaseEntry(key);
		    DatabaseEntry theData = new DatabaseEntry(data);
		    myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
		    // Exception handling goes here
		}
	}
	
	public void insert(String key, byte[] data){
		try {
		    DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		    DatabaseEntry theData = new DatabaseEntry(data);
		    myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
		    // Exception handling goes here
		}
	}
	
	public void insert(String key, String data){
		try {
		    DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		    DatabaseEntry theData = new DatabaseEntry(data.getBytes("UTF-8"));
		    myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
		    // Exception handling goes here
		}
	}
	
	public byte[] getRecord(byte[] key) throws Exception{
		try {
			// Create a pair of DatabaseEntry objects. theKey
			// is used to perform the search. theData is used
			// to store the data returned by the get() operation.
			DatabaseEntry theKey = new DatabaseEntry(key);
			DatabaseEntry theData = new DatabaseEntry();

			// Perform the get.
			if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {

				// Recreate the data String.
				byte[] retData = theData.getData();
				return retData;
			} else {
				return null;
			} 
		} catch (Exception e) {
			throw new Exception("error!!");
		}
	}
	
	public byte[] getRecord(String key) throws Exception{
		try {
			// Create a pair of DatabaseEntry objects. theKey
			// is used to perform the search. theData is used
			// to store the data returned by the get() operation.
			DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry();

			// Perform the get.
			if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {

				// Recreate the data String.
				byte[] retData = theData.getData();
				return retData;
			} else {
				return null;
			} 
		} catch (Exception e) {
			throw new Exception("error!!");
		}
	}
	
	public void deleteRecord(String key) throws Exception{
		try {
		    String aKey = "myFirstKey";
		    DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
		    
		    // Perform the deletion. All records that use this key are
		    // deleted.
		    myDatabase.delete(null, theKey); 
		} catch (Exception e) {
		    // Exception handling goes here
		}
	}
	
	public void deleteRecord(byte[] key) throws Exception{
		try {
		    DatabaseEntry theKey = new DatabaseEntry(key);
		    
		    // Perform the deletion. All records that use this key are
		    // deleted.
		    myDatabase.delete(null, theKey); 
		} catch (Exception e) {
		    // Exception handling goes here
		}
	}
	
	//暂时使用decides database
	public void openSpace()throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry("openSpace".getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry("true".getBytes("UTF-8"));
		decides.put(null, theKey, theData);
	}
	public void closeSpace()throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry("openSpace".getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry("false".getBytes("UTF-8"));
		decides.put(null, theKey, theData);
	}
	
	public boolean isOpenSpace()throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry("openSpace".getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry("false".getBytes("UTF-8"));
		OperationStatus status = decides.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	byte[] retData = theData.getData();
	        String res = new String(retData, "UTF-8");
	        if(res.equals("true")) {
	        	return true;
	        }else {
	        	return false;
	        }
	    }else{ // if(status == OperationStatus.NOTFOUND)
	    	return true;
	    }
	}
	/**
	 * key:definition name
	 * index:definition signature
	 * @param def
	 * @param txn
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public void putDefinition(Definition def, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{	
		TupleBinding<Definition> dataBinding = new DefinitionBinding();	
		DatabaseEntry theKey = new DatabaseEntry(def.getName().getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		
		dataBinding.objectToEntry(def, theData);
		try {
			OperationStatus status = defDatabase.get(null, theKey, theData, LockMode.DEFAULT);
			if(status == OperationStatus.SUCCESS){
				throw new ApplicationException();
			}
			defDatabase.put(txn, theKey, theData);
			
			for(Property prop:def.getProps().values()){
				//update def.takeSignature() to getName. at 20160920
				String key = prop.getName() + "@" + def.getName();
				DatabaseEntry propkey = new DatabaseEntry(key.getBytes("UTF-8"));
				DatabaseEntry propdata = new DatabaseEntry();
				TupleBinding<Property> propBinding = new PropertyBinding();
				propBinding.objectToEntry(prop, propdata);
				idLink.put(txn, propkey, propdata);
			}
			
			for(String str:def.getStates()){
				String key = str + "@" + def.getName();
				DatabaseEntry propkey = new DatabaseEntry(key.getBytes("UTF-8"));
				DatabaseEntry propdata = new DatabaseEntry(str.getBytes("UTF-8"));
				idState.put(txn, propkey, propdata);
			}			
		} catch (Exception e) {
			throw new ApplicationException();
		}
		LocalHost.getInstance().getDefPool().put(def);		
	}
	
	public Definition getDefinitionByName(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = LocalHost.getInstance().getDefPool().getByName(name);
		if(def != null){
			return def;
		}
		if(SystemDefinition.contains(name)){
			def = SystemDefinition.defs.get(name);
			return def;
		}
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = defDatabase.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	TupleBinding<Definition> dataBinding = new DefinitionBinding();
	    	def = dataBinding.entryToObject(theData);
	    }else{ // if(status == OperationStatus.NOTFOUND)
	    	return null;
	    }
//	    String sign = def.takeSignature();
	    SecondaryCursor cursor = null;
	    DatabaseEntry skey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
		try {
			cursor = indexPropDef.openCursor(null, null);
			OperationStatus retVal = cursor.getSearchKey(skey, key, data, LockMode.DEFAULT);
			TupleBinding<Property> dataBinding = new PropertyBinding();		
			while(retVal == OperationStatus.SUCCESS){
				Property prop = dataBinding.entryToObject(data);
				def.getProps().put(prop.getName(), prop);
				retVal = cursor.getNextDup(skey, key, data, LockMode.DEFAULT);
			}
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		LocalHost.getInstance().getDefPool().put(def);
	    return def;
	}
	
	public Definition getDefinitionBySign(String sign)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = null;
		if(SystemDefinition.idname.containsKey(sign)){
			String name = SystemDefinition.idname.get(sign);
			def = SystemDefinition.defs.get(name);
			return def;
		}
		def = LocalHost.getInstance().getDefPool().getBySignature(sign);
		if(def != null){
			return def;
		}
		DatabaseEntry skey = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

	    SecondaryCursor defcur = null;
	    SecondaryCursor cursor = null;
	   
		try {
			defcur = indexDefinitionSign.openCursor(null, null);
		    OperationStatus status = defcur.getSearchKey(skey, key, data, LockMode.DEFAULT);
		    if(status == OperationStatus.SUCCESS){
		    	TupleBinding<Definition> dataBinding = new DefinitionBinding();
		    	def = dataBinding.entryToObject(data);
		    }else{ // if(status == OperationStatus.NOTFOUND)
		    	return null;
		    }
		    
			cursor = indexPropDef.openCursor(null, null);
			OperationStatus retVal = cursor.getSearchKey(skey, key, data, LockMode.DEFAULT);
			TupleBinding<Property> dataBinding = new PropertyBinding();		
			while(retVal == OperationStatus.SUCCESS){
				Property prop = dataBinding.entryToObject(data);
				def.getProps().put(prop.getName(), prop);
				retVal = cursor.getNextDup(skey, key, data, LockMode.DEFAULT);
			}
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (defcur != null) {
					defcur.close();
				}
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		LocalHost.getInstance().getDefPool().put(def);
	    return def;
	}
	
	public String getDefinitionSign(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = null;
		if(SystemDefinition.contains(name)){
			def = SystemDefinition.defs.get(name);
			return def.getSignature();
		}
		def = LocalHost.getInstance().getDefPool().getByName(name);
		if(def != null){
			return def.getSignature();
		}
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = defDatabase.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	TupleBinding<Definition> dataBinding = new DefinitionBinding();
	    	def = dataBinding.entryToObject(theData);
	    	return def.getSignature();
	    }else{
	    	return null;
	    }
	}
	
	public String getDefinitionName(String sign)
			throws IOException, DatabaseException, ApplicationException, Exception{
		if(SystemDefinition.idname.containsKey(sign)){
			return SystemDefinition.idname.get(sign);
		}
		Definition def = LocalHost.getInstance().getDefPool().getBySignature(sign);
		if(def != null){
			return def.getName();
		}
		DatabaseEntry skey = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

	    SecondaryCursor defcur = null;
	   
		try {
			defcur = indexDefinitionSign.openCursor(null, null);
		    OperationStatus status = defcur.getSearchKey(skey, key, data, LockMode.DEFAULT);
		    if(status == OperationStatus.SUCCESS){
		    	TupleBinding<Definition> dataBinding = new DefinitionBinding();
		    	def = dataBinding.entryToObject(data);
		    	return def.getName();
		    }else{ 
		    	return null;
		    }
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (defcur != null) {
					defcur.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public void deleteDefinitionByName(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
		OperationStatus status = defDatabase.delete(null, theKey);
		if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		LocalHost.getInstance().getDefPool().remove(name);
	}
	
	public void putChildInstance(HiwiiInstance inst, Entity host, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = EntityUtil.getUUID();// + "@" + hostId;
		String sign = "";
		Definition def = null;
		if(inst.getClassName() != null){
			def = EntityUtil.proxyGetDefinition(inst.getClassName());
			sign = def.getSignature();
		}else{
			throw new ApplicationException();
		}
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(sign.getBytes("UTF-8"));
	    
	    Definition hostDef = EntityUtil.proxyGetDefinition(host.getClassName());
	    if(hostDef == null){
	    	throw new ApplicationException();
	    }
	    String hsign = hostDef.getSignature();
	    if(!StringUtil.matched(sign, hsign)){
	    	throw new ApplicationException();
	    }
//	    int max = hostDef.getPartNumber(sign);
	    //assume entity partNumber = 0
	    
	    //"User" is signature of UserDefinition
	    if(StringUtil.matched(sign, "User")){
	    	Assignment ass = inst.getAssignments().get("id");
	    	if(ass == null){
	    		throw new ApplicationException("id!");
	    	}
	    	String userid = ((StringExpression)ass.getValue()).getValue();
	    	DatabaseEntry usrKey = new DatabaseEntry(userid.getBytes("UTF-8"));
	    	DatabaseEntry data = new DatabaseEntry();
	    	OperationStatus retVal = userDatabase.get(null, usrKey, data, LockMode.DEFAULT);
	    	if(retVal == OperationStatus.NOTFOUND){
	    		userDatabase.put(txn, usrKey, theKey);
	    	}else if(retVal == OperationStatus.SUCCESS){
	    		throw new ApplicationException("user existed!");
	    	}
	    }

		entityDatabase.put(txn, theKey, theData);
		
		try {
			TupleBinding<StoredValue> dataBinding = new ValueBinding();
			for(Assignment ass:inst.getAssignments().values()){
				String key0 = ass.getName() + "@" + key;
				DatabaseEntry asskey = new DatabaseEntry(key0.getBytes("UTF-8"));
				DatabaseEntry assdata = new DatabaseEntry();
				StoredValue rec = EntityUtil.entityToRecord(ass.getValue());
				dataBinding.objectToEntry(rec, assdata);
				idAssign.put(txn, asskey, assdata);
			}
		} catch (Exception e) {
			throw new ApplicationException();
		}
	}
	
	public int howManyChild(String uid, String childType){
		return 0;
	}
	
	public String putChildEntity(String parentId, Entity child, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = parentId + "%" + EntityUtil.getUUID();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		
		StoredValue rec = EntityUtil.entityToRecord(child);
		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		dataBinding.objectToEntry(rec, theData);
	    
		childEntity.put(txn, theKey, theData);

		return key;
	}
	
	public String putInstance(String defname, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = EntityUtil.getUUID();  //new key
		String sign = "";
		Definition def = EntityUtil.proxyGetDefinition(defname);
		if(def == null) {
			throw new ApplicationException();
		}
		sign = def.getSignature();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(sign.getBytes("UTF-8"));

		entityDatabase.put(txn, theKey, theData);

		return key;
	}
	
	public String putInstance(Definition def, List<Expression> content, Transaction txn, HiwiiContext  context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = EntityUtil.getUUID();  //new key
		String sign = "";
		sign = def.getSignature();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(sign.getBytes("UTF-8"));

		entityDatabase.put(txn, theKey, theData);
		for(Expression expr:content) {
			if(expr instanceof BinaryOperation){
				BinaryOperation bo = (BinaryOperation) expr;
				Expression left = bo.getLeft();
				Expression right = bo.getRight();

				if(bo.getOperator().equals(":=")){
					Entity val = context.doCalculation(right);
					if(val instanceof HiwiiException){
						throw new ApplicationException();
					}
					//property set
					if(left instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) left;
						putIdAssignment(key, ie.getName(), val, txn);
					}else{
						throw new ApplicationException();
					}
				}else if(bo.getOperator().equals("::")){
					Expression ret = context.doDecision(right);
					if(!(ret instanceof JudgmentResult)) {
						throw new ApplicationException();
					}
					JudgmentResult jr = (JudgmentResult) ret;
					if(left instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) left;
						turnIdJudgment(key, ie.getName(), jr, txn);
					}else{
						throw new ApplicationException();
					}
				}
			}else{
				throw new ApplicationException();
			}
		}

		return key;
	}
	
	public String putInstance(HiwiiInstance inst, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = null;  //new key
		if(inst.getUuid() == null) {
			key = EntityUtil.getUUID();
		}else {
			key = inst.getUuid();
		}
		String sign = "";
		if(inst.getClassName() != null){
			Definition def = EntityUtil.proxyGetDefinition(inst.getClassName());
			sign = def.getSignature();
		}else{
			throw new ApplicationException();
		}
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(sign.getBytes("UTF-8"));

		entityDatabase.put(txn, theKey, theData);
		
		try {
			TupleBinding<StoredValue> dataBinding = new ValueBinding();
			for(Assignment ass:inst.getAssignments().values()){
				Entity value = ass.getValue();
				if(value instanceof EntityList){
					//必须判断是否instanceList
					EntityList list = (EntityList) value;
					String id = EntityUtil.getUUID();
					String key0 = ass.getName() + "@" + key + "%" + id;
					DatabaseEntry asskey = new DatabaseEntry(key0.getBytes("UTF-8"));
					DatabaseEntry assdata = new DatabaseEntry();
					StoredValue rec = EntityUtil.entityToRecord(ass.getValue());
					dataBinding.objectToEntry(rec, assdata);
					idAssign.put(txn, asskey, assdata);
					for(Entity ent:list.getItems()){
						Assignment item = new Assignment();
						item.setValue(ent);
						idAssign.put(txn, asskey, assdata);
					}					
				}else{
					String key0 = ass.getName() + "@" + key;
					DatabaseEntry asskey = new DatabaseEntry(key0.getBytes("UTF-8"));
					DatabaseEntry assdata = new DatabaseEntry();
					StoredValue rec = EntityUtil.entityToRecord(ass.getValue());
					dataBinding.objectToEntry(rec, assdata);
					idAssign.put(txn, asskey, assdata);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException();
		}
		return key;
	}
	
	public HiwiiInstance getInstanceByName(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
//		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    OperationStatus retVal = nameDatabase.get(null, theKey, data, LockMode.DEFAULT);
	    if(retVal == OperationStatus.SUCCESS){
	    	String id = new String(data.getData(), "UTF-8");
	    	return getInstanceById(id);
	    }
	    return null;
	}
	public HiwiiInstance getInstanceById(String id)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(id.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor curasg = null;
	    SecondaryCursor curjdg = null;
		try {
			OperationStatus retVal = entityDatabase.get(null, theKey, data, LockMode.DEFAULT);
					
			if(retVal == OperationStatus.SUCCESS){
				String sig = new String(data.getData(), "UTF-8");
				Definition def = getDefinitionBySign(sig);
				if(def == null) {
					throw new ApplicationException("definition is null!");
				}
				String type = def.getName();
				HiwiiInstance ret = new HiwiiInstance();
				ret.setClassName(type);
				ret.setUuid(id);
				
				theKey = new DatabaseEntry(ret.getUuid().getBytes("UTF-8"));				
				OperationStatus status = indexInstanceName.get(null, theKey, key, data, LockMode.DEFAULT);
				if(status == OperationStatus.SUCCESS){
					String name0 = new String(key.getData(), "UTF-8");
					ret.setName(name0);
				}
				
				List<Assignment> asslist = getInstanceAssignments(ret.getUuid());
				for(Assignment ass:asslist) {
					ret.getAssignments().put(ass.getName(), ass);
				}
				
				NavigableMap<String,JudgmentResult> judges = getInstanceJudgments(ret.getUuid());
				ret.setJudgments(judges);
				
				NavigableMap<String,String> switches = getInstanceSwitches(ret.getUuid());
				ret.setSwitches(switches);
				return ret;
			}else{
				throw new ApplicationException();
			}
			
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (curasg != null) {
					curasg.close();
				}
				if (curjdg != null) {
					curjdg.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public Definition getInstanceClassName(String id)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(id.getBytes("UTF-8"));
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor curasg = null;
	    SecondaryCursor curjdg = null;
		try {
			OperationStatus retVal = entityDatabase.get(null, theKey, data, LockMode.DEFAULT);
					
			if(retVal == OperationStatus.SUCCESS){
				String sig = new String(data.getData(), "UTF-8");
				Definition def = getDefinitionBySign(sig);
				if(def == null) {
					throw new ApplicationException("definition is null!");
				}
				return def;
			}else {
				throw new ApplicationException("definition is null!");
			}
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (curasg != null) {
					curasg.close();
				}
				if (curjdg != null) {
					curjdg.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	
	/**
	 * 
	 * @param uid
	 * @param limits
	 * @return
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public EntityList getListProperty(String uid, List<Expression> limits)
			throws IOException, DatabaseException, ApplicationException, Exception{
		EntityList list = new EntityList();
		DatabaseEntry key = new DatabaseEntry(uid.getBytes("UTF-8"));
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<StoredValue> binding = new ValueBinding();
	    Cursor cursor = null;
	    try {
	    	cursor = listAssignDatabase.openCursor(null, null);
	    	OperationStatus ret = cursor.getSearchKeyRange(key, data, LockMode.DEFAULT);
	    	while(ret == OperationStatus.SUCCESS){
	    		String fkey = new String(key.getData(), "UTF-8");
	    		if(!StringUtil.matched(fkey, uid)){
	    			break;
	    		}
	    		StoredValue rec = binding.entryToObject(data);
	    		if(rec.getType() == 's'){
	    			Expression expr = StringUtil.parseString(rec.getValue());
	    			SessionContext sc = LocalHost.getInstance().newSessionContext();
	    			Entity ent = sc.doCalculation(expr);
	    			if(ent instanceof HiwiiException){
	    				throw new ApplicationException();
	    			}
	    			if(ent != null){
	    				list.add(ent);
	    			}
	    		}else if(rec.getType() == 'i'){
	    			HiwiiInstance inst = getInstanceById(rec.getValue());
	    			if(inst != null){
	    				list.add(inst);
	    			}
	    		}else if(rec.getType() == 'm'){

	    		}
	    		ret = cursor.getNext(key, data, LockMode.DEFAULT);
	    	}
	    } finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		return list;
	}
	
	public HiwiiInstance getSingleInstance(String type, List<Expression> limits, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = getDefinitionByName(type);
		return getSingleInstance(def, limits, context);
	}
	public HiwiiInstance getSingleInstance(Definition def, List<Expression> limits, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String sign0 = def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
		DatabaseEntry uid = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
	    SecondaryCursor cur2 = null;
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			cur2 = indexAssignHost.openCursor(null, null);			
//			TupleBinding<StoredValue> dataBinding = new ValueBinding();	
			String dname, sign; //propName
			while(ret1 == OperationStatus.SUCCESS){
				HiwiiInstance ret = new HiwiiInstance();
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				dname = getDefinitionName(sign);
				ret.setClassName(dname);
				ret.setUuid(new String(key.getData(), "UTF-8"));
				uid = new DatabaseEntry(ret.getUuid().getBytes("UTF-8"));
				
				//TODO instance没有名字，名字不是instance状态，可以删除此段
				OperationStatus status = indexInstanceName.get(null, uid, key, data, LockMode.DEFAULT);
				if(status == OperationStatus.SUCCESS){
					String name0 = new String(key.getData(), "UTF-8");
					ret.setName(name0);
				}
				
				List<Entity> entities = getInstanceChildren(ret.getUuid());
				ret.setEntities(entities);
				List<Assignment> asslist = getInstanceAssignments(ret.getUuid());
				for(Assignment ass:asslist) {
					ret.getAssignments().put(ass.getName(), ass);
				}
				
				NavigableMap<String,JudgmentResult> judges = getInstanceJudgments(ret.getUuid());
				ret.setJudgments(judges);
				
				NavigableMap<String,String> switches = getInstanceSwitches(ret.getUuid());
				ret.setSwitches(switches);
//				if(limits != null){
//					Expression judge = EntityUtil.judgeEntityLimit(ret, limits);
//					if(EntityUtil.judge(judge)){
//						return ret;
//					}
//				}else{
//					return ret;
//				}
				if(limits != null){
					boolean full = true;
					for(Expression expr:limits) {
						Expression judge = context.doDecision(ret, expr);
						if(!EntityUtil.judge(judge)) {
							full = false;
							break;
						}
					}
					if(full){
						return ret;
					}
				}else{
					return ret;
				}
				ret1 = cursor.getNext(theKey, key, data, LockMode.DEFAULT);
				String index = new String(theKey.getData(), "UTF-8");				
				if(!index.equals(sign0)){
					if(!StringUtil.matched(index, sign0)){
						break;
					}
				}
			}
			
			throw new ApplicationException("empty records!");
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
				if (cur2 != null) {
					cur2.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public HiwiiInstance getSingleInstance(String type, List<Expression> limits)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = getDefinitionByName(type);
		return getSingleInstance(def, limits);
	}
	public HiwiiInstance getSingleInstance(Definition def, List<Expression> limits)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String sign0 = def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
		DatabaseEntry uid = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
	    SecondaryCursor cur2 = null;
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			cur2 = indexAssignHost.openCursor(null, null);			
//			TupleBinding<StoredValue> dataBinding = new ValueBinding();	
			String dname, sign; //propName
			while(ret1 == OperationStatus.SUCCESS){
				HiwiiInstance ret = new HiwiiInstance();
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				dname = getDefinitionName(sign);
				ret.setClassName(dname);
				ret.setUuid(new String(key.getData(), "UTF-8"));
				uid = new DatabaseEntry(ret.getUuid().getBytes("UTF-8"));
				
				//TODO instance没有名字，名字不是instance状态，可以删除此段
				OperationStatus status = indexInstanceName.get(null, uid, key, data, LockMode.DEFAULT);
				if(status == OperationStatus.SUCCESS){
					String name0 = new String(key.getData(), "UTF-8");
					ret.setName(name0);
				}
				List<Entity> entities = getInstanceChildren(ret.getUuid());
				ret.setEntities(entities);
				
				List<Assignment> asslist = getInstanceAssignments(ret.getUuid());
				for(Assignment ass:asslist) {
					ret.getAssignments().put(ass.getName(), ass);
				}
				
				NavigableMap<String,JudgmentResult> judges = getInstanceJudgments(ret.getUuid());
				ret.setJudgments(judges);
				
				NavigableMap<String,String> switches = getInstanceSwitches(ret.getUuid());
				ret.setSwitches(switches);
				if(limits != null){
					Expression judge = EntityUtil.judgeEntityLimit(ret, limits);
					if(EntityUtil.judge(judge)){
						return ret;
					}
				}else{
					return ret;
				}
//				if(limits != null){
//					boolean full = true;
//					for(Expression expr:limits) {
//						Expression judge = EntityUtil.judgeEntityLimit(ret, limits);
//						if(!EntityUtil.judge(judge)) {
//							full = false;
//							break;
//						}
//					}
//					if(full){
//						return ret;
//					}
//				}else{
//					return ret;
//				}
				ret1 = cursor.getNext(theKey, key, data, LockMode.DEFAULT);
				String index = new String(theKey.getData(), "UTF-8");				
				if(!index.equals(sign0)){
					if(!StringUtil.matched(index, sign0)){
						break;
					}
				}
			}
			
			throw new ApplicationException("empty records!");
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
				if (cur2 != null) {
					cur2.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public List<Assignment> getInstanceChild(String sign)
			throws IOException, DatabaseException, ApplicationException, Exception{
		return null;
	}
	
	public List<Assignment> getInstanceAssignments(String sign)
			throws IOException, DatabaseException, ApplicationException, Exception{
		List<Assignment> list = new ArrayList<Assignment>();
//		DatabaseEntry theKey = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry uid = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		SecondaryCursor cursor = indexAssignHost.openCursor(null, null);
		
		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		String pname; //propName
		
		try {
			OperationStatus ret2 = cursor.getSearchKey(uid, key, data, LockMode.DEFAULT);
			while(ret2 == OperationStatus.SUCCESS){
				StoredValue rec = dataBinding.entryToObject(data);
				Assignment ass = EntityUtil.recordToAssignment(rec);
				String pkey = new String(key.getData(), "UTF-8");
				int pos = pkey.indexOf('@');
				if(pos > 0){
					pname = pkey.substring(0, pos);
				}else {
					pname = pkey;
				}
				ass.setName(pname);
				list.add(ass);
				ret2 = cursor.getNextDup(uid, key, data, LockMode.DEFAULT);
			}
		}catch(DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		return list;
	}
	
	public NavigableMap<String,String> getInstanceSwitches(String sign)
			throws IOException, DatabaseException, ApplicationException, Exception{
		NavigableMap<String,String> result = new TreeMap<String,String>();
//		DatabaseEntry theKey = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry uid = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		SecondaryCursor cursor = indexSwitchHost.openCursor(null, null);		
		String pname; //propName
		
		try {
			OperationStatus ret2 = cursor.getSearchKey(uid, key, data, LockMode.DEFAULT);
			while(ret2 == OperationStatus.SUCCESS){
				String pkey = new String(key.getData(), "UTF-8");
				int pos = pkey.indexOf('@');
				if(pos > 0){
					pname = pkey.substring(0, pos);
				}else {
					pname = pkey;
				}
				String value = new String(data.getData(), "UTF-8");
				result.put(pname, value);
				ret2 = cursor.getNextDup(uid, key, data, LockMode.DEFAULT);
			}
		}catch(DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		return result;
	}
	
	public NavigableMap<String,JudgmentResult> getInstanceJudgments(String sign)
			throws IOException, DatabaseException, ApplicationException, Exception{
		NavigableMap<String,JudgmentResult> result = new TreeMap<String,JudgmentResult>();
		//		DatabaseEntry theKey = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry uid = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		SecondaryCursor cursor = indexJudgeHost.openCursor(null, null);

		try {
			OperationStatus retVal = cursor.getSearchKey(uid, key, data, LockMode.DEFAULT);
			retVal = cursor.getSearchKey(uid, key, data, LockMode.DEFAULT);
			BooleanBinding boobind = new BooleanBinding();	
			while(retVal == OperationStatus.SUCCESS){
				boolean boo = boobind.entryToObject(data);
				JudgmentResult jdg = EntityUtil.decide(boo);
				String pkey = new String(key.getData(), "UTF-8");
				int pos = pkey.indexOf('@');
				String name = null;
				if(pos > 0){
					name = pkey.substring(0, pos);
				}
				result.put(name, jdg);
				retVal = cursor.getNextDup(uid, key, data, LockMode.DEFAULT);
			}
		}catch(DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		return result;
	}
	
	public List<Entity> getInstanceChildren(String sign)
			throws IOException, DatabaseException, ApplicationException, Exception{
		List<Entity> result = new ArrayList<Entity>();
		//		DatabaseEntry theKey = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry uid = new DatabaseEntry(sign.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		SecondaryCursor cursor = indexHostChild.openCursor(null, null);

		try {
			OperationStatus retVal = cursor.getSearchKey(uid, key, data, LockMode.DEFAULT);
			retVal = cursor.getSearchKey(uid, key, data, LockMode.DEFAULT);
			TupleBinding<StoredValue> dataBinding = new ValueBinding();
			while(retVal == OperationStatus.SUCCESS){
				StoredValue val = dataBinding.entryToObject(data);
				Entity ent = EntityUtil.recordToEntity(val);
				result.add(ent);
				retVal = cursor.getNextDup(uid, key, data, LockMode.DEFAULT);
			}
		}catch(DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		return result;
	}
	
	public Entity getInstanceProperty(String uuid, String pname)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String pkey = pname + "@" + uuid;
		DatabaseEntry key = new DatabaseEntry(pkey.getBytes("UTF-8"));
		DatabaseEntry data = new DatabaseEntry();
		
		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		
		try {
			OperationStatus ret = idAssign.get(null, key, data, LockMode.DEFAULT);
			if(ret == OperationStatus.SUCCESS){
				StoredValue rec = dataBinding.entryToObject(data);
				Assignment ass = EntityUtil.recordToAssignment(rec);
				return ass.getValue();
			}
		}catch(DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; //not found.
	}
	
	public HiwiiInstance getSingleInstanceFields(String type, List<Expression> limits, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = getDefinitionByName(type);
		return getSingleInstance(def, limits, context);
	}
	public HiwiiInstance getSingleInstanceFields(Definition def, List<Expression> limits)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String sign0 = def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
		DatabaseEntry uid = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
	    SecondaryCursor cur2 = null;
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			cur2 = indexAssignHost.openCursor(null, null);			
			TupleBinding<StoredValue> dataBinding = new ValueBinding();	
			String dname, sign, pname; //propName
			while(ret1 == OperationStatus.SUCCESS){
				HiwiiInstance ret = new HiwiiInstance();
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				dname = getDefinitionName(sign);
				ret.setClassName(dname);
				ret.setUuid(new String(key.getData(), "UTF-8"));
				uid = new DatabaseEntry(ret.getUuid().getBytes("UTF-8"));
				
				//TODO instance没有名字，名字不是instance状态，可以删除此段
				OperationStatus status = indexInstanceName.get(null, uid, key, data, LockMode.DEFAULT);
				if(status == OperationStatus.SUCCESS){
					String name0 = new String(key.getData(), "UTF-8");
					ret.setName(name0);
				}
				
				OperationStatus ret2 = cur2.getSearchKey(uid, key, data, LockMode.DEFAULT);
					
				while(ret2 == OperationStatus.SUCCESS){
					StoredValue rec = dataBinding.entryToObject(data);
					Assignment ass = EntityUtil.recordToAssignment(rec);
					String pkey = new String(key.getData(), "UTF-8");
					int pos = pkey.indexOf('@');
					if(pos > 0){
						pname = pkey.substring(0, pos);
					}else {
						pname = pkey;
					}
					ass.setName(pname);
					ret.getAssignments().put(ass.getName(), ass);
					//？Duplicate key
					ret2 = cur2.getNextDup(uid, key, data, LockMode.DEFAULT);
				}
				if(limits != null){
					Expression judge = EntityUtil.judgeEntityLimit(ret, limits);
					if(EntityUtil.judge(judge)){
						return ret;
					}
				}else{
					return ret;
				}
				ret1 = cursor.getNext(theKey, key, data, LockMode.DEFAULT);
				String index = new String(theKey.getData(), "UTF-8");				
				if(!index.equals(sign0)){
					if(!StringUtil.matched(index, sign0)){
						break;
					}
				}
			}
			
			throw new ApplicationException("empty records!");
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
				if (cur2 != null) {
					cur2.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}


	public Entity getMultiInstanceView(String type, List<Expression> fields, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = getDefinitionByName(type);
		String sign0 = def.getSignature();
		if(sign0 == null){
			throw new ApplicationException();
		}
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
//		DatabaseEntry uid = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
	    Cursor cur2 = null;
	    List<ProjectedObject> list = new ArrayList<ProjectedObject>();
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			cur2 = idAssign.openCursor(null, null);	
			TupleBinding<StoredValue> dataBinding = new ValueBinding();	
			String dname, sign;
			MultiObjectList ret = new MultiObjectList();
			ret.setFields(fields);
			while(ret1 == OperationStatus.SUCCESS){
				ProjectedObject ent = new ProjectedObject();
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				dname = def.getName();
				ent.setClassName(dname);
				ent.setUuid(new String(key.getData(), "UTF-8"));
				
				
				if(fields != null){
					List<Entity> values = new ArrayList<Entity>();
					for(Expression field:fields) {
						if(field instanceof IdentifierExpression) {
							IdentifierExpression ie = (IdentifierExpression) field;
							String fname = ie.getName();
							if(def.definedProperty(fname)) {
								String asskey = fname + "@" + ent.getUuid();
								key = new DatabaseEntry(asskey.getBytes("UTF-8"));
								OperationStatus ret2 = cur2.getSearchKey(key, data, LockMode.DEFAULT);
								
								if(ret2 == OperationStatus.SUCCESS){
									StoredValue rec = dataBinding.entryToObject(data);
									Entity obj = EntityUtil.recordToEntity(rec);
									values.add(obj);
								}else {
									values.add(new NullValue());
								}
							}else if(def.definedState(fname)){
								String fkey = fname + "@" + ent.getUuid();
								JudgmentResult val = getJudgment(fkey, null);
								if(val != null) {
									values.add(val);
								}else {
									values.add(new NullValue());
								}
							}else {
								
							}
						}else {
							//field is not identifier.
							
						}						
					}
					ent.setValues(values);
				}else{
					throw new ApplicationException();
				}
//				ret.getValues().add(ent);
				ret1 = cursor.getNext(theKey, key, data, LockMode.DEFAULT);
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				list.add(ent);
			}
			ret.setValues(list);
//			TypedEntityList result = new TypedEntityList();
//			result.setType(type);
//			for(Entity item:list){
//				result.add(item);
//			}
			return ret;
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
				if (cur2 != null) {
					cur2.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public Entity getMultiLimits(String type, List<Expression> fields, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = getDefinitionByName(type);
		String sign0 = def.getSignature();
		if(sign0 == null){
			throw new ApplicationException();
		}
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
//		DatabaseEntry uid = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
	    Cursor cur2 = null;
	    List<ProjectedObject> list = new ArrayList<ProjectedObject>();
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			cur2 = idAssign.openCursor(null, null);	
			TupleBinding<StoredValue> dataBinding = new ValueBinding();	
			String dname, sign;
			MultiObjectList ret = new MultiObjectList();
			ret.setFields(fields);
			while(ret1 == OperationStatus.SUCCESS){
				ProjectedObject ent = new ProjectedObject();
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				dname = def.getName();
				ent.setClassName("Collection");
				ent.setUuid(new String(key.getData(), "UTF-8"));
				
				
				if(fields != null){
					List<Entity> values = new ArrayList<Entity>();
					for(Expression field:fields) {
						if(field instanceof IdentifierExpression) {
							IdentifierExpression ie = (IdentifierExpression) field;
							String fname = ie.getName();
							if(def.definedProperty(fname)) {
								String asskey = fname + "@" + ent.getUuid();
								key = new DatabaseEntry(asskey.getBytes("UTF-8"));
								OperationStatus ret2 = cur2.getSearchKey(key, data, LockMode.DEFAULT);
								
								if(ret2 == OperationStatus.SUCCESS){
									StoredValue rec = dataBinding.entryToObject(data);
									Entity obj = EntityUtil.recordToEntity(rec);
									values.add(obj);
								}else {
									values.add(new NullValue());
								}
							}else if(def.definedState(fname)){
								String fkey = fname + "@" + ent.getUuid();
								JudgmentResult val = getJudgment(fkey, null);
								if(val != null) {
									values.add(val);
								}else {
									values.add(new NullValue());
								}
							}else {
								
							}
						}else {
							//field is not identifier.
							
						}						
					}
					ent.setValues(values);
				}else{
					throw new ApplicationException();
				}
//				ret.getValues().add(ent);
				ret1 = cursor.getNext(theKey, key, data, LockMode.DEFAULT);
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				list.add(ent);
			}
			ret.setValues(list);
//			TypedEntityList result = new TypedEntityList();
//			result.setType(type);
//			for(Entity item:list){
//				result.add(item);
//			}
			return ret;
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
				if (cur2 != null) {
					cur2.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public Entity getMultiLimitView(String type, List<Expression> fields, List<Expression> limits, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = getDefinitionByName(type);
		String sign0 = def.getSignature();
		if(sign0 == null){
			throw new ApplicationException();
		}
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
//		DatabaseEntry uid = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
	    Cursor cur2 = null;
	    List<ProjectedObject> list = new ArrayList<ProjectedObject>();
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			cur2 = idAssign.openCursor(null, null);	
			TupleBinding<StoredValue> dataBinding = new ValueBinding();	
			String dname, sign;
			MultiObjectList ret = new MultiObjectList();
			ret.setFields(fields);
			while(ret1 == OperationStatus.SUCCESS){
				ProjectedObject ent = new ProjectedObject();
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				dname = def.getName();
				ent.setClassName(dname);
				String uuid = new String(key.getData(), "UTF-8");
				ent.setUuid(uuid);
				
				
				if(fields != null){
					List<Entity> values = new ArrayList<Entity>();
					ent.setFields(fields);
					for(Expression field:fields) {
						if(field instanceof IdentifierExpression) {
							IdentifierExpression ie = (IdentifierExpression) field;
							String fname = ie.getName();
							if(def.definedProperty(fname)) {
								String asskey = fname + "@" + ent.getUuid();
								key = new DatabaseEntry(asskey.getBytes("UTF-8"));
								OperationStatus ret2 = cur2.getSearchKey(key, data, LockMode.DEFAULT);
								
								if(ret2 == OperationStatus.SUCCESS){
									StoredValue rec = dataBinding.entryToObject(data);
									Entity obj = EntityUtil.recordToEntity(rec);
									values.add(obj);
								}else {
									values.add(new NullValue());
								}
							}else if(def.definedState(fname)){
								String fkey = fname + "@" + ent.getUuid();
								JudgmentResult val = getJudgment(fkey, null);
								if(val != null) {
									values.add(val);
								}else {
									values.add(new NullValue());
								}
							}else {
								
							}
						}else {
							//field is not identifier.
							
						}						
					}
					ent.setValues(values);
				}else{
					throw new ApplicationException();
				}
				InstanceObject io = new InstanceObject();
				io.setUuid(uuid);
				if(limits != null){
					boolean full = true;
					for(Expression expr:limits) {
						Expression judge = context.doDecision(io, expr);
						if(!EntityUtil.judge(judge)) {
							full = false;
							break;
						}
					}
					if(full){
						list.add(ent);
					}
				}else{
					list.add(ent);
				}
//				ret.getValues().add(ent);
				ret1 = cursor.getNext(theKey, key, data, LockMode.DEFAULT);
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}				
			}
			ret.setValues(list);
//			TypedEntityList result = new TypedEntityList();
//			result.setType(type);
//			for(Entity item:list){
//				result.add(item);
//			}
			return ret;
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
				if (cur2 != null) {
					cur2.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public TypedEntityList getMultiInstance(String type, List<Expression> limits, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String sign0 = getDefinitionSign(type);
		if(sign0 == null){
			return null;
		}
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
	    List<HiwiiInstance> list = new ArrayList<HiwiiInstance>();
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			String sign;
			while(ret1 == OperationStatus.SUCCESS){
				HiwiiInstance ret = new HiwiiInstance();
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				String uuid = new String(key.getData(), "UTF-8");
				ret = getInstanceById(uuid);

				if(limits != null){
					boolean found = true;
					for(Expression exp:limits) {
						Expression judge = context.doDecision(ret, exp);
						if(!EntityUtil.judge(judge)){
							found = false;
							break;
						}
					}
					if(found) {
						list.add(ret);
					}
				}else{
					list.add(ret);
				}
				ret1 = cursor.getNext(theKey, key, data, LockMode.DEFAULT);
				String index = new String(theKey.getData(), "UTF-8");				
				if(!index.equals(sign0)){
					if(!StringUtil.matched(index, sign0)){
						break;
					}
				}
			}
			TypedEntityList result = new TypedEntityList();
			result.setType(type);
			for(HiwiiInstance item:list){
				result.add(item);
			}
			return result;
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public TypedEntityList getMultiInstance(String type, List<Expression> limits)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String sign0 = getDefinitionSign(type);
		if(sign0 == null){
			return null;
		}
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
	    List<HiwiiInstance> list = new ArrayList<HiwiiInstance>();
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			String sign;
			while(ret1 == OperationStatus.SUCCESS){
				HiwiiInstance ret = new HiwiiInstance();
				sign = new String(theKey.getData(), "UTF-8");
				if(!StringUtil.matched(sign, sign0)){
					break;
				}
				String uuid = new String(key.getData(), "UTF-8");
				ret = getInstanceById(uuid);

				if(limits != null){
					for(Expression exp:limits) {
//						Expression judge = context.doDecision(ret, exp);
//						if(EntityUtil.judge(judge)){
//							list.add(ret);
//						}
					}
				}else{
					list.add(ret);
				}
				ret1 = cursor.getNext(theKey, key, data, LockMode.DEFAULT);
				String index = new String(theKey.getData(), "UTF-8");				
				if(!index.equals(sign0)){
					if(!StringUtil.matched(index, sign0)){
						break;
					}
				}
			}
			TypedEntityList result = new TypedEntityList();
			result.setType(type);
			for(HiwiiInstance item:list){
				result.add(item);
			}
			return result;
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public int getTypeCount(Definition def)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String sign0 = def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(sign0.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    SecondaryCursor cursor = null;
		try {
			cursor = indexInstType.openCursor(null, null);

			OperationStatus ret1 = cursor.getSearchKeyRange(theKey, key, data, LockMode.DEFAULT);
			
			if(ret1 == OperationStatus.SUCCESS){
				int count = cursor.count();
				return count;
			}
			return 0;
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}	
	}
	public void deleteSingleInstance(String type, List<Expression> props, List<Expression> states)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = getDefinitionByName(type);
		DatabaseEntry theKey = new DatabaseEntry(def.takeSignature().getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
		SecondaryCursor cursor = null;
		try {
			cursor = indexInstType.openCursor(null, null);
			OperationStatus retVal = cursor.getSearchKeyRange(theKey, theData, 
                    LockMode.DEFAULT);
			if(retVal == OperationStatus.SUCCESS){
				cursor.delete();
			}else{
				throw new ApplicationException();
			}
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public void deleteMultiInstance(String type)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = getDefinitionByName(type);
		DatabaseEntry secKey = new DatabaseEntry(def.takeSignature().getBytes("UTF-8"));
		DatabaseEntry theKey = new DatabaseEntry();
	    DatabaseEntry theData = new DatabaseEntry();
		SecondaryCursor cursor = null;
		SecondaryCursor propcur = null;
		Transaction txn = null;
		try {
			txn = beginTransaction();
			cursor = indexInstType.openCursor(txn, null);
			propcur = indexAssignHost.openCursor(txn, null);
					
			OperationStatus retVal = cursor.getSearchKey(secKey, theKey, theData, 
                    LockMode.DEFAULT);
			
			while(retVal == OperationStatus.SUCCESS){
				String instKey = new String(theKey.getData());
				deleteInstanceProperty(instKey, txn, propcur);
				cursor.delete();
				retVal = cursor.getNextDup(secKey, theKey, theData, LockMode.DEFAULT);
			}
			
			if (cursor != null) {
				cursor.close();
			}
			if (propcur != null) {
				propcur.close();
			}
			txn.commit();
		} catch(DatabaseException e) {
			txn.abort();
			throw new ApplicationException();
		} finally  {			
			
			if (txn != null) {				
				txn = null;
			}
			if (cursor != null) {
				cursor.close();
			}
			if (propcur != null) {
				propcur.close();
			}
		}
	}
	
	public void deleteInstanceProperty(String instKey, Transaction txn, SecondaryCursor cursor)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry secKey = new DatabaseEntry(instKey.getBytes("UTF-8"));
		DatabaseEntry theKey = new DatabaseEntry();
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus retVal = cursor.getSearchKey(secKey, theKey, theData, 
	    		LockMode.DEFAULT);

	    while(retVal == OperationStatus.SUCCESS){
	    	cursor.delete();
	    	retVal = cursor.getNextDup(secKey, theKey, theData, LockMode.DEFAULT);
	    }
	}
	
	public void putVariable(String key, VariableStore var, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = variables.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		TupleBinding<VariableStore> dataBinding = new VariableBinding();
		dataBinding.objectToEntry(var, theData);
		variables.put(txn, theKey, theData);
	}
	
	public void updateVariable(String key, Entity value, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = variables.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.NOTFOUND){
	    	throw new ApplicationException();
		}
		if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		TupleBinding<VariableStore> dataBinding = new VariableBinding();
		VariableStore vs = dataBinding.entryToObject(theData);
		boolean match = EntityUtil.judgeValueToType(value, vs.getType(), vs.getLimits());
		if(!match){
			throw new ApplicationException();
		}
		if(value instanceof HiwiiInstance){
	    	HiwiiInstance inst = (HiwiiInstance) value;
	    	vs.setValueType('i');
	    	vs.setValue(inst.getUuid());
	    }else{
	    	vs.setValueType('s');
	    	vs.setValue(value.toString());
	    }
		dataBinding.objectToEntry(vs, theData);
		variables.put(txn, theKey, theData);
	}
	
	public VariableStore getVariable(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = variables.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.NOTFOUND){
			return null;
		}
		if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		TupleBinding<VariableStore> dataBinding = new VariableBinding();
		VariableStore vs = dataBinding.entryToObject(theData);
		return vs;
	}
	
	/**
	 * TODO Expression type,暂时使用String type
	 * @param key
	 * @param type
	 * @param txn
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public void putProperty(String key, String type, Transaction txn)  
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idLink.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		StringBinding.stringToEntry(type, theData);
		idLink.put(txn, theKey, theData);
	}
//	public void putProperty(String key, Property prop, Transaction txn) ---20190428
//			throws IOException, DatabaseException, ApplicationException, Exception{
//		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
//	    DatabaseEntry theData = new DatabaseEntry();
//
//	    OperationStatus status = idLink.get(null, theKey, theData, LockMode.DEFAULT);
//		if(status == OperationStatus.SUCCESS){
//			throw new ApplicationException();
//		}
//		TupleBinding<Property> dataBinding = new PropertyBinding();
//		dataBinding.objectToEntry(prop, theData);
//		idLink.put(txn, theKey, theData);
//	}
	public void putProperty(Definition def, Property prop, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		//update def.takeSignature() to getName. at 20160920
//		String key = prop.getName() + "@" + def.getName();
		//update def.takeSignature(). at 20181020
		String key = prop.getName() + "@" + def.getSignature();
		putProperty(key, prop.getType(), txn);
	}
	
	public Property getProperty(String pname, String dname, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = pname + "@" + dname;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    OperationStatus status = idLink.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
//	    	TupleBinding<Property> dataBinding = new PropertyBinding();
	    	String type = StringBinding.entryToString(theData);
	    	Property prop = new Property();
	    	prop.setName(pname);
	    	prop.setType(type);
	    	return prop;
	    }else{ // if(status == OperationStatus.NOTFOUND)
	    	return null;
	    }
	}
	public Property getProperty(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idLink.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	String type = StringBinding.entryToString(theData);
	    	Property prop = new Property();
	    	prop.setName(key);
	    	prop.setType(type);
	    	return prop;
	    }else{ // if(status == OperationStatus.NOTFOUND)
	    	return null;
	    }
	}
	
	public Property getLikeProperty(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    Cursor cursor = null;
		cursor = idLink.openCursor(null, null);
		try{
			OperationStatus status = cursor.getSearchKeyRange(theKey, theData, LockMode.DEFAULT);
			if(status == OperationStatus.SUCCESS){
				String key1 = new String(theKey.getData(), "UTF-8");
				if(StringUtil.matched(key1, key)){
					TupleBinding<Property> dataBinding = new PropertyBinding();
			    	return dataBinding.entryToObject(theData);
				}				
			}
		}catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;	   
	}
	
	public void deleteProperty(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idLink.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	idLink.delete(null, theKey);
	    }else{
	    	throw new ApplicationException("do not exist!");
	    }
	}
	
	public void putFunctionLink(FunctionExpression func, String type, Transaction txn)  
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = null;
		key = func.getName() + "#" + func.getArguments().size() + "%" + EntityUtil.getUUID();
		FunctionHead head = new FunctionHead();
		head.setType(type);
		List<String> args = new ArrayList<String>();
		for(Expression exp:func.getArguments()) {
			if(exp instanceof IdentifierExpression) {
				IdentifierExpression ie = (IdentifierExpression) exp;
				Definition def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null) {
					throw new ApplicationException();
				}
				args.add(ie.getName());
			}else {
				throw new ApplicationException();
			}
//			args.add(exp.toString());
		}
		head.setArgumentType(args);
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    List<FunctionHead> result = getFunctionLink(func, txn);
	    if(!(result.size() == 0 || result == null)) {
	    	throw new ApplicationException("has defined function.");
	    }
		TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
		binding.objectToEntry(head, theData);
		functionLink.put(txn, theKey, theData);
	}
	
	public List<FunctionHead> getFunctionLink(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		String str = func.getName() + "#" + func.getArguments().size();
		DatabaseEntry theKey = new DatabaseEntry(str.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
	    List<FunctionHead> result = new ArrayList<FunctionHead>();
		try {
			cursor = indexFunctionLink.openCursor(txn, null);
			OperationStatus found = cursor.getSearchKey(theKey, key, data, LockMode.DEFAULT);
//			String key0 = new String(theKey.getData(), "UTF-8");
	    	while (found == OperationStatus.SUCCESS)  {
				boolean match1 = true, match2 = true;
				FunctionHead head = binding.entryToObject(data);
				List<Expression> args = func.getArguments();
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
					if(!EntityUtil.judgeDefinitionIsAnother(type, head.getArgumentType().get(i))) {
						match1 = false;
						break;
					}
				}
				//需要判定两次。函数定义即不能包容以前，也不能被以前的函数包容。
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
					if(!EntityUtil.judgeDefinitionIsAnother(head.getArgumentType().get(i), type)) {
						match2 = false;
						break;
					}
				}
				
				if(match1 && match2) {
//					String ret = new String(key.getData(), "UTF-8");
					result.add(head);
				}
	    		found = cursor.getNextDup(theKey, key, data, LockMode.DEFAULT);
	    	}
	    	return result;
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public boolean hasFunctionLink(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		List<FunctionHead> result = getFunctionLink(func, txn);
		if(result.size() == 0 || result == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public FunctionHead deleteFunctionLink(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		String str = func.getName() + "#" + func.getArguments().size();
		DatabaseEntry theKey = new DatabaseEntry(str.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
//	    List<FunctionHead> result = new ArrayList<FunctionHead>();
		try {
			cursor = indexFunctionLink.openCursor(txn, null);
			OperationStatus found = cursor.getSearchKey(theKey, key, data, LockMode.DEFAULT);
//			String key0 = new String(theKey.getData(), "UTF-8");
	    	while (found == OperationStatus.SUCCESS)  {
				boolean match = true;
				FunctionHead head = binding.entryToObject(data);
				List<Expression> args = func.getArguments();
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
//					if(!EntityUtil.judgeDefinitionIsAnother(head.getArgumentType().get(i), type)) {
//						match = false;
//						break;
//					}
					if(!head.getArgumentType().get(i).equals(type)) {
						match = false;
						break;
					}
				}
				
				if(match) {
					cursor.delete();
					return head;
				}
	    		found = cursor.getNextDup(theKey, key, data, LockMode.DEFAULT);
	    	}
//	    	return null;
	    	throw new ApplicationException("not found!");
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public String getFunctionLinkKey(String name, List<Entity> args, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		String str = name + "#" + args.size();
		DatabaseEntry theKey = new DatabaseEntry(str.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
		try {
			cursor = indexFunctionLink.openCursor(txn, null);
			OperationStatus found = cursor.getSearchKey(theKey, key, data, LockMode.DEFAULT);
//			String key0 = new String(theKey.getData(), "UTF-8");
//    		if(!str.equals(key0)){
//    			return null;
//    		}
	    	while (found == OperationStatus.SUCCESS)  {
				boolean match = true;
				FunctionHead head = binding.entryToObject(data);
				for(int i=0;i<args.size();i++) {
					if(!EntityUtil.judgeEntityIsDefinition(args.get(i), head.getArgumentType().get(i))) {
						match = false;
						break;
					}
				}
				if(match) {
					String ret = new String(key.getData(), "UTF-8");
					return ret;
				}
	    		found = cursor.getNextDup(theKey, key, data, LockMode.DEFAULT);
	    	}
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
	
	public FunctionHead getFunctionLinkByKey(String linkKey, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry key = new DatabaseEntry(linkKey.getBytes("UTF-8"));
	    DatabaseEntry data = new DatabaseEntry();
	    OperationStatus found = functionLink.get(txn, key, data, LockMode.DEFAULT);
	    if (found == OperationStatus.SUCCESS)  {
	    	TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
	    	FunctionHead head = binding.entryToObject(data);
	    	return head;
	    }
		return null;
	}
	
	/**
	 * declare[Calculation:f(Integer x), expression]
	 * 当类型是Object，可以省略
	 * @param func
	 * @param txn
	 * @return
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public String getFunctionLinkKeyByDeclare(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		String str = func.getName() + "#" + func.getArguments().size();
		DatabaseEntry theKey = new DatabaseEntry(str.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
	    int len = func.getArguments().size();
		try {
			cursor = indexFunctionLink.openCursor(txn, null);
			OperationStatus found = cursor.getSearchKey(theKey, key, data, LockMode.DEFAULT);
			
	    	while (found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		if(!str.equals(key0)){
	    			return null;
	    		}
				boolean match = true;
				FunctionHead head = binding.entryToObject(data);
				for(int i=0;i<len;i++) {
					Expression exp = func.getArguments().get(i);
					BinaryOperation bo = null;
					String type = null;
					if(exp instanceof BinaryOperation) {
						bo = (BinaryOperation) exp;
						if(!bo.getOperator().equals("")) {
							throw new ApplicationException();
						}
						if(!(bo.getLeft() instanceof IdentifierExpression)) {
							throw new ApplicationException();
						}
						IdentifierExpression ie = (IdentifierExpression) bo.getLeft();
						type = ie.getName();
					}else if(exp instanceof IdentifierExpression){
//						IdentifierExpression ie = (IdentifierExpression) exp;变量名
						type = "Object";
					}else {						
						throw new ApplicationException();
					}
					
					if(!EntityUtil.judgeDefinitionIsAnother(type, head.getArgumentType().get(i))) {
						match = false;
					}
				}
				if(match) {
					String ret = new String(key.getData(), "UTF-8");
					return ret;
				}
	    		found = cursor.getNextDup(theKey, key, data, LockMode.DEFAULT);
	    	}
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
	
	public void putFunctionAssign(String name, List<Entity> args, Entity value, Transaction txn)  
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKey(name, args, null);
		if(fkey == null) {
			throw new ApplicationException();
		}
		String hash = StringUtil.hashArgument(args);
//		FunctionHead head = getFunctionLinkByKey(fkey, txn);
		String key = null;
		key = fkey + "^" + hash;
		FunctionAssign ass = getFunctionAssignByKey(key, txn);
		if(ass != null) {
			ass.setValue(value);
		}else {
			ass = new FunctionAssign();
			ass.setName(name);
			ass.setArguments(args);
			ass.setValue(value);
		}

		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    TupleBinding<FunctionAssign> binding = new FunctionAssignBinding();
		binding.objectToEntry(ass, theData);
		functionAssign.put(txn, theKey, theData);
	}
	
	/**
	 * functionAssign的key：
	 * name+"#"args.size+"%"+hashCode
 	 */
	public Entity getFunctionAssign(String name, List<Entity> args, Transaction txn)  
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKey(name, args, null);
		if(fkey == null) {
			throw new ApplicationException();
		}
		String hash = StringUtil.hashArgument(args);
		String key = null;
		key = fkey + "^" + hash;
		FunctionAssign ass = getFunctionAssignByKey(key, txn);
		if(ass != null) {
			return ass.getValue();
		}
		return null;
	}
	
	public FunctionAssign getFunctionAssignByKey(String key, Transaction txn)  
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    OperationStatus found = functionAssign.get(txn, theKey, theData, LockMode.DEFAULT);
	    if(found == OperationStatus.SUCCESS) {
	    	TupleBinding<FunctionAssign> binding = new FunctionAssignBinding();
	    	FunctionAssign ret = binding.entryToObject(theData);
	    	return ret;
	    }
		return null;
	}
	public void putFunctionState(FunctionExpression func, Transaction txn)  
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = null;
		key = func.getName() + "#" + func.getArguments().size() + "%" + EntityUtil.getUUID();
		FunctionHead head = new FunctionHead();
		head.setType("State");
		List<String> args = new ArrayList<String>();
		for(Expression exp:func.getArguments()) {
			if(exp instanceof IdentifierExpression) {
				IdentifierExpression ie = (IdentifierExpression) exp;
				Definition def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null) {
					throw new ApplicationException();
				}
				args.add(ie.getName());
			}else {
				throw new ApplicationException();
			}
//			args.add(exp.toString());
		}
		head.setArgumentType(args);
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    List<FunctionHead> result = getFunctionState(func, txn);
	    if(!(result.size() == 0 || result == null)) {
	    	throw new ApplicationException("has defined function.");
	    }
		TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
		binding.objectToEntry(head, theData);
		functionState.put(txn, theKey, theData);
	}
	
	public List<FunctionHead> getFunctionState(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		String str = func.getName() + "#" + func.getArguments().size();
		DatabaseEntry theKey = new DatabaseEntry(str.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
	    List<FunctionHead> result = new ArrayList<FunctionHead>();
		try {
			cursor = indexFunctionState.openCursor(txn, null);
			OperationStatus found = cursor.getSearchKey(theKey, key, data, LockMode.DEFAULT);
//			String key0 = new String(theKey.getData(), "UTF-8");
	    	while (found == OperationStatus.SUCCESS)  {
				boolean match1 = true, match2 = true;
				FunctionHead head = binding.entryToObject(data);
				List<Expression> args = func.getArguments();
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
					if(!EntityUtil.judgeDefinitionIsAnother(type, head.getArgumentType().get(i))) {
						match1 = false;
						break;
					}
				}
				//需要判定两次。函数定义即不能包容以前，也不能被以前的函数包容。
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
					if(!EntityUtil.judgeDefinitionIsAnother(head.getArgumentType().get(i), type)) {
						match2 = false;
						break;
					}
				}
				
				if(match1 && match2) {
					result.add(head);
				}
	    		found = cursor.getNextDup(theKey, key, data, LockMode.DEFAULT);
	    	}
	    	return result;
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public boolean hasFunctionState(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		List<FunctionHead> result = getFunctionState(func, txn);
		if(result.size() == 0 || result == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public FunctionHead deleteFunctionState(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		String str = func.getName() + "#" + func.getArguments().size();
		DatabaseEntry theKey = new DatabaseEntry(str.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
//	    List<FunctionHead> result = new ArrayList<FunctionHead>();
		try {
			cursor = indexFunctionState.openCursor(txn, null);
			OperationStatus found = cursor.getSearchKey(theKey, key, data, LockMode.DEFAULT);
//			String key0 = new String(theKey.getData(), "UTF-8");
	    	while (found == OperationStatus.SUCCESS)  {
				boolean match = true;
				FunctionHead head = binding.entryToObject(data);
				List<Expression> args = func.getArguments();
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
//					if(!EntityUtil.judgeDefinitionIsAnother(head.getArgumentType().get(i), type)) {
//						match = false;
//						break;
//					}
					if(!head.getArgumentType().get(i).equals(type)) {
						match = false;
						break;
					}
				}
				
				if(match) {
					cursor.delete();
					return head;
				}
	    		found = cursor.getNextDup(theKey, key, data, LockMode.DEFAULT);
	    	}
//	    	return null;
	    	throw new ApplicationException("not found!");
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public void putFunctionAction(FunctionExpression func, Transaction txn)  
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = null;
		key = func.getName() + "#" + func.getArguments().size() + "%" + EntityUtil.getUUID();
		FunctionHead head = new FunctionHead();
		head.setType("Action");
		List<String> args = new ArrayList<String>();
		for(Expression exp:func.getArguments()) {
			if(exp instanceof IdentifierExpression) {
				IdentifierExpression ie = (IdentifierExpression) exp;
				Definition def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null) {
					throw new ApplicationException();
				}
				args.add(ie.getName());
			}else {
				throw new ApplicationException();
			}
		}
		head.setArgumentType(args);
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    List<FunctionHead> result = getFunctionState(func, txn);
	    if(!(result.size() == 0 || result == null)) {
	    	throw new ApplicationException("has defined function.");
	    }
		TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
		binding.objectToEntry(head, theData);
		functionAction.put(txn, theKey, theData);
	}
	
	public List<FunctionHead> getFunctionAction(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		String str = func.getName() + "#" + func.getArguments().size();
		DatabaseEntry theKey = new DatabaseEntry(str.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
	    List<FunctionHead> result = new ArrayList<FunctionHead>();
		try {
			cursor = indexFunctionAction.openCursor(txn, null);
			OperationStatus found = cursor.getSearchKey(theKey, key, data, LockMode.DEFAULT);
//			String key0 = new String(theKey.getData(), "UTF-8");
	    	while (found == OperationStatus.SUCCESS)  {
				boolean match1 = true, match2 = true;
				FunctionHead head = binding.entryToObject(data);
				List<Expression> args = func.getArguments();
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
					if(!EntityUtil.judgeDefinitionIsAnother(type, head.getArgumentType().get(i))) {
						match1 = false;
						break;
					}
				}
				//需要判定两次。函数定义即不能包容以前，也不能被以前的函数包容。
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
					if(!EntityUtil.judgeDefinitionIsAnother(head.getArgumentType().get(i), type)) {
						match2 = false;
						break;
					}
				}
				
				if(match1 && match2) {
					result.add(head);
				}
	    		found = cursor.getNextDup(theKey, key, data, LockMode.DEFAULT);
	    	}
	    	return result;
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public boolean hasFunctionAction(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		List<FunctionHead> result = getFunctionAction(func, txn);
		if(result.size() == 0 || result == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public FunctionHead deleteFunctionAction(FunctionExpression func, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		String str = func.getName() + "#" + func.getArguments().size();
		DatabaseEntry theKey = new DatabaseEntry(str.getBytes("UTF-8"));
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    TupleBinding<FunctionHead> binding = new FunctionHeadBinding();
//	    List<FunctionHead> result = new ArrayList<FunctionHead>();
		try {
			cursor = indexFunctionAction.openCursor(txn, null);
			OperationStatus found = cursor.getSearchKey(theKey, key, data, LockMode.DEFAULT);
//			String key0 = new String(theKey.getData(), "UTF-8");
	    	while (found == OperationStatus.SUCCESS)  {
				boolean match = true;
				FunctionHead head = binding.entryToObject(data);
				List<Expression> args = func.getArguments();
				for(int i = 0;i<args.size();i++) {
					String type = args.get(i).toString();
//					if(!EntityUtil.judgeDefinitionIsAnother(head.getArgumentType().get(i), type)) {
//						match = false;
//						break;
//					}
					if(!head.getArgumentType().get(i).equals(type)) {
						match = false;
						break;
					}
				}
				
				if(match) {
					cursor.delete();
					return head;
				}
	    		found = cursor.getNextDup(theKey, key, data, LockMode.DEFAULT);
	    	}
//	    	return null;
	    	throw new ApplicationException("not found!");
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public void putMappingLink(MappingExpression map, String type, Transaction txn)  
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = null;
		key = map.getName() + "#" + map.getArguments().size();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(type.getBytes("UTF-8"));

	    String result = getMappingLink(map, txn);
	    if(!(result == null || result == "")) {
	    	throw new ApplicationException("has defined mapping.");
	    }
		mappingLink.put(txn, theKey, theData);
	}
	
	public String getMappingLink(MappingExpression map, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = null;
		key = map.getName() + "#" + map.getArguments().size();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    OperationStatus found = mappingLink.get(txn, theKey, theData, LockMode.DEFAULT);
	    if(found == OperationStatus.SUCCESS) {
	    	String type = new String(theData.getData(), "UTF-8");
	    	return type;
	    }
		return null;
	}
	
	public String getMappingLink(String name, List<Expression> args, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = null;
		key = name + "#" + args.size();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    OperationStatus found = mappingLink.get(txn, theKey, theData, LockMode.DEFAULT);
	    if(found == OperationStatus.SUCCESS) {
	    	String type = new String(theData.getData(), "UTF-8");
	    	return type;
	    }
		return null;
	}
	
	public void putAssignment(String key, Assignment ass, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    //如果没有则创建，如果有记录，则覆盖。
		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		StoredValue rec = EntityUtil.entityToRecord(ass.getValue());
		dataBinding.objectToEntry(rec, theData);
		idAssign.put(txn, theKey, theData);
	}
	
	public void putIdAssignment(String name, Entity ent, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Property prop = getProperty(name, txn);//, this.getClassName());
		if(prop == null){
			throw new ApplicationException();
		}
		if(!EntityUtil.judgeValueToProperty(ent, prop)){
			throw new ApplicationException();
		}
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    //如果没有则创建，如果有记录，则覆盖。
	    StoredValue value = EntityUtil.entityToRecord(ent);
		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		dataBinding.objectToEntry(value, theData);
		idAssign.put(txn, theKey, theData);
	}
	
	public void putIdAssignment(String instId, String name, Entity ent, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Property prop = getProperty(name, txn);//, this.getClassName());
		if(prop == null){
			throw new ApplicationException();
		}
		if(!EntityUtil.judgeValueToProperty(ent, prop)){
			throw new ApplicationException();
		}
		String key = name + "@" + instId;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    //如果没有则创建，如果有记录，则覆盖。
	    StoredValue value = EntityUtil.entityToRecord(ent);
		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		dataBinding.objectToEntry(value, theData);
		idAssign.put(txn, theKey, theData);
	}
	
	public void putProcess(String key, String str, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(str.getBytes("UTF-8"));

	    OperationStatus status = processDB.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		processDB.put(txn, theKey, theData);
	}
	
	public String getIdProcess(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		OperationStatus status = processDB.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			return new String(theData.getData(), "UTF-8");
		}
		return null;
	}
	
	public void putReference(String key, Entity ent, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		StoredValue rec = null;
		if(ent instanceof HiwiiInstance) {
			HiwiiInstance inst = (HiwiiInstance) ent;
			if(!inst.isPersisted()) {
//				inst.setPersisted(true);
//				inst.setUuid(EntityUtil.getUUID());
//				putInstance(inst, txn);
				throw new ApplicationException();//不能存储内存对象。
			}
			rec = EntityUtil.entityToRecord(inst);
		}else {
			rec = EntityUtil.entityToRecord(ent);
		}
	    //如果没有则创建，如果有记录，则覆盖。		
		dataBinding.objectToEntry(rec, theData);
		refers.put(txn, theKey, theData);
	}
	
	public Entity getReference(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = refers.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	TupleBinding<StoredValue> dataBinding = new ValueBinding();
	    	StoredValue rec = dataBinding.entryToObject(theData);
	    	return EntityUtil.recordToEntity(rec);
	    }else{ // if(status == OperationStatus.NOTFOUND)
	    	return null;
	    }
	}
	/**
	 * 多记录属性增加
	 * @param key:instance uuid.
	 * @param ass
	 * @param txn
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public void addAssignment(String uid, String pname, Entity value, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = pname + "@" + uid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    Definition def = EntityUtil.proxyGetDefinition(value.getClassName());
	    //如果没有则创建，如果有记录，则覆盖。
		TupleBinding<StoredValue> binding = new ValueBinding();
		DatabaseEntry data0 = new DatabaseEntry();
	    StoredValue rec = new StoredValue();
	    if(value instanceof HiwiiInstance){
	    	HiwiiInstance inst = (HiwiiInstance) value;
	    	rec.setType('i');
	    	rec.setSign(def.getSignature());
	    	rec.setValue(inst.getUuid());
	    }else{
	    	rec.setType('s');
	    	rec.setSign(def.getSignature());
	    	rec.setValue(value.toString());
	    }
	    binding.objectToEntry(rec, data0);
		OperationStatus status = idAssign.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			StoredValue val = binding.entryToObject(theData);
			String str = val.getValue() + "%" + EntityUtil.getUUID();
			DatabaseEntry key0 = new DatabaseEntry(str.getBytes("UTF-8"));
		    listAssignDatabase.put(txn, key0, data0);
		}else if(status == OperationStatus.NOTFOUND){
			String pid = EntityUtil.getUUID();
			StoredValue val = new StoredValue();
			val.setType('m');
			val.setSign("000000");
			val.setValue(pid);
			binding.objectToEntry(val, theData);
			idAssign.put(txn, theKey, theData);
			String str = val.getValue() + "%" + EntityUtil.getUUID();
			DatabaseEntry key0 = new DatabaseEntry(str.getBytes("UTF-8"));
		    listAssignDatabase.put(txn, key0, data0);
		}
	}
	
	public Assignment getAssignment(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idAssign.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	TupleBinding<StoredValue> dataBinding = new ValueBinding();
	    	StoredValue rec = dataBinding.entryToObject(theData);
	    	return EntityUtil.recordToAssignment(rec);
	    }else{ // if(status == OperationStatus.NOTFOUND)
	    	return null;
	    }
	}
	
	public Entity getAssignment(HiwiiInstance inst, String pname,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = pname + "&" + inst.getUuid();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    
		TupleBinding<StoredValue> binding = new ValueBinding();
		OperationStatus status = idAssign.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			StoredValue val = binding.entryToObject(theData);
			Entity ret = getValue(val);
			return ret;
		}
		return null;
	}
	
	public Entity getPropertyValue(String instId, String pname,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = pname + "&" + instId;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    
		TupleBinding<StoredValue> binding = new ValueBinding();
		OperationStatus status = idAssign.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			StoredValue val = binding.entryToObject(theData);
			Entity ret = getValue(val);
			return ret;
		}
		throw new ApplicationException("null value!");
	}
	
	public void deleteAssignment(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));

	    OperationStatus status = idAssign.delete(null, theKey);
	    if(status != OperationStatus.SUCCESS){
	    	throw new ApplicationException();
	    }
	}
	
	public List<Assignment> allAssignment(){
		Cursor cursor = null;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		try {
			cursor = idAssign.openCursor(null, null);
			List<Assignment> list = new ArrayList<Assignment>();
			OperationStatus retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				StoredValue rec = dataBinding.entryToObject(data);
				Assignment ass = EntityUtil.recordToAssignment(rec);
				ass.setName(new String(key.getData()));
				list.add(ass);
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			}
			return list;
		} catch(DatabaseException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<HiwiiInstance> allInstance(){
		Cursor cursor = null;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		try {
			cursor = entityDatabase.openCursor(null, null);
			List<HiwiiInstance> list = new ArrayList<HiwiiInstance>();
			OperationStatus retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				HiwiiInstance inst = new HiwiiInstance();
				inst.setUuid(new String(key.getData()));
				list.add(inst);
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			}
			return list;
		} catch(DatabaseException e) {
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<Assignment> allAssignmentKey(){
		SecondaryCursor cursor = null;
		DatabaseEntry ikey = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		TupleBinding<Assignment> dataBinding = new AssignmentBinding();
		try {
			cursor = indexAssignHost.openCursor(null, null);
			List<Assignment> list = new ArrayList<Assignment>();
			OperationStatus retVal = cursor.getNext(ikey, key, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				Assignment ass = dataBinding.entryToObject(data);
				try {
					String val = new String(ikey.getData(), "UTF-8") + "+" 
							+ new String(key.getData(), "UTF-8");
					ass.setName(val);
				} catch (UnsupportedEncodingException e) {
					//for test
					e.printStackTrace();
				}
				list.add(ass);
				retVal = cursor.getNext(ikey, key, data, LockMode.DEFAULT);
			}
			return list;
		} catch(DatabaseException e) {
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<String> allKeys(String dbname){
		Database db0 = getDbEnvironment().openDatabase(null, dbname, getDbConfig());
		Cursor cursor = null;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		try {
			cursor = db0.openCursor(null, null);
			List<String> list = new ArrayList<String>();
			OperationStatus retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				String str = new String(key.getData(), "UTF-8");
				list.add(str);
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			}
			return list;
		} catch(DatabaseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
				if (db0 != null) {
					db0.close();
				}
			} catch(DatabaseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * ie. functionLink is DB, indexFunctionLink is a secondaryDB
	 * @param secname
	 * @return
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public List<String> getSecondaryKeys(String dbname, String secname)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Database db0 = getDbEnvironment().openDatabase(null, dbname, getDbConfig());
		SecondaryDatabase secDB = getDbEnvironment().openSecondaryDatabase(null, secname, db0, 
				getMySecConfig());
		SecondaryCursor cursor = null;
		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
		try {
			cursor = secDB.openCursor(null, null);
			List<String> list = new ArrayList<String>();
			OperationStatus retVal = cursor.getNext(key, theKey, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				String str = new String(key.getData(), "UTF-8") + "," + new String(theKey.getData(), "UTF-8");
				list.add(str);
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			}
			return list;
		}finally  {			
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public void putStatus(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry("State".getBytes("UTF-8"));

	    OperationStatus status = idState.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		
		idState.put(txn, theKey, theData);
	}
	
	public boolean hasStatus(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idState.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
//	    	String ret = new String(theData.getData(), "UTF-8");
	    	return true;
	    }else{ // if(status == OperationStatus.NOTFOUND)
	    	return false;
	    }
	}
	
	public void putAction(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(key.getBytes("UTF-8"));

	    OperationStatus status = actionDatabase.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		
		actionDatabase.put(txn, theKey, theData);
	}
	
	public boolean hasAction(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = actionDatabase.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	return true;
	    }else{ 
	    	return false;
	    }
	}
	
	public void putSwitch(String key, List<Expression> items, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    TupleBinding<List<Expression>> binding = new ListExpressionBinding();
	    binding.objectToEntry(items, theData);
	    OperationStatus status = switchDef.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		
		switchDef.put(txn, theKey, theData);
	}
	
	public List<Expression> hasSwitch(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = switchDef.get(null, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.SUCCESS){
	    	TupleBinding<List<Expression>> binding = new ListExpressionBinding();
	    	List<Expression> ret = binding.entryToObject(theData);
	    	return ret;
	    }else{ // if(status == OperationStatus.NOTFOUND)
	    	return null;
	    }
	}
	
	public List<String> allStatus()
			throws IOException, DatabaseException, ApplicationException, Exception{
		Cursor cursor = null;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
//		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		try {
			cursor = idState.openCursor(null, null);
			List<String> list = new ArrayList<String>();
			OperationStatus retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				String val = new String(key.getData(), "UTF-8");
				list.add(val);
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			}
			return list;
		} catch(DatabaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				e.printStackTrace();
			}
		}
		return null;

	}
	
	public List<String> allSwitches2()
			throws IOException, DatabaseException, ApplicationException, Exception{
		Cursor cursor = null;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
//		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		try {
			cursor = switchDef.openCursor(null, null);
			List<String> list = new ArrayList<String>();
			OperationStatus retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				String val = new String(key.getData(), "UTF-8");
				list.add(val);
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			}
			return list;
		} catch(DatabaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				e.printStackTrace();
			}
		}
		return null;

	}
	
	public List<String> allSwitches()
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		DatabaseEntry uid = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
//		TupleBinding<StoredValue> dataBinding = new ValueBinding();
		try {
			cursor = indexSwitchHost.openCursor(null, null);
			List<String> list = new ArrayList<String>();
//			OperationStatus retVal = cursor.getFirst(uid, key, data, LockMode.DEFAULT);
			OperationStatus retVal = cursor.getFirst(key, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				String val = new String(key.getData(), "UTF-8");
				list.add(val);
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			}
			return list;
		} catch(DatabaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				e.printStackTrace();
			}
		}
		return null;

	}
	
	public void putUser(User user, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(user.getUserid().getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(user.getPassword().getBytes("UTF-8"));

	    OperationStatus status = userDatabase.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		
		userDatabase.put(txn, theKey, theData);
	}
	
	/**
	 * 调用getUser需要LocalHost.getInstance()，而init()方法在new LocalHost中。
	 * @param key
	 * @param txn
	 * @return
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public boolean hasUser(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = userDatabase.get(null, theKey, theData, LockMode.DEFAULT);
	    
	    if(status == OperationStatus.SUCCESS){
	    	return true;
	    }else{ 
	    	return false;
	    }
	}
	
	public User getUser(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = userDatabase.get(txn, theKey, theData, LockMode.DEFAULT);
	    
	    if(status == OperationStatus.SUCCESS){
	    	User user = new User();
	    	user.setUserid(key);
	    	user.setPassword(new String(theData.getData(), "UTF-8"));
	    	return user;
	    }
	    return null;
	}
	
	public Group getGroup(String groupid, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(groupid.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    OperationStatus status = groupDatabase.get(null, theKey, theData, LockMode.DEFAULT);
	    TupleBinding<Group> dataBinding = new GroupBinding();
	    
		if(status == OperationStatus.SUCCESS){
			Group ret = dataBinding.entryToObject(theData);
			return ret;
		}
		return null;
	}
	
	public List<Group> getAllGroup()
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();

	    Cursor cur = groupDatabase.openCursor(null, null);
	    try {
			List<Group> list = new ArrayList<Group>();
			TupleBinding<Group> dataBinding = new GroupBinding();
			OperationStatus retVal = cur.getNext(key, data, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS){
				Group grp = dataBinding.entryToObject(data);
				grp.setGroupId(new String(key.getData(),"UTF-8"));
				list.add(grp);
				retVal = cur.getNext(key, data, LockMode.DEFAULT);
			}
			return list;
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cur != null) {
					cur.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public void putMapUserGroup(String userid, String gid, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = userid + '@' + gid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(userid.getBytes("UTF-8"));

	    OperationStatus status = mapUserGroup.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		
		mapUserGroup.put(txn, theKey, theData);
	}
	
	public void putJudgment(String key, JudgmentResult jdg, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = judgeDatabase.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		BooleanBinding binding = new BooleanBinding();
		binding.objectToEntry(EntityUtil.judge(jdg), theData);

		judgeDatabase.put(txn, theKey, theData);
	}
	
	public void turnIdJudgment(String name, JudgmentResult jdg, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		if(!hasStatus(name, txn)) {
			throw new ApplicationException();
		}
		String key = name;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		BooleanBinding binding = new BooleanBinding();
		binding.objectToEntry(EntityUtil.judge(jdg), theData);
		
		judgeDatabase.put(txn, theKey, theData);
	}
	
	public void turnIdJudgment(String instId, String name, JudgmentResult jdg, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		if(!hasStatus(name, txn)) {
			throw new ApplicationException();
		}
		String key = name + "@" + instId;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		BooleanBinding binding = new BooleanBinding();
		binding.objectToEntry(EntityUtil.judge(jdg), theData);
		
		judgeDatabase.put(txn, theKey, theData);
	}
	
	public void turnJudgment(String key, JudgmentResult jdg, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		BooleanBinding binding = new BooleanBinding();
		binding.objectToEntry(EntityUtil.judge(jdg), theData);
		
		judgeDatabase.put(txn, theKey, theData);
	}
	
	public void putInstanceJudgment(HiwiiInstance inst, String name, JudgmentResult result, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "&" + inst.getUuid();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		BooleanBinding binding = new BooleanBinding();
		binding.objectToEntry(EntityUtil.judge(result), theData);
	    OperationStatus status = judgeDatabase.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		judgeDatabase.put(txn, theKey, theData);
	}
	
	public void putSwitchResult(HiwiiInstance inst, String name, String value, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "&" + inst.getUuid();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(value.getBytes("UTF-8"));

//	    OperationStatus status = judgeDatabase.get(null, theKey, theData, LockMode.DEFAULT);
//		if(status == OperationStatus.SUCCESS){
//			throw new ApplicationException();
//		}
		OperationStatus status = switchResult.put(txn, theKey, theData);
		if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
	}
	
	public void updateInstanceJudgment(HiwiiInstance inst, String name, JudgmentResult result, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "&" + inst.getUuid();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		BooleanBinding binding = new BooleanBinding();
		binding.objectToEntry(EntityUtil.judge(result), theData);
//	    OperationStatus status = judgeDatabase.get(null, theKey, theData, LockMode.DEFAULT);
//		if(status == OperationStatus.SUCCESS){
//			throw new ApplicationException();
//		}
		judgeDatabase.put(txn, theKey, theData);
	}
	
	public void putDefinitionJudgment(Definition def, String name, JudgmentResult result, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "#" + def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		BooleanBinding binding = new BooleanBinding();
		binding.objectToEntry(EntityUtil.judge(result), theData);
	    OperationStatus status = judgeDatabase.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		judgeDatabase.put(txn, theKey, theData);
	}
	
	public JudgmentResult getJudgment(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		OperationStatus status = judgeDatabase.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.NOTFOUND){
			return null;
		}else if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		BooleanBinding binding = new BooleanBinding();
		Boolean bool = binding.entryToObject(theData);
		return EntityUtil.decide(bool);
	}

	public JudgmentResult getInstanceJudgment(HiwiiInstance inst, String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "@" + inst.getUuid();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		OperationStatus status = judgeDatabase.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.NOTFOUND){
			Definition def = getDefinitionByName(inst.getClassName());
			if(def == null){
				throw new ApplicationException();
			}
			return getDefinitionJudgment(def, name, txn);
		}else if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		BooleanBinding binding = new BooleanBinding();
		Boolean bool = binding.entryToObject(theData);
		return EntityUtil.decide(bool);
	}
	
	public JudgmentResult getDefinitionJudgment(Definition def, String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "@" + def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		Cursor cur = fCalculation.openCursor(null, null);
		try {
			OperationStatus status = cur.getSearchKeyRange(theKey, theData, LockMode.DEFAULT);
			if(status == OperationStatus.NOTFOUND){
				return null;
			}else if(status != OperationStatus.SUCCESS){
				throw new ApplicationException();
			}
			BooleanBinding binding = new BooleanBinding();
			Boolean bool = binding.entryToObject(theData);
			return EntityUtil.decide(bool);	
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cur != null) {
					cur.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
	}
	
	public boolean hasCalculation(String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idCalculation.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			return true;
		}
		Cursor cur = fCalculation.openCursor(null, null);
		try {
			OperationStatus retVal = cur.getSearchKeyRange(theKey, theData, LockMode.DEFAULT);
			if(retVal == OperationStatus.SUCCESS){
				String key = new String(theKey.getData(), "UTF-8");
				String head;
				int pos = key.indexOf('#');
				if(pos > 0){
					head = key.substring(0, pos);
				}else{
					head = key;
				}				
				if(head.equals(name)){
					return true;
				}
			}
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cur != null) {
					cur.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		return false;
	}
	
	public void putIdCalculation(String key, String expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(expr.getBytes("UTF-8"));

	    OperationStatus status = idCalculation.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		idCalculation.put(txn, theKey, theData);
	}
	
	public void dropIdCalculation(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
//		DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idCalculation.delete(null, theKey);
		if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
	}

	public void putIdAction(String key, String expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		boolean boo = hasAction(key, txn);
		if(!boo) {
			throw new ApplicationException();
		}
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(expr.getBytes("UTF-8"));

//	    OperationStatus status = idAction.get(txn, theKey, theData, LockMode.DEFAULT);
//		if(status == OperationStatus.SUCCESS){
//			throw new ApplicationException();
//		}

		idAction.put(txn, theKey, theData);
	}
	
	public void dropIdAction(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));

	    OperationStatus status = idAction.delete(null, theKey);
		if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
	}
	
	public void putIdDecision(String key, String expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(expr.getBytes("UTF-8"));

	    OperationStatus status = idDecision.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		idDecision.put(txn, theKey, theData);
	}
	
	public String getIdCalculation(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		OperationStatus status = idCalculation.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			return new String(theData.getData(), "UTF-8");
		}
		return null;
	}
	
	public String getIdCalculation(Entity subject, String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		OperationStatus status = idCalculation.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			return new String(theData.getData(), "UTF-8");
		}
		return null;
	}
	
	public void putIdCalculation(Definition def, String name, String expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '@' + def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(expr.getBytes("UTF-8"));

	    OperationStatus status = idCalculation.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		idCalculation.put(txn, theKey, theData);
	}
	
	public void putInstIdCalculation(HiwiiInstance inst, String name, String expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '&' + inst.getUuid();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(expr.getBytes("UTF-8"));

	    OperationStatus status = inst_idCalculation.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		inst_idCalculation.put(txn, theKey, theData);
	}
	
	public String getInstIdCalculation(HiwiiInstance inst, String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '&' + inst.getUuid();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = inst_idCalculation.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.NOTFOUND){
//			throw new ApplicationException();
			return null;
		}
		
		String str = new String(theData.getData(), "UTF-8");
//		Expression exp = StringUtil.parseString(str);
		return str;
	}
	
	public String getIdAction(String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		OperationStatus status = idAction.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			return new String(theData.getData(), "UTF-8");
		}
		return null;
	}
	
	public void putIdAction(HiwiiInstance inst, String name, String expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '&' + inst.getUuid();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(expr.getBytes("UTF-8"));

	    OperationStatus status = idAction.get(null, theKey, theData, LockMode.DEFAULT);
//		if(status == OperationStatus.SUCCESS){
//			throw new ApplicationException();
//		}

		idAction.put(txn, theKey, theData);
	}
	
	public void putIdAction(Definition def, String name, String expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '@' + def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(expr.getBytes("UTF-8"));

	    OperationStatus status = idAction.get(null, theKey, theData, LockMode.DEFAULT);
//		if(status == OperationStatus.SUCCESS){
//			throw new ApplicationException();
//		}

		idAction.put(txn, theKey, theData);
	}
	
	public void putIdDecision(Definition def, String name, String expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '@' + def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(expr.getBytes("UTF-8"));

	    OperationStatus status = idDecision.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}

		idDecision.put(txn, theKey, theData);
	}
	
	public String getIdCalculation(Definition def, String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '@' + def.getSignature();
		
		Cursor cursor = null;
		cursor = idCalculation.openCursor(null, null);
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry data = new DatabaseEntry();
	    
	    try {
	    	OperationStatus found = cursor.getSearchKeyRange(theKey,data, LockMode.DEFAULT);
	    	if (found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		if(StringUtil.matched(key0, key)){
	    			return new String(data.getData(), "UTF-8");
	    		}
	    	}
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;
	}
	
	public String getIdCalculation(HiwiiInstance inst, String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '&' + inst.getUuid();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry data = new DatabaseEntry();
	    
	    OperationStatus found = idCalculation.get(null, theKey,data, LockMode.DEFAULT);
    	if (found == OperationStatus.SUCCESS)  {
    		String key0 = new String(theKey.getData(), "UTF-8");
    		return key0;
    	}
    	return null;
	}
	
	public String getIdAction(Definition def, String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '@' + def.getSignature();
		
		Cursor cursor = null;
		cursor = idAction.openCursor(null, null);
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry data = new DatabaseEntry();
	    
	    try {
	    	OperationStatus found = cursor.getSearchKeyRange(theKey,data, LockMode.DEFAULT);
	    	if (found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		if(StringUtil.matched(key0, key)){
	    			return new String(data.getData(), "UTF-8");
	    		}
	    	}
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;
	}
	
	public void putFunctionCalculation(FunctionExpression source, Expression expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKeyByDeclare(source, txn);
		if(fkey == null) {
			throw new ApplicationException();
		}
		String key = fkey + "^" + EntityUtil.getUUID();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		FunctionDeclaration dec = new FunctionDeclaration();
		dec.setStatement(expr);
		dec.setArguments(EntityUtil.getFunctionArgument(source));
		dec.setArgType(EntityUtil.getFunctionArgumentType(source));
		TupleBinding<FunctionDeclaration> dataBinding = new FunctionDeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		fCalculation.put(txn, theKey, theData);
	}
	
	public void putFunctionCalculation(Definition def, FunctionExpression source, Expression expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKeyByDeclare(source, txn);
		if(fkey == null) {
			throw new ApplicationException();
		}
	
		String key = fkey + "@" + def.getSignature() + "%" + EntityUtil.getUUID();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		FunctionDeclaration dec = new FunctionDeclaration();
		dec.setStatement(expr);
		dec.setArguments(EntityUtil.getFunctionArgument(source));
		dec.setArgType(EntityUtil.getFunctionArgumentType(source));
		TupleBinding<FunctionDeclaration> dataBinding = new FunctionDeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		fCalculationDef.put(txn, theKey, theData);
	}
	
	public void putFunctionCalculation_Inst(HiwiiInstance inst, FunctionExpression source, Expression expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKeyByDeclare(source, txn);
		if(fkey == null) {
			throw new ApplicationException();
		}
	
		String key = fkey + "&" + inst.getUuid() + "%" + EntityUtil.getUUID();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		FunctionDeclaration dec = new FunctionDeclaration();
		dec.setStatement(expr);
		dec.setArguments(EntityUtil.getFunctionArgument(source));
		dec.setArgType(EntityUtil.getFunctionArgumentType(source));
		TupleBinding<FunctionDeclaration> dataBinding = new FunctionDeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		fCalculationInst.put(txn, theKey, theData);
	}
	
	public void declareFunctionAction(FunctionExpression source, Expression expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKeyByDeclare(source, txn);
		if(fkey == null) {
			throw new ApplicationException();
		}
		String key = fkey + "^" + EntityUtil.getUUID();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		FunctionDeclaration dec = new FunctionDeclaration();
		dec.setStatement(expr);
		dec.setArguments(EntityUtil.getFunctionArgument(source));
		dec.setArgType(EntityUtil.getFunctionArgumentType(source));
		TupleBinding<FunctionDeclaration> dataBinding = new FunctionDeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		fCalculation.put(txn, theKey, theData);
	}
	
	public void declareFunctionAction(Definition def, FunctionExpression source, Expression expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKeyByDeclare(source, txn);
		if(fkey == null) {
			throw new ApplicationException();
		}
	
		String key = fkey + "@" + def.getSignature() + "%" + EntityUtil.getUUID();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		FunctionDeclaration dec = new FunctionDeclaration();
		dec.setStatement(expr);
		dec.setArguments(EntityUtil.getFunctionArgument(source));
		dec.setArgType(EntityUtil.getFunctionArgumentType(source));
		TupleBinding<FunctionDeclaration> dataBinding = new FunctionDeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		fCalculationDef.put(txn, theKey, theData);
	}
	
	public void declareFunctionAction_Inst(HiwiiInstance inst, FunctionExpression source, Expression expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKeyByDeclare(source, txn);
		if(fkey == null) {
			throw new ApplicationException();
		}
	
		String key = fkey + "&" + inst.getUuid() + "%" + EntityUtil.getUUID();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		FunctionDeclaration dec = new FunctionDeclaration();
		dec.setStatement(expr);
		dec.setArguments(EntityUtil.getFunctionArgument(source));
		dec.setArgType(EntityUtil.getFunctionArgumentType(source));
		TupleBinding<FunctionDeclaration> dataBinding = new FunctionDeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		fCalculationInst.put(txn, theKey, theData);
	}
	
//	public void putFunDecision(Definition def, FunctionDeclaration dec, Transaction txn)
//			throws IOException, DatabaseException, ApplicationException, Exception{
//		String key0 = dec.getName() + "#" + dec.getArguments().size() 
//				+ "." + def.getSignature();
//		String key = key0 + "%" + EntityUtil.getUUID();
//		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
//		DatabaseEntry theData = new DatabaseEntry();
//		Cursor cursor = null;
//		cursor = fDecision.openCursor(null, null);
//		try{
//			OperationStatus status = cursor.getSearchKeyRange(theKey, theData, LockMode.DEFAULT);
//			if(status == OperationStatus.SUCCESS){
//				String key1 = new String(theKey.getData(), "UTF-8");
//				if(StringUtil.matched(key1, key0)){
//					throw new ApplicationException();
//				}
//			}
//		}catch(DatabaseException e) {
//	    	throw new ApplicationException();
//	    } finally  {
//	    	try {
//	    		if (cursor != null) {
//	    			cursor.close();
//	    		}
//	    	} catch(DatabaseException e) {
//	    		throw new ApplicationException();
//	    	}
//	    }
//
//		TupleBinding<FunctionDeclaration> dataBinding = new FunctionDeclarationBinding();
//		dataBinding.objectToEntry(dec, theData);
//		fDecision.put(txn, theKey, theData);
//	}
//	
//	public void putFunDecision(FunctionDeclaration dec, Transaction txn)
//			throws IOException, DatabaseException, ApplicationException, Exception{
//		String key = dec.getName() + "#" + dec.getArguments().size();
//		key = key + "%" + EntityUtil.getUUID();
//		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
//		DatabaseEntry theData = new DatabaseEntry();
//
//	    OperationStatus status = fDecision.get(null, theKey, theData, LockMode.DEFAULT);
//		if(status == OperationStatus.SUCCESS){
//			throw new ApplicationException();
//		}
//
//		TupleBinding<FunctionDeclaration> dataBinding = new FunctionDeclarationBinding();
//		dataBinding.objectToEntry(dec, theData);
//		fDecision.put(txn, theKey, theData);
//	}
	
	public FunctionDeclaration getFunctionCalculation(Entity subject, String name, List<Entity> args, Transaction txn, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		cursor = indexfCalculation.openCursor(null, null);
		String key = name + "#" + args.size();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
	    try {
	    	OperationStatus found = cursor.getSearchKey(theKey, pkey, data, LockMode.DEFAULT);
	    	RuntimeContext rc = null;
	    	FunctionDeclaration fd = null;
	    	while (found == OperationStatus.SUCCESS)  {
	    		fd = binding.entryToObject(data);
	    		rc = context.getLadder().newRuntimeContext('c');
				int i = 0;
//				for(Argument arg:fd.getArguments()){
//					rc.getRefers().put(arg.getName(), args.get(i));
//					i++;
//				}				
//				boolean state = true;
//				for(Argument arg:fd.getArguments()){
//					if(arg instanceof DecoratedArgument) {
//						DecoratedArgument darg = (DecoratedArgument) arg;
//						for(Expression expr:darg.getStates()) {
//							Expression result = rc.doDecision(expr);
//							if(!EntityUtil.judge(result)) {
//								state = false;
//								break;
//							}
//						}
//					}
//				}
//				if(state) {
//					return fd;
//				}
	    		found = cursor.getNextDup(theKey, pkey, data, LockMode.DEFAULT);
	    	}
//	    	Entity ret = rc.doCalculation(fd.getStatement());
	    	rc = null;  //释放内存	    	
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;
	}
	
	public FunctionDeclaration getFunctionCalculation(String name, List<Entity> args, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKey(name, args, txn);
		if(fkey == null) {
			return null;
		}
		SecondaryCursor cursor = null;
		cursor = indexfCalculation.openCursor(null, null);
//		String key = name + "#" + args.size();
		
		DatabaseEntry theKey = new DatabaseEntry(fkey.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
	    try {
	    	OperationStatus found = cursor.getSearchKey(theKey, pkey, data, LockMode.DEFAULT);
	    	FunctionDeclaration fd = null;
	    	while (found == OperationStatus.SUCCESS)  {
	    		fd = binding.entryToObject(data);
				boolean match = true;
				for(int i=0;i<args.size();i++) {
					if(!EntityUtil.judgeEntityIsDefinition(args.get(i), fd.getArguments().get(i), fd.getArgType().get(i))) {
						match = false;
						break;
					}
				}
				if(match) {
					return fd;
				}
	    		found = cursor.getNextDup(theKey, pkey, data, LockMode.DEFAULT);
	    	}
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;
	}
	
	public FunctionDeclaration getFunctionAction(String name, List<Entity> args, Transaction txn, HiwiiContext context)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		cursor = indexfAction.openCursor(null, null);
		String key = name + "#" + args.size();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
	    try {
	    	OperationStatus found = cursor.getSearchKey(theKey, pkey, data, LockMode.DEFAULT);
	    	RuntimeContext rc = null;
	    	FunctionDeclaration fd = null;
	    	while (found == OperationStatus.SUCCESS)  {
	    		fd = binding.entryToObject(data);
	    		rc = context.getLadder().newRuntimeContext('a');
				int i = 0;
//				for(Argument arg:fd.getArguments()){
//					rc.getRefers().put(arg.getName(), args.get(i));
//					i++;
//				}				
//				boolean state = true;
//				for(Argument arg:fd.getArguments()){
//					if(arg instanceof DecoratedArgument) {
//						DecoratedArgument darg = (DecoratedArgument) arg;
//						for(Expression expr:darg.getStates()) {
//							Expression result = rc.doDecision(expr);
//							if(!EntityUtil.judge(result)) {
//								state = false;
//								break;
//							}
//						}
//					}
//				}
//				if(state) {
//					return fd;
//				}
	    		found = cursor.getNextDup(theKey, pkey, data, LockMode.DEFAULT);
	    	}
//	    	Entity ret = rc.doCalculation(fd.getStatement());
	    	rc = null;  //释放内存	    	
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;
	}
	
	public void dropFunCalculation(FunctionDeclaration fun, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		cursor = indexfCalculation.openCursor(txn, null);
		String key = "#" + fun.getArguments().size();//fun.getName() + 
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
	    try {
	    	OperationStatus found = cursor.getSearchKey(theKey, pkey, data, LockMode.DEFAULT);
	    	while (found == OperationStatus.SUCCESS)  {
	    		FunctionDeclaration fd = binding.entryToObject(data);
//	    		if(EntityUtil.matchArguments(args, fd.getArguments())){
//	    			return;
//	    		}
	    		OperationStatus drop = cursor.delete();
	    		found = cursor.getNextDup(theKey, pkey, data, LockMode.DEFAULT);
	    	}
//	    	txn.commit();
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
	}
	
	public void dropFunAction(FunctionDeclaration fun, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		cursor = indexfAction.openCursor(txn, null);
		String key = "#" + fun.getArguments().size();//fun.getName() + 
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
	    try {
	    	OperationStatus found = cursor.getSearchKey(theKey, pkey, data, LockMode.DEFAULT);
	    	while (found == OperationStatus.SUCCESS)  {
//	    		FunctionDeclaration fd = binding.entryToObject(data);
//	    		if(EntityUtil.matchArguments(args, fd.getArguments())){
//	    			return;
//	    		}
	    		OperationStatus drop = cursor.delete();
	    		found = cursor.getNextDup(theKey, pkey, data, LockMode.DEFAULT);
	    	}
//	    	txn.commit();
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
	}
	
	public FunctionDeclaration getFunctionCalculation(Definition def, String name, List<Entity> args, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = getFunctionLinkKey(name, args, txn);
		if(fkey == null) {
			return null;
		}
		SecondaryCursor cursor = null;
		cursor = indexfCalculationDef.openCursor(null, null);
		String key = fkey + "@" + def.getSignature();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
	    try {
	    	OperationStatus found = cursor.getSearchKeyRange(theKey, pkey, data, LockMode.DEFAULT);
	    	while (found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		if(!StringUtil.matched(key, key0)){
	    			break;
	    		}
	    		FunctionDeclaration fd = binding.entryToObject(data);
				boolean match = true;
				for(int i=0;i<args.size();i++) {
					if(!EntityUtil.judgeEntityIsDefinition(args.get(i), fd.getArguments().get(i), fd.getArgType().get(i))) {
						match = false;
						break;
					}
				}
				if(match) {
					return fd;
				}
	    		found = cursor.getNextDup(theKey, pkey, data, LockMode.DEFAULT);
//	    		found = cursor.getLast(theKey, pkey, data, LockMode.DEFAULT);
	    	}
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;
	}
	
	public FunctionDeclaration getFunctionCalculation_Subject(Entity subject, String name, List<Entity> args, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		if(subject instanceof HiwiiInstance) {
			HiwiiInstance inst = (HiwiiInstance) subject;
			String fkey = getFunctionLinkKey(name, args, txn);
			if(fkey == null) {
				return null;
			}
			SecondaryCursor cursor = null;
			cursor = indexfCalculationInst.openCursor(null, null);
			String key = fkey + "&" + inst.getUuid();
			
			DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
			DatabaseEntry pkey = new DatabaseEntry();
		    DatabaseEntry data = new DatabaseEntry();
		    
		    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
		    try {
		    	OperationStatus found = cursor.getSearchKeyRange(theKey, pkey, data, LockMode.DEFAULT);
		    	while (found == OperationStatus.SUCCESS)  {
		    		String key0 = new String(theKey.getData(), "UTF-8");
		    		if(!StringUtil.matched(key, key0)){
		    			break;
		    		}
		    		FunctionDeclaration fd = binding.entryToObject(data);
					boolean match = true;
					for(int i=0;i<args.size();i++) {
						if(!EntityUtil.judgeEntityIsDefinition(args.get(i), fd.getArguments().get(i), fd.getArgType().get(i))) {
							match = false;
							break;
						}
					}
					if(match) {
						return fd;
					}
		    		found = cursor.getNextDup(theKey, pkey, data, LockMode.DEFAULT);
//		    		found = cursor.getLast(theKey, pkey, data, LockMode.DEFAULT);
		    	}
		    }catch(DatabaseException e) {
		    	throw new ApplicationException();
		    } finally  {
		    	try {
		    		if (cursor != null) {
		    			cursor.close();
		    		}
		    	} catch(DatabaseException e) {
		    		throw new ApplicationException();
		    	}
		    }
		}else if(subject instanceof StringExpression) {
			
		}
		
		return null;
	}
	
	public FunctionDeclaration getFunctionAction(Definition def, String name, List<Entity> args, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		cursor = indexfAction.openCursor(null, null);
		String key = name + "#" + args.size() + "." + def.getSignature();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
	    try {
	    	OperationStatus found = cursor.getSearchKeyRange(theKey, pkey, data, LockMode.DEFAULT);
	    	while (found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		if(!StringUtil.matched(key, key0)){
	    			break;
	    		}
	    		FunctionDeclaration fd = binding.entryToObject(data);
//	    		if(EntityUtil.matchArguments(args, fd.getArguments())){
//	    			return fd;
//	    		}
	    		found = cursor.getNext(theKey, pkey, data, LockMode.DEFAULT);
	    	}
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;
	}
	
	public FunctionDeclaration getFunctionDecision(String name, List<Entity> args, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		cursor = indexfDecision.openCursor(null, null);
		String key = name + "#" + args.size();
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<FunctionDeclaration> binding = new FunctionDeclarationBinding();
	    try {
	    	OperationStatus found = cursor.getSearchKey(theKey, pkey, data, LockMode.DEFAULT);
	    	while (found == OperationStatus.SUCCESS)  {
	    		FunctionDeclaration fd = binding.entryToObject(data);
//	    		if(EntityUtil.matchArguments(args, fd.getArguments())){
//	    			return fd;
//	    		}
	    		found = cursor.getNextDup(theKey, pkey, data, LockMode.DEFAULT);
	    	}
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;
	}
	
	public void putMappingCalculation(MappingExpression map, Expression expr, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = map.getName() + "#" + map.getArguments().size();
		String type = getMappingLink(map, txn);
		if(type == null || type.length() == 0) {
			throw new ApplicationException();
		}
		MappingDeclaration md = getMappingCalculation(map.getName(), map.getArguments(), txn);
		if(md != null) {
			throw new ApplicationException();
		}
		List<String> args = new ArrayList<String>();
		for(Expression exp:map.getArguments()) {
			if(!(exp instanceof IdentifierExpression)) {
				throw new ApplicationException();
			}
			IdentifierExpression ie = (IdentifierExpression) exp;
			args.add(ie.getName());
		}
		String key = fkey;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

		MappingDeclaration dec = new MappingDeclaration();
		dec.setStatement(expr);
		dec.setArguments(args);
		
		TupleBinding<MappingDeclaration> dataBinding = new MappingDeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		mCalculation.put(txn, theKey, theData);
	}
	
	public MappingDeclaration getMappingCalculation(String name, List<Expression> args,  Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String fkey = name + "#" + args.size();
		String type = getMappingLink(name, args, txn);
		if(type == null || type.length() == 0) {
			throw new ApplicationException();
		}
		String key = fkey;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();
		
		TupleBinding<MappingDeclaration> binding = new MappingDeclarationBinding();
		OperationStatus found = mCalculation.get(txn, theKey, theData, LockMode.DEFAULT);
    	if(found == OperationStatus.SUCCESS)  {
    		MappingDeclaration md = binding.entryToObject(theData);
    		return md;
    	}

    	return null;
	}
	
	public void putConditionCalculation(ConditionDeclaration dec, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		
	}
	public void putDecision(String key, Declaration dec, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idDecision.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		
		theData = new DatabaseEntry(key.getBytes("UTF-8"));
		TupleBinding<Declaration> dataBinding = new DeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		idDecision.put(txn, theKey, theData);
	}
	
	public void putIdAction(String key, Declaration dec, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idAction.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		
		theData = new DatabaseEntry(key.getBytes("UTF-8"));
		TupleBinding<Declaration> dataBinding = new DeclarationBinding();
		dataBinding.objectToEntry(dec, theData);
		idAction.put(txn, theKey, theData);
	}
	
	public void lockIdCalculation(String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(name.getBytes("UTF-8"));
	    OperationStatus status = closinIdCalculation.get(txn, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.NOTFOUND){
			closinIdCalculation.put(txn, theKey, theData);
		}
	}
	
	public void lockIdAction(String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(name.getBytes("UTF-8"));
	    OperationStatus status = closinIdAction.get(txn, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.NOTFOUND){
			closinIdAction.put(txn, theKey, theData);
		}
	}
	
	public boolean lockedIdCalculation(String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    OperationStatus status = closinIdCalculation.get(txn, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean lockedIdAction(String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    OperationStatus status = closinIdAction.get(txn, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * action right for userId,group,roleId
	 */
	public void putUserActionRight(String name, String userid, boolean grant, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "@" +userid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    BooleanBinding boobind = new BooleanBinding();
	    boobind.objectToEntry(grant, theData);
	    OperationStatus status = rightIdAction.get(txn, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.NOTFOUND){
			rightIdAction.put(txn, theKey, theData);
		}
	}
	
	public void putUserActionRight(String dname, String name, String userid, 
			boolean grant, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = dname + "#" + name + "@" +userid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    BooleanBinding boobind = new BooleanBinding();
	    boobind.objectToEntry(grant, theData);
	    OperationStatus status = rightIdAction.get(txn, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.NOTFOUND){
			rightIdAction.put(txn, theKey, theData);
		}
	}
	public void putIdentifierAction(String name, String userid, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "@" + userid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(userid.getBytes("UTF-8"));
	    OperationStatus status = rightIdAction.get(txn, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.NOTFOUND){
			rightIdAction.put(txn, theKey, theData);
		}
	}
	
	public void putIdentifierAction(Definition def, String name, String userid, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "@" + userid + "#" + def.getSignature();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(userid.getBytes("UTF-8"));
	    OperationStatus status = rightIdAction.get(txn, theKey, theData, LockMode.DEFAULT);
	    if(status == OperationStatus.NOTFOUND){
			rightIdAction.put(txn, theKey, theData);
		}
	}
	
	public void putIdentifierCalculation(String name, String userid, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "@" + userid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(userid.getBytes("UTF-8"));
	    OperationStatus status = rightIdCalculation.get(txn, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.NOTFOUND){
			rightIdCalculation.put(txn, theKey, theData);
		}
	}
	/**
	 * 默认为允许。
	 * 法无禁止则是允许。
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public boolean userIdentifierAction(String name, String userid)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "@" + userid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    
	    OperationStatus ret = rightIdAction.get(null, theKey, theData, LockMode.DEFAULT);
	    if(ret == OperationStatus.NOTFOUND){
//	    	throw new ApplicationException("not found!");
	    	return false;
	    }
	    BooleanBinding boobind = new BooleanBinding();
	    boolean result = boobind.entryToObject(theData);
		return result;
	}
	
	public JudgmentResult userIdentifierAction(String dname, String name, String userid)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = dname + "#" + name + "@" + userid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    
	    OperationStatus ret = rightIdAction.get(null, theKey, theData, LockMode.DEFAULT);
	    if(ret == OperationStatus.NOTFOUND){
//	    	throw new ApplicationException("not found!");
	    	return null;
	    }
	    BooleanBinding boobind = new BooleanBinding();
	    boolean result = boobind.entryToObject(theData);
		return EntityUtil.decide(result);
	}
	
	public boolean userIdentifierCalculation(String name, String userid)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + "@" + userid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    
	    OperationStatus ret = rightIdCalculation.get(null, theKey, theData, LockMode.DEFAULT);
	    if(ret == OperationStatus.SUCCESS){
	    	return true;
	    }
		return false;
	}
	
	public void putSpaceExclude(String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(name.getBytes("UTF-8"));

	    OperationStatus status = spaceExclude.get(null, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
		
		spaceExclude.put(txn, theKey, theData);
	}
	
	public boolean isSpaceExclude(String key,Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = spaceExclude.get(null, theKey, theData, LockMode.DEFAULT);
	    
	    if(status == OperationStatus.SUCCESS){
	    	return true;
	    }else{ 
	    	return false;
	    }
	}
	
	public void dropSpaceExclude(String key, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));

	    OperationStatus status = spaceExclude.delete(null, theKey);
		if(status != OperationStatus.SUCCESS){
			throw new ApplicationException();
		}
	}
	
	public void putInstanceName(String name, String uid, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
//		String key = name + '@' +  uid;
		DatabaseEntry theKey = new DatabaseEntry(name.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry(uid.getBytes("UTF-8"));
	    
	    OperationStatus status = nameDatabase.get(txn, theKey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.NOTFOUND){
			nameDatabase.put(txn, theKey, theData);
		}
	}
	
	public String getInstanceName(String uid, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
//		String key = name + '@' +  uid;
		DatabaseEntry theKey = new DatabaseEntry(uid.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry theData = new DatabaseEntry();
	    
	    OperationStatus status = indexInstanceName.get(txn, theKey, pkey, theData, LockMode.DEFAULT);
		if(status == OperationStatus.SUCCESS){
			String refer = new String(pkey.getData(), "UTF-8");
			return refer;
		}
		return null;
	}
	
	public Entity getInstanceProp(String uid, String name, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		String key = name + '@' + uid;
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();

	    OperationStatus status = idAssign.get(txn, theKey, theData, LockMode.DEFAULT);
		TupleBinding<StoredValue> binding = new ValueBinding();
		if(status == OperationStatus.SUCCESS){
			StoredValue rec = binding.entryToObject(theData);
			if(rec.getType() == 's'){
				Expression expr = StringUtil.parseString(rec.getValue());
				SessionContext sc = LocalHost.getInstance().newSessionContext();
				Entity ent = sc.doCalculation(expr);
				return ent;
			}else if(rec.getType() == 'i'){
				return getInstanceById(rec.getValue());
			}else if(rec.getType() == 'm'){
				return getListProperty(rec.getValue(), null);
			}
		}
		return null;
	}
	
	public void putEntity(Entity ent)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = EntityUtil.proxyGetDefinition(ent.getClassName());
		if(def == null){
			throw new ApplicationException();
		}
	}
	
	public String putPartEntity(String uid, Entity ent, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		Definition def = EntityUtil.proxyGetDefinition(ent.getClassName());
		if(def == null){
			throw new ApplicationException();
		}
		String entid = EntityUtil.getUUID();
		String key = entid + '@' + uid;
		TupleBinding<StoredValue> binding = new ValueBinding();
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
	    DatabaseEntry theData = new DatabaseEntry();
	    char type = 's'; //else type = 's'
	    if(ent instanceof EntityList){
	    	
	    }else{
	    	if(ent instanceof HiwiiInstance){
	    		type = 'i';
	    	}
	    	StoredValue val = new StoredValue();
	    	val.setType(type);
	    	val.setSign(def.getSignature());
	    	val.setValue(ent.toString());
	    	binding.objectToEntry(val, theData);
	    	entityDatabase.put(txn, theKey, theData);
	    }
	    return entid;
	}

	public Entity getPartEntity(String uid, String typeid, Transaction txn)
			throws IOException, DatabaseException, ApplicationException, Exception{
		SecondaryCursor cursor = null;
		cursor = indexEntityPart.openCursor(null, null);
		String key = uid + "." + typeid;
		
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry pkey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    TupleBinding<StoredValue> binding = new ValueBinding();
	    try {
	    	TypedEntityList list = new TypedEntityList();
	    	OperationStatus found = cursor.getSearchKeyRange(theKey, pkey, data, LockMode.DEFAULT);
	    	while (found == OperationStatus.SUCCESS)  {
	    		String real = new String(theKey.getData(), "UTF-8");
	    		if(StringUtil.matched(real, key)){
	    			break;
	    		}
	    		StoredValue val = binding.entryToObject(data);
	    		Entity item = getValue(val);
	    		list.add(item);
	    		found = cursor.getNext(theKey, pkey, data, LockMode.DEFAULT);
	    	}
	    	if(list.getItems().size() == 1){
	    		return list.getItems().get(0);
	    	}else if(list.getItems().size() > 1){
	    		return list;
	    	}
	    }catch(DatabaseException e) {
	    	throw new ApplicationException();
	    } finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		throw new ApplicationException();
	    	}
	    }
		return null;	
	}

	public Entity getValue(StoredValue rec) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		if(rec.getType() == 's'){
			Expression expr = StringUtil.parseString(rec.getValue());
			SessionContext sc = LocalHost.getInstance().newSessionContext();
			Entity ent = sc.doCalculation(expr);
			return ent;
		}else if(rec.getType() == 'i'){
			return getInstanceById(rec.getValue());
		}else if(rec.getType() == 'm'){
			return getListProperty(rec.getValue(), null);
		}
		throw new ApplicationException();
	}
	
	/************20170325
	 * @throws Exception 
	 * @throws ApplicationException 
	 * @throws IOException 
	 * @throws DatabaseException *************/
	public String getEntityIdCalculation(Entity subject, String name, Transaction txn)
			throws DatabaseException, IOException, ApplicationException, Exception{
		if(subject instanceof HiwiiInstance){
			HiwiiInstance inst = (HiwiiInstance) subject;
//			String key = name + "#" + inst.getUuid();
			String ret = getInstIdCalculation(inst, name, txn);
			if(ret != null) {
				return ret;
//				throw new ApplicationException("not found!");
			}
//			return getIdCalculation(inst, name, null);
			Definition def = EntityUtil.proxyGetDefinition(inst.getClassName());
			return getIdCalculation(def, name, null);
		}else{
			
		}
		return null;
	}
	
	public String getEntityIdProcess(Entity subject, String name, Transaction txn)
			throws DatabaseException, IOException, ApplicationException, Exception{
		if(subject instanceof HiwiiInstance) {
			HiwiiInstance inst = (HiwiiInstance) subject;
			String uuid = inst.getUuid();
			String key = name + "@" + uuid;
			DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		    DatabaseEntry data = new DatabaseEntry();
		    
		    OperationStatus found = processDB.get(null, theKey, data, LockMode.DEFAULT);
	    	if (found == OperationStatus.SUCCESS)  {
	    		return new String(data.getData(), "UTF-8");
	    	}
	    	
	    	Definition def = getDefinitionByName(subject.getClassName());
	    	if(def != null) {
	    		
	    	}
		}
		return null;
	}
	
	/**
	 * 20190130
	 * 
	 * 
	 * @param expr
	 * @param txn
	 * @return
	 * @throws DatabaseException
	 * @throws IOException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public String saveExpression(Expression expr, Transaction txn)
			throws DatabaseException, IOException, ApplicationException, Exception{
		String key=null, data = null;
		DatabaseEntry thekey = null;
		DatabaseEntry thedata = null;
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return ie.getName();//
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String s1=new DecimalFormat("0000000000").format(fe.getArguments().size());
			key = fe.getName() + "%" + EntityUtil.getUUID()  ;
			data = key + "(";
			for(Expression arg:fe.getArguments()) {
				String str = saveExpression(arg, txn);
				data = "@" + str;
			}
			
			thekey = new DatabaseEntry(key.getBytes("UTF-8"));
			StringBinding binding = new StringBinding();
			binding.objectToEntry(key, thedata);
			return key;
		}else if(expr instanceof MappingExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else if(expr instanceof SubjectAction){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else if(expr instanceof BraceExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else if(expr instanceof BracketExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else if(expr instanceof ParenExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else if(expr instanceof UnaryOperation){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else if(expr instanceof BinaryOperation){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else if(expr instanceof NumberExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else if(expr instanceof StringExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			key = fe.getName() + "%" + EntityUtil.getUUID();
			return key;
		}else {
			throw new ApplicationException("invalid expression!");
		}
	}
	
	public String getExpression(String key, Transaction txn) {
		return null;
	}
	
	public void saveMessage(Message msg, Transaction txn)
			throws DatabaseException, IOException, ApplicationException, Exception{
		DatabaseEntry thekey = null;
		DatabaseEntry thedata = new DatabaseEntry();
		String key=null;
		if(msg.isInput()) {
			key = "i#" + StringUtil.getTimeNow() + "%" + EntityUtil.getUUID();
			
		}else {
			key = "o#" + StringUtil.getTimeNow() + "%" + EntityUtil.getUUID();
		}
		thekey = new DatabaseEntry(key.getBytes("UTF-8"));
		MessageBinding binding = new MessageBinding();
		binding.objectToEntry(msg, thedata);
		messageDB.put(txn, thekey, thedata);
	}
	
	public String historyMessage()
			throws DatabaseException, IOException, ApplicationException, Exception{
		DatabaseEntry thekey = new DatabaseEntry();
		DatabaseEntry thedata = new DatabaseEntry();
		String ret = "";

		Cursor cursor = null;
		try {
			cursor = messageDB.openCursor(null, null);
			OperationStatus retVal = cursor.getLast(thekey, thedata, LockMode.DEFAULT);
			MessageBinding binding = new MessageBinding();
			int i = 10; //max value
			DatabaseEntry tmp = null;
			while(retVal == OperationStatus.SUCCESS) {
				i--;
				if(i == 0) {
					break;
				}
				tmp = thekey;
				retVal = cursor.getPrev(thekey, thedata, LockMode.DEFAULT);
			}
			retVal = cursor.getNext(tmp, thedata, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS) {
				Message msg = binding.entryToObject(thedata);
				String in = "out";
				if(msg.isInput()) {
					in = "in";
				}
				ret = ret + msg.getTime() + " " + in + " " + msg.getContent() + "\r\n";
				retVal = cursor.getNext(thekey, thedata, LockMode.DEFAULT);
			}
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
		
		return ret;
	}
	
	public void clearHistory(Transaction txn)
			throws DatabaseException, IOException, ApplicationException, Exception{
		DatabaseEntry thekey = new DatabaseEntry();
		DatabaseEntry thedata = new DatabaseEntry();
		Cursor cursor = null;
		try {
			cursor = messageDB.openCursor(txn, null);
			OperationStatus retVal = cursor.getFirst(thekey, thedata, LockMode.DEFAULT);
			while(retVal == OperationStatus.SUCCESS) {
				retVal = cursor.getNext(thekey, thedata, LockMode.DEFAULT);
				cursor.delete();
			}
		} catch(DatabaseException e) {
			throw new ApplicationException();
		} finally  {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch(DatabaseException e) {
				throw new ApplicationException();
			}
		}
//		return ret;
	}
}

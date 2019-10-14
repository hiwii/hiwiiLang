package net.hiwii.system.syntax.sent;

import java.util.List;

import net.hiwii.cognition.Expression;

public class BranchSentence extends Expression {
	private List<Expression> judges;
	private List<Expression> programs;
	private Expression elseCase;
	public List<Expression> getJudges() {
		return judges;
	}
	public void setJudges(List<Expression> judges) {
		this.judges = judges;
	}
	public List<Expression> getPrograms() {
		return programs;
	}
	public void setPrograms(List<Expression> programs) {
		this.programs = programs;
	}
	public Expression getElseCase() {
		return elseCase;
	}
	public void setElseCase(Expression elseCase) {
		this.elseCase = elseCase;
	}

}

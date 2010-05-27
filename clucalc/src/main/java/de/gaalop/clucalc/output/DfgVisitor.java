package de.gaalop.clucalc.output;

import de.gaalop.dfg.*;

/**
 * Creates CLUCalc code for a single expression. The code can afterwards be queried via {@link #getCode()}.
 */
public class DfgVisitor implements ExpressionVisitor {

	private StringBuilder code = new StringBuilder();

	public String getCode() {
		return code.toString();
	}

	private void handleInfix(BinaryOperation operation, String operator, boolean withSpaces) {
		addChild(operation, operation.getLeft());
		if (withSpaces) {
			code.append(' ');
			code.append(operator);
			code.append(' ');
		} else {
			code.append(operator);
		}
		addChild(operation, operation.getRight());
	}

	private void addChild(Expression parent, Expression child) {
		if (OperatorPriority.hasLowerPriority(parent, child)) {
			code.append('(');
			child.accept(this);
			code.append(')');
		} else {
			child.accept(this);
		}
	}

	@Override
	public void visit(Subtraction subtraction) {
		handleInfix(subtraction, "-", true);
	}

	@Override
	public void visit(Addition addition) {
		handleInfix(addition, "+", true);
	}

	@Override
	public void visit(Division division) {
		handleInfix(division, "/", true);
	}

	@Override
	public void visit(InnerProduct innerProduct) {
		handleInfix(innerProduct, ".", true);
//		throw new UnsupportedOperationException("Inner product is unsupported for code generation.");
	}

	@Override
	public void visit(Multiplication multiplication) {
		handleInfix(multiplication, "*", true);
	}

	@Override
	public void visit(MathFunctionCall mathFunctionCall) {
		code.append(mathFunctionCall.getFunction().toString());
		code.append('(');
		mathFunctionCall.getOperand().accept(this);
		code.append(')');
	}

	@Override
	public void visit(Variable variable) {
		code.append(variable.getName());
	}

	@Override
	public void visit(MultivectorComponent component) {
		code.append(component.getName());
		code.append('(');
		code.append(component.getBladeIndex() + 1);
		code.append(')');
	}

	@Override
	public void visit(Exponentiation exponentiation) {
		handleInfix(exponentiation, "^^", false);
	}

	@Override
	public void visit(FloatConstant floatConstant) {
		code.append(Float.toString(floatConstant.getValue()).replace('E', 'e'));
	}

	@Override
	public void visit(OuterProduct outerProduct) {
		handleInfix(outerProduct, "^", false);
	}

	@Override
	public void visit(BaseVector baseVector) {
		// TODO Correctly handle the underlying algebra mode here
		code.append("e");
		switch (baseVector.getOrder()) {
		case 1:
		case 2:
		case 3:
			code.append(baseVector.getIndex());
			break;
		case 4:
			if (baseVector.getIndex().equals("inf")) {
				code.append("inf");
			} else {
				code.append('p');
			}
			break;
		case 5:
			if (baseVector.getIndex().equals("0")) {
				code.append(0);
			} else {
				code.append('m');
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid base vector index: " + baseVector.getIndex());
		}
	}

	@Override
	public void visit(Negation negation) {
		code.append('-');
		addChild(negation, negation.getOperand());
	}

	@Override
	public void visit(Reverse node) {
		code.append('~');
		addChild(node, node.getOperand());
	}

	@Override
	public void visit(LogicalOr node) {
		handleInfix(node, "||", true);
	}

	@Override
	public void visit(LogicalAnd node) {
		handleInfix(node, "&&", true);
	}

	@Override
	public void visit(Equality node) {
		handleInfix(node, "==", true);
	}

	@Override
	public void visit(Inequality node) {
		handleInfix(node, "!=", true);
	}

	@Override
	public void visit(Relation relation) {
		handleInfix(relation, relation.getTypeString(), false);
	}

	@Override
	public void visit(FunctionArgument node) {		
		throw new IllegalStateException("Macros should have been inlined and are not allowed for output.");
	}

	@Override
	public void visit(MacroCall node) {
		// TODO Auto-generated method stub
		
	}

}

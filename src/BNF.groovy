trait Token { def content }
class Digit          implements Token {}
class DigitList      implements Token {}
class Fraction       implements Token {}
class Negative       implements Token {}
class FractionNeg    implements Token {}
class Symbol         implements Token {}
class SymbolList     implements Token {}
class Lambda         implements Token {}
class LambdaArgs     implements Token {}
class Method         implements Token {}
class ObjMethod      implements Token {}
class ObjCleanMethod implements Token {}
class ExprEnd        implements Token {}
class Expr           implements Token {}
class ExprList       implements Token {}

class ExprFactory {
	def setProperty(String property, value) {
		[
			DIGIT : Digit,
			DIGIT_L : DigitList,
			FRACTION : Fraction,
			NEGATIVE : Negative,
			FRACTION_NEG : FractionNeg,
			SYMBOL : Symbol,
			SYMBOL_L : SymbolList,
			LAMBDA : { },
			LAMBDA_ARGS : { },
			METHOD : { },
			OBJ_METHOD : { },
			OBJ_CLEAN_METHOD : { },
			EXPR_END : { },
			EXPR : { },
			EXPR_LIST : { }
		][ property ]
	}
}

class StringExprCategory {
	static Expr plus(String s1, wat) {
		assert false
	}
}

class Granaur {
	def declare(Closure c) {
		return this
	}
	
	def parse() {
	}
}

g = new Granaur()

g.declare {
	DIGIT = /\d/
	DIGIT_L = DIGIT + "+"
	FRACTION = DIGIT_L + "." + DIGIT_L
	NEGATIVE = "-" + DIGIT_L
	FRACTION_NEG = "-" + FRACTION
	
	SYMBOL = /\w/
	SYMBOL_L = SYMBOL + "+"
	
	LAMBDA = "{" + EXPR_LIST + "}"
	LAMBDA_ARGS = "{" + ARGS + "->" + EXPR_LIST + "}"
	METHOD = SYMBOL_L + "(" + (METHOD | SYMBOL_L | DIGIT_LIST) + ")"
	OBJ_METHOD = SYMBOL_L + " " + METHOD
	OBJ_CLEAN_METHOD = SYMBOL_L + " " + SYMBOL_L + " " + (SYMBOL_L | DIGIT_L)
	
	EXPR_END = ( ";" | System.lineSeparator() )
	
	EXPR = OBJ_CLEAN_METHOD | OBJ_METHOD | METHOD
	EXPR_LIST = EXPR + "+" 
}

g.with {
	def e = { what -> parse what }
	
	assert e('3.14') == [ NEGATIVE(3.14) ]
	assert e('wat') == [ SYMBOL('wat') ]
	assert e('"captain"') == [ SYMBOL('"captain"') ]
	assert e('you wot(m8)') == [ OBJ_METHOD( SYMBOL_L('you'), METHOD() ) ]
	assert e('a is 90') == [ OBJ_CLEAN_METHOD( SYMBOL_L('a'), SYMBOL_L('is'), DIGIT_L(90) ) ]
	assert e('{ wot }') == [ LAMBDA( SYMBOL_L('wot') ) ]
	assert e('{ a -> }') == [ LAMBDA_ARGS( ARGS('a'), EXPR_LIST() ) ]
}
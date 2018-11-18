// This is an autogenerated file. Do not edit. Run 'npm run gen-models' to generate.
// Run 'BALLERINA_HOME="your/ballerina/home" npm run gen-ast-interfaces' to generate.
import * as Ballerina from "./ast-interfaces";

export interface Visitor {
  beginVisitASTNode?(node: Ballerina.ASTNode): void;
  endVisitASTNode?(node: Ballerina.ASTNode): void;

  beginVisitAbort?(node: Ballerina.Abort): void;
  endVisitAbort?(node: Ballerina.Abort): void;

  beginVisitAnnotation?(node: Ballerina.Annotation): void;
  endVisitAnnotation?(node: Ballerina.Annotation): void;

  beginVisitAnnotationAttachment?(node: Ballerina.AnnotationAttachment): void;
  endVisitAnnotationAttachment?(node: Ballerina.AnnotationAttachment): void;

  beginVisitArrayLiteralExpr?(node: Ballerina.ArrayLiteralExpr): void;
  endVisitArrayLiteralExpr?(node: Ballerina.ArrayLiteralExpr): void;

  beginVisitArrayType?(node: Ballerina.ArrayType): void;
  endVisitArrayType?(node: Ballerina.ArrayType): void;

  beginVisitArrowExpr?(node: Ballerina.ArrowExpr): void;
  endVisitArrowExpr?(node: Ballerina.ArrowExpr): void;

  beginVisitAssignment?(node: Ballerina.Assignment): void;
  endVisitAssignment?(node: Ballerina.Assignment): void;

  beginVisitAwaitExpr?(node: Ballerina.AwaitExpr): void;
  endVisitAwaitExpr?(node: Ballerina.AwaitExpr): void;

  beginVisitBinaryExpr?(node: Ballerina.BinaryExpr): void;
  endVisitBinaryExpr?(node: Ballerina.BinaryExpr): void;

  beginVisitBlock?(node: Ballerina.Block): void;
  endVisitBlock?(node: Ballerina.Block): void;

  beginVisitBracedTupleExpr?(node: Ballerina.BracedTupleExpr): void;
  endVisitBracedTupleExpr?(node: Ballerina.BracedTupleExpr): void;

  beginVisitBreak?(node: Ballerina.Break): void;
  endVisitBreak?(node: Ballerina.Break): void;

  beginVisitBuiltInRefType?(node: Ballerina.BuiltInRefType): void;
  endVisitBuiltInRefType?(node: Ballerina.BuiltInRefType): void;

  beginVisitCatch?(node: Ballerina.Catch): void;
  endVisitCatch?(node: Ballerina.Catch): void;

  beginVisitCheckExpr?(node: Ballerina.CheckExpr): void;
  endVisitCheckExpr?(node: Ballerina.CheckExpr): void;

  beginVisitCompensate?(node: Ballerina.Compensate): void;
  endVisitCompensate?(node: Ballerina.Compensate): void;

  beginVisitCompilationUnit?(node: Ballerina.CompilationUnit): void;
  endVisitCompilationUnit?(node: Ballerina.CompilationUnit): void;

  beginVisitCompoundAssignment?(node: Ballerina.CompoundAssignment): void;
  endVisitCompoundAssignment?(node: Ballerina.CompoundAssignment): void;

  beginVisitConstrainedType?(node: Ballerina.ConstrainedType): void;
  endVisitConstrainedType?(node: Ballerina.ConstrainedType): void;

  beginVisitDeprecated?(node: Ballerina.Deprecated): void;
  endVisitDeprecated?(node: Ballerina.Deprecated): void;

  beginVisitDocumentationDescription?(
    node: Ballerina.DocumentationDescription
  ): void;
  endVisitDocumentationDescription?(
    node: Ballerina.DocumentationDescription
  ): void;

  beginVisitDocumentationParameter?(
    node: Ballerina.DocumentationParameter
  ): void;
  endVisitDocumentationParameter?(node: Ballerina.DocumentationParameter): void;

  beginVisitDone?(node: Ballerina.Done): void;
  endVisitDone?(node: Ballerina.Done): void;

  beginVisitElvisExpr?(node: Ballerina.ElvisExpr): void;
  endVisitElvisExpr?(node: Ballerina.ElvisExpr): void;

  beginVisitEndpoint?(node: Ballerina.Endpoint): void;
  endVisitEndpoint?(node: Ballerina.Endpoint): void;

  beginVisitExpressionStatement?(node: Ballerina.ExpressionStatement): void;
  endVisitExpressionStatement?(node: Ballerina.ExpressionStatement): void;

  beginVisitFieldBasedAccessExpr?(node: Ballerina.FieldBasedAccessExpr): void;
  endVisitFieldBasedAccessExpr?(node: Ballerina.FieldBasedAccessExpr): void;

  beginVisitFiniteTypeNode?(node: Ballerina.FiniteTypeNode): void;
  endVisitFiniteTypeNode?(node: Ballerina.FiniteTypeNode): void;

  beginVisitForeach?(node: Ballerina.Foreach): void;
  endVisitForeach?(node: Ballerina.Foreach): void;

  beginVisitForever?(node: Ballerina.Forever): void;
  endVisitForever?(node: Ballerina.Forever): void;

  beginVisitForkJoin?(node: Ballerina.ForkJoin): void;
  endVisitForkJoin?(node: Ballerina.ForkJoin): void;

  beginVisitFunction?(node: Ballerina.Function): void;
  endVisitFunction?(node: Ballerina.Function): void;

  beginVisitFunctionType?(node: Ballerina.FunctionType): void;
  endVisitFunctionType?(node: Ballerina.FunctionType): void;

  beginVisitGroupBy?(node: Ballerina.GroupBy): void;
  endVisitGroupBy?(node: Ballerina.GroupBy): void;

  beginVisitHaving?(node: Ballerina.Having): void;
  endVisitHaving?(node: Ballerina.Having): void;

  beginVisitIdentifier?(node: Ballerina.Identifier): void;
  endVisitIdentifier?(node: Ballerina.Identifier): void;

  beginVisitIf?(node: Ballerina.If): void;
  endVisitIf?(node: Ballerina.If): void;

  beginVisitImport?(node: Ballerina.Import): void;
  endVisitImport?(node: Ballerina.Import): void;

  beginVisitIndexBasedAccessExpr?(node: Ballerina.IndexBasedAccessExpr): void;
  endVisitIndexBasedAccessExpr?(node: Ballerina.IndexBasedAccessExpr): void;

  beginVisitIntRangeExpr?(node: Ballerina.IntRangeExpr): void;
  endVisitIntRangeExpr?(node: Ballerina.IntRangeExpr): void;

  beginVisitInvocation?(node: Ballerina.Invocation): void;
  endVisitInvocation?(node: Ballerina.Invocation): void;

  beginVisitJoinStreamingInput?(node: Ballerina.JoinStreamingInput): void;
  endVisitJoinStreamingInput?(node: Ballerina.JoinStreamingInput): void;

  beginVisitLambda?(node: Ballerina.Lambda): void;
  endVisitLambda?(node: Ballerina.Lambda): void;

  beginVisitLimit?(node: Ballerina.Limit): void;
  endVisitLimit?(node: Ballerina.Limit): void;

  beginVisitLiteral?(node: Ballerina.Literal): void;
  endVisitLiteral?(node: Ballerina.Literal): void;

  beginVisitLock?(node: Ballerina.Lock): void;
  endVisitLock?(node: Ballerina.Lock): void;

  beginVisitMarkdownDocumentation?(node: Ballerina.MarkdownDocumentation): void;
  endVisitMarkdownDocumentation?(node: Ballerina.MarkdownDocumentation): void;

  beginVisitMatch?(node: Ballerina.Match): void;
  endVisitMatch?(node: Ballerina.Match): void;

  beginVisitMatchExpression?(node: Ballerina.MatchExpression): void;
  endVisitMatchExpression?(node: Ballerina.MatchExpression): void;

  beginVisitMatchExpressionPatternClause?(
    node: Ballerina.MatchExpressionPatternClause
  ): void;
  endVisitMatchExpressionPatternClause?(
    node: Ballerina.MatchExpressionPatternClause
  ): void;

  beginVisitMatchPatternClause?(node: Ballerina.MatchPatternClause): void;
  endVisitMatchPatternClause?(node: Ballerina.MatchPatternClause): void;

  beginVisitNamedArgsExpr?(node: Ballerina.NamedArgsExpr): void;
  endVisitNamedArgsExpr?(node: Ballerina.NamedArgsExpr): void;

  beginVisitNext?(node: Ballerina.Next): void;
  endVisitNext?(node: Ballerina.Next): void;

  beginVisitObjectType?(node: Ballerina.ObjectType): void;
  endVisitObjectType?(node: Ballerina.ObjectType): void;

  beginVisitOrderBy?(node: Ballerina.OrderBy): void;
  endVisitOrderBy?(node: Ballerina.OrderBy): void;

  beginVisitOrderByVariable?(node: Ballerina.OrderByVariable): void;
  endVisitOrderByVariable?(node: Ballerina.OrderByVariable): void;

  beginVisitOutputRateLimit?(node: Ballerina.OutputRateLimit): void;
  endVisitOutputRateLimit?(node: Ballerina.OutputRateLimit): void;

  beginVisitPatternClause?(node: Ballerina.PatternClause): void;
  endVisitPatternClause?(node: Ballerina.PatternClause): void;

  beginVisitPatternStreamingEdgeInput?(
    node: Ballerina.PatternStreamingEdgeInput
  ): void;
  endVisitPatternStreamingEdgeInput?(
    node: Ballerina.PatternStreamingEdgeInput
  ): void;

  beginVisitPatternStreamingInput?(node: Ballerina.PatternStreamingInput): void;
  endVisitPatternStreamingInput?(node: Ballerina.PatternStreamingInput): void;

  beginVisitRecordLiteralExpr?(node: Ballerina.RecordLiteralExpr): void;
  endVisitRecordLiteralExpr?(node: Ballerina.RecordLiteralExpr): void;

  beginVisitRecordLiteralKeyValue?(node: Ballerina.RecordLiteralKeyValue): void;
  endVisitRecordLiteralKeyValue?(node: Ballerina.RecordLiteralKeyValue): void;

  beginVisitRecordType?(node: Ballerina.RecordType): void;
  endVisitRecordType?(node: Ballerina.RecordType): void;

  beginVisitResource?(node: Ballerina.Resource): void;
  endVisitResource?(node: Ballerina.Resource): void;

  beginVisitRestArgsExpr?(node: Ballerina.RestArgsExpr): void;
  endVisitRestArgsExpr?(node: Ballerina.RestArgsExpr): void;

  beginVisitRetry?(node: Ballerina.Retry): void;
  endVisitRetry?(node: Ballerina.Retry): void;

  beginVisitReturn?(node: Ballerina.Return): void;
  endVisitReturn?(node: Ballerina.Return): void;

  beginVisitScope?(node: Ballerina.Scope): void;
  endVisitScope?(node: Ballerina.Scope): void;

  beginVisitSelectClause?(node: Ballerina.SelectClause): void;
  endVisitSelectClause?(node: Ballerina.SelectClause): void;

  beginVisitSelectExpression?(node: Ballerina.SelectExpression): void;
  endVisitSelectExpression?(node: Ballerina.SelectExpression): void;

  beginVisitService?(node: Ballerina.Service): void;
  endVisitService?(node: Ballerina.Service): void;

  beginVisitSimpleVariableRef?(node: Ballerina.SimpleVariableRef): void;
  endVisitSimpleVariableRef?(node: Ballerina.SimpleVariableRef): void;

  beginVisitStreamAction?(node: Ballerina.StreamAction): void;
  endVisitStreamAction?(node: Ballerina.StreamAction): void;

  beginVisitStreamingInput?(node: Ballerina.StreamingInput): void;
  endVisitStreamingInput?(node: Ballerina.StreamingInput): void;

  beginVisitStreamingQuery?(node: Ballerina.StreamingQuery): void;
  endVisitStreamingQuery?(node: Ballerina.StreamingQuery): void;

  beginVisitStringTemplateLiteral?(node: Ballerina.StringTemplateLiteral): void;
  endVisitStringTemplateLiteral?(node: Ballerina.StringTemplateLiteral): void;

  beginVisitTable?(node: Ballerina.Table): void;
  endVisitTable?(node: Ballerina.Table): void;

  beginVisitTableColumn?(node: Ballerina.TableColumn): void;
  endVisitTableColumn?(node: Ballerina.TableColumn): void;

  beginVisitTableQuery?(node: Ballerina.TableQuery): void;
  endVisitTableQuery?(node: Ballerina.TableQuery): void;

  beginVisitTableQueryExpression?(node: Ballerina.TableQueryExpression): void;
  endVisitTableQueryExpression?(node: Ballerina.TableQueryExpression): void;

  beginVisitTernaryExpr?(node: Ballerina.TernaryExpr): void;
  endVisitTernaryExpr?(node: Ballerina.TernaryExpr): void;

  beginVisitThrow?(node: Ballerina.Throw): void;
  endVisitThrow?(node: Ballerina.Throw): void;

  beginVisitTransaction?(node: Ballerina.Transaction): void;
  endVisitTransaction?(node: Ballerina.Transaction): void;

  beginVisitTry?(node: Ballerina.Try): void;
  endVisitTry?(node: Ballerina.Try): void;

  beginVisitTupleDestructure?(node: Ballerina.TupleDestructure): void;
  endVisitTupleDestructure?(node: Ballerina.TupleDestructure): void;

  beginVisitTupleTypeNode?(node: Ballerina.TupleTypeNode): void;
  endVisitTupleTypeNode?(node: Ballerina.TupleTypeNode): void;

  beginVisitTypeConversionExpr?(node: Ballerina.TypeConversionExpr): void;
  endVisitTypeConversionExpr?(node: Ballerina.TypeConversionExpr): void;

  beginVisitTypeDefinition?(node: Ballerina.TypeDefinition): void;
  endVisitTypeDefinition?(node: Ballerina.TypeDefinition): void;

  beginVisitTypeInitExpr?(node: Ballerina.TypeInitExpr): void;
  endVisitTypeInitExpr?(node: Ballerina.TypeInitExpr): void;

  beginVisitTypedescExpression?(node: Ballerina.TypedescExpression): void;
  endVisitTypedescExpression?(node: Ballerina.TypedescExpression): void;

  beginVisitUnaryExpr?(node: Ballerina.UnaryExpr): void;
  endVisitUnaryExpr?(node: Ballerina.UnaryExpr): void;

  beginVisitUnionTypeNode?(node: Ballerina.UnionTypeNode): void;
  endVisitUnionTypeNode?(node: Ballerina.UnionTypeNode): void;

  beginVisitUserDefinedType?(node: Ballerina.UserDefinedType): void;
  endVisitUserDefinedType?(node: Ballerina.UserDefinedType): void;

  beginVisitValueType?(node: Ballerina.ValueType): void;
  endVisitValueType?(node: Ballerina.ValueType): void;

  beginVisitVariable?(node: Ballerina.Variable): void;
  endVisitVariable?(node: Ballerina.Variable): void;

  beginVisitVariableDef?(node: Ballerina.VariableDef): void;
  endVisitVariableDef?(node: Ballerina.VariableDef): void;

  beginVisitWhere?(node: Ballerina.Where): void;
  endVisitWhere?(node: Ballerina.Where): void;

  beginVisitWhile?(node: Ballerina.While): void;
  endVisitWhile?(node: Ballerina.While): void;

  beginVisitWindowClause?(node: Ballerina.WindowClause): void;
  endVisitWindowClause?(node: Ballerina.WindowClause): void;

  beginVisitWithin?(node: Ballerina.Within): void;
  endVisitWithin?(node: Ballerina.Within): void;

  beginVisitWorker?(node: Ballerina.Worker): void;
  endVisitWorker?(node: Ballerina.Worker): void;

  beginVisitWorkerReceive?(node: Ballerina.WorkerReceive): void;
  endVisitWorkerReceive?(node: Ballerina.WorkerReceive): void;

  beginVisitWorkerSend?(node: Ballerina.WorkerSend): void;
  endVisitWorkerSend?(node: Ballerina.WorkerSend): void;

  beginVisitXmlAttribute?(node: Ballerina.XmlAttribute): void;
  endVisitXmlAttribute?(node: Ballerina.XmlAttribute): void;

  beginVisitXmlAttributeAccessExpr?(
    node: Ballerina.XmlAttributeAccessExpr
  ): void;
  endVisitXmlAttributeAccessExpr?(node: Ballerina.XmlAttributeAccessExpr): void;

  beginVisitXmlCommentLiteral?(node: Ballerina.XmlCommentLiteral): void;
  endVisitXmlCommentLiteral?(node: Ballerina.XmlCommentLiteral): void;

  beginVisitXmlElementLiteral?(node: Ballerina.XmlElementLiteral): void;
  endVisitXmlElementLiteral?(node: Ballerina.XmlElementLiteral): void;

  beginVisitXmlPiLiteral?(node: Ballerina.XmlPiLiteral): void;
  endVisitXmlPiLiteral?(node: Ballerina.XmlPiLiteral): void;

  beginVisitXmlQname?(node: Ballerina.XmlQname): void;
  endVisitXmlQname?(node: Ballerina.XmlQname): void;

  beginVisitXmlQuotedString?(node: Ballerina.XmlQuotedString): void;
  endVisitXmlQuotedString?(node: Ballerina.XmlQuotedString): void;

  beginVisitXmlTextLiteral?(node: Ballerina.XmlTextLiteral): void;
  endVisitXmlTextLiteral?(node: Ballerina.XmlTextLiteral): void;

  beginVisitXmlns?(node: Ballerina.Xmlns): void;
  endVisitXmlns?(node: Ballerina.Xmlns): void;
}

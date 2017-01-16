/**
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
define(['lodash', 'log', 'event_channel', '../ast/module', './try-catch-statement-view', './try-statement-view',
        './catch-statement-view', './if-else-statement-view', './if-statement-view', './else-statement-view',
        './else-if-statement-view', './assignment-view', './function-invocation-view',
        './action-invocation-statement-view', './while-statement-view', './reply-statement-view',
        './logical-expression-view', './arithmetic-expression-view', './return-statement-view'],
    function (_, log, EventChannel, AST, TryCatchStatementView, TryStatementView, CatchStatementView,
              IfElseStatementView, IfStatementView, ElseStatementView, ElseIfStatementView, AssignmentStatementView,
              FunctionInvocationStatementView, ActionInvocationStatementView, WhileStatementView, ReplyStatementView,
              LogicalExpressionView, ArithmeticExpressionView, ReturnStatement) {

        var StatementViewFactory = function () {
        };

        StatementViewFactory.prototype.getStatementView = function (args) {
            var statement  = _.get(args, "model");
            if (statement instanceof AST.TryCatchStatement) {
                return new TryCatchStatementView(args);
            } else if (statement instanceof AST.TryStatement) {
                return new TryStatementView(args);
            } else if (statement instanceof AST.CatchStatement) {
                return new CatchStatementView(args);
            } else if (statement instanceof AST.IfElseStatement) {
                return new IfElseStatementView(args);
            } else if (statement instanceof AST.IfStatement) {
                return new IfStatementView(args);
            } else if (statement instanceof AST.ElseStatement) {
                return new ElseStatementView(args);
            }  else if (statement instanceof AST.ElseIfStatement) {
                return new ElseIfStatementView(args);
            } else if (statement instanceof AST.Assignment) {
                return new AssignmentStatementView(args);
            } else if (statement instanceof AST.FunctionInvocation) {
                return new FunctionInvocationStatementView(args);
            } else if (statement instanceof AST.WhileStatement) {
                return new WhileStatementView(args);
            } else if (statement instanceof AST.ActionInvocationStatement) {
                return new ActionInvocationStatementView(args);
            } else if (statement instanceof AST.ReplyStatement) {
                return new ReplyStatementView(args);
            } else if (statement instanceof AST.LogicalExpression) {
                return new LogicalExpressionView(args);
            } else if (statement instanceof AST.ArithmeticExpression) {
                return new ArithmeticExpressionView(args);
            } else if (statement instanceof AST.ReturnStatement) {
                return new ReturnStatement(args);
            } else if (statement instanceof AST.AssignmentStatement){
                // TODO : This logic needs to be refactored.
                var children  = _.get(statement, "children");
                var assignmenStatement = undefined;
                _.each(children, function (statementChild) {
                    if(AST.BallerinaASTFactory.isRightOperandExpression(statementChild)) {
                        var operands  = _.get(statementChild, "children");
                        _.each(operands, function (child) {
                            if (AST.BallerinaASTFactory.isActionInvocationExpression(child)) {
                                _.set(args, 'model', child);
                                assignmenStatement = new ActionInvocationStatementView(args);
                            } else if (AST.BallerinaASTFactory.isAssignment(child)) {
                                _.set(args, 'model', child);
                                assignmenStatement = new AssignmentStatementView(args);
                            } else if (AST.BallerinaASTFactory.isVariableReferenceExpression(child)) {
                                _.set(args, 'model', child);
                                assignmenStatement = new AssignmentStatementView(args);
                            } else if (AST.BallerinaASTFactory.isInstanceCreationExpression(child)){
                                _.set(args, 'model', child);
                                assignmenStatement = new AssignmentStatementView(args);
                            } else if(AST.BallerinaASTFactory.isBasicLiteralExpression(child)){
                                _.set(args, 'model', child);
                                assignmenStatement = new AssignmentStatementView(args);
                            } else if (AST.BallerinaASTFactory.isFunctionInvocationExpression(child)){
                                _.set(args, 'model', child);
                                assignmenStatement = new AssignmentStatementView(args);
                            }
                        });
                    }
                });
                if (_.isUndefined(assignmenStatement)) {
                    _.set(args, 'model', AST.BallerinaASTFactory.createAssignment());
                    _.get(args, 'model').setExpression(children[0].getVariableReferenceName() + " = " + children[1].getRightOperandExpressionString());
                    assignmenStatement = new AssignmentStatementView(args);
                }
                return assignmenStatement;
            }
        };

        return StatementViewFactory;
    });
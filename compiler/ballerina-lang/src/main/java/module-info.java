module io.ballerina.lang {
    uses io.ballerina.projects.plugins.CompilerPlugin;
    requires java.compiler;
    requires toml4j;
    requires gson;
    requires java.xml;
    requires org.objectweb.asm;
    requires io.ballerina.runtime;
    requires io.netty.buffer;
    requires io.ballerina.parser;
    requires io.ballerina.tools.api;
    requires org.apache.commons.compress;
    requires org.apache.commons.io;
    requires io.ballerina.toml;
    requires io.ballerina.central.client;
    requires java.semver;
    exports io.ballerina.compiler.api;
    exports io.ballerina.compiler.api.symbols;
    exports io.ballerina.compiler.api.symbols.resourcepath;
    exports io.ballerina.compiler.api.symbols.resourcepath.util;
    exports org.wso2.ballerinalang.compiler.util;
    exports org.ballerinalang.toml.model;
    exports org.wso2.ballerinalang.util;
    exports org.ballerinalang.model.types;
    exports org.wso2.ballerinalang.compiler.tree;
    exports org.wso2.ballerinalang.compiler.tree.types;
    exports org.ballerinalang.compiler;
    exports org.ballerinalang.compiler.plugins;
    exports org.ballerinalang.model.tree;
    exports org.ballerinalang.model.elements;
    exports org.ballerinalang.util.diagnostic;
    exports org.wso2.ballerinalang.compiler.semantics.model.types;
    exports org.wso2.ballerinalang.compiler.semantics.model.symbols;
    exports org.wso2.ballerinalang.compiler.tree.expressions;
    exports org.ballerinalang.model.tree.expressions;
    exports org.wso2.ballerinalang.compiler.util.diagnotic;
    exports org.ballerinalang.natives.annotations;
    exports org.wso2.ballerinalang.compiler.semantics.analyzer;
    exports org.wso2.ballerinalang.compiler.semantics.model;
    exports org.ballerinalang.model;
    exports org.wso2.ballerinalang.compiler.desugar;
    exports org.ballerinalang.model.tree.statements;
    exports org.wso2.ballerinalang.compiler.tree.statements;
    exports org.ballerinalang.annotation;
    exports org.ballerinalang.codegen;
    exports org.ballerinalang.spi;
    exports org.wso2.ballerinalang.compiler;
    exports org.wso2.ballerinalang.programfile;
    exports org.ballerinalang.repository;
    exports org.wso2.ballerinalang.compiler.bir;
    exports org.wso2.ballerinalang.compiler.bir.model;
    exports org.ballerinalang.toml.exceptions;
    exports org.ballerinalang.model.tree.types;
    exports org.wso2.ballerinalang.compiler.parser;
    exports org.ballerinalang.model.symbols;
    exports org.wso2.ballerinalang.compiler.spi;
    exports org.ballerinalang.util;
    exports org.wso2.ballerinalang.compiler.tree.clauses;
    exports org.ballerinalang.model.clauses;
    exports org.wso2.ballerinalang.compiler.diagnostic;
    exports org.wso2.ballerinalang.compiler.tree.bindingpatterns;
    exports org.wso2.ballerinalang.compiler.tree.matchpatterns;
    exports io.ballerina.projects;
    exports io.ballerina.projects.environment;
    exports io.ballerina.projects.util;
    exports io.ballerina.projects.directory;
    exports io.ballerina.projects.bala;
    exports io.ballerina.projects.repos;
    exports io.ballerina.projects.plugins;
    exports io.ballerina.projects.internal.model; // TODO Remove this exports
    exports io.ballerina.projects.internal.environment;
    exports io.ballerina.projects.internal; // TODO Remove these exports
}

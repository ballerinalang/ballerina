/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.packerina.task;

import org.ballerinalang.compiler.BLangCompilerException;
import org.ballerinalang.model.elements.PackageID;
import org.ballerinalang.packerina.BuilderUtils;
import org.ballerinalang.packerina.buildcontext.BuildContext;
import org.ballerinalang.packerina.buildcontext.BuildContextField;
import org.ballerinalang.packerina.buildcontext.sourcecontext.MultiModuleContext;
import org.ballerinalang.packerina.buildcontext.sourcecontext.SingleFileContext;
import org.ballerinalang.packerina.buildcontext.sourcecontext.SingleModuleContext;
import org.wso2.ballerinalang.compiler.semantics.model.symbols.BPackageSymbol;
import org.wso2.ballerinalang.compiler.tree.BLangPackage;
import org.wso2.ballerinalang.compiler.util.ProjectDirConstants;
import org.wso2.ballerinalang.compiler.util.ProjectDirs;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

import static org.wso2.ballerinalang.compiler.util.ProjectDirConstants.BLANG_COMPILED_JAR_EXT;

/**
 * Task for creating the executable jar file.
 */
public class CreateExecutableTask implements Task {
    @Override
    public void execute(BuildContext buildContext) {
        Path targetDir = buildContext.get(BuildContextField.TARGET_DIR);
        try {
            // create '<target>/cache/jar_cache' dir
            Path executableDir = targetDir.resolve(ProjectDirConstants.BIN_DIR_NAME);
        
            if (!Files.exists(executableDir)) {
                Files.createDirectories(executableDir);
            }
        
            // add jar_cache to build context
            buildContext.put(BuildContextField.EXECUTABLE_DIR, executableDir);
        
            OUT.println("Generating executables");
            switch (buildContext.getSourceType()) {
                case BAL_FILE:
                    SingleFileContext singleFileContext = buildContext.get(BuildContextField.SOURCE_CONTEXT);
                    BLangPackage balFileModule = singleFileContext.getModule();
                    if (balFileModule.symbol.entryPointExists) {
                        assembleExecutable(buildContext, balFileModule);
                    }
                    break;
                case SINGLE_MODULE:
                    SingleModuleContext moduleContext = buildContext.get(BuildContextField.SOURCE_CONTEXT);
                    BLangPackage module = moduleContext.getModule();
                    if (module.symbol.entryPointExists) {
                        assembleExecutable(buildContext, module);
                    }
                    break;
                case ALL_MODULES:
                    MultiModuleContext multiModuleContext = buildContext.get(BuildContextField.SOURCE_CONTEXT);
                    multiModuleContext.getModules().stream()
                            .filter(m -> m.symbol.entryPointExists)
                            .forEach(m -> assembleExecutable(buildContext, m));
                    break;
                default:
                    throw new BLangCompilerException("unable to run compiler plugins for build source");
            }
        } catch (IOException e) {
            throw new BLangCompilerException("error occurred creating directory for executables: " + targetDir);
        }
    }
    
    private static void assembleExecutable(BuildContext buildContext, BLangPackage bLangPackage) {
        Path sourceRoot = buildContext.get(BuildContextField.SOURCE_ROOT);
        try {
            String moduleJarName = bLangPackage.packageID.name.value
                                   + BLANG_COMPILED_JAR_EXT;
            // Copy the jar from cache to bin directory
            Path uberJar = BuilderUtils.resolveExecutablePath(buildContext, bLangPackage.packageID);
            Path moduleJar = BuilderUtils.resolveJarPath(buildContext, bLangPackage.packageID)
                    .resolve(bLangPackage.packageID.orgName.value)
                    .resolve(bLangPackage.packageID.name.value)
                    .resolve(bLangPackage.packageID.version.value)
                    .resolve(moduleJarName);
            
            // Check if the package has an entry point.
            if (bLangPackage.symbol.entryPointExists) {
                Files.copy(moduleJar, uberJar, StandardCopyOption.REPLACE_EXISTING);
                // Get the fs handle to the jar file
                
                // Iterate through the imports and copy dependencies.
                for (BPackageSymbol importz : bLangPackage.symbol.imports) {
                    Path importJar = findImportJarPath(buildContext, importz, sourceRoot);
                    
                    if (importJar != null && Files.exists(importJar)) {
                        copyFromJarToJar(importJar, uberJar);
                    }
                }
            }
            // Copy dependency jar
            // Copy dependency libraries
            // Executable is created at give location.
            OUT.println(sourceRoot.relativize(uberJar).toString());
            // If no entry point is found we do nothing.
        } catch (IOException e) {
            throw new BLangCompilerException("Unable to create the executable :" + e.getMessage());
        }
    }
    
    private static Path findImportJarPath(BuildContext buildContext, BPackageSymbol importz, Path project) {
        // Get the jar paths
        PackageID id = importz.pkgID;
        
        // Skip ballerina and ballerinax
        if (id.orgName.value.equals("ballerina") || id.orgName.value.equals("ballerinax")) {
            return null;
        }
        // Look if it is a project module.
        if (ProjectDirs.isModuleExist(project, id.name.value)) {
            // If so fetch from project jar cache
            return BuilderUtils.resolveJarPath(buildContext, id);
        } else {
            // If not fetch from home jar cache.
            return BuilderUtils.resolveJarPath(buildContext,
                    buildContext.get(BuildContextField.HOME_JAR_CACHE_REPO), id);
        }
        // return the path
    }
    
    
    private static void copyFromJarToJar(Path fromJar, Path toJar) throws IOException {
        URI uberJarUri = URI.create("jar:" + toJar.toUri().toString());
        // Load the to jar to a file system
        try (FileSystem toFs = FileSystems.newFileSystem(uberJarUri, Collections.emptyMap())) {
            Path to = toFs.getRootDirectories().iterator().next();
            URI moduleJarUri = URI.create("jar:" + fromJar.toUri().toString());
            // Load the from jar to a file system.
            try (FileSystem fromFs = FileSystems.newFileSystem(moduleJarUri, Collections.emptyMap())) {
                Path from = fromFs.getRootDirectories().iterator().next();
                // Walk and copy the files.
                Files.walkFileTree(from, new Copy(from, to));
            }
        }
    }
    
    static class Copy extends SimpleFileVisitor<Path> {
        private Path fromPath;
        private Path toPath;
        private StandardCopyOption copyOption;
        
        
        public Copy(Path fromPath, Path toPath, StandardCopyOption copyOption) {
            this.fromPath = fromPath;
            this.toPath = toPath;
            this.copyOption = copyOption;
        }
        
        public Copy(Path fromPath, Path toPath) {
            this(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException {
            
            Path targetPath = toPath.resolve(fromPath.relativize(dir).toString());
            if (!Files.exists(targetPath)) {
                Files.createDirectory(targetPath);
            }
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException {
            Path toFile = toPath.resolve(fromPath.relativize(file).toString());
            if (!Files.exists(toFile)) {
                Files.copy(file, toFile, copyOption);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
